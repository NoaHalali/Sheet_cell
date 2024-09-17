package parts;

import XMLFile.FileManager;
import parts.cell.*;
import parts.sheet.cell.Cell;
import parts.sheet.cell.coordinate.Coordinate;
import parts.sheet.Sheet;
import parts.sheet.cell.expression.effectiveValue.EffectiveValue;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
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

    @Override
    public boolean sheetLoadad() {
        return currentSheet != null;
    }

    //1
    @Override
    public void readFileData(String filePath) throws Exception {
        try {
            //Sheet lastSheet = currentSheet;
            currentSheet = fileManager.processFile(filePath);
            versionsList.clear();
            addVersion(currentSheet, currentSheet.howManyActiveCellsInSheet());
        }
        catch (Exception e) {
           e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }


    //2
    @Override
    public SheetDTO getCurrentSheetDTO() {
        if (!sheetLoadad()) {
            throw new IllegalStateException(SHEET_NOT_LOADED_MESSAGE);
        }
        return currentSheet.toSheetDTO(); //temp
    }

    //3
    @Override
    public CellDTO getCellDTOByCoordinate(Coordinate coordinate) throws IllegalArgumentException {
        if (!sheetLoadad()) {
            throw new IllegalStateException(SHEET_NOT_LOADED_MESSAGE);
        }

        Cell cell = currentSheet.getCellByCoord(coordinate);
        if (cell != null)
        {
           return cell.toCellDTO();
        }
        return new EmptyCellDTO(coordinate, 0, null);

    }


    //4
    @Override
    public boolean updateCellValue(String newOriginalValue, Coordinate coord) throws Exception {
        if (!sheetLoadad()) {
            throw new IllegalStateException(SHEET_NOT_LOADED_MESSAGE);
        }
        Sheet clonedSheet = currentSheet.cloneSheet();
        int numOfCellsChanged;

        if (clonedSheet != null) {
            Cell cell = clonedSheet.getCellByCoord(coord);
            // clonedSheet.upgradeVersion(); //פה או בתוך התנאים?
            boolean isDeleted = false;
            String oldOriginalValue;

            if (cell != null) {
                oldOriginalValue = cell.getOriginalValue();
                boolean originalValueChanged = !newOriginalValue.equals(oldOriginalValue);

                if (originalValueChanged) {
                    clonedSheet.updateCellValue(newOriginalValue, cell);

                    numOfCellsChanged = clonedSheet.upgradeCellsVersionsAndGetNumOfChanges();
                    if (numOfCellsChanged == 0) {
                        clonedSheet.upgradeCellVersion(cell);

                    }
                } else { // original value didn't change
                    return false;
                }
            }
            else { // cell is null
                cell = clonedSheet.createNewCellForCommand4(newOriginalValue, coord);
                clonedSheet.upgradeCellVersion(cell);
                numOfCellsChanged = 1;

            }


            addVersion(clonedSheet, numOfCellsChanged);
            currentSheet = clonedSheet;

        }
        return true;
    }

    //5
    @Override
    public SheetDTO getSheetDTOByVersion(int versionNumber) throws IllegalArgumentException, IllegalStateException {
        if (!sheetLoadad()) {
            throw new IllegalStateException(SHEET_NOT_LOADED_MESSAGE);
        }

        return getSheetByVersion(versionNumber).toSheetDTO();
    }

    //5 - לא באמת מחזיר גרסאות, יותר רשימה של מספרי תאים שהשתנו
    @Override
    public List<Integer> getVersions(){
        if (!sheetLoadad()) {
            throw new IllegalStateException(SHEET_NOT_LOADED_MESSAGE);
        }
        return getNumberOfCellsChangedListDeepClone();
    }

    //5
    @Override
    public int getNumOfVersions(){
        if (!sheetLoadad()) {
            throw new IllegalStateException(SHEET_NOT_LOADED_MESSAGE);
        }
        return versionsList.size();
    }

    //5
    @Override
    public Sheet getSheetByVersion(int version) throws IllegalArgumentException {

        if(versionsList.size()<version){
            throw new IllegalArgumentException("Version: "+version+" not created yet.");
        } else if (version<1){
            throw new IllegalArgumentException("Version: "+version+" is not valid. Version number must be a positive integer from 1 to "+versionsList.size()+".");
        }
        return versionsList.get(version-1).getSheet();
    }

    //5
    @Override
    public void addVersion( Sheet sheet,int numberOfCellsChanged) {
        versionsList.addLast(new Version(sheet,numberOfCellsChanged));
    }

    //5
    @Override
    public List<Integer> getNumberOfCellsChangedListDeepClone() {
        return versionsList.stream()
                .map(Version::getNumberOfCellsChanged)
                .collect(Collectors.toList());
    }


    //6
    @Override
    public void saveSystemState(String filePath) throws Exception {
        if (!sheetLoadad()) {
            throw new IllegalStateException(SHEET_NOT_LOADED_MESSAGE);
        }
        String filePathWithEnding = filePath + ".dat";

        try (FileOutputStream fileOut = new FileOutputStream(filePathWithEnding);
             ObjectOutputStream oos = new ObjectOutputStream(fileOut)) {
            oos.writeObject(currentSheet);
            oos.writeObject(versionsList);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save system state to file: " + filePath, e);
        }
    }


    //7
    @Override
    public void loadSystemState(String filePath) throws FileNotFoundException, IOException, ClassNotFoundException {

        String filePathWithEnding = filePath + ".dat";

        try (FileInputStream fileIn = new FileInputStream(filePathWithEnding);
             ObjectInputStream ois = new ObjectInputStream(fileIn)) {
            currentSheet = (Sheet) ois.readObject();
            versionsList = (List<Version>) ois.readObject();
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File not found: " + filePath);
        } catch (IOException e) {
            throw new IOException("Error reading the file: " + filePath, e);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Class not found while loading the system state", e);
        }
    }
    //8
    @Override
    public void addRange(String rangeName, String rangeDefinition) throws Exception
    {
        if (!sheetLoadad()) {
            throw new IllegalStateException(SHEET_NOT_LOADED_MESSAGE);
        }

        Coordinate[] rangeEdgeCoordinates=Range.parseRange(rangeDefinition);
        Coordinate topLeftCoord = rangeEdgeCoordinates[0];
        Coordinate bottomRightCoord = rangeEdgeCoordinates[1];
        currentSheet.addRange(rangeName, topLeftCoord, bottomRightCoord);
    }

    @Override
    public List<Coordinate> getRangeCoordinates(String rangeName) {
        return currentSheet.getRangeCoordinates(rangeName);
    }

    @Override
    public void deleteRange(String selectedRangeName) {
        currentSheet.deleteRange(selectedRangeName);
    }

    public List<String> getRangesNames(){
        return currentSheet.getRangesNames();
    }

    @Override
    public SheetDTO getSortedSheetDTO(String rangeDefinition, List<Character> columnsToSortBy) throws IllegalArgumentException {
        return currentSheet.getSortedSheet(rangeDefinition, columnsToSortBy).toSheetDTO();
    }

    @Override
    public Set<EffectiveValue> getDistinctValuesOfColInRange(String col,String rangeDefinition) throws IllegalArgumentException {
        Coordinate[] rangeEdgeCoordinates =Range.parseRange(rangeDefinition);
        Coordinate topLeftCoord = rangeEdgeCoordinates[0];
        Coordinate bottomRightCoord = rangeEdgeCoordinates[1];
        return currentSheet.getDistinctValuesOfColInRange(col, topLeftCoord, bottomRightCoord);
    }

}