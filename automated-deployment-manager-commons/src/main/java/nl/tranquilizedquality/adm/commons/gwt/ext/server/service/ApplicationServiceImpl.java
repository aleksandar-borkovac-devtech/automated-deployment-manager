package nl.tranquilizedquality.adm.commons.gwt.ext.server.service;

import java.util.HashMap;
import java.util.Map;

import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.ApplicationService;

/**
 * Generic service used to retrieve application specific information.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 */
public class ApplicationServiceImpl implements ApplicationService {

	/** All the application properties. */
	private Map<String, String> properties;

	/**
	 * Default constructor.
	 */
	public ApplicationServiceImpl() {
		properties = new HashMap<String, String>();
	}

	public String getProperty(final String key) {
		return properties.get(key);
	}

	/**
	 * @return the properties
	 */
	public Map<String, String> getProperties() {
		return properties;
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(final Map<String, String> properties) {
		this.properties = properties;
	}

}
