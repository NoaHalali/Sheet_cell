package XMLFile;

import XMLFile.GeneratedFiles.STLCell;
import XMLFile.GeneratedFiles.STLLayout;
import XMLFile.GeneratedFiles.STLSheet;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import parts.Sheet;
import parts.cell.Cell;
import parts.cell.coordinate.Coordinate;
import parts.cell.coordinate.CoordinateImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileManager {

    private static final String JAXB_XML_GAME_PACKAGE_NAME = "XMLFile.GeneratedFiles";
    private static final int maxRows = 50;
    private static final int minRows = 1;
    private static final int maxCols = 20;
    private static final int minCols = 1;
    private static final char minCol = 'A';
    private static final int minRow = 1;

    public Sheet processFile(String filePath) throws Exception {

        validatePath(filePath);
        STLSheet XMLSheet = loadXML(filePath);
        validateSheetSize(XMLSheet);
        vaidateCells(XMLSheet);
        Sheet sheet = convertSLTSheetToSheet(XMLSheet);

        sheet.validateSheetExpressions();

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
            throw new IllegalArgumentException(String.format("Rows number must be between %d and %d. Given: %d", minRows, maxRows, rows));
        }
        if (cols < minCols || cols > maxCols) {
            throw new IllegalArgumentException(String.format("Columns number must be between %d and %d. Given: %d", minCols, maxCols, cols));
        }
    }

    private void vaidateCells(STLSheet xmlSheet) {
        checkCellInBoardRange(xmlSheet);
        //TODO - 4
    }

    public void checkCellInBoardRange(STLSheet sheet) throws IllegalArgumentException {

        int numberOfRows = sheet.getSTLLayout().getRows();
        int numberOfCols = sheet.getSTLLayout().getColumns();

        char maxCol = (char) (minCol + numberOfCols - 1);
        int maxRow = numberOfRows;

        for (STLCell cell : sheet.getSTLCells().getSTLCell()) {
            String colStr = cell.getColumn();

            if (colStr.length() != 1 || colStr.charAt(0) < minCol || colStr.charAt(0) > maxCol) {
                throw new IllegalArgumentException(
                        String.format("Column must be between '%c' and '%c'. Given: %s", minCol, maxCol, colStr)
                );
            }

            if (cell.getRow() < minRow || cell.getRow() > maxRow) {
                throw new IllegalArgumentException(
                        String.format("Cell row must be between %d and %d. Given: %d", minRow, maxRow, cell.getRow())
                );
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
            int row = xmlCell.getRow(); // Assuming 1-based indexing in XML
            int col = xmlCell.getColumn().charAt(0) - 'A' + 1; // Assuming columns are A, B, C...

            Coordinate coord = new CoordinateImpl(row, col);

            // אתחול התא בעזרת הקונסטרקטור החדש
            Cell cell = new Cell(
                    coord,
                    xmlCell.getSTLOriginalValue() // יצירת Expression מערך התא
            );

            cellsMatrix[row - 1][col - 1] = cell;
        }
        sheet.setCellsMatrix(cellsMatrix);

        return sheet;
    }
}
    //1. set original value V
    //2. new Expression (Numeric, bool,....) V
    //3. check types in expressions V
    //4. evaluete - ref and etc..
    //5. update dependencies

//    private void parseExpressions(Sheet sheet) throws Exception {
//        Cell[][] cellsMatrix = sheet.getCellsMatrix();
//        for(Cell[] cells : cellsMatrix){
//            for(Cell cell : cells){
//                if(cell!=null){
//                    sheet.updateCellValue(cell.getOriginalValue(),cell.getCoordinate());
//                }
//            }
//        }
//    }






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









