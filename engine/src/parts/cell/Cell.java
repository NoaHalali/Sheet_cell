package parts.cell;

import parts.CellDTO;
import parts.cell.coordinate.Coordinate;
import parts.cell.expression.Expression;
import parts.cell.expression.effectiveValue.EffectiveValue;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Cell implements Serializable {
    private static final long serialVersionUID = 1L;
    //row and col, or coordinate?
    private Coordinate coordinate;
    private int lastUpdatedVersion;
    private String originalValue;
    private EffectiveValue effectiveValue;
    private Expression cellValue;
    private List<Cell> influencingOn;//משפיע על התאים האלה
    private List<Cell> dependsOn; //התאים שמושפע מהם

    //TODO - maybe send version of sheet
    public Cell(Coordinate coordinate, String originalValue) {
        this.coordinate = coordinate;
        this.originalValue = originalValue;
        lastUpdatedVersion = 1;

        this.influencingOn = new LinkedList<Cell>();
        this.dependsOn = new LinkedList<Cell>();
    }

    public boolean calculateAndCheckIfUpdated() {
        EffectiveValue oldEffectiveValue = effectiveValue; //in case the next line changes it
        EffectiveValue newEffectiveValue = getAndUpdateEffectiveValue();
        if (oldEffectiveValue.equals(newEffectiveValue)) {
            return false;
        }
        effectiveValue = newEffectiveValue;
        return true;
    }

    public void setCellOriginalValue(String originalValue) {
        this.originalValue = originalValue;
    }

    public CellDTO toCellDTO() {
        return new CellDTO(
                coordinate, //"A4"
                originalValue,
                //cellValue.calculateEffectiveValue(),
                getAndUpdateEffectiveValue(),
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

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void checkIfCellCanBeDeleted() {
        if (!influencingOn.isEmpty()) {
            throw new RuntimeException("cannot delete cell on coordinate " + coordinate + " because he is influencing other cell with ref");
        }
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


    public void checkForCircularDependencyWrapper(Coordinate coordinate, List<Cell> dependsOn) {
        HashSet<Coordinate> coordSet = new HashSet<>();
        coordSet.add(coordinate);
        checkForCircularDependency(coordSet, dependsOn);

    }

    public void checkForCircularDependency(HashSet<Coordinate> coordSet, List<Cell> dependsOn) {
        for (Cell cell : dependsOn) {
            if (coordSet.contains(cell.getCoordinate())) {
                throw new RuntimeException("Circular dependency found");//לחפור וזה
            }
            coordSet.add(cell.getCoordinate());
            cell.checkForCircularDependency(coordSet, cell.getDependsOn());
        }

    }

    public String getOriginalValue() {
        return originalValue;
    }

    public EffectiveValue getAndUpdateEffectiveValue() {

        effectiveValue = cellValue.calculateEffectiveValue();
        return effectiveValue;
    }

    public EffectiveValue getEffectiveValue() {
        return cellValue.calculateEffectiveValue();
    }
}
