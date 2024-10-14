package shticell.sheets.manager;

import parts.SheetDetailsDTO;
import shticell.engines.sheetEngine.SheetEngine;
import shticell.engines.sheetEngine.SheetEngineImpl;
import shticell.permissions.PermissionType;
import shticell.sheets.sheet.Sheet;

import java.util.*;

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
    public synchronized void addSheetEngine(Sheet sheet,String owner) {
//        if (this.sheetEngines.containsKey(sheet.getSheetName())) {
//            throw new IllegalArgumentException("Sheet already exists");
//        }
        this.sheetEngines.put(sheet.getSheetName(), new SheetEngineImpl(sheet,owner));
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

//    public Collection<SheetEngine> getAllSheetEngines() {
//        return sheetEngines.values();
//    }
    public synchronized List<SheetDetailsDTO> getSheetsDetalisList() {
        List<SheetDetailsDTO> list = new LinkedList<>();
        for (SheetEngine sheetEngine : sheetEngines.values()) {
            list.add(sheetEngine.getSheetDetailsDTO());
        }
        return list;
    }

    public void giveDefaultPermissionsToUser(String usernameFromParameter) { //maybe
        for (SheetEngine sheetEngine : sheetEngines.values()) {
            sheetEngine.givePermissionToUser(usernameFromParameter, PermissionType.NONE);
        }
    }
}
