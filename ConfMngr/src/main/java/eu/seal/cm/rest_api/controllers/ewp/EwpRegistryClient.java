/**
Copyright © 2019  Atos Spain SA, UNIT. All rights reserved.
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

import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eu.erasmuswithoutpaper.registryclient.ClientImpl;
import eu.erasmuswithoutpaper.registryclient.ClientImplOptions;
import eu.erasmuswithoutpaper.registryclient.DefaultCatalogueFetcher;
import eu.erasmuswithoutpaper.registryclient.RegistryClient;


@Controller
public class EwpRegistryClient {

    Logger log = Logger.getLogger(EwpRegistryClient.class.getName());

    private eu.erasmuswithoutpaper.registryclient.RegistryClient client = null;
    
    @Value("${seal.cm.ewp.enabled}")
    boolean ewpEnabled;

    @Value("${seal.cm.ewp.registry.url}")
    String ewpRegistryUrl;

    @Value("${seal.cm.ewp.registry.autoRefresh}")
    String ewpRegistryAutoRefresh;
    
    
    @PostConstruct
    private void loadRegistryClient() {
    
    	if (ewpEnabled) {
    		
        try {
            // ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory
            // .getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
            // root.setLevel(ch.qos.logback.classic.Level.DEBUG);

            log.info("Starting registry client, registry URL: " + "https://" + ewpRegistryUrl + "/catalogue-v1.xml");
            ClientImplOptions options = new ClientImplOptions();
            options.setCatalogueFetcher(new DefaultCatalogueFetcher(ewpRegistryUrl));
            options.setAutoRefreshing("true".equalsIgnoreCase(ewpRegistryAutoRefresh));
            client = new ClientImpl(options);

            client.refresh();
        } catch (Exception ex) {
            log.severe( ex.getMessage());
        }
        
    	}
    }


    public RegistryClient getClient() {
        return this.client;
    }

    public Collection<String> getHeisCoveredByCertificate(String fingerprint) {
        if (fingerprint != null && client.isCertificateKnown(fingerprint)) {
            Collection<String> heiIds = client.getHeisCoveredByCertificate(fingerprint);
            return heiIds;
        }

        return new ArrayList<>();
    }


    public Collection<String> getHeisCoveredByClientKey(RSAPublicKey clientKey) {
        return client.getHeisCoveredByClientKey(clientKey);
    }


    public boolean isClientKeyKnown(RSAPublicKey clientKey) {
    	
    	log.info("isClientKeyKnown: " + client.isClientKeyKnown(clientKey));
        return client.isClientKeyKnown(clientKey);
    }


    public Collection<RSAPublicKey> getServerKeysCoveringApi(Element apiElement) {
        return client.getServerKeysCoveringApi(apiElement);
    }


    public RSAPublicKey findClientRsaPublicKey(String fingerprint) {
        if (fingerprint != null) {
            RSAPublicKey key = client.findRsaPublicKey(fingerprint);
            
            //if (key != null && isClientKeyKnown(key)) {
	            // Matija says:
	            // "I traced it all the way to the implementation in the registry client...
	            // return this.cliKeyHeis.containsKey(fingerprint);
	            // There are no HEIs registered for our SEAL hosts.
	            // so, isClientKeyKnown should not be called in SEAL."
            if (key != null	) {
                return key;
            }
        }
        return null;
    }


    private Map<String, String> getUrlsFromManifestElement(Element manifestElement) {
        Map<String, String> urlMap = new HashMap<>();
        NodeList childNodeList = manifestElement.getChildNodes();
        for (int i = 0; i < childNodeList.getLength(); i++) {
            Node childNode = childNodeList.item(i);
            if ("url".equalsIgnoreCase(childNode.getLocalName())) {
                urlMap.put("url", childNode.getFirstChild().getNodeValue());
            }
        }

        return urlMap;
    }


    private int getIntProperty(String value, int defaultValue) {
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                log.log(Level.SEVERE, "Not a number " + value, e);
            }
        }
        return defaultValue;
    }

}
