package parts.cell;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Cell {
    //row and col, or coordinate?
    private Coordinate coordinate;
    private int lastUpdatedVersion;
    private Expression cellValue;
    //private String EffectiveValue; //אולי להוסיף intetface במקום ב-string
    private List<Cell> neighbors;
    private List<Cell> influencingOn ;//משפיע על התאים האלה
    private List<Cell> dependsOn ; //התאים שמושפע מהם

//    public Cell()
//    {//
//       // cellValue = new Expression();
//    }


    public void updateValue(Expression newValue) {
        cellValue = newValue; //בינתיים
        cellValue.setOriginalValue(newValue);
    }
    public EffectiveValue geEffectiveValue()
    {
        return cellValue.evaluate();
    }

//    public String geEffectiveValue() {
//        return EffectiveValue;
//    }
//    public void updateEffectiveValue(Expression newValue) {
//        //בדיקת תקינות
//        EffectiveValue = newValue.evaluate();
//    }

//    public CellDTO toCellDTO() {
//        return new CellDTO(
//                getIdentity(), //"A4"
//                cellValue.getOriginalValue(),
//                lastUpdatedVersion,
//                geEffectiveValue(),
//                getAffectOnNames(),
//                getAffectedByNames()
//        );
//    }


    public List<Cell> getInfluencingOn() {
        return influencingOn;
    }

    public List<Cell> getDependsOn() {
        return dependsOn;
    }

    //Move to UI
    public List<String> getDependsOnNames() {
        return dependsOn.stream().map(Cell::getCoordinateString).collect(Collectors.toList());

    }

    public List<String> getInfluencingOnNames() {
        return influencingOn.stream().map(Cell::getCoordinateString).collect(Collectors.toList());
    }

    public String getCoordinateString() {
        return coordinate.toString();
    }

    public String getOriginalValue() {
        return cellValue.getOriginalValue();
    }

    public EffectiveValue getEffectiveValue() {
        return cellValue.evaluate();
    }

    public int getLastUpdatedVersion() {
        return lastUpdatedVersion;
    }

}
