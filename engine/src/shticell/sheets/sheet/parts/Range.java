package shticell.sheets.sheet.parts;

import shticell.sheets.sheet.parts.cell.Cell;
import shticell.sheets.sheet.parts.cell.coordinate.Coordinate;
import shticell.sheets.sheet.parts.cell.coordinate.CoordinateImpl;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.CellType;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Range implements Serializable {

    private Coordinate topLeftCellID;
    private Coordinate bottomRightCellID;
    private boolean isUsed;//todo - check if needed
    private List<Cell> cells;
    private List<Coordinate> influencingOnCoordinates;//התאים שמשתמשים בטווח \ה
    public Range(){}

    public Range(Coordinate topLeftCellID, Coordinate bottomRightCellID,List<Cell> cells ) {
       setRange(topLeftCellID,bottomRightCellID,cells);
    }
    public double calculateCellsSum () {
        double sum = 0 ,num;
        EffectiveValue tmpVal;

        for (Cell cell : cells) {
            tmpVal = cell.getEffectiveValue();
            if(tmpVal != null&&cell.isExist() && tmpVal.getCellType() == CellType.NUMERIC) {
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
            if(tmpVal != null&&cell.isExist() && tmpVal.getCellType() == CellType.NUMERIC) {
                num = tmpVal.extractValueWithExpectation(Double.class);
                if(num!=Double.NaN) {
                    numberOfCellsWithNumbers++;
                    sum += num;
                }
            }
        }
        if(numberOfCellsWithNumbers==0) {
           return Double.NaN;
        }
        return sum/numberOfCellsWithNumbers;
    }
    public void setRange(Coordinate topLeftCellID, Coordinate bottomRightCellID,List<Cell> cells ){
        this.topLeftCellID = topLeftCellID;
        this.bottomRightCellID = bottomRightCellID;
        isUsed = false;
        this.cells = cells;
        influencingOnCoordinates = new ArrayList<>();
    }


    public List<Coordinate> getRangeCoordinates() {
        return cells.stream().map(Cell::getCoordinate).collect(Collectors.toList());
    }
    public void removeCoordinateFromInfluencingOnCoordinates(Coordinate coordinate) {
        influencingOnCoordinates.remove(coordinate);

    }
    public void addCoordinateFromInfluencingOnCoordinates(Coordinate coordinate) {
        influencingOnCoordinates.add(coordinate);
    }
    public List<Coordinate> getInfluencingOnList(){
        return influencingOnCoordinates;
    }
    public boolean isBeingUsed() {
        return influencingOnCoordinates.size() > 0;
    }
    public List<Cell> getCellsInRange(){
        return cells;
    }

    public static void isValidRange(Coordinate topLeftCoord, Coordinate bottomRightCoord) throws IllegalArgumentException {
        // בדוק אם השורה והעמודה של הקואורדינטה העליונה השמאלית קטנים או שווים לאלו של הקואורדינטה התחתונה הימנית

         if(!(topLeftCoord.getRow() <= bottomRightCoord.getRow() && topLeftCoord.getCol() <= bottomRightCoord.getCol()))
         {
             throw new IllegalArgumentException("Invalid range coordinates: " + topLeftCoord + " to " + bottomRightCoord + ".\n" +
                     "Top-left coordinate must be smaller or equal to bottom-right coordinate.");
         }
    }
    public static Coordinate[] parseRange(String rangeDefinition) {
        if (rangeDefinition == null || rangeDefinition.trim().isEmpty()) {
            throw new IllegalArgumentException("Range definition cannot be null or empty.");
        }
        String[] parts = rangeDefinition.split("\\.\\.");
        // Ensure there are exactly two parts after the split
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid range format. The range must contain two coordinates separated by '..'. Example: 'A1..B2'.");
        }
        try {
            Coordinate topLeftCoord = CoordinateImpl.parseCoordinate(parts[0]);
            Coordinate bottomRightCoord = CoordinateImpl.parseCoordinate(parts[1]);
            return new Coordinate[] {topLeftCoord, bottomRightCoord};
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Invalid range definition. Range must be seperated by 2 valid coordinates");
        }



    }


}
