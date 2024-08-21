package parts;

import XMLFile.FileManager;
import XMLFile.GeneratedFiles.STLSheet;
import parts.cell.*;
import parts.cell.impl.BoolExpression;
import parts.cell.impl.NumberExpression;
import parts.cell.impl.StringExpression;
import parts.cell.impl.function.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class EngineImpl implements Engine{

    //maybe static? (Amir, REF function)
    private  Sheet currentSheet = null;
    private FileManager fileManager = null; ;
    private static final String SHEET_NOT_LOADED_MESSAGE = "Sheet is not loaded. Please load a sheet before attempting to access it.";

    //TODO move all functions to UI,

    //1
    // change names to get and not print/show
    public void readFileData(String filePath) throws FileNotFoundException ,IllegalArgumentException {
          currentSheet = fileManager.processFile(filePath);

//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Please enter the full path to the XML file: ");
//        String filePath = scanner.nextLine();
//
//        STLSheet sheet = Loader.loadXML(filePath);
//        Validator.validate()

    }



    //2
    //TODO - maybe return data (DTO?) and not void?
    public SheetDTO getCurrentSheetDTO() {
        if(!sheetLoadad()) {
            throw new IllegalStateException(SHEET_NOT_LOADED_MESSAGE);
        }

        //currentSheet.toSheetDTO();
        return null; //temp
    }

    //3
    public CellDTO getCellDTOByCoordinate(Coordinate coordinate){
        if(!sheetLoadad()) {
            throw new IllegalStateException(SHEET_NOT_LOADED_MESSAGE);
        }

        return currentSheet.GetCellByCoord(coordinate).toCellDTO();

    }


    //4 , ???
    public void updateCellValueFromOriginalValue(String originalValue,Coordinate coord){
        if(!sheetLoadad()) {
            throw new IllegalStateException(SHEET_NOT_LOADED_MESSAGE);
        }

        currentSheet.updateCellValueFromOriginalValue(originalValue,coord);
    }

//    //4
//    public void updeteCellValue(Cell cell, Expression value) //?
//    {
//        cell.updateValue(value);
//        //.............. להמשיך
//        //......
//    }

    //5
    public void showVersions()
    {
        if(!sheetLoadad()) {
            throw new IllegalStateException(SHEET_NOT_LOADED_MESSAGE);
        }

        //TODO

    }

    public void exit()
    {
        //אם מממשים שמירה בקובץ אז לשמור ולצאת
        //אחרת פשוט לצאת וזהו?
    }


    public boolean sheetLoadad()
    {
        return currentSheet != null;
    }
//    public CellDTO getCellData(String cellId) {
//        /*
//        TODO
//         Parse Cell
//         return cell.toCellDTO
//        */
//
//        return null;
//    }
}
