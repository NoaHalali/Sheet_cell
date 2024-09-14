package parts;

import parts.sheet.Sheet;

import java.io.Serializable;

public class Version implements Serializable {

    private Sheet sheet;
    private int numberOfCellsChanged;

    public Version(Sheet sheet, int numberOfCellsChanged) {
        this.sheet = sheet;
        this.numberOfCellsChanged = numberOfCellsChanged;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public int getNumberOfCellsChanged() {
        return numberOfCellsChanged;
    }
}
