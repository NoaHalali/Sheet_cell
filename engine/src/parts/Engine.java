package parts;

import parts.cell.CellDTO;
import parts.sheet.cell.coordinate.Coordinate;
import parts.sheet.Sheet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface Engine {

    public boolean sheetLoadad();

    public void readFileData(String filePath) throws Exception;

    public SheetDTO getCurrentSheetDTO();

    public CellDTO getCellDTOByCoordinate(Coordinate coordinate) throws IllegalArgumentException;

    public boolean updateCellValue(String value, Coordinate coordinate) throws Exception;

    public SheetDTO getSheetDTOByVersion(int versionNumber) throws IllegalArgumentException, IllegalStateException;

    public List<Integer> getVersions();

    public int getNumOfVersions();

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
}


