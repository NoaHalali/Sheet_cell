package parts.cell;

import parts.cell.coordinate.Coordinate;
import parts.cell.expression.Expression;
import parts.cell.expression.effectiveValue.EffectiveValue;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Cell {
    //row and col, or coordinate?
    private Coordinate coordinate;
    private int lastUpdatedVersion;
    private String originalValue;
    //private EffectiveValue effectiveValue;
    private Expression cellValue;
    private List<Cell> influencingOn ;//משפיע על התאים האלה
    private List<Cell> dependsOn ; //התאים שמושפע מהם

    //TODO - maybe send version of sheet
    public Cell(Coordinate coordinate, String originalValue) {
        this.coordinate = coordinate;
        this.originalValue = originalValue;
        lastUpdatedVersion = 1;

        this.influencingOn = new LinkedList<Cell>();
        this.dependsOn = new LinkedList<Cell>();
    }

    public void setCellValue(String originalValue)
    {

    }


//    public void updateValue(Expression newValue) {
//        cellValue = newValue; //בינתיים
//        cellValue.setOriginalValue(newValue);
//    }
//    public EffectiveValue geEffectiveValue()
//    {
//        return cellValue.evaluate();
//    }


//    public void updateEffectiveValue(Expression newValue) {
//        //בדיקת תקינות
//        EffectiveValue = newValue.evaluate();
//    }in

    public CellDTO toCellDTO() {
        return new CellDTO(
                coordinate, //"A4"
                originalValue,
                //cellValue.calculateEffectiveValue(),
                getEffectiveValue(),
                lastUpdatedVersion,
                getInfluencingOnCoordinates(),
                getDependsOnCoordinates()
        );
    }

    public Expression getCellValue() {
        return cellValue;
    }

    public void setExpression(Expression cellValue) {
       this.cellValue = cellValue;
    }

    public void removeCellFromInfluencingOnList(Cell cell){
        influencingOn.remove(cell);
    }

    public void AddCellToInfluencingOnList(Cell cell){
        influencingOn.add(cell);
    }

    public void updateCellsVersions(int currentVersion){
        lastUpdatedVersion= currentVersion;
        for(Cell cell : influencingOn){
            updateCellsVersions(currentVersion);
        }
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

    public void checkForCircularDependencyWrapper(Coordinate coordinate ,List<Cell> dependsOn) {
        HashSet<Coordinate> Coordset = new HashSet<>();
        Coordset.add(coordinate);
        checkForCircularDependency(Coordset,dependsOn);

    }
    public void checkForCircularDependency(HashSet<Coordinate> coordSet,List<Cell> dependsOn) {
        for(Cell cell: dependsOn){
            if(coordSet.contains(cell.getCoordinate())){
                throw new RuntimeException("Circular dependency found");//לחפור וזה
            }
            coordSet.add(cell.getCoordinate());
            cell.checkForCircularDependency(coordSet,cell.getDependsOn());
        }

    }


    public String getCoordinateString() {
        return coordinate.toString();
    }

    public String getOriginalValue() {
        return originalValue;
    }

    public EffectiveValue getEffectiveValue() {
        //return new EffectiveValueImpl(CellType.NUMERIC, 9.0);
        return cellValue.calculateEffectiveValue();
    }


    public int getLastUpdatedVersion() {
        return lastUpdatedVersion;
    }



    }
