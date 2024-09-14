package parts.cell;
//import engine.parts.Version;

import parts.sheet.cell.coordinate.Coordinate;
import parts.sheet.cell.expression.effectiveValue.EffectiveValue;

import java.util.List;

public interface CellDTO {


    public int getLastUpdatedVersion();

    public EffectiveValue getEffectiveValue();

    public List<Coordinate> getInfluencingOn();


    public List<Coordinate> getDependsOn();


    public Coordinate getCoord();


    public String getOriginalValue();

}