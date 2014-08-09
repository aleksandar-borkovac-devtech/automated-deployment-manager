package nl.tranquilizedquality.adm.commons.logging;

import java.net.URISyntaxException;
import java.net.URL;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.FileWatchdog;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * <pre>
 * Initializer class used to initialize log4j.<br/>
 * <br/>
 * Configure this class as a bean in your Spring configuration. There are two
 * constructors. Both take the location of the log4j configuration file as
 * argument. One takes the number of milliseconds between checks on whether the
 * log4j configuration file has changed. If changed the file will be reloaded.
 * If you use the no-argument constructor the interval will be set to 60
 * seconds.<br/>
 * <br/>
 * The log4j configuration file can be XML based or property based. It must be
 * reachable from the classpath, but can of course be located in a subdirectory. <br/>
 * <br/>
 * If ~tomcat/shared/classes is on the classpath, and the log4j file is located
 * in ~tomcat/shared/classes/properties/burlodo-agent, then you can initialize
 * with:
 * 
 * <pre>
 * <code>
 *      Log4jInitializer("properties/burlodo-agent/log4j.xml", 300000);
 * </code>
 * </pre>
 * 
 * The constructor inspects the filename extension to decide on XML or property
 * based initialization. Use ".xml" (lowercase) as extension to trigger XML
 * based initialization. <br/>
 * </pre>
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 */
public class Log4jInitializer {

	/**
	 * Initialize log4j with the given file and reload check interval. It
	 * accepts XML as well as property based configuration files located on the
	 * class path. It will check at every <code>reloadCheckInterval</code> if
	 * the file has changed. If changed it will be reloaded to configure log4j.
	 * 
	 * @param configFilePath
	 *            - the path to the configuration file.
	 * @param reloadCheckInterval
	 *            - the reload check interval.
	 */
	public Log4jInitializer(final String configFilePath, final long reloadCheckInterval) {
		final URL url = Thread.currentThread().getContextClassLoader().getResource(configFilePath);

		// If found, set the logging configuration.
		if (url != null) {
			try {
				if (configFilePath.endsWith(".xml")) {
					DOMConfigurator.configureAndWatch(url.toURI().getPath(), reloadCheckInterval);
				}
				else {
					PropertyConfigurator.configureAndWatch(url.toURI().getPath(), reloadCheckInterval);
				}
			}
			catch (final URISyntaxException e) {
				throw new IllegalArgumentException("Could not locate configuration file "
						+ configFilePath);
			}
		}
		else {
			throw new IllegalArgumentException("Configuration file (" + configFilePath
					+ ") not found");
		}
	}

	/**
	 * Initialize log4j with the given file and reload check interval. It works
	 * the same as {@link #Log4jInitializer(String, long)} except that it uses
	 * {@link FileWatchdog.DEFAULT_DELAY} for the reload check interval.
	 * 
	 * @param configFilePath
	 *            - the path to the configuration file.
	 */
	public Log4jInitializer(final String configFilePath) {
		this(configFilePath, FileWatchdog.DEFAULT_DELAY);
	}

}
