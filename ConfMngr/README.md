# Configuration Manager microservice

Reference *DELIVERABLE D2.1*

## Overview  

Back channel interface. It allows retrieving configuration items from a central repository.

The code is structured to have a clear MVC structure.
Notice the use of the OperationId(s) of the corresponding swagger specs.

The underlying library integrating swagger to SpringBoot is [springfox](https://github.com/springfox/springfox)

The following environment variables are to be set:

            - KEYSTORE_PATH
            - KEY_PASS
            - STORE_PASS
            - HTTPSIG_CERT_ALIAS
            - SIGNING_SECRET
            - ASYNC_SIGNATURE=true
            - SSL_KEYSTORE_PATH
            - SSL_STORE_PASS
            - SSL_KEY_PASS
            - SSL_CERT_ALIAS
  

For testing, try https://localhost:8433/swagger-ui.html          


