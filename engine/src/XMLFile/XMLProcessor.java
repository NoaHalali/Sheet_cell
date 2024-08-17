package XMLFile;

import XMLFile.GeneratedFiles.STLSheet;
import jakarta.xml.bind.JAXBException;

public class XMLProcessor {

    private final Loader loader;
    private final Validator validator;
    private STLSheet sheet;  // שדה שמייצג את אובייקט ה-STLSheet שעובד

    public XMLProcessor() {
        this.loader = new Loader();
        this.validator = new Validator();
    }

    public void process(String filePath) {

        // טעינת ה-XML
        this.sheet = loader.loadXML(filePath);

        if (validator.validate(filePath)) {
            System.out.println("XML file loaded and managed successfully.");
        } else {
            System.out.println("Invalid XML file.");


//         catch (JAXBException e) {
//            System.err.println("Failed to load XML file: " + e.getMessage());
//            e.printStackTrace();
//        }

        }
    }

    public STLSheet getSheet() {
        return this.sheet;
    }
}
