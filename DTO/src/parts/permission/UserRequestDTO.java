package parts.permission;

import shticell.permissions.PermissionType;
import shticell.permissions.RequestStatus;

public class UserRequestDTO {
    private final String username;
    private final PermissionType permission;
    private final RequestStatus requestStatus;
    private final int requestNubmer;

    public UserRequestDTO(String username , PermissionType permission, RequestStatus requestStatus, int requestNubmer) {
        this.username = username;
        this.permission = permission;
        this.requestStatus = requestStatus;
        this.requestNubmer = requestNubmer;
    }

    public PermissionType getPermission() {
        return permission;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public String getUsername() {
        return username;
    }

    public int getRequestNubmer() {
        return requestNubmer;
    }
}
