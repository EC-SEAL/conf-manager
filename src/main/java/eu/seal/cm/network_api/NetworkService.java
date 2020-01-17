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
package eu.seal.cm.network_api;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.apache.commons.httpclient.NameValuePair;


/**
 *
 * @author UAegean
 */
public interface NetworkService {

	
	public String sendGetURIParams(String hostUrl, String uri, List<NameValuePair> urlParameters, int attempt) throws IOException, NoSuchAlgorithmException;

    public String sendGet(String hostUrl, String uri, List<NameValuePair> urlParameters, int attempt) throws IOException, NoSuchAlgorithmException;

    public String sendPostForm(String hostUrl, String uri, List<NameValuePair> urlParameters, int attempt) throws IOException, NoSuchAlgorithmException;

    public String sendPostBody(String hostUrl, String uri, Object postBody, String contentType, int attempt) throws IOException, NoSuchAlgorithmException;



}
