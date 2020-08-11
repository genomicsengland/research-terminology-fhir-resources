package uk.co.genomicsengland.re.fhir.tools.icdo3;

class MorphRecordParser {

    MorphRecord parse(String line) {
        String[] parts = line.split("\t");
        if (parts.length != 3) {
            throw new RuntimeException("Failed to parse: " + line);
        }
        String code = parts[0].trim();
        String struct = parts[1].trim();
        String label = parts[2].trim();
        if (label.charAt(0) == '"') {
            label = label.substring(1);
        }
        if (label.charAt(label.length() - 1) == '"') {
            label = label.substring(0, label.length() - 1);
        }
        return new MorphRecord(code, struct, label);
    }
}
