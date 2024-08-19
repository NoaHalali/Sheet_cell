package parts.cell;

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


//    public void updateEffectiveValue(Expression newValue) {
//        //בדיקת תקינות
//        EffectiveValue = newValue.evaluate();
//    }in

    public CellDTO toCellDTO() {
        return new CellDTO(
                coordinate, //"A4"
                cellValue.getOriginalValue(),
                geEffectiveValue(),
                lastUpdatedVersion,
                getInfluencingOnCoordinates(),
                getDependsOnCoordinates()
        );
    }

public Expression getCellValue() {
        return cellValue;
}

public void removeCellFromInfluencingOnList(Cell cell){
        influencingOn.remove(cell);
}
    public void AddCellToInfluencingOnList(Cell cell){
        influencingOn.add(cell);
    }

    public List<Cell> getInfluencingOn() {
        return influencingOn;
    }

    public List<Cell> getDependsOn() {
        return dependsOn;
    }
    public void setDependsOn(List<Cell> dependsOn) {
        this.dependsOn = dependsOn;
    }

    //TODO- Move to UI
    public List<String> getDependsOnNames() {
        return dependsOn.stream().map(Cell::getCoordinateString).collect(Collectors.toList());

    }
    public List<String> getInfluencingOnNames() {
        return influencingOn.stream().map(Cell::getCoordinateString).collect(Collectors.toList());
    }


    public List<Coordinate> getDependsOnCoordinates() {
        return dependsOn.stream().map(Cell::getCoordinate).collect(Collectors.toList());

    }

    public List<Coordinate> getInfluencingOnCoordinates() {
        return influencingOn.stream().map(Cell::getCoordinate).collect(Collectors.toList());

    }

    public Coordinate getCoordinate() {
        return coordinate;
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
