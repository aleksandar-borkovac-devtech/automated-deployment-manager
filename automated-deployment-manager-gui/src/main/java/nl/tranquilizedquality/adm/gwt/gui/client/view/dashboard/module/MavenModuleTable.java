/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 29 sep. 2011 File: MavenModuleTable.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.view.dashboard.module
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
package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.module;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.security.AuthorizationServiceAsync;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractGroupingGridPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.controller.navigation.AdmNavigationController;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.artifact.ClientMavenArtifact;
import nl.tranquilizedquality.adm.gwt.gui.client.model.artifact.ClientMavenModule;
import nl.tranquilizedquality.adm.gwt.gui.client.model.artifact.ClientMavenModuleSearchCommand;
import nl.tranquilizedquality.adm.gwt.gui.client.service.artifact.ArtifactServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.AdmTabs;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.repository.RepositoryTable;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.RowNumberer;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Table where all maven modules are displayed in.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 29 sep. 2011
 */
public class MavenModuleTable extends AbstractGroupingGridPanel {

    /** The icons of the application. */
    private final AdmIcons icons;

    /** The search criteria. */
    private final ClientMavenModuleSearchCommand sc;

    /** The repository service. */
    private ArtifactServiceAsync artifactService;

    /** The add button. */
    private Button addButton;

    /** The edit menu item. */
    private MenuItem editMenuItem;

    /**
     * Constructor that takes the search criteria to filter on.
     * 
     * @param sc
     *        The search criteria.
     */
    public MavenModuleTable(final ClientMavenModuleSearchCommand sc) {
        setHeading("Maven Modules");
        setId("maven-module-search-result-table");

        this.sc = sc;
        this.groupingField = "group";

        this.icons = Registry.get(AdmModule.ICONS);
        setIcon(AbstractImagePrototype.create(icons.artifacts()));

        initializeWidgets();

        performPrivilegeCheck();
    }

    private void performPrivilegeCheck() {
        final AuthorizationServiceAsync authorizationService = Registry.get(AdmModule.AUTHORIZATION_SERVICE);
        final AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

            @Override
            public void onFailure(final Throwable throwable) {
                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.ERROR);
                box.setTitle("Create maven module check.");
                box.setMessage(throwable.getMessage());
                box.setButtons(MessageBox.OK);
                box.show();
            }

            @Override
            public void onSuccess(final Boolean authorized) {
                if (authorized) {
                    addButton.enable();
                    editMenuItem.enable();
                } else {
                    addButton.disable();
                    editMenuItem.disable();
                }
            }

        };
        authorizationService.isLoggedInUserAuthorized("ADD_MAVEN_MODULE", callback);
    }

    @Override
    protected void initializeWidgets() {
        artifactService = Registry.get(AdmModule.ARTIFACT_SERVICE);

        proxy = new RpcProxy<PagingLoadResult<ClientMavenModule>>() {

            @Override
            public void load(final Object loadConfig, final AsyncCallback<PagingLoadResult<ClientMavenModule>> callback) {
                artifactService.findMavenModules((PagingLoadConfig) loadConfig, MavenModuleTable.this.sc, callback);
            }
        };

        this.panelStateId = RepositoryTable.class.getName();

        addButton = new Button("Add");
        addButton.setIcon(AbstractImagePrototype.create(icons.addMavenModule()));

        final SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                add();
            }

        };
        addButton.addSelectionListener(listener);

        this.menuBarButtons.add(addButton);

        editMenuItem = new MenuItem();
        editMenuItem.setIcon(AbstractImagePrototype.create(icons.edit()));
        editMenuItem.setText("Edit");
        editMenuItem.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(final MenuEvent ce) {
                view();
            }
        });
        menuItems.add(editMenuItem);

        super.initializeWidgets();
    }

    /**
     * Refreshes the content of the table.
     */
    public void refreshTable() {
        super.refresh();
    }

    /**
     * Creates a fresh {@link ClientMavenArtifact} and navigates to the details
     * panel.
     */
    private void add() {
        final ClientMavenArtifact artifact = new ClientMavenArtifact();

        final AdmNavigationController controller = Registry.get(AdmModule.NAVIGATION_CONTROLLER);
        controller.selectTab(AdmTabs.MAVEN_MODULE_DETAILS_TAB, artifact);
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
        column.setId("created");
        column.setHeader("Created");
        column.setWidth(100);
        column.setSortable(true);
        column.setDateTimeFormat(DateTimeFormat.getMediumDateTimeFormat());
        configs.add(column);

        column = new ColumnConfig();
        column.setId("createdBy");
        column.setHeader("Created By");
        column.setWidth(60);
        column.setSortable(false);
        column.setDateTimeFormat(DateTimeFormat.getShortDateTimeFormat());
        configs.add(column);

        column = new ColumnConfig();
        column.setId("altered");
        column.setHeader("Altered");
        column.setWidth(100);
        column.setSortable(true);
        column.setDateTimeFormat(DateTimeFormat.getMediumDateTimeFormat());
        configs.add(column);

        column = new ColumnConfig();
        column.setId("alteredBy");
        column.setHeader("Altered By");
        column.setWidth(60);
        column.setSortable(false);
        column.setDateTimeFormat(DateTimeFormat.getShortDateTimeFormat());
        configs.add(column);

        return configs;
    }

    /**
     * Retrieves the selected item and displays it in the repository details
     * screen.
     */
    private void view() {
        final GridSelectionModel<BeanModel> selectionModel = grid.getSelectionModel();
        final BeanModel selectedItem = selectionModel.getSelectedItem();

        if (selectedItem != null) {
            final ClientMavenModule module = selectedItem.getBean();

            final AdmNavigationController controller = Registry.get(AdmModule.NAVIGATION_CONTROLLER);
            controller.selectTab(AdmTabs.MAVEN_MODULE_DETAILS_TAB, module);
        }
    }

    @Override
    protected void handleDoubleClick() {
        view();
    }

}
