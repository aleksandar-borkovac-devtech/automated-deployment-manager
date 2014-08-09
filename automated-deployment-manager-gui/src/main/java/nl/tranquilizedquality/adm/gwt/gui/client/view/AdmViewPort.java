package nl.tranquilizedquality.adm.gwt.gui.client.view;

import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.Header;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.AdmDashBoard;
import nl.tranquilizedquality.adm.gwt.gui.client.view.navigation.AdmNavigationPanel;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;

/**
 * Main {@link Viewport} for the blacklist application.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 */
public class AdmViewPort extends Viewport {

	/**
	 * Default constructor.
	 */
	public AdmViewPort() {
		final BorderLayout layout = new BorderLayout();
		setLayout(layout);

		initialize();
	}

	/**
	 * Initializes the widgets.
	 */
	private void initialize() {
		createNorthPanel();

		createWestPanel();

		createCenterPanel();
	}

	/**
	 * Creates the north panel.
	 */
	private void createNorthPanel() {
		final Header header = new Header("Automated Deployment Manager");

		final BorderLayoutData data = new BorderLayoutData(LayoutRegion.NORTH);
		data.setMargins(new Margins());
		data.setSize(30);
		add(header, data);
	}

	/**
	 * Creates the center panel.
	 */
	private void createCenterPanel() {
		final AdmDashBoard dashBoard = new AdmDashBoard();

		final BorderLayoutData data = new BorderLayoutData(LayoutRegion.CENTER);
		data.setMargins(new Margins(5, 5, 5, 0));
		add(dashBoard, data);
	}

	/**
	 * Creates the west panel.
	 */
	private void createWestPanel() {
		final AdmNavigationPanel navPanel = new AdmNavigationPanel();

		final BorderLayoutData data = new BorderLayoutData(LayoutRegion.WEST, 220, 150, 320);
		data.setMargins(new Margins(5, 5, 5, 5));
		data.setCollapsible(true);
		add(navPanel, data);
	}

}
