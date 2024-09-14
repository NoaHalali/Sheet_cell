package parts.sheet;

import parts.sheet.cell.Cell;

import java.util.List;

public class SheetRow {
    private List<Cell> cells; // רשימה של תאים בשורה

    public SheetRow(List<Cell> cells) {
        this.cells = cells; // אתחול הרשימה של התאים
    }

    public Cell getCell(int columnIndex) {
        return cells.get(columnIndex); // החזרת תא לפי אינדקס העמודה
    }

    public List<Cell> getCells() {
        return cells; // החזרת כל התאים בשורה
    }
}
