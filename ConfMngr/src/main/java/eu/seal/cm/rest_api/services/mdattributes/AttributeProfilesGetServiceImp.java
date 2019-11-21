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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import eu.seal.cm.configuration.Constants;

@Service
public class AttributeProfilesGetServiceImp implements AttributeProfilesGetService{
	
	@Value ( "${seal.cm.attributes.path}" )
	String msFile;
	
	@Value ( "${seal.cm.files.extension}" )
	String fileExtension;
	
	@Override
	public List<String> attributeProfilesGet ()  throws Exception {
		// TO BE AWARE of the directory name expected!!!
		
		File dir = new File(msFile);
		String[] files = dir.list();
		
		List<String> profiles = new ArrayList<String>();
		
		if (files != null && files.length != 0) {
			  String fileName = new String();			 
			  for (int x=0;x<files.length;x++) {
				  fileName = files[x];
				  
				  profiles.add(fileName.substring (0,fileName.indexOf(fileExtension)));
			  }
			    
		}
		else // Empty directory
			throw new Exception (Constants.PROFILES_NOT_FOUND);
		
		return (profiles);
	}

}

