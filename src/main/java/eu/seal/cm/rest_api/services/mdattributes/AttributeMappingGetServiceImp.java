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
package eu.seal.cm.rest_api.services.mdattributes;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import eu.seal.cm.configuration.Constants;
import eu.seal.cm.rest_api.domain.AttributeMapList;

import eu.seal.cm.utils.Utils;

@Service
public class AttributeMappingGetServiceImp implements AttributeMappingGetService {
	
	@Value ( "${seal.cm.attributes.path}" )
	String msFile;
	
	@Value ( "${seal.cm.files.extension}" )
	String fileExtension;
	
	@Override
	public AttributeMapList attributeMappingGet (String attrProfileId) throws Exception {
		// TO BE AWARE of the fileName expected!!!
		
		AttributeMapList attrMappings = new AttributeMapList();
		//TODO
		
		// File with eIDAS to eduPerson mappings:
		
		// OLD OLD OLD
//		String fileStringValue = "[{ \n" +
//				"\"keyProfile\" : \"eIDAS\", \n" +
//				"\"valueProfile\" : \"eduPerson\", \n" +
//				"\"mappings\" : \n" +
//				"  { \n" +
//				"    \"CurrentGivenName\" : [\"givenName\", \"displayName\",\"cn\"], \n" +
//				"    \"CurrentFamilyName\" : [\"sn\", \"displayName\",\"cn\"] \n" +
//				"  } \n" +
//				"}]";

		String fileStringValue = "[{ \n" +
					  "\"description\": \"eIDAS to eduPerson fake mapping, showing both concatenation and transformation in one side\",\n" +
					  "\"pairings\": [ \n" +
					    "{ \n" +
					      "\"profile\" :  \"eIDAS\", \n" +
					      "\"issuer\" : \"http://clave.redsara.es/\", \n" +
					      "\"attributes\" : [\"$surnames\"] \n" +
					    "}, \n" +
					    "{ \n" +
					    "\"profile\" :  \"eduPerson\", \n" +
					    "\"attributes\" : [\"$surname1\",\"#\",\"$surname2\"], \n" +
					    "\"regexp\" : \"^(-,a-zA-Z)+#(-,a-zA-Z)+$\", \n" +
					    "\"replace\" : \"\1 \2\" \n" +
					    "} \n" +
					  "] \n" +
					"}, \n" +

					"{ \n" +
					"\"description\": \"Basic mapping, also inaccurate, but to show the options\", \n" +
					"\"pairings\": [ \n" +
					    "{ \n" +
					    "\"profile\" :  \"eIDAS\", \n" +
					    "\"attributes\" : [\"$CurrentGivenName\"] \n" +
					    "}, \n" +
					    "{ \n" +
					    "\"profile\" :  \"eduPerson\", \n" +
					    "\"attributes\" : [\"$givenName\"] \n" +
					    "}, \n" +
					    "{ \n" +
					    "\"profile\" :  \"eduPerson\",  \n" +
					    "\"attributes\" : [\"$displayName\"] \n" +
					    "}, \n" +
					    "{ \n" +
					    "\"profile\" :  \"eduPerson\", \n" +
					    "\"attributes\" : [\"$cn\"] \n" +
					    "} \n" +
					  "] \n" +
					"}] \n" ;

	
		
		try {
			Gson gson = new Gson();
			attrMappings = gson.fromJson(fileStringValue, AttributeMapList.class);
		//} catch (IOException e){
		//	throw new IOException(e);
		} catch (JsonSyntaxException e) {
			throw new JsonSyntaxException(e);
		} catch (Exception e){			
			throw new Exception(e);
		}
		//return attrMappings.getMapList(attrProfileId);
		return attrMappings;
		
//		AttributeTypeList attributes= new AttributeTypeList();
//		
//		String fileStringValue;
//		try {
//				
//			fileStringValue = Utils.readFile (msFile + attrProfileId + fileExtension);
//			Gson gson = new Gson();
//			attributes = gson.fromJson(fileStringValue, AttributeTypeList.class);
//			if (attributes.isEmpty())
//				throw new Exception(Constants.ATTRIBUTES_NOT_FOUND);
//			
//		} catch (IOException e){
//			throw new IOException(e);
//		} catch (JsonSyntaxException e) {
//			throw new JsonSyntaxException(e);
//		} catch (Exception e){			
//			throw new Exception(e);
//		}
//		
//		return attributes;
	}

}

