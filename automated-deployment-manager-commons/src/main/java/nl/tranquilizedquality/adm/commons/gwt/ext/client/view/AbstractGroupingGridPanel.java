/**
 * <pre>
 * Project: automated-deployment-manager-commons Created on: 24 sep. 2011 File: AbstractGroupingGridPanel.java
 * Package: nl.tranquilizedquality.adm.commons.gwt.ext.client.view
 * 
 * Copyright (c) 2011 Tranquilized Quality www.Tranquilized Quality.nl All rights
 * reserved.
 * 
 * This software is the confidential and proprietary information of Tranquilized Quality
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Tranquilized Quality.
 * </pre>
 */
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
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridGroupRenderer;
import com.extjs.gxt.ui.client.widget.grid.GroupColumnData;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;

/**
 * Abstract base class for a {@link ContentPanel} that contains a paging grid
 * with menu bar, context menu and double click handling.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 24 sep. 2011
 */
public abstract class AbstractGroupingGridPanel extends AbstractGridPanel {

    /** Field to group on. */
    protected String groupingField;

    /**
     * Default constructor.
     */
    public AbstractGroupingGridPanel() {
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
    @Override
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
     * Creates the table on a {@link ContentPanel}.
     * 
     * @return Returns the {@link ContentPanel} containing the grid.
     */
    @Override
    protected Grid<BeanModel> createTable() {
        final List<ColumnConfig> configs = createColumns();
        final ColumnModel cm = new ColumnModel(configs);

        loader = new BasePagingLoader<PagingLoadResult<ModelData>>(proxy, new BeanModelReader());
        loader.setRemoteSort(true);

        final GroupingStore<BeanModel> store = new GroupingStore<BeanModel>(loader);
        store.groupBy(groupingField);

        final GroupingView view = new GroupingView();
        view.setShowGroupedColumn(false);
        view.setForceFit(true);
        view.setGroupRenderer(new GridGroupRenderer() {
            public String render(final GroupColumnData data) {
                final String f = cm.getColumnById(data.field).getHeader();
                final String l = data.models.size() == 1 ? "Item" : "Items";
                return f + ": " + data.group + " (" + data.models.size() + " " + l + ")";
            }
        });

        final Grid<BeanModel> grid = new Grid<BeanModel>(store, cm);
        grid.setView(view);
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

}
