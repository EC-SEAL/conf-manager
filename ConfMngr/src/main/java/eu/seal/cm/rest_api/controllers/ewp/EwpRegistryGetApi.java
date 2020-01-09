/**
Copyright © 2019  Atos Spain SA, UNIT. All rights reserved.
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
package eu.seal.cm.rest_api.controllers.ewp;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "ewp", description = "the EWP API")

public interface EwpRegistryGetApi
{

    @ApiOperation(value = "Get the EWP Manifest", nickname = "ewpManifestGet", notes = "Get ...", response = String.class, responseContainer = "List", tags = {
            "Registry", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation", response = String.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "Manifest not found") })
    @RequestMapping(value = "/ewp/manifest", produces = {
            "application/xml" }, method = RequestMethod.GET)
    ResponseEntity<String> ewpManifest();

    @ApiOperation(value = "Get the registered SEAL hosts", nickname = "ewpEsmoHostsGet", notes = "Get ...", response = String.class, responseContainer = "List", tags = {
            "Registry", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation", response = String.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "No SEAL hosts found in the registry") })
    @RequestMapping(value = "/ewp/esmoHosts", produces = {
            "application/json" }, method = RequestMethod.GET)
    //ResponseEntity<String> ewpEsmoHosts();
    void ewpEsmoHosts();

    @ApiOperation(value = "Get the SEAL metadata", nickname = "ewpEsmoMetadataGet", notes = "Get ...", response = String.class, responseContainer = "List", tags = {
            "Registry", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation", response = String.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "No SEAL metadata found in the SEAL network") })
    @RequestMapping(value = "/ewp/esmo-metadata", produces = {
            "application/json" }, method = RequestMethod.GET)
    ResponseEntity<String> ewpEsmoMetadata();
    
    public EwpRegistryClient getRegistryClient();

}
