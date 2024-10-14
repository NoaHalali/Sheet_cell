package shticell.engines.sheetEngine;

import parts.SheetDetailsDTO;
import shticell.exceptions.SheetNotLoadedException;
import shticell.permissions.PermissionType;
import shticell.permissions.PermissionsManager;
import shticell.sheets.sheet.parts.Range;
import shticell.sheets.sheet.parts.Version;
import shticell.sheets.sheet.Sheet;
import shticell.sheets.sheet.parts.cell.Cell;
import shticell.sheets.sheet.parts.cell.coordinate.Coordinate;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValue;
import shticell.sheets.sheet.parts.cell.expression.impl.NumberExpression;
import parts.SheetDTO;
import parts.cell.CellDTO;

import java.util.*;
import java.util.stream.Collectors;

public class SheetEngineImpl implements SheetEngine {

    private Sheet currentSheet = null;
    //private FileManager fileManager = new FileManager();
    private List <Version> versionsList = new LinkedList<Version>();
    private String owner;
    private final PermissionsManager permissionsManager = new PermissionsManager();
    private static final String SHEET_NOT_LOADED_MESSAGE = "Sheet is not loaded. Please load a sheet before attempting to access it.";
    public Sheet whatIfSheet = null;
    public Coordinate whatIfCSelectedCoordinate = null;

    @Override
    public boolean sheetLoadad() {
        return currentSheet != null;
    }

    public SheetEngineImpl(Sheet sheet,String owner) {
        currentSheet = sheet;
        this.owner = owner;
        //versionsList.clear();
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
        return new CellDTO(coordinate,null,null, 0,List.of(), List.of());
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

    //8
    @Override
    public void addRange(String rangeName, String rangeDefinition) throws IllegalArgumentException
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
    public void deleteRange(String selectedRangeName) throws IllegalArgumentException {
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
//    @Override
//    public Sheet getClonedSheet(){
//        return currentSheet.cloneSheet();
//    }

    public int getNumberOfColumns(){
        return currentSheet.getNumberOfColumns();
    }

    @Override
    public SheetDetailsDTO getSheetDetailsDTO() {
        String size = currentSheet.getSizeString();
        String sheetName = currentSheet.getSheetName();
        return new SheetDetailsDTO(owner, sheetName, size);
    }

    @Override
    public void givePermissionToUser(String usernameFromParameter, PermissionType permission) throws IllegalArgumentException {
        permissionsManager.givePermissionToUser(usernameFromParameter, permission);
    }

    @Override
    public void addUserPermissionRequest(String usernameFromParameter, PermissionType permission) throws IllegalArgumentException {
        permissionsManager.addUserPermissionRequest(usernameFromParameter, permission);
    }
}
