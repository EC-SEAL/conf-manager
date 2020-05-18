# conf-manager
Configuration Manager microservice

## Overview  

Back channel interface. It allows retrieving configuration items from a central repository.

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

## Configuration files

A keystore.jks is provided in the **/resources/testKeys** directory.

The **microservices** configuration json file are allocated at **/resources**. Find msMetadataList.json as an example. 

The json files describing the different **attribute schemas**, are put at **/resources/attributeLists**. Examples provided with the deployment are: eduOrg.json, eduPerson.json, eIDAS.json, schac.json.

The json files with the **external entities** are found at **/resources/externalEntities**. Some examples are deployed: ATTRSOURCEmetadata.json, AUTHSOURCEmetadata.json, EDUGAINmetadata.json, EIDASmetadata.json, PERSISTENCEmetadata.json. 

The json file with the *internal* configuration can be found at */resources/internal*. A LGW_config.json is deployed. [TO BE REMOVED]

Customize the above files before starting your CM. If the CM is running already, to change those data:
```
sudo docker cp /yourDirectory/yourConfData/msMetadataList.json confMngr_ps_num:/resources/
sudo docker cp /yourDirectory/yourConfData/EIDASmetadata.json confMngr_ps_num:/resources/externalEntities
etc.
```


## Docker container: mvjatos/seal-cm (check for the current version)

The following versions are available:

|**Tag**|**Description**|
| ------ | ------ |
| 0.0.1| First version.|
| ...| ...| 



## docker-compose example

```
ConfManager:
      image: mvjatos/seal-cm:0.0.1 
      environment:
            - KEYSTORE_PATH=/resources/testKeys/keystore.jks
            - KEY_PASS=xxxxx
            - STORE_PASS=xxxxx
            - HTTPSIG_CERT_ALIAS=xxxxx
            - SIGNING_SECRET=xxxxx
            - ASYNC_SIGNATURE=true
            - SSL_KEYSTORE_PATH=/resources/keystoreatos.jks
            - SSL_STORE_PASS=xxxxx
            - SSL_KEY_PASS=xxxxx
            - SSL_CERT_ALIAS=xxxxx
        volumes:
            - /SEAL/CM/resources:/resources
        ports:
            - 9083:8083
