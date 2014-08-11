package nl.tranquilizedquality.adm.core.business.template.renderer;

/**
 * Exception used when the message contains incorrect data.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 19 okt. 2012
 */
public class IncorrectTemplateException extends RuntimeException {

    private static final long serialVersionUID = -7554554849372375719L;

    /**
     * Constructor taking only a message.
     * 
     * @param message
     *            The error message.
     */
    public IncorrectTemplateException(final String message) {
        super(message);
    }

    /**
     * Constructor taking a message and the {@link Throwable} cause.
     * 
     * @param message
     *            The error message.
     * @param cause
     *            The root cause of the exception.
     */
    public IncorrectTemplateException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor taking the {@link Throwable} cause.
     * 
     * @param cause
     *            The rout cause of the exception.
     */
    public IncorrectTemplateException(final Throwable cause) {
        super(cause);
    }
}
