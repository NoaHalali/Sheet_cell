package parts;

import java.util.LinkedList;
import java.util.List;

public class Version {
    private List<Sheet> versionsList= new LinkedList<Sheet>();

    public Sheet getSpecificVersion(int version) {
        if(versionsList.size()<version){
            throw new IllegalArgumentException("Version: "+version+" not created yet.");
        }
        return versionsList.get(version-1);
    }
    public void addVersion( Sheet sheet) {
        versionsList.addLast(sheet);
    }
}
