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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@javax.annotation.Generated(value = "eu.seal.cm.codegen.languages.SpringCodegen", date = "2018-12-10T12:53:06.421Z")

@Configuration
public class SwaggerDocumentationConfig {

    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("Configuration Manager")
            .description("**TO BE UPDATED:** \n The ConfMngr is in charge of providing the configuration sets needed by each ms in the SEAL deployment which includes:  \n" + 
            		"  \n" + 
            		"  *  The list of microservices:\n" + 
            		"  -- ms identifier\n" + 
            		"  -- type (i.e. SessionMngr, ConfMngr, ACM, SP, AP, IDP, GW2GW, etc.)\n" + 
            		"  -- Public keys of the ms (to be used on the client authentication in HTTP Signature)\n" + 
            		"\n" + 
            		"  -- Published APIs:\n" + 
            		"  -- API class: SessionMngr, ConfMngr, ACM, SP, AP, IDP, GW2GW  \n" + 
            		"  -- API call: e.g. attrRequest, or attrResponse  \n" + 
            		"  -- API connection type: post, get\n" + 
            		"  -- API endpoint: its URL\n" + 
            		"  \n" + 
            		"  * The list of external entities: SPs, local APs, IdPs, remote GWs, remote APs.\n" + 
            		"  \n" + 
            		"  * Internal entities: local GW configuration at the moment.\n" + 
            		"  \n" + 
            		"  * List of attributes: provided by eIDAS, eduPerson, eduOrg, schac...\n" + 
            		"  \n" + 
            		"  * EWP registry processing. \n" + 
            		"  \n" + 
            		"  \n" + 
            		"  Note: each ms will be in charge of configuring their own CA signed certificates for server TLS.\n")
            .license("")
            .licenseUrl("http://unlicense.org")
            .termsOfServiceUrl("")
            .version("0.0.1")
            .contact(new Contact("","", ""))
            .build();
    }

    @Bean
    public Docket customImplementation(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                    .apis(RequestHandlerSelectors.basePackage("eu.seal.cm.rest_api"))
                    .build()
                .directModelSubstitute(org.threeten.bp.LocalDate.class, java.sql.Date.class)
                .directModelSubstitute(org.threeten.bp.OffsetDateTime.class, java.util.Date.class)
                .apiInfo(apiInfo());
    }
    
//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.any())
//                .paths(PathSelectors.any())
//                .build()
//                .apiInfo(apiInfo());
//    }


}
