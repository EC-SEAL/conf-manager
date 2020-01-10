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
package eu.seal.cm.rest_api.controllers.mdinternal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonSyntaxException;

import eu.seal.cm.configuration.Constants;
import eu.seal.cm.rest_api.domain.EntityMetadata;
import eu.seal.cm.rest_api.services.mdinternal.ConfigurationGetService;
import io.swagger.annotations.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@javax.annotation.Generated(value = "eu.seal.cm.codegen.languages.SpringCodegen", date = "2018-12-10T12:53:06.421Z")

@Controller
public class ConfigurationGetApiController implements ConfigurationGetApi {
	
	@Autowired
	private ConfigurationGetService configurationGetService;

    private static final Logger log = LoggerFactory.getLogger(ConfigurationGetApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public ConfigurationGetApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }
    
    public ResponseEntity<EntityMetadata> configurationGet(@ApiParam(value = "",required=true) @PathVariable("confId") String confId) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
        	
        	EntityMetadata internalConf = new EntityMetadata();
        	
        	try {
        		internalConf= configurationGetService.ConfigurationGet(confId);
        		return new ResponseEntity<EntityMetadata>(internalConf, HttpStatus.OK);
                }
        	catch (IOException e) {
            	log.error(Constants.FILE_ERROR, e);
        		return new ResponseEntity<EntityMetadata>(HttpStatus.NOT_FOUND);
            }
        	catch (JsonSyntaxException e) {
            	log.error(Constants.JSON_SYNTAX_ERROR, e);
        		return new ResponseEntity<EntityMetadata>(HttpStatus.NOT_FOUND);
            }
            catch (Exception e) {
        		log.error(Constants.INTERNAL_CONF_NOT_FOUND, e);
        		return new ResponseEntity<EntityMetadata>(HttpStatus.NOT_FOUND);
            }
            
        	
        }

        return new ResponseEntity<EntityMetadata>(HttpStatus.NOT_IMPLEMENTED);
    }

}
