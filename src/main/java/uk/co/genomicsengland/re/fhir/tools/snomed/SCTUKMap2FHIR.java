package uk.co.genomicsengland.re.fhir.tools.snomed;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.ConceptMap;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SCTUKMap2FHIR {

    public static void main(String... args) throws IOException {

        /*
         * The input file name (UKMap refset from the SNOMED UK Edition distribution)
         */
        String inputFile = args[0];

        /*
         *  ID of the reference set to be converted to FHIR.
         *
         *  "999002271000000101"; // ICD-10 5 character
         *  "999002261000000108"; // ICD-10 character
         *  "999002741000000101"; // OPCS
         */
        String referenceSetId = args[1];

        /*
         * The output file name. Must be a valid FHIR ConceptMap resource (JSON).
         * The first group element will be overwritten with mappings from the source file.
         */
        String outputFile = args[2];

        ConceptMap map = new SCTUKMap2FHIR().convert(inputFile, referenceSetId, outputFile);

        System.out.println("Writing ConceptMap...");
        Writer writer = new FileWriter(outputFile);
        FhirContext.forR4()
                .newJsonParser().setPrettyPrint(true)
                .encodeResourceToWriter(map, writer);
    }

    private void writeProgress(AtomicInteger value) {
        int i = value.incrementAndGet();
        if (i % 100000 == 0) {
            System.out.println(i / 100000);
        }
    }

    private ConceptMap convert(String file, String referenceSetId, String outputFile) throws IOException {
        RefSetParser parser = new RefSetParser();

        Path path = Paths.get(file);
        try (Stream<String> lines = Files.lines(path)) {
            System.out.println("Parsing and grouping records...");
            AtomicInteger progress = new AtomicInteger(0);
            Map<String, List<RefSetRecord>> result = lines
                    // skip header
                    .skip(1)
                    .peek(line -> writeProgress(progress))
                    // parse each line
                    .map(parser::parse)
                    // filter the map of interest
                    .filter(record -> record.getRefSetId().equals(referenceSetId))
                    // group by source concept
                    .collect(Collectors.groupingBy(RefSetRecord::getReferencedComponentId));

            ConceptMap map = (ConceptMap)FhirContext.forR4().newJsonParser().parseResource(new FileReader(outputFile));
            ConceptMap.ConceptMapGroupComponent group = map.getGroupFirstRep();
            group.setElement(null);

            System.out.println("Building ConceptMap...");
            RefSetRecordConverter converter = new RefSetRecordConverter();
            result.values().stream()
                    .map(converter::createSourceElement)
                    .filter(Objects::nonNull)
                    .forEach(group::addElement);
            return map;
        }
    }

}
