package nl.tranquilizedquality.adm.commons.gwt.ext.client.service;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Generic service used to retrieve application specific information.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 */
@RemoteServiceRelativePath("ApplicationService.rpc")
public interface ApplicationService extends RemoteService {

	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {

		private static ApplicationServiceAsync instance;

		public static ApplicationServiceAsync getInstance() {
			if (instance == null) {
				instance = GWT.create(ApplicationService.class);
			}
			return instance;
		}
	}

	/**
	 * Retrieves the property value with the specified key.
	 * 
	 * @param key
	 *            The key of the value that needs to be retrieved.
	 * @return Returns a {@link String} value of the property.
	 */
	String getProperty(String key);

}
