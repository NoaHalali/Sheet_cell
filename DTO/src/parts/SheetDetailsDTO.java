package parts;

import shticell.permissions.PermissionType;

public class SheetDetailsDTO {
    private final String ownerName;
    private final String sheetName;
    private final String sheetSize;
    private final PermissionType permission;

    public SheetDetailsDTO(String ownerName, String sheetName, String sheetSize, PermissionType permission) {
        this.ownerName = ownerName;
        this.sheetName = sheetName;
        this.sheetSize = sheetSize;
        this.permission = permission;
    }
    public String getOwnerName() {
        return ownerName;
    }

    public PermissionType getPermission() {
        return permission;
    }

    public String getSheetName() {
        return sheetName;
    }
    public String getSheetSize() {
        return sheetSize;
    }

}
