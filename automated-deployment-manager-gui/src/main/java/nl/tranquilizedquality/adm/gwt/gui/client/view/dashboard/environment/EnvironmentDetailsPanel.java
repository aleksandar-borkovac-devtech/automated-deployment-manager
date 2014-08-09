/**
 * <pre>
 * Project: automated-deployment-manager-gui 
 * Created on: Aug 1, 2012
 * File: EnvironmentDetailsPanel.java
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

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.AbstractModule;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.AbstractServiceException;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.security.AuthorizationServiceAsync;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractDetailPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientEnvironment;
import nl.tranquilizedquality.adm.gwt.gui.client.service.environment.EnvironmentServiceAsync;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Details panel where the details of an environment are displayed on.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 1, 2012
 */
public class EnvironmentDetailsPanel extends AbstractDetailPanel<ClientEnvironment> {

    /** The icons of the application. */
    private final AdmIcons icons;

    /** The name of the environment */
    private TextField<String> name;

    /** The description of the environment */
    private TextArea description;

    /** Determines if this is a production environment. */
    private CheckBox production;

    /** The button used to store the environment */
    private Button saveButton;

    /** Table where the users are displayed in that are part of the user group. */
    private UsersTable userTable;

    /**
     * Panel where you can search for users to be added as deployer for this
     * environment.
     */
    private AvailableUsersSearchPanel availableUsersSearchPanel;

    /**
     * Default constructor.
     */
    public EnvironmentDetailsPanel() {
        icons = Registry.get(AbstractModule.ICONS);
        initializeWidgets();
        performPrivilegeCheck();
    }

    @Override
    protected void initializeWidgets() {
        final BorderLayout layout = new BorderLayout();
        setLayout(layout);

        formPanel = createDetailPanel();

        final LayoutContainer relationsPanel = createRelationsPanel();

        add(this.formPanel, new BorderLayoutData(LayoutRegion.NORTH, 290, 290, 290));
        add(relationsPanel, new BorderLayoutData(LayoutRegion.CENTER));
    }

    private LayoutContainer createRelationsPanel() {
        final LayoutContainer container = new LayoutContainer();
        final RowLayout layout = new RowLayout(Orientation.HORIZONTAL);
        container.setLayout(layout);

        userTable = new UsersTable(this);
        availableUsersSearchPanel = new AvailableUsersSearchPanel(this);

        container.add(userTable, new RowData(0.5, 1, new Margins(4, 0, 4, 4)));
        container.add(availableUsersSearchPanel, new RowData(0.5, 1, new Margins(4, 4, 4, 4)));

        return container;
    }

    @Override
    public void setModel(final ClientEnvironment model) {
        this.model = model;

        if (this.model.isPersistent()) {
            final AsyncCallback<ClientEnvironment> callback = new AsyncCallback<ClientEnvironment>() {

                @Override
                public void onFailure(final Throwable throwable) {
                    final MessageBox box = new MessageBox();
                    box.setIcon(MessageBox.ERROR);
                    box.setTitle("Retrieve environment.");
                    box.setMessage(throwable.getMessage());
                    box.setButtons(MessageBox.OK);
                    box.show();
                }

                @Override
                public void onSuccess(final ClientEnvironment environment) {
                    EnvironmentDetailsPanel.this.model = environment;
                    bindModel(EnvironmentDetailsPanel.this.model);

                    final List<User> users = EnvironmentDetailsPanel.this.model.getUsers();
                    userTable.setModel(users);
                    availableUsersSearchPanel.initialize();
                }

            };

            final EnvironmentServiceAsync service = Registry.get(AdmModule.ENVIRONMENT_SERVICE);
            service.findEnvironmentById(this.model.getId(), callback);
        } else {
            bindModel(this.model);
            userTable.initialize();
            availableUsersSearchPanel.initialize();
        }
    }

    @Override
    protected void performPrivilegeCheck() {
        final AuthorizationServiceAsync authorizationService = Registry.get(AdmModule.AUTHORIZATION_SERVICE);
        final AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

            @Override
            public void onFailure(final Throwable throwable) {
                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.ERROR);
                box.setTitle("Create environment check.");
                box.setMessage(throwable.getMessage());
                box.setButtons(MessageBox.OK);
                box.show();
            }

            @Override
            public void onSuccess(final Boolean authorized) {
                name.setReadOnly(!authorized);
                description.setReadOnly(!authorized);

                if (authorized) {
                    saveButton.show();
                } else {
                    saveButton.hide();
                }
            }

        };
        authorizationService.isLoggedInUserAuthorized("ADD_ENVIRONMENT", callback);
    }

    @Override
    protected FormPanel createDetailPanel() {
        final FormPanel formPanel = new FormPanel();
        formPanel.setHeading("Details");
        formPanel.setFrame(true);
        formPanel.setLabelWidth(110);
        formPanel.setIcon(AbstractImagePrototype.create(icons.addEnvironment()));
        formPanel.setButtonAlign(HorizontalAlignment.RIGHT);

        /*
         * Create field set for repository related information.
         */
        final FieldSet destinationInfoFieldSet = new FieldSet();
        destinationInfoFieldSet.setLayout(new FormLayout());
        destinationInfoFieldSet.setCollapsible(true);
        destinationInfoFieldSet.setAutoHeight(true);
        destinationInfoFieldSet.setHeading("Environment Information");

        /*
         * Add environment name.
         */
        name = new TextField<String>();
        name.setId("environment-details-pnl-name");
        name.setAllowBlank(false);
        name.setName("name");
        name.setFieldLabel("Name");
        destinationInfoFieldSet.add(name);

        production = new CheckBox();
        production.setId("environment-details-pnl-production");
        production.setName("production");
        production.setFieldLabel("Production");
        destinationInfoFieldSet.add(production, new FormData(15, 20));

        /*
         * Add environment description.
         */
        description = new TextArea();
        description.setId("environment-details-pnl-description");
        description.setAllowBlank(true);
        description.setName("description");
        description.setFieldLabel("Description");
        description.setHeight(100);
        description.setAllowBlank(false);
        destinationInfoFieldSet.add(description, new FormData("100%"));

        formPanel.add(destinationInfoFieldSet);

        /*
         * Add save button.
         */
        saveButton = new Button();
        saveButton.setId("save-environment-btn");
        saveButton.setText("Save");

        final SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                saveEnvironment();
            }

        };
        saveButton.addSelectionListener(listener);

        formPanel.addButton(saveButton);

        final FormButtonBinding binding = new FormButtonBinding(formPanel);
        binding.addButton(saveButton);

        return formPanel;
    }

    public ClientEnvironment getEnvironment() {
        return model;
    }

    /**
     * Saves the environment.
     */
    public void saveEnvironment() {
        for (final FieldBinding fieldBinding : fieldBindings) {
            fieldBinding.updateModel();
        }

        final EnvironmentServiceAsync service = Registry.get(AdmModule.ENVIRONMENT_SERVICE);
        final AsyncCallback<ClientEnvironment> callback = new AsyncCallback<ClientEnvironment>() {

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
                box.setTitle("Failed to save environment.");
                box.setMessage(builder.toString());
                box.setButtons(MessageBox.OK);
                box.show();
            }

            @Override
            public void onSuccess(final ClientEnvironment environment) {

                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.INFO);
                box.setTitle("Save environment.");
                box.setMessage("Successfully saved " + environment.getName() + ".");
                box.setButtons(MessageBox.OK);
                box.show();

                setModel(environment);
            }

        };

        service.saveEnvironment(model, callback);
    }

}
