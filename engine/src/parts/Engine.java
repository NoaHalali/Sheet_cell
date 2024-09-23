package parts;

import exceptions.SheetNotLoadedException;
import parts.cell.CellDTO;
import parts.sheet.cell.coordinate.Coordinate;
import parts.sheet.Sheet;
import parts.sheet.cell.expression.effectiveValue.EffectiveValue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Engine {

    public boolean sheetLoadad();

    public void readFileData(String filePath) throws Exception;

    public SheetDTO getCurrentSheetDTO() throws SheetNotLoadedException;

    public CellDTO getCellDTOByCoordinate(Coordinate coordinate) throws IllegalArgumentException, SheetNotLoadedException;

    public boolean updateCellValue(String value, Coordinate coordinate) throws Exception;

    public SheetDTO getSheetDTOByVersion(int versionNumber) throws IllegalArgumentException, SheetNotLoadedException;

    public List<Integer> getVersions() throws SheetNotLoadedException;

    public int getNumOfVersions() throws SheetNotLoadedException;

    public Sheet getSheetByVersion(int version) throws IllegalArgumentException;

    public void addVersion(Sheet sheet, int numberOfCellsChanged);

    public void saveSystemState(String filePath) throws Exception;

    public List<Integer> getNumberOfCellsChangedListDeepClone();

    public void loadSystemState(String filePath) throws FileNotFoundException, IOException, ClassNotFoundException;

    public void addRange(String rangeName, String rangeDefinition) throws Exception;

    public List<Coordinate> getRangeCoordinates(String rangeName);

    public void deleteRange(String selectedRangeName);

    public List<String> getRangesNames();

    public SheetDTO getSortedSheetDTO(String rangeDefinition, List<Character> columnsToSortBy) throws IllegalArgumentException;

    public Set<EffectiveValue> getDistinctValuesOfColInRange(String col, String rangeDefinition) throws IllegalArgumentException;

    public SheetDTO getFilteredSheetDTO(Set<EffectiveValue> filteredValues, String col,String rangeDefinition) throws IllegalArgumentException;

    public List<CellDTO> getColumnData(String colStr);

    public void checkIfSheetHasBeenLoaded() throws SheetNotLoadedException;

    public int getNumberOfColumns();

    public Map<String,Set<EffectiveValue>> getDistinctValuesOfMultipleColsInRange(List<Character> columnsToFilterBy, String rangeDefinition) throws IllegalArgumentException;

    public SheetDTO getFilteredSheetDTOFromMultipleCols(Map<String,Set<EffectiveValue>> filteredValues,String rangeDefinition) throws IllegalArgumentException;

    public SheetDTO calculateWhatIfValueForCell(double value,Coordinate coord);
}


