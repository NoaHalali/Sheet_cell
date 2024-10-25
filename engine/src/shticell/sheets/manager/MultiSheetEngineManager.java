package shticell.sheets.manager;

import parts.SheetDetailsDTO;
import shticell.engines.sheetEngine.SheetEngine;
import shticell.engines.sheetEngine.SheetEngineImpl;
import shticell.sheets.sheet.Sheet;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MultiSheetEngineManager {
   // private Map<String, SheetEngine> sheetEngines;
    private final Map<String, Lock> sheetLocks = new HashMap<>(); // מנעול לכל גיליון
    private final Map<String, SheetEngine> sheetEngines = new HashMap<>(); // מנועים של גיליונות

    // החזרת המנוע של גיליון עם סנכרון על כל גיליון בנפרד
    public SheetEngine getSheetEngine(String sheetName) throws IllegalArgumentException {
        if (!isSheetNameExists(sheetName)) {
            throw new IllegalArgumentException("Sheet with name " + sheetName + " does not exist.");
        }

        Lock lock = sheetLocks.computeIfAbsent(sheetName, k -> new ReentrantLock());
        // נועל את הגישה למנוע של הגיליון
        lock.lock();
        try {
            return sheetEngines.get(sheetName);
        }
        finally {
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
        this.sheetEngines.put(sheet.getSheetName().toLowerCase(), new SheetEngineImpl(sheet,owner));
    }
    public boolean isSheetNameExists(String sheetName) {
        return this.sheetEngines.containsKey(sheetName.toLowerCase());
    }

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

}
