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

import io.swagger.annotations.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eu.seal.cm.rest_api.domain.EntityMetadata;

@javax.annotation.Generated(value = "eu.seal.cm.codegen.languages.SpringCodegen", date = "2018-12-10T12:53:06.421Z")

@Api(value = "metadata", description = "the metadata API")
public interface ConfigurationGetApi {

    
	@ApiOperation(value = "Get the configuration data for a given internal entity (the local GW, at the moment).", nickname = "configurationGet", notes = "Get ...", response = EntityMetadata.class, 
			//authorizations = { @Authorization(value = "httpSig")},
			tags={ "ConfigManager", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful operation", response = EntityMetadata.class),
        @ApiResponse(code = 404, message = "Internal configuration not found") })
    @RequestMapping(value = "/cm/metadata/internal/{confId}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<EntityMetadata> configurationGet(@ApiParam(value = "",required=true) @PathVariable("confId") String confId);


}
