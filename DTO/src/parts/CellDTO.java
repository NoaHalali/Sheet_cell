package parts;
//import engine.parts.Version;

import parts.cell.coordinate.Coordinate;
import parts.cell.expression.effectiveValue.EffectiveValue;

import java.util.List;

public interface CellDTO {


    public int getLastUpdatedVersion();

    public EffectiveValue getEffectiveValue();

    public List<Coordinate> getInfluencingOn();


    public List<Coordinate> getDependsOn();


    public Coordinate getCoord();


    public String getOriginalValue();

}