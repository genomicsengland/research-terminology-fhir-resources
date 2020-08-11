package uk.co.genomicsengland.re.fhir.tools.icdo3;

import org.hl7.fhir.r4.model.CodeSystem;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

class TopoRecordConverter {

    CodeSystem.ConceptDefinitionComponent createSourceElement(List<TopoRecord> records) {

        Map<String, List<TopoRecord>> recordsByCode = records
                .stream()
                .collect(groupingBy(TopoRecord::getCode));

        CodeSystem.ConceptDefinitionComponent concept = new CodeSystem.ConceptDefinitionComponent();
        recordsByCode.keySet().stream().sorted().
                map(recordsByCode::get).forEach(records2 -> {
                    CodeSystem.ConceptDefinitionComponent subConcept = concept.addConcept();
                    records2.stream().sorted().forEach(record -> {
                        String normalisedCode = normaliseCode(record.getCode());
                        switch (record.getLevel()) {
                            case "3":
                                concept.setCode(normalisedCode);
                                concept.setDisplay(record.getTitle());
                                break;
                            case "4":
                                subConcept.setCode(normalisedCode);
                                subConcept.setDisplay(record.getTitle());
                                break;
                            case "incl":
                                CodeSystem.ConceptDefinitionDesignationComponent designation = subConcept.addDesignation();
                                designation.setValue(record.getTitle());
                                break;
                        }
                    });
                });
        return concept;
    }


    private String normaliseCode(String code) {
        return code.replace(".", "");
    }
}