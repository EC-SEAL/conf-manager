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
package eu.seal.cm.httpSig_api;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.seal.cm.rest_api.controllers.ewp.EwpRegistryGetApiController;
import eu.seal.cm.rest_api.domain.HttpResponseEnum;
import eu.seal.cm.rest_api.domain.MsMetadataList;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

//import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.tomitribe.auth.signatures.Algorithm;
import org.tomitribe.auth.signatures.Signature;
import org.tomitribe.auth.signatures.Signer;
import org.tomitribe.auth.signatures.Verifier;


/**
 *
 * @author UAegean, Atos
 */
//@Service
public class HttpSignatureServiceImpl implements HttpSignatureService {

    private final static Logger log = LoggerFactory.getLogger(HttpSignatureServiceImpl.class);

    public static String[] requiredHeaders = {"(request-target)", "host", "original-date", "digest", "x-request-id"};
    private static final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss z";
    private static final int DATE_DIFF_ALLOWED = 5;
    
    
    private String keyId;
    private Key siginingKey;
    

    //@Autowired
    public HttpSignatureServiceImpl(String keyId, Key signingKey)
            throws InvalidKeySpecException, IOException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
        try {
            this.keyId = keyId;
            this.siginingKey = signingKey;
            //this.signer = new Signer(this.siginingKey, new Signature(this.keyId, algorithm, null, "(request-target)", "host", "original-date", "digest", "x-request-id"));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    
    @Override
    public HttpResponseEnum verifySignature(HttpServletRequest httpRequest, MsMetadataList allMicroservices, EwpRegistryGetApiController ewpRegistryServ) {
    //public HttpResponseEnum verifySignature(HttpServletRequest httpRequest, MSConfigurationService confServ) {
        String authorization = httpRequest.getHeader("authorization");
        if (authorization != null) {
            final Signature sigToVerify = Signature.fromString(authorization);
            log.info("HTTP Signature received: " + sigToVerify);
            /* check request contains all mandatory headers
             */
            boolean emptyRequiredHeader
                    = sigToVerify.getHeaders()
                            .stream()
                            .anyMatch(headerName -> {
                                return StringUtils.isEmpty(httpRequest.getHeader(headerName)) && !(headerName.equals("(request-target)"));
                            });
            /* Verify that all the required headers are signed (i.e. are part of the http signature)
                and that all the signed headers are present in the request
             */
            if (!sigToVerify.getHeaders().containsAll(Arrays.asList(requiredHeaders))
                    || emptyRequiredHeader) {
                log.error("error header is missing!!!");
                return HttpResponseEnum.HEADER_MISSING;
            }

            final Map<String, String> headers = new HashMap<String, String>();
            Collections.list(httpRequest.getHeaderNames())
                    .stream().forEach(hName -> {
                        headers.put(hName, httpRequest.getHeader(hName));
                    });

            final String clientTime = StringUtils.isEmpty(httpRequest.getHeader("date"))
                    ? httpRequest.getHeader("original-date") : httpRequest.getHeader("date");

            try {
                //TODO blacklist requestIds to remove replay attacks?
//                String requestId = UUID.fromString(httpRequest.getHeader("x-request-id")).toString();
                if (!hasValidRequestTime(clientTime)) {
                    return HttpResponseEnum.BAD_REQUEST;
                }
                final byte[] requestBodyRaw = IOUtils.toByteArray(httpRequest.getInputStream());
                final byte[] digest = MessageDigest.getInstance("SHA-256").digest(requestBodyRaw);
                final String digestCalculated = new String(Base64.getEncoder().encodeToString(digest));

                if (!areDigestsEqual(httpRequest.getHeader("digest"), digestCalculated)) {
                    log.error("Digest missmatch");
                    return HttpResponseEnum.UN_AUTHORIZED;
                }

                String method = httpRequest.getMethod().toLowerCase();
                String uri = httpRequest.getRequestURI();
                if (!StringUtils.isEmpty(httpRequest.getQueryString())) {
                    uri += "?" + httpRequest.getQueryString();
                }
                log.info("Veryfing signature for " + uri + " and verb " + method);

                
                if (allMicroservices != null) {
	                //if (isSignatureValid(sigToVerify, confServ, method, uri, headers)) {
	                if (isSignatureValid(sigToVerify,  allMicroservices, method, uri, headers)) {
	                    return HttpResponseEnum.AUTHORIZED;
	                } else {
	                    log.error("could not verify signature!! from library method: " + method + " uri: " + uri);
	                }
                }
                else {
                	if (isSignatureValidEWP(sigToVerify,  ewpRegistryServ, method, uri, headers)) {
	                    return HttpResponseEnum.AUTHORIZED;
	                } else {
	                    log.error("could not verify signature!! from library method: " + method + " uri: " + uri);
	                }              	
                }
                
                return HttpResponseEnum.UN_AUTHORIZED;

            } catch (IllegalArgumentException e) {
                log.error("Wrong request ID");
                log.error(e.getMessage());
                return HttpResponseEnum.BAD_REQUEST;
            } catch (ParseException ex) {
                log.error("Error parsing date");
                log.error(ex.getMessage());
                return HttpResponseEnum.BAD_REQUEST;
            } catch (IOException ex) {
                log.error("ERROR getting request content");
                log.error(ex.getMessage());
                return HttpResponseEnum.BAD_REQUEST;
            } catch (NoSuchAlgorithmException ex) {
                log.error(ex.getMessage());
                return HttpResponseEnum.UN_AUTHORIZED;
            } catch (InvalidKeyException | InvalidKeySpecException | SignatureException ex) {
                log.error("Error verifying Signature");
                log.error(ex.getMessage());
                return HttpResponseEnum.UN_AUTHORIZED;
            }
        }
        return HttpResponseEnum.UN_AUTHORIZED;
    }

    public boolean hasValidRequestTime(String receivedTime) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        final Date timeServer = new Date();
        final Date timeClient = format.parse(receivedTime);
        long diff = Math.abs(timeClient.getTime() - timeServer.getTime());
        long diffMinutes = diff / (60 * 1000) % 60;
//        return true;
        return diffMinutes < DATE_DIFF_ALLOWED;
    }

    public boolean areDigestsEqual(String requestDigest, String calculatedDigest) {
        String reqDigestSha256;
        Pattern p = Pattern.compile("SHA-256=([^,$]+)");
        Matcher m = p.matcher(requestDigest);
        if (m.find()) {
            reqDigestSha256 = m.group(1);
            //log.info("Extracted SHA-256 digest: " + reqDigestSha256);
        } else {
            return false;
        }
        return calculatedDigest.equals(reqDigestSha256);
    }

    public boolean isSignatureValid(Signature sigToVerify,
            //MSConfigurationService msConfigServ, 
    		MsMetadataList allMicroservices,
            String method, String uri, Map<String, String> headers) throws InvalidKeyException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, SignatureException {
        String fingerprint = sigToVerify.getKeyId();
        log.info("fingerprint: ", fingerprint);
        final Optional<PublicKey> pubKey = allMicroservices.getPublicKeyFromFingerPrint(fingerprint);
        log.info("publickey: " + pubKey);
        
        headers.entrySet().forEach(e -> {
            log.info(e.getKey() + ":-->" + e.getValue());
        });
        log.info("URI " + uri);
        log.info("Method " + method);
        log.info(headers.get("original-date") + "Original-date");
        
        if (pubKey.isPresent()) {
            final Verifier verifier = new Verifier(pubKey.get(), sigToVerify);
            return verifier.verify(method, uri, headers);
            
        } else {
            log.error("could not find sender key!");
        }
        return false;
    }
    
    
    public boolean isSignatureValidEWP(Signature sigToVerify,
    		EwpRegistryGetApiController ewpRegistryServ,
            String method, String uri, Map<String, String> headers) throws InvalidKeyException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, SignatureException {
        
    	String fingerprint = sigToVerify.getKeyId();
    	//log.info("EWPvalidating... fingerprint: " + fingerprint);
    	
    	// 7a9ba747ab5ac50e640a07d90611ce612b7bde775457f2e57b804517a87c813b
        // It does not work:
    	RSAPublicKey RSApubKey = ewpRegistryServ.getRegistryClient().findClientRsaPublicKey(fingerprint);
        
        if (RSApubKey != null) {
	        final Optional<PublicKey> pubKey = Optional.of(getKey (RSApubKey.getEncoded()));
	        
	        //headers.entrySet().forEach(e -> {
	            //log.info(e.getKey() + ":-->" + e.getValue());
	        //});
	        //log.info("URI " + uri);
	        //log.info("Method " + method);
	        //log.info(headers.get("original-date") + "Original-date");
	        
	        if (pubKey.isPresent()) {
	            final Verifier verifier = new Verifier(pubKey.get(), sigToVerify);
	            return verifier.verify(method, uri, headers);
	            
	        } else {
	            log.error("could not find sender key!");
	        }
        
        }
        log.info("Not found in the EWP catalog");
        return false;
    }
    
    
    public static PublicKey getKey(byte[] byteKey){
        try{
            //byte[] byteKey = Base64.decode(key.getBytes(), Base64.DEFAULT);
            X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");

            return kf.generatePublic(X509publicKey);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }
    
    @Override
    public String generateSignature(String hostUrl, String method, String uri, Object postParams, String contentType, String requestId)
            throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, UnsupportedEncodingException, IOException {
    	
    	//log.info("Generate Signature ...");
    	
        final Map<String, String> signatureHeaders = new HashMap<>();
        signatureHeaders.put("host", hostUrl);
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM YYYY HH:mm:ss z", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        
//        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM YYYY HH:mm:ss z");
        
//        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM YYYY HH:mm:ss z",Locale.ENGLISH);
        
        String nowDate = formatter.format(date);
        signatureHeaders.put("original-date", nowDate);
        signatureHeaders.put("Content-Type", contentType);

        byte[] digest;
        if (postParams != null && contentType.contains("application/json")) {
            ObjectMapper mapper = new ObjectMapper();
            String updateString = mapper.writeValueAsString(postParams);
            digest = MessageDigest.getInstance("SHA-256").digest(updateString.getBytes());
        } else {
            if (postParams != null && contentType.contains("x-www-form-urlencoded") && postParams instanceof Map) {
                digest = MessageDigest.getInstance("SHA-256").digest(getParamsString((Map<String, String>) postParams).getBytes());
            } else {
                digest = MessageDigest.getInstance("SHA-256").digest("".getBytes());
            }
        }
        signatureHeaders.put("digest", "SHA-256=" + new String(org.tomitribe.auth.signatures.Base64.encodeBase64(digest)));
        signatureHeaders.put("Accept", "*/*");
        signatureHeaders.put("Content-Length", Integer.toString(digest.length));
        signatureHeaders.put("x-request-id", requestId);
        signatureHeaders.put("(request-target)", method + " " + uri);

        Algorithm algorithm = Algorithm.RSA_SHA256;
        Signer signer = new Signer(this.siginingKey, new Signature(this.keyId, algorithm, null, "(request-target)", "host", "original-date", "digest", "x-request-id"));
        Signature signed = signer.sign(method, uri, signatureHeaders); //getSigner(this.siginingKey, this.keyId).sign(method, uri, signatureHeaders);
        return signed.toString();
    }

    public static String getParamsString(Map<String, String> params)
            throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            result.append("&");
        }
        String resultString = result.toString();
        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }

}
