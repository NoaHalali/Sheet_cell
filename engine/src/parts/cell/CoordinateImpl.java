package parts.cell;

public class CoordinateImpl implements Coordinate{
    private final int row;
    private final int column;


    public CoordinateImpl(int row, int column) {
        this.row = row;
        this.column = column;

    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getCol() {
        return column;
    }
    @Override
    public String toString() {

        char char1= (char) ('A'+row);
        char char2= (char) ('1'+column);
        return ""+char1+char2;
    }
}
