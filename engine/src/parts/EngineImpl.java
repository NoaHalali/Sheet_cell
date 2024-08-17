package parts;

import XMLFile.GeneratedFiles.STLSheet;
import XMLFile.Loader;
import XMLFile.Validator;
import parts.cell.*;
import parts.cell.impl.BoolExpression;
import parts.cell.impl.NumberExpression;
import parts.cell.impl.StringExpression;
import parts.cell.impl.function.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class EngineImpl implements Engine{

    //maybe static? (Amir, REF function)
    private  Sheet currentSheet = null;

    //TODO move all functions to UI,

    //1
    // change names to get and not print/show
    public void readFileData()
    {
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Please enter the full path to the XML file: ");
//        String filePath = scanner.nextLine();
//
//        STLSheet sheet = Loader.loadXML(filePath);
//        Validator.validate()

    }



    //2
    //TODO - maybe return data (DTO?) and not void?
    public void showCurrentSheet() {
        currentSheet.printSheetData();
    }

    //3
    public void ShowCellState(Coordinate coord){

        System.out.println("Cell identity: " + coord.toString());
        Cell cell = currentSheet.GetCellByCoord(coord);

        String originalValue = cell.getOriginalValue();
        System.out.println("Original Value: " + originalValue);

        //TODO - implement the toString()
        EffectiveValue effectiveValue = cell.getEffectiveValue();
        System.out.println("Effective Value: " + effectiveValue);

        int version = cell.getLastUpdatedVersion();
        System.out.println("Last Updated Version: " + version);

        List<String> dependsOnNames = cell.getDependsOnNames();
        System.out.println("DependsOn Names: " + dependsOnNames);

        List<String> influencingOn = cell.getInfluencingOnNames();
        System.out.println("InfluencingOn Names: " + influencingOn);
    }


    //4 , ???
    public void updateCellValueFromOriginalValue(String originalValue){
        //נבדוק אם תא זהקיים במבנה הנתונים אם לא נקצה מקום תא לו נעדכן ערך
        Cell changeCell =new Cell();// למצוא אותו במבנה הנתונים
       //ליצור רשימה חדשה של תאים ונבצע השמה ל- רשימת התאים מהם הוא מושפע בנוסף נשמור את הרשימה הישנה במשתנה כלשהו
        List<Cell> newAffectByCellList=new LinkedList<Cell>();
        Expression expression = getExpressionForCell(changeCell,originalValue,newAffectByCellList);//אם הכל עבר בהצלחה
        // נעבור על הרשימה העדכנית של changecell עבוא התאים שמשפיע עליהם ועבור כל תא נוציא את changecell מרשימת התאים שמפעים עליהם
        // נעדכן את הרשימה של changecell לרשימה זו
        //נחשב את הערך
    }

//    //4
//    public void updeteCellValue(Cell cell, Expression value) //?
//    {
//        cell.updateValue(value);
//        //.............. להמשיך
//        //......
//    }

    //5
    public void showVersions()
    {

    }

    public void exit()
    {
        //אם מממשים שמירה בקובץ אז לשמור ולצאת
        //אחרת פשוט לצאת וזהו?
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
        if (OriginalValue.trim().toLowerCase()=="false"||OriginalValue.trim().toLowerCase()=="true"){
            return new BoolExpression(false);
        }
        try{
            double num=Double.parseDouble(OriginalValue);
            return new NumberExpression(num);
        }catch (Exception e){
            return new StringExpression(OriginalValue);
        }
    }

    public Expression getExpressionForCell(Cell SourceCell,String OriginalValue,List<Cell> newAffectByCellList) {//עוד בבדיקה !!!
        List<String> list = parseExpression(OriginalValue);
        Expression res=null;
        if(list.size() == 1){
            res= getSmallArgs(list.get(0));
        }
        else {
            Expression arg1 = getExpressionForCell( SourceCell,list.get(1),newAffectByCellList);
            Expression arg2 = getExpressionForCell(SourceCell,list.get(2),newAffectByCellList);
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
                        Expression arg3 = getExpressionForCell(SourceCell,list.get(3),newAffectByCellList);
                        res= new Sub(arg1,arg2,arg3);
                    }
                    break;
                case "REF"://sheet סטטי ?
                    Cell refcell=null;//find Cell in map or 2dim array and cell coord: list.get(1)
                    newAffectByCellList.add(refcell); // לתא עליו התבקשנו לעדכן אערך נקצה רשימה חדשה בההתאים המשפיעים על תא זה שהיא תהיה רשימת המושפעים מהתא עליו נבצע עדכון +refcell

                    //res =refcell.getCellValue
//להוסיף תא זה רלשימת המשפעים ומושפעים


                    break;
                default:


            }
        }
        return res;
    }// איך לעדכן מידע של תאים בעולם של רפרנס
    // רשימה של תאים המושפעים ישירות מתא זה כלומר בהנחה שמעדכנים תא X נרמה רשימה ל X המקיימת את כל תאי Ref(x)
    // כאשר נעדכן את X נעבוא על רשימת תאי אלו ונעדכן את ערכם לאחר עדכון ערכם נעבור על רשימת Ref שלהם וכך הלאה עד שיסתיים

//    public CellDTO getCellData(String cellId) {
//        /*
//        TODO
//         Parse Cell
//         return cell.toCellDTO
//        */
//
//        return null;
//    }
}
