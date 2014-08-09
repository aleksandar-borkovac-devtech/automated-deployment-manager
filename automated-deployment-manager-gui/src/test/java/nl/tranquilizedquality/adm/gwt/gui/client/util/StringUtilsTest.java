package nl.tranquilizedquality.adm.gwt.gui.client.util;

import nl.tranquilizedquality.adm.gwt.gui.client.util.StringUtils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author e-pragt (erik.pragt@Tranquilized Quality.com)
 * @since 10/23/12
 */
public class StringUtilsTest {

    @Test
    public void testEmpty() {
        assertTrue(StringUtils.isEmpty(null));
        assertTrue(StringUtils.isEmpty(""));

        assertFalse(StringUtils.isEmpty(" "));
        assertFalse(StringUtils.isEmpty("abc"));
        assertFalse(StringUtils.isEmpty("null"));
    }

    @Test
    public void testNotEmpty() {
        assertTrue(StringUtils.isNotEmpty(" "));
        assertTrue(StringUtils.isNotEmpty("abc"));
        assertTrue(StringUtils.isNotEmpty("null"));

        assertFalse(StringUtils.isNotEmpty(null));
        assertFalse(StringUtils.isNotEmpty(""));
    }

    @Test
    public void testDefaultstring() {
        assertEquals("", StringUtils.defaultString(null));
        assertEquals("", StringUtils.defaultString(""));
        assertEquals("erik", StringUtils.defaultString("erik"));
    }
}
