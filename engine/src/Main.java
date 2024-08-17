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

        String  S ="{MINUS,{Minus,4{PLUS,4,5}},{POW,2,3}}";
        List<String> list = parseExpressionS(S);
        list.stream().forEach(System.out::println);

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
    public static List<String> parseExpressionS(String expression) {
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

}






