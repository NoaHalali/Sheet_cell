package parts;

import parts.cell.coordinate.Coordinate;
import parts.cell.expression.effectiveValue.EffectiveValue;

import java.util.List;

public class NonEmptyCellDTO implements CellDTO {
    private final Coordinate coordinate; // "A4"
    private final String originalValue;
    private final EffectiveValue effectiveValue;
    private final int lastUpdatedVersion;
    private final List<Coordinate> influencingOn;
    private final List<Coordinate> dependsOn;


    public NonEmptyCellDTO(Coordinate coordinate, String originalValue,
                   EffectiveValue effectiveValue, int lastUpdatedVersion, List<Coordinate> influencingOn, List<Coordinate>  dependsOn) {
        this.coordinate = coordinate;
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

    public Coordinate getCoord() {
        return coordinate;
    }

    public String getOriginalValue() {
        return originalValue;
    }
}
