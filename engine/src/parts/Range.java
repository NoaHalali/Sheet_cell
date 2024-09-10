package parts;

import parts.cell.Cell;
import parts.cell.coordinate.Coordinate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Range implements Serializable {

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

    public List<Coordinate> getRangeCoordinates() {
        return cells.stream().map(Cell::getCoordinate).collect(Collectors.toList());
    }


}
