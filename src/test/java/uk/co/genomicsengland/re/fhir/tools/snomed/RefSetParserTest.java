package uk.co.genomicsengland.re.fhir.tools.snomed;


import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RefSetParserTest {


    @Test
    public void parseTest() {
        String line = "11331673-4ecb-5c0c-b9cc-e65cda6af90c\t20181001\t1\t999000031000000106\t999002741000000101\t367336001\t1\t2\t\tALWAYS X38.4 | ADDITIONAL CODE POSSIBLE\tX384\t447561005\t1";
        RefSetRecord record = new RefSetParser().parse(line);
        assertEquals("11331673-4ecb-5c0c-b9cc-e65cda6af90c", record.getId());
        assertEquals(LocalDate.parse("2018-10-01"), record.getEffectiveTime());
        assertEquals("999000031000000106", record.getModuleId());
        assertEquals("999002741000000101", record.getRefSetId());
        assertEquals("367336001", record.getReferencedComponentId());
        assertEquals(1, record.getMapGroup().intValue());
        assertEquals(2, record.getMapPriority().intValue());
        assertEquals("", record.getMapRule());
        assertEquals("ALWAYS X38.4 | ADDITIONAL CODE POSSIBLE", record.getMapAdvice());
        assertEquals("X384", record.getMapTarget());
        assertEquals("447561005", record.getCorrelationId());
        assertEquals(1, record.getMapBlock().intValue());
    }

}
