package XMLFile;

import XMLFile.GeneratedFiles.STLSheet;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import parts.Sheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileManager {
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "XMLFile/GeneratedFiles";
    //validate path
    //load from xml to stlsheet
    //stl sheet check
    //stlsheet to sheet

    public Sheet processFile(String filePath) throws FileNotFoundException,IllegalArgumentException {

        validatePath(filePath);
        STLSheet

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

    public  STLSheet loadXML(String filePath)
    {
        STLSheet sheet=null;
        try {
            InputStream inputStream = new FileInputStream(new File(filePath));

            sheet = deserializeFrom(inputStream);

        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return sheet;
    }

    private STLSheet deserializeFrom(InputStream in) throws JAXBException {

        JAXBContext context = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (STLSheet) unmarshaller.unmarshal(in);
    }
}




}
