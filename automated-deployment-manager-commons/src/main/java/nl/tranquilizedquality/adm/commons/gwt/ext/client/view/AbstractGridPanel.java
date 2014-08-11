package nl.tranquilizedquality.adm.commons.gwt.ext.client.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelReader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;

/**
 * Abstract base class for a {@link ContentPanel} that contains a paging grid
 * with menu bar, context menu and double click handling.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 */
public abstract class AbstractGridPanel extends ContentPanel {

    /** The {@link Grid} containing all the records. */
    protected Grid<BeanModel> grid;

    /** The {@link PagingLoader} used to load data per page. */
    protected PagingLoader<PagingLoadResult<ModelData>> loader;

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
     * The {@link RpcProxy} used to retrieve the data for the
     * {@link PagingLoader}.
     */
    @SuppressWarnings("rawtypes")
    protected RpcProxy proxy;

    /** The state ID to use for this panel. */
    protected String panelStateId;

    /** The {@link ToolBar} that will be shown on the bottom of the panel. */
    protected PagingToolBar bottomToolbar;

    /** The maximum number of records that will be displayed in the paging grid. */
    protected int maxNumberOfRecords;

    /**
     * Default constructor.
     */
    public AbstractGridPanel() {
        setBodyBorder(false);
        setHeading("Records");
        setButtonAlign(HorizontalAlignment.CENTER);
        setLayout(new FitLayout());

        maxNumberOfRecords = 50;

        menuItems = new ArrayList<MenuItem>();
        menuBarButtons = new ArrayList<Button>();
    }

    /**
     * Creates all widgets on the {@link ContentPanel}.
     */
    protected void initializeWidgets() {
        grid = createTable();

        if (!menuBarButtons.isEmpty()) {
            final ToolBar topToolbar = createTopToolbar();
            setTopComponent(topToolbar);
        }

        bottomToolbar = createBottomToolbar();
        setBottomComponent(bottomToolbar);

        add(grid);
    }

    /**
     * Creates the {@link ToolBar} that will be placed on the top of the
     * {@link Grid}.
     * 
     * @return Returns a {@link ToolBar} with its components.
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
     * Refreshes the data in the grid by using the paging toolbar refresh
     * method.
     */
    public void refresh() {
        this.bottomToolbar.refresh();
    }

    /**
     * Creates the {@link ToolBar} that will be placed on the bottom of the
     * {@link Grid}.
     * 
     * @return Returns a {@link ToolBar} with its components.
     */
    protected PagingToolBar createBottomToolbar() {
        /*
         * Setup the toolbar.
         */
        final PagingToolBar toolBar = new PagingToolBar(maxNumberOfRecords);
        toolBar.bind(loader);

        return toolBar;
    }

    /**
     * Create the columns of the {@link Grid}.
     * 
     * @return Returns a {@link List} containing {@link ColumnConfig} objects
     *         representing the configuration of the columns.
     */
    protected abstract List<ColumnConfig> createColumns();

    /**
     * Creates the table on a {@link ContentPanel}.
     * 
     * @return Returns the {@link ContentPanel} containing the grid.
     */
    protected Grid<BeanModel> createTable() {
        final List<ColumnConfig> configs = createColumns();
        final ColumnModel cm = new ColumnModel(configs);

        loader = new BasePagingLoader<PagingLoadResult<ModelData>>(proxy, new BeanModelReader());
        loader.setRemoteSort(true);

        final ListStore<BeanModel> store = new ListStore<BeanModel>(loader);

        final Grid<BeanModel> grid = new Grid<BeanModel>(store, cm);
        grid.setStyleAttribute("borderTop", "none");
        grid.getView().setForceFit(true);
        grid.setBorders(true);
        grid.setLoadMask(true);
        grid.setStateId(panelStateId);
        grid.setStateful(true);
        grid.addListener(Events.CellDoubleClick, new Listener<GridEvent<BeanModel>>() {

            public void handleEvent(final GridEvent<BeanModel> be) {
                handleDoubleClick();
            }

        });

        if (!menuItems.isEmpty()) {
            final Menu menu = createContextMenu();
            grid.setContextMenu(menu);
        }

        grid.addListener(Events.Attach, new Listener<GridEvent<BeanModel>>() {

            public void handleEvent(final GridEvent<BeanModel> be) {
                final PagingLoadConfig config = new BasePagingLoadConfig();
                config.setOffset(0);
                config.setLimit(50);

                final Map<String, Object> state = grid.getState();
                if (state.containsKey("offset")) {
                    final int offset = (Integer) state.get("offset");
                    final int limit = (Integer) state.get("limit");
                    config.setOffset(offset);
                    config.setLimit(limit);
                }
                if (state.containsKey("sortField")) {
                    config.setSortField((String) state.get("sortField"));
                    config.setSortDir(SortDir.valueOf((String) state.get("sortDir")));
                }
                loader.load(config);
            }
        });

        return grid;
    }

    /**
     * Handle the double click event.
     */
    protected void handleDoubleClick() {
        /*
         * By default nothing happens on a double click.
         */
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

}
