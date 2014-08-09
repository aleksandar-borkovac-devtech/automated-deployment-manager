package nl.tranquilizedquality.adm.commons.gwt.ext.client.view;

/**
 * Item used for navigation where a model object can be set.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 * @param <T>
 *            The implementation type
 */
public interface NavigationalItem<T> {

	/**
	 * Set the model for the GUI object that is navigated to.
	 * 
	 * @param model
	 *            the model that contains the (key) data for the item to
	 *            display.
	 */
	void setModel(T model);
}
