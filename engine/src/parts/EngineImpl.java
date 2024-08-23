package parts;

import XMLFile.FileManager;
import parts.cell.*;
import parts.cell.coordinate.Coordinate;

import java.io.FileNotFoundException;

public class EngineImpl implements Engine {

    private Sheet currentSheet = null;
    private FileManager fileManager = new FileManager();
    private static final String SHEET_NOT_LOADED_MESSAGE = "Sheet is not loaded. Please load a sheet before attempting to access it.";


    //1
    public void readFileData(String filePath) throws Exception {
        try {
            currentSheet = fileManager.processFile(filePath);
        }
        catch (Exception e) {
            currentSheet = null;
            throw new Exception(e.getMessage());
        }

    }

    //2
    public SheetDTO getCurrentSheetDTO() {
        if (!sheetLoadad()) {
            throw new IllegalStateException(SHEET_NOT_LOADED_MESSAGE);
        }

        return currentSheet.toSheetDTO(); //temp
    }

    //3
    public CellDTO getCellDTOByCoordinate(Coordinate coordinate) {
        if (!sheetLoadad()) {
            throw new IllegalStateException(SHEET_NOT_LOADED_MESSAGE);
        }
        Cell cell=currentSheet.getCellByCoord(coordinate);

        if(cell!= null){
            return cell.toCellDTO();
        }
        return null;

    }


    //4 , ???
    public void updateCellValueFromOriginalValue(String originalValue, Coordinate coord) throws Exception {
        if (!sheetLoadad()) {
            throw new IllegalStateException(SHEET_NOT_LOADED_MESSAGE);
        }

        currentSheet.updateCellValueFromOriginalValue(originalValue, coord);
    }

    //5
    public void showVersions() {
        if (!sheetLoadad()) {
            throw new IllegalStateException(SHEET_NOT_LOADED_MESSAGE);
        }

        //TODO

    }

    public void exit() {
        //אם מממשים שמירה בקובץ אז לשמור ולצאת
        //אחרת פשוט לצאת וזהו?
    }


    public boolean sheetLoadad() {
        return currentSheet != null;
    }

}
