/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 15 sep. 2011 File: DestinationLocationsTable.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.view.dashboard.environment
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
package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.environment.location;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameter;
import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.AbstractServiceException;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractRelationListTable;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientDeployerParameter;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientDestination;
import nl.tranquilizedquality.adm.gwt.gui.client.service.environment.EnvironmentServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.environment.DestinationDetailsPanel;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.dnd.DND.Feedback;
import com.extjs.gxt.ui.client.dnd.GridDragSource;
import com.extjs.gxt.ui.client.dnd.GridDropTarget;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.event.DNDListener;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Table where the deployer parameters are displayed in.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 15 sep. 2011
 */
public class DeployerParametersTable extends AbstractRelationListTable<DeployerParameter, ClientDeployerParameter> {

    /** The destination where the locations displayed in the list are part off. */
    private ClientDestination destination;

    /** The icons of the application. */
    private final AdmIcons icons;

    /** The details panel of a {@link Destination}. */
    private final DestinationDetailsPanel detailsPanel;

    /** Button where you can add a parameter. */
    private Button addButton;

    /** Button where you can delete a configured parameter. */
    private Button deleteButton;

    /** Window where the {@link DeployerParameter} will be managed in. */
    private DeployerParameterWindow window;

    /**
     * Constructor that takes the details panel where this table is displayed
     * on.
     * 
     * @param detailsPanel
     *        The parent panel.
     */
    public DeployerParametersTable(final DestinationDetailsPanel detailsPanel) {
        setHeading("Deployer Parameters");

        this.icons = Registry.get(AdmModule.ICONS);
        this.detailsPanel = detailsPanel;

        setIcon(AbstractImagePrototype.create(icons.destinationLocation()));

        /*
         * Initialize the widgets.
         */
        initializeWidgets();

    }

    @Override
    protected void initializeWidgets() {
        /*
         * Add add button.
         */
        addButton = new Button("Add");
        addButton.setId("add-deployer-parameter-btn");
        addButton.setIcon(AbstractImagePrototype.create(icons.add()));

        SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                if (destination != null && destination.isPersistent()) {
                    if (window == null) {
                        window = new DeployerParameterWindow(destination, DeployerParametersTable.this.detailsPanel);
                    } else {
                        window.setClientDestination(destination);
                    }

                    window.setModel(new ClientDeployerParameter());
                    window.setShim(true);
                    window.setShadow(true);
                    window.show();
                    window.refreshParameterTypes();

                    final Viewport viewport = Registry.get(AdmModule.VIEW_PORT);
                    viewport.mask();
                } else {
                    final String msg =
                            "Cannot add locations to a destination that isn't saved yet! Save the destination first before adding locations.";

                    final MessageBox box = new MessageBox();
                    box.setIcon(MessageBox.WARNING);
                    box.setTitle("Destination locations.");
                    box.setMessage(msg);
                    box.setButtons(MessageBox.OK);
                    box.show();
                }
            }

        };
        addButton.addSelectionListener(listener);

        this.menuBarButtons.add(addButton);

        /*
         * Delete add button.
         */
        deleteButton = new Button("Delete");
        deleteButton.setId("delete-deployer-parameter-btn");
        deleteButton.setIcon(AbstractImagePrototype.create(icons.delete()));

        listener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                deleteParameter();
            }

        };
        deleteButton.addSelectionListener(listener);

        this.menuBarButtons.add(deleteButton);

        /*
         * Add menu items.
         */
        final MenuItem editItem = new MenuItem("Edit location");
        editItem.setIcon(AbstractImagePrototype.create(icons.edit()));
        final SelectionListener<? extends MenuEvent> editListener = new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(final MenuEvent ce) {
                editParameter();
            }

        };
        editItem.addSelectionListener(editListener);
        menuItems.add(editItem);

        final MenuItem deleteItem = new MenuItem("Delete location");
        deleteItem.setIcon(AbstractImagePrototype.create(icons.delete()));
        final SelectionListener<? extends MenuEvent> deleteListener = new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(final MenuEvent ce) {
                deleteParameter();
            }

        };
        deleteItem.addSelectionListener(deleteListener);
        menuItems.add(deleteItem);

        super.initializeWidgets();

        new GridDragSource(grid);

        final GridDropTarget target = new GridDropTarget(grid);
        target.setAllowSelfAsSource(true);
        target.setFeedback(Feedback.INSERT);
        final DNDListener dndListener = new DNDListener() {

            @Override
            public void dragDrop(final DNDEvent e) {
                super.dragDrop(e);

                /*
                 * Reorder collection.
                 */
                final ListStore<BeanModel> store = grid.getStore();
                final List<BeanModel> models = store.getModels();
                final List<DeployerParameter> parameters = new ArrayList<DeployerParameter>();
                int i = 1;
                for (final BeanModel beanModel : models) {
                    /*
                     * Retrieve bean and update rank value.
                     */
                    final ClientDeployerParameter parameter = beanModel.getBean();
                    parameter.setRank(i);

                    /*
                     * Go to next rank.
                     */
                    i++;

                    parameters.add(parameter);

                    /*
                     * Notify store to update models in the GUI.
                     */
                    store.update(beanModel);
                }

                /*
                 * Save artifact.
                 */
                saveDestination(parameters);
            }

        };

        target.addDNDListener(dndListener);
    }

    /**
     * Saves the parameters ranking order.
     * 
     * @param parameters
     *        The parameters that will be updated.
     */
    private void saveDestination(final List<DeployerParameter> parameters) {
        destination.setDeployerParameters(parameters);

        final EnvironmentServiceAsync service = Registry.get(AdmModule.ENVIRONMENT_SERVICE);
        service.saveDestination(destination, new AsyncCallback<ClientDestination>() {

            @Override
            public void onSuccess(final ClientDestination destination) {

            }

            @Override
            public void onFailure(final Throwable throwable) {
                final StringBuilder builder = new StringBuilder();

                if (throwable instanceof AbstractServiceException) {
                    final AbstractServiceException ex = (AbstractServiceException) throwable;
                    final List<String> errors = ex.getErrors();

                    for (final String string : errors) {
                        builder.append(string);
                        builder.append("<br>");
                    }
                } else {
                    builder.append(throwable.getMessage());
                }

                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.ERROR);
                box.setTitle("Failed to save parameters.");
                box.setMessage(builder.toString());
                box.setButtons(MessageBox.OK);
                box.show();
            }
        });
    }

    /**
     * Edits the selected parameter in the grid by opning up the pop-up window.
     */
    private void editParameter() {
        final GridSelectionModel<BeanModel> selectionModel = grid.getSelectionModel();
        final BeanModel selectedItem = selectionModel.getSelectedItem();

        if (selectedItem != null) {
            final ClientDeployerParameter location = selectedItem.getBean();

            if (window == null) {
                window = new DeployerParameterWindow(destination, DeployerParametersTable.this.detailsPanel);
            } else {
                window.setClientDestination(destination);
            }

            window.setModel(location);
            window.setShim(true);
            window.setShadow(true);
            window.show();

            final Viewport viewport = Registry.get(AdmModule.VIEW_PORT);
            viewport.mask();
        }
    }

    /**
     * Deletes the selected parameter from the grid.
     */
    private void deleteParameter() {
        if (destination != null && destination.isPersistent()) {
            final GridSelectionModel<BeanModel> selectionModel = grid.getSelectionModel();
            final BeanModel selectedItem = selectionModel.getSelectedItem();

            if (selectedItem != null) {

                final ClientDeployerParameter location = selectedItem.getBean();

                final List<DeployerParameter> parameters = destination.getDeployerParameters();
                parameters.remove(location);

                /*
                 * Save the destination.
                 */
                final EnvironmentServiceAsync service = Registry.get(AdmModule.ENVIRONMENT_SERVICE);
                final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                    @Override
                    public void onFailure(final Throwable throwable) {
                        final StringBuilder builder = new StringBuilder();

                        if (throwable instanceof AbstractServiceException) {
                            final AbstractServiceException ex = (AbstractServiceException) throwable;
                            final List<String> errors = ex.getErrors();

                            for (final String string : errors) {
                                builder.append(string);
                                builder.append("<br>");
                            }
                        } else {
                            builder.append(throwable.getMessage());
                        }

                        final MessageBox box = new MessageBox();
                        box.setIcon(MessageBox.ERROR);
                        box.setTitle("Failed to delete location.");
                        box.setMessage(builder.toString());
                        box.setButtons(MessageBox.OK);
                        box.show();
                    }

                    @Override
                    public void onSuccess(final Void destination) {
                        final MessageBox box = new MessageBox();
                        box.setIcon(MessageBox.INFO);
                        box.setTitle("Delete location.");
                        box.setMessage("Successfully removed location!");
                        box.setButtons(MessageBox.OK);
                        box.show();
                    }

                };

                service.deleteDestinationLocation(location, callback);

                detailsPanel.setModel(destination);
            } else {
                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.WARNING);
                box.setTitle("Delete location.");
                box.setMessage("Select an item to be deleted!");
                box.setButtons(MessageBox.OK);
                box.show();
            }
        } else {
            final String msg =
                    "Cannot delete locations from a destination that isn't saved yet! Save the destination first before deleting locations.";

            final MessageBox box = new MessageBox();
            box.setIcon(MessageBox.WARNING);
            box.setTitle("Deployer Parameters.");
            box.setMessage(msg);
            box.setButtons(MessageBox.OK);
            box.show();
        }
    }

    @Override
    protected String getPanelStateId() {
        return DeployerParametersTable.class.getName();
    }

    @Override
    protected Class<ClientDeployerParameter> getBeanModelClass() {
        return ClientDeployerParameter.class;
    }

    @Override
    protected List<ColumnConfig> createColumns() {

        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        ColumnConfig column = new ColumnConfig();
        column.setId("rank");
        column.setHeader("Rank");
        column.setWidth(40);
        column.setSortable(true);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("value");
        column.setHeader("Value");
        column.setWidth(100);
        column.setSortable(true);
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
        column.setDateTimeFormat(DateTimeFormat.getShortDateTimeFormat());
        configs.add(column);

        return configs;
    }

    @Override
    protected void handleDoubleClick(final GridEvent<BeanModel> gridEvent) {
        editParameter();
    }

    /**
     * @param destination
     *        the destination to set
     */
    public void setDestination(final ClientDestination destination) {
        this.destination = destination;
    }

}
