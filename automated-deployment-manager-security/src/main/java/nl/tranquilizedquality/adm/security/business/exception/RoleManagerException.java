package nl.tranquilizedquality.adm.security.business.exception;

import nl.tranquilizedquality.adm.security.business.manager.RoleManager;

/**
 * Exception thrown by a {@link RoleManager}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public class RoleManagerException extends RuntimeException {

    /**
     * Unique identifier used for serialization.
     */
    private static final long serialVersionUID = 1171498378760615721L;

    /**
     * Default constructor.
     */
    public RoleManagerException() {
    }

    /**
     * Constructor where you can specify the error message.
     * 
     * @param msg
     *            The error message that will be used.
     */
    public RoleManagerException(final String msg) {
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
    public RoleManagerException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

}
