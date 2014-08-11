package nl.tranquilizedquality.adm.commons.gwt.ext.client.model.converter;

import static junit.framework.Assert.assertEquals;

import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.converter.SimpleComboValueConverter;

import org.junit.Before;
import org.junit.Test;

import com.extjs.gxt.ui.client.widget.form.SimpleComboValue;

/**
 * 
 * Test for {@link SimpleComboValueConverter}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 feb. 2011
 * 
 * 
 */
public class SimpleComboValueConverterTest {

    /** Converter that will be tested. */
    private SimpleComboValueConverter<String> converter;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        converter = new SimpleComboValueConverter<String>();
    }

    @SuppressWarnings("serial")
    @Test
    public void testConvertFieldValueObject() {
        final SimpleComboValue<String> combo = new SimpleComboValue<String>("test") {

        };

        final String value = (String) converter.convertFieldValue(combo);

        assertEquals("test", value);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testConvertModelValueObject() {

        final SimpleComboValue<String> comboValue = (SimpleComboValue<String>) converter.convertModelValue("test");

        assertEquals("test", comboValue.getValue());
    }

}
