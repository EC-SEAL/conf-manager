/**
Copyright © 2019  Atos Spain SA. All rights reserved.
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
package eu.seal.cm.manifest_api;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.seal.cm.network_api.NetworkServiceImpl;
import eu.seal.cm.params_api.KeyStoreService;
import eu.seal.cm.rest_api.domain.EsmoManifest;


@Service
public class ConfMngrConnServiceImp implements ConfMngrConnService
{
	private static final Logger log = LoggerFactory.getLogger(ConfMngrConnServiceImp.class);

	private KeyStoreService keyStoreService;
	
	private NetworkServiceImpl network = null;
	
	
	@Value("${seal.cm.getEsmoMetadataPath}")
	private String getEsmoMetadataPath;
	
	
	@Autowired
	public ConfMngrConnServiceImp (KeyStoreService keyStoreServ) {
		
    //public ConfMngrConnServiceImp (ParameterService paramServ, KeyStoreService keyStoreServ) {
		//this.paramServ = paramServ;
		
        //cmUrl = this.paramServ.getParam("CONFIGURATION_MANAGER_URL");
        
        this.keyStoreService = keyStoreServ;
	}
	
	
	// /ewp/seal-metadata
	@Override
	public EsmoManifest getEsmoManifest (String cmUrl) {
		
		EsmoManifest result = null;
		
		try {
			if (network == null)
			{
					network = new NetworkServiceImpl(keyStoreService);
			}
			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			
			String jsonResult = network.sendGet (cmUrl, // of the SEAL node being asked
					getEsmoMetadataPath, 
					urlParameters, 1);
			
			if (jsonResult != null) {
				//log.info("Result: "+ jsonResult);
		        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		        result = mapper.readValue(jsonResult, EsmoManifest.class);
			}
			
		}
		catch (Exception e) {
			log.error("CM exception", e);
			return null;
		}
		
		return result;
		
	}
	

	
}