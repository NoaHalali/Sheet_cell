package parts.cell;
//import engine.parts.Version;

import java.util.ArrayList;
import java.util.List;

public class CellDTO {
    private final String coordinate  ;//TODO - maybe create coordinate class?
    private final String originalValue;
    private final String effectiveValue;
    private final int version;
    private final List<String> affectOn;
    private final List<String> affectedBy;

    public CellDTO(String coordinate ,String originalValue, String effectiveValue,
                   int version, List<String> affectOn, List<String> affectedBy) {
        this.coordinate = coordinate;
        this.originalValue = originalValue;
        this.effectiveValue = effectiveValue;
        this.version = version;
        this.affectOn = new ArrayList<>(affectOn);
        this.affectedBy = new ArrayList<>(affectedBy);
    }
}
