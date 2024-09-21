package parts;

import parts.sheet.cell.Cell;
import parts.sheet.cell.coordinate.Coordinate;
import parts.sheet.cell.coordinate.CoordinateImpl;
import parts.sheet.cell.expression.effectiveValue.CellType;
import parts.sheet.cell.expression.effectiveValue.EffectiveValue;

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

    public Range(Coordinate topLeftCellID, Coordinate bottomRightCellID,List<Cell> cells ) {
        this.topLeftCellID = topLeftCellID;
        this.bottomRightCellID = bottomRightCellID;
        isUsed = false;
        this.cells = cells;
        influencingOnCoordinates = new ArrayList<>();
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
        return sum/numberOfCellsWithNumbers;
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

    public static void isValidRange(Coordinate topLeftCoord, Coordinate bottomRightCoord) throws IllegalArgumentException {
        // בדוק אם השורה והעמודה של הקואורדינטה העליונה השמאלית קטנים או שווים לאלו של הקואורדינטה התחתונה הימנית

         if(!(topLeftCoord.getRow() <= bottomRightCoord.getRow() && topLeftCoord.getCol() <= bottomRightCoord.getCol()))
         {
             throw new IllegalArgumentException("Invalid range coordinates: " + topLeftCoord + " to " + bottomRightCoord + ".\n" +
                     "Top-left coordinate must be smaller or equal to bottom-right coordinate.");
         }
    }
    public static Coordinate[] parseRange(String rangeDefinition) {
        String[] parts = rangeDefinition.split("\\.\\.");
        Coordinate topLeftCoord = CoordinateImpl.parseCoordinate(parts[0]);
        Coordinate bottomRightCoord = CoordinateImpl.parseCoordinate(parts[1]);

        // מחזיר מערך עם שתי הקואורדינטות
        return new Coordinate[] {topLeftCoord, bottomRightCoord};
    }


}
