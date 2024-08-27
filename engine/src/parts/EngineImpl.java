package parts;

import XMLFile.FileManager;
import parts.cell.*;
import parts.cell.coordinate.Coordinate;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


public class EngineImpl implements Engine {

    private Sheet currentSheet = null;
    private FileManager fileManager = new FileManager();
    private List <Version> versionsList = new LinkedList<Version>();
    private static final String SHEET_NOT_LOADED_MESSAGE = "Sheet is not loaded. Please load a sheet before attempting to access it.";


    //1
    public void readFileData(String filePath) throws Exception {
        try {
            //Sheet lastSheet = currentSheet;
            currentSheet = fileManager.processFile(filePath);
            versionsList.clear();
            addVersion(currentSheet, currentSheet.howManyActiveCellsInSheet());
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
    public CellDTO getCellDTOByCoordinate(Coordinate coordinate) throws IllegalArgumentException{
        if (!sheetLoadad()) {
            throw new IllegalStateException(SHEET_NOT_LOADED_MESSAGE);
        }

        Cell cell=currentSheet.getCellByCoord(coordinate);
        if(cell!= null){
            return cell.toCellDTO();
        }
        return null;

    }
    //3
    public int getLastVersionOfEmptyCell(Coordinate coordinate){
        return currentSheet.getEmptyCellVersion(coordinate);
    }


    //4
    public void updateCellValue(String originalValue, Coordinate coord) throws Exception {
        if (!sheetLoadad()) {
            throw new IllegalStateException(SHEET_NOT_LOADED_MESSAGE);
        }

        Sheet clonedSheet = currentSheet.cloneSheet();
        int numOfCellsChanged;

        if (clonedSheet != null) {
            Cell cell=clonedSheet.getCellByCoord(coord);
            clonedSheet.upgradeVersion(); //פה או בתוך התנאים?

            if(cell != null){
                clonedSheet.updateCellValue(originalValue,cell);
                numOfCellsChanged = clonedSheet.upgradeCellsVersionsAndGetNumOfChanges();
            }
            else{
                cell = clonedSheet.createNewCellForCommand4(originalValue,coord);
                clonedSheet.upgradeCellVersion(cell);
                numOfCellsChanged=1;
            }

            currentSheet = clonedSheet;
            addVersion(currentSheet,numOfCellsChanged);

        }
        //else - stay as it was before and throw exception

//        catch (Exception e) {
//            throw new Exception(e.getMessage());
//        }

    }

    //5
    public SheetDTO getSheetDTOByVersion(int versionNumber) throws IllegalArgumentException, IllegalStateException {
        if (!sheetLoadad()) {
            throw new IllegalStateException(SHEET_NOT_LOADED_MESSAGE);
        }

        return getSheetByVersion(versionNumber).toSheetDTO();
    }

    //5
    public List<Integer> getVersions(){
        if (!sheetLoadad()) {
            throw new IllegalStateException(SHEET_NOT_LOADED_MESSAGE);
        }
        return getNumberOfCellsChangedListDeepClone();
    }


    public boolean sheetLoadad() {
        return currentSheet != null;
    }

    public Sheet getSheetByVersion(int version) throws IllegalArgumentException {

        if(versionsList.size()<version){
            throw new IllegalArgumentException("Version: "+version+" not created yet.");
        } else if (version<1){
            throw new IllegalArgumentException("Version: "+version+" is not valid. Version number must be a positive integer from 1 to "+versionsList.size()+".");
        }
        return versionsList.get(version-1).getSheet();
    }

    public void addVersion( Sheet sheet,int numberOfCellsChanged) {
        versionsList.addLast(new Version(sheet,numberOfCellsChanged));

    }

    public List<Integer> getNumberOfCellsChangedListDeepClone() {
        return versionsList.stream()
                .map(Version::getNumberOfCellsChanged)
                .collect(Collectors.toList());
    }


}