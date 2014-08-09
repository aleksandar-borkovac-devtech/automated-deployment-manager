package nl.tranquilizedquality.adm.commons.gwt.ext.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Generic service used to retrieve application specific information.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 */
public interface ApplicationServiceAsync {

	/**
	 * Retrieves the property value with the specified key.
	 * 
	 * @param key
	 *            The key of the value that needs to be retrieved.
	 * @param callback
	 *            The {@link AsyncCallback} used to return a {@link String}
	 *            value of the property.
	 */
	void getProperty(String key, AsyncCallback<String> callback);

}
