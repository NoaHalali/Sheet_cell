package parts;

import XMLFile.FileManager;
import parts.cell.*;
import parts.cell.coordinate.Coordinate;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;



public class EngineImpl implements Engine {

    private Sheet currentSheet = null;
    private FileManager fileManager = new FileManager();
    private List <Version> versionsList = new LinkedList<Version>();
    private static final String SHEET_NOT_LOADED_MESSAGE = "Sheet is not loaded. Please load a sheet before attempting to access it.";

    public boolean sheetLoadad() {
        return currentSheet != null;
    }

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

    //5
    public Sheet getSheetByVersion(int version) throws IllegalArgumentException {

        if(versionsList.size()<version){
            throw new IllegalArgumentException("Version: "+version+" not created yet.");
        } else if (version<1){
            throw new IllegalArgumentException("Version: "+version+" is not valid. Version number must be a positive integer from 1 to "+versionsList.size()+".");
        }
        return versionsList.get(version-1).getSheet();
    }

    //4
    public void addVersion( Sheet sheet,int numberOfCellsChanged) {
        versionsList.addLast(new Version(sheet,numberOfCellsChanged));

    }

    //5
    public List<Integer> getNumberOfCellsChangedListDeepClone() {
        return versionsList.stream()
                .map(Version::getNumberOfCellsChanged)
                .collect(Collectors.toList());
    }


    //6
    public void saveSystemState(String filePath) throws Exception{
        if (!sheetLoadad()) {
            throw new IllegalStateException(SHEET_NOT_LOADED_MESSAGE);
        }
        String filePathWithEnding = filePath + ".dat";
        try (FileOutputStream fileOut = new FileOutputStream(filePathWithEnding);
             ObjectOutputStream oos = new ObjectOutputStream(fileOut)) {
            oos.writeObject(currentSheet);
            //System.out.println("System state saved to " + filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save system state to file: " + filePath, e);
        }
    }

    //7
    public void loadSystemState(String filePath) throws FileNotFoundException, IOException, ClassNotFoundException {
        String filePathWithEnding = filePath + ".dat";
        try (FileInputStream fileIn = new FileInputStream(filePathWithEnding);
             ObjectInputStream ois = new ObjectInputStream(fileIn)) {
            currentSheet = (Sheet) ois.readObject();
            resetVersions();  // אפס את הגרסאות לאחר טעינה
            addVersion(currentSheet, currentSheet.howManyActiveCellsInSheet());

        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File not found: " + filePath);
        } catch (IOException e) {
            throw new IOException("Error reading the file: " + filePath, e);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Class not found while loading the system state", e);
        }
    }

    public void resetVersions() {
        versionsList.clear();  // אפס את רשימת הגרסאות
        if (currentSheet != null) {
            currentSheet.setVersion(1);  // עדכן את הגרסה של הגיליון ל-1
        }
    }

}