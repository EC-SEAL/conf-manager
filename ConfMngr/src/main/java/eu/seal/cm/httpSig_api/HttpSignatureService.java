/**
Copyright © 2019  Atos Spain SA, UAegean. All rights reserved.
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
package eu.seal.cm.httpSig_api;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import javax.servlet.http.HttpServletRequest;

import eu.seal.cm.rest_api.controllers.ewp.EwpRegistryGetApiController;
import eu.seal.cm.rest_api.domain.HttpResponseEnum;
import eu.seal.cm.rest_api.domain.MsMetadataList;
/**
 *
 * @author UAegean, Atos
 */
public interface HttpSignatureService {

	public HttpResponseEnum verifySignature(HttpServletRequest httpRequest, MsMetadataList allMicroservices, EwpRegistryGetApiController ewpRegistryServ) ;
	
    public String generateSignature(String hostUrl, String method, String uri, Object postParams, String contentType, String requestId)
            throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, UnsupportedEncodingException, IOException, UnsupportedEncodingException;
     
}
