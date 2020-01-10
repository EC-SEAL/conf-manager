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
package eu.seal.cm.rest_api.controllers.mdmicroservices;

import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eu.seal.cm.rest_api.domain.MsMetadataList;

@javax.annotation.Generated(value = "eu.seal.cm.codegen.languages.SpringCodegen", date = "2018-12-10T12:53:06.421Z")

@Api(value = "metadata", description = "the metadata API")
public interface ClassMicroservicesGetApi {

    @ApiOperation(value = "Get the configuration metadata for all microservices of the specified api class.", nickname = "classMicroservicesGet", notes = "Get ...", response = MsMetadataList.class, tags={ "ConfigManager", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful operation", response = MsMetadataList.class),
        @ApiResponse(code = 404, message = "Bad microservice type or none found") })
    @RequestMapping(value = "cm/metadata/microservices/{apiClass}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<MsMetadataList> classMicroservicesGet(@ApiParam(value = "",required=true, 
    allowableValues = "SM, CM, CL, SPCL, RM, SP, AS, IS, PER, IDBOOT, LINK, LINKAPP, REVOKED") @PathVariable("apiClass") String apiClass);
    

}

//public interface ClassMicroservicesGetApi {
//
//    @ApiOperation(value = "Get the configuration metadata for all microservices of the specified api class.", nickname = "classMicroservicesGet", notes = "Get ...", response = MsMetadataList.class, tags={ "Registry", })
//    @ApiResponses(value = { 
//        @ApiResponse(code = 200, message = "Successful operation", response = MsMetadataList.class),
//        @ApiResponse(code = 404, message = "Bad microservice type or none found") })
//    @RequestMapping(value = "/metadata/microservices",
//        produces = { "application/json" }, 
//        method = RequestMethod.GET)
//    ResponseEntity<MsMetadataList> classMicroservicesGet(@RequestParam(value = "apiClass",required=true) String apiClass);
//    
//}
