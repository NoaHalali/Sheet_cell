package parts.sheet;

import parts.FunctionParser;
import parts.Range;
import parts.SheetDTO;
import parts.cell.*;
import parts.sheet.cell.Cell;
import parts.sheet.cell.coordinate.Coordinate;
import parts.sheet.cell.coordinate.CoordinateImpl;
import parts.sheet.cell.expression.Expression;
import parts.sheet.cell.expression.effectiveValue.EffectiveValue;

import java.io.*;
import java.util.*;

public class Sheet implements Serializable {
    private static final long serialVersionUID = 1L;
    private int  version;
    private final String name;
    private final int numberOfRows;
    private final int numberOfCols;
    private final int columnWidth;
    private final int rowHeight;
    private Cell[][] cellsMatrix; // מערך דו-ממדי של תאים
    private Map<String, Range> ranges = new HashMap<String,Range>();
    //private Map<String,Integer> deletedCells= new HashMap<String,Integer>();
    private static final char minCol = 'A';
    private static final int minRow = 1;
    private final char maxCol;
    private final int maxRow;


    public Sheet(String name, int numberOfRows, int numberOfCols, int columnWidth, int rowHeight) {
        this.name = name;
        this.numberOfRows = numberOfRows;
        this.numberOfCols = numberOfCols;
        this.columnWidth = columnWidth;
        this.rowHeight = rowHeight;
        this.version = 1;
        this.maxCol = (char) (minCol + numberOfCols - 1);
        this.maxRow = numberOfRows;

    }

    public String getSheetName() {
        return name;
    }

    public int getVersion() {
        return version;
    }

    public SheetDTO toSheetDTO() {
        return new SheetDTO(
                version, //"A4"
                name,
                numberOfRows,
                numberOfCols,
                columnWidth,
                rowHeight,
                getCellsDTOMatrix(),
                new ArrayList<>(ranges.keySet())
        );
    }
    public CellDTO[][] getCellsDTOMatrix() {
        return Arrays.stream(cellsMatrix)
                .map(row -> Arrays.stream(row)
                        .map(cell -> cell != null ? cell.toCellDTO() : null)
                        .toArray(CellDTO[]::new))
                .toArray(CellDTO[][]::new);
    }

    public void setCellsMatrix(Cell[][] cellsMatrix) {
        this.cellsMatrix = cellsMatrix;
    }

    public Cell getCellByCoord(Coordinate coord)throws IllegalArgumentException{

        validateCoordinateBounds(coord);
        Cell cell = cellsMatrix[coord.getRow()-1][coord.getCol()-1];

        return cell;
    }
    public int howManyActiveCellsInSheet(){
        return Arrays.stream(cellsMatrix)
                .flatMap(Arrays::stream)
                .filter(cell -> cell != null)
                .mapToInt(cell -> 1)
                .sum();
    }


    public void validateSheetExpressions() throws Exception {
        parseExpressions();
        checkForCircularDependencies();
        evaluateSheetValuesForRefCheck();
    }



//    public void setVersion(int version) {
//        this.version = version;
//        Arrays.stream(cellsMatrix)                          // Stream over rows of cellsMatrix
//                .flatMap(Arrays::stream)                    // Flatten the stream of rows into a single stream of cells
//                .filter(cell -> cell != null).forEach(cell -> cell.setLastUpdatedVersion(version));
//
//    }

    public void upgradeVersion(){
        version++;
    }

    public int upgradeCellsVersionsAndGetNumOfChanges(){
        List<Cell>changedCells = Arrays.stream(cellsMatrix)                          // Stream over rows of cellsMatrix
                .flatMap(Arrays::stream)                    // Flatten the stream of rows into a single stream of cells
                .filter(cell -> cell != null)               // Filter out null cells
                .filter(Cell::calculateAndCheckIfUpdated)   // Filter cells that have been updated
                .toList();
        //  if(changedCells.size() != 0){//תכלס שטום דבר לא השתנה

        changedCells.stream().forEach(cell -> cell.setLastUpdatedVersion(version));
        if(changedCells.size() == 0){//אף תא לא השתנה לכן רק התא לו שינינו את ערך המקור ולכן קיים שינוי 1 במידה וערך המקור לא השתנה אז זה שגיאה ???
            return 0; //TODO -  לשאול את אמיר
        }
        version++;
        changedCells.stream().forEach(cell -> cell.setLastUpdatedVersion(version));
        return changedCells.size();  // Filter cells that have been updated
    }

    public void deleteCell(Coordinate coord){


        cellsMatrix[coord.getRow()-1][coord.getCol()-1].resetCell();
    }

    public void addCell(Coordinate coord, Cell cell){
        cellsMatrix[coord.getRow()-1][coord.getCol()-1] = cell;
    }

    public void createNewCellValueForCommand1(Cell cell) throws Exception {

        List<Cell> dependsOnCellList = new LinkedList<Cell>();
        List<Range> rangesDependsOnList= new LinkedList<Range>();
        Expression expression = FunctionParser.getExpressionOfCell(cell.getOriginalValue(), dependsOnCellList,rangesDependsOnList,this);
        cell.setExpression(expression);
        updateDependencies(cell, dependsOnCellList,rangesDependsOnList);

    }


    public Cell createNewCellForCommand4(String originalValue, Coordinate coord) throws Exception {
        if(originalValue.isEmpty()){
            throw new Exception("Cell at coordinate "+coord+" is already empty!");
        }

        Cell cell;
        List<Cell> dependsOnCellList = new LinkedList<Cell>();
        List<Range> rangesDependsOnList= new LinkedList<Range>();
        Expression expression = FunctionParser.getExpressionOfCell(originalValue, dependsOnCellList,rangesDependsOnList,this);
        Cell newCell = new Cell(coord,originalValue);
        addCell(coord, newCell);
        cell = getCellByCoord(coord);
        cell.setExpression(expression);
        //changeCell.setCellOriginalValue(originalValue);
        try {
            cell.getAndUpdateEffectiveValue();
            updateDependencies(cell, dependsOnCellList,rangesDependsOnList);
            return cell;
        }
        catch(Exception e){
            deleteCell(coord);
            throw new Exception("Cell at coordinate "+coord+" cannot be created :" +e.getMessage());

        }
    }

    public boolean updateCellValue(String originalValue,Cell changeCell) throws Exception {
        boolean isDeleted;
        if (originalValue.isEmpty()) {
            //changeCell.checkIfCellCanBeDeleted();
            deleteCell(changeCell.getCoordinate());
            isDeleted = true;
        }
        else {
            isDeleted = false;
            List<Cell> dependsOnCellList = new LinkedList<Cell>();
            List<Range> rangesDependsOnList= new LinkedList<Range>();
            Expression expression = FunctionParser.getExpressionOfCell(originalValue, dependsOnCellList,rangesDependsOnList,this);;
            //changeCell = getCellByCoord(coord);
            Expression oldExpression = changeCell.getCellValue();
            changeCell.checkForCircularDependencyWrapper(changeCell.getCoordinate(), dependsOnCellList);
            changeCell.setExpression(expression);

            try {
                changeCell.checkIfCellExpressionCanBeUpdatedWrapper();
            } catch (Exception e) {
                changeCell.setExpression(oldExpression);
                throw new Exception(e.getMessage());
            }
            changeCell.setCellOriginalValue(originalValue);
            updateDependencies(changeCell, dependsOnCellList,rangesDependsOnList);
            changeCell.setIsExist(true);
        }
        return isDeleted;
    }




    public Cell[][] getCellsMatrix() {
        return cellsMatrix;
    }

    public void validateCoordinateBounds(Coordinate coord) throws IllegalArgumentException{
        int row = coord.getRow();
        int col = coord.getCol();

        if(!(row > 0 && row <= numberOfRows && col > 0 && col <= numberOfCols)){
            String errorMessage = String.format("The %s coordinate is out of bounds!\n" +
                            "Valid column range: %c to %c, " +
                            "Valid row range: %d to %d.",
                    coord.toString(),minCol, maxCol, minRow, maxRow);

            throw new IllegalArgumentException(errorMessage);
        };

    }

    public void parseExpressions() throws Exception {
        for(Cell[] cells : cellsMatrix){
            for(Cell cell : cells){
                if(cell!=null&&cell.getIsExist()){
                    createNewCellValueForCommand1(cell);
                }
            }
        }
    }

    public void evaluateSheetValuesForRefCheck(){
        for(Cell[] cells : cellsMatrix){
            for(Cell cell : cells){
                if(cell!=null){
                    cell.getAndUpdateEffectiveValue();
                }
            }
        }
    }

    public void checkForCircularDependencies() {
        for (Cell[] cells : cellsMatrix) {
            for (Cell cell : cells) {
                if (cell != null) {
                    cell.checkForCircularDependencyWrapper(cell.getCoordinate(), cell.getDependsOn());

                }
            }
        }
    }

    public void updateDependencies(Cell changeCell, List<Cell> dependsOnCellList,List<Range> rangesDependsOnList )
    {
        List<Cell> oldDependsList = changeCell.getDependsOn();
        List<Range> oldRangesDependsList = changeCell.getRangesDependsOnList();
        changeCell.setDependsOn(dependsOnCellList);
        changeCell.setRangesDependsOnList(rangesDependsOnList);
        Range tmpRange;
        for(Range range : oldRangesDependsList){
           // tmpRange=getRange(range);
            range.removeCoordinateFromInfluencingOnCoordinates(changeCell.getCoordinate());

        }
        for(Range range : rangesDependsOnList){
            //tmpRange=getRange(range);
            range.addCoordinateFromInfluencingOnCoordinates(changeCell.getCoordinate());
        }


        for (Cell cell : oldDependsList) {
            cell.removeCellFromInfluencingOnList(changeCell);
        }
        for (Cell cell : dependsOnCellList) {
            cell.AddCellToInfluencingOnList(changeCell);
        }
    }
    public void updateVersionForSheetAndCreatedCell(Coordinate coord){
        getCellByCoord(coord).setLastUpdatedVersion(version);
    }
    public Sheet cloneSheet() {
        try {
            // Serialize the object to a byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this);
            oos.flush();
            oos.close();

            // Deserialize the byte array to a new object
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            return (Sheet) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void upgradeCellVersion(Cell cell) {
        version++;
        cell.setLastUpdatedVersion(version);
    }

    public void CreateNewEmptyCell(Coordinate coord){
        Cell emptyCell=Cell.createEmptyCell(coord);
        cellsMatrix[coord.getRow()-1][coord.getCol()-1] = emptyCell;
    }
    public List<Cell> getRangeCellsList(Coordinate topLeftCoord, Coordinate bottomRightCoord) {
        List<Cell> rangeList = new LinkedList<>();
        int bottomRow = bottomRightCoord.getRow();
        int leftCol = topLeftCoord.getCol();
        int topRow = topLeftCoord.getRow();
        int rightCol = bottomRightCoord.getCol();
        Coordinate tmpCoord;
        for(int i=topRow;i<=bottomRow;i++){
            for(int j=leftCol;j<=rightCol;j++){
                Cell cell = cellsMatrix[i-1][j-1];
                if(cell==null){
                    tmpCoord= new CoordinateImpl(i, j);
                    cell = Cell.createEmptyCell(tmpCoord);
                    cellsMatrix[i-1][j-1]=cell;
                }
                rangeList.add(cell);
            }
        }
        return rangeList;
    }

    public void addRange(String rangeName, Coordinate topLeftCoord, Coordinate bottomRightCoord) throws IllegalArgumentException{

        if(rangeName.equals("")){
            throw new IllegalArgumentException("Range name cannot be empty.");
        }
        if(ranges.containsKey(rangeName)){
            throw new IllegalArgumentException("Range with the name " + rangeName + " already exists.");
        }

        validateCoordinateBounds(topLeftCoord);
        validateCoordinateBounds(bottomRightCoord);
        Range.isValidRange(topLeftCoord, bottomRightCoord);

        List<Cell> rangeList = getRangeCellsList(topLeftCoord, bottomRightCoord);
        Range range = new Range(topLeftCoord, bottomRightCoord,rangeList);
        for (Cell cell : rangeList) {
            cell.AddRangeToInfluencingOnRange(range);
        }
        ranges.put(rangeName, range);
    }

    //TODO - add check if range is usued for function, maybe with boolean field
    public void deleteRange(String rangeName) throws IllegalArgumentException{
        if(!ranges.containsKey(rangeName)){
            throw new IllegalArgumentException("Range with the name " + rangeName + " does not exist.");
        }
        Range range = ranges.get(rangeName);
        if(range.isBeingUsed())
        {
            throw new IllegalArgumentException("Range with the name " + rangeName + " is being used in a function, and therefore can't be deleted.");
        }
        List<Coordinate> cellCoord=range.getRangeCoordinates();
        for(Coordinate coord : cellCoord){
            Cell InfluencedCell=getCellByCoord(coord);
            InfluencedCell.removeRangeFromInfluencingOnRange(range);

        }
        ranges.remove(rangeName);
    }

    public Range getRange(String rangeName) throws IllegalArgumentException{
        if(!ranges.containsKey(rangeName)){
            throw new IllegalArgumentException("Range with the name " + rangeName + " does not exist.");
        }
        return ranges.get(rangeName);
    }

    public List<Coordinate> getRangeCoordinates(String rangeName) {
        if(!ranges.containsKey(rangeName)){
            throw new IllegalArgumentException("Range with the name " + rangeName + " does not exist.");
        }
        return ranges.get(rangeName).getRangeCoordinates();
    }

    public List<String> getRangesNames(){
        return new ArrayList(ranges.keySet());
    }

    public Sheet getSortedSheet(String rangeDefinition, List<Character> columnsToSortBy) throws IllegalArgumentException {
        Sheet sortedSheet = this.cloneSheet();

        Coordinate[] rangeEdgeCoordinates = Range.parseRange(rangeDefinition);
        Coordinate topLeftCoord = rangeEdgeCoordinates[0];
        Coordinate bottomRightCoord = rangeEdgeCoordinates[1];
        char leftRangeColChar = topLeftCoord.getColChar();
        char rightRangeColChar = bottomRightCoord.getColChar();

        // בדיקת הקואורדינטות
        sortedSheet.validateCoordinateBounds(topLeftCoord);
        sortedSheet.validateCoordinateBounds(bottomRightCoord);
        Range.isValidRange(topLeftCoord, bottomRightCoord);

        validateSelectedColumnsInRange(columnsToSortBy, leftRangeColChar, rightRangeColChar);

        // המרת המטריצה לרשימה של תאים בטווח שנבחר
        List<SheetRow> rowsToSort = convertMatrixToRowList(sortedSheet.getCellsMatrix(), topLeftCoord, bottomRightCoord);

        try {
            // מיון השורות לפי העמודות שנבחרו
            rowsToSort.sort(createMultiLevelComparator(columnsToSortBy, leftRangeColChar));
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("One or more cells in the selected columns to sort are empty, and can't be sortd.");
        }
//        // מיון השורות לפי העמודות שנבחרו
//        rowsToSort.sort(createMultiLevelComparator(columnsToSortBy, leftRangeColChar));

        // עדכון המטריצה של התאים בעותק הממוין
        sortedSheet.updateCellsMatrix(rowsToSort, topLeftCoord, bottomRightCoord);

        return sortedSheet;
    }


    // פונקציה להמרת המטריצה לרשימה של שורות בטווח שנבחר
    private List<SheetRow> convertMatrixToRowList(Cell[][] cellsMatrix, Coordinate topLeft, Coordinate bottomRight) {
        List<SheetRow> rows = new ArrayList<>();
        for (int i = topLeft.getRow() - 1; i <= bottomRight.getRow() - 1; i++) {
            List<Cell> cells = new ArrayList<>();
            for (int j = topLeft.getCol() - 1; j <= bottomRight.getCol() - 1; j++) {
                cells.add(cellsMatrix[i][j]);
            }
            rows.add(new SheetRow(cells));
        }
        return rows;
    }

    // פונקציה ליצירת Comparator רב-שלבי
    private Comparator<SheetRow> createMultiLevelComparator(List<Character> columnsToSortBy,
                                                            char leftRangeColChar) throws IllegalArgumentException {
        return (row1, row2) -> {
            for (char colChar : columnsToSortBy) {
                //chekIfColumnInRange(colChar, leftRangeColChar);
                int colIndex = colChar - leftRangeColChar; //convert letter to colChar index according to the number of columns in the selected range
                Double value1 = row1.getCell(colIndex).getEffectiveValue().extractValueWithExpectation(Double.class);
                Double value2 = row2.getCell(colIndex).getEffectiveValue().extractValueWithExpectation(Double.class);

                if (value1 != null && value2 != null) {
                    int compareResult = Double.compare(value1, value2);
                    if (compareResult != 0) {
                        return compareResult; // נמצא הבדל, החזר את תוצאת ההשוואה
                    }
                }
            }
            return 0; // אם כל העמודות שוות, שמור על הסדר המקורי
        };
    }

//    private void checkForEmptyCells(SheetRow row1, SheetRow row2, int colIndex) throws IllegalArgumentException {
//        EffectiveValue effectiveValue1 = row1.getCell(colIndex).getEffectiveValue();
//        EffectiveValue effectiveValue2 = row2.getCell(colIndex).getEffectiveValue();
//        if(effectiveValue1 == null || effectiveValue2 == null){
//            throw new IllegalArgumentException("Can't sort column with empty cells");
//        }
//    }


    // עדכון מטריצת התאים בגיליון לאחר המיון
    private void updateCellsMatrix(List<SheetRow> sortedRows, Coordinate topLeft, Coordinate bottomRight) {
        int startRow = topLeft.getRow() - 1;
        int colStart = topLeft.getCol() - 1;
        Cell[][] cellsMatrix = this.getCellsMatrix();

        // מילוי המטריצה עם התאים הממויינים
        for (int i = 0; i < sortedRows.size(); i++) {
            List<Cell> rowCells = sortedRows.get(i).getCells();
            for (int j = 0; j < rowCells.size(); j++) {
                cellsMatrix[startRow + i][colStart + j] = rowCells.get(j);
                //System.out.println("cell "+rowCells.get(j).getCoordinate()+" is now in "+(startRow + i)+" "+(colStart + j));
            }
        }
    }

    private void validateSelectedColumnsInRange(List<Character> columnsToSortBy, char leftRangeColChar, char rightRangeColChar) throws IllegalArgumentException {
        for (char colChar : columnsToSortBy) {
            if (colChar < leftRangeColChar || colChar > rightRangeColChar) {
                throw new IllegalArgumentException("Column " + colChar + " is not in the selected range.");
            }
        }
    }
    public Set<EffectiveValue> getDistinctValuesOfColInRange(String col, Coordinate rangeTopLeftCoord, Coordinate rangeBottomRightCoord){
        Set<EffectiveValue> effectiveValues = new HashSet<EffectiveValue>();
        int topRow = rangeTopLeftCoord.getRow();
        int bottomRow = rangeBottomRightCoord.getRow();
        int colIndex =CoordinateImpl.columnStringToIndex(col);

        for (int row = topRow; row <= bottomRow; row++) {
            Cell cell = cellsMatrix[row - 1][colIndex - 1];
            if (cell != null && cell.getIsExist()) {
                EffectiveValue effectiveValue = cell.getEffectiveValue();
                effectiveValues.add(effectiveValue); // יתווסף רק אם ייחודי
            }
        }

        return effectiveValues;
    }

    public Sheet getFilteredSheetByColumnInRange( Set<EffectiveValue> valuesToMatch, String colStr,Coordinate topLeftCoord, Coordinate bottomRightCoord) {
        // שכפול הגיליון הנוכחי
        Sheet filteredSheet = this.cloneSheet();

        int colIndex =CoordinateImpl.columnStringToIndex(colStr);

        // וידוא שטווח התאים בעמודה תקין
        if (colIndex < 1 || colIndex > numberOfCols) {
            throw new IllegalArgumentException("Invalid column index.");
        }

        // רשימה זמנית שתכיל את השורות שתואמות לערכים הנבחרים
        List<SheetRow> filteredRows = new ArrayList<>();

        // מעבר על כל השורות בטווח הרלוונטי
        for (int row = topLeftCoord.getRow(); row <= bottomRightCoord.getRow(); row++) {
            List<Cell> filteredRowCells = new ArrayList<>();
            boolean rowMatches = false;  // דגל שיציין אם יש ערכים שתואמים בשורה

            // נתחום את העמודות וניקח רק את התאים הנמצאים בטווח של העמודות
            for (int col = topLeftCoord.getCol(); col <= bottomRightCoord.getCol(); col++) {
                Cell cell = filteredSheet.getCellsMatrix()[row - 1][col - 1];

                // בדיקה אם התא קיים ואם הערך מתאים לאחד הערכים מהרשימה
                if (cell != null && valuesToMatch.contains(cell.getEffectiveValue())) {
                    rowMatches = true;
                }
                filteredRowCells.add(cell);  // נוסיף את התא לרשימה של השורה המסוננת
            }

            // אם מצאנו ערכים תואמים בשורה, נוסיף את השורה לרשימת השורות המסוננות
            if (rowMatches) {
                filteredRows.add(new SheetRow(filteredRowCells));
            }
        }

        // עדכון המטריצה של התאים בגיליון המשוכפל עם השורות המסוננות
        filteredSheet.updateCellsMatrix(filteredRows, topLeftCoord, bottomRightCoord);
        resetRemainingCells(filteredSheet, topLeftCoord, bottomRightCoord, filteredRows);

        // החזרת הגיליון המסונן
        return filteredSheet;
    }

    private void resetRemainingCells(Sheet filteredSheet, Coordinate topLeft, Coordinate bottomRight, List<SheetRow> filteredRows) {
        Cell[][] cellsMatrix = filteredSheet.getCellsMatrix();
        int numberOfFilteredRows = filteredRows.size();
        int totalRows = bottomRight.getRow() - topLeft.getRow() + 1;

        // איפוס התאים בשורות שלא מולאו (מתחילים משורת הסיום של הסינון עד סוף הטווח)
        for (int rowIndex = numberOfFilteredRows; rowIndex < totalRows; rowIndex++) {
            for (int col = topLeft.getCol(); col <= bottomRight.getCol(); col++) {
               // cellsMatrix[topLeft.getRow() - 1 + rowIndex][col - 1] = Cell.createEmptyCell(new CoordinateImpl(topLeft.getRow() - 1 + rowIndex, col));
                cellsMatrix[topLeft.getRow() - 1 + rowIndex][col - 1] = Cell.createEmptyCell(CoordinateImpl.notExists);
            }
        }
    }

    public void validateRowHeight(int rowHeight) throws IllegalArgumentException {
        if (rowHeight <= 0) {
            throw new IllegalArgumentException("Row height must be a positive number.");
        }
    }
}



//    // פונקציה עזר לפרש ערכים מספריים
//    private Double parseNumericValue(String value) {
//        try {
//            return Double.parseDouble(value);
//        } catch (NumberFormatException e) {
//            return null; // מחזיר null אם לא מדובר במספר
//        }
//    }