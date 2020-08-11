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

public class TopoTSVConverter {

    public static void main(String... args) throws IOException {
        String inputFile = args[0];
        String outputFile = args[1];
        CodeSystem codeSystem = new TopoTSVConverter().convert(inputFile, outputFile);
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
        TopoRecordParser parser = new TopoRecordParser();

        Path path = Paths.get(file);
        try (Stream<String> lines = Files.lines(path)) {
            System.out.println("Parsing and grouping records...");
            AtomicInteger progress = new AtomicInteger(0);
            Map<String, List<TopoRecord>> result = lines
                    .skip(1)
                    .peek(line -> writeProgress(progress))
                    .map(parser::parse)
                    .collect(Collectors.groupingBy(record -> record.getCode().substring(0, 3)));

            CodeSystem codeSystem;
            File out = new File(outputFile);
            if (out.exists()) {
                codeSystem = (CodeSystem) FhirContext.forR4().newJsonParser().parseResource(new FileReader(outputFile));
                codeSystem.setConcept(null);
            } else {
                codeSystem = new CodeSystem();
            }

            System.out.println("Building CodeSystem...");
            TopoRecordConverter converter = new TopoRecordConverter();
            result.keySet().stream()
                    .sorted()
                    .map(result::get)
                    .map(converter::createSourceElement)
                    .filter(Objects::nonNull)
                    .forEach(codeSystem::addConcept);
            return codeSystem;
        }
    }

}
