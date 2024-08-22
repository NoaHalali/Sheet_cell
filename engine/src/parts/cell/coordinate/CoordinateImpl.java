package parts.cell.coordinate;

public class CoordinateImpl implements Coordinate {
    private final int row;
    private final int column;


    public CoordinateImpl(int row, int column) {
        this.row = row;
        this.column = column;

    }

    public static Coordinate stringToCoord(String Coord){
      if(Coord.length()!=2){
          //error
          return null;
      }
      else{
          int column = Coord.charAt(0) - 'A'+1;
          int row = Coord.charAt(1) - '1'+1;
          //בדיקה שהשורה והעמודה אכן בטווח TODO
          return new CoordinateImpl(row, column);
      }
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
}
