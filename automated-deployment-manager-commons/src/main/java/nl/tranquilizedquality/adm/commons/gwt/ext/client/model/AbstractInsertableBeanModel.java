package nl.tranquilizedquality.adm.commons.gwt.ext.client.model;

import nl.tranquilizedquality.adm.commons.domain.AbstractInsertableModel;

import com.extjs.gxt.ui.client.data.BeanModelTag;

/**
 * Client side representation of an instertable object.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 * @param <T>
 *            The implementation type of the primary key of this object.
 */
public abstract class AbstractInsertableBeanModel<T> extends AbstractInsertableModel<T> implements
        BeanModelTag {

    /** Unique identifier. */
    private static final long serialVersionUID = -5540897967771867864L;

}
