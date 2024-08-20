package XMLFile;

import parts.Sheet;

import java.io.File;
import java.io.FileNotFoundException;

public class FileManager {
    //validate path
    //load from xml to stlsheet
    //stl sheet check
    //stlsheet to sheet

    public Sheet processFile(String filePath) {

        validatePath(filePath);

    }


    public void validatePath(String filePath) throws FileNotFoundException, IllegalArgumentException {
        File file = new File(filePath);

        if (!file.exists()) {
            throw new FileNotFoundException("File does not exist: " + filePath);
        }

        if (!filePath.endsWith(".xml")) {
            throw new IllegalArgumentException("The file is not an XML file: " + filePath);
        }
    }




}
