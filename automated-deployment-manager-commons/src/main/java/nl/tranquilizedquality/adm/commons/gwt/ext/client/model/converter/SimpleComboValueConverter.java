package nl.tranquilizedquality.adm.commons.gwt.ext.client.model.converter;

import com.extjs.gxt.ui.client.binding.Converter;
import com.extjs.gxt.ui.client.widget.form.SimpleComboValue;

/**
 * 
 * Converts the a value into a {@link SimpleComboValue} and visa versa.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 feb. 2011
 * 
 * 
 * @param <T>
 */
public class SimpleComboValueConverter<T> extends Converter {

	@SuppressWarnings("unchecked")
	@Override
	public Object convertFieldValue(final Object value) {
		if (value == null) {
			return value;
		}

		final SimpleComboValue<T> comboValue = (SimpleComboValue<T>) value;

		return comboValue.getValue();
	}

	@Override
	public Object convertModelValue(final Object value) {
		if (value == null) {
			return value;
		}

		final SimpleComboValue<String> comboValue = new SimpleComboValue<String>() {

			/**
             * 
             */
			private static final long serialVersionUID = -2024505771795986397L;
		};

		comboValue.setValue(value.toString());

		return comboValue;
	}

}
