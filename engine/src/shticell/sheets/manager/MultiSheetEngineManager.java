package shticell.sheets.manager;

import shticell.engine.impl.SheetEngine;
import shticell.sheets.sheet.Sheet;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MultiSheetEngineManager {
    private Map<String, SheetEngine> sheetEngines; // מיפוי מנועים לפי מזהה גיליון

    public MultiSheetEngineManager() {
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
    public synchronized void addSheetEngine(Sheet sheet) {
//        if (this.sheetEngines.containsKey(sheet.getSheetName())) {
//            throw new IllegalArgumentException("Sheet already exists");
//        }
        this.sheetEngines.put(sheet.getSheetName(), new SheetEngine(sheet));
    }
    public boolean isSheetNameExists(String sheetName) {
        return this.sheetEngines.containsKey(sheetName);
    }

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
