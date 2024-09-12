package parts;

import parts.cell.*;
import parts.cell.coordinate.Coordinate;
import parts.cell.coordinate.CoordinateImpl;
import parts.cell.expression.Expression;

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
    private Map<String,Range> ranges = new HashMap<String,Range>();
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

        if(ranges.containsKey(rangeName)){
            throw new IllegalArgumentException("Range with the name " + rangeName + " already exists.");
        }

        validateCoordinateBounds(topLeftCoord);
        validateCoordinateBounds(bottomRightCoord);

        List<Cell> rangeList = getRangeCellsList(topLeftCoord, bottomRightCoord);
        Range range = new Range(topLeftCoord, bottomRightCoord,rangeList);
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

}