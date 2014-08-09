package nl.tranquilizedquality.adm.commons.gwt.ext.client.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
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
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;

/**
 * Abstract panel for managing relationship with given objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 feb. 2011
 * @param <T>
 *        The interface of the bean that is being displayed.
 * @param <C>
 *        The class of the bean that is being displayed.
 */
public abstract class AbstractRelationListTable<T, C> extends ContentPanel implements NavigationalItem<Collection<T>> {

    /** The {@link Grid} containing all the records. */
    protected Grid<BeanModel> grid;

    /** The state ID to use for this panel. */
    private String panelStateId;

    /** The model used in this grid */
    private Collection<T> model;

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
    public AbstractRelationListTable() {
        setBodyBorder(false);
        setHeading("Records");
        setButtonAlign(HorizontalAlignment.CENTER);
        setLayout(new FitLayout());

        menuItems = new ArrayList<MenuItem>();
        menuBarButtons = new ArrayList<Button>();
    }

    /**
     * Set the model for the panel.
     */
    @Override
    public void setModel(final Collection<T> model) {
        this.model = model;
        final ListStore<BeanModel> store = grid.getStore();

        store.removeAll();
        store.add(createBeanModel());
    }

    protected abstract String getPanelStateId();

    protected void initializeWidgets() {
        this.panelStateId = getPanelStateId();
        grid = createTable();

        if (!menuBarButtons.isEmpty()) {
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
     * Retrieve the Class of the {@link BeanModel} used.
     * 
     * @return the class of the model.
     */
    protected abstract Class<C> getBeanModelClass();

    /**
     * Creates the BeanModel used in this panel.
     * 
     * @return a list of {@link BeanModel} items.
     */
    protected List<BeanModel> createBeanModel() {
        final BeanModelFactory factory = BeanModelLookup.get().getFactory(getBeanModelClass());
        final List<BeanModel> beanModelList = new ArrayList<BeanModel>();

        for (final T modelItem : model) {
            beanModelList.add(factory.createModel(modelItem));
        }

        return beanModelList;
    }

    /**
     * Create the column configuration for the grid.
     * 
     * @return a list of column configurations.
     */
    protected abstract List<ColumnConfig> createColumns();

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
     * Creates the table on a {@link ContentPanel}.
     * 
     * @return Returns the grid.
     */
    protected Grid<BeanModel> createTable() {
        final List<ColumnConfig> configs = createColumns();
        final ColumnModel cm = new ColumnModel(configs);

        final ListStore<BeanModel> store = new ListStore<BeanModel>();

        final Grid<BeanModel> grid = new Grid<BeanModel>(store, cm);
        grid.setStyleAttribute("borderTop", "none");
        grid.getView().setForceFit(true);
        grid.setBorders(true);
        grid.setLoadMask(true);
        grid.setStateId(panelStateId);
        grid.setStateful(true);
        grid.addListener(Events.CellDoubleClick, new Listener<GridEvent<BeanModel>>() {

            @Override
            public void handleEvent(final GridEvent<BeanModel> gridEvent) {
                handleDoubleClick(gridEvent);
            }

        });

        if (!menuItems.isEmpty()) {
            final Menu menu = createContextMenu();
            grid.setContextMenu(menu);
        }

        return grid;
    }

    protected void handleDoubleClick(final GridEvent<BeanModel> gridEvent) {
        /*
         * By default nothing happens on a double click.
         */
    }
}
