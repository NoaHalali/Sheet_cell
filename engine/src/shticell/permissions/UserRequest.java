package shticell.permissions;

import parts.permission.UserRequestDTO;

public class UserRequest {
    private final String username;
    private final PermissionType permission;
    private RequestStatus requestStatus;
    private final int requestNumber;

    public UserRequest(String username, PermissionType permission, RequestStatus requestStatus, int requestNumber) {
        this.username = username;
        this.permission = permission;
        this.requestStatus = requestStatus;
        this.requestNumber = requestNumber;
    }

    public String getUsername() {
        return username;
    }

    public PermissionType getPermission() {
        return permission;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public UserRequestDTO toDTO() {
        return new UserRequestDTO(username, permission, requestStatus,requestNumber );
    }
}
