package shticell.sheets.manager;

import parts.SheetDetailsDTO;
import shticell.engines.sheetEngine.SheetEngine;
import shticell.engines.sheetEngine.SheetEngineImpl;
import shticell.permissions.PermissionType;
import shticell.sheets.sheet.Sheet;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MultiSheetEngineManager {
   // private Map<String, SheetEngine> sheetEngines;
    private final Map<String, Lock> sheetLocks = new HashMap<>(); // מנעול לכל גיליון
    private final Map<String, SheetEngine> sheetEngines = new HashMap<>(); // מנועים של גיליונות

    // החזרת המנוע של גיליון עם סנכרון על כל גיליון בנפרד
    public SheetEngine getSheetEngine(String sheetId) {
        Lock lock = sheetLocks.computeIfAbsent(sheetId, k -> new ReentrantLock());

        // נועל את הגישה למנוע של הגיליון
        lock.lock();
        try {
            return sheetEngines.get(sheetId);

        } finally {
            // משחרר את הנעילה לאחר סיום השימוש במנוע
            lock.unlock();
        }
    }


//
//    public MultiSheetEngineManager() {
//        this.sheetEngines = new HashMap<>();
//    }

//
//    public synchronized void  addSheet(String sheetId, File initialSheet) {
//        SheetEngine engine = new SheetEngine();
//        try{
//            engine.readFileData(initialSheet);
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        sheetEngines.put(sheetId, new SheetEngine(initialSheet));
//    }
    public synchronized void addSheetEngine(Sheet sheet,String owner) {
//        if (this.sheetEngines.containsKey(sheet.getSheetName())) {
//            throw new IllegalArgumentException("Sheet already exists");
//        }
        this.sheetEngines.put(sheet.getSheetName(), new SheetEngineImpl(sheet,owner));
    }
    public boolean isSheetNameExists(String sheetName) {
        return this.sheetEngines.containsKey(sheetName);
    }

//    public synchronized SheetEngine getSheetEngine(String sheetId) {
//        return sheetEngines.get(sheetId);
//    }

    public synchronized void removeSheet(String sheetId) {
        sheetEngines.remove(sheetId);
    }

    public synchronized List<SheetDetailsDTO> getSheetsDetalisListForUser(String username) {
        List<SheetDetailsDTO> list = new LinkedList<>();
        for (SheetEngine sheetEngine : sheetEngines.values()) {
            list.add(sheetEngine.getSheetDetailsDTOForUser(username));
        }
        return list;
    }

//    public void giveDefaultPermissionsToUser(String usernameFromParameter) { //maybe
//        for (SheetEngine sheetEngine : sheetEngines.values()) {
//            sheetEngine.givePermissionToUser(usernameFromParameter, PermissionType.NONE);
//        }
//    }
}
