package nl.tranquilizedquality.adm.commons.gwt.ext.client.view;

import nl.tranquilizedquality.adm.commons.gwt.ext.client.AbstractModule;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.widget.ContentPanel;

/**
 * The header panel.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 */
public class Header extends ContentPanel {

	/**
	 * Default constructor.
	 */
	public Header(final String applicationName) {
		setFrame(true);
		setBorders(true);
		setHeaderVisible(false);
		setId("application-header");
		final String version = Registry.get(AbstractModule.VERSION);

		addText("<b>" + applicationName + " - " + version + "</b>");
	}

}
