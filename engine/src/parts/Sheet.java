package parts;

import parts.cell.Cell;

import java.util.ArrayList;

public class Sheet {
    private String sheetName;
    private int numOfRows;
    private int numOfCols;
    private ArrayList<ArrayList<Cell>> cellsMatrix ;
    private int versionNumber = 1;
    private int maxRows;//?
    private int maxCols;//???

    public String getSheetName() {
        return sheetName;
    }

    public int getVersionNumber()
    {
        return versionNumber;
    }

    //print function - shouldnt be here!!!!!!!
    public void printSheetCell()
    {
        //maybe change to row col
         for(ArrayList<Cell> row : cellsMatrix)
             for(Cell cell : row)
                 cell.print();
    }

}
