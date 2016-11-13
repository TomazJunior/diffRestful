package models;

public class NullContainerException extends Exception {
    
	public NullContainerException() {
        super("Container id not informed");
    }
}