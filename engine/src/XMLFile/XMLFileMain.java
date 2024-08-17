package XMLFile;

import XMLFile.GeneratedFiles.STLSheet;
import jakarta.xml.bind.JAXBException;

public class XMLFileMain {
    public static void main(String[] args) throws JAXBException {
        String filePath = "path/to/your/basic.xml";

        XMLProcessor processor = new XMLProcessor();
        processor.process(filePath);

        STLSheet sheet = processor.getSheet();

        System.out.println("Sheet Name: " + sheet.getName());
        System.out.println("Number of Rows: " + sheet.getSTLLayout().getRows());
        System.out.println("Number of Columns: " + sheet.getSTLLayout().getColumns());
        sheet.getSTLCells().getSTLCell().forEach(cell -> {
            System.out.println("Cell [" + cell.getRow() + ", " + cell.getColumn() + "]: " + cell.getSTLOriginalValue());
        });

    }
}
