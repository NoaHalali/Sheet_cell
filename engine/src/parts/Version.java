package parts;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Version implements Serializable {

    private  Sheet sheet;
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
