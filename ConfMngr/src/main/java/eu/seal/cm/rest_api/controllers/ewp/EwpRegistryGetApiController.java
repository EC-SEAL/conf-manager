/**
Copyright © 2019  Atos Spain SA, UJI, UNIT. All rights reserved.
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
package eu.seal.cm.rest_api.controllers.ewp;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

//import javax.net.ssl.HttpsURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import eu.erasmuswithoutpaper.registryclient.ApiSearchConditions;
import eu.seal.cm.configuration.Constants;
import eu.seal.cm.manifest_api.ConfMngrConnService;
import eu.seal.cm.rest_api.domain.EndpointType;
import eu.seal.cm.rest_api.domain.EntityMetadata;
import eu.seal.cm.rest_api.domain.EntityMetadataList;
import eu.seal.cm.rest_api.domain.EsmoManifest;
import eu.seal.cm.rest_api.services.mdinternal.ConfigurationGetServiceImp;
import eu.seal.cm.utils.Utils;

@Controller
@EnableScheduling
public class EwpRegistryGetApiController implements EwpRegistryGetApi
{

    private static final Logger log = LoggerFactory
            .getLogger(EwpRegistryGetApiController.class);

    @Autowired
    EwpRegistryClient ewpRegistryClient;
    
    @Autowired
	private ConfMngrConnService confMngrConnService;

    @Value("${seal.cm.ewp.enabled}")
    boolean ewpEnabled;
    
    @Value("${seal.cm.ewp.path}")
    String ewpConfigPath;

    @Value("${seal.cm.ewp.manifest}")
    String ewpManifestFile;

    @Value("${seal.cm.ewp.gw.file}")
    String ewpGatewayFile;

    @Value("${seal.cm.ewp.aps.file}")
    String ewpApsFile;

    @Value("${seal.cm.ewp.esmo.manifest}")
    String ewpEsmoManifestFile;

    @Value("${remote.cm.ewp.gws.file}")
    String ewpRemoteGatewaysFile;

    @Value("${remote.cm.ewp.aps.file}")
    String ewpRemoteApsFile;
    
    @Value("${server.port}")
    String port;
    
    String esmoManifestJson = "";
    String fixedRateInMiliseconds = "";
    
    @Autowired
    private ConfigurationGetServiceImp confServ;
    

    @Autowired
    public EwpRegistryGetApiController(	@Value("${seal.cm.ewp.path}") final String ewpConfigPath,
    									@Value("${seal.cm.ewp.gw.file}") final String ewpGatewayFile,
    									@Value("${seal.cm.ewp.aps.file}") final String ewpApsFile,
    									@Value("${seal.cm.ewp.esmo.manifest}") final String ewpEsmoManifestFile) {
    	
    	if (ewpEnabled) {
    	
    	try
        {
	    	String gatewayFile = Utils.readFile(ewpConfigPath + "/" + ewpGatewayFile);
	        String apsFile = Utils.readFile(ewpConfigPath + "/" + ewpApsFile);
	
	        ObjectMapper mapper = new ObjectMapper();
	        EntityMetadata gatewayMetadata = mapper.readValue(gatewayFile, EntityMetadata.class);
	        EntityMetadataList apsMetadata = mapper.readValue(apsFile, EntityMetadataList.class);
	
	        EsmoManifest esmoManifest = new EsmoManifest();
	        esmoManifest.setGateway(gatewayMetadata);
	        esmoManifest.setProxiedEntities(apsMetadata);
	
	        esmoManifestJson = mapper.writeValueAsString(esmoManifest);
	
	        FileWriter fr = new FileWriter(ewpConfigPath + "/" + ewpEsmoManifestFile);
	        fr.write(esmoManifestJson);
	        fr.close();
	        
	        //log.info("esmoManifest built.");
	        
        }
    	catch (Exception e)
        {
            e.printStackTrace();
            log.info("esmoManifest NOT built.");
        }
    	
    	}
        
    }
    
    public EwpRegistryClient getRegistryClient() {
    	return ewpRegistryClient;
    	
    }
    
    public ResponseEntity<String> ewpManifest()
    {
        try
        {
            /*
             * TODO: Create dynamically/template, based on configuration file (only the URL of this
             * manifest (discovery -> url), the SEAL manifest (esmo -> url) and public keys
             * (server-credentials-in-use -> rsa-public-key) are delivered, and this would avoid
             * having these configured in multiple places.
             */
            String manifest = Utils.readFile(ewpConfigPath + "/" + ewpManifestFile);
            return new ResponseEntity<String>(manifest, HttpStatus.OK);

        }
        catch (Exception e)
        {
            return new ResponseEntity<String>(Constants.EWP_MANIFEST_NOT_FOUND,
                    HttpStatus.NOT_FOUND);
        }
    }

    //public ResponseEntity<String> ewpEsmoHosts()
    
    //@Scheduled(fixedRate = 10000)
    //@Scheduled(initialDelay = 5000)  // Five seconds. Testing...
    @Scheduled(fixedRateString = "${seal.cm.ewp.fixedRate.in.milliseconds}")
    public void ewpEsmoHosts()
    {
    	if (ewpEnabled) {
    		
    	
    	log.info("Start refreshing esmo-manifest..... " );
    	try {
	      String gatewayFile = Utils.readFile(ewpConfigPath + "/" + ewpGatewayFile);
	      String apsFile = Utils.readFile(ewpConfigPath + "/" + ewpApsFile);
	
	      ObjectMapper mapper = new ObjectMapper();
	      EntityMetadata gatewayMetadata = mapper.readValue(gatewayFile, EntityMetadata.class);
	      EntityMetadataList apsMetadata = mapper.readValue(apsFile, EntityMetadataList.class);
	
	      EsmoManifest myEsmoManifest = new EsmoManifest();
	      myEsmoManifest.setGateway(gatewayMetadata);
	      myEsmoManifest.setProxiedEntities(apsMetadata);
	
	      String esmoManifestJson = mapper.writeValueAsString(myEsmoManifest);
	
	      FileWriter fr = new FileWriter(ewpConfigPath + "/" + ewpEsmoManifestFile);
	      fr.write(esmoManifestJson);
	      fr.close();
    	}
    	
    	 catch (Exception e)
        {
    		 log.info("Could not refresh SEAL manifest");
             log.info("Error: " + e);
        }
    	
    	//log.info("Refreshing rAPs and rGWs - " + System.currentTimeMillis() / 10000); // Every 10 seconds
    	log.info("Start refreshing rAPs and rGWs..... " );
    	
        //String jsonStringAllEntities = null;

        List<EsmoManifest> esmoManifestList = new ArrayList<EsmoManifest>();

        ApiSearchConditions myConditions = new ApiSearchConditions();
        myConditions.setApiClassRequired(null, "esmo");
        Collection<Element> esmoHostsInRegistry = ewpRegistryClient.getClient()
                .findApis(myConditions);
        //log.info("EWP: Found " + esmoHostsInRegistry.size() + " SEAL hosts in the registry");

        if (esmoHostsInRegistry.size() == 0)
        {
            //return new ResponseEntity<String>(Constants.EWP_NO_SEAL_HOSTS, HttpStatus.NOT_FOUND);
        	log.info(Constants.EWP_NO_SEAL_HOSTS);
        	return;
        }
        
        String ownUrl = ownURL() + ":" + port;
        
        for (Element esmoNode : esmoHostsInRegistry)
        {
            NodeList urls = esmoNode.getElementsByTagName("url");

            if (urls != null == urls.getLength() > 0)
            {
                String urlString = urls.item(0).getTextContent().replaceAll("\\s+","");

                URL url;
                try
                {
                    url = new URL(urlString);
                }
                catch (MalformedURLException e)
                {
                    log.info("EWP: Incorrect SEAL URL in EWP registry " + urlString);
                    continue;
                }

                try
                {
                    log.info("EWP: Fetching SEAL manifest from " + urlString); // E.g.: http://5.79.83.118:8080/ewp/esmo-metadata

                    /*
                     * Send a signed request and verify signature in the response
                     */
                    EsmoManifest esmoManifest = null;
                    String cmUrl = urlString.substring(0, urlString.indexOf ("/ewp/"));
                    
                    if (!cmUrl.contains(ownUrl)) {
                    
	                    esmoManifest = confMngrConnService.getEsmoManifest(cmUrl);
	                    if (esmoManifest != null)
	                    	esmoManifestList.add(esmoManifest);
	                    else
	                    	
	                    	log.info("ERROR when fetching SEAL manifest from " + cmUrl);
                    
                    }
                    
                    /*
                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setAllowUserInteraction(false);
                    conn.connect();

                    int status = conn.getResponseCode();

                    if (status == 200)
                    {
                        Collection<RSAPublicKey> rsaPublicKeys = ewpRegistryClient
                                .getServerKeysCoveringApi(esmoNode);
                        log.info("EWP: Accepted RSA public keys for verifying signature: "
                                + rsaPublicKeys.size());

                        byte[] content = readEntireStream(conn.getInputStream());
                        String jsonStringValue = new String(content);

                        Gson gson = new Gson();
                        EsmoManifest esmoManifest;

                        esmoManifest = gson.fromJson(jsonStringValue, EsmoManifest.class);

                        esmoManifestList.add(esmoManifest);
                    }
                    else
                    {
                        log.info("EWP: HTTP Response status " + status);
                    }
                    */

                }
                catch (Exception e)
                {
                    log.info("EWP: Could not fetch SEAL manifest from host " + urlString);
                    log.info("EWP: Error: " + e);
                }
            }
        }

        try
        {
        	;
            updateRemoteEsmoManifestData(esmoManifestList);
        	
        }
        catch (Exception e)
        {
            log.info("EWP: Error updating SEAL manifest data " + e);
            //return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
            return;
        }

        /*
         * Gson gson = new Gson(); jsonStringAllEntities = gson.toJson(esmoManifestList);
         *
         * return new ResponseEntity<String>(jsonStringAllEntities, HttpStatus.OK);
         */

        //return new ResponseEntity<String>(HttpStatus.OK);
        
        //log.info("TESTING: no update of rGWs and rAPs...");
        //log.info("rAPs and rGWs updated successfully.");
        
    	}
        return;
    }

    public static Iterable<Node> iterable(final NodeList nodeList)
    {
        return () -> new Iterator<Node>()
        {
            private int index = 0;

            @Override
            public boolean hasNext()
            {
                return index < nodeList.getLength();
            }

            @Override
            public Node next()
            {
                if (!hasNext())
                    throw new NoSuchElementException();
                return nodeList.item(index++);
            }
        };
    }

    private static byte[] readEntireStream(InputStream is) throws IOException
    {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nread;
        byte[] data = new byte[16384];
        while ((nread = is.read(data, 0, data.length)) != -1)
        {
            buffer.write(data, 0, nread);
        }
        buffer.flush();
        return buffer.toByteArray();
    }

    @Override
    public ResponseEntity<String> ewpEsmoMetadata()	
    {
        try
        {
//            String gatewayFile = Utils.readFile(ewpConfigPath + "/" + ewpGatewayFile);
//            String apsFile = Utils.readFile(ewpConfigPath + "/" + ewpApsFile);
//
//            ObjectMapper mapper = new ObjectMapper();
//            EntityMetadata gatewayMetadata = mapper.readValue(gatewayFile, EntityMetadata.class);
//            EntityMetadataList apsMetadata = mapper.readValue(apsFile, EntityMetadataList.class);
//
//            EsmoManifest esmoManifest = new EsmoManifest();
//            esmoManifest.setGateway(gatewayMetadata);
//            esmoManifest.setProxiedEntities(apsMetadata);
//
//            String esmoManifestJson = mapper.writeValueAsString(esmoManifest);
//
//            FileWriter fr = new FileWriter(ewpConfigPath + "/" + ewpEsmoManifestFile);
//            fr.write(esmoManifestJson);
//            fr.close();
        	
            return new ResponseEntity<String>(esmoManifestJson, HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<String>(Constants.EWP_NO_SEAL_HOSTS, HttpStatus.NOT_FOUND);
        }
    }

    private void updateRemoteEsmoManifestData(List<EsmoManifest> esmoManifestList) throws Exception
    {
        // Read own gateway and avoid
        // we need 2 lists: remote gateways and remote aps

        String gatewayFile = Utils.readFile(ewpConfigPath + "/" + ewpGatewayFile);
        ObjectMapper mapper = new ObjectMapper();
        EntityMetadata gatewayMetadata = mapper.readValue(gatewayFile, EntityMetadata.class);

        EntityMetadataList remoteGateways = new EntityMetadataList();
        EntityMetadataList remoteAps = new EntityMetadataList();

        for (EsmoManifest esmoManifest : esmoManifestList)
        {
            if (!esmoManifest.getGateway().getEntityId().trim()
                    .equals(gatewayMetadata.getEntityId().trim()))
            {
            	if ((esmoManifest.getGateway().getOtherData() == null ) ||
					(esmoManifest.getGateway().getOtherData() != null  &&
            	    !isHidden (esmoManifest.getGateway().getOtherData()))) {
            		// It can be shown in  other SEAL nodes
            		remoteGateways.add(esmoManifest.getGateway());
            	}
                
                List<EndpointType> endpoints = esmoManifest.getGateway().getEndpoints();
                // Updating the endpoints for every one
//              "endpoints": [
//                            {
//                              "type": "dsaRequest",
//                              "method": "HTTP-POST",
//                              "url": destiny + ":8050/esmo/gw/dsaRequest"
//                            },
//                            {
//                              "type": "dsaResponse",
//                              "method": "HTTP-POST",
//                              "url": destiny + ":8050/esmo/gw/dsaResponse"
//                            }
//                          ]
                EntityMetadataList allProxiedEntities = esmoManifest.getProxiedEntities();
                for (EntityMetadata pE : allProxiedEntities) {
                	
                	if ((pE.getOtherData() == null) || 
                		(pE.getOtherData() != null && !isHidden (pE.getOtherData()))) {
                		// It can be shown in other SEAL nodes
                		pE.setEndpoints(endpoints);
                		remoteAps.add(pE);
                	}
                }
                
            }
        }
        
        // Updating the microservice id for every rAP: the local GWms one

        EntityMetadata internals = confServ.ConfigurationGet("LGW");
        List<String> msId = internals.getMicroservice();
	
        
        for (EntityMetadata rAP :  remoteAps)
        	rAP.setMicroservice(msId);      	
        

        FileWriter frGws = new FileWriter(ewpConfigPath + "/" + ewpRemoteGatewaysFile);
        frGws.write(mapper.writeValueAsString(remoteGateways));
        frGws.close();

        FileWriter frAps = new FileWriter(ewpConfigPath + "/" + ewpRemoteApsFile);
        frAps.write(mapper.writeValueAsString(remoteAps));
        frAps.close();
    }
    
    private String ownURL () {
    	
    	String ownUrl = "";
    	
    	try {
			EntityMetadata internals = confServ.ConfigurationGet("LGW");
			
			String auxUrl = "";
			for (EndpointType eP : internals.getEndpoints()) {
				auxUrl = eP.getUrl();
				ownUrl = auxUrl.substring(0, auxUrl.lastIndexOf(":"));
				break;
				
			}
			//ownUrl = ownUrl + ":" + port;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
    	
    	//log.info( "ownUrl: " + ownUrl);
    	return ownUrl;
    }
    
    // Returns true only if hidden == true.
    private boolean isHidden (Object otherData) {
    	
    	Gson gson = new Gson();
    	String jsonString = gson.toJson(otherData); 	
    	JsonObject jobj = new Gson().fromJson(jsonString, JsonObject.class);
    		
    	if (jobj.get("hidden") != null)
    		return (jobj.get("hidden").getAsBoolean());
    	else return false;
    	
    }
}
