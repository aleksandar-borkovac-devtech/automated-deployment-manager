package nl.tranquilizedquality.adm.commons.gwt.ext.client.view;

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
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
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
 * Abstract implementation of a {@link ContentPanel} where all management panels
 * should extend from. This {@link ContentPanel} contains of a grid with a top
 * and bottom toolbar already in place. Only the actual GUI logic needs to be
 * implemented and the record fields that will be shown.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 */
public abstract class AbstractManagementPanel extends ContentPanel {

    /** The {@link Grid} containing all the records. */
    protected Grid<BeanModel> grid;

    /** The {@link PagingLoader} used to load data per page. */
    protected PagingLoader<PagingLoadResult<ModelData>> loader;

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

    /** Add button that will be placed on the toolbar. */
    protected Button addButton;

    /** Edit button that will be placed on the toolbar. */
    protected Button editButton;

    /** Delete button that will be placed on the toolbar. */
    protected Button deleteButton;

    /** Remove {@link MenuItem} that will be placed in the context menu. */
    protected MenuItem removeMenuItem;

    /** Edit {@link MenuItem} that will be placed in the context menu. */
    protected MenuItem editMenuItem;

    /**
     * Default constructor.
     */
    public AbstractManagementPanel() {
        setBodyBorder(false);
        setHeading("Records");
        setButtonAlign(HorizontalAlignment.CENTER);
        setLayout(new FitLayout());

        initializeWidgets();
    }

    /**
     * Creates all widgets on the {@link ContentPanel}.
     */
    protected void initializeWidgets() {
        grid = createTable();
        final ToolBar topToolbar = createTopToolbar();
        bottomToolbar = createBottomToolbar();

        setTopComponent(topToolbar);
        setBottomComponent(bottomToolbar);
        add(grid);
    }

    /**
     * Creates the {@link ToolBar} that will be placed on the top of the
     * {@link Grid}.
     * 
     * @return Returns a {@link ToolBar} with its components.
     */
    private ToolBar createTopToolbar() {
        /*
         * Create the toolbar itself.
         */
        final ToolBar toolBar = new ToolBar();

        addButton = new Button("Add new");
        toolBar.add(addButton);
        toolBar.add(new SeparatorToolItem());

        addButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                add();
            }

        });

        editButton = new Button("Edit selected");
        toolBar.add(editButton);
        toolBar.add(new SeparatorToolItem());

        editButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                edit();
            }

        });

        deleteButton = new Button("Delete selected");
        toolBar.add(deleteButton);
        toolBar.add(new SeparatorToolItem());

        deleteButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                deleteSelected();
            }

        });

        return toolBar;
    }

    /**
     * Logic that will be executed on an add action.
     */
    protected abstract void add();

    /**
     * Logic that will be executed on an edit action.
     */
    protected abstract void edit();

    /**
     * Logic that will be executed on a delete action.
     */
    protected abstract void deleteSelected();

    /**
     * Refreshes the data in the grid by using the paging toolbar refresh
     * method.
     */
    protected void refresh() {
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
        final PagingToolBar toolBar = new PagingToolBar(50);
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
        final Menu menu = createContextMenu();

        loader = new BasePagingLoader<PagingLoadResult<ModelData>>(proxy, new BeanModelReader());
        loader.setRemoteSort(true);

        final ListStore<BeanModel> store = new ListStore<BeanModel>(loader);

        final Grid<BeanModel> grid = new Grid<BeanModel>(store, cm);
        grid.setStyleAttribute("borderTop", "none");
        grid.setBorders(true);
        grid.setContextMenu(menu);
        grid.setLoadMask(true);
        grid.setStateId(panelStateId);
        grid.setStateful(true);
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
     * Creates a context menu.
     * 
     * @return Returns a {@link Menu}.
     */
    protected Menu createContextMenu() {
        final Menu menu = new Menu();

        editMenuItem = new MenuItem();
        editMenuItem.setText("Edit");
        editMenuItem.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(final MenuEvent ce) {
                edit();
            }
        });
        menu.add(editMenuItem);

        removeMenuItem = new MenuItem();
        removeMenuItem.setText("Delete");
        removeMenuItem.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(final MenuEvent ce) {
                deleteSelected();
            }
        });
        menu.add(removeMenuItem);

        return menu;
    }
}
