package nl.tranquilizedquality.adm.core.util;

import nl.tranquilizedquality.adm.core.business.deployer.exception.DeployException;

import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class assists in validating arguments, and wraps any exceptions into DeployExceptions.
 *
 * @author e-pragt (erik.pragt@Tranquilized Quality.com)
 * @since 10/23/12
 */
public abstract class AdmValidate {

    /** Logger for this class. */
    private static final Log LOGGER = LogFactory.getLog(AdmValidate.class);

    /**
     * Wrapper for Validate.isTrue.
     *
     * @param expression the boolean expression to check
     * @param message the exception message if invalid
     */
    public static void isTrue(boolean expression, String message) {
        try {
            Validate.isTrue(expression, message);
        } catch(IllegalArgumentException e) {
            handleException(message);
        }
    }

    /**
     * Wrapper for Validate.notNull
     *
     * @param object the object to check
     * @param message the exception message if invalid
     */
    public static void notNull(Object object, String message) {
        try {
            Validate.notNull(object, message);
        } catch(IllegalArgumentException e) {
            handleException(message);
        }
    }

    /**
     * Wrapper for Validate.notEmpty
     *
     * @param string the string to check
     * @param message the exception message if invalid
     */
    public static void notEmpty(String string, String message) {
        try {
            Validate.notEmpty(string, message);
        } catch (IllegalArgumentException e) {
            handleException(message);
        }
    }

    /**
     * Logs and rethrows the exception.
     *
     * @param message The message to log and rethrow.
     */
    private static void handleException(String message) {
        if (LOGGER.isErrorEnabled()) {
            LOGGER.error(message);
        }
        throw new DeployException(message);
    }
}
