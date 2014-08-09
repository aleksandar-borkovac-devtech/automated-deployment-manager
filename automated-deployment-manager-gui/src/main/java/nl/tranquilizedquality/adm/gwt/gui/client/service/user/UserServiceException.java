package nl.tranquilizedquality.adm.gwt.gui.client.service.user;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.AbstractServiceException;

/**
 * Client side exception used to distinguish RPC call errors and business logic
 * errors.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public class UserServiceException extends AbstractServiceException {

	/**
     * 
     */
	private static final long serialVersionUID = -8033532775163430270L;

	/**
	 * Default constructor.
	 */
	public UserServiceException() {
		errors = new ArrayList<String>();
	}

	/**
	 * Constructor where you can specify the error message.
	 * 
	 * @param msg
	 *            The error message that will be used.
	 */
	public UserServiceException(final String msg) {
		super(msg);

		errors = new ArrayList<String>();
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
	public UserServiceException(final String msg, final Throwable cause) {
		super(msg, cause);

		errors = new ArrayList<String>();
	}

	/**
	 * Constructor where you can specify the error message.
	 * 
	 * @param msg
	 *            The error message that will be used.
	 */
	public UserServiceException(final String msg, final List<String> errors) {
		super(msg);

		this.errors = errors;
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
	public UserServiceException(final String msg, final Throwable cause, final List<String> errors) {
		super(msg, cause);

		this.errors = errors;
	}

}
