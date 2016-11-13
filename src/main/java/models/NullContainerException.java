package models;

/**
 * 
 * Exception class for null container id creation
 *
 */
public class NullContainerException extends Exception {
    
	private static final long serialVersionUID = 1L;

	public NullContainerException() {
        super("Container id not informed");
    }
}