package parts;

import shticell.permissions.PermissionType;

public class SheetDetailsDTO {
    private String ownerName;
    private String sheetName;
    private String sheetSize;
    private PermissionType permission;

    public SheetDetailsDTO(String ownerName, String sheetName, String sheetSize, PermissionType permission) {
        this.ownerName = ownerName;
        this.sheetName = sheetName;
        this.sheetSize = sheetSize;
        this.permission = permission;
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
