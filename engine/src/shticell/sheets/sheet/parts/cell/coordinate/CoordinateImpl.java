package shticell.sheets.sheet.parts.cell.coordinate;

import java.io.Serializable;

public class CoordinateImpl implements Coordinate, Serializable {
    private final int row;
    private final int column;
    public final static Coordinate notExists = new CoordinateImpl(-1, -1);


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
        if(row == -1 && column == -1){
            return "Not Exists";
        }
        char char1= (char) ('A'+ column -1);
        String number= String.valueOf(row);
        return indexToColumnString(column)+number;
    }

    @Override
    public char getColChar() {
        return indexToColumnString(column).charAt(0); //assuming the column is a single letter
    }

    public static Coordinate parseCoordinate(String input) throws IllegalArgumentException {

        if(input==null) {
            System.out.println("was here");
        }

        if (input.matches("^[A-Za-z]+[0-9]+$")) { // Accepts both uppercase and lowercase letters
            String columnString = input.replaceAll("[0-9]", "");
            String rowString = input.replaceAll("[A-Za-z]", ""); // Also removes lowercase letters

            int column = columnStringToIndex(columnString); // Convert to uppercase before processing
            int row = Integer.parseInt(rowString);

            return new CoordinateImpl(row, column);
        } else {
            throw new IllegalArgumentException("Invalid coordinate format!\n" +
                    "Input must start with a letter and end with a number, e.g., A23.");
        }
    }
    public static int columnStringToIndex(String column) {
           column = column.toUpperCase();
        int index = 0;
        for (int i = 0; i < column.length(); i++) {
            index = index * 26 + (column.charAt(i) - 'A' + 1);
        }
        return index ;
    }
    public static String indexToColumnString(int index) {
        StringBuilder column = new StringBuilder();

        while (index > 0) {
            index--; // מפחיתים 1 כדי להתאים את האינדקס לשימוש נכון (1-based ל-0-based)
            int remainder = index % 26;
            char letter = (char) (remainder + 'A');
            column.insert(0, letter); // מוסיפים את האות הנכונה להתחלה
            index = index / 26;
        }

        return column.toString();
    }
}
