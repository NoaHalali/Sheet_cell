package parts.cell;

import java.util.ArrayList;
import java.util.List;

public class Cell {
    //row and col, or coordinate?
    private int rowsIndex;
    private int columnsIndex;
    private int lastUpdatedVersion;
    private Expression cellValue;
    //private String EffectiveValue; //אולי להוסיף intetface במקום ב-string
    private List<Cell> neighbors;
    private List<Cell> affectOn;//משפיע על התאים האלה
    private List<Cell> affectedBy; //התאים שמושפע מהם

//    public Cell()
//    {
//       // cellValue = new Expression();
//    }
    public void updateValue(Expression newValue) {
        cellValue = newValue; //בינתיים
        cellValue.setOriginalValue(newValue);
    }
    public EffectiveValue geEffectiveValue()
    {
        return cellValue.evaluate();
    }

//    public String geEffectiveValue() {
//        return EffectiveValue;
//    }
//    public void updateEffectiveValue(Expression newValue) {
//        //בדיקת תקינות
//        EffectiveValue = newValue.evaluate();
//    }

    public CellDTO toCellDTO() {
        return new CellDTO(
                getIdentity(), //"A4"
                cellValue.getOriginalValue(),
                lastUpdatedVersion,
                geEffectiveValue(),
                getAffectOnNames(),
                getAffectedByNames()
        );
    }

    private List<String> getAffectedByNames() {
        //List<String> affectOnNames = affectOn.stream().map() משהו בסגנון
        return new ArrayList<String>();
        //TODO
    }

    private List<String> getAffectOnNames() {
        //List<String> affectOnNames = affectOn.stream().map()
        return new ArrayList<String>();
        //TODO

    }

    private String getIdentity() {
        return "";
        //TODO
        //getCoordinate?
    }



}
