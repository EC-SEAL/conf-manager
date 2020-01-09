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
package eu.seal.cm.rest_api.controllers.mdexternalentities;

import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eu.seal.cm.rest_api.domain.EntityMetadataList;

@javax.annotation.Generated(value = "eu.seal.cm.codegen.languages.SpringCodegen", date = "2018-12-10T12:53:06.421Z")

@Api(value = "metadata", description = "the metadata API")
public interface ClassEntitiesGetApi {

    
    @ApiOperation(value = "Get the list of entityMetadata objects for all the external entities belonging to a determined set (APs, SPs, IdPS, etc.).", nickname = "classEntitiesGet", notes = "Get ...", response = EntityMetadataList.class, tags={ "ConfigManager", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful operation", response = EntityMetadataList.class),
        @ApiResponse(code = 404, message = "Collection not found") })
    @RequestMapping(value = "/cm/metadata/externalEntities/{collectionId}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<EntityMetadataList> classEntitiesGet(@ApiParam(value = "",required=true) @PathVariable("collectionId") String collectionId);


}
