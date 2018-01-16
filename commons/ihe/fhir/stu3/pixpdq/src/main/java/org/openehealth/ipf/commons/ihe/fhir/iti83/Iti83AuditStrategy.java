/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.fhir.iti83;

import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.Type;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.core.atna.event.QueryInformationBuilder;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.commons.ihe.fhir.FhirQueryAuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirEventTypeCodes;
import org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirParticipantObjectIdTypeCodes;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirQueryAuditDataset;

import java.util.Map;

/**
 * Strategy for auditing ITI-83 transactions
 *
 * @author Christian Ohr
 * @since 3.4
 */
public class Iti83AuditStrategy extends FhirQueryAuditStrategy {

    public Iti83AuditStrategy(boolean serverSide) {
        super(serverSide);
    }

    @Override
    public AuditMessage[] makeAuditMessage(AuditContext auditContext, FhirQueryAuditDataset auditDataset) {
        return new QueryInformationBuilder<>(auditContext, auditDataset, FhirEventTypeCodes.MobilePatientIdentifierCrossReferenceQuery)
                .addPatients(auditDataset.getPatientIds())
                .setQueryParameters(
                        "PIXmQuery",
                        FhirParticipantObjectIdTypeCodes.MobilePatientIdentifierCrossReferenceQuery,
                        auditDataset.getQueryString())

                .getMessages();
    }

    @Override
    public FhirQueryAuditDataset enrichAuditDatasetFromRequest(FhirQueryAuditDataset auditDataset, Object request, Map<String, Object> parameters) {
        FhirQueryAuditDataset dataset = super.enrichAuditDatasetFromRequest(auditDataset, request, parameters);

        Parameters params = (Parameters) request;
        if (params != null) {
            Type sourceIdentifier = params.getParameter().stream()
                    .filter(ppc -> Constants.SOURCE_IDENTIFIER_NAME.equals(ppc.getName()))
                    .map(Parameters.ParametersParameterComponent::getValue)
                    .findFirst().orElseThrow(() -> new RuntimeException("No sourceIdentifier in PIX query"));

            if (sourceIdentifier instanceof Identifier) {
                Identifier identifier = (Identifier) sourceIdentifier;
                dataset.getPatientIds().add(String.format("%s|%s", identifier.getSystem(), identifier.getValue()));
            } else if (sourceIdentifier instanceof StringType) {
                StringType identifier = (StringType) sourceIdentifier;
                dataset.getPatientIds().add(identifier.getValue());
            } else {
                dataset.getPatientIds().add(sourceIdentifier.toString());
            }
        }
        return dataset;
    }

}
