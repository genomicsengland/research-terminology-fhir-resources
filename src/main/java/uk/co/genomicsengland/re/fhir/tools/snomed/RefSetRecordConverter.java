package uk.co.genomicsengland.re.fhir.tools.snomed;

import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.Enumerations;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class RefSetRecordConverter {

    private final Boolean dotNotation;

    RefSetRecordConverter(Boolean dotNotation) {
        this.dotNotation = dotNotation;
    }

    ConceptMap.SourceElementComponent createSourceElement(List<RefSetRecord> records) {
        Set<Integer> multiGroupBlocks = records.stream()
                .filter(record -> record.getMapGroup() > 1)
                .map(RefSetRecord::getMapBlock)
                .collect(Collectors.toSet());
        ConceptMap.SourceElementComponent element = new ConceptMap.SourceElementComponent();
        element.setCode(records.get(0).getReferencedComponentId());
        records.stream()
                // codes from multi-group blocks can be (much) wider and are not useful for our purpose
                .filter(record -> !multiGroupBlocks.contains(record.getMapBlock()))
                // exclude targets such as #NIS, #HLT etc.
                .filter(this::inScope)
                // remove A/D/X/space characters
                .map(this::cleanCode)
                .distinct()
                .map(this::createTargetElement)
                .forEach(element::addTarget);
        return element.getTarget().size() > 0 ? element : null;
    }

    private RefSetRecord cleanCode(RefSetRecord record) {
        String code = record.getMapTarget();
        StringBuilder result = new StringBuilder(code.length());
        for (char c: code.toCharArray()) {
            if (Character.isDigit(c) || result.length() == 0) {
                if (this.dotNotation && result.length() == 3) {
                    result.append('.');
                }
                result.append(c);
            }
        }
        record.setMapTarget(result.toString());
        return record;
    }

    private boolean inScope(RefSetRecord record) {
        String target = record.getMapTarget();
        return target.length() > 0 && target.charAt(0) != '#';
    }

    private ConceptMap.TargetElementComponent createTargetElement(RefSetRecord record) {
        ConceptMap.TargetElementComponent targetElement = new ConceptMap.TargetElementComponent();
        targetElement.setCode(record.getMapTarget());
        if ("447561005".equals(record.getCorrelationId())) {
            // "Correlation not specified"
            targetElement.setEquivalence(Enumerations.ConceptMapEquivalence.RELATEDTO);
        } else {
            throw new RuntimeException("Unknown correlation ID: " + record.getCorrelationId());
        }
        return targetElement;
    }
}
