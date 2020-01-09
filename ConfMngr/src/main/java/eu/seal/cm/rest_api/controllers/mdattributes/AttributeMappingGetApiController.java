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
package eu.seal.cm.rest_api.controllers.mdattributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonSyntaxException;

import eu.seal.cm.configuration.Constants;
import eu.seal.cm.rest_api.domain.AttributeMapList;
import eu.seal.cm.rest_api.services.mdattributes.AttributeMappingGetService;
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
public class AttributeMappingGetApiController implements AttributeMappingGetApi {
	
	@Autowired
	private AttributeMappingGetService attributeMappingGetService;

    private static final Logger log = LoggerFactory.getLogger(AttributeMappingGetApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public AttributeMappingGetApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<AttributeMapList> attributeMappingGet(@ApiParam(value = "",required=true) @PathVariable("attrProfileId") String attrProfileId) {
        String accept = request.getHeader("Accept");
        
        if (accept != null && accept.contains("application/json")) {
        	AttributeMapList attributeMappings = new AttributeMapList();
        	
        	try {
        		attributeMappings = attributeMappingGetService.attributeMappingGet(attrProfileId);
        		return new ResponseEntity<AttributeMapList>(attributeMappings,HttpStatus.OK);
                }
        	catch (IOException e) {
            	log.error(Constants.FILE_ERROR, e);
        		return new ResponseEntity<AttributeMapList>(HttpStatus.NOT_FOUND);
            }
        	catch (JsonSyntaxException e) {
            	log.error(Constants.JSON_SYNTAX_ERROR, e);
        		return new ResponseEntity<AttributeMapList>(HttpStatus.NOT_FOUND);
            }
            catch (Exception e) {
            	log.error(Constants.ATTRIBUTES_NOT_FOUND, e);
        		return new ResponseEntity<AttributeMapList>(HttpStatus.NOT_FOUND);
            }
        	
        	/*
            try {
                return new ResponseEntity<AttributeTypeList>(objectMapper.readValue("\"\"", AttributeTypeList.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<AttributeTypeList>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            */
        }

        return new ResponseEntity<AttributeMapList>(HttpStatus.NOT_IMPLEMENTED);
    }

    
}
