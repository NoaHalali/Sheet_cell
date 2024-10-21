package shticell.exceptions;

public class OutdatedSheetVersionException  extends RuntimeException{


    public OutdatedSheetVersionException(String message) {
        super(message);
    }
}
