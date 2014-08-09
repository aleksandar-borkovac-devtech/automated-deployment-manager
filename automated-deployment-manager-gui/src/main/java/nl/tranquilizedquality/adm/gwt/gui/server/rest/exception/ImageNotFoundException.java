package nl.tranquilizedquality.adm.gwt.gui.server.rest.exception;

/**
 * Exception thrown when an image couldn't be found.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 26, 2012
 * 
 */
public class ImageNotFoundException extends RuntimeException {

	/**
	 * Unique identifier used for serialization.
	 */
	private static final long serialVersionUID = -7827381296163688892L;

	/**
	 * Default constructor.
	 */
	public ImageNotFoundException() {
	}

	/**
	 * Constructor where you can specify the error message.
	 * 
	 * @param msg
	 *            The error message that will be used.
	 */
	public ImageNotFoundException(final String msg) {
		super(msg);
	}

	/**
	 * Constructor where you can specify the error message and the cause of this
	 * exception.
	 * 
	 * @param msg
	 *            The error message that will be used.
	 * @param cause
	 *            The {@link Throwable} that caused this exception.
	 */
	public ImageNotFoundException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

}
