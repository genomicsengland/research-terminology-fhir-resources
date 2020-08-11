package uk.co.genomicsengland.re.fhir.tools.icdo3;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.CodeSystem;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MorphTSVConverter {

    public static void main(String... args) throws IOException {
        String inputFile = args[0];
        String outputFile = args[1];
        CodeSystem codeSystem = new MorphTSVConverter().convert(inputFile, outputFile);
        System.out.println("Writing CodeSystem...");
        Writer writer = new FileWriter(outputFile);
        FhirContext.forR4()
                .newJsonParser().setPrettyPrint(true)
                .encodeResourceToWriter(codeSystem, writer);
    }

    private void writeProgress(AtomicInteger value) {
        int i = value.incrementAndGet();
        if (i % 100000 == 0) {
            System.out.println(i / 100000);
        }
    }


    private CodeSystem convert(String file,  String outputFile) throws IOException {
        MorphRecordParser parser = new MorphRecordParser();

        Path path = Paths.get(file);
        try (Stream<String> lines = Files.lines(path)) {
            System.out.println("Parsing and grouping records...");
            AtomicInteger progress = new AtomicInteger(0);
            Map<String, List<MorphRecord>> result = lines
                    .skip(1)
                    .peek(line -> writeProgress(progress))
                    .map(parser::parse)
                    .collect(Collectors.groupingBy(MorphRecord::getCode));

            CodeSystem codeSystem;
            File out = new File(outputFile);
            if (out.exists()) {
                codeSystem = (CodeSystem) FhirContext.forR4().newJsonParser().parseResource(new FileReader(outputFile));
                codeSystem.setConcept(null);
            } else {
                codeSystem = new CodeSystem();
            }

            System.out.println("Building CodeSystem...");
            MorphRecordConverter converter = new MorphRecordConverter();
            result.keySet().stream().sorted()
                    .map(result::get)
                    .map(converter::createSourceElement)
                    .filter(Objects::nonNull)
                    .forEach(codeSystem::addConcept);
            return codeSystem;
        }
    }

}
