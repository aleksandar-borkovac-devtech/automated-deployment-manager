/*
 * @(#)DependencyTable.java 8 feb. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.module.dependency;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.MavenModule;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.security.AuthorizationServiceAsync;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractRelationListTable;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.controller.navigation.AdmNavigationController;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.artifact.ClientMavenModule;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.AdmTabs;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.module.MavenModuleDetailsPanel;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Table where the current dependencies are displayed in.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 8 feb. 2013
 */
public class DependenciesTable extends AbstractRelationListTable<MavenModule, ClientMavenModule> {

    /** The icons of the application. */
    private final AdmIcons icons;

    /** Button to remove the selected destinations from the {@link MavenModule}. */
    private Button removeButton;

    /** Details panel where the {@link MavenModule} is displayed in. */
    private final MavenModuleDetailsPanel detailsPanel;

    /**
     * Constructor taking the panel where this table is displayed on.
     * 
     * @param detailsPanel
     *        The details panel where this panel is displayed on.
     */
    public DependenciesTable(final MavenModuleDetailsPanel detailsPanel) {
        setHeading("Dependencies");
        setId("maven-module-dependencies-tbl");

        this.icons = Registry.get(AdmModule.ICONS);
        this.detailsPanel = detailsPanel;

        setIcon(AbstractImagePrototype.create(icons.findArtifacts()));

        initializeWidgets();

        performPrivilegeCheck();
    }

    /**
     * Validates if the user has the proper privileges.
     */
    private void performPrivilegeCheck() {
        final AuthorizationServiceAsync authorizationService = Registry.get(AdmModule.AUTHORIZATION_SERVICE);
        authorizationService.isLoggedInUserAuthorized("ADD_DESTINATION", new AsyncCallback<Boolean>() {

            @Override
            public void onFailure(final Throwable throwable) {
                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.ERROR);
                box.setTitle("Add destination check.");
                box.setMessage(throwable.getMessage());
                box.setButtons(MessageBox.OK);
                box.show();
            }

            @Override
            public void onSuccess(final Boolean authorized) {
                if (authorized) {
                    removeButton.enable();
                } else {
                    removeButton.disable();
                }
            }

        });
    }

    @Override
    protected void initializeWidgets() {

        removeButton = new Button("Remove Dependency");
        removeButton.setId("remove-depdendency-btn");
        removeButton.setIcon(AbstractImagePrototype.create(icons.delete()));
        final SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                final ClientMavenModule module = detailsPanel.getModel();
                final List<MavenModule> dependencies = module.getDeploymentDependencies();

                final GridSelectionModel<BeanModel> selectionModel = grid.getSelectionModel();
                final List<BeanModel> selectedItems = selectionModel.getSelectedItems();
                if (!selectedItems.isEmpty()) {
                    for (final BeanModel beanModel : selectedItems) {
                        final ClientMavenModule moduleToRemove = beanModel.getBean();
                        dependencies.remove(moduleToRemove);
                    }

                    detailsPanel.saveMavenModule();
                }
            }
        };
        removeButton.addSelectionListener(listener);

        menuBarButtons.add(removeButton);

        super.initializeWidgets();
    }

    @Override
    protected String getPanelStateId() {
        return DependenciesTable.class.getName();
    }

    @Override
    protected Class<ClientMavenModule> getBeanModelClass() {
        return ClientMavenModule.class;
    }

    @Override
    protected List<ColumnConfig> createColumns() {
        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        ColumnConfig column = new ColumnConfig();
        column.setId("group");
        column.setHeader("Group");
        column.setWidth(100);
        column.setSortable(true);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("artifactId");
        column.setHeader("Artifact Id");
        column.setWidth(100);
        column.setSortable(true);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("name");
        column.setHeader("Name");
        column.setWidth(100);
        column.setSortable(true);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("type");
        column.setHeader("Type");
        column.setWidth(100);
        column.setSortable(false);
        configs.add(column);

        return configs;
    }

    /**
     * Retrieves the selected item and displays it in the maven module details
     * screen.
     */
    private void view() {
        final GridSelectionModel<BeanModel> selectionModel = grid.getSelectionModel();
        final BeanModel selectedItem = selectionModel.getSelectedItem();

        if (selectedItem != null) {
            final ClientMavenModule mavenModule = selectedItem.getBean();
            final AdmNavigationController controller = Registry.get(AdmModule.NAVIGATION_CONTROLLER);
            controller.selectTab(AdmTabs.MAVEN_MODULE_DETAILS_TAB, mavenModule);
        }
    }

    @Override
    protected void handleDoubleClick(final GridEvent<BeanModel> gridEvent) {
        view();
    }

}
