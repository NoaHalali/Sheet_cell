import shticell.files.FileManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class TestDepenedns {
    public void foo(){
        FileManager f= new FileManager();
        try{

       // f.processFile( new FileInputStream(new File("C:/Users/amir/IdeaProjects/Sheet_cell/engine/src/shticell/files/resources/grades.xml")));
        }catch (Exception E)
        {
            E.printStackTrace();
        }
    }
    public static void main(String[] args){
        TestDepenedns t= new TestDepenedns();
        t.foo();

    }
}
