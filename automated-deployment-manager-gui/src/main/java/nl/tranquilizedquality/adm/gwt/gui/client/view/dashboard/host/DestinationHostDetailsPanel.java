/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 30 okt. 2011 File: DestinationHostDetailsPanel.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.view.dashboard.host
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
package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.host;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.Protocol;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.converter.BeanModelConverter;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.converter.EnumConverter;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.converter.EnumWrapper;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.AbstractServiceException;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.security.AuthorizationServiceAsync;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractDetailPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientDestinationHost;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUserGroup;
import nl.tranquilizedquality.adm.gwt.gui.client.service.environment.EnvironmentServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.service.security.UserGroupServiceAsync;

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
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Panel where the details of a {@link ClientDestinationHost} are displayed in.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 30 okt. 2011
 */
public class DestinationHostDetailsPanel extends AbstractDetailPanel<ClientDestinationHost> {

    /** The icons of the application. */
    private final AdmIcons icons;

    /** The host name. */
    private TextField<String> name;

    /** The port number to connect to. */
    private NumberField portNumber;

    /** The save button. */
    private Button saveButton;

    /** The user name. */
    private TextField<String> userName;

    /** The password. */
    private TextField<String> password;

    /** The {@link ComboBox} containing a list of protocols. */
    private ComboBox<EnumWrapper<Protocol>> protocol;

    /** The {@link ComboBox} containing a list of user groups. */
    private ComboBox<ModelData> userGroup;

    /** The private key to use for authentication when connecting using SSH. */
    private TextArea privateKey;

    /** The terminal to use when connecting to the host. */
    private TextField<String> terminal;

    /**
     * Default constructor.
     */
    public DestinationHostDetailsPanel() {
        this.icons = Registry.get(AdmModule.ICONS);

        initializeWidgets();
        performPrivilegeCheck();
        refreshData();
    }

    @Override
    protected void initializeWidgets() {
        final RowLayout layout = new RowLayout(Orientation.VERTICAL);
        setLayout(layout);

        formPanel = createDetailPanel();

        /*
         * Add custom field bindings.
         */
        FieldBinding binding = new FieldBinding(this.protocol, "protocol");
        binding.setConverter(new EnumConverter<Protocol>());
        fieldBindings.add(binding);

        binding = new FieldBinding(this.userGroup, "userGroup");
        binding.setConverter(new BeanModelConverter());
        fieldBindings.add(binding);

        add(this.formPanel, new RowData(1, 510, new Margins(0)));
        // add(destinationLocationsTable, new RowData(1, 1, new Margins(0)));
    }

    @Override
    public void setModel(final ClientDestinationHost model) {
        this.model = model;

        if (this.model.isPersistent()) {
            final AsyncCallback<ClientDestinationHost> callback = new AsyncCallback<ClientDestinationHost>() {

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
                public void onSuccess(final ClientDestinationHost destinationHost) {
                    DestinationHostDetailsPanel.this.model = destinationHost;
                    bindModel(DestinationHostDetailsPanel.this.model);
                }

            };

            final EnvironmentServiceAsync service = Registry.get(AdmModule.ENVIRONMENT_SERVICE);
            service.findDestinationHostById(this.model.getId(), callback);
        } else {
            bindModel(this.model);
        }
    }

    @Override
    protected void performPrivilegeCheck() {
        final AuthorizationServiceAsync authorizationService = Registry.get(AdmModule.AUTHORIZATION_SERVICE);
        authorizationService.isLoggedInUserAuthorized("ADD_HOST", new AsyncCallback<Boolean>() {

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
                name.setReadOnly(!authorized);
                portNumber.setReadOnly(!authorized);
                userName.setReadOnly(!authorized);
                password.setReadOnly(!authorized);
                protocol.setReadOnly(!authorized);
                privateKey.setReadOnly(!authorized);
                userGroup.setReadOnly(!authorized);
                terminal.setReadOnly(!authorized);

                if (authorized) {
                    saveButton.show();
                } else {
                    saveButton.hide();
                }
            }

        });

        authorizationService.isLoggedInUserAuthorized("ADD_PRIVATE_KEY", new AsyncCallback<Boolean>() {

            @Override
            public void onFailure(final Throwable throwable) {
                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.ERROR);
                box.setTitle("Add private key check");
                box.setMessage(throwable.getMessage());
                box.setButtons(MessageBox.OK);
                box.show();
            }

            @Override
            public void onSuccess(final Boolean authorized) {
                privateKey.setReadOnly(!authorized);
            }

        });
    }

    @Override
    protected FormPanel createDetailPanel() {
        final FormPanel formPanel = new FormPanel();
        formPanel.setHeading("Details");
        formPanel.setFrame(true);
        formPanel.setLabelWidth(110);
        formPanel.setIcon(AbstractImagePrototype.create(icons.addHost()));
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
         * Add host name
         */
        name = new TextField<String>();
        name.setId("destination-host-details-pnl-host-name");
        name.setAllowBlank(false);
        name.setName("hostName");
        name.setFieldLabel("Host Name");
        destinationInfoFieldSet.add(name);

        /*
         * Add port number
         */
        portNumber = new NumberField();
        portNumber.setId("destination-host-details-pnl-port");
        portNumber.setAllowBlank(false);
        portNumber.setName("port");
        portNumber.setFieldLabel("Port");
        portNumber.setPropertyEditorType(Integer.class);
        destinationInfoFieldSet.add(portNumber);

        /*
         * Add protocol
         */
        protocol = new ComboBox<EnumWrapper<Protocol>>();
        protocol.setId("destination-host-details-pnl-protocol");
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
         * Add the terminal.
         */
        terminal = new TextField<String>();
        terminal.setId("destination-host-details-pnl-terminal");
        terminal.setAllowBlank(true);
        terminal.setName("terminal");
        terminal.setFieldLabel("Terminal");
        terminal.setToolTip("The terminal to use when connecting to the host. If left blank it will use the default 'gogrid' terminal.");
        destinationInfoFieldSet.add(terminal);

        /*
         * Add user group.
         */
        userGroup = new ComboBox<ModelData>();
        userGroup.setId("destination-host-details-pnl-group");
        userGroup.setName("group");
        userGroup.setFieldLabel("Group");
        userGroup.setDisplayField("name");
        userGroup.setEmptyText("Select group..");
        userGroup.setTriggerAction(TriggerAction.ALL);
        userGroup.setForceSelection(true);
        userGroup.setEditable(false);
        userGroup.setToolTip("The user group this host is part off. Only the users that are part of that group will see this host.");
        userGroup.setAllowBlank(false);

        /*
         * Add enums.
         */
        final ListStore<ModelData> userGroupStore = new ListStore<ModelData>();
        userGroup.setStore(userGroupStore);
        destinationInfoFieldSet.add(userGroup);

        /*
         * Add user name.
         */
        userName = new TextField<String>();
        userName.setId("destination-host-details-pnl-host-user-name");
        userName.setAllowBlank(false);
        userName.setName("username");
        userName.setFieldLabel("User name");
        destinationInfoFieldSet.add(userName);

        /*
         * Add password.
         */
        password = new TextField<String>();
        password.setId("destination-host-details-pnl-password");
        password.setAllowBlank(true);
        password.setName("password");
        password.setFieldLabel("Password");
        password.setPassword(true);
        destinationInfoFieldSet.add(password);

        /*
         * Add private key.
         */
        privateKey = new TextArea();
        privateKey.setId("destination-host-details-pnl-private-key");
        privateKey.setAllowBlank(true);
        privateKey.setName("authorizedPrivateKey");
        privateKey.setFieldLabel("Private Key");
        privateKey.setHeight(200);
        destinationInfoFieldSet.add(privateKey, new FormData("100%"));

        formPanel.add(destinationInfoFieldSet);

        /*
         * Add save button.
         */
        saveButton = new Button();
        saveButton.setId("save-destination-host-btn");
        saveButton.setText("Save");

        final SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                for (final FieldBinding fieldBinding : fieldBindings) {
                    fieldBinding.updateModel();
                }

                final EnvironmentServiceAsync service = Registry.get(AdmModule.ENVIRONMENT_SERVICE);
                final AsyncCallback<ClientDestinationHost> callback = new AsyncCallback<ClientDestinationHost>() {

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
                        box.setTitle("Failed to save destination host.");
                        box.setMessage(builder.toString());
                        box.setButtons(MessageBox.OK);
                        box.show();
                    }

                    @Override
                    public void onSuccess(final ClientDestinationHost destinationHost) {

                        final MessageBox box = new MessageBox();
                        box.setIcon(MessageBox.INFO);
                        box.setTitle("Save destination host.");
                        box.setMessage("Successfully saved " + destinationHost.getHostName() + ".");
                        box.setButtons(MessageBox.OK);
                        box.show();

                        setModel(destinationHost);
                    }

                };

                service.saveDestinationHost(model, callback);
            }

        };
        saveButton.addSelectionListener(listener);

        formPanel.addButton(saveButton);

        final FormButtonBinding binding = new FormButtonBinding(formPanel);
        binding.addButton(saveButton);

        return formPanel;
    }

    /**
     * Retrieves current data from the server.
     */
    private void refreshData() {
        final AsyncCallback<List<ClientUserGroup>> callback = new AsyncCallback<List<ClientUserGroup>>() {

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
        userGroupService.findUserGroups(callback);
    }

}
