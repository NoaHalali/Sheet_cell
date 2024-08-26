package parts.cell.coordinate;

import java.io.Serializable;

public class CoordinateImpl implements Coordinate, Serializable {
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
        char char1= (char) ('A'+ column -1);
        char char2= (char) ('1'+row-1);
        return ""+char1+char2;
    }

    public static Coordinate parseCoordinate(String input) throws IllegalArgumentException {

        if (input.matches("^[A-Za-z]+[0-9]+$")) { // Accepts both uppercase and lowercase letters
            String columnString = input.replaceAll("[0-9]", "");
            String rowString = input.replaceAll("[A-Za-z]", ""); // Also removes lowercase letters

            int column = columnStringToIndex(columnString.toUpperCase()); // Convert to uppercase before processing
            int row = Integer.parseInt(rowString);

            return new CoordinateImpl(row, column);
        } else {
            throw new IllegalArgumentException("Invalid coordinate format!\n" +
                    "Input must start with a letter and end with a number, e.g., A23.");
        }
    }
    public static int columnStringToIndex(String column) {

        int index = 0;
        for (int i = 0; i < column.length(); i++) {
            index = index * 26 + (column.charAt(i) - 'A' + 1);
        }
        return index ;
    }
}
