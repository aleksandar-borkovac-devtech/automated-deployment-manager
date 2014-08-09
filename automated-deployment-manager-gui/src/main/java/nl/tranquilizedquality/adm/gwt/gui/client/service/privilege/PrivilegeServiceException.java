package nl.tranquilizedquality.adm.gwt.gui.client.service.privilege;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.AbstractServiceException;

/**
 * Exception used by the {@link PrivilegeService}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public class PrivilegeServiceException extends AbstractServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -930573883382900269L;

	/**
	 * Default constructor.
	 */
	public PrivilegeServiceException() {
		errors = new ArrayList<String>();
	}

	/**
	 * Constructor where you can specify the error message.
	 * 
	 * @param msg
	 *            The error message that will be used.
	 */
	public PrivilegeServiceException(final String msg) {
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
	public PrivilegeServiceException(final String msg, final Throwable cause) {
		super(msg, cause);

		errors = new ArrayList<String>();
	}

	/**
	 * Constructor where you can specify the error message.
	 * 
	 * @param msg
	 *            The error message that will be used.
	 */
	public PrivilegeServiceException(final String msg, final List<String> errors) {
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
	public PrivilegeServiceException(final String msg, final Throwable cause, final List<String> errors) {
		super(msg, cause);

		this.errors = errors;
	}

}
