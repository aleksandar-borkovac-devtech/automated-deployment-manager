package nl.tranquilizedquality.adm.commons.gwt.ext.client.service;

import java.util.List;

/**
 * Base {@link Exception} used for GWT services.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 */
public abstract class AbstractServiceException extends Exception {

	/** Unique identifier */
	private static final long serialVersionUID = -7565794778046194125L;

	/** List of errors that occurred. */
	protected List<String> errors;

	public AbstractServiceException() {
		super();
	}

	public AbstractServiceException(final String message) {
		super(message);
	}

	public AbstractServiceException(final Throwable cause) {
		super(cause);
	}

	public AbstractServiceException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * @return the errors
	 */
	public List<String> getErrors() {
		return errors;
	}

	/**
	 * @param errors
	 *            the errors to set
	 */
	public void setErrors(final List<String> errors) {
		this.errors = errors;
	}

}
