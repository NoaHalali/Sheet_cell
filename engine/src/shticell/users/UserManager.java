package shticell.users;

import java.util.*;

/*
Adding and retrieving users is synchronized and in that manner - these actions are thread safe
Note that asking if a user exists (isUserExists) does not participate in the synchronization and it is the responsibility
of the user of this class to handle the synchronization of isUserExists with other methods here on it's own
 */
public class UserManager {

    private final Set<String> usersSet;
    private final Map<String, PermissionUpdate> usersPermissionsUdates;

    public UserManager() {
        usersSet = new HashSet<>();
        usersPermissionsUdates = new HashMap<>();
    }

    public synchronized void addUser(String username) {
        usersSet.add(username.toUpperCase());
    }

    public synchronized void addPermissionUpdate(String username, PermissionUpdate update) {
        usersPermissionsUdates.put(username,update);
    }

//    public synchronized void removeUser(String username) {
//        usersSet.remove(username);
//    }

    public synchronized Set<String> getUsers() {
        return Collections.unmodifiableSet(usersSet);
    }

    public boolean isUserExists(String username) {
        System.out.println("Engine is checking if user exists: " + username);
        return usersSet.contains(username);
    }
}
