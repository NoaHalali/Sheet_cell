package parts;


import parts.cell.CellDTO;
import parts.cell.EmptyCellDTO;
import parts.sheet.cell.coordinate.Coordinate;

import java.util.List;

public class SheetDTO {
    private int version;
    private String name;
    private int numberOfRows;
    private int numberOfCols;
    private int columnWidth;
    private int rowHeight;
    private CellDTO[][] cellsMatrix; // מערך דו-ממדי של תאים
    private List<String> rangesNames;

    public SheetDTO(int version, String name, int numberOfRows, int numberOfCols,
                    int columnWidth, int rowHeight, CellDTO[][] cellsDTOMatrix, List<String> rangesNames) {
        this.version = version;
        this.name = name;
        this.numberOfRows = numberOfRows;
        this.numberOfCols = numberOfCols;
        this.columnWidth = columnWidth;
        this.rowHeight = rowHeight;
        this.cellsMatrix = cellsDTOMatrix;
        this.rangesNames = rangesNames;
    }

    public int getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public int getNumberOfCols() {
        return numberOfCols;
    }

    public int getColumnWidth() {
        return columnWidth;
    }

    public int getRowHeight() {
        return rowHeight;
    }

    public CellDTO[][] getCellsMatrix() {
        return cellsMatrix;
    }
    public CellDTO getCell(Coordinate coordinate) {
        CellDTO cell= cellsMatrix[coordinate.getRow()-1][coordinate.getCol()-1];
        if(cell != null){
            return cell;
        }else{
           return new EmptyCellDTO(coordinate, 0, List.of());
        }
    }

    public List<String> getRangesNames() {
        return rangesNames;
    }
}
