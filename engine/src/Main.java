import XMLFile.FileManager;
import parts.EngineImpl;
import parts.Sheet;
import parts.SheetDTO;
import parts.cell.Cell;
import parts.cell.impl.BoolExpression;
import parts.cell.impl.NumberExpression;
import parts.cell.impl.StringExpression;

import java.io.File;

public class Main {

//    private final static String JAXB_XML_GAME_PACKAGE_NAME = "XMLFile/GeneratedFiles";
    private final static String FILE_PATH = "C:\\Users\\noa40\\OneDrive - The Academic College of Tel-Aviv Jaffa - MTA\\שנה ב\\קורסי בחירה\\פיתוח תוכנה מבוסס גאווה\\מטלות\\Shticell\\engine\\src\\resources\\basic.xml";

    public static void main(String[] args) {
        EngineImpl engine = new EngineImpl();
        try {
            //File xmlFile = new File("path/to/your/basic.xml");
            engine.readFileData(FILE_PATH);
            SheetDTO sheet = engine.getCurrentSheetDTO();
            System.out.println("Hi");


        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            //System.out.println("ERROR");
        }

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
//
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
//
//}
//    public static void main(String[] args) {
//
//        try {
//            File xmlFile = new File("path/to/your/basic.xml");
//            JAXBContext context = JAXBContext.newInstance(STLSheet.class);
//            Unmarshaller unmarshaller = context.createUnmarshaller();
//            STLSheet sheet = (STLSheet) unmarshaller.unmarshal(xmlFile);
//
//            // עבודה עם האובייקטים
//            System.out.println("Sheet Name: " + sheet.getName());
//            System.out.println("Number of Rows: " + sheet.getLayout().getRows());
//            System.out.println("Number of Columns: " + sheet.getLayout().getColumns());
//            sheet.getCells().getCells().forEach(cell -> {
//                System.out.println("Cell [" + cell.getRow() + ", " + cell.getColumn() + "]: " + cell.getOriginalValue());
//            });
//
//        } catch (JAXBException e) {
//            e.printStackTrace();
//        }
//    }


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




