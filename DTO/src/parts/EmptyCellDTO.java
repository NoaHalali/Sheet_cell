package parts;

import parts.cell.coordinate.Coordinate;
import parts.cell.expression.effectiveValue.EffectiveValue;

import java.io.Serializable;
import java.util.List;

public class EmptyCellDTO implements CellDTO {
    private final Coordinate coordinate;
    private final int lastUpdatedVersion;
    private final List<Coordinate> influencingOn;

    public EmptyCellDTO(Coordinate coordinate, int lastUpdatedVersion, List<Coordinate> influencingOn) {
        this.coordinate = coordinate;
        this.lastUpdatedVersion = lastUpdatedVersion;
        this.influencingOn = influencingOn;
    }


    @Override
    public int getLastUpdatedVersion() {
        return lastUpdatedVersion;
    }

    @Override
    public EffectiveValue getEffectiveValue() {
        return null;
    }

    @Override
    public List<Coordinate> getInfluencingOn() {
        return List.of();
    }

    @Override
    public List<Coordinate> getDependsOn() {
        return List.of();
    }

    @Override
    public Coordinate getCoord() {
        return coordinate;
    }

    @Override
    public String getOriginalValue() {
        return "";
    }

}
