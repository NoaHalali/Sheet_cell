package shticell.users;

import java.util.*;

/*
Adding and retrieving users is synchronized and in that manner - these actions are thread safe
Note that asking if a user exists (isUserExists) does not participate in the synchronization and it is the responsibility
of the user of this class to handle the synchronization of isUserExists with other methods here on it's own
 */
public class UserManager {
    ///private final Queue<PermissionUpdate> NO_UPDATE = ;


    private final Set<String> usersSet;
    private final Map<String, Queue<PermissionUpdate>> usersPermissionsUpdates;

    public UserManager() {
        usersSet = new HashSet<>();
        usersPermissionsUpdates = new HashMap<>();
    }

    public synchronized void addUser(String username) {
        usersSet.add(username.toUpperCase());
        usersPermissionsUpdates.put(username.toUpperCase(), new LinkedList<>());
    }

    public synchronized void addPermissionUpdate(String username, PermissionUpdate update) {
        Queue<PermissionUpdate> queue = usersPermissionsUpdates.get(username.toUpperCase());
        queue.add(update);
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

    public synchronized PermissionUpdate getUserPermissionsUpdates(String username) {
        Queue<PermissionUpdate> queue = usersPermissionsUpdates.get(username.toUpperCase());
        return queue.poll();
        }

}
