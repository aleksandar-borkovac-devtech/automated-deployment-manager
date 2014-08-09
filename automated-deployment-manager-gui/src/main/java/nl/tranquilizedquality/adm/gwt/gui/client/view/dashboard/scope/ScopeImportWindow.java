package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.scope;

import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

/**
 * The {@link Window} where you can import an XML file containing a scope with
 * it's privileges and roles.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 26, 2012
 * 
 */
public class ScopeImportWindow extends Window {

	/** The import panel itself. */
	private final ScopeImportPanel importPanel;

	/** The grid where the scopes are displayed in. */
	private final ScopeTable scopeTable;

	/**
	 * Default constructor.
	 */
	public ScopeImportWindow(final ScopeTable scopeTable) {
		setLayout(new FitLayout());
		setSize(350, 150);

		this.scopeTable = scopeTable;

		importPanel = new ScopeImportPanel();
		importPanel.setWindow(this);

		add(importPanel);
	}

	@Override
	public void hide() {
		this.scopeTable.refresh();

		super.hide();
	}

}
