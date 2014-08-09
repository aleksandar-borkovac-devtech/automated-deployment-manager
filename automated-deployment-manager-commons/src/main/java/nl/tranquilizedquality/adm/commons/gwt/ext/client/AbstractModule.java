package nl.tranquilizedquality.adm.commons.gwt.ext.client;

import com.google.gwt.core.client.EntryPoint;

/**
 * 
 * Abstract base class for all entry point modules.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 27 jan. 2011
 * 
 * 
 */
public abstract class AbstractModule implements EntryPoint {

	/**
	 * The key used to retrieve the viewport where the whole application is
	 * displayed in.
	 */
	public static final String VIEW_PORT = "VIEW_PORT";

	/** Navigation constants used within the application. */
	public static final String NAVIGATION_CONSTANTS = "navigation_constants";

	/** All icons used within the application. */
	public static final String ICONS = "icons";

	/**
	 * The key used to retrieve the application service from the
	 * {@link com.extjs.gxt.ui.client.Registry}.
	 */
	public static final String APPLICATION_SERVICE = "application_service";

	/**
	 * The key used to retrieve the authorization service from the
	 * {@link com.extjs.gxt.ui.client.Registry}.
	 */
	public static final String AUTHORIZATION_SERVICE = "authorization_service";

	/**
	 * The key used to retrieve the navigation controller from the
	 * {@link com.extjs.gxt.ui.client.Registry}.
	 */
	public static final String NAVIGATION_CONTROLLER = "navigation_controller";

	/** The key used to retrieve the application version. */
	public static final String VERSION = "version";

}
