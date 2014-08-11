/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 1 okt. 2011 File: ReleaseHistoryTable.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.view.dashboard.release.history
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
package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.release.history;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.DeployStatus;
import nl.tranquilizedquality.adm.commons.business.domain.Release;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractGridPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.controller.navigation.AdmNavigationController;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientReleaseExecution;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientReleaseExecutionSearchCommand;
import nl.tranquilizedquality.adm.gwt.gui.client.service.release.ReleaseServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.AdmTabs;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Table where the release history is displayed in.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 1 okt. 2011
 */
public class ReleaseHistoryTable extends AbstractGridPanel {

    /** The icons of the application. */
    private final AdmIcons icons;

    /** The release where the release history needs to be displayed for. */
    private final ClientReleaseExecutionSearchCommand sc;

    /**
     * Default constructor.
     */
    public ReleaseHistoryTable() {
        setHeading("Release History");

        this.sc = new ClientReleaseExecutionSearchCommand();
        this.icons = Registry.get(AdmModule.ICONS);

        setIcon(AbstractImagePrototype.create(icons.releaseHistory()));

        /*
         * Initialize the widgets.
         */
        initializeWidgets();
    }

    @Override
    protected List<ColumnConfig> createColumns() {
        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        ColumnConfig column = new ColumnConfig();
        column.setId("releaseStatus");
        column.setHeader("Status");
        column.setWidth(80);
        column.setSortable(true);
        column.setRenderer(new GridCellRenderer<ModelData>() {

            @Override
            public Object render(final ModelData model, final String property, final ColumnData config, final int rowIndex,
                    final int colIndex, final ListStore<ModelData> store,
                    final Grid<ModelData> grid) {
                final Object value = model.get(property);

                String style = "black";

                if (value != null) {
                    GWT.log(value.toString());
                    GWT.log(value.getClass().getName());
                    final DeployStatus status = (DeployStatus) value;

                    switch (status) {
                        case FAILED:
                            style = "red";
                            break;

                        case SUCCESS:
                            style = "green";
                            break;

                        case ONGOING:
                            style = "orange";
                            break;

                    }
                }

                return "<span style='font-weight:bold; color:" + style + "'>" + String.valueOf(value) + "</span>";
            }
        });
        configs.add(column);

        column = new ColumnConfig();
        column.setId("releaseDate");
        column.setHeader("Date");
        column.setWidth(100);
        column.setSortable(true);
        column.setDateTimeFormat(DateTimeFormat.getShortDateTimeFormat());
        configs.add(column);

        column = new ColumnConfig();
        column.setId("environmentName");
        column.setHeader("Environment");
        column.setWidth(100);
        column.setSortable(false);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("createdBy");
        column.setHeader("Deployer");
        column.setWidth(100);
        column.setSortable(true);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("numberOfArtifacts");
        column.setHeader("Artifacts");
        column.setWidth(60);
        column.setSortable(false);
        configs.add(column);

        return configs;
    }

    /**
     * Retrieves the selected item and displays it in the release history
     * screen.
     */
    private void view() {
        final GridSelectionModel<BeanModel> selectionModel = grid.getSelectionModel();
        final BeanModel selectedItem = selectionModel.getSelectedItem();

        if (selectedItem != null) {
            final ClientReleaseExecution releaseHistory = selectedItem.getBean();
            final AdmNavigationController controller = Registry.get(AdmModule.NAVIGATION_CONTROLLER);
            controller.selectTab(AdmTabs.RELEASE_HISTORY_TAB, releaseHistory);
        }
    }

    @Override
    protected void handleDoubleClick() {
        view();
    }

    @Override
    protected void initializeWidgets() {
        final ReleaseServiceAsync releaseService = Registry.get(AdmModule.RELEASE_SERVICE);

        proxy = new RpcProxy<PagingLoadResult<ClientReleaseExecution>>() {

            @Override
            public void load(final Object loadConfig, final AsyncCallback<PagingLoadResult<ClientReleaseExecution>> callback) {
                releaseService.findReleaseHistory((PagingLoadConfig) loadConfig, ReleaseHistoryTable.this.sc, callback);
            }
        };

        super.initializeWidgets();
    }

    public void setRelease(final Release release) {
        sc.setRelease(release);

        refresh();
    }

}
