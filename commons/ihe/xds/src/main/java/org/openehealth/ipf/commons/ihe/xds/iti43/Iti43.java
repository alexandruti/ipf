/*
 * Copyright 2009 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.xds.iti43;

import javax.xml.namespace.QName;

import org.openehealth.ipf.commons.ihe.xds.core.XdsClientFactory;
import org.openehealth.ipf.commons.ihe.xds.core.XdsServiceFactory;
import org.openehealth.ipf.commons.ihe.xds.core.XdsServiceInfo;

/**
 * Provides access to the service and client factories for ITI-43.
 * @author Jens Riemschneider
 */
public class Iti43 {
    private final static XdsServiceInfo ITI_43 = new XdsServiceInfo(
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRepository_Service", "ihe"),
            Iti43PortType.class,
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRepository_Binding_Soap12", "ihe"),
            true,
            "wsdl/iti43.wsdl",
            true,
            false,
            false);
           
    /**
     * Returns a factory for client stubs of the ITI-43 service.
     * @param audit
     *          <code>true</code> to enable auditing.
     * @param allowIncompleteAudit
     *          <code>true</code> if audit entries are logged even if not all 
     *          necessary data is available. 
     * @param serviceUrl
     *          the URL of the service to use.
     * @return the client factory.
     */
    public static XdsClientFactory getClientFactory(boolean audit, boolean allowIncompleteAudit, String serviceUrl) {        
        return new XdsClientFactory(ITI_43, audit ? new Iti43ClientAuditStrategy(allowIncompleteAudit) : null, serviceUrl);
    }

    /**
     * Returns a factory for ITI-43 services.
     * @param audit
     *          <code>true</code> to enable auditing.
     * @param allowIncompleteAudit
     *          <code>true</code> if audit entries are logged even if not all 
     *          necessary data is available. 
     * @param serviceAddress
     *          the address used to publish the service in the current context.  
     * @return the service factory.
     */
    public static XdsServiceFactory getServiceFactory(boolean audit, boolean allowIncompleteAudit, String serviceAddress) {
        return new XdsServiceFactory(ITI_43, audit ? new Iti43ServerAuditStrategy(allowIncompleteAudit) : null, serviceAddress);
    }
}