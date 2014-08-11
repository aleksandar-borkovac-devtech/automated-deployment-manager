/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: Aug 24, 2012 File: fUserGroupTable.java
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
import java.util.List;

import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.security.AuthorizationServiceAsync;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractGridPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.controller.navigation.AdmNavigationController;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUserGroup;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUserGroupSearchCommand;
import nl.tranquilizedquality.adm.gwt.gui.client.service.security.UserGroupServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.AdmTabs;

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
 * Grid where user groups are displayed in.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 24, 2012
 * 
 */
public class UserGroupTable extends AbstractGridPanel {

    /** The icons of the application. */
    private final AdmIcons icons;

    /** The search criteria. */
    private final ClientUserGroupSearchCommand sc;

    /** The repository service. */
    private UserGroupServiceAsync userGroupService;

    /** The add button. */
    private Button addButton;

    /** The edit menu item. */
    private MenuItem editMenuItem;

    /**
     * Constructor that takes the search criteria to filter on.
     * 
     * @param sc
     *            The search criteria.
     */
    public UserGroupTable(final ClientUserGroupSearchCommand sc) {
        setHeading("User Groups");
        this.sc = sc;
        this.icons = Registry.get(AdmModule.ICONS);
        initializeWidgets();
        performPrivilegeCheck();
    }

    @Override
    protected void initializeWidgets() {
        userGroupService = Registry.get(AdmModule.USER_GROUP_SERVICE);

        proxy = new RpcProxy<PagingLoadResult<ClientUserGroup>>() {

            @Override
            public void load(final Object loadConfig, final AsyncCallback<PagingLoadResult<ClientUserGroup>> callback) {
                if (UserGroupTable.this.sc != null) {
                    userGroupService.findUserGroups((PagingLoadConfig) loadConfig, UserGroupTable.this.sc, callback);
                }
            }
        };

        this.panelStateId = UserGroupTable.class.getName();

        addButton = new Button("Add");
        addButton.setIcon(AbstractImagePrototype.create(icons.addUserGroup()));

        final SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                add();
            }

        };
        addButton.addSelectionListener(listener);

        this.menuBarButtons.add(addButton);

        editMenuItem = new MenuItem();
        editMenuItem.setIcon(AbstractImagePrototype.create(icons.editUserGroup()));
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
     * Creates a fresh {@link ClientUserGroup} and navigates to the details
     * panel.
     */
    private void add() {
        final ClientUserGroup userGroup = new ClientUserGroup();

        final AdmNavigationController controller = Registry.get(AdmModule.NAVIGATION_CONTROLLER);
        controller.selectTab(AdmTabs.USER_GROUP_DETAILS_TAB, userGroup);
    }

    @Override
    protected List<ColumnConfig> createColumns() {
        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        configs.add(new RowNumberer());

        ColumnConfig column = new ColumnConfig();
        column.setId("name");
        column.setHeader("Name");
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

    /**
     * Retrieves the selected item and displays it in the user group details
     * screen.
     */
    private void view() {
        final GridSelectionModel<BeanModel> selectionModel = grid.getSelectionModel();
        final BeanModel selectedItem = selectionModel.getSelectedItem();

        if (selectedItem != null) {
            final ClientUserGroup userGroup = selectedItem.getBean();
            final AdmNavigationController controller = Registry.get(AdmModule.NAVIGATION_CONTROLLER);
            controller.selectTab(AdmTabs.USER_GROUP_DETAILS_TAB, userGroup);
        }
    }

    @Override
    protected void handleDoubleClick() {
        view();
    }

    private void performPrivilegeCheck() {
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
                if (authorized) {
                    addButton.show();
                    editMenuItem.enable();
                }
                else {
                    addButton.hide();
                    editMenuItem.disable();
                }
            }

        });
    }

}
