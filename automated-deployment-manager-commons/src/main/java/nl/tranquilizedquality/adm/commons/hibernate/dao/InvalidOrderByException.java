package nl.tranquilizedquality.adm.commons.hibernate.dao;

import org.springframework.dao.DataAccessException;

/**
 * Used by DAO's that do cross module searches when the order by clause construction failes.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jun. 2011
 */
public class InvalidOrderByException extends DataAccessException {
    private static final long serialVersionUID = -2112935907701532848L;

    /**
     * Constructor that takes a message and the cause of the exception.
     * 
     * @param msg
     *        The error message.
     * @param cause
     *        The {@link Throwable} that was the cause of the exception.
     */
    public InvalidOrderByException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
