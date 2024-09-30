package engine.impl;

import engine.XMLFile.FileManager;
import engine.api.Engine;
import engine.exceptions.SheetNotLoadedException;
import engine.parts.Range;
import engine.parts.Version;
import engine.parts.sheet.Sheet;
import engine.parts.sheet.cell.Cell;
import engine.parts.sheet.cell.coordinate.Coordinate;
import engine.parts.sheet.cell.expression.effectiveValue.EffectiveValue;
import engine.parts.sheet.cell.expression.impl.NumberExpression;
import parts.SheetDTO;
import parts.cell.*;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public class EngineImpl implements Engine {
    private Sheet currentSheet = null;
    public Sheet whatIfSheet = null;
    public Coordinate whatIfCSelectedCoordinate = null;
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
        //Sheet lastSheet = currentSheet;
        validatePath(filePath);
        InputStream inputStream = new FileInputStream(new File(filePath));
        currentSheet = fileManager.processFile(inputStream);
        versionsList.clear();
        addVersion(currentSheet, currentSheet.howManyActiveCellsInSheet());
    }

    //2
    @Override
    public SheetDTO getCurrentSheetDTO() throws SheetNotLoadedException {
        checkIfSheetHasBeenLoaded();
        return currentSheet.toSheetDTO(); //temp
    }

    //3
    @Override
    public CellDTO getCellDTOByCoordinate(Coordinate coordinate) throws IllegalArgumentException, SheetNotLoadedException {
        checkIfSheetHasBeenLoaded();

        Cell cell = currentSheet.getCellByCoord(coordinate);
        if (cell != null)
        {
           return cell.toCellDTO();
        }
        return new EmptyCellDTO(coordinate, 0, List.of());
    }

    //4
    @Override
    public boolean updateCellValue(String newOriginalValue, Coordinate coord) throws Exception {
        checkIfSheetHasBeenLoaded();
        Sheet clonedSheet = currentSheet.cloneSheet();
        int numOfCellsChanged;

        if (clonedSheet != null) {
            Cell cell = clonedSheet.getCellByCoord(coord);
            String oldOriginalValue;
            if (cell != null) {
                oldOriginalValue = cell.getOriginalValue();
                boolean originalValueChanged = !newOriginalValue.equals(oldOriginalValue);
                boolean tryToDeleteGhostCell = newOriginalValue.isEmpty();

                if (originalValueChanged) {
                    clonedSheet.updateCellValue(newOriginalValue, cell);

                    numOfCellsChanged = clonedSheet.upgradeCellsVersionsAndGetNumOfChanges();
                    if (numOfCellsChanged == 0) {
                        clonedSheet.upgradeCellVersion(cell);

                    }
                }
                else{ // original value didn't change
                    if(tryToDeleteGhostCell){ //trying to delete ghost cell
                        throw new Exception("Cell at coordinate "+coord+" is already empty!");
                    }
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
    public SheetDTO getSheetDTOByVersion(int versionNumber) throws IllegalArgumentException, SheetNotLoadedException {
        checkIfSheetHasBeenLoaded();
        return getSheetByVersion(versionNumber).toSheetDTO();
    }

    //5 - לא באמת מחזיר גרסאות, יותר רשימה של מספרי תאים שהשתנו
    @Override
    public List<Integer> getVersions() throws SheetNotLoadedException {
        checkIfSheetHasBeenLoaded();
        return getNumberOfCellsChangedListDeepClone();
    }

    //5
    @Override
    public int getNumOfVersions() throws SheetNotLoadedException {
        checkIfSheetHasBeenLoaded();
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
        checkIfSheetHasBeenLoaded();
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
        checkIfSheetHasBeenLoaded();
        Coordinate[] rangeEdgeCoordinates= Range.parseRange(rangeDefinition);
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
    public Set<EffectiveValue> getDistinctValuesOfColInRange(String colStr, String rangeDefinition) throws IllegalArgumentException {
        Coordinate[] rangeEdgeCoordinates =Range.parseRange(rangeDefinition);
        Coordinate topLeftCoord = rangeEdgeCoordinates[0];
        Coordinate bottomRightCoord = rangeEdgeCoordinates[1];
        return currentSheet.getDistinctValuesOfSingleColInRange(colStr, topLeftCoord, bottomRightCoord);
    }
    @Override
    public Map<String,Set<EffectiveValue>> getDistinctValuesOfMultipleColsInRange(List<Character> columnsToFilterBy, String rangeDefinition) throws IllegalArgumentException {
        Coordinate[] rangeEdgeCoordinates =Range.parseRange(rangeDefinition);
        Coordinate topLeftCoord = rangeEdgeCoordinates[0];
        Coordinate bottomRightCoord = rangeEdgeCoordinates[1];
        return currentSheet.getDistinctValuesOfColumnsInRange(columnsToFilterBy,topLeftCoord,bottomRightCoord);
    }

    @Override
    public SheetDTO getFilteredSheetDTO(Set<EffectiveValue> filteredValues, String colStr,String rangeDefinition) throws IllegalArgumentException {
        Coordinate[] rangeEdgeCoordinates =Range.parseRange(rangeDefinition);
        Coordinate topLeftCoord = rangeEdgeCoordinates[0];
        Coordinate bottomRightCoord = rangeEdgeCoordinates[1];
        Sheet filteredSheet = currentSheet.cloneSheet();

        return filteredSheet.getFilteredSheetBySingleColumnInRange(filteredValues, colStr, topLeftCoord, bottomRightCoord).toSheetDTO();

    }
    @Override
    public SheetDTO getFilteredSheetDTOFromMultipleCols(Map<String,Set<EffectiveValue>> filteredValues,String rangeDefinition) throws IllegalArgumentException {
        Coordinate[] rangeEdgeCoordinates =Range.parseRange(rangeDefinition);
        Coordinate topLeftCoord = rangeEdgeCoordinates[0];
        Coordinate bottomRightCoord = rangeEdgeCoordinates[1];

        return currentSheet.getFilteredSheetByMultipleColumnsInRange(filteredValues, topLeftCoord, bottomRightCoord).toSheetDTO();

    }

    public List<CellDTO> getColumnDataInRange(String rangeDefinition) throws IllegalArgumentException{
        Coordinate[] rangeEdgeCoordinates =Range.parseRange(rangeDefinition);
        Coordinate topCoord = rangeEdgeCoordinates[0];
        Coordinate bottomCoord = rangeEdgeCoordinates[1];
        return currentSheet.getColumnCellsInRange(topCoord, bottomCoord);
    }


    @Override
    public void checkIfSheetHasBeenLoaded() throws SheetNotLoadedException {
        if (!sheetLoadad()) {
            throw new SheetNotLoadedException(SHEET_NOT_LOADED_MESSAGE);
        }
    }
    @Override
    public void setEngineInWhatIfMode(Coordinate coord)throws IllegalStateException {
        boolean isCellValidForWhatIf = currentSheet.getCellByCoord(coord).IsCellExpressionIsNumber();
        if(!isCellValidForWhatIf){
            throw new IllegalStateException ("must select cell with simple number original value");
        }
        whatIfCSelectedCoordinate=coord;
        whatIfSheet=currentSheet.cloneSheet();

    }
    @Override
    public SheetDTO calculateWhatIfValueForCell(double value){
        Cell cell =whatIfSheet.getCellByCoord(whatIfCSelectedCoordinate);
        cell.setExpression(new NumberExpression(value));
        return whatIfSheet.toSheetDTO();
    }

    public int getNumberOfColumns(){
        return currentSheet.getNumberOfColumns();
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