package parts;

import parts.cell.Cell;
import parts.cell.coordinate.Coordinate;
import parts.cell.expression.effectiveValue.CellType;
import parts.cell.expression.effectiveValue.EffectiveValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Range implements Serializable {

    private Coordinate topLeftCellID;
    private Coordinate bottomRightCellID;
    private boolean isUsed;//todo - check if needed
    private List<Cell> cells;
    private List<Coordinate> InfluencingOnCoordinates;//התאים שמשתמשים בטווח \ה

    public Range(Coordinate topLeftCellID, Coordinate bottomRightCellID,List<Cell> cells ) {
        this.topLeftCellID = topLeftCellID;
        this.bottomRightCellID = bottomRightCellID;
        isUsed = false;
        this.cells = cells;
        InfluencingOnCoordinates = new ArrayList<>();
    }
    public double calculateCellsSum () {
        double sum = 0 ,num;
        EffectiveValue tmpVal;

        for (Cell cell : cells) {
            tmpVal = cell.getEffectiveValue();
            if(tmpVal != null&&cell.getIsExist() && tmpVal.getCellType() == CellType.NUMERIC) {
              num = tmpVal.extractValueWithExpectation(Double.class);
              if(num!=Double.NaN) {
                  sum += num;
              }

            }

        }
        return sum;
    }
    public double calculateCellsAverage() {
        double sum = 0 ,num,numberOfCellsWithNumbers=0;
        EffectiveValue tmpVal;

        for (Cell cell : cells) {
            tmpVal = cell.getEffectiveValue();
            if(tmpVal != null&&cell.getIsExist() && tmpVal.getCellType() == CellType.NUMERIC) {
                num = tmpVal.extractValueWithExpectation(Double.class);
                if(num!=Double.NaN) {
                    numberOfCellsWithNumbers++;
                    sum += num;
                }

            }

        }
        return sum/numberOfCellsWithNumbers;
    }

    public List<Coordinate> getRangeCoordinates() {
        return cells.stream().map(Cell::getCoordinate).collect(Collectors.toList());
    }
    public void removeCoordinateFromInfluencingOnCoordinates(Coordinate coordinate) {
        InfluencingOnCoordinates.remove(coordinate);

    }
    public void addCoordinateFromInfluencingOnCoordinates(Coordinate coordinate) {
        InfluencingOnCoordinates.add(coordinate);
    }
    public boolean isBeingUsed() {
        return InfluencingOnCoordinates.size() > 0;
    }




}
