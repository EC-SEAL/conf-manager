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
package eu.seal.cm.rest_api.services.mdinternal;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import eu.seal.cm.configuration.Constants;
import eu.seal.cm.rest_api.domain.EntityMetadata;
import eu.seal.cm.utils.Utils;

@Service
public class ConfigurationGetServiceImp implements ConfigurationGetService{
	
	@Value ( "${seal.cm.internal.path}" )
	String internalPath;
	
	@Value ( "${seal.cm.internal.file.secondpart}" )
	String fileSecondPart;
	
	@Override
	public EntityMetadata ConfigurationGet (String confId) throws Exception {
		// TO BE AWARE of the fileName expected!!!
		
		EntityMetadata internalConf = null;
		
		String fileStringValue;
		try {
			String fileName = confId + fileSecondPart;
			// TO BE AWARE of the fileName expected
			
			fileStringValue = Utils.readFile (internalPath + fileName);
			Gson gson = new Gson();
			
			internalConf = gson.fromJson(fileStringValue, EntityMetadata.class);
			if (internalConf.equals (null))
				throw new Exception(Constants.INTERNAL_CONF_NOT_FOUND); 
				
			
		} catch (IOException e){
			throw new IOException(e);
		} catch (JsonSyntaxException e) {
			throw new JsonSyntaxException(e);
		} catch (Exception e){			
			throw new Exception(e);
		}
		
		return internalConf;
	}

}

