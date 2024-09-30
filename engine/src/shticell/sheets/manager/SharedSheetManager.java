package shticell.sheets.manager;

import shticell.engine.impl.SheetEngine;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SharedSheetManager {
    private Map<String, SheetEngine> sheetEngines; // מיפוי מנועים לפי מזהה גיליון

    public SharedSheetManager() {
        this.sheetEngines = new HashMap<>();
    }
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

    public synchronized SheetEngine getSheetEngine(String sheetId) {
        return sheetEngines.get(sheetId);
    }

    public synchronized void removeSheet(String sheetId) {
        sheetEngines.remove(sheetId);
    }

    public Collection<SheetEngine> getAllSheetEngines() {
        return sheetEngines.values();
    }

}
