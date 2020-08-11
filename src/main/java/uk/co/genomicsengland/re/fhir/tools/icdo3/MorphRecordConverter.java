package uk.co.genomicsengland.re.fhir.tools.icdo3;

import org.hl7.fhir.r4.model.CodeSystem;

import java.util.List;

class MorphRecordConverter {

    org.hl7.fhir.r4.model.CodeSystem.ConceptDefinitionComponent createSourceElement(List<MorphRecord> records) {
        CodeSystem.ConceptDefinitionComponent concept = new CodeSystem.ConceptDefinitionComponent();
        for (MorphRecord record : records) {
            switch (record.getStruct()) {
                case "title":
                    concept.setCode(record.getCode().replace("/",""));
                    concept.setDisplay(record.getLabel());
                    break;
                case "sub":
                    CodeSystem.ConceptDefinitionDesignationComponent designation = concept.addDesignation();
                    designation.setValue(record.getLabel());
            }
        }
        return concept;
    }
}