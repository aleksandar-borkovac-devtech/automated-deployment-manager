package nl.tranquilizedquality.adm.gwt.gui.client.service.role;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.AbstractServiceException;

/**
 * Exception thrown when something goes wrong in the role service.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public class RoleServiceException extends AbstractServiceException {

	/**
     * 
     */
	private static final long serialVersionUID = -2373427478416918257L;

	/**
	 * Default constructor.
	 */
	public RoleServiceException() {
		errors = new ArrayList<String>();
	}

	/**
	 * Constructor where you can specify the error message.
	 * 
	 * @param msg
	 *            The error message that will be used.
	 */
	public RoleServiceException(final String msg) {
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
	public RoleServiceException(final String msg, final Throwable cause) {
		super(msg, cause);

		errors = new ArrayList<String>();
	}

	/**
	 * Constructor where you can specify the error message.
	 * 
	 * @param msg
	 *            The error message that will be used.
	 */
	public RoleServiceException(final String msg, final List<String> errors) {
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
	public RoleServiceException(final String msg, final Throwable cause, final List<String> errors) {
		super(msg, cause);

		this.errors = errors;
	}

}
