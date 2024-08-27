package parts.cell;

public class EmptyCellDTO implements CellDTO {

    //boolean created;
    int lastUpdateVersion;

    public EmptyCellDTO(int lastUpdateVersion) {
        this.lastUpdateVersion = lastUpdateVersion;
    }

    public int getLastUpdatedVersion() {
        return lastUpdateVersion;
    }
}
