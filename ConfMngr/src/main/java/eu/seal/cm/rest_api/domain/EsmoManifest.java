/**
Copyright © 2019  UJI. All rights reserved.
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
package eu.seal.cm.rest_api.domain;

// Gateway and local APs info
public class EsmoManifest
{
    private EntityMetadata gateway;

    private EntityMetadataList proxiedEntities;

    public EntityMetadata getGateway()
    {
        return gateway;
    }

    public void setGateway(EntityMetadata gateway)
    {
        this.gateway = gateway;
    }

    public EntityMetadataList getProxiedEntities()
    {
        return proxiedEntities;
    }

    public void setProxiedEntities(EntityMetadataList proxiedEntities)
    {
        this.proxiedEntities = proxiedEntities;
    }
}
