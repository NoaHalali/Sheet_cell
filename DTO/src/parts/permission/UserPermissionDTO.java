package parts.permission;

import shticell.permissions.PermissionType;

public class UserPermissionDTO {
    private final PermissionType permission;
    private final Boolean approved;

    public UserPermissionDTO(PermissionType permission, Boolean approved) {
        this.permission = permission;
        this.approved = approved;
    }

    public PermissionType getPermission() {
        return permission;
    }

    public Boolean getApproved() {
        return approved;
    }
}
