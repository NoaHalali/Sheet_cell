package parts.cell;
//import engine.parts.Version;

import java.util.List;

public class CellDTO {
    private final String identity; // "A4"
    private final String originalValue;
    private final String effectiveValue;
    private final int lastUpdatedVersion;
    private final List<String> affectOn;
    private final List<String> affectedBy;

    public CellDTO(String identity, String originalValue, int lastUpdatedVersion,
                   String effectiveValue, List<String> affectOn, List<String>  affectedBy) {
        this.identity = identity;
        this.originalValue = originalValue;
        this.lastUpdatedVersion = lastUpdatedVersion;
//        this.rowsIndex = rowsIndex;
//        this.columnsIndex = columnsIndex;
        this.effectiveValue = effectiveValue;
        this.affectOn = affectOn;
        this.affectedBy = affectedBy;
    }

    public int getLastUpdatedVersion() {
        return lastUpdatedVersion;
    }


    public String getEffectiveValue() {
        return effectiveValue;
    }

    public List<String> getAffectOn() {
        return affectOn;
    }

    public List<String> getAffectedBy() {
        return affectedBy;
    }

    public String getIdentity() {
        return identity;
    }

    public String getOriginalValue() {
        return originalValue;
    }
}
