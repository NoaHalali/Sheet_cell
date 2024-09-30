package engine.sheets.manager;

import engine.impl.SheetEngine;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SharedSheetManager {
    private Map<String, SheetEngine> sheetEngines; // מיפוי מנועים לפי מזהה גיליון

    public SharedSheetManager() {
        this.sheetEngines = new HashMap<>();
    }

//    public void addSheet(String sheetId, Sheet initialSheet) {
//        sheetEngines.put(sheetId, new SheetEngine(initialSheet));
//    }

    public SheetEngine getSheetEngine(String sheetId) {
        return sheetEngines.get(sheetId);
    }

    public void removeSheet(String sheetId) {
        sheetEngines.remove(sheetId);
    }

    public Collection<SheetEngine> getAllSheetEngines() {
        return sheetEngines.values();
    }

}
