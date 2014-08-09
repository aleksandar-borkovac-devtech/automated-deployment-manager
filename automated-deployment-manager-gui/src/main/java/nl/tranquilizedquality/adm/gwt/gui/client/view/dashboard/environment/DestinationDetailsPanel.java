/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 11 sep. 2011 File: DestinationDetailsPanel.java
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
package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.environment;

import java.util.HashSet;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameter;
import nl.tranquilizedquality.adm.commons.business.domain.Protocol;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.converter.BeanModelConverter;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.converter.EnumConverter;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.converter.EnumWrapper;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.AbstractServiceException;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.security.AuthorizationServiceAsync;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractDetailPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientDeployer;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientDestination;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientDestinationHost;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientEnvironment;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUserGroup;
import nl.tranquilizedquality.adm.gwt.gui.client.service.deployment.DeploymentServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.service.environment.EnvironmentServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.service.security.UserGroupServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.environment.location.DeployerParametersTable;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Details panel where a {@link ClientDestination} is displayed and managed in.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 11 sep. 2011
 */
public class DestinationDetailsPanel extends AbstractDetailPanel<ClientDestination> {

    /** The icons of the application. */
    private final AdmIcons icons;

    /** The save button. */
    private Button saveButton;

    /** The host this destination is located on. */
    private ComboBox<ModelData> host;

    /** The name of the destination. */
    private TextField<String> name;

    /** The deployer id. */
    private ComboBox<ModelData> deployerId;

    /**
     * The prefix of the environment used when retrieving configuration files
     * from a distribution module.
     */
    private TextField<String> prefix;

    /** The environment this destination represents. */
    private ComboBox<ModelData> environment;

    /** The {@link ComboBox} containing a list of protocols. */
    private ComboBox<EnumWrapper<Protocol>> protocol;

    /** The {@link ComboBox} containing a list of user groups. */
    private ComboBox<ModelData> userGroup;

    /** Table where the destination locations are displayed in. */
    private DeployerParametersTable destinationLocationsTable;

    /**
     * Default constructor.
     */
    public DestinationDetailsPanel() {
        this.icons = Registry.get(AdmModule.ICONS);

        /*
         * Initializes the widgets on the details panel.
         */
        initializeWidgets();

        /*
         * Add custom field bindings.
         */
        FieldBinding binding = new FieldBinding(this.protocol, "protocol");
        binding.setConverter(new EnumConverter<Protocol>());
        fieldBindings.add(binding);

        /*
         * Environment binding.
         */
        binding = new FieldBinding(this.environment, "environment");
        binding.setConverter(new BeanModelConverter());
        fieldBindings.add(binding);

        /*
         * Host binding.
         */
        binding = new FieldBinding(this.host, "destinationHost");
        binding.setConverter(new BeanModelConverter());
        fieldBindings.add(binding);

        /*
         * User group binding
         */
        binding = new FieldBinding(this.userGroup, "userGroup");
        binding.setConverter(new BeanModelConverter());
        fieldBindings.add(binding);

        /*
         * Add binding for deployers
         */
        binding = new FieldBinding(this.deployerId, "deployer");
        binding.setConverter(new BeanModelConverter());
        fieldBindings.add(binding);

        /*
         * Refreshes the dynamic data on the details panel.
         */
        refreshData();

        performPrivilegeCheck();

        Registry.register("DESTINATION_DETAILS_PNL", this);
    }

    @Override
    protected void initializeWidgets() {
        final RowLayout layout = new RowLayout(Orientation.VERTICAL);
        setLayout(layout);

        formPanel = createDetailPanel();

        destinationLocationsTable = new DeployerParametersTable(this);
        destinationLocationsTable.setDestination(model);

        add(this.formPanel, new RowData(1, 310, new Margins(0)));
        add(destinationLocationsTable, new RowData(1, 1, new Margins(0)));
    }

    private void refreshData() {
        final EnvironmentServiceAsync environmentService = Registry.get(AdmModule.ENVIRONMENT_SERVICE);

        final AsyncCallback<List<ClientEnvironment>> callback = new AsyncCallback<List<ClientEnvironment>>() {

            @Override
            public void onSuccess(final List<ClientEnvironment> environments) {
                final ListStore<ModelData> store = environment.getStore();
                store.removeAll();

                final BeanModelFactory factory = BeanModelLookup.get().getFactory(ClientEnvironment.class);

                final List<BeanModel> beanModels = factory.createModel(environments);
                for (final BeanModel beanModel : beanModels) {
                    store.add(beanModel);
                }

            }

            @Override
            public void onFailure(final Throwable throwable) {

            }
        };
        environmentService.findEnvironments(callback);

        final AsyncCallback<List<ClientDestinationHost>> hostsCallback = new AsyncCallback<List<ClientDestinationHost>>() {

            @Override
            public void onSuccess(final List<ClientDestinationHost> hosts) {
                final ListStore<ModelData> store = host.getStore();
                store.removeAll();

                final BeanModelFactory factory = BeanModelLookup.get().getFactory(ClientDestinationHost.class);

                final List<BeanModel> beanModels = factory.createModel(hosts);
                for (final BeanModel beanModel : beanModels) {
                    store.add(beanModel);
                }
            }

            @Override
            public void onFailure(final Throwable throwable) {

            }
        };
        environmentService.findDestinationHosts(hostsCallback);

        final AsyncCallback<List<ClientUserGroup>> userGroupCallback = new AsyncCallback<List<ClientUserGroup>>() {

            @Override
            public void onFailure(final Throwable throwable) {
                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.ERROR);
                box.setTitle("Retrieve organizations.");
                box.setMessage(throwable.getMessage());
                box.setButtons(MessageBox.OK);
                box.show();
            }

            @Override
            public void onSuccess(final List<ClientUserGroup> userGroups) {
                final ListStore<ModelData> store = userGroup.getStore();
                store.removeAll();

                final BeanModelFactory factory = BeanModelLookup.get().getFactory(ClientUserGroup.class);

                for (final ClientUserGroup userGroup : userGroups) {

                    final BeanModel model = factory.createModel(userGroup);

                    store.add(model);
                }

                userGroup.setStore(store);
            }
        };

        final UserGroupServiceAsync userGroupService = Registry.get(AdmModule.USER_GROUP_SERVICE);
        userGroupService.findUserGroups(userGroupCallback);

        refreshDeployers();
    }

    @Override
    public void setModel(final ClientDestination model) {
        this.model = model;

        if (this.model.isPersistent()) {
            final AsyncCallback<ClientDestination> callback = new AsyncCallback<ClientDestination>() {

                @Override
                public void onFailure(final Throwable throwable) {
                    final MessageBox box = new MessageBox();
                    box.setIcon(MessageBox.ERROR);
                    box.setTitle("Retrieve destination.");
                    box.setMessage(throwable.getMessage());
                    box.setButtons(MessageBox.OK);
                    box.show();
                }

                @Override
                public void onSuccess(final ClientDestination office) {
                    DestinationDetailsPanel.this.model = office;
                    bindModel(DestinationDetailsPanel.this.model);

                    final List<DeployerParameter> parameters = DestinationDetailsPanel.this.model.getDeployerParameters();
                    destinationLocationsTable.setModel(parameters);
                }

            };

            final EnvironmentServiceAsync service = Registry.get(AdmModule.ENVIRONMENT_SERVICE);
            service.findDestinationById(this.model.getId(), callback);
        } else {
            bindModel(this.model);

            destinationLocationsTable.setModel(new HashSet<DeployerParameter>());
        }

        destinationLocationsTable.setDestination(this.model);
    }

    @Override
    protected void performPrivilegeCheck() {
        final AuthorizationServiceAsync authorizationService = Registry.get(AdmModule.AUTHORIZATION_SERVICE);
        authorizationService.isLoggedInUserAuthorized("ADD_DESTINATION", new AsyncCallback<Boolean>() {

            @Override
            public void onFailure(final Throwable throwable) {
                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.ERROR);
                box.setTitle("Create destination check.");
                box.setMessage(throwable.getMessage());
                box.setButtons(MessageBox.OK);
                box.show();
            }

            @Override
            public void onSuccess(final Boolean authorized) {
                name.setReadOnly(!authorized);
                host.setReadOnly(!authorized);
                deployerId.setReadOnly(!authorized);
                environment.setReadOnly(!authorized);
                protocol.setReadOnly(!authorized);
                userGroup.setReadOnly(!authorized);
                prefix.setReadOnly(!authorized);

                if (authorized) {
                    saveButton.show();
                } else {
                    saveButton.hide();
                }
            }

        });
    }

    @Override
    protected FormPanel createDetailPanel() {
        final FormPanel formPanel = new FormPanel();
        formPanel.setHeading("Details");
        formPanel.setFrame(true);
        formPanel.setLabelWidth(110);
        formPanel.setIcon(AbstractImagePrototype.create(icons.destination()));
        formPanel.setButtonAlign(HorizontalAlignment.RIGHT);

        /*
         * Create field set for repository related information.
         */
        final FieldSet destinationInfoFieldSet = new FieldSet();
        destinationInfoFieldSet.setLayout(new FormLayout());
        destinationInfoFieldSet.setCollapsible(true);
        destinationInfoFieldSet.setAutoHeight(true);
        destinationInfoFieldSet.setHeading("Destination Information");

        /*
         * Add deployer identifier.
         */
        name = new TextField<String>();
        name.setId("destination-details-pnl-name");
        name.setAllowBlank(false);
        name.setName("name");
        name.setFieldLabel("Name");
        destinationInfoFieldSet.add(name);

        /*
         * Add host.
         */
        host = new ComboBox<ModelData>();
        host.setId("destination-details-pnl-host");
        host.setName("host");
        host.setFieldLabel("Host");
        host.setDisplayField("hostName");
        host.setEmptyText("Select host..");
        host.setTriggerAction(TriggerAction.ALL);
        host.setForceSelection(true);
        host.setEditable(false);
        host.setAllowBlank(false);

        /*
         * Add store for hosts.
         */
        final ListStore<ModelData> hostStore = new ListStore<ModelData>();
        host.setStore(hostStore);
        destinationInfoFieldSet.add(host);

        /*
         * Add protocol
         */
        protocol = new ComboBox<EnumWrapper<Protocol>>();
        protocol.setId("destination-details-pnl-protocol");
        protocol.setName("protocol");
        protocol.setFieldLabel("Protocol");
        protocol.setDisplayField("value");
        protocol.setEmptyText("Select protocol..");
        protocol.setTriggerAction(TriggerAction.ALL);
        protocol.setForceSelection(true);
        protocol.setEditable(false);
        protocol.setAllowBlank(false);

        /*
         * Add enums.
         */
        final ListStore<EnumWrapper<Protocol>> store = new ListStore<EnumWrapper<Protocol>>();
        final Protocol[] values = Protocol.values();
        for (final Protocol protocol : values) {
            final EnumWrapper<Protocol> wrapper = new EnumWrapper<Protocol>(protocol);
            store.add(wrapper);
        }

        protocol.setStore(store);
        destinationInfoFieldSet.add(protocol);

        /*
         * Add user group.
         */
        userGroup = new ComboBox<ModelData>();
        userGroup.setId("destination-details-pnl-user-group");
        userGroup.setName("userGroup");
        userGroup.setFieldLabel("Group");
        userGroup.setDisplayField("name");
        userGroup.setEmptyText("Select group..");
        userGroup.setTriggerAction(TriggerAction.ALL);
        userGroup.setForceSelection(true);
        userGroup.setEditable(false);
        userGroup.setAllowBlank(false);

        /*
         * Add enums.
         */
        final ListStore<ModelData> userGroupStore = new ListStore<ModelData>();
        userGroup.setStore(userGroupStore);
        destinationInfoFieldSet.add(userGroup);

        /*
         * Add deployer identifier.
         */
        deployerId = new ComboBox<ModelData>();
        deployerId.setId("destination-details-pnl-deployer-id");
        deployerId.setName("deployer");
        deployerId.setFieldLabel("Deployer ID");
        deployerId.setDisplayField("name");
        deployerId.setEmptyText("Select deployer..");
        deployerId.setTriggerAction(TriggerAction.ALL);
        deployerId.setForceSelection(true);
        deployerId.setEditable(false);
        deployerId.setAllowBlank(false);

        final ListStore<ModelData> deployerStore = new ListStore<ModelData>();
        deployerId.setStore(deployerStore);
        destinationInfoFieldSet.add(deployerId);

        /*
         * Add environment.
         */
        environment = new ComboBox<ModelData>();
        environment.setId("destination-details-pnl-environment");
        environment.setName("environment");
        environment.setFieldLabel("Environment");
        environment.setDisplayField("name");
        environment.setEmptyText("Select environment..");
        environment.setTriggerAction(TriggerAction.ALL);
        environment.setForceSelection(true);
        environment.setEditable(false);
        environment.setAllowBlank(false);

        /*
         * Add store for environments.
         */
        final ListStore<ModelData> environmentStore = new ListStore<ModelData>();
        environment.setStore(environmentStore);
        destinationInfoFieldSet.add(environment);

        /*
         * Add environment prefix.
         */
        prefix = new TextField<String>();
        prefix.setId("destination-details-pnl-prefix");
        prefix.setAllowBlank(true);
        prefix.setName("prefix");
        prefix.setFieldLabel("Prefix");
        destinationInfoFieldSet.add(prefix);

        /*
         * Add fieldset to form.
         */
        formPanel.add(destinationInfoFieldSet);

        /*
         * Add save button.
         */
        saveButton = new Button();
        saveButton.setId("save-destination-btn");
        saveButton.setText("Save");

        final SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                for (final FieldBinding fieldBinding : fieldBindings) {
                    fieldBinding.updateModel();
                }

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
                        box.setTitle("Failed to save destination.");
                        box.setMessage(builder.toString());
                        box.setButtons(MessageBox.OK);
                        box.show();
                    }

                    @Override
                    public void onSuccess(final ClientDestination destination) {
                        final MessageBox box = new MessageBox();
                        box.setIcon(MessageBox.INFO);
                        box.setTitle("Save destination.");
                        box.setMessage("Successfully saved " + destination.getName() + ".");
                        box.setButtons(MessageBox.OK);
                        box.show();

                        setModel(destination);
                    }

                };

                service.saveDestination(model, callback);
            }

        };
        saveButton.addSelectionListener(listener);

        formPanel.addButton(saveButton);

        final FormButtonBinding binding = new FormButtonBinding(formPanel);
        binding.addButton(saveButton);

        return formPanel;
    }

    /**
     * Sets and populates the store on the deployer combobox with the available deployers.
     */
    private void refreshDeployers() {

        final DeploymentServiceAsync deploymentService = Registry.get(AdmModule.DEPLOYMENT_SERVICE);
        deploymentService.findAvailableDeployers(new AsyncCallback<List<ClientDeployer>>() {

            @Override
            public void onSuccess(final List<ClientDeployer> deployers) {
                final ListStore<ModelData> store = deployerId.getStore();
                store.removeAll();

                final BeanModelFactory factory = BeanModelLookup.get().getFactory(ClientDeployer.class);

                final List<BeanModel> beanModels = factory.createModel(deployers);
                for (final BeanModel beanModel : beanModels) {
                    store.add(beanModel);
                }
            }

            @Override
            public void onFailure(final Throwable throwable) {

            }
        });
    }

    public ClientDeployer getSelectedDeployer() {
        return (ClientDeployer) this.model.getDeployer();
    }

}
