/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: Aug 24, 2012 File: fUsersTable.java
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractRelationListTable;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUser;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUserGroup;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.module.destination.AvailableDestinationTable;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Grid where all users that are part of the user group are displayed in.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 24, 2012
 */
class UsersTable extends AbstractRelationListTable<User, ClientUser> {

    /** The icons of the application. */
    private final AdmIcons icons;

    /** Button to remove the selected user(s) from a {@link UserGroup}. */
    private Button removeButton;

    /** Details panel where the {@link UserGroup} is displayed in. */
    private final UserGroupDetailsPanel detailsPanel;

    /**
     * Default constructor.
     */
    public UsersTable(final UserGroupDetailsPanel detailsPanel) {
        setHeading("Users in group");
        this.icons = Registry.get(AdmModule.ICONS);
        this.detailsPanel = detailsPanel;
        setIcon(AbstractImagePrototype.create(icons.usersInGroup()));
        initializeWidgets();
    }

    public void initialize() {
        final ClientUserGroup userGroup = detailsPanel.getUserGroup();
        if (userGroup.isPersistent()) {
            removeButton.enable();
        }
        else {
            removeButton.disable();
        }
    }

    @Override
    protected void initializeWidgets() {

        removeButton = new Button("Remove User from group");
        removeButton.setIcon(AbstractImagePrototype.create(icons.removeUserFromGroup()));
        final SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                final ClientUserGroup userGroup = detailsPanel.getUserGroup();
                final List<User> users = userGroup.getUsers();

                final GridSelectionModel<BeanModel> selectionModel = grid.getSelectionModel();
                final List<BeanModel> selectedItems = selectionModel.getSelectedItems();

                if (!selectedItems.isEmpty()) {
                    for (final BeanModel beanModel : selectedItems) {
                        final ClientUser user = beanModel.getBean();
                        users.remove(user);
                    }

                    detailsPanel.saveUserGroup();
                }
            }
        };
        removeButton.addSelectionListener(listener);

        menuBarButtons.add(removeButton);

        super.initializeWidgets();
    }

    @Override
    protected String getPanelStateId() {
        return AvailableDestinationTable.class.getName();
    }

    @Override
    protected Class<ClientUser> getBeanModelClass() {
        return ClientUser.class;
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
        column.setId("userName");
        column.setHeader("Username");
        column.setWidth(100);
        column.setSortable(true);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("created");
        column.setHeader("Created");
        column.setWidth(60);
        column.setSortable(true);
        column.setDateTimeFormat(DateTimeFormat.getShortDateTimeFormat());
        configs.add(column);

        column = new ColumnConfig();
        column.setId("createdBy");
        column.setHeader("Created By");
        column.setWidth(60);
        column.setSortable(false);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("altered");
        column.setHeader("Altered");
        column.setWidth(60);
        column.setSortable(true);
        column.setDateTimeFormat(DateTimeFormat.getShortDateTimeFormat());
        configs.add(column);

        column = new ColumnConfig();
        column.setId("alteredBy");
        column.setHeader("Altered By");
        column.setWidth(60);
        column.setSortable(false);
        configs.add(column);

        return configs;
    }

    @Override
    public void setModel(final Collection<User> model) {
        super.setModel(model);

        initialize();
    }

}
