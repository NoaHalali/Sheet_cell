package parts;

import parts.cell.CellDTO;

public class SheetDTO {
    private int version;
    private String name;
    private int numberOfRows;
    private int numberOfCols;
    private int columnWidth;
    private int rowHeight;
    private CellDTO[][] cellsMatrix; // מערך דו-ממדי של תאים

}
