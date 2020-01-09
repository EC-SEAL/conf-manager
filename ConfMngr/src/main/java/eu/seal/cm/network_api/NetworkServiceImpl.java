/**
Copyright © 2019  Atos Spain SA, UAegean. All rights reserved.
This file is part of SEAL Configuration Manager (SEAL ConfMngr).
SEAL ConfMngr is free software: you can redistribute it and/or modify it under the terms of EUPL 1.2.
THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT ANY WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT, 
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
DAMAGES OR OTHER LIABILITY, WHETHER IN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
See README file for the full disclaimer information and LICENSE file for full license information in the project root.
*/
package eu.seal.cm.network_api;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.seal.cm.httpSig_api.HttpSignatureService;
import eu.seal.cm.httpSig_api.HttpSignatureServiceImpl;
import eu.seal.cm.params_api.KeyStoreService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author UAegean, Atos
 * 
 * ONLY USED FOR SIGNING ewp/seal-metadata!!
 * 
 * IMPORTANT:
 * The keys stored in the ewp catalogue are NOT in PKCS#1 RSAPublicKey
 * They are like the keys stored in the *.json (CM files) were in X.509 SubjectPublicKeyInfo
 */
@Service
public class NetworkServiceImpl implements NetworkService 
{
	//@Autowired
	private final HttpSignatureService sigServ;
	
    private final static Logger LOG = LoggerFactory.getLogger(NetworkServiceImpl.class);

    
    @Autowired
    public NetworkServiceImpl(KeyStoreService keyServ) throws KeyStoreException, UnsupportedEncodingException, NoSuchAlgorithmException, UnrecoverableKeyException, InvalidKeySpecException, IOException {
        this.sigServ = new HttpSignatureServiceImpl(DigestUtils.sha256Hex(keyServ.getHttpSigPublicKey().getEncoded()), keyServ.getHttpSigningKey());
        //LOG.info ("fingerprint: " + DigestUtils.sha256Hex(keyServ.getHttpSigPublicKey().getEncoded()));
        
//    	String RSApk = Base64.getEncoder().encodeToString(keyServ.getHttpSigPublicKey().getEncoded());  // From key to string
//    	
//    	this.sigServ = new HttpSignatureServiceImpl(keyServ.getFingerPrintFromStringPubKey(RSApk), keyServ.getHttpSigningKey());
//    	LOG.info ("RSApk: " + RSApk);
//    	LOG.info ("fingerprint: " + keyServ.getFingerPrintFromStringPubKey(RSApk));
   
        
        
//    	byte[] pkcs1 = keyServ.getX509PubKeytoRSABinaryFormat (keyServ.getHttpSigPublicKey());
//    	this.sigServ = new HttpSignatureServiceImpl(DigestUtils.sha256Hex(pkcs1), keyServ.getHttpSigningKey());
//    	
//    	LOG.info ("fingerprint: " + DigestUtils.sha256Hex(pkcs1));
    	
    	
    }
    
    

    // With URI (URL) parameters
    @Override
	public String sendGetURIParams(String hostUrl, String uri, List<NameValuePair> urlParameters, int attempt)
			throws IOException, NoSuchAlgorithmException 
	{
		Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM YYYY HH:mm:ss z", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        String nowDate = formatter.format(date);
        String requestId = UUID.randomUUID().toString();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(hostUrl + uri);
        Map<String, String> map = new HashMap();
        if (urlParameters != null) {
           
            urlParameters.stream().forEach(nameVal -> {
                map.put(nameVal.getName(), nameVal.getValue());
            });
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders requestHeaders = new HttpHeaders();
        String host = hostUrl.replace("http://", "").replace("https://", "");
        byte[] digest = MessageDigest.getInstance("SHA-256").digest("".getBytes());
        try {
            requestHeaders.add("host", host);
            requestHeaders.add("original-date", nowDate);
            requestHeaders.add("digest", "SHA-256=" + new String(org.tomitribe.auth.signatures.Base64.encodeBase64(digest)));
            requestHeaders.add("x-request-id", requestId);
            URL url = new URL(builder.buildAndExpand(map).toUriString());

            requestHeaders.add("authorization", sigServ.generateSignature(host, "GET", url.getPath(), null, "application/json", requestId));

        } catch (IOException | KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            LOG.error("could not generate signature!!");
            LOG.error(e.getMessage());
        }

        HttpEntity entity = new HttpEntity(requestHeaders);
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    builder.buildAndExpand(map).toUriString(), HttpMethod.GET, entity, String.class);
            
            if (response.getBody() == null && attempt < 2) {
            	LOG.error("A SECOND trial!");
            
            	return sendGetURIParams(hostUrl, uri,
                        urlParameters, attempt + 1);
            }
            
            return response.getBody();
        } catch (RestClientException e) {
        	LOG.error("Exception: A SECOND trial!");
            LOG.error(e.getMessage());
            if (attempt < 2) {
                return sendGetURIParams(hostUrl, uri,
                        urlParameters, attempt + 1);
            }
        }
        return null;
	}
    
    // The following sendGet is only with query parameters.
	@Override
	public String sendGet(String hostUrl, String uri, List<NameValuePair> urlParameters, int attempt)
			throws IOException, NoSuchAlgorithmException 
	{
		Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM YYYY HH:mm:ss z", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        String nowDate = formatter.format(date);
        String requestId = UUID.randomUUID().toString();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(hostUrl + uri);
        if (urlParameters != null) {
            Map<String, String> map = new HashMap();
            urlParameters.stream().forEach(nameVal -> {
                map.put(nameVal.getName(), nameVal.getValue());
                builder.queryParam(nameVal.getName(), nameVal.getValue());
            });
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders requestHeaders = new HttpHeaders();
        String host = hostUrl.replace("http://", "").replace("https://", "");
        byte[] digest = MessageDigest.getInstance("SHA-256").digest("".getBytes());
        try {
            requestHeaders.add("host", host);
            requestHeaders.add("original-date", nowDate);
            requestHeaders.add("digest", "SHA-256=" + new String(org.tomitribe.auth.signatures.Base64.encodeBase64(digest)));
            requestHeaders.add("x-request-id", requestId);
            URL url = new URL(builder.toUriString());

            //requestHeaders.add("authorization", sigServ.generateSignature(host, "GET", url.getPath() + "?" + url.getQuery(), null, "application/x-www-form-urlencoded", requestId));

            String getURL = StringUtils.isEmpty(url.getQuery())?url.getPath():url.getPath() + "?" + url.getQuery();
            
            requestHeaders.add("authorization", sigServ.generateSignature(host, "GET", getURL, null, "application/x-www-form-urlencoded", requestId));
            
        } catch (IOException | KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            LOG.error("could not generate signature!!");
            LOG.error(e.getMessage());
        }

        HttpEntity entity = new HttpEntity(requestHeaders);
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    builder.toUriString(), HttpMethod.GET, entity, String.class);
            
            if (response.getBody() == null && attempt < 2) {
            	LOG.error("A SECOND trial!");
            
            	return sendGetURIParams(hostUrl, uri,
                        urlParameters, attempt + 1);
            }
            
            return response.getBody();
        } catch (RestClientException e) {
        	LOG.error("Exception: A SECOND trial!");
            LOG.error(e.getMessage());
            if (attempt < 2) {
                return sendGet(hostUrl, uri,
                        urlParameters, attempt + 1);
            }
        }
        return null;
	}

	@Override
	public String sendPostForm(String hostUrl, String uri, List<NameValuePair> urlParameters, int attempt)
			throws IOException, NoSuchAlgorithmException 
	{
		Map<String, String> map = new HashMap();
        MultiValueMap<String, String> multiMap = new LinkedMultiValueMap<>();

        urlParameters.stream().forEach(nameVal -> {
            map.put(nameVal.getName(), nameVal.getValue());
            multiMap.add(nameVal.getName(), nameVal.getValue());
        });

        String requestId = UUID.randomUUID().toString();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String host = hostUrl.replace("http://", "").replace("https://", "");

        try {
            headers.add("authorization", sigServ.generateSignature(host, "POST", uri, null, "application/x-www-form-urlencoded", requestId));
            Date date = new Date();
            byte[] digestBytes;
            //only when the request is json encoded are the post params added to the body of the request
            // else they eventually become encoded to the url
            digestBytes = MessageDigest.getInstance("SHA-256").digest("".getBytes());
            addHeaders(headers, host, date, digestBytes, uri, requestId);

        } catch (IOException | KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            LOG.error("could not generate signature!!");
            LOG.error(e.getMessage());
        }

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(multiMap, headers);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    hostUrl + uri, request, String.class);
            return response.getBody();
        } catch (RestClientException e) {
            LOG.info("request failed will retry");
            if (attempt < 2) {
                return sendPostForm(hostUrl, uri,
                        urlParameters, attempt + 1);
            }
        }
        return null;
	}

	@Override
	public String sendPostBody(String hostUrl, String uri, Object postBody, String contentType, int attempt)
			throws IOException, NoSuchAlgorithmException 
	{
		Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM YYYY HH:mm:ss z", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        String nowDate = formatter.format(date);
        String requestId = UUID.randomUUID().toString();

        ObjectMapper mapper = new ObjectMapper();
        String updateString = mapper.writeValueAsString(postBody);
        byte[] digest = MessageDigest.getInstance("SHA-256").digest(updateString.getBytes()); // post parameters are added as uri parameters not in the body when form-encoding
        String host = hostUrl.replace("http://", "").replace("https://", "");
        try {
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.add("authorization", sigServ.generateSignature(host, "POST", "/sm/updateSessionData", postBody, "application/json;charset=UTF-8", requestId));
            requestHeaders.add("host", hostUrl);
            requestHeaders.add("original-date", nowDate);
            requestHeaders.add("digest", "SHA-256=" + new String(org.tomitribe.auth.signatures.Base64.encodeBase64(MessageDigest.getInstance("SHA-256").digest(updateString.getBytes()))));
            requestHeaders.add("x-request-id", requestId);
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(hostUrl + uri);
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<Object> requestEntity = new HttpEntity<>(postBody, requestHeaders);
            try {
                ResponseEntity<String> response
                        = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, requestEntity,
                                String.class);
                return response.getBody();
            } catch (RestClientException e) {
                LOG.info("request failed will retry");
                if (attempt < 2) {
                    return sendPostBody(hostUrl, uri, postBody, contentType, attempt + 1);
                }
            }
        } catch (UnrecoverableKeyException e) {
            LOG.info(e.getMessage());
        } catch (KeyStoreException e) {
            LOG.info(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            LOG.info(e.getMessage());
        }
        return null;

	}


	
	private void addHeaders(HttpHeaders headers, String host, Date date, byte[] digestBytes, String uri, String requestId) throws NoSuchAlgorithmException 
	{
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM YYYY HH:mm:ss z", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        String nowDate = formatter.format(date);
        headers.add("host", host);
        headers.add("original-date", nowDate);
        headers.add("digest", "SHA-256=" + new String(org.tomitribe.auth.signatures.Base64.encodeBase64(digestBytes)));
        headers.add("x-request-id", requestId);
    }

}
