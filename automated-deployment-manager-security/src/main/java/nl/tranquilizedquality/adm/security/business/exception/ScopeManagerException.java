package nl.tranquilizedquality.adm.security.business.exception;

import nl.tranquilizedquality.adm.security.business.manager.ScopeManager;

/**
 * Exception thrown by the {@link ScopeManager} when something goes wrong.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public class ScopeManagerException extends RuntimeException {

    /**
     * Unique identifier used for serialization.
     */
    private static final long serialVersionUID = 2706899332162346886L;

    /**
     * Default constructor.
     */
    public ScopeManagerException() {
    }

    /**
     * Constructor where you can specify the error message.
     * 
     * @param msg
     *            The error message that will be used.
     */
    public ScopeManagerException(final String msg) {
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
    public ScopeManagerException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
