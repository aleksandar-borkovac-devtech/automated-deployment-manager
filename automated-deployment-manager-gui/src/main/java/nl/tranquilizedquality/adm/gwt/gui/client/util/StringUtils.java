package nl.tranquilizedquality.adm.gwt.gui.client.util;

/**
 * Lightweight version of StringUtils to be used in views.
 *
 * @author e-pragt (erik.pragt@Tranquilized Quality.com)
 * @since 10/23/12
 */
public class StringUtils {

    /**
     * Checks if a String is empty ("") or null.
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * Checks if a String is not empty ("") and not null.
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    /**
     * Checks if a String is null, returning "" instead
     */
    public static String defaultString(String str) {
        if (str == null) {
            return "";
        } else {
            return str;
        }
    }

}
