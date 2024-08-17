package XMLFile;

import java.io.File;

public class Validator {

    public static boolean validate(String filePath) {
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("File does not exist.");
            return false;
        }

        if (!filePath.endsWith(".xml")) {
            System.out.println("The file is not an XML file.");
            return false;
        }

        return true;
    }
}
