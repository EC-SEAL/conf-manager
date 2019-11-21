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
package eu.seal.cm.configuration;

import eu.seal.cm.filters.HttpSignatureFilter;
import eu.seal.cm.httpSig_api.HttpSignatureService;
import eu.seal.cm.httpSig_api.HttpSignatureServiceImpl;
import eu.seal.cm.params_api.KeyStoreService;
import eu.seal.cm.rest_api.controllers.ewp.EwpRegistryGetApiController;
import eu.seal.cm.rest_api.services.mdmicroservices.AllMicroservicesGetService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.spec.InvalidKeySpecException;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 *
 * @authors UAegean, Atos
 */
@Configuration
public class CustomWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    private HttpSignatureService sigServ;
    
    private KeyStoreService keysServ;
    
    private AllMicroservicesGetService allMicroservicesServ;
    
    EwpRegistryGetApiController ewpRegistryServ;
    
    @Autowired
    public CustomWebSecurityConfigurerAdapter(	KeyStoreService keysServ, 
    											AllMicroservicesGetService allMicroservicesServ,
    											EwpRegistryGetApiController ewpRegistryServ) throws KeyStoreException, UnsupportedEncodingException, NoSuchAlgorithmException, UnrecoverableKeyException, InvalidKeySpecException, IOException {

        this.sigServ = new HttpSignatureServiceImpl(DigestUtils.sha256Hex(keysServ.getHttpSigPublicKey().getEncoded()), keysServ.getHttpSigningKey());
        this.allMicroservicesServ = allMicroservicesServ;
        this.keysServ = keysServ;
        
        this.ewpRegistryServ = ewpRegistryServ;
        

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
    	
    	/*
        //http.antMatcher("/cm/**")
    	http.requestMatchers()
    			//.antMatchers("/cm/**","/ewp/seal-metadata")
    			.antMatchers("/cm/**","/ewp/seal-metadata", "/ewp/sealHosts")
    			.and()
            .addFilterBefore(new HttpSignatureFilter(this.sigServ, this.allMicroservicesServ, this.ewpRegistryClient), BasicAuthenticationFilter.class)
        	.csrf().disable();
    	*/
    	
    	http.antMatcher("/**")
    		.addFilterBefore(new HttpSignatureFilter(this.sigServ, this.allMicroservicesServ, this.ewpRegistryServ), BasicAuthenticationFilter.class)
    		.csrf().disable();
    	
    }

 

}
