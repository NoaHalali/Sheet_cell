package parts.cell;
//import engine.parts.Version;

import java.util.List;

public class CellDTO {
    private final Coordinate identity; // "A4"
    private final String originalValue;
    private final EffectiveValue effectiveValue;
    private final int lastUpdatedVersion;
    private final List<Coordinate> influencingOn;
    private final List<Coordinate> dependsOn;

    public CellDTO(Coordinate identity, String originalValue,
                   EffectiveValue effectiveValue, int lastUpdatedVersion, List<Coordinate> influencingOn, List<Coordinate>  dependsOn) {
        this.identity = identity;
        this.originalValue = originalValue;
        this.lastUpdatedVersion = lastUpdatedVersion;
//        this.rowsIndex = rowsIndex;
//        this.columnsIndex = columnsIndex;
        this.effectiveValue = effectiveValue;
        this.influencingOn = influencingOn;
        this.dependsOn = dependsOn;
    }

    public int getLastUpdatedVersion() {
        return lastUpdatedVersion;
    }


    public EffectiveValue getEffectiveValue() {
        return effectiveValue;
    }

    public List<Coordinate> getInfluencingOn() {
        return influencingOn;
    }

    public List<Coordinate> getDependsOn() {
        return dependsOn;
    }

    public Coordinate getIdentity() {
        return identity;
    }

    public String getOriginalValue() {
        return originalValue;
    }
}
