package uk.co.genomicsengland.re.fhir.tools.snomed;

import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.Enumerations;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RefSetRecordConverterTest {

    @Test
    void createSourceElementMultiGroupTest() {
        RefSetRecord record1 = new RefSetRecord("1", null, null, null, null,
                "123456", 1, null, null, null,
                "D011", "447561005", null);
        RefSetRecord record2 = new RefSetRecord("1", null, null, null, null,
                "123456", 2, null, null, null,
                "Z012", "447561005", null);
        ConceptMap.SourceElementComponent result = new RefSetRecordConverter().createSourceElement(Arrays.asList(record1, record2));
        assertNull(result);
    }

    @Test
    void createSourceElementMultiBlockTest() {
        RefSetRecord record1 = new RefSetRecord("1", null, null, null, null,
                "123456", 1, null, null, null,
                "D011", "447561005", 1);
        RefSetRecord record2 = new RefSetRecord("1", null, null, null, null,
                "123456", 2, null, null, null,
                "Z012", "447561005", 1);
        RefSetRecord record3 = new RefSetRecord("1", null, null, null, null,
                "123456", 1, null, null, null,
                "E140", "447561005", 2);
        RefSetRecord record4 = new RefSetRecord("1", null, null, null, null,
                "123456", 1, null, null, null,
                "E141", "447561005", 2);

        ConceptMap.SourceElementComponent result = new RefSetRecordConverter().createSourceElement(Arrays.asList(record1, record2, record3, record4));
        assertEquals("123456", result.getCode());
        List<ConceptMap.TargetElementComponent> targets = result.getTarget();
        assertEquals(2, targets.size());
        ConceptMap.TargetElementComponent target = targets.get(0);
        assertEquals("E140", target.getCode());
        assertEquals(Enumerations.ConceptMapEquivalence.RELATEDTO, target.getEquivalence());
        target = targets.get(1);
        assertEquals("E141", target.getCode());
        assertEquals(Enumerations.ConceptMapEquivalence.RELATEDTO, target.getEquivalence());
    }

    @Test
    void createSourceElementTest() {
        RefSetRecord record1 = new RefSetRecord("1", null, null, null, null,
                "123456", 0, null, null, null,
                "D011", "447561005", null);
        RefSetRecord record2 = new RefSetRecord("1", null, null, null, null,
                "123456", 0, null, null, null,
                "D012", "447561005", null);
        ConceptMap.SourceElementComponent result = new RefSetRecordConverter().createSourceElement(Arrays.asList(record1, record2));
        assertEquals("123456", result.getCode());
        List<ConceptMap.TargetElementComponent> targets = result.getTarget();
        assertEquals(2, targets.size());
        ConceptMap.TargetElementComponent target = targets.get(0);
        assertEquals("D011", target.getCode());
        assertEquals(Enumerations.ConceptMapEquivalence.RELATEDTO, target.getEquivalence());
        target = targets.get(1);
        assertEquals("D012", target.getCode());
        assertEquals(Enumerations.ConceptMapEquivalence.RELATEDTO, target.getEquivalence());
    }


    @Test
    void createSourceElementXTest() {
        RefSetRecord record = new RefSetRecord("1", null, null, null, null,
                "123456", 0, null, null, null,
                "G20X", "447561005", null);
        ConceptMap.SourceElementComponent result = new RefSetRecordConverter().createSourceElement(Collections.singletonList(record));
        assertEquals("123456", result.getCode());
        List<ConceptMap.TargetElementComponent> targets = result.getTarget();
        ConceptMap.TargetElementComponent target = targets.get(0);
        assertEquals("G20", target.getCode());
        assertEquals(Enumerations.ConceptMapEquivalence.RELATEDTO, target.getEquivalence());
    }

    @Test
    void createSourceElementATest() {
        RefSetRecord record = new RefSetRecord("1", null, null, null, null,
                "123456", 0, null, null, null,
                "M001 A", "447561005", null);
        ConceptMap.SourceElementComponent result = new RefSetRecordConverter().createSourceElement(Collections.singletonList(record));
        assertEquals("123456", result.getCode());
        List<ConceptMap.TargetElementComponent> targets = result.getTarget();
        ConceptMap.TargetElementComponent target = targets.get(0);
        assertEquals("M001", target.getCode());
        assertEquals(Enumerations.ConceptMapEquivalence.RELATEDTO, target.getEquivalence());
    }

    @Test
    void createSourceElementDuplicateTest() {
        RefSetRecord record1 = new RefSetRecord("1", null, null, null, null,
                "123456", 0, null, null, null,
                "M001", "447561005", null);
        RefSetRecord record2 = new RefSetRecord("1", null, null, null, null,
                "123456", 0, null, null, null,
                "M001 A", "447561005", null);
        ConceptMap.SourceElementComponent result = new RefSetRecordConverter().createSourceElement(Arrays.asList(record1, record2));
        assertEquals("123456", result.getCode());
        List<ConceptMap.TargetElementComponent> targets = result.getTarget();
        assertEquals(1, targets.size());
        ConceptMap.TargetElementComponent target = targets.get(0);
        assertEquals("M001", target.getCode());
        assertEquals(Enumerations.ConceptMapEquivalence.RELATEDTO, target.getEquivalence());
    }

    @Test
    void createSourceElementStartATest() {
        RefSetRecord record = new RefSetRecord("1", null, null, null, null,
                "123456", 0, null, null, null,
                "A123", "447561005", null);
        ConceptMap.SourceElementComponent result = new RefSetRecordConverter().createSourceElement(Collections.singletonList(record));
        assertEquals("123456", result.getCode());
        List<ConceptMap.TargetElementComponent> targets = result.getTarget();
        ConceptMap.TargetElementComponent target = targets.get(0);
        assertEquals("A123", target.getCode());
        assertEquals(Enumerations.ConceptMapEquivalence.RELATEDTO, target.getEquivalence());
    }
}
