package uk.co.genomicsengland.re.fhir.tools.icdo3;

class MorphRecord {
    private final String code;
    private final String struct;
    private final String label;

    MorphRecord(String code, String struct, String label) {
        this.code = code;
        this.struct = struct;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public String getStruct() {
        return struct;
    }

    public String getLabel() {
        return label;
    }
}
