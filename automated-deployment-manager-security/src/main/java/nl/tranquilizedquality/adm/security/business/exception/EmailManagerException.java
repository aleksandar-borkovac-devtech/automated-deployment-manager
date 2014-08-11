package nl.tranquilizedquality.adm.security.business.exception;

import nl.tranquilizedquality.adm.security.business.manager.EmailManager;

/**
 * Exception thrown by the {@link EmailManager}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public class EmailManagerException extends RuntimeException {

    /**
     * Unique identifier used for serialization.
     */
    private static final long serialVersionUID = 8743990880578590534L;

    /**
     * Default constructor.
     */
    public EmailManagerException() {
    }

    /**
     * Constructor where you can specify the error message.
     * 
     * @param msg
     *            The error message that will be used.
     */
    public EmailManagerException(final String msg) {
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
    public EmailManagerException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

}
