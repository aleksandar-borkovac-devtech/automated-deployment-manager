package nl.tranquilizedquality.adm.commons.gwt.ext.client.model;

import nl.tranquilizedquality.adm.commons.domain.AbstractUpdatableModel;

import com.extjs.gxt.ui.client.data.BeanModelTag;

/**
 * Client side representation of a updateable object.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 * @param <T>
 *            The implementaiton type.
 */
public abstract class AbstractUpdatableBeanModel<T> extends AbstractUpdatableModel<T> implements
        BeanModelTag {

    /** Unique identifier. */
    private static final long serialVersionUID = 704572028962551430L;

}
