package parts.cell;

import shticell.sheets.sheet.parts.cell.coordinate.Coordinate;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValue;

import java.util.List;

public class CellDTO {
    private final Coordinate coordinate; // "A4"
    private final String originalValue;
    private final EffectiveValue effectiveValue;
    private final int lastUpdatedVersion;
    private final List<Coordinate> influencingOn;
    private final List<Coordinate> dependsOn;
    private final String lastEditedBy;

    public CellDTO(Coordinate coordinate, String originalValue,
                           EffectiveValue effectiveValue, int lastUpdatedVersion, List<Coordinate> influencingOn,
                   List<Coordinate>  dependsOn, String lastUpdatedBy) {
        this.coordinate = coordinate;
        this.originalValue = originalValue;
        this.lastUpdatedVersion = lastUpdatedVersion;
        this.effectiveValue = effectiveValue;
        this.influencingOn = influencingOn;
        this.dependsOn = dependsOn;
        this.lastEditedBy = lastUpdatedBy;
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

    public String getLastEditedBy() {
        return lastEditedBy;
    }
}
