package org.yeastrc.paws.exceptions;

/**
 * Thrown in Module common RunProgramMain when tracking id not on server
 *
 */
public class PawsTrackingIdNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PawsTrackingIdNotFoundException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PawsTrackingIdNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public PawsTrackingIdNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public PawsTrackingIdNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public PawsTrackingIdNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
