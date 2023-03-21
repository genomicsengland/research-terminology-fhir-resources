package uk.co.genomicsengland.re.fhir.tools.snomed;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.ConceptMap;

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

        if (args.length != 4) {
            System.out.println("Invalid number of parameters");
            System.exit(1);
        }

        /*
         * The input file name (UKMap refset from the SNOMED UK Edition distribution)
         * E.g.: ./src/main/resources/snomed/der2_iisssciRefset_ExtendedMapUKCLSnapshot_GB1000000_20230215.txt
         */
        String inputFile = args[0];

        /*
         *  ID of the reference set (refsetId) to be converted to FHIR.
         *
         *  "999002271000000101"; // SNOMED to ICD-10 5th edition five character
         *  "999002741000000101"; // SNOMED to OPCS 4.8 (retired - not in latest distribution)
         *  "1126441000000105" // SNOMED to OPCS 4.9
         *  "1382401000000109" // SNOMED to OPCS 4.10
         */
        String referenceSetId = args[1];

        /*
         * The output file name.
         */
        String outputFile = args[2];

        /*
         * Whether to insert a dot between the 3rd and 4th character in the target (true) or not (false)
         */
        Boolean dotNotation = Boolean.parseBoolean(args[3]);

        ConceptMap map = new SCTUKMap2FHIR().convert(inputFile, referenceSetId, outputFile, dotNotation);

        System.out.println("Writing ConceptMap...");
        Writer writer = new FileWriter(outputFile);
        FhirContext.forR4()
                .newJsonParser().setPrettyPrint(true)
                .encodeResourceToWriter(map, writer);
    }

    private void writeProgress(AtomicInteger value) {
        int i = value.incrementAndGet();
        if (i % 100000 == 0) {
            System.out.printf("%s00K%n", i / 100000);
        }
    }

    private ConceptMap convert(String file, String referenceSetId, String outputFile, Boolean dotNotation) throws IOException {
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

            ConceptMap map = new ConceptMap();
            ConceptMap.ConceptMapGroupComponent group = map.getGroupFirstRep();
            group.setElement(null);

            System.out.println("Building ConceptMap...");
            RefSetRecordConverter converter = new RefSetRecordConverter(dotNotation);
            result.values().stream()
                    .map(converter::createSourceElement)
                    .filter(Objects::nonNull)
                    .forEach(group::addElement);
            return map;
        }
    }

}
