package nl.tranquilizedquality.adm.commons.gwt.ext.client.model.converter;

import com.extjs.gxt.ui.client.binding.Converter;

/**
 * 
 * Converts an {@link EnumWrapper} to a {@link String} and visa versa.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 feb. 2011
 * 
 * 
 * @param <E>
 *            The neum type that needs to be converted.
 */
public class EnumConverter<E extends Enum<E>> extends Converter {

	@SuppressWarnings("rawtypes")
	@Override
	public Object convertFieldValue(final Object value) {
		if (value == null) {
			return null;
		}

		return ((EnumWrapper) value).get("enum");
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object convertModelValue(final Object value) {
		if (value == null) {
			return null;
		}

		return new EnumWrapper<E>((E) value);
	}
}
