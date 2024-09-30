package shticell.sheets.sheet.parts.cell.expression.impl.functions.functions;

import shticell.parts.function.*;
import shticell.sheets.sheet.Sheet;
import shticell.sheets.sheet.parts.Range;
import shticell.sheets.sheet.parts.cell.Cell;
import shticell.sheets.sheet.parts.cell.coordinate.Coordinate;
import shticell.sheets.sheet.parts.cell.coordinate.CoordinateImpl;
import shticell.sheets.sheet.parts.cell.expression.Expression;
import shticell.sheets.sheet.parts.cell.expression.impl.BoolExpression;
import shticell.sheets.sheet.parts.cell.expression.impl.NumberExpression;
import shticell.sheets.sheet.parts.cell.expression.impl.StringExpression;
import shticell.sheets.sheet.parts.cell.expression.impl.functions.functions.logic.*;
import shticell.sheets.sheet.parts.cell.expression.impl.functions.functions.math.*;
import shticell.sheets.sheet.parts.cell.expression.impl.functions.functions.string.Concat;
import shticell.sheets.sheet.parts.cell.expression.impl.functions.functions.string.Sub;

import java.util.ArrayList;
import java.util.List;

public  class FunctionParser {


    public static List<String> splitExpressionToStrings(String expression) {
        expression = expression;
        List<String> parsed = new ArrayList<>();

        if (expression.startsWith("{") && expression.endsWith("}")) {
            expression = expression.substring(1, expression.length() - 1);
        }

        int bracketDepth = 0;
        StringBuilder token = new StringBuilder();

        for (char c : expression.toCharArray()) {
            if (c == '{') {
                if (bracketDepth == 0 && token.length() > 0) {
                    parsed.add(token.toString());
                    token.setLength(0);
                }
                bracketDepth++;
            } else if (c == '}') {
                bracketDepth--;
                if (bracketDepth == 0) {
                    token.append(c);
                    parsed.add(token.toString());
                    token.setLength(0);
                    continue;
                }
            } else if (c == ',' && bracketDepth == 0) {
                if (token.length() > 0) {
                    parsed.add(token.toString());
                    token.setLength(0);
                }
                continue;
            }
            token.append(c);
        }

        if (token.length() > 0) {
            parsed.add(token.toString());
        }
//        List<String> updatedList = parsed.stream()
//                .filter(str -> str != null && !str.trim().isEmpty())
//                .collect(Collectors.toList());
        return parsed;
    }

    public static Expression getSmallArgs(String OriginalValue){
        if (OriginalValue.trim().equalsIgnoreCase("FALSE") || OriginalValue.trim().equalsIgnoreCase("TRUE")){
            return new BoolExpression(OriginalValue.trim().equalsIgnoreCase("TRUE"));
        }
        try{
            double num = Double.parseDouble(OriginalValue);
            return new NumberExpression(num);
        }catch (Exception e){
            return new StringExpression(OriginalValue);
        }
    }


    //מחזירה אקספרשיון מערך מקור
    public static Expression getExpressionOfCell(String OriginalValue, List<Cell> dependsOnCellList, List<Range>rangesDependsOnList, Sheet evalSheet) throws Exception {
        List<String> list = splitExpressionToStrings(OriginalValue);
        Expression arg2 = null,arg3 = null, res = null;
        Range range;
        String rangeName;
        if(list.size() == 1){
            res = getSmallArgs(list.get(0));
        }
        else {

            Expression  arg1 = getExpressionOfCell(list.get(1), dependsOnCellList, rangesDependsOnList, evalSheet);

            if(list.size() > 2 ){
                arg2 = getExpressionOfCell(list.get(2), dependsOnCellList,rangesDependsOnList,evalSheet);
            }
            switch (list.get(0).toUpperCase()) {
                case"AVERAGE":
                    if(list.size() != 2){
                        throw new IllegalArgumentException("AVERAGE function expected to get 1 arguments");
                    }
                    rangeName=list.get(1);
                    range=evalSheet.getRange(rangeName);
                    rangesDependsOnList.add(range);
                    res = new Average(range);
                    break;
                case "SUM":
                    if(list.size() != 2){
                        throw new IllegalArgumentException("SUM function expected to get 1 arguments");
                    }
                    rangeName=list.get(1);
                    range = evalSheet.getRange(rangeName);
                    rangesDependsOnList.add(range);
                    res = new Sum(range);
                    break;
                case "EQUAL":
                    if(list.size() != 3){
                        throw new IllegalArgumentException("EQUAL function expected to get 2 arguments") ;
                    }
                    res = new Equal(arg1, arg2);
                    break;
                case "BIGGER":
                    if(list.size() != 3){
                        throw new IllegalArgumentException("BIGGER function expected to get 2 arguments") ;
                    }
                    res =new Bigger(arg1, arg2);
                    break;
                case "LESS":
                    if(list.size() != 3){
                        throw new IllegalArgumentException("LESS function expected to get 2 arguments") ;
                    }
                    res = new Less(arg1, arg2);
                    break;
                case "OR":
                    if(list.size() != 3){
                        throw new IllegalArgumentException("OR function expected to get 2 arguments") ;
                    }
                    res = new Or(arg1, arg2);
                    break;
                case "NOT":
                    if(list.size() != 2){
                        throw new IllegalArgumentException("NOT function expected to get 1 arguments") ;
                    }
                    res= new Not(arg1);
                    break;
                case "AND":
                    if(list.size() != 3){
                        throw new IllegalArgumentException("AND function expected to get 2 arguments") ;
                    }
                    res = new And(arg1, arg2);
                    break;
                case "IF":
                    if(list.size() != 4){
                        throw new IllegalArgumentException("IF function expected to get 3 arguments") ;
                    }
                    arg3 = getExpressionOfCell(list.get(3), dependsOnCellList,rangesDependsOnList,evalSheet);
                    res = new If(arg1,arg2,arg3);
                    break;
                case "PERCENT":
                    if(list.size() != 3){
                        throw new IllegalArgumentException("PERCENT function expected to get 2 arguments") ;
                    }
                    res = new Percent(arg1, arg2);
                    break;
                case "PLUS":
                    if(list.size() != 3){
                        throw new IllegalArgumentException("PLUS function expected to get 2 arguments") ;
                    }
                    res = new Plus(arg1, arg2);
                    break;
                case "MINUS":
                    if(list.size() != 3){
                        throw new IllegalArgumentException("MINUS function expected to get 2 arguments") ;
                    }
                    res = new Minus(arg1, arg2);
                    break;
                case "POW":
                    if(list.size() != 3){
                        throw new IllegalArgumentException("POW function expected to get 2 arguments") ;
                    }
                    res = new Pow(arg1, arg2);
                    break;
                case "ABS":
                    if(list.size() != 2){
                        throw new IllegalArgumentException("ABS function expected to get 1 arguments") ;
                    }
                    res = new Abs(arg1);
                    break;
                case "DIVIDE":
                    if(list.size() != 3){
                        throw new IllegalArgumentException("DIVIDE function expected to get 2 arguments") ;
                    }
                    res = new Divide(arg1, arg2);
                    break;
                case "TIMES":
                    if(list.size() != 3){
                        throw new IllegalArgumentException("TIMES function expected to get 2 arguments") ;
                    }
                    res = new Times(arg1, arg2);
                    break;
                case "MOD":
                    if(list.size() != 3){
                        throw new IllegalArgumentException("MOD function expected to get 2 arguments") ;
                    }
                    res = new Mod(arg1, arg2);
                    break;
                case "CONCAT":
                    if(list.size() != 3){
                        throw new IllegalArgumentException("CONCAT function expected to get 2 arguments") ;
                    }
                    res = new Concat(arg1, arg2);
                    break;
                case "SUB":
                    if(list.size() != 4){
                        throw new IllegalArgumentException("SUB function expected to get 3 arguments") ;
                    }
                    arg3 = getExpressionOfCell(list.get(3), dependsOnCellList,rangesDependsOnList,evalSheet);
                    res = new Sub(arg1,arg2,arg3);
                    break;
                case "REF"://sheet סטטי ?
                    if(list.size() != 2){
                        throw new IllegalArgumentException("REF function expected to get 1 arguments") ;
                    }
                    Coordinate refCoord = CoordinateImpl.parseCoordinate(list.get(1));
                    evalSheet.validateCoordinateBounds(refCoord);
                    Cell refcell = evalSheet.getCellByCoord(refCoord);//find Cell in map or 2dim array and cell coord: list.get(1)
                    if(refcell == null){
                        evalSheet.CreateNewEmptyCell(refCoord);
                        refcell = evalSheet.getCellByCoord(refCoord);
                    }
                    dependsOnCellList.add(refcell); // לתא עליו התבקשנו לעדכן אערך נקצה רשימה חדשה בההתאים המשפיעים על תא זה שהיא תהיה רשימת המושפעים מהתא עליו נבצע עדכון +refcell
                    res = new Ref(refcell);

                    break;
                default:
                    throw new IllegalArgumentException("Illegal function name. " + list.get(0) + " is not an option.");
            }
        }
        return res;
    }

}
//    public static List<String> splitExpressionToStrings(String expression) {
//        expression = expression;
//        List<String> parsed = new ArrayList<>();
//
//        if (expression.startsWith("{") && expression.endsWith("}")) {
//            expression = expression.substring(1, expression.length() - 1);
//        }
//
//        int bracketDepth = 0;
//        StringBuilder token = new StringBuilder();
//
//        for (char c : expression.toCharArray()) {
//            if (c == '{') {
//                if (bracketDepth == 0 && token.length() > 0) {
//                    parsed.add(token.toString());
//                    token.setLength(0);
//                }
//                bracketDepth++;
//            } else if (c == '}') {
//                bracketDepth--;
//                if (bracketDepth == 0) {
//                    token.append(c);
//                    parsed.add(token.toString());
//                    token.setLength(0);
//                    continue;
//                }
//            } else if (c == ',' && bracketDepth == 0) {
//                if (token.length() > 0) {
//                    parsed.add(token.toString());
//                    token.setLength(0);
//                }
//                continue;
//            }
//            token.append(c);
//        }
//
//        if (token.length() > 0) {
//            parsed.add(token.toString());
//        }
////        List<String> updatedList = parsed.stream()
////                .filter(str -> str != null && !str.trim().isEmpty())
////                .collect(Collectors.toList());
//        return parsed;
//    }
//
//    public Expression getSmallArgs(String OriginalValue){
//        if (OriginalValue.trim().equalsIgnoreCase("FALSE") || OriginalValue.trim().equalsIgnoreCase("TRUE")){
//            return new BoolExpression(OriginalValue.trim().equalsIgnoreCase("TRUE"));
//        }
//        try{
//            double num = Double.parseDouble(OriginalValue);
//            return new NumberExpression(num);
//        }catch (Exception e){
//            return new StringExpression(OriginalValue);
//        }
//    }
//
//
//    //מחזירה אקספרשיון מערך מקור
//    public Expression getExpressionOfCell(String OriginalValue, List<Cell> dependsOnCellList) throws Exception {
//        List<String> list = splitExpressionToStrings(OriginalValue);
//        Expression arg2 = null,arg3 = null, res = null;
//        Range range;
//        if(list.size() == 1){
//            res = getSmallArgs(list.get(0));
//        }
//        else {
//            Expression arg1 = getExpressionOfCell(list.get(1), dependsOnCellList);
//            if(list.size() > 2 ){
//                arg2 = getExpressionOfCell(list.get(2), dependsOnCellList);
//            }
//            switch (list.get(0).toUpperCase()) {
//                case"AVERAGE":
//                    if(list.size() != 2){
//                        throw new IllegalArgumentException("AVERAGE function expected to get 1 arguments");
//                    }
//                     range=getRange(list.get(1));
//                    res = new Average(range);
//                    break;
//                case "SUM":
//                    if(list.size() != 2){
//                        throw new IllegalArgumentException("SUM function expected to get 1 arguments");
//                    }
//                    range=getRange(list.get(1));
//                    res = new Sum(range);
//                    break;
//                case "EQUAL":
//                    if(list.size() != 3){
//                        throw new IllegalArgumentException("EQUAL function expected to get 2 arguments") ;
//                    }
//                    res = new Equal(arg1, arg2);
//                    break;
//                case "BIGGER":
//                    if(list.size() != 3){
//                        throw new IllegalArgumentException("BIGGER function expected to get 2 arguments") ;
//                    }
//                    res =new Bigger(arg1, arg2);
//                    break;
//                case "LESS":
//                    if(list.size() != 3){
//                        throw new IllegalArgumentException("LESS function expected to get 2 arguments") ;
//                    }
//                    res = new Less(arg1, arg2);
//                    break;
//                case "OR":
//                    if(list.size() != 3){
//                        throw new IllegalArgumentException("OR function expected to get 2 arguments") ;
//                    }
//                    res = new Or(arg1, arg2);
//                    break;
//                case "NOT":
//                     if(list.size() != 2){
//                         throw new IllegalArgumentException("NOT function expected to get 1 arguments") ;
//                     }
//                     res= new Not(arg1);
//                     break;
//                case "AND":
//                    if(list.size() != 3){
//                        throw new IllegalArgumentException("AND function expected to get 2 arguments") ;
//                    }
//                    res = new And(arg1, arg2);
//                    break;
//                case "IF":
//                    if(list.size() != 4){
//                        throw new IllegalArgumentException("IF function expected to get 3 arguments") ;
//                    }
//                    arg3 = getExpressionOfCell(list.get(3), dependsOnCellList);
//                    res = new If(arg1,arg2,arg3);
//                    break;
//                case "PERCENT":
//                    if(list.size() != 3){
//                        throw new IllegalArgumentException("PERCENT function expected to get 2 arguments") ;
//                    }
//                    res = new Percent(arg1, arg2);
//                case "PLUS":
//                    if(list.size() != 3){
//                        throw new IllegalArgumentException("PLUS function expected to get 2 arguments") ;
//                    }
//                    res = new Plus(arg1, arg2);
//                    break;
//                case "MINUS":
//                    if(list.size() != 3){
//                        throw new IllegalArgumentException("MINUS function expected to get 2 arguments") ;
//                    }
//                    res = new Minus(arg1, arg2);
//                    break;
//                case "POW":
//                    if(list.size() != 3){
//                        throw new IllegalArgumentException("POW function expected to get 2 arguments") ;
//                    }
//                    res = new Pow(arg1, arg2);
//                    break;
//                case "ABS":
//                    if(list.size() != 2){
//                        throw new IllegalArgumentException("ABS function expected to get 1 arguments") ;
//                    }
//                    res = new Abs(arg1);
//                    break;
//                case "DIVIDE":
//                    if(list.size() != 3){
//                        throw new IllegalArgumentException("DIVIDE function expected to get 2 arguments") ;
//                    }
//                    res = new Divide(arg1, arg2);
//                    break;
//                case "TIMES":
//                    if(list.size() != 3){
//                        throw new IllegalArgumentException("TIMES function expected to get 2 arguments") ;
//                    }
//                    res = new Times(arg1, arg2);
//                    break;
//                case "MOD":
//                    if(list.size() != 3){
//                        throw new IllegalArgumentException("MOD function expected to get 2 arguments") ;
//                    }
//                    res = new Mod(arg1, arg2);
//                    break;
//                case "CONCAT":
//                    if(list.size() != 3){
//                        throw new IllegalArgumentException("CONCAT function expected to get 2 arguments") ;
//                    }
//                    res = new Concat(arg1, arg2);
//                    break;
//                case "SUB":
//                    if(list.size() != 4){
//                        throw new IllegalArgumentException("SUB function expected to get 3 arguments") ;
//                    }
//                    arg3 = getExpressionOfCell(list.get(3), dependsOnCellList);
//                    res = new Sub(arg1,arg2,arg3);
//                    break;
//                case "REF"://sheet סטטי ?
//                    if(list.size() != 2){
//                        throw new IllegalArgumentException("REF function expected to get 1 arguments") ;
//                    }
//                    Coordinate refCoord = CoordinateImpl.parseCoordinate(list.get(1));
//                    validateCoordinateBounds(refCoord);
//                    Cell refcell = getCellByCoord(refCoord);//find Cell in map or 2dim array and cell coord: list.get(1)
//                    if(refcell == null){
//                        CreateNewEmptyCell(refCoord);
//                        refcell = getCellByCoord(refCoord);
//                    }
//                    dependsOnCellList.add(refcell); // לתא עליו התבקשנו לעדכן אערך נקצה רשימה חדשה בההתאים המשפיעים על תא זה שהיא תהיה רשימת המושפעים מהתא עליו נבצע עדכון +refcell
//                    res = new Ref(refcell);
//
//                    break;
//                default:
//                    throw new IllegalArgumentException("Illegal function name. " + list.get(0) + " is not an option.");
//            }
//        }
//        return res;
//    }
