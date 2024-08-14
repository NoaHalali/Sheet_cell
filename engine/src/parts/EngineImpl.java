package parts;

import parts.cell.Cell;
import parts.cell.Expression;

public class EngineImpl implements Engine{

    //maybe static? (Amir, REF function)
    Sheet currentSheet = null;

    //TODO move all functions to UI,
    // change names to get and not print/show
    public void readFileData()
    {
        //TODO
    }

    //TODO - maybe return data (DTO?) and not void?
    public void showCurrentSheet()
    {
        currentSheet.printSheetData();
    }

    //הUI יקבל מחרוזת 'A4' למשל, ויעביר למנוע את התא עצמו?
    public void showCellEffectiveValue(Cell cell)
    {

    }

    public void updeteCellValue(Cell cell, Expression value) //?
    {
        cell.updateValue(value);
        //.............. להמשיך
        //......
    }

    public void showVersions()
    {

    }

    public void exit()
    {
        //אם מממשים שמירה בקובץ אז לשמור ולצאת
        //אחרת פשוט לצאת וזהו?
    }









}
