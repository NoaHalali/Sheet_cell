package shticell.engines.sheetEngine;

import parts.SheetDTO;
import parts.SheetDetailsDTO;
import parts.cell.CellDTO;
import parts.permission.UserRequestDTO;
import shticell.exceptions.OutdatedSheetVersionException;
import shticell.exceptions.SheetNotLoadedException;
import shticell.permissions.PermissionType;
import shticell.permissions.PermissionsManager;
import shticell.sheets.sheet.Sheet;
import shticell.sheets.sheet.parts.cell.coordinate.Coordinate;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValue;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SheetEngine {

    public boolean sheetLoadad();

    public SheetDTO getCurrentSheetDTO() throws SheetNotLoadedException;

    public CellDTO getCellDTOByCoordinate(Coordinate coordinate) throws IllegalArgumentException, SheetNotLoadedException;

    public boolean updateCellValue(String value, Coordinate coordinate) throws Exception;

    public SheetDTO getSheetDTOByVersion(int versionNumber) throws IllegalArgumentException, SheetNotLoadedException;

    public List<Integer> getVersions() throws SheetNotLoadedException;

    public int getNumOfVersions() throws SheetNotLoadedException;

    public Sheet getSheetByVersion(int version) throws IllegalArgumentException;

    public void addVersion(Sheet sheet, int numberOfCellsChanged);

    public List<Integer> getNumberOfCellsChangedListDeepClone();

    public void addRange(String rangeName, String rangeDefinition) throws IllegalArgumentException;

    public List<Coordinate> getRangeCoordinates(String rangeName);

    public void deleteRange(String selectedRangeName) throws IllegalArgumentException;

    public List<String> getRangesNames();

    public SheetDTO getSortedSheetDTO(String rangeDefinition, List<Character> columnsToSortBy) throws IllegalArgumentException;

    public Set<EffectiveValue> getDistinctValuesOfColInRange(String col, String rangeDefinition) throws IllegalArgumentException;

    public SheetDTO getFilteredSheetDTO(Set<EffectiveValue> filteredValues, String col,String rangeDefinition) throws IllegalArgumentException;

    public List<CellDTO> getColumnDataInRange(String rangeDefinition) throws IllegalArgumentException;

    public void checkIfSheetHasBeenLoaded() throws SheetNotLoadedException;

    public int getNumberOfColumns();

    public Map<String,Set<EffectiveValue>> getDistinctValuesOfMultipleColsInRange(List<Character> columnsToFilterBy, String rangeDefinition) throws IllegalArgumentException;

    public SheetDTO getFilteredSheetDTOFromMultipleCols(Map<String,Set<EffectiveValue>> filteredValues,String rangeDefinition) throws IllegalArgumentException;

    public SheetDTO calculateWhatIfValueForCell(double value);

    public void setEngineInWhatIfMode(Coordinate coord)throws IllegalStateException ;

    //public Sheet getClonedSheet();

    public SheetDetailsDTO getSheetDetailsDTOForUser(String username) throws IllegalArgumentException;

    public void addUserPermissionRequest(String usernameFromParameter, PermissionType permission) throws IllegalArgumentException;

    public void approvePermissionRequest(int requestNumber) throws IllegalArgumentException;

    public void denyPermissionRequest(int requestNumber) throws IllegalArgumentException ;

    public List<UserRequestDTO> getRequestsDTOList() throws IllegalArgumentException;

    public PermissionsManager getPermissionsManager();

    public int getCurrentVersion();

    public void checkIfVersionIsUpdated(String versionStr) throws OutdatedSheetVersionException;

    public boolean isVersionUpToDate(String versionStr) throws IllegalArgumentException;
}


