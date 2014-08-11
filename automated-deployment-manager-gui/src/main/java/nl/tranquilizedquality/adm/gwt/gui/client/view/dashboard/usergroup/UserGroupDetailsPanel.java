/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: Aug 24, 2012 File: fUserGroupDetailsPanel.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.view.dashboard.security
 * 
 * Copyright (c) 2012 Tranquilized Quality www.Tranquilized Quality.nl All rights
 * reserved.
 * 
 * This software is the confidential and proprietary information of Tranquilized Quality
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Tranquilized Quality.
 * </pre>
 */
package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.usergroup;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.AbstractServiceException;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.security.AuthorizationServiceAsync;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractDetailPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUserGroup;
import nl.tranquilizedquality.adm.gwt.gui.client.service.security.UserGroupServiceAsync;

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
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Panel where a user group is managed on.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 24, 2012
 */
public class UserGroupDetailsPanel extends AbstractDetailPanel<ClientUserGroup> {

    /** The icons of the application. */
    private final AdmIcons icons;

    /** The name of the repository. */
    private TextField<String> name;

    /** Save button. */
    private Button saveButton;

    /** Table where the users are displayed in that are part of the user group. */
    private UsersTable userTable;

    private AvailableUsersSearchPanel availableUsersSearchPanel;

    /**
     * Default constructor.
     */
    public UserGroupDetailsPanel() {
        this.icons = Registry.get(AdmModule.ICONS);
        initializeWidgets();
        performPrivilegeCheck();
    }

    @Override
    protected void initializeWidgets() {
        final BorderLayout layout = new BorderLayout();
        setLayout(layout);

        formPanel = createDetailPanel();

        final LayoutContainer relationsPanel = createRelationsPanel();

        add(this.formPanel, new BorderLayoutData(LayoutRegion.NORTH, 150, 150, 150));
        add(relationsPanel, new BorderLayoutData(LayoutRegion.CENTER));
    }

    /**
     * @return
     */
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
    public void setModel(final ClientUserGroup model) {
        this.model = model;

        if (this.model.isPersistent()) {
            final AsyncCallback<ClientUserGroup> callback = new AsyncCallback<ClientUserGroup>() {

                @Override
                public void onFailure(final Throwable throwable) {
                    final MessageBox box = new MessageBox();
                    box.setIcon(MessageBox.ERROR);
                    box.setTitle("Retrieve user group.");
                    box.setMessage(throwable.getMessage());
                    box.setButtons(MessageBox.OK);
                    box.show();
                }

                @Override
                public void onSuccess(final ClientUserGroup userGroup) {
                    UserGroupDetailsPanel.this.model = userGroup;
                    bindModel(UserGroupDetailsPanel.this.model);

                    final List<User> users = userGroup.getUsers();
                    userTable.setModel(users);
                    availableUsersSearchPanel.initialize();
                }

            };

            final UserGroupServiceAsync userGroupService = Registry.get(AdmModule.USER_GROUP_SERVICE);
            userGroupService.findUserGroupById(this.model.getId(), callback);
        }
        else {
            bindModel(this.model);
            userTable.initialize();
            availableUsersSearchPanel.initialize();
        }
    }

    @Override
    protected void performPrivilegeCheck() {
        final AuthorizationServiceAsync authorizationService = Registry.get(AdmModule.AUTHORIZATION_SERVICE);
        authorizationService.isLoggedInUserAuthorized("ADD_USER_GROUP", new AsyncCallback<Boolean>() {

            @Override
            public void onFailure(final Throwable throwable) {
                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.ERROR);
                box.setTitle("Create user group check.");
                box.setMessage(throwable.getMessage());
                box.setButtons(MessageBox.OK);
                box.show();
            }

            @Override
            public void onSuccess(final Boolean authorized) {
                name.setReadOnly(!authorized);

                if (authorized) {
                    saveButton.show();
                }
                else {
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
        formPanel.setIcon(AbstractImagePrototype.create(icons.userGroup()));
        formPanel.setButtonAlign(HorizontalAlignment.RIGHT);

        /*
         * Create field set for user group related information.
         */
        final FieldSet repositoryInfoFieldSet = new FieldSet();
        repositoryInfoFieldSet.setLayout(new FormLayout());
        repositoryInfoFieldSet.setCollapsible(true);
        repositoryInfoFieldSet.setAutoHeight(true);
        repositoryInfoFieldSet.setHeading("User Group Information");

        name = new TextField<String>();
        name.setId("user-group-details-pnl-name");
        name.setAllowBlank(false);
        name.setName("name");
        name.setFieldLabel("Name");
        repositoryInfoFieldSet.add(name);

        formPanel.add(repositoryInfoFieldSet);

        saveButton = new Button();
        saveButton.setId("save-user-group-btn");
        saveButton.setText("Save");

        final SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                saveUserGroup();
            }

        };
        saveButton.addSelectionListener(listener);

        formPanel.addButton(saveButton);

        return formPanel;
    }

    /**
     * Saves the user group.
     */
    public void saveUserGroup() {
        for (final FieldBinding fieldBinding : fieldBindings) {
            fieldBinding.updateModel();
        }

        final UserGroupServiceAsync userGroupService = Registry.get(AdmModule.USER_GROUP_SERVICE);
        final AsyncCallback<ClientUserGroup> callback = new AsyncCallback<ClientUserGroup>() {

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
                }
                else {
                    builder.append(throwable.getMessage());
                }

                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.ERROR);
                box.setTitle("Failed to save user group.");
                box.setMessage(builder.toString());
                box.setButtons(MessageBox.OK);
                box.show();
            }

            @Override
            public void onSuccess(final ClientUserGroup userGroup) {
                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.INFO);
                box.setTitle("Save user group.");
                box.setMessage("Successfully saved " + userGroup.getName() + ".");
                box.setButtons(MessageBox.OK);
                box.show();

                setModel(userGroup);
            }

        };

        userGroupService.saveUserGroup(model, callback);
    }

    public ClientUserGroup getUserGroup() {
        return model;
    }

}
