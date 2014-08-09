package nl.tranquilizedquality.adm.commons.gwt.ext.client.view;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.data.TreeLoader;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TreeGridEvent;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;

/**
 * 
 * * Abstract panel for managing relationship tree with given objects and its
 * children.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 27 jan. 2011
 * 
 * 
 * @param <T>
 *            The implementation type of the model this table is displaying.
 */
public abstract class AbstractRelationTreeTable<T> extends ContentPanel implements
		NavigationalItem<List<T>> {

	/** The {@link TreeGrid} containing all the records. */
	protected TreeGrid<ModelData> grid;

	/** The model used in this grid */
	protected List<T> model;

	/**
	 * A {@link List} containing {@link MenuItem} objects that will be used to
	 * create a context menu on the grid.
	 */
	protected List<MenuItem> menuItems;

	/**
	 * A {@link List} containing {@link Button} objects that will be put on the
	 * toolbar.
	 */
	protected List<Button> menuBarButtons;

	/**
	 * Default constructor.
	 */
	public AbstractRelationTreeTable() {
		setBodyBorder(false);
		setHeading("Records");
		setButtonAlign(HorizontalAlignment.CENTER);
		setLayout(new FitLayout());

		menuItems = new ArrayList<MenuItem>();
		menuBarButtons = new ArrayList<Button>();
	}

	/**
	 * Set the model used by the grid.
	 * 
	 * @param model
	 *            the model.
	 */
	public void setModel(List<T> model) {
		// The model must be a plain ArrayList, otherwise it won't be
		// serializable
		if (!(model instanceof ArrayList<?>)) {
			final List<T> objects = new ArrayList<T>();

			if (model != null) {
				for (final T object : model) {
					objects.add(object);
				}
			}

			model = objects;
		}

		this.model = model;

		final TreeLoader<ModelData> loader = grid.getTreeStore().getLoader();
		loader.load(null);
	}

	/**
	 * @return the model
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<T> getModel() {
		return model;
	}

	protected void initializeWidgets() {
		grid = createTable();

		if (menuBarButtons != null && !menuBarButtons.isEmpty()) {
			setTopComponent(createTopToolbar());
		}

		add(grid);
	}

	/**
	 * Create a toolbar for the panel.
	 * 
	 * @return the {@link ToolBar}.
	 */
	protected ToolBar createTopToolbar() {
		/*
		 * Create the toolbar itself.
		 */
		final ToolBar toolBar = new ToolBar();

		for (final Button button : menuBarButtons) {
			toolBar.add(button);
			toolBar.add(new SeparatorToolItem());
		}

		return toolBar;
	}

	/**
	 * Creates a context menu.
	 * 
	 * @return Returns a {@link Menu}.
	 */
	protected Menu createContextMenu() {
		final Menu menu = new Menu();

		for (final MenuItem menuItem : this.menuItems) {
			menu.add(menuItem);
		}

		return menu;
	}

	/**
	 * Create the column configuration for the grid.
	 * 
	 * @return a list of column configurations.
	 */
	protected abstract List<ColumnConfig> createColumns();

	/**
	 * Get the proxy that will retrieve the children of a parent object.
	 * 
	 * @return the proxy to use for the grid.
	 */
	protected abstract RpcProxy<List<ModelData>> getProxy();

	/**
	 * Determine if the given {@link ModelData} object is a parent that could
	 * have children to show.
	 * 
	 * @param data
	 *            the tree item that is a possible parent.
	 * @return a boolean indicating if it is a parent.
	 */
	protected abstract boolean isParent(ModelData data);

	/**
	 * Creates the table on a {@link ContentPanel}.
	 * 
	 * @return Returns the grid.
	 */
	protected TreeGrid<ModelData> createTable() {
		final List<ColumnConfig> configs = createColumns();
		final ColumnModel cm = new ColumnModel(configs);

		// Tree loader
		final TreeLoader<ModelData> loader = new BaseTreeLoader<ModelData>(getProxy()) {

			@Override
			public boolean hasChildren(final ModelData parent) {
				return isParent(parent);
			}
		};

		// Trees store
		final TreeStore<ModelData> store = new TreeStore<ModelData>(loader);

		final TreeGrid<ModelData> grid = new TreeGrid<ModelData>(store, cm);
		grid.setStyleAttribute("borderTop", "none");
		grid.getView().setForceFit(true);
		grid.setBorders(true);
		grid.addListener(Events.CellDoubleClick, new Listener<TreeGridEvent<ModelData>>() {

			public void handleEvent(final TreeGridEvent<ModelData> be) {
				handleDoubleClick(be);
			}

		});

		if (this.menuItems != null && !this.menuItems.isEmpty()) {
			final Menu menu = createContextMenu();
			grid.setContextMenu(menu);
		}

		return grid;
	}

	protected void handleDoubleClick(final TreeGridEvent<ModelData> be) {

	}
}
