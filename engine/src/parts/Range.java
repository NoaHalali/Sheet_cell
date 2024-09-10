package parts;

import parts.cell.Cell;
import parts.cell.coordinate.Coordinate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Range {

    private Coordinate topLeftCellID;
    private Coordinate bottomRightCellID;
    private boolean isUsed;//todo - check if needed
    private List<Cell> cells;
    public Range(Coordinate topLeftCellID, Coordinate bottomRightCellID,List<Cell> cells ) {
        this.topLeftCellID = topLeftCellID;
        this.bottomRightCellID = bottomRightCellID;
        isUsed = false;
        this.cells = cells;
    }





}
