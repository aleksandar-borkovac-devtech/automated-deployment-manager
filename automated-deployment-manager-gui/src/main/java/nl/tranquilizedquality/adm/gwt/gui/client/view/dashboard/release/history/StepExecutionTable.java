/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 20 okt. 2011 File: StepExecutionTable.java
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
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseStepExecution;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractRelationListTable;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientReleaseStepExecution;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Table where the steps that were done in a release are displayed.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 20 okt. 2011
 */
public class StepExecutionTable extends AbstractRelationListTable<ReleaseStepExecution, ClientReleaseStepExecution> {

    private StepExecutionDetailsWindow window;

    /** The icons of the application. */
    private final AdmIcons icons;

    /**
     * Default constructor
     */
    public StepExecutionTable() {
        setHeading("Steps");

        this.icons = Registry.get(AdmModule.ICONS);

        setIcon(AbstractImagePrototype.create(icons.steps()));

        /*
         * Initializes the widgets.
         */
        initializeWidgets();
    }

    @Override
    protected String getPanelStateId() {
        return StepExecutionTable.class.getName();
    }

    @Override
    protected Class<ClientReleaseStepExecution> getBeanModelClass() {
        return ClientReleaseStepExecution.class;
    }

    @Override
    protected List<ColumnConfig> createColumns() {
        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        ColumnConfig column = new ColumnConfig();
        column.setId("name");
        column.setHeader("Step");
        column.setWidth(100);
        column.setSortable(false);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("executionDate");
        column.setHeader("Date");
        column.setWidth(50);
        column.setSortable(false);
        column.setDateTimeFormat(DateTimeFormat.getShortDateTimeFormat());
        configs.add(column);

        column = new ColumnConfig();
        column.setId("status");
        column.setHeader("Status");
        column.setWidth(50);
        column.setSortable(false);
        column.setRenderer(new GridCellRenderer<ModelData>() {

            @Override
            public Object render(final ModelData model, final String property, final ColumnData config, final int rowIndex,
                    final int colIndex, final ListStore<ModelData> store, final Grid<ModelData> grid) {
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
        column.setId("errorMessage");
        column.setHeader("Message");
        column.setWidth(200);
        column.setSortable(false);
        configs.add(column);

        return configs;
    }

    @Override
    protected void handleDoubleClick(final GridEvent<BeanModel> gridEvent) {
        view();
    }

    /**
     * Retrieves the selected item and displays it in the release details
     * screen.
     */
    private void view() {
        final GridSelectionModel<BeanModel> selectionModel = grid.getSelectionModel();
        final BeanModel selectedItem = selectionModel.getSelectedItem();

        if (selectedItem != null) {
            final ClientReleaseStepExecution step = selectedItem.getBean();

            if (window == null) {
                window = new StepExecutionDetailsWindow();
            }
            window.setModel(step);
            window.show();
        }
    }

}
