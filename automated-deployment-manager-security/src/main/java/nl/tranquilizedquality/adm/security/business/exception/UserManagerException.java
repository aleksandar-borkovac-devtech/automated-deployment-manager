package nl.tranquilizedquality.adm.security.business.exception;

/**
 * Exception thrown by the {@link UserManager} when something goes wrong.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public class UserManagerException extends RuntimeException {

    /**
     * Unique identifier used for serialization.
     */
    private static final long serialVersionUID = 14998185481083538L;

    /**
     * Default constructor.
     */
    public UserManagerException() {
    }

    /**
     * Constructor where you can specify the error message.
     * 
     * @param msg
     *            The error message that will be used.
     */
    public UserManagerException(final String msg) {
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
    public UserManagerException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

}
