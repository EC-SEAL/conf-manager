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

import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eu.seal.cm.rest_api.domain.AttributeMapList;
import eu.seal.cm.rest_api.domain.AttributeTypeList;

@Api(value = "metadata", description = "the metadata API")
public interface AttributeMappingGetApi {

    @ApiOperation(value = "Get the attribute set for the profile just specified.", nickname = "attributeMappingGet", notes = "Get ...", response = AttributeTypeList.class, tags={ "ConfigManager", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful operation", response = AttributeMapList.class),
        @ApiResponse(code = 404, message = "Attribute Profile not found") })
    @RequestMapping(value = "/cm/metadata/attributes/{attrProfileId}/maps",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<AttributeMapList> attributeMappingGet(@ApiParam(value = "",required=true) @PathVariable("attrProfileId") String attrProfileId);



}
