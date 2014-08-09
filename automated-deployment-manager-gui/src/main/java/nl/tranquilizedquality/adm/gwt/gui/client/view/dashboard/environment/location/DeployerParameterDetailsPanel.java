/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 15 sep. 2011 File: DestinationLocationDetailsPanel.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.view.dashboard.environment.location
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

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameter;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameterTemplate;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameterType;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.converter.EnumConverter;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.converter.EnumWrapper;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.AbstractServiceException;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractDetailPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientDeployer;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientDeployerParameter;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientDestination;
import nl.tranquilizedquality.adm.gwt.gui.client.service.environment.EnvironmentServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.environment.DestinationDetailsPanel;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Details panel where you display deployer parameter details.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 15 sep. 2011
 */
public class DeployerParameterDetailsPanel extends AbstractDetailPanel<ClientDeployerParameter> {

    /** The icons of the application. */
    private final AdmIcons icons;

    /** The path of the location. */
    private TextField<String> value;

    /** Types you can select for this destination location. */
    private ComboBox<EnumWrapper<DeployerParameterType>> type;

    /** The destination where this location is part of. */
    private ClientDestination destination;

    /** The save button. */
    private Button saveButton;

    /** The window where this panel is displayed in. */
    private Window window;

    /**
     * Details panel.
     */
    public DeployerParameterDetailsPanel() {
        icons = Registry.get(AdmModule.ICONS);

        /*
         * Initialize the widgets.
         */
        initializeWidgets();

        /*
         * Add custom field bindings.
         */
        final FieldBinding binding = new FieldBinding(this.type, "type");
        binding.setConverter(new EnumConverter<DeployerParameterType>());
        fieldBindings.add(binding);
    }

    @Override
    protected void initializeWidgets() {
        setLayout(new FitLayout());

        formPanel = createDetailPanel();

        add(this.formPanel);

        refreshParameterTypes();
    }

    @Override
    public void setModel(final ClientDeployerParameter model) {
        this.model = model;

        if (this.model.isPersistent()) {
            final AsyncCallback<ClientDeployerParameter> callback = new AsyncCallback<ClientDeployerParameter>() {

                @Override
                public void onFailure(final Throwable throwable) {
                    final MessageBox box = new MessageBox();
                    box.setIcon(MessageBox.ERROR);
                    box.setTitle("Retrieve parameter.");
                    box.setMessage(throwable.getMessage());
                    box.setButtons(MessageBox.OK);
                    box.show();
                }

                @Override
                public void onSuccess(final ClientDeployerParameter parameter) {
                    DeployerParameterDetailsPanel.this.model = parameter;

                    bindModel(DeployerParameterDetailsPanel.this.model);
                }

            };

            final EnvironmentServiceAsync service = Registry.get(AdmModule.ENVIRONMENT_SERVICE);
            service.findLocationById(this.model.getId(), callback);
        } else {
            bindModel(this.model);
        }
    }

    @Override
    protected void performPrivilegeCheck() {

    }

    @Override
    protected FormPanel createDetailPanel() {
        final FormPanel formPanel = new FormPanel();
        formPanel.setHeaderVisible(false);
        formPanel.setFrame(false);
        formPanel.setBodyBorder(false);
        formPanel.setBorders(false);
        formPanel.setLabelWidth(110);
        formPanel.setIcon(AbstractImagePrototype.create(icons.destinationLocation()));
        formPanel.setButtonAlign(HorizontalAlignment.RIGHT);

        /*
         * Create field set for repository related information.
         */
        final FieldSet locationInfoFieldSet = new FieldSet();
        locationInfoFieldSet.setLayout(new FormLayout());
        locationInfoFieldSet.setCollapsible(true);
        locationInfoFieldSet.setAutoHeight(true);
        locationInfoFieldSet.setHeading("Deployer Parameter Information");

        /*
         * Add path
         */
        value = new TextField<String>();
        value.setId("deployer-parameter-details-pnl-value");
        value.setAllowBlank(false);
        value.setName("value");
        value.setFieldLabel("Value");
        locationInfoFieldSet.add(value);

        /*
         * Add type
         */
        type = new ComboBox<EnumWrapper<DeployerParameterType>>();
        type.setId("deployer-parameter-details-pnl-type");
        type.setName("type");
        type.setFieldLabel("Type");
        type.setDisplayField("value");
        type.setEmptyText("Select type..");
        type.setTriggerAction(TriggerAction.ALL);
        type.setForceSelection(true);
        type.setEditable(false);

        /*
         * Add enums.
         */
        final ListStore<EnumWrapper<DeployerParameterType>> store = new ListStore<EnumWrapper<DeployerParameterType>>();
        type.setStore(store);
        locationInfoFieldSet.add(type);

        formPanel.add(locationInfoFieldSet);

        /*
         * Add save button.
         */
        saveButton = new Button();
        saveButton.setId("save-deployer-parameter-btn");
        saveButton.setText("Save");

        final SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                for (final FieldBinding fieldBinding : fieldBindings) {
                    fieldBinding.updateModel();
                }

                final List<DeployerParameter> parameters = destination.getDeployerParameters();

                /*
                 * Check if we have to remove the destination location.
                 */
                final boolean contains = parameters.contains(model);
                if (contains) {
                    parameters.remove(model);
                }

                /*
                 * Add the model that is currently edited.
                 */
                parameters.add(model);

                /*
                 * Save the destination.
                 */
                final EnvironmentServiceAsync service = Registry.get(AdmModule.ENVIRONMENT_SERVICE);
                final AsyncCallback<ClientDestination> callback = new AsyncCallback<ClientDestination>() {

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
                        box.setTitle("Failed to save parameter.");
                        box.setMessage(builder.toString());
                        box.setButtons(MessageBox.OK);
                        box.show();
                    }

                    @Override
                    public void onSuccess(final ClientDestination destination) {
                        final MessageBox box = new MessageBox();
                        box.setIcon(MessageBox.INFO);
                        box.setTitle("Save parameter.");
                        box.setMessage("Successfully saved " + destination.getName() + ".");
                        box.setButtons(MessageBox.OK);
                        box.show();

                        /*
                         * Hide the window that contains this details panel.
                         */
                        if (window != null) {
                            final Listener<MessageBoxEvent> messageBoxListener = new Listener<MessageBoxEvent>() {

                                @Override
                                public void handleEvent(final MessageBoxEvent be) {
                                    window.hide();
                                }

                            };
                            box.addCallback(messageBoxListener);
                        }
                    }

                };

                service.saveDestination(destination, callback);
            }

        };
        saveButton.addSelectionListener(listener);

        formPanel.addButton(saveButton);

        return formPanel;
    }

    public void refreshParameterTypes() {
        final DestinationDetailsPanel destinationDetailsPanel = Registry.get("DESTINATION_DETAILS_PNL");
        final ClientDeployer selectedDeployer = destinationDetailsPanel.getSelectedDeployer();
        final List<DeployerParameterTemplate> parameters = selectedDeployer.getParameters();

        final ListStore<EnumWrapper<DeployerParameterType>> store = type.getStore();
        store.removeAll();
        for (final DeployerParameterTemplate deployerParameterTemplate : parameters) {
            final DeployerParameterType parameterType = deployerParameterTemplate.getType();
            final EnumWrapper<DeployerParameterType> wrapper = new EnumWrapper<DeployerParameterType>(parameterType);
            store.add(wrapper);
        }
        type.setStore(store);
    }

    /**
     * @param destination
     *        the destination to set
     */
    public void setDestination(final ClientDestination destination) {
        this.destination = destination;
    }

    public void setWindow(final Window window) {
        this.window = window;
    }

}
