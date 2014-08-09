/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 20 okt. 2011 File: SnapshotArtifactsTable.java
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

import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifactSnapshot;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecution;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecutionLog;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractRelationListTable;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.controller.navigation.AdmNavigationController;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientMavenArtifactSnapshot;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientReleaseExecution;
import nl.tranquilizedquality.adm.gwt.gui.client.service.release.ReleaseServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.AdmTabs;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Table where the snapshot artifacts are displayed in.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 20 okt. 2011
 */
public class SnapshotArtifactsTable extends AbstractRelationListTable<MavenArtifactSnapshot, ClientMavenArtifactSnapshot> {

    /** The icons of the application. */
    private final AdmIcons icons;

    /**
     * Default constructor.
     */
    public SnapshotArtifactsTable() {
        setHeading("Artifacts");

        this.icons = Registry.get(AdmModule.ICONS);

        setIcon(AbstractImagePrototype.create(icons.artifacts()));

        /*
         * Initialize the widgets.
         */
        initializeWidgets();
    }

    @Override
    protected String getPanelStateId() {
        return SnapshotArtifactsTable.class.getName();
    }

    @Override
    protected Class<ClientMavenArtifactSnapshot> getBeanModelClass() {
        return ClientMavenArtifactSnapshot.class;
    }

    @Override
    protected List<ColumnConfig> createColumns() {
        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        ColumnConfig column = new ColumnConfig();
        column.setId("name");
        column.setHeader("Name");
        column.setWidth(100);
        column.setSortable(false);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("group");
        column.setHeader("Group");
        column.setWidth(100);
        column.setSortable(false);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("artifactId");
        column.setHeader("Artifact Id");
        column.setWidth(60);
        column.setSortable(false);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("type");
        column.setHeader("Type");
        column.setWidth(100);
        column.setSortable(false);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("version");
        column.setHeader("Version");
        column.setWidth(100);
        column.setSortable(false);
        configs.add(column);

        column = new ColumnConfig();
        column.setHeader("Logs");
        column.setWidth(100);
        column.setSortable(false);
        column.setRenderer(new GridCellRenderer<ModelData>() {

            @Override
            public Object render(final ModelData model, final String property, final ColumnData config, final int rowIndex,
                    final int colIndex, final ListStore<ModelData> store, final Grid<ModelData> grid) {
                final Button showLogsButton = new Button("Show logs", new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(final ButtonEvent ce) {
                        final GridSelectionModel<ModelData> selectionModel = grid.getSelectionModel();
                        selectionModel.select(rowIndex, false);

                        handleDoubleClick(null);
                    }
                });
                showLogsButton.setWidth(grid.getColumnModel().getColumnWidth(colIndex) - 12);
                showLogsButton.setToolTip("Shows the detailed logs for the artifact.");
                showLogsButton.setIcon(AbstractImagePrototype.create(icons.showLogs()));

                return showLogsButton;
            }
        });
        configs.add(column);

        return configs;
    }

    @Override
    protected void handleDoubleClick(final GridEvent<BeanModel> gridEvent) {
        final GridSelectionModel<BeanModel> selectionModel = grid.getSelectionModel();
        final BeanModel selectedItem = selectionModel.getSelectedItem();
        final ClientMavenArtifactSnapshot snapshot = selectedItem.getBean();
        final ReleaseExecution releaseExecution = snapshot.getReleaseExecution();
        final Long releaseExecutionId = releaseExecution.getId();

        final ReleaseServiceAsync releaseService = Registry.get(AdmModule.RELEASE_SERVICE);
        releaseService.findReleaseExecutionById(releaseExecutionId, new AsyncCallback<ClientReleaseExecution>() {

            @Override
            public void onSuccess(final ClientReleaseExecution releaseExecution) {
                final List<ReleaseExecutionLog> logs = releaseExecution.getLogs();
                GWT.log("Retrieve logs [" + logs.size() + "]");

                if (logs.isEmpty()) {
                    final MessageBox box = new MessageBox();
                    box.setIcon(MessageBox.INFO);
                    box.setTitle("Logs");
                    box.setMessage("There are no detailed logs available for this artifact");
                    box.setButtons(MessageBox.OK);
                    box.show();
                } else {
                    for (final ReleaseExecutionLog releaseExecutionLog : logs) {
                        final String snapshotName = snapshot.getName();

                        final MavenArtifactSnapshot mavenArtifact = releaseExecutionLog.getMavenArtifact();
                        final String artifactName = mavenArtifact.getName();

                        GWT.log("Validating artifact name: " + artifactName + " == " + snapshotName);

                        if (artifactName.equals(snapshotName)) {
                            final AdmNavigationController navigationController = Registry.get(AdmModule.NAVIGATION_CONTROLLER);
                            navigationController.selectTab(AdmTabs.RELEASE_EXECUTION_LOG_DETAILS_TAB, releaseExecutionLog);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(final Throwable throwable) {
                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.ERROR);
                box.setTitle("Retrieve release execution logs.");
                box.setMessage(throwable.getMessage());
                box.setButtons(MessageBox.OK);
                box.show();
            }
        });
    }
}
