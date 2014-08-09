/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 27 sep. 2011 File: DestinationTable.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.view.dashboard.artifact.destination
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
package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.module.destination;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.MavenModule;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.security.AuthorizationServiceAsync;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractRelationListTable;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.controller.navigation.AdmNavigationController;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.artifact.ClientMavenModule;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientDestination;
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
 * Table where the destinations of a {@link MavenArtifact} are displayed in.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 29 sep. 2011
 */
public class DestinationTable extends AbstractRelationListTable<Destination, ClientDestination> {

    /** The icons of the application. */
    private final AdmIcons icons;

    /** Button to remove the selected destinations from the {@link MavenModule}. */
    private Button removeButton;

    /** Details panel where the {@link MavenModule} is displayed in. */
    private final MavenModuleDetailsPanel detailsPanel;

    /**
     * Constructor taking the details panel where this table is displayed on.
     * 
     * @param detailsPanel
     *        The details panel where this table is displayed on.
     */
    public DestinationTable(final MavenModuleDetailsPanel detailsPanel) {
        setHeading("Destinations");
        setId("maven-module-destinations-tbl");

        this.icons = Registry.get(AdmModule.ICONS);
        this.detailsPanel = detailsPanel;

        setIcon(AbstractImagePrototype.create(icons.destination()));

        /*
         * Initialize the widgets.
         */
        initializeWidgets();

        performPrivilegeCheck();
    }

    @Override
    protected void initializeWidgets() {

        removeButton = new Button("Remove Destination");
        removeButton.setId("remove-destination-btn");
        removeButton.setIcon(AbstractImagePrototype.create(icons.delete()));
        final SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                final ClientMavenModule module = detailsPanel.getModel();
                final List<Destination> destinations = module.getDestinations();

                final GridSelectionModel<BeanModel> selectionModel = grid.getSelectionModel();
                final List<BeanModel> selectedItems = selectionModel.getSelectedItems();
                if (!selectedItems.isEmpty()) {
                    for (final BeanModel beanModel : selectedItems) {
                        final ClientDestination destination = beanModel.getBean();
                        destinations.remove(destination);
                    }

                    detailsPanel.saveMavenModule();
                }
            }
        };
        removeButton.addSelectionListener(listener);

        menuBarButtons.add(removeButton);

        super.initializeWidgets();
    }

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
    protected String getPanelStateId() {
        return DestinationTable.class.getName();
    }

    @Override
    protected Class<ClientDestination> getBeanModelClass() {
        return ClientDestination.class;
    }

    @Override
    protected List<ColumnConfig> createColumns() {
        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        ColumnConfig column = new ColumnConfig();
        column.setId("name");
        column.setHeader("Name");
        column.setWidth(100);
        column.setSortable(true);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("hostName");
        column.setHeader("Host");
        column.setWidth(100);
        column.setSortable(true);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("protocol");
        column.setHeader("Protocol");
        column.setWidth(100);
        column.setSortable(true);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("environmentName");
        column.setHeader("Environment");
        column.setWidth(100);
        column.setSortable(false);
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
            final ClientDestination destination = selectedItem.getBean();
            final AdmNavigationController controller = Registry.get(AdmModule.NAVIGATION_CONTROLLER);
            controller.selectTab(AdmTabs.DESTINATION_DETAILS_TAB, destination);
        }
    }

    @Override
    protected void handleDoubleClick(final GridEvent<BeanModel> gridEvent) {
        view();
    }

}
