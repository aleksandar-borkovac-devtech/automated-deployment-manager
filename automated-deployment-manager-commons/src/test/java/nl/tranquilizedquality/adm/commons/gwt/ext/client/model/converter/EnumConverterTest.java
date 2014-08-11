package nl.tranquilizedquality.adm.commons.gwt.ext.client.model.converter;

import static org.junit.Assert.assertEquals;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.Action;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.converter.EnumConverter;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.converter.EnumWrapper;

import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link EnumConverter}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 */
public class EnumConverterTest {

    /** Converter that will be tested. */
    private EnumConverter<Action> converter;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        converter = new EnumConverter<Action>();
    }

    @Test
    public void testConvertFieldValueObject() {
        final EnumWrapper<Action> wrapper = new EnumWrapper<Action>(Action.BLAAT);

        final Action value = (Action) converter.convertFieldValue(wrapper);

        assertEquals(Action.BLAAT, value);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testConvertModelValueObject() {
        final EnumWrapper<Action> wrapper = (EnumWrapper<Action>) converter.convertModelValue(Action.BLAAT);

        final Action testEnum = wrapper.get("enum");

        assertEquals(Action.BLAAT, testEnum);
    }

}
