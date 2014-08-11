package nl.tranquilizedquality.adm.commons.gwt.ext.client.model.converter;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * 
 * Wrapper for java enums so they can be used within the binding context of
 * GWT-EXT.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 feb. 2011
 * 
 * 
 * @param <E>
 *            The enum type that will be wrapped.
 */
public class EnumWrapper<E extends Enum<E>> extends BaseModelData {

    /**
     * Unique identifier for serialization.
     */
    private static final long serialVersionUID = 7468265852759958259L;

    /**
     * Constructor taking the actual enumeration.
     * 
     * @param enumValue
     *            The enum that will be wrapped.
     */
    public EnumWrapper(final E enumValue) {
        if (enumValue != null) {
            set("enum", enumValue);
            set("value", enumValue.toString());
        }
        else {
            /*
             * This means it should have a value of Any..
             */
            set("enum", enumValue);
            set("value", "Any...");
        }
    }

    /**
     * @return the String value of the enum.
     */
    public String getValue() {
        return get("value");
    }

    /**
     * @return the wrapped enum.
     */
    @SuppressWarnings("unchecked")
    public E getEnum() {
        return (E) get("enum");
    }

}
