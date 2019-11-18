package uk.co.genomicsengland.re.fhir.tools.snomed;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

class RefSetParser {

    private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder().appendPattern("yyyyMMdd").toFormatter();

    RefSetRecord parse(String line) {
        String[] parts = line.split("\t");
        String id = parts[0].trim();
        LocalDate effectiveTime = FORMATTER.parse(parts[1].trim(), LocalDate::from);
        Boolean active = Boolean.parseBoolean(parts[2].trim());
        String moduleId = parts[3].trim();
        String refSetId = parts[4].trim();
        String referencedComponentId = parts[5].trim();
        Integer mapGroup = Integer.parseInt(parts[6].trim());
        Integer mapPriority = Integer.parseInt(parts[7].trim());
        String mapRule = parts[8].trim();
        String mapAdvice = parts[9].trim();
        String mapTarget = parts[10].trim();
        String correlationId = parts[11].trim();
        Integer mapBlock = Integer.parseInt(parts[12].trim());
        return new RefSetRecord(id, effectiveTime, active, moduleId, refSetId, referencedComponentId, mapGroup, mapPriority, mapRule, mapAdvice, mapTarget, correlationId, mapBlock);
    }
}
