package parts;

import parts.cell.NonEmptyCellDTO;

public class SheetDTO {
    private int version;
    private String name;
    private int numberOfRows;
    private int numberOfCols;
    private int columnWidth;
    private int rowHeight;
    private NonEmptyCellDTO[][] cellsMatrix; // מערך דו-ממדי של תאים

    //TODO - create method in sheet that creates the sheetDTO
    public SheetDTO(int version, String name, int numberOfRows, int numberOfCols,
                    int columnWidth, int rowHeight, NonEmptyCellDTO[][] cellsDTOMatrix) {
        this.version = version;
        this.name = name;
        this.numberOfRows = numberOfRows;
        this.numberOfCols = numberOfCols;
        this.columnWidth = columnWidth;
        this.rowHeight = rowHeight;
        this.cellsMatrix = cellsDTOMatrix;
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

    public NonEmptyCellDTO[][] getCellsMatrix() {
        return cellsMatrix;
    }
}
