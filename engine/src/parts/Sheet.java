package parts;

import parts.cell.*;
import parts.cell.coordinate.Coordinate;
import parts.cell.coordinate.CoordinateImpl;
import parts.cell.expression.Expression;
import parts.cell.expression.impl.BoolExpression;
import parts.cell.expression.impl.NumberExpression;
import parts.cell.expression.impl.StringExpression;
import parts.function.*;
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
                getCellsDTOMatrix()
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
                .filter(cell -> cell != null)
                .filter(cell-> cell.getIsExist())
                // Filter out null cells
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

//    public void deleteCell(Coordinate coord){
//        cellsMatrix[coord.getRow()-1][coord.getCol()-1] = null;
//        if(deletedCells.containsKey(coord.toString())){
//            deletedCells.remove(coord.toString());
//        }
//        deletedCells.put(coord.toString(),version);
//
//    }
//    public int getEmptyCellVersion(Coordinate coord){
//        if(deletedCells.containsKey(coord.toString())){
//            return deletedCells.get(coord.toString());
//        }
//        return 0;
//    }

    public void addCell(Coordinate coord, Cell cell){
        cellsMatrix[coord.getRow()-1][coord.getCol()-1] = cell;
    }

    public void createNewCellValueForCommand1(Cell cell) throws Exception {

        List<Cell> dependsOnCellList = new LinkedList<Cell>();
        Expression expression = getExpressionOfCell(cell.getOriginalValue(), dependsOnCellList);
        cell.setExpression(expression);
        updateDependencies(cell, dependsOnCellList);

    }

    public Cell createNewCellForCommand4(String originalValue, Coordinate coord) throws Exception {
        if(originalValue.isEmpty()){
            throw new Exception("Cell at coordinate "+coord+" is already empty!");
        }

        Cell cell;
        List<Cell> dependsOnCellList = new LinkedList<Cell>();
        Expression expression = getExpressionOfCell(originalValue, dependsOnCellList);
        Cell newCell = new Cell(coord,originalValue);
        addCell(coord, newCell);
        cell = getCellByCoord(coord);
        cell.setExpression(expression);
        //changeCell.setCellOriginalValue(originalValue);
        try {
            cell.getAndUpdateEffectiveValue();
            updateDependencies(cell, dependsOnCellList);
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
            Expression expression = getExpressionOfCell(originalValue, dependsOnCellList);
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
            updateDependencies(changeCell, dependsOnCellList);
            changeCell.setIsExist(true);
        }
        return isDeleted;
    }


    public static List<String> splitExpressionToStrings(String expression) {
        expression = expression;
        List<String> parsed = new ArrayList<>();

        if (expression.startsWith("{") && expression.endsWith("}")) {
            expression = expression.substring(1, expression.length() - 1);
        }

        int bracketDepth = 0;
        StringBuilder token = new StringBuilder();

        for (char c : expression.toCharArray()) {
            if (c == '{') {
                if (bracketDepth == 0 && token.length() > 0) {
                    parsed.add(token.toString());
                    token.setLength(0);
                }
                bracketDepth++;
            } else if (c == '}') {
                bracketDepth--;
                if (bracketDepth == 0) {
                    token.append(c);
                    parsed.add(token.toString());
                    token.setLength(0);
                    continue;
                }
            } else if (c == ',' && bracketDepth == 0) {
                if (token.length() > 0) {
                    parsed.add(token.toString());
                    token.setLength(0);
                }
                continue;
            }
            token.append(c);
        }

        if (token.length() > 0) {
            parsed.add(token.toString());
        }
//        List<String> updatedList = parsed.stream()
//                .filter(str -> str != null && !str.trim().isEmpty())
//                .collect(Collectors.toList());
        return parsed;
    }

    public Expression getSmallArgs(String OriginalValue){
        if (OriginalValue.trim().equalsIgnoreCase("FALSE") || OriginalValue.trim().equalsIgnoreCase("TRUE")){
            return new BoolExpression(OriginalValue.trim().equalsIgnoreCase("TRUE"));
        }
        try{
            double num = Double.parseDouble(OriginalValue);
            return new NumberExpression(num);
        }catch (Exception e){
            return new StringExpression(OriginalValue);
        }
    }


    //מחזירה אקספרשיון מערך מקור
    public Expression getExpressionOfCell(String OriginalValue, List<Cell> dependsOnCellList) throws Exception {
        List<String> list = splitExpressionToStrings(OriginalValue);
        Expression arg2 = null;
        Expression res = null;
        if(list.size() == 1){
            res = getSmallArgs(list.get(0));
        }
        else {
            Expression arg1 = getExpressionOfCell(list.get(1), dependsOnCellList);
            if(list.size() > 2 ){
                arg2 = getExpressionOfCell(list.get(2), dependsOnCellList);
            }
            switch (list.get(0).toUpperCase()) {
                case "PLUS":
                    if(list.size() != 3){
                        throw new IllegalArgumentException("PLUS function expected to get 2 arguments") ;
                    }
                    res = new Plus(arg1, arg2);
                    break;
                case "MINUS":
                    if(list.size() != 3){
                        throw new IllegalArgumentException("MINUS function expected to get 2 arguments") ;
                    }
                    res = new Minus(arg1, arg2);
                    break;
                case "POW":
                    if(list.size() != 3){
                        throw new IllegalArgumentException("POW function expected to get 2 arguments") ;
                    }
                    res = new Pow(arg1, arg2);
                    break;
                case "ABS":
                    if(list.size() != 2){
                        throw new IllegalArgumentException("ABS function expected to get 1 arguments") ;
                    }
                    res = new Abs(arg1);
                    break;
                case "DIVIDE":
                    if(list.size() != 3){
                        throw new IllegalArgumentException("DIVIDE function expected to get 2 arguments") ;
                    }
                    res = new Divide(arg1, arg2);
                    break;
                case "TIMES":
                    if(list.size() != 3){
                        throw new IllegalArgumentException("TIMES function expected to get 2 arguments") ;
                    }
                    res = new Times(arg1, arg2);
                    break;
                case "MOD":
                    if(list.size() != 3){
                        throw new IllegalArgumentException("MOD function expected to get 2 arguments") ;
                    }
                    res = new Mod(arg1, arg2);
                    break;
                case "CONCAT":
                    if(list.size() != 3){
                        throw new IllegalArgumentException("CONCAT function expected to get 2 arguments") ;
                    }
                    res = new Concat(arg1, arg2);
                    break;
                case "SUB":
                    if(list.size() != 4){
                        throw new IllegalArgumentException("SUB function expected to get 3 arguments") ;
                    }
                    Expression arg3 = getExpressionOfCell(list.get(3), dependsOnCellList);
                    res = new Sub(arg1,arg2,arg3);
                    break;
                case "REF"://sheet סטטי ?
                    if(list.size() != 2){
                        throw new IllegalArgumentException("REF function expected to get 1 arguments") ;
                    }
                    Coordinate refCoord = CoordinateImpl.parseCoordinate(list.get(1));
                    validateCoordinateBounds(refCoord);
                    Cell refcell = getCellByCoord(refCoord);//find Cell in map or 2dim array and cell coord: list.get(1)
                    if(refcell == null){
                        CreateNewEmptyCell(refCoord);
                        refcell = getCellByCoord(refCoord);
                    }
                    dependsOnCellList.add(refcell); // לתא עליו התבקשנו לעדכן אערך נקצה רשימה חדשה בההתאים המשפיעים על תא זה שהיא תהיה רשימת המושפעים מהתא עליו נבצע עדכון +refcell
                    res = new Ref(refcell);

                    break;
                default:
                    throw new IllegalArgumentException("Illegal function name. " + list.get(0) + " is not an option.");
            }
        }
        return res;
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
                if(cell!=null){
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

    public void updateDependencies(Cell changeCell, List<Cell> dependsOnCellList )
    {
        List<Cell> oldList = changeCell.getDependsOn();
        changeCell.setDependsOn(dependsOnCellList);

        for (Cell cell : oldList) {
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

    public void addRange(String rangeName, Range range) throws IllegalArgumentException{
        if(ranges.containsKey(rangeName)){
            throw new IllegalArgumentException("Range with the name " + rangeName + " already exists.");
        }
        ranges.put(rangeName, range);
    }

    //TODO - add check if range is usued for function, maybe with boolean field
    public void deleteRange(String rangeName) throws IllegalArgumentException{
        if(!ranges.containsKey(rangeName)){
            throw new IllegalArgumentException("Range with the name " + rangeName + " does not exist.");
        }
        ranges.remove(rangeName);
    }

    public Range getRange(String rangeName) throws IllegalArgumentException{
        if(!ranges.containsKey(rangeName)){
            throw new IllegalArgumentException("Range with the name " + rangeName + " does not exist.");
        }
        return ranges.get(rangeName);
    }

}