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
import eu.seal.cm.rest_api.domain.AttributeTypeList;
import eu.seal.cm.utils.Utils;

@Service
public class AttributeSetGetServiceImp implements AttributeSetGetService {
	
	@Value ( "${seal.cm.attributes.path}" )
	String msFile;
	
	@Value ( "${seal.cm.files.extension}" )
	String fileExtension;
	
	@Override
	public AttributeTypeList attributeSetGet (String attrProfileId) throws Exception {
		// TO BE AWARE of the fileName expected!!!
		
		AttributeTypeList attributes= new AttributeTypeList();
		
		String fileStringValue;
		try {
				
			fileStringValue = Utils.readFile (msFile + attrProfileId + fileExtension);
			Gson gson = new Gson();
			attributes = gson.fromJson(fileStringValue, AttributeTypeList.class);
			if (attributes.isEmpty())
				throw new Exception(Constants.ATTRIBUTES_NOT_FOUND);
			
		} catch (IOException e){
			throw new IOException(e);
		} catch (JsonSyntaxException e) {
			throw new JsonSyntaxException(e);
		} catch (Exception e){			
			throw new Exception(e);
		}
		
		return attributes;
	}

}

