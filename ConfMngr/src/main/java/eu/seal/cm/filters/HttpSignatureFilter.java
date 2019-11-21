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
package eu.seal.cm.filters;

import eu.seal.cm.httpSig_api.HttpSignatureService;
import eu.seal.cm.rest_api.controllers.ewp.EwpRegistryGetApiController;
import eu.seal.cm.rest_api.domain.HttpResponseEnum;
import eu.seal.cm.rest_api.services.mdmicroservices.AllMicroservicesGetService;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
//import java.security.KeyStoreException;
//import java.security.NoSuchAlgorithmException;
//import java.security.UnrecoverableKeyException;
import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.GenericFilterBean;

/**
 *
 * @author nikos, Atos
 */
public class HttpSignatureFilter extends GenericFilterBean {

    private final HttpSignatureService sigServ;
    private final AllMicroservicesGetService allMicroservicesServ;
    private final EwpRegistryGetApiController ewpRegistryServ;
    
    private final Logger Logger = LoggerFactory.getLogger(HttpSignatureFilter.class);

    @Autowired
    //public HttpSignatureFilter(HttpSignatureService sigServ, AllMicroservicesGetService allMicroservicesServ) {
    public HttpSignatureFilter(HttpSignatureService sigServ, AllMicroservicesGetService allMicroservicesServ, EwpRegistryGetApiController ewpRegistryServ) {
    	this.sigServ = sigServ;
        this.allMicroservicesServ = allMicroservicesServ;
        this.ewpRegistryServ = ewpRegistryServ;
    }

    
    @Value("${seal.cm.httpsig.enabled}")
    boolean httpsigEnabled;
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        try {

            final HttpServletRequest currentRequest = (HttpServletRequest) request;

            if (currentRequest.getMethod().toLowerCase().equals("post")) {
            	
            	//Logger.info("Post methods...");
            	
                MultiReadHttpServletRequest multiReadRequest = new MultiReadHttpServletRequest(currentRequest);
                boolean result = sigServ.verifySignature ((HttpServletRequest) multiReadRequest, allMicroservicesServ.allMicroservicesGet(), null).equals(HttpResponseEnum.AUTHORIZED);
                if (result) {
                    chain.doFilter(multiReadRequest, response);
                } else {
                    throw new ServletException("POST methods: Error Validating Http Signature from request");
                }

            } else {
            	if (currentRequest.getRequestURL().indexOf("/ewp/") == -1) { // Verifying with the keys specified in the current node.
            		
            		Logger.info("cm/metadata methods...");
            		
            		boolean result;
            		if (httpsigEnabled) {
            			result = sigServ.verifySignature ((HttpServletRequest) request, allMicroservicesServ.allMicroservicesGet(), null).equals(HttpResponseEnum.AUTHORIZED);
	                
            		} else result = true; // Only testing in local
            		
            		if (result) {
	                    chain.doFilter(request, response);
	                } else {
	                    throw new ServletException("Error Validating Http Signature from request");
	                }
            	}
                else {  // Verifying with the key specified in the Manifest.
                	
                	Logger.info("ewp methods...");
                	
                	if (currentRequest.getRequestURL().indexOf("/ewp/manifest") == -1) {
                	
		                boolean result = sigServ.verifySignature ((HttpServletRequest) request, null, ewpRegistryServ).equals(HttpResponseEnum.AUTHORIZED);
		                if (result) {
		                    chain.doFilter(request, response);
		                } else {
		                    throw new ServletException("EWP: Error Validating Http Signature from request");
		                }
                	}
                	else { // Without verifying anything
                		Logger.info("ewp/manifest ...");
                		chain.doFilter(request, response);
                	
                	}
                }

            }

//        } catch (KeyStoreException ex) {
//            Logger.error(ex.getMessage());
//        } catch (NoSuchAlgorithmException ex) {
//            Logger.error(ex.getMessage());
//        } catch (UnrecoverableKeyException ex) {
//            Logger.error(ex.getMessage());
        } catch (UnsupportedEncodingException ex) {
            Logger.error(ex.toString());
        } catch (Exception ex) {
        	Logger.error(ex.toString());
    }

    }

    public class MultiReadHttpServletRequest extends HttpServletRequestWrapper {

        private ByteArrayOutputStream cachedBytes;

        public MultiReadHttpServletRequest(HttpServletRequest request) {
            super(request);
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            if (cachedBytes == null) {
                cacheInputStream();
            }

            return new CachedServletInputStream();
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(getInputStream()));
        }

        private void cacheInputStream() throws IOException {
            /* Cache the inputstream in order to read it multiple times. For
     * convenience, I use apache.commons IOUtils
             */
            cachedBytes = new ByteArrayOutputStream();
            IOUtils.copy(super.getInputStream(), cachedBytes);
        }

        /* An inputstream which reads the cached request body */
        public class CachedServletInputStream extends ServletInputStream {

            private ByteArrayInputStream input;

            public CachedServletInputStream() {
                /* create a new input stream from the cached request body */
                input = new ByteArrayInputStream(cachedBytes.toByteArray());
            }

            @Override
            public int read() throws IOException {
                return input.read();
            }

            @Override
            public boolean isFinished() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public boolean isReady() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void setReadListener(ReadListener rl) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }
    }

}
