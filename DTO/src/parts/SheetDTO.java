package parts;


import parts.cell.coordinate.Coordinate;

public class SheetDTO {
    private int version;
    private String name;
    private int numberOfRows;
    private int numberOfCols;
    private int columnWidth;
    private int rowHeight;
    private CellDTO[][] cellsMatrix; // מערך דו-ממדי של תאים

    public SheetDTO(int version, String name, int numberOfRows, int numberOfCols,
                    int columnWidth, int rowHeight, CellDTO[][] cellsDTOMatrix) {
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

    public CellDTO[][] getCellsMatrix() {
        return cellsMatrix;
    }
    public CellDTO getCell(Coordinate coordinate) {
        return cellsMatrix[coordinate.getRow()-1][coordinate.getCol()-1];
    }
}
