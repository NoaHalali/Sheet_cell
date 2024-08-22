package XMLFile;

import XMLFile.GeneratedFiles.STLCell;
import XMLFile.GeneratedFiles.STLLayout;
import XMLFile.GeneratedFiles.STLSheet;
import XMLFile.GeneratedFiles.ObjectFactory;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import parts.Sheet;
import parts.cell.Cell;
import parts.cell.Coordinate;
import parts.cell.CoordinateImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private static final String JAXB_XML_GAME_PACKAGE_NAME = "XMLFile.GeneratedFiles";
    private final int maxRows = 50;
    private final int minRows = 1;
    private final int maxCols = 20;
    private final int minCols = 1;

    //validate path
    //load from xml to stlsheet
    //stl sheet check
    //stlsheet to sheet

    public Sheet processFile(String filePath) throws FileNotFoundException, IllegalArgumentException {

        validatePath(filePath);
        STLSheet XMLSheet = loadXML(filePath);
        validateSheetSize(XMLSheet);
        vaidateCells(XMLSheet);
        Sheet sheet = convertSLTSheetToSheet(XMLSheet);
        parseExpressions(sheet);

        return sheet;
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

    public STLSheet loadXML(String filePath) {
        try {
            InputStream inputStream = new FileInputStream(new File(filePath));
            return deserializeFrom(inputStream);
        } catch (JAXBException | FileNotFoundException e) {
            throw new RuntimeException("Failed to load XML file", e);
        }
    }

    private STLSheet deserializeFrom(InputStream in) throws JAXBException {

        JAXBContext context = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (STLSheet) unmarshaller.unmarshal(in);
    }

    public void validateSheetSize(STLSheet sheet) throws IllegalArgumentException {
        int rows = sheet.getSTLLayout().getRows();
        int cols = sheet.getSTLLayout().getColumns();

        if (rows < minRows || rows > maxRows) {
            throw new IllegalArgumentException(String.format("Rows number must be between %d and %d.", minRows, maxRows));
        }
        if (cols < minCols || cols > maxCols) {
            throw new IllegalArgumentException(String.format("Cols number must be between %d and %d.", minRows, maxRows));
        }
    }

    private void vaidateCells(STLSheet xmlSheet) {
        checkCellInBoardRange(xmlSheet);
        //TODO - 4
    }

    public void checkCellInBoardRange(STLSheet sheet) throws IllegalArgumentException{

        for(STLCell cell : sheet.getSTLCells().getSTLCell()){
            String colStr = cell.getColumn();
            if(colStr.length() != 1){
                throw new IllegalArgumentException("Column must be between 'A' to 'T'.");
            }

            int cellCol = colStr.charAt(0)-'A'+1;
            int givenRowsRange = sheet.getSTLLayout().getRows();
            int givenColsRange = sheet.getSTLLayout().getColumns();

            if(cell.getRow() < minRows||cell.getRow()> givenRowsRange ){
                throw new IllegalArgumentException(String.format("Cell row must be between %d and %d.", minRows, givenRowsRange));
            }
            else if(cellCol < minCols || cellCol > givenColsRange){
                throw new IllegalArgumentException(String.format("Cell column must be between %d and %d.", minCols, givenColsRange));
            }
        }
    }

    private Sheet convertSLTSheetToSheet(STLSheet xmlSheet) {
        STLLayout layout = xmlSheet.getSTLLayout();
        int rows = layout.getRows();
        int columns = layout.getColumns();
        int columnWidth = layout.getSTLSize().getColumnWidthUnits();
        int rowHeight = layout.getSTLSize().getRowsHeightUnits();
        String name = xmlSheet.getName();

        Sheet sheet = new Sheet(name, rows, columns, columnWidth, rowHeight);

        // קביעת מערך התאים
        Cell[][] cellsMatrix = new Cell[rows][columns];

        for (STLCell xmlCell : xmlSheet.getSTLCells().getSTLCell()) {
            int row = xmlCell.getRow() ; // Assuming 1-based indexing in XML
            int col = xmlCell.getColumn().charAt(0) - 'A'+1; // Assuming columns are A, B, C...

            // אתחול רשימות לרשימות שמשפיעות ותלויות
//            List<Cell> neighbors = new ArrayList<>();
//            List<Cell> influencingOn = new ArrayList<>();
////            List<Cell> dependsOn = new ArrayList<>();
            Coordinate coord = new CoordinateImpl(row, col);

            // אתחול התא בעזרת הקונסטרקטור החדש
            Cell cell = new Cell(
                    coord,
                    xmlCell.getSTLOriginalValue() // יצירת Expression מערך התא
            );
            //sheet.updateCellValueFromOriginalValue(xmlCell.getSTLOriginalValue(),coord);

            cellsMatrix[row-1][col-1] = cell;
        }

        sheet.setCellsMatrix(cellsMatrix);

        return sheet;
    }

    //1. set original value V
    //2. new Expression (Numeric, bool,....) V
    //3. check types in expressions V
    //4. evaluete - ref and etc..
    //5. update dependencies
    private void parseExpressions(Sheet sheet) {
        Cell[][] cellsMatrix = sheet.getCellsMatrix();
        for(Cell[] cells : cellsMatrix){
            for(Cell cell : cells){
                if(cell!=null){
                    sheet.updateCellValueFromOriginalValue(cell.getOriginalValue(),cell.getCoordinate());
                }
            }
        }
    }


}





//private void parseExpressions(Cell[][] cellsMatrix) {
//    Cell[][] cellsMatrix = sheet.getCellsMatrix();
//    for()


//        public class ValueOutOfRangeException extends RuntimeException {
//            public ValueOutOfRangeException(String message) {
//                super(message);
//            }
//        }
//למשל
//        public void setPercentage(int percentage) {
//            if (percentage < 0 || percentage > 100) {
//                throw new ValueOutOfRangeException("Percentage must be between 0 and 100.");
//            }
//            // Set the percentage
//        }









