package parts;

public class SheetDetailsDTO {
    private String ownerName;
    private String sheetName;
    private String sheetSize;
    //private String permission;//todo enum

    public SheetDetailsDTO(String ownerName, String sheetName, String sheetSize) {
        this.ownerName = ownerName;
        this.sheetName = sheetName;
        this.sheetSize = sheetSize;
    }
    public String getOwnerName() {
        return ownerName;

    }


    public String getSheetName() {
        return sheetName;
    }
    public String getSheetSize() {
        return sheetSize;
    }


}
