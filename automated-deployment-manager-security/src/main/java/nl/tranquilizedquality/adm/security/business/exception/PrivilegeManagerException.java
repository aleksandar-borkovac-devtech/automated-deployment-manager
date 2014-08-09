package nl.tranquilizedquality.adm.security.business.exception;

import nl.tranquilizedquality.adm.security.business.manager.PrivilegeManager;

/**
 * Exception used by the {@link PrivilegeManager}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public class PrivilegeManagerException extends RuntimeException {

	/**
	 * Unique identifier used for serialization.
	 */
	private static final long serialVersionUID = 1358453094005321929L;

	/**
	 * Default constructor.
	 */
	public PrivilegeManagerException() {
	}

	/**
	 * Constructor where you can specify the error message.
	 * 
	 * @param msg
	 *            The error message that will be used.
	 */
	public PrivilegeManagerException(final String msg) {
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
	public PrivilegeManagerException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

}
