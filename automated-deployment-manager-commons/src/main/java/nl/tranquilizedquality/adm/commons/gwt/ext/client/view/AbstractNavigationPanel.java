package nl.tranquilizedquality.adm.commons.gwt.ext.client.view;

import java.util.List;

import nl.tranquilizedquality.adm.commons.gwt.ext.client.AbstractModule;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.controller.navigation.AbstractNavigationController;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.icons.ApplicationIcons;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.navigation.TreeMenuItem;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanelSelectionModel;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.Window;

/**
 * The {@link ContentPanel} that will show the navigation menu.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 * @param <Consts>
 *            The navigation constants.
 * @param <Icons>
 *            The icons used in the navigation panel.
 */
public abstract class AbstractNavigationPanel<Consts extends Constants, Icons extends ApplicationIcons>
		extends ContentPanel {

	/** The navigation constants of the application. */
	protected final Consts constants;

	/** The {@link TreePanel} that will be used as navigation menu. */
	protected TreePanel<ModelData> tree;

	/**
	 * Default constructor.
	 */
	@SuppressWarnings("unchecked")
	public AbstractNavigationPanel() {
		setId("navigation-panel");
		setHeading("Menu");
		setLayout(new FitLayout());
		setBorders(false);
		setEnabled(true);

		constants = (Consts) Registry.get(AbstractModule.NAVIGATION_CONSTANTS);

		initialize();
	}

	/**
	 * Initializes the widgets. This can be overridden. You can get at the
	 * {@link TreePanel} with the {@link AbstractNavigationPanel#getTreePanel()}
	 * .
	 */
	protected void initialize() {
		final TreeStore<ModelData> store = createMenu();

		tree = new TreePanel<ModelData>(store);
		tree.setId("navigation-tree-panel");
		tree.setDisplayProperty("name");
		tree.setEnabled(true);
		tree.expandAll();

		final TreePanelSelectionModel<ModelData> selectionModel = tree.getSelectionModel();
		selectionModel.addSelectionChangedListener(new SelectionChangedListener<ModelData>() {

			@Override
			public void selectionChanged(final SelectionChangedEvent<ModelData> se) {
				final TreeMenuItem selectedItem = (TreeMenuItem) se.getSelectedItem();

				if (selectedItem.isLogoutItem()) {
					Window.Location.replace("j_spring_security_logout");
				}
				else {
					final TabEnum tab = selectedItem.getTab();

					if (tab != null) {
						selectTab(tab);
					}
				}
			}

		});

		add(tree);
	}

	/**
	 * Get the {@link TreePanel} that holds the {@link TreeMenuItem}s.
	 * 
	 * @return the {@link TreePanel} object.
	 */
	protected TreePanel<ModelData> getTreePanel() {
		return tree;
	}

	/**
	 * @return the logout {@link TreeMenuItem}.
	 */
	public abstract TreeMenuItem getLogoutTreeMenuItem();

	/**
	 * Creates the navigation menu.
	 * 
	 * @return Returns the root {@link TreeMenuItem}s that make up the menu
	 *         structure.
	 */
	public abstract List<TreeMenuItem> getRootTreeMenuItems();

	/**
	 * Creates the navigation menu.
	 * 
	 * @return Returns the {@link TreeStore} containing the menu structure.
	 */
	private TreeStore<ModelData> createMenu() {
		/*
		 * Create the logout menu item.
		 */
		final TreeMenuItem logoutItem = getLogoutTreeMenuItem();

		/*
		 * Get the menu items.
		 */
		final List<TreeMenuItem> treeMenuItems = getRootTreeMenuItems();

		/*
		 * Create root menu.
		 */
		final TreeMenuItem root = new TreeMenuItem("root");
		for (final TreeMenuItem treeMenuItem : treeMenuItems) {
			root.add(treeMenuItem);
		}

		/*
		 * Add the items to the store.
		 */
		final TreeStore<ModelData> store = new TreeStore<ModelData>();
		store.add(root.getChildren(), true);
		store.add(logoutItem, true);
		return store;
	}

	/**
	 * Selects the tab with the specified {@link PromTab} identifier.
	 * 
	 * @param tab
	 *            The {@link PromTab} identifier of a specific tab.
	 */
	private void selectTab(final TabEnum tab) {
		final AbstractNavigationController<Consts, Icons, TabEnum> controller = Registry.get(AbstractModule.NAVIGATION_CONTROLLER);

		controller.selectTab(tab);
	}
}
