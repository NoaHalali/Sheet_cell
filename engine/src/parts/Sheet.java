package parts;

import parts.cell.*;
import parts.cell.impl.BoolExpression;
import parts.cell.impl.NumberExpression;
import parts.cell.impl.StringExpression;
import parts.cell.impl.function.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Sheet {
    private int  version;
    private String name;
    private int numberOfRows;
    private int numberOfCols;
    private int columnWidth;
    private int rowHeight;
    private Cell[][] cellsMatrix; // מערך דו-ממדי של תאים

    public Sheet(String name, int numberOfRows, int numberOfCols, int columnWidth, int rowHeight) {
        this.name = name;
        this.numberOfRows = numberOfRows;
        this.numberOfCols = numberOfCols;
        this.columnWidth = columnWidth;
        this.rowHeight = rowHeight;

//        this.columnWidth = 4; // לדוגמה, אפשר להתאים את הערך על פי הדרישה
//        this.rowHeight = 4; // לדוגמה, אפשר להתאים את הערך על פי הדרישה
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

    public Cell GetCellByCoord(Coordinate coord){
        //בדיקות תקינות

        return cellsMatrix[coord.getRow()][coord.getCol()];
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


    public int getVersionNumber() {
        return version;
    }

//    //TODO - Move to UI or something
//    public void printSheetData()
//    {
//        System.out.println("Version: " + version);
//        System.out.println("Sheet Name: " + name);
//        System.out.println();
//        printCellsMatrix();
//    }

    public void CreateNewCell(Coordinate coord,String originalValue){
        cellsMatrix[coord.getRow()][coord.getCol()] = new Cell(coord,originalValue);
    }

//    public void printCellsMatrix() {
//        // ריפוד לרוחב השורה עבור מספרי השורות
//        for (int i = 0; i < 3; i++) {
//            System.out.print(" ");
//        }
//
//        // הדפסת שמות העמודות
//        for (int col = 0; col < numberOfCols; col++) {
//            char columnName = (char) ('A' + col);
//            System.out.print("|" + columnName);
//            // הוספת רווחים בהתאם לרוחב העמודה
//            for (int i = 1; i < columnWidth; i++) {
//                System.out.print(" ");
//            }
//        }
//        System.out.println();
//
//        // הדפסת התאים בשורות ובעמודות
//        for (int row = 0; row < numberOfRows; row++) {
//            // הדפסת מספר שורה בפורמט של שתי ספרות
//            String rowNumber = String.format("%02d", row + 1);
//            System.out.print(rowNumber + " ");
//
//            for (int col = 0; col < numberOfCols; col++) {
//                Cell cell = cellsMatrix[row][col];
//                String cellEffectiveValue = cell != null ? String.valueOf(cell.geEffectiveValue().getValue()) : ""; //צריך?
//
//                System.out.print("|");
//                int strIndex = 0;
//                while (strIndex < cellEffectiveValue.length() && strIndex < columnWidth) {
//                    System.out.print(cellEffectiveValue.charAt(strIndex));
//                    strIndex++;
//                }
//
//                while(strIndex < columnWidth)
//                {
//                    System.out.print(" ");
//                    strIndex++;
//                }
//
//                //TODO - in the next missions - add the option of overflow to next line (if possible according to the height of cell)
//
////                הדפסת ערך התא
////                System.out.print("|" + cellEffectiveValue);
////               הוספת רווחים אם התוכן קצר יותר מרוחב העמודה
////                for (int i = cellEffectiveValue.length(); i < columnWidth; i++) {
////                    System.out.print(" ");
////                }
//            }
//            System.out.println(); // מעבר לשורה הבאה
//        }
//    }

    public void setCellsMatrix(Cell[][] cellsMatrix) {
        this.cellsMatrix = cellsMatrix;
    }

    public void updateCellValueFromOriginalValue(String originalValue,Coordinate coord){

        //נבדוק אם תא זהקיים במבנה הנתונים אם לא נקצה מקום תא לו נעדכן ערך
        //Cell changeCell =currentSheet.GetCellByCoord(coord);// למצוא אותו במבנה הנתונים
        //ליצור רשימה חדשה של תאים ונבצע השמה ל- רשימת התאים מהם הוא מושפע בנוסף נשמור את הרשימה הישנה במשתנה כלשהו
        List<Cell> dependsOnCellList = new LinkedList<Cell>();
        try {
            Expression expression = getExpressionOfCell(originalValue, dependsOnCellList);
            //אם הכל עבר בהצלחה

            if(GetCellByCoord(coord) == null){
                CreateNewCell(coord,originalValue);
            }
            Cell changeCell = GetCellByCoord(coord);
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
            //לעדכן את הרשימה החדשה של התלויות
            upgradeVersion();
            changeCell.UpdateCellEffectiveValue(getVersion());

        }
        catch (Exception ex){
            //TODO throw new Exception(ex);
        }
        //נחשב את הערך
    }
    public static List<String> parseExpression(String expression) {
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

    public Expression getExpressionOfCell(String OriginalValue,List<Cell> dependsONCellList) throws Exception {//עוד בבדיקה !!!
        List<String> list = parseExpression(OriginalValue);
        Expression arg2 = null;
        Expression res = null;
        if(list.size() == 1){
            res = getSmallArgs(list.get(0));
        }
        else {
            Expression arg1 = getExpressionOfCell(list.get(1),dependsONCellList);
            if(list.size() > 2 ){
                arg2 = getExpressionOfCell(list.get(2),dependsONCellList);
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
                        Expression arg3 = getExpressionOfCell(list.get(3),dependsONCellList);
                        res = new Sub(arg1,arg2,arg3);
                    }
                    break;
                case "REF"://sheet סטטי ?
                    //לבדוק שאין ארגומנט שלישי
                    Coordinate RefCoord = CoordinateImpl.StringToCoord(list.get(1));
                    Cell refcell = GetCellByCoord(RefCoord);//find Cell in map or 2dim array and cell coord: list.get(1)
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
    // איך לעדכן מידע של תאים בעולם של רפרנס
    // רשימה של תאים המושפעים ישירות מתא זה כלומר בהנחה שמעדכנים תא X נרמה רשימה ל X המקיימת את כל תאי Ref(x)
    // כאשר נעדכן את X נעבוא על רשימת תאי אלו ונעדכן את ערכם לאחר עדכון ערכם נעבור על רשימת Ref שלהם וכך הלאה עד שיסתיים

}
