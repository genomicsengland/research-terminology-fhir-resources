package uk.co.genomicsengland.re.fhir.tools.snomed;

import java.time.LocalDate;
import java.util.Objects;

class RefSetRecord {
    private final String id;
    private final LocalDate effectiveTime;
    private final Boolean active;
    private final String moduleId;
    private final String refSetId;
    private final String referencedComponentId;
    private final Integer mapGroup;
    private final Integer mapPriority;
    private final String mapRule;
    private final String mapAdvice;
    private String mapTarget;
    private final String correlationId;
    private final Integer mapBlock;

    RefSetRecord(String id, LocalDate effectiveTime, Boolean active, String moduleId, String refSetId, String referencedComponentId, Integer mapGroup, Integer mapPriority, String mapRule, String mapAdvice, String mapTarget, String correlationId,
                 Integer mapBlock) {
        this.id = id;
        this.effectiveTime = effectiveTime;
        this.active = active;
        this.moduleId = moduleId;
        this.refSetId = refSetId;
        this.referencedComponentId = referencedComponentId;
        this.mapGroup = mapGroup;
        this.mapPriority = mapPriority;
        this.mapRule = mapRule;
        this.mapAdvice = mapAdvice;
        this.mapTarget = mapTarget;
        this.correlationId = correlationId;
        this.mapBlock = mapBlock;
    }

    String getId() {
        return id;
    }

    LocalDate getEffectiveTime() {
        return effectiveTime;
    }

    Boolean getActive() {
        return active;
    }

    String getModuleId() {
        return moduleId;
    }

    String getRefSetId() {
        return refSetId;
    }

    String getReferencedComponentId() {
        return referencedComponentId;
    }

    Integer getMapGroup() {
        return mapGroup;
    }

    Integer getMapPriority() {
        return mapPriority;
    }

    String getMapRule() {
        return mapRule;
    }

    String getMapAdvice() {
        return mapAdvice;
    }

    String getMapTarget() {
        return mapTarget;
    }

    String getCorrelationId() {
        return correlationId;
    }

    Integer getMapBlock() {
        return mapBlock;
    }

    public void setMapTarget(String mapTarget) {
        this.mapTarget = mapTarget;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefSetRecord record = (RefSetRecord) o;
        return Objects.equals(refSetId, record.refSetId) &&
                Objects.equals(referencedComponentId, record.referencedComponentId) &&
                Objects.equals(mapTarget, record.mapTarget);
    }

    @Override
    public int hashCode() {
        return Objects.hash(refSetId, referencedComponentId, mapTarget);
    }
}
