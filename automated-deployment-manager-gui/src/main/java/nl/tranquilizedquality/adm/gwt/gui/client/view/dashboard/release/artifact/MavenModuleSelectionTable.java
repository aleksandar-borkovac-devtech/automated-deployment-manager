/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 3 okt. 2011 File: MavenModuleSelectionTable.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.view.dashboard.release.artifact
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
package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.release.artifact;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractGroupingGridPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.artifact.ClientMavenModule;
import nl.tranquilizedquality.adm.gwt.gui.client.model.artifact.ClientMavenModuleSearchCommand;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientRelease;
import nl.tranquilizedquality.adm.gwt.gui.client.service.artifact.ArtifactServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.view.AdmViewPort;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.repository.RepositoryTable;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.RowNumberer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Table that displays the available maven modules.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 okt. 2011
 */
public class MavenModuleSelectionTable extends AbstractGroupingGridPanel {

    /** The icons of the application. */
    private final AdmIcons icons;

    /** The search criteria. */
    private final ClientMavenModuleSearchCommand sc;

    /** The repository service. */
    private ArtifactServiceAsync artifactService;

    /** The select button. */
    private Button selectButton;

    /** Window where this panel is displayed in. */
    private final Window window;

    /** Window that holds the details panel. */
    private ArtifactDetailsWindow detailsWindow;

    /** The release that where an artifact is being added. */
    private ClientRelease release;

    /**
     * Constructor that takes the search criteria to filter on.
     * 
     * @param sc
     *        The search criteria.
     */
    public MavenModuleSelectionTable(final ClientMavenModuleSearchCommand sc, final Window window) {
        setHeading("Maven Modules");
        setId("maven-module-selection-table");

        this.icons = Registry.get(AdmModule.ICONS);
        this.window = window;
        this.sc = sc;
        this.groupingField = "group";

        initializeWidgets();
    }

    @Override
    protected void initializeWidgets() {
        /*
         * Add select button.
         */
        selectButton = new Button("Select");
        selectButton.setId("maven-module-select-btn");
        selectButton.setIcon(AbstractImagePrototype.create(icons.add()));

        final SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                selectModule();
            }

        };
        selectButton.addSelectionListener(listener);

        this.menuBarButtons.add(selectButton);

        artifactService = Registry.get(AdmModule.ARTIFACT_SERVICE);

        /*
         * Add proxy loader.
         */
        proxy = new RpcProxy<PagingLoadResult<ClientMavenModule>>() {

            @Override
            public void load(final Object loadConfig, final AsyncCallback<PagingLoadResult<ClientMavenModule>> callback) {
                artifactService.findMavenModules((PagingLoadConfig) loadConfig, MavenModuleSelectionTable.this.sc, callback);
            }
        };

        this.panelStateId = RepositoryTable.class.getName();

        /*
         * Initialize widgets.
         */
        super.initializeWidgets();
    }

    /**
     * Selects the currently selected module and opens a window where the Maven
     * artifact can be configured.
     */
    private void selectModule() {
        final GridSelectionModel<BeanModel> selectionModel = grid.getSelectionModel();
        final BeanModel selectedItem = selectionModel.getSelectedItem();

        if (selectedItem != null) {
            final ClientMavenModule module = selectedItem.getBean();

            if (detailsWindow == null) {
                detailsWindow = new ArtifactDetailsWindow(module, release);
            } else {
                detailsWindow.setUpArtifact(module, release);
            }

            window.hide();
            detailsWindow.show();

            final AdmViewPort viewPort = Registry.get(AdmModule.VIEW_PORT);
            viewPort.mask();
        }

    }

    /**
     * Refreshes the content of the table.
     */
    public void refreshTable() {
        super.refresh();
    }

    @Override
    protected List<ColumnConfig> createColumns() {
        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        configs.add(new RowNumberer());

        ColumnConfig column = new ColumnConfig();
        column.setId("name");
        column.setHeader("Module Name");
        column.setWidth(100);
        column.setSortable(true);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("group");
        column.setHeader("Group");
        column.setWidth(100);
        column.setSortable(true);
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
        column.setSortable(true);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("version");
        column.setHeader("Version");
        column.setWidth(100);
        column.setSortable(true);
        configs.add(column);

        return configs;
    }

    @Override
    protected void handleDoubleClick() {
        selectModule();
    }

    /**
     * Sets the release that is currently being edited.
     * 
     * @param release
     *        The release that will be set.
     */
    public void setRelease(final ClientRelease release) {
        this.release = release;
    }

}
