package nl.tranquilizedquality.adm.commons.gwt.ext.client.model.converter;

import com.extjs.gxt.ui.client.binding.Converter;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;

/**
 * Converts {@link BeanModel} into the correct underlying bean and visa versa.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 feb. 2011
 */
public class BeanModelConverter extends Converter {

    @Override
    public Object convertFieldValue(final Object value) {
        if (value == null) {
            return value;
        }

        final BeanModel model = (BeanModel) value;

        return model.getBean();
    }

    @Override
    public Object convertModelValue(final Object value) {
        if (value == null) {
            return null;
        }

        final Class<? extends Object> classValue = value.getClass();
        final BeanModelLookup beanModelLookup = BeanModelLookup.get();
        final BeanModelFactory factory = beanModelLookup.getFactory(classValue);

        return factory.createModel(value);
    }

}
