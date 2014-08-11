package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.AbstractServiceException;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.security.AuthorizationServiceAsync;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractGridPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.controller.navigation.AdmNavigationController;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUser;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUserSearchCommand;
import nl.tranquilizedquality.adm.gwt.gui.client.service.user.UserServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.service.user.UserServiceException;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.AdmTabs;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.user.role.AssignMultipleUserRoleWindow;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.RowNumberer;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Table that displays {@link User} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 22 jan. 2011
 */
public class UserManagementTable extends AbstractGridPanel {

    /** The search criteria. */
    private final ClientUserSearchCommand sc;

    /** The remote service that manages users. */
    private final UserServiceAsync userService;

    /** The icons of the application. */
    private final AdmIcons icons;

    /** The edit menu item to edit the selected user. */
    private MenuItem editMenuItem;

    /** The {@link Button} that will reset the password of the selected user. */
    private Button resetPasswordButton;

    /**
     * The menu item that will appear in the context menu that can reset the
     * password of the selected user.
     */
    private MenuItem resetPasswordMenuItem;

    private AssignMultipleUserRoleWindow multipleUserRoleAssignmentWindow;

    private MenuItem assignRoleMenuItem;

    private Button assignRoleButton;

    private Button exportToExcelButton;

    /** The {@link Button} which is used to add users. */
    private Button addButton;

    /**
     * Default constructor.
     */
    public UserManagementTable(final ClientUserSearchCommand sc) {
        userService = Registry.get(AdmModule.USER_SERVICE);
        this.icons = Registry.get(AdmModule.ICONS);
        this.sc = sc;

        proxy = new RpcProxy<PagingLoadResult<ClientUser>>() {

            @Override
            public void load(final Object loadConfig, final AsyncCallback<PagingLoadResult<ClientUser>> callback) {
                userService.findUsers((PagingLoadConfig) loadConfig, UserManagementTable.this.sc, callback);
            }
        };

        this.panelStateId = UserManagementTable.class.getName();

        setIcon(AbstractImagePrototype.create(this.icons.userGroup()));

        /*
         * Initialize the widgets.
         */
        initializeWidgets();

        /*
         * Check logged in user authorization.
         */
        checkAuthorization();

        /*
         * Set selection model to be in MULTI selection model.
         */
        final GridSelectionModel<BeanModel> selectionModel = grid.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.MULTI);
        grid.setSelectionModel(selectionModel);

        setHeading("Users");

        performPrivilegeCheck();
    }

    private void performPrivilegeCheck() {
        final AuthorizationServiceAsync authorizationService = Registry.get(AdmModule.AUTHORIZATION_SERVICE);
        authorizationService.isLoggedInUserAuthorized("ADD_USER", new AsyncCallback<Boolean>() {

            @Override
            public void onFailure(final Throwable throwable) {
                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.ERROR);
                box.setTitle("Create user check.");
                box.setMessage(throwable.getMessage());
                box.setButtons(MessageBox.OK);
                box.show();
            }

            @Override
            public void onSuccess(final Boolean authorized) {
                if (authorized) {
                    editMenuItem.enable();
                    resetPasswordButton.enable();
                    resetPasswordMenuItem.enable();
                    assignRoleMenuItem.enable();
                    assignRoleButton.enable();
                    exportToExcelButton.enable();
                    addButton.enable();
                }
                else {
                    editMenuItem.disable();
                    resetPasswordButton.disable();
                    resetPasswordMenuItem.disable();
                    assignRoleMenuItem.disable();
                    assignRoleButton.disable();
                    exportToExcelButton.disable();
                    addButton.disable();
                }
            }

        });
    }

    @Override
    protected void initializeWidgets() {
        /*
         * Add add new user button.
         */
        addButton = new Button("Add");
        addButton.setIcon(AbstractImagePrototype.create(icons.add()));

        final SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                add();
            }

        };
        addButton.addSelectionListener(listener);
        this.menuBarButtons.add(addButton);

        /*
         * Add reset password button.
         */
        resetPasswordButton = new Button("Reset password");
        resetPasswordButton.setIcon(AbstractImagePrototype.create(icons.reset()));

        final SelectionListener<ButtonEvent> resetPasswordListener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                resetPassword();
            }

        };
        resetPasswordButton.addSelectionListener(resetPasswordListener);
        this.menuBarButtons.add(resetPasswordButton);

        /*
         * Add assign roles button.
         */
        assignRoleButton = new Button("Assign roles");
        assignRoleButton.setIcon(AbstractImagePrototype.create(icons.role()));

        final SelectionListener<ButtonEvent> assignRoleListener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                assignRolesToSelectedUsers();
            }

        };
        assignRoleButton.addSelectionListener(assignRoleListener);
        // this.menuBarButtons.add(assignRoleButton);

        /*
         * Add export to excel button.
         */
        exportToExcelButton = new Button("Export to Excel");
        exportToExcelButton.setIcon(AbstractImagePrototype.create(icons.excel()));

        final SelectionListener<ButtonEvent> exportToExcelListener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                exportToExcel();
            }

        };
        exportToExcelButton.addSelectionListener(exportToExcelListener);
        // this.menuBarButtons.add(exportToExcelButton);

        /*
         * Add edit menu item.
         */
        editMenuItem = new MenuItem();
        editMenuItem.setIcon(AbstractImagePrototype.create(icons.edit()));
        editMenuItem.setText("Edit");
        editMenuItem.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(final MenuEvent ce) {
                view();
            }
        });
        menuItems.add(editMenuItem);

        /*
         * Add reset password menu item.
         */
        resetPasswordMenuItem = new MenuItem();
        resetPasswordMenuItem.setIcon(AbstractImagePrototype.create(icons.reset()));
        resetPasswordMenuItem.setText("Reset password");
        resetPasswordMenuItem.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(final MenuEvent ce) {
                resetPassword();
            }
        });
        menuItems.add(resetPasswordMenuItem);

        /*
         * Add assign roles menu item.
         */
        assignRoleMenuItem = new MenuItem();
        assignRoleMenuItem.setIcon(AbstractImagePrototype.create(icons.reset()));
        assignRoleMenuItem.setText("Assign role");
        assignRoleMenuItem.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(final MenuEvent ce) {
                assignRolesToSelectedUsers();
            }
        });
        // menuItems.add(assignRoleMenuItem);

        super.initializeWidgets();
    }

    private void checkAuthorization() {

    }

    @Override
    protected List<ColumnConfig> createColumns() {
        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        configs.add(new RowNumberer());

        ColumnConfig column = new ColumnConfig();

        final GridCellRenderer<BeanModel> renderer = new GridCellRenderer<BeanModel>() {

            @Override
            public Object render(final BeanModel model, final String property, final ColumnData config, final int rowIndex,
                    final int colIndex, final ListStore<BeanModel> store,
                    final Grid<BeanModel> grid) {

                final ClientUser user = model.getBean();

                return "<img src=\"" + GWT.getHostPageBaseURL() + "services/images/user/" + user.getId() + "?"
                        + new Date().getTime() + "\" width=\"67px\" height=\"90px\" />";
            }
        };

        column = new ColumnConfig();
        column.setId("userPicture");
        column.setHeader("Picture");
        column.setWidth(70);
        column.setSortable(false);
        column.setRenderer(renderer);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("id");
        column.setHeader("User ID");
        column.setWidth(60);
        column.setSortable(true);
        configs.add(column);

        column = new ColumnConfig();
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
        column.setId("comments");
        column.setHeader("Comments");
        column.setWidth(200);
        column.setSortable(false);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("created");
        column.setHeader("Created");
        column.setWidth(60);
        column.setSortable(true);
        column.setDateTimeFormat(DateTimeFormat.getMediumDateTimeFormat());
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
        column.setDateTimeFormat(DateTimeFormat.getMediumDateTimeFormat());
        configs.add(column);

        column = new ColumnConfig();
        column.setId("alteredBy");
        column.setHeader("Altered By");
        column.setWidth(60);
        column.setSortable(false);
        configs.add(column);

        return configs;
    }

    protected void view() {
        final GridSelectionModel<BeanModel> selectionModel = grid.getSelectionModel();
        final BeanModel selectedItem = selectionModel.getSelectedItem();

        if (selectedItem != null) {
            final ClientUser user = selectedItem.getBean();
            final AdmNavigationController controller = Registry.get(AdmModule.NAVIGATION_CONTROLLER);
            controller.selectTab(AdmTabs.USER_DETAIL_TAB, user);
        }
    }

    @Override
    protected void handleDoubleClick() {
        view();
    }

    private void add() {
        final ClientUser user = new ClientUser();

        final AdmNavigationController controller = Registry.get(AdmModule.NAVIGATION_CONTROLLER);
        controller.selectTab(AdmTabs.USER_DETAIL_TAB, user);
    }

    private void resetPassword() {
        final GridSelectionModel<BeanModel> selectionModel = this.grid.getSelectionModel();

        final BeanModel selectedItem = selectionModel.getSelectedItem();
        if (selectedItem != null) {
            /*
             * Set password sent to false so it will be resent and set password
             * to null so the password will be auto generated again.
             */
            final ClientUser user = selectedItem.getBean();

            /*
             * Call user service to save the user.
             */
            final UserServiceAsync userService = Registry.get(AdmModule.USER_SERVICE);
            final AsyncCallback<ClientUser> callback = new AsyncCallback<ClientUser>() {

                @Override
                public void onFailure(final Throwable throwable) {
                    final StringBuilder builder = new StringBuilder();

                    if (throwable instanceof UserServiceException) {
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
                    box.setTitle("Failed to reset user password.");
                    box.setMessage(builder.toString());
                    box.setButtons(MessageBox.OK);
                    box.show();
                }

                @Override
                public void onSuccess(final ClientUser user) {
                    final MessageBox box = new MessageBox();
                    box.setIcon(MessageBox.INFO);
                    box.setTitle("Reset user password.");
                    box.setMessage("Successfully resetted password for " + user.getName() + ".");
                    box.setButtons(MessageBox.OK);
                    box.show();
                }

            };

            userService.resetPassword(user, callback);
        }
    }

    private void assignRolesToSelectedUsers() {
        final GridSelectionModel<BeanModel> selectionModel = this.grid.getSelectionModel();

        final List<BeanModel> selectedItems = selectionModel.getSelectedItems();

        if (selectedItems != null) {
            final List<ClientUser> users = new ArrayList<ClientUser>();

            for (final BeanModel beanModel : selectedItems) {
                final ClientUser user = beanModel.getBean();
                users.add(user);
            }

            if (multipleUserRoleAssignmentWindow == null) {
                multipleUserRoleAssignmentWindow = new AssignMultipleUserRoleWindow(users);
            }
            else {
                multipleUserRoleAssignmentWindow.setUsers(users);
            }

            multipleUserRoleAssignmentWindow.show();
        }

    }

    private void exportToExcel() {
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            @Override
            public void onSuccess(final Void result) {

                final StringBuilder builder = new StringBuilder();
                builder.append("view/userlist");

                Window.Location.replace(builder.toString());
            }

            @Override
            public void onFailure(final Throwable throwable) {
                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.ERROR);
                box.setTitle("Failed to export users.");
                box.setMessage("Export of users failed!");
                box.setButtons(MessageBox.OK);
                box.show();
            }
        };
        userService.setExportCriteria(this.sc, callback);

    }

}
