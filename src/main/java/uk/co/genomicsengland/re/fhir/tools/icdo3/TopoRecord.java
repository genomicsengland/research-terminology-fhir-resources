package uk.co.genomicsengland.re.fhir.tools.icdo3;

class TopoRecord implements Comparable {
    private final String code;
    private final String level;
    private final String titel;

    public TopoRecord(String code, String level, String titel) {
        this.code = code;
        this.level = level;
        this.titel = titel;
    }

    public String getCode() {
        return code;
    }

    public String getLevel() {
        return level;
    }

    public String getTitle() {
        return titel;
    }

    @Override
    public int compareTo(Object o) {
        TopoRecord other = (TopoRecord)o;
        int result = this.getCode().compareTo(other.getCode());
        if (result == 0) {
            result = this.getLevel().compareTo(other.getLevel());
        }
        if (result == 0) {
            result = this.getTitle().compareToIgnoreCase(other.getTitle());
        }
        return result;
    }
}
