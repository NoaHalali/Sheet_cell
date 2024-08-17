import parts.Sheet;
import parts.cell.Cell;
import parts.cell.Expression;
import parts.cell.impl.BoolExpression;
import parts.cell.impl.NumberExpression;
import parts.cell.impl.StringExpression;
import parts.cell.impl.function.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    private final static String JAXB_XML_GAME_PACKAGE_NAME = "GeneratedFiles";

    public static void main(String[] args) {

        try {
            InputStream inputStream = new FileInputStream(new File("src/resources/world.xml"));

            Countries countries = deserializeFrom(inputStream);
            System.out.println("name of first country is: " + countries.getCountry().get(0).getName());
        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    private static Countries deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (Countries) u.unmarshal(in);



//        Sheet sheet = new Sheet(2, 3);
//
//        Cell[][] matrix = new Cell[2][3];
//
//        int value = 1;
//
//        for (int i = 0; i < 2; i++) {
//            for (int j = 0; j < 3; j++) {
//                matrix[i][j] = new Cell();
//            }
//            matrix[i][0].updateValue(new NumberExpression(value));
//            value++;
//            matrix[i][1].updateValue(new BoolExpression(false));
//            matrix[i][2].updateValue(new StringExpression("Amir"));
//        }
//
//        sheet.setCellsMatrix((matrix));
//        sheet.printSheetData();

    }

}

//String  S ="1";
// List<String> list = parseExpression(S);
//list.stream().forEach(System.out::println);
//        Expression i=new Plus(new Plus(new Number(7),new Number(5)),new Plus(new Number(1),new Number(3)));
//        String t=   i.evaluate();
//        System.out.println(t);

//        for (int i = 0; i < 2; i++) {
//            for (int j = 0; j < 3; j++) {
//                matrix[i][j]=new Cell();
//                matrix[i][j].updateValue(String.valueOf(value));
//                value++;
//            }
//        }




