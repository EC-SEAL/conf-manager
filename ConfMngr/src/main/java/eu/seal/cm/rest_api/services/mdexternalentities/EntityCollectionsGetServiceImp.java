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
package eu.seal.cm.rest_api.services.mdexternalentities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import eu.seal.cm.configuration.Constants;

@Service
public class EntityCollectionsGetServiceImp implements EntityCollectionsGetService{

	@Value ( "${seal.cm.externalentities.path}" )
	String externalEntitiesPath;
	
	@Override
	public List<String> entityCollectionsGet () throws Exception {
		// TO BE AWARE of the fileName expected!!
		try {
			
		
			File dir = new File(externalEntitiesPath);
			String[] files = dir.list();
		
			List<String> entityTypes = new ArrayList<String>();
		
			if (files != null && files.length != 0) {
			  String fileName = new String();
			  Boolean unknownKind;
			  for (int x=0;x<files.length;x++) {
				  fileName = files[x];
				  
				  String entityKind = new String();
				  entityKind = fileName.substring (0,fileName.indexOf(".json"));
				  
				  String entityCollection = new String();
				  unknownKind = false;
				  // TO BE AWARE of the fileName expected
				  if (entityKind.contains ("SP") || entityKind.contains ("ServiceProvider"))
					  entityCollection = "SP";
				  else 
					  if (entityKind.contains ("rAP") || entityKind.contains ("RemoteAttributeProvider"))
						  entityCollection = "rAP";
					  else
						  if (entityKind.contains ("IdP") || entityKind.contains ("IDP") || entityKind.contains ("IdentityProvider"))
							  entityCollection = "IdP";
						  else
							  if (entityKind.contains ("rGW") || entityKind.contains ("RemoteGateway"))
								  entityCollection = "rGW";
							  else 
								  if (entityKind.contains ("AP") || entityKind.contains ("LocalAttributeProvider"))
									  entityCollection = "AP";
								  else {unknownKind = true; throw new Exception("Unknown kind of entity");}
				  
				  if (!unknownKind) entityTypes.add(entityCollection);
			  	}
			  
			    
			}
			else // Empty directory
				throw new Exception(Constants.ENTITY_FILES_NOT_FOUND);
		
			return (entityTypes);
		}
		catch (Exception e) {
			throw new Exception (e);
		}
	}

}

