package parts;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Version {
    private List<Sheet> versionsList= new LinkedList<Sheet>();
    private List <Integer> numberOfCellsChanged =new LinkedList<Integer>();

    public Sheet getSpecificVersion(int version) {
        if(versionsList.size()<version){
            throw new IllegalArgumentException("Version: "+version+" not created yet.");
        }
        return versionsList.get(version-1);
    }
    public void addVersion( Sheet sheet,int numberOfCellsChanged) {
        versionsList.addLast(sheet);
        this.numberOfCellsChanged.add(numberOfCellsChanged);
    }
    public List<Integer> getNumberOfCellsChangedListDeepClone() {
        return numberOfCellsChanged.stream()
                 .map(Integer::valueOf)
                     .collect(Collectors.toList());
    }

}
