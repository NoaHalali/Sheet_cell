package parts.cell;

import java.util.List;

public class Cell {

    private int lastUpdatedVersion;
    private int rowsIndex;
    private int columnsIndex;
    private Expression originalValue;
    private String EffectiveValue; //אולי להוסיף intetface במקום ב-string
    private List<Cell> neighbors;
    private List<Cell> affectOn;//משפיע על התאים האלה
    private List<Cell> affectedBy; //התאים שמושפע מהם

    public String geEffectiveValue() {
        return EffectiveValue;
    }
    public void updateEffectiveValue(Expression newValue) {
        //בדיקת תקינות
        EffectiveValue = newValue.evaluate();
    }
}
