package parts;

import parts.cell.*;
import parts.cell.coordinate.Coordinate;
import parts.cell.coordinate.CoordinateImpl;
import parts.cell.expression.Expression;
import parts.cell.expression.impl.BoolExpression;
import parts.cell.expression.impl.NumberExpression;
import parts.cell.expression.impl.StringExpression;
import parts.cell.expression.impl.function.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Sheet {
    private int  version;
    private final String name;
    private final int numberOfRows;
    private final int numberOfCols;
    private final int columnWidth;
    private final int rowHeight;
    private Cell[][] cellsMatrix; // מערך דו-ממדי של תאים

    private static final char minCol = 'A';
    private static final int minRow = 1;
    private final char maxCol;
    private final int maxRow;

    public Sheet(String name, int numberOfRows, int numberOfCols, int columnWidth, int rowHeight) {
        this.name = name;
        this.numberOfRows = numberOfRows;
        this.numberOfCols = numberOfCols;
        this.columnWidth = columnWidth;
        this.rowHeight = rowHeight;
        this.version = 1;
        this.maxCol = (char) (minCol + numberOfCols - 1);
        this.maxRow = numberOfRows;
    }

    public String getSheetName() {
        return name;
    }
    public void upgradeVersion(){
        version++;
    }
    public int getVersion() {
        return version;
    }

    public SheetDTO toSheetDTO() {
        return new SheetDTO(
                version, //"A4"
                name,
                numberOfRows,
                numberOfCols,
                columnWidth,
                rowHeight,
                getCellsDTOMatrix()
        );
    }
    public CellDTO[][] getCellsDTOMatrix() {
        return Arrays.stream(cellsMatrix)
                .map(row -> Arrays.stream(row)
                        .map(cell -> cell != null ? cell.toCellDTO() : null)
                        .toArray(CellDTO[]::new))
                .toArray(CellDTO[][]::new);
    }

    public Cell getCellByCoord(Coordinate coord){

        //TODO - add checking if cell is out of bounds, or empty?
        if (!isCoordinateValid(coord)) {


            String errorMessage = String.format("The coordinate is out of bounds. " +
                            "Valid column range: %c to %c, " +
                            "Valid row range: %d to %d. " +
                            "Given coordinate: %s",
                    minCol, maxCol, minRow, maxRow, coord.toString());

            throw new IllegalArgumentException(errorMessage);
        }
        return cellsMatrix[coord.getRow()-1][coord.getCol()-1];
    }


    public void createNewCell(Coordinate coord, String originalValue){
        cellsMatrix[coord.getRow()][coord.getCol()] = new Cell(coord,originalValue);
    }


    public void setCellsMatrix(Cell[][] cellsMatrix) {
        this.cellsMatrix = cellsMatrix;
    }

    //TODO - maybe move to Cell
    public void updateCellValueFromOriginalValue(String originalValue, Coordinate coord){

        //נבדוק אם תא זהקיים במבנה הנתונים אם לא נקצה מקום תא לו נעדכן ערך
        //Cell changeCell =currentSheet.getCellByCoord(coord);// למצוא אותו במבנה הנתונים
        //ליצור רשימה חדשה של תאים ונבצע השמה ל- רשימת התאים מהם הוא מושפע בנוסף נשמור את הרשימה הישנה במשתנה כלשהו
        List<Cell> dependsOnCellList = new LinkedList<Cell>();
        try {
            Expression expression = getExpressionOfCellCommand4(originalValue, dependsOnCellList);
            //אם הכל עבר בהצלחה

            if(getCellByCoord(coord) == null){
                createNewCell(coord,originalValue);
            }
            Cell changeCell = getCellByCoord(coord);
            changeCell.checkForCircularDependencyWrapper(coord,dependsOnCellList);
            changeCell.setExpression(expression);
            List<Cell> tmpList = changeCell.getDependsOn();
            changeCell.setDependsOn(dependsOnCellList);
            for (Cell cell : tmpList) {
                cell.removeCellFromInfluencingOnList(changeCell);
            }
            for(Cell cell : dependsOnCellList){
                cell.AddCellToInfluencingOnList(changeCell);
            }
            changeCell.checkForCircularDependencyWrapper(coord,dependsOnCellList);

//            //לעדכן את הרשימה החדשה של התלויות
//            upgradeVersion();
            //changeCell.updateCellsVersions(getVersion());

        }
        catch (Exception ex){
            //TODO throw new Exception(ex);
        }
        //נחשב את הערך
    }

    public void setCellValueFromOriginalValueCommand1(String originalValue,Coordinate coord){
        try {
            Cell changeCell = getCellByCoord(coord);
            Expression expression = getExpressionOfCellCommand1(originalValue);
            changeCell.setExpression(expression);
        }
        catch (Exception ex){
            //TODO throw new Exception(ex);
        }
        //נחשב את הערך
    }

    public static List<String> splitExpressionToStrings(String expression) {
        expression = expression.trim();
        List<String> parsed = new ArrayList<>();

        if (expression.startsWith("{") && expression.endsWith("}")) {
            expression = expression.substring(1, expression.length() - 1).trim();
        }

        int bracketDepth = 0;
        StringBuilder token = new StringBuilder();

        for (char c : expression.toCharArray()) {
            if (c == '{') {
                if (bracketDepth == 0 && token.length() > 0) {
                    parsed.add(token.toString().trim());
                    token.setLength(0);
                }
                bracketDepth++;
            } else if (c == '}') {
                bracketDepth--;
                if (bracketDepth == 0) {
                    token.append(c);
                    parsed.add(token.toString().trim());
                    token.setLength(0);
                    continue;
                }
            } else if (c == ',' && bracketDepth == 0) {
                if (token.length() > 0) {
                    parsed.add(token.toString().trim());
                    token.setLength(0);
                }
                continue;
            }
            token.append(c);
        }

        if (token.length() > 0) {
            parsed.add(token.toString().trim());
        }
        List<String> updatedList = parsed.stream()
                .filter(str -> str != null && !str.trim().isEmpty())
                .collect(Collectors.toList());
        return updatedList;
    }

    public Expression getSmallArgs(String OriginalValue){
        if (OriginalValue.trim().toLowerCase() == "false"||OriginalValue.trim().toLowerCase() == "true"){
            return new BoolExpression(false);
        }
        try{
            double num = Double.parseDouble(OriginalValue);
            return new NumberExpression(num);
        }catch (Exception e){
            return new StringExpression(OriginalValue);
        }
    }

    public Expression getExpressionOfCellCommand1(String OriginalValue) throws Exception {
        List<String> list = splitExpressionToStrings(OriginalValue);
        Expression arg2 = null;
        Expression res = null;
        if(list.size() == 1){
            res = getSmallArgs(list.get(0));
        }
        else {
            Expression arg1 = getExpressionOfCellCommand1(list.get(1));
            if(list.size() > 2 ){
                arg2 = getExpressionOfCellCommand1(list.get(2));
            }

            switch (list.get(0)) {
                case "PLUS":
                    res = new Plus(arg1, arg2);
                    break;
                case "MINUS":
                    res = new Minus(arg1, arg2);
                    break;
                case "POW":
                    res = new Pow(arg1, arg2);
                    break;
                case "ABS":
                    res = new Abs(arg1);
                    break;
                case "DIVIDE":
                    res = new Divide(arg1, arg2);
                    break;
                case "TIMES":
                    res = new Times(arg1, arg2);
                    break;
                case "MOD":
                    res = new Mod(arg1, arg2);
                    break;
                case "CONCAT":
                    res = new Concat(arg1, arg2);
                    break;
                case "SUB":
                    if (list.size() > 2) {
                        Expression arg3 = getExpressionOfCellCommand1(list.get(3));
                        res = new Sub(arg1,arg2,arg3);
                    }
                    break;
                case "REF":
                    Coordinate RefCoord = CoordinateImpl.stringToCoord(list.get(1));
                    Cell refcell = getCellByCoord(RefCoord);//find Cell in map or 2dim array and cell coord: list.get(1)
                    res = new Ref(refcell);

//להוסיף תא זה רלשימת המשפעים ומושפעים
                    break;
                default:
                    //EXEPTION
            }
        }
        return res;
    }

    public Expression getExpressionOfCellCommand4(String OriginalValue,List<Cell> dependsONCellList) throws Exception {//עוד בבדיקה !!!
        List<String> list = splitExpressionToStrings(OriginalValue);
        Expression arg2 = null;
        Expression res = null;
        if(list.size() == 1){
            res = getSmallArgs(list.get(0));
        }
        else {
            Expression arg1 = getExpressionOfCellCommand4(list.get(1),dependsONCellList);
            if(list.size() > 2 ){
                arg2 = getExpressionOfCellCommand4(list.get(2),dependsONCellList);
            }

            switch (list.get(0)) {
                case "PLUS":
                    res = new Plus(arg1, arg2);
                    break;
                case "MINUS":
                    res = new Minus(arg1, arg2);
                    break;
                case "POW":
                    res = new Pow(arg1, arg2);
                    break;
                case "ABS":
                    res = new Abs(arg1);
                    break;
                case "DIVIDE":
                    res = new Divide(arg1, arg2);
                    break;
                case "TIMES":
                    res = new Times(arg1, arg2);
                    break;
                case "MOD":
                    res = new Mod(arg1, arg2);
                    break;
                case "CONCAT":
                    res = new Concat(arg1, arg2);
                    break;
                case "SUB":
                    if (list.size() > 2) {
                        Expression arg3 = getExpressionOfCellCommand4(list.get(3),dependsONCellList);
                        res = new Sub(arg1,arg2,arg3);
                    }
                    break;
                case "REF"://sheet סטטי ?
                    //לבדוק שאין ארגומנט שלישי
                    Coordinate RefCoord = CoordinateImpl.stringToCoord(list.get(1));
                    Cell refcell = getCellByCoord(RefCoord);//find Cell in map or 2dim array and cell coord: list.get(1)
                    dependsONCellList.add(refcell); // לתא עליו התבקשנו לעדכן אערך נקצה רשימה חדשה בההתאים המשפיעים על תא זה שהיא תהיה רשימת המושפעים מהתא עליו נבצע עדכון +refcell
                    //res = refcell.getCellValue();
                    res = new Ref(refcell);

//להוסיף תא זה רלשימת המשפעים ומושפעים
                    break;
                default:
                    //EXEPTION

            }
        }
        return res;
    }

    public Cell[][] getCellsMatrix() {
        return cellsMatrix;
    }

    public boolean isCoordinateValid(Coordinate coord) {
        int row = coord.getRow();
        int col = coord.getCol();

        // בדיקה שהשורה והעמודה נמצאות בתוך הגבולות של הגיליון
        return row > 0 && row <= numberOfRows && col > 0 && col <= numberOfCols;
    }
}
    // איך לעדכן מידע של תאים בעולם של רפרנס
    // רשימה של תאים המושפעים ישירות מתא זה כלומר בהנחה שמעדכנים תא X נרמה רשימה ל X המקיימת את כל תאי Ref(x)
    // כאשר נעדכן את X נעבוא על רשימת תאי אלו ונעדכן את ערכם לאחר עדכון ערכם נעבור על רשימת Ref שלהם וכך הלאה עד שיסתיים

