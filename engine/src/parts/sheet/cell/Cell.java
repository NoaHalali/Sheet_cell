package parts.sheet.cell;

import parts.Range;
import parts.cell.CellDTO;
import parts.cell.EmptyCellDTO;
import parts.cell.NonEmptyCellDTO;
import parts.sheet.cell.coordinate.Coordinate;
import parts.sheet.cell.expression.Expression;
import parts.sheet.cell.expression.effectiveValue.EffectiveValue;
import parts.sheet.cell.expression.impl.NumberExpression;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Cell implements Serializable {
    private static final long serialVersionUID = 1L;
    //row and col, or coordinate?
    private Coordinate coordinate;
    private int lastUpdatedVersion;
    private String originalValue;
    private EffectiveValue effectiveValue;
    private boolean isExist;
    private Expression cellValue;
    private List<Cell> influencingOn;//משפיע על התאים האלה
    private List<Cell> dependsOn; //התאים שמושפע מהם
    private List<Range> rangesDependsOn;
    private List<Range>InfluencingOnRanges;


    //TODO - maybe send version of sheet
    public Cell(Coordinate coordinate, String originalValue) {
        this.coordinate = coordinate;
        this.originalValue = originalValue;
        lastUpdatedVersion = 1;
        isExist = true;
        this.influencingOn = new LinkedList<Cell>();
        this.dependsOn = new LinkedList<Cell>();
        this.rangesDependsOn = new LinkedList<Range>();
        this.InfluencingOnRanges = new LinkedList<Range>();
    }
    public static Cell createEmptyCell(Coordinate coordinate) {
        Cell cell= new Cell(coordinate, "");
        cell.lastUpdatedVersion = 0;
        cell.isExist = false;
        return cell;
    }
    public void setIsExist(boolean isExist) {
        this.isExist = isExist;
    }
//    public void setLastUpdatedVersion(int  lastUpdatedVersion) {
//        lastUpdatedVersion=0;
//    }



    public boolean calculateAndCheckIfUpdated() {
        EffectiveValue oldEffectiveValue = effectiveValue; //in case the next line changes it
        EffectiveValue newEffectiveValue = getAndUpdateEffectiveValue();

        if(oldEffectiveValue == null ) {
            if (newEffectiveValue == null) {
                return false;
            } else {
                return true;
            }
        }
        else if(newEffectiveValue == null ) {
            if (oldEffectiveValue == null) {
                return false;
            } else {
                return true;
            }
        }

        else if (oldEffectiveValue.equals(newEffectiveValue)) {
            return false;
        }
        else { //changed
            effectiveValue = newEffectiveValue;
            return true;
        }
    }

    public void setCellOriginalValue(String originalValue) {
        this.originalValue = originalValue;
    }

//    public NonEmptyCellDTO toNonEmptyCellDTO() {
//        return new NonEmptyCellDTO(
//                coordinate, //"A4"
//                originalValue,
//                getAndUpdateEffectiveValue(),
//                lastUpdatedVersion,
//                getInfluencingOnCoordinates(),
//                getDependsOnCoordinates()
//        );
//    }
//    public EmptyCellDTO toEmptyCellDTO() {
//
//    }
    public List<Coordinate> getFullDependsOnCoordList(){
        List<Coordinate> fullList = getDependsOnCoordinates();
        for(Range range : rangesDependsOn){
            fullList.addAll(range.getRangeCoordinates());
        }
        return fullList;
    }
    public List<Cell>getFullDependsOnCellList(){
        List<Cell> fullList= new LinkedList<Cell>(dependsOn);
        for(Range range : rangesDependsOn){
            fullList.addAll(range.getCellsInRange());
        }
        return fullList;
    }
    public CellDTO toCellDTO() {
        if (isExist)
        {
            return new NonEmptyCellDTO(
                    coordinate, //"A4"
                    originalValue,
                    getAndUpdateEffectiveValue(),
                    lastUpdatedVersion,
                    getFullInfluencingOnCoordinates(),
                    getFullDependsOnCoordList()
            );
        }
        else {
            return new EmptyCellDTO(
                    coordinate, //"A4"
                    lastUpdatedVersion,
                   getFullInfluencingOnCoordinates()
            );
        }

    }
    public List<Range>getRangesDependsOnList(){
        return rangesDependsOn;
    }
    public void setRangesDependsOnList(List<Range> rangesDependsOn) {
        this.rangesDependsOn = rangesDependsOn;
    }

    public void AddRangeToInfluencingOnRange(Range influencingOnRange)
    {
        InfluencingOnRanges.add(influencingOnRange);
    }
    public void removeRangeFromInfluencingOnRange(Range influencingOnRange){
        InfluencingOnRanges.remove(influencingOnRange);
    }
    public Expression getCellValue() {
        return cellValue;
    }

    public void setExpression(Expression cellValue) {
        this.cellValue = cellValue;
    }

    public void setLastUpdatedVersion(int newVerion) {
        lastUpdatedVersion = newVerion;
    }

    public void removeCellFromInfluencingOnList(Cell cell) {
        influencingOn.remove(cell);
    }

    public void AddCellToInfluencingOnList(Cell cell) {
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

    public List<Coordinate> getDependsOnCoordinates() {
        return dependsOn.stream().map(Cell::getCoordinate).collect(Collectors.toList());

    }

    public List<Coordinate> getInfluencingOnCoordinates() {
        return influencingOn.stream().map(Cell::getCoordinate).collect(Collectors.toList());

    }
    public List<Coordinate> getFullInfluencingOnCoordinates() {
        List <Coordinate> fullList = getInfluencingOnCoordinates();
        for(Range range : InfluencingOnRanges){
            fullList.addAll(range.getInfluencingOnList());
        }
        return fullList;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

//    public void checkIfCellCanBeDeleted() {
//        if (!influencingOn.isEmpty()) {
//            throw new RuntimeException("cannot delete cell on coordinate " + coordinate + " because he is influencing other cell with ref");
//        }
//    }
    public void resetCell() {
        originalValue = "";
        //effectiveValue = null;
        isExist = false;
        cellValue = null;
        for (Cell cell :dependsOn){
            cell.removeCellFromInfluencingOnList(this);
        }
        dependsOn.clear();
        for(Range range : rangesDependsOn) {
            range.removeCoordinateFromInfluencingOnCoordinates(coordinate);
        }
        rangesDependsOn.clear();
        //influencing ??
    }

    public void checkIfCellExpressionCanBeUpdatedWrapper() {
        HashSet<Coordinate> Coordset = new HashSet<>();
        Coordset.add(coordinate);
        checkIfCellExpressionCanBeUpdated(Coordset);

    }

    public void checkIfCellExpressionCanBeUpdated(HashSet<Coordinate> coordSet) {

        cellValue.calculateEffectiveValue();
        for (Cell cell : influencingOn) {
            if (!coordSet.contains(cell.getCoordinate())) {
                coordSet.add(cell.getCoordinate());
                try {
                    cell.checkIfCellExpressionCanBeUpdated(coordSet);
                } catch (ClassCastException e) {
                    throw new ClassCastException(cell.getCoordinate() + " " + e.getMessage());
                }
            }

        }
    }


    public void checkForCircularDependencyWrapper(Coordinate coordinate ) {
        HashSet<String> coordSet = new HashSet<>();

        coordSet.add(coordinate.toString());
        checkForCircularDependency(coordSet, getFullDependsOnCellList());

    }

    public void checkForCircularDependency(Set<String> coordSet, List<Cell> dependsOn) {
        Set<String> ClonedSet ;

        for (Cell cell : dependsOn) {
            ClonedSet = new HashSet<String>(coordSet);
            if (coordSet.contains(cell.getCoordinate().toString())) {
                throw new RuntimeException("Circular dependency found");//לחפור וזה
            }
            ClonedSet.add(cell.getCoordinate().toString());
            cell.checkForCircularDependency(ClonedSet,cell.getFullDependsOnCellList());
        }

    }

    public String getOriginalValue() {
        return originalValue;
    }

    public EffectiveValue getAndUpdateEffectiveValue() {
        if(isExist) {
            effectiveValue = cellValue.calculateEffectiveValue();
        }else{
            effectiveValue = null;
        }
        return effectiveValue;
    }

    public EffectiveValue getEffectiveValue() {
        if(isExist) {
            return cellValue.calculateEffectiveValue();
        }
        return null;
    }
    public boolean IsCellExpressionIsNumber() {
        return cellValue instanceof NumberExpression;
    }

    public boolean isExist() {
        return isExist;
    }

    public static boolean isGhostCell(Cell cell) {
        return cell != null && !cell.isExist();
    }

}
