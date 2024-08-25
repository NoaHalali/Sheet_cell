package parts;

import XMLFile.FileManager;
import parts.cell.*;
import parts.cell.coordinate.Coordinate;

public class EngineImpl implements Engine {

    private Sheet currentSheet = null;
    private FileManager fileManager = new FileManager();
    private Version versions =new Version();
    private static final String SHEET_NOT_LOADED_MESSAGE = "Sheet is not loaded. Please load a sheet before attempting to access it.";


    //1
    public void readFileData(String filePath) throws Exception {
        try {
            //Sheet lastSheet = currentSheet;
            currentSheet = fileManager.processFile(filePath);
            versions.addVersion(currentSheet);
        }
        catch (Exception e) {
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


    //4
    public void updateCellValue(String originalValue, Coordinate coord) throws Exception {
        if (!sheetLoadad()) {
            throw new IllegalStateException(SHEET_NOT_LOADED_MESSAGE);
        }

        Sheet clonedSheet = currentSheet.cloneSheet();

        if (clonedSheet != null) {
            if(clonedSheet.getCellByCoord(coord)==null){
               clonedSheet.createNewCellValueFromOriginalValue(originalValue,coord);
               clonedSheet.updateVersionForSheetAndCreatedCell(coord);
            }else{
                clonedSheet.updateCellValueFromOriginalValue(originalValue,coord);
                clonedSheet.upgradeCellsVersions();
            }
            clonedSheet.upgradeCellsVersions();
            currentSheet = clonedSheet;
            versions.addVersion(currentSheet);

        }
        //else - stay as it was before and throw exception

//        catch (Exception e) {
//            throw new Exception(e.getMessage());
//        }

    }

    //5
    public SheetDTO showVersions(int versionNumber) throws Exception {
        if (!sheetLoadad()) {
            throw new IllegalStateException(SHEET_NOT_LOADED_MESSAGE);
        }

        return versions.getSpecificVersion(versionNumber).toSheetDTO();

    }

    public void exit() {
        //אם מממשים שמירה בקובץ אז לשמור ולצאת
        //אחרת פשוט לצאת וזהו?
    }


    public boolean sheetLoadad() {
        return currentSheet != null;
    }

}
