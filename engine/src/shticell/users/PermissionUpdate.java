package shticell.users;

import shticell.permissions.PermissionType;
import shticell.permissions.RequestStatus;

public class PermissionUpdate { //also DTO
    private final String sheetName;
    private final PermissionType permission;
    private final RequestStatus requestStatus;

    public PermissionUpdate(String sheetName, PermissionType permission, RequestStatus requestStatus) {
        this.sheetName = sheetName;
        this.permission = permission;
        this.requestStatus = requestStatus;
        }

    public PermissionType getPermission() {
        return permission;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public String getUsername() {
        return sheetName;
    }
}
