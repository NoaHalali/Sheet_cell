import parts.Sheet;
import parts.cell.Cell;
import parts.cell.Expression;
import parts.cell.impl.BoolExpression;
import parts.cell.impl.NumberExpression;
import parts.cell.impl.StringExpression;
import parts.cell.impl.function.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public Main() {
    }

    public static void main(String[] args) {

        Sheet sheet = new Sheet(2, 3);
        Cell[][] matrix = new Cell[2][3];

        int value = 1;

//        for (int i = 0; i < 2; i++) {
//            for (int j = 0; j < 3; j++) {
//                matrix[i][j]=new Cell();
//                matrix[i][j].updateValue(String.valueOf(value));
//                value++;
//            }
//        }

        String  S ="1";
        List<String> list = parseExpression(S);
        list.stream().forEach(System.out::println);
        List<String> updatedList = list.stream()
                .filter(str -> str != null && !str.trim().isEmpty())
                .collect(Collectors.toList());
        System.out.println(list.size());
        updatedList.stream().forEach(System.out::println);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                matrix[i][j] = new Cell();
            }
            matrix[i][0].updateValue(new NumberExpression(value));
            value++;
            matrix[i][1].updateValue(new BoolExpression(false));
            matrix[i][2].updateValue(new StringExpression("Amir"));

        }




        //sheet.printSheetCell();

//        Expression i=new Plus(new Plus(new Number(7),new Number(5)),new Plus(new Number(1),new Number(3)));
//        String t=   i.evaluate();
//        System.out.println(t);
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

        return parsed;
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

    public Expression getExpression(String OriginalValue) {//עוד בבדיקה !!!
        List<String> list = parseExpression(OriginalValue);
        Expression res=null;
        if(list.size() == 1){
            res= getSmallArgs(list.get(0));
        }
        else {
            Expression arg1 = getExpression(list.get(1));
            Expression arg2 = getExpression(list.get(2));
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
                        Expression arg3 = getExpression(list.get(3));
                        res= new Sub(arg1,arg2,arg3);
                    }
                    break;
                case "REF"://sheet סטטי ?
                    Cell refcell;//מוצאים את התא במבנה התונים של sheet
                    // לתא עליו התבקשנו לעדכן אערך נקצה רשימה חדשה בההתאים המשפיעים על תא זה שהיא תהיה רשימת המושפעים מהתא עליו נבצע עדכון +refcell
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
}






