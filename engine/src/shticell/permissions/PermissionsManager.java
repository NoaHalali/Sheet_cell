package shticell.permissions;

import parts.permission.UserRequestDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionsManager {

    private Map<String, PermissionType> usersPermissions = new HashMap<>();//תכלס
    private List<UserRequest> requestsHistory = new ArrayList<>();
    private int requestsNumber;

    public PermissionsManager(String owner) {
        usersPermissions.put(owner, PermissionType.OWNER);
        requestsHistory.add(new UserRequest( owner,PermissionType.OWNER,RequestStatus.APPROVED)); //TODO maybe add request number here
        requestsNumber = 1;
    }

    //Map<Integer, UserPermissionDTO> allHistory = new HashMap<>();//אושרו
    //Map<String, PermissionType> notApprovedYet=new HashMap<>();//לאגור את כל הבקשות

    public void addUserPermissionRequest(String userName, PermissionType permission) { //User call this method
        requestsHistory.add(new UserRequest(userName,permission,RequestStatus.PENDING));
        requestsNumber++;
    }

    public void approvePermissionRequest(int permissionNumber) //Owner call this method
    {
        UserRequest request = requestsHistory.get(permissionNumber-1);
        String username = request.getUsername();
        PermissionType permission = request.getPermission();
        request.setRequestStatus(RequestStatus.APPROVED);

        usersPermissions.put(username,permission);
    }

    public void denyPermissionRequest(int permissionNumber) //Owner call this method
    {
        UserRequest request = requestsHistory.get(permissionNumber-1);
        request.setRequestStatus(RequestStatus.DENIED);
    }

    public List<UserRequestDTO> getRequestsDTOList()
    {
        List<UserRequestDTO> requestsDTOList = new ArrayList<>();
        for (UserRequest request : requestsHistory) {
            requestsDTOList.add(request.toDTO());
        }
        return requestsDTOList;
    }

    public PermissionType getPermissionForUser(String username) {
        if (!usersPermissions.containsKey(username)) {
            return PermissionType.NONE;
        }

        return usersPermissions.get(username);
    }





    //TODO: if someone can be shown multiple times, List insdead of Map, and delete the if in line 30
    //TODO: if the method returns list it will be PermissionDTO, otherwise UserPermissionDTO (without username)








}
