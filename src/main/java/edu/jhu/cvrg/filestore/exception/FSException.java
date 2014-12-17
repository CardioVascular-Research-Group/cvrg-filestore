package edu.jhu.cvrg.filestore.exception;

public class FSException extends Exception {

	private static final long serialVersionUID = 1085916238606592085L;
	
	public FSException(String message, Throwable cause) {
		super(message, cause);
	}

	public FSException(String message) {
		super(message);
	}


}
