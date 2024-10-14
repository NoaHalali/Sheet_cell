package shticell.permissions;

import parts.permission.UserPermissionDTO;

import java.util.HashMap;
import java.util.Map;

public class PermissionsManager {

//    Map<String, String> permissions; //TODO: maybe change value accordings to Avias'd response
//
//
    Map<String, PermissionType> approved=new HashMap<>();
    Map<String, PermissionType> notApprovedYet=new HashMap<>();

    public void addUserPermissionRequest(String usernameFromParameter, PermissionType permission) { //User call this method
        notApprovedYet.put(usernameFromParameter, permission);
    }

    public void givePermissionToUser(String username, PermissionType permission) //Owner call this method
    {
        if (notApprovedYet.containsKey(username)) //useful for None
        {
            notApprovedYet.remove(username);
        }
        approved.put(username,permission);
    }

    public void denyPermissionToUser(String username) //Owner call this method
    {
        notApprovedYet.remove(username);
    }

    Map<String,UserPermissionDTO> getPermissionsTable()
    {
        Map<String,UserPermissionDTO> permissionsTable = new HashMap<String,UserPermissionDTO>();
        for (String username : notApprovedYet.keySet())
        {
            permissionsTable.put(username,new UserPermissionDTO(notApprovedYet.get(username),false));
        }

        for (String username : approved.keySet())
        {
            if (!permissionsTable.containsKey(username)) {
                permissionsTable.put(username, new UserPermissionDTO(approved.get(username),true));
            }
        }

        return permissionsTable;
    }

    //TODO: if someone can be shown multiple times, List insdead of Map, and delete the if in line 30
    //TODO: if the method returns list it will be PermissionDTO, otherwise UserPermissionDTO (without username)








}
