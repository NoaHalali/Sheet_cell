package XMLFile;

import XMLFile.GeneratedFiles.STLSheet;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Loader {

    private final static String JAXB_XML_GAME_PACKAGE_NAME = "XMLFile/GeneratedFiles";

    public static STLSheet loadXML(String filePath)
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

    private static STLSheet deserializeFrom(InputStream in) throws JAXBException {

        JAXBContext context = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (STLSheet) unmarshaller.unmarshal(in);
    }
}
