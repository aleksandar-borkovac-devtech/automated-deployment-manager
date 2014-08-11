package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.role;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.Role;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.security.AuthorizationServiceAsync;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractDetailPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.model.role.ClientRole;
import nl.tranquilizedquality.adm.gwt.gui.client.service.role.RoleServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.service.role.RoleServiceException;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Panel where the details of a {@link ClientRole} are displayed and can be
 * altered.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 26, 2012
 * 
 */
public class RoleDetailPanel extends AbstractDetailPanel<ClientRole> {

    /** The role that is displayed on this panel. */
    private ClientRole role;

    /** The panel where the users are displayed in that have this role. */
    private RoleUserTable roleUserPanel;

    /** The panel where the privileges of the role are displayed in. */
    private RolePrivilegesTable rolePrivilegesPanel;

    /** The name of the role. */
    private TextField<String> name;

    /** The valid or not checkbox. */
    private CheckBox valid;

    /** The frozen or not checkbox. */
    private CheckBox frozen;

    /** The description of the role. */
    private TextArea description;

    /** The save button to save the role. */
    private Button saveButton;

    /**
     * Default constructor.
     */
    public RoleDetailPanel() {
        setId(this.getClass().getName());

        /*
         * Initialize the GUI.
         */
        initializeWidgets();

        /*
         * Perform the privilege checks.
         */
        performPrivilegeCheck();
    }

    /**
     * Checks the privileges of the logged in user to enable and disable certain
     * GUI functionality.
     */
    @Override
    protected void performPrivilegeCheck() {
        /*
         * Retrieve the authorization service to perform privilege checks with.
         */
        final AuthorizationServiceAsync authorizationService = Registry.get(AdmModule.AUTHORIZATION_SERVICE);

        /*
         * Check if the user is allowed to add a role or not. If not the save
         * button is hidden and all fields are read only.
         */
        AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

            @Override
            public void onFailure(final Throwable throwable) {
                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.ERROR);
                box.setTitle("Create role check.");
                box.setMessage(throwable.getMessage());
                box.setButtons(MessageBox.OK);
                box.show();
            }

            @Override
            public void onSuccess(final Boolean authorized) {
                if (authorized) {
                    name.setReadOnly(false);
                    valid.setReadOnly(false);
                    description.setReadOnly(false);
                    saveButton.show();
                }
                else {
                    name.setReadOnly(true);
                    valid.setReadOnly(true);
                    description.setReadOnly(true);
                    saveButton.hide();
                }
            }

        };
        authorizationService.isLoggedInUserAuthorized("ADD_ROLE", callback);

        /*
         * Check if the user is allowed to edit a frozen role. If not the frozen
         * check box will be read only so the user can't change it.
         */
        callback = new AsyncCallback<Boolean>() {

            @Override
            public void onFailure(final Throwable throwable) {
                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.ERROR);
                box.setTitle("Edit frozen role check.");
                box.setMessage(throwable.getMessage());
                box.setButtons(MessageBox.OK);
                box.show();
            }

            @Override
            public void onSuccess(final Boolean authorized) {
                if (authorized) {
                    frozen.setReadOnly(false);
                }
                else {
                    frozen.setReadOnly(true);
                }
            }

        };
        authorizationService.isLoggedInUserAuthorized("EDIT_FROZEN_ROLE", callback);
    }

    /**
     * Create a vertical layout to show the user details and its relations.
     */
    @Override
    protected void initializeWidgets() {
        final RowLayout layout = new RowLayout(Orientation.VERTICAL);
        setLayout(layout);

        /*
         * Create the detail panel.
         */
        final FormPanel detailPanel = createDetailPanel();

        add(detailPanel, new RowData(1, 230, new Margins(0)));

        /*
         * Create relations panel.
         */
        final LayoutContainer relationsPanel = createRelationsPanel();

        add(relationsPanel, new RowData(1, 1, new Margins(0)));
    }

    /**
     * Create the panel with the relation tables.
     * 
     * @return the container containing the tables.
     */
    private LayoutContainer createRelationsPanel() {
        final LayoutContainer container = new LayoutContainer();
        final RowLayout layout = new RowLayout(Orientation.HORIZONTAL);
        container.setLayout(layout);

        this.roleUserPanel = new RoleUserTable();
        this.rolePrivilegesPanel = new RolePrivilegesTable();

        container.add(roleUserPanel, new RowData(0.50, 1, new Margins(4)));
        container.add(rolePrivilegesPanel, new RowData(0.50, 1, new Margins(4, 0, 4, 0)));

        return container;
    }

    /**
     * Creates the details panel where the details of the {@link Role} are
     * displayed in.
     * 
     * @return Returns a {@link FormPanel} with all the appropriate controls on
     *         it.
     */
    @Override
    protected FormPanel createDetailPanel() {

        final LayoutContainer left = new LayoutContainer();
        left.setStyleAttribute("paddingRight", "10px");
        final FormLayout layout = new FormLayout();
        layout.setLabelWidth(110);
        left.setLayout(layout);

        final FormData formData = new FormData("100%");

        final LayoutContainer main = new LayoutContainer();
        main.setLayout(new ColumnLayout());

        formPanel = new FormPanel();
        formPanel.setId("role-form-panel");
        formPanel.setHeading("Details");
        formPanel.setFrame(true);
        formPanel.setLabelWidth(110);
        formPanel.setHeight(230);
        formPanel.setButtonAlign(HorizontalAlignment.RIGHT);

        name = new TextField<String>();
        name.setId("role-name");
        name.setAllowBlank(false);
        name.setName("name");
        name.setFieldLabel("Name");
        left.add(name, formData);

        valid = new CheckBox();
        valid.setId("role-valid");
        valid.setName("valid");
        valid.setFieldLabel("Valid");
        left.add(valid, new FormData(15, 20));

        frozen = new CheckBox();
        frozen.setId("role-frozen");
        frozen.setName("frozen");
        frozen.setFieldLabel("Frozen");
        frozen.setReadOnly(true);
        left.add(frozen, new FormData(15, 20));

        main.add(left, new ColumnData(350));
        formPanel.add(main);

        description = new TextArea();
        description.setId("role-description");
        description.setAllowBlank(true);
        description.setName("description");
        description.setFieldLabel("Description");
        formPanel.add(description, new FormData("100%"));

        saveButton = new Button();
        saveButton.setText("Save");

        final SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                for (final FieldBinding fieldBinding : fieldBindings) {
                    fieldBinding.updateModel();
                }

                final Boolean roleIsFrozen = role.isFrozen();

                /*
                 * If the role is a frozen role we need to check if the user is
                 * allowed to edit a frozen role.
                 */
                if (roleIsFrozen) {
                    final AuthorizationServiceAsync authorizationService = Registry.get(AdmModule.AUTHORIZATION_SERVICE);
                    final AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

                        @Override
                        public void onFailure(final Throwable throwable) {
                            final MessageBox box = new MessageBox();
                            box.setIcon(MessageBox.ERROR);
                            box.setTitle("Edit frozen role check.");
                            box.setMessage(throwable.getMessage());
                            box.setButtons(MessageBox.OK);
                            box.show();
                        }

                        @Override
                        public void onSuccess(final Boolean authorized) {

                            if (authorized) {
                                /*
                                 * When the logged in user is authorized the
                                 * save is done.
                                 */
                                saveRole();
                            }
                            else {
                                final String msg = "You are not allowed to edit a frozen role. Contact your administrator if this should not be the case.";

                                final MessageBox box = new MessageBox();
                                box.setIcon(MessageBox.WARNING);
                                box.setTitle("Edit role.");
                                box.setMessage(msg);
                                box.setButtons(MessageBox.OK);
                                box.show();
                            }
                        }

                    };

                    authorizationService.isLoggedInUserAuthorized("EDIT_FROZEN_ROLE", callback);
                }
                else {
                    /*
                     * The role is not frozen so the save is allowed.
                     */
                    saveRole();
                }
            }

        };
        saveButton.addSelectionListener(listener);

        formPanel.addButton(saveButton);

        return formPanel;
    }

    /**
     * Saves the role.
     */
    private void saveRole() {
        final RoleServiceAsync roleService = Registry.get(AdmModule.ROLE_SERVICE);

        final AsyncCallback<ClientRole> callback = new AsyncCallback<ClientRole>() {

            @Override
            public void onFailure(final Throwable throwable) {
                final StringBuilder builder = new StringBuilder();

                if (throwable instanceof RoleServiceException) {
                    final RoleServiceException ex = (RoleServiceException) throwable;
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
                box.setTitle("Failed to save role.");
                box.setMessage(builder.toString());
                box.setButtons(MessageBox.OK);
                box.show();
            }

            @Override
            public void onSuccess(final ClientRole role) {
                setModel(role);

                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.INFO);
                box.setTitle("Save role.");
                box.setMessage("Successfully saved " + role.getName() + ".");
                box.setButtons(MessageBox.OK);
                box.show();
            }

        };

        roleService.saveRole(role, callback);
    }

    @Override
    public void setModel(final ClientRole model) {
        this.role = model;

        if (role.isPersistent()) {
            final AsyncCallback<ClientRole> callback = new AsyncCallback<ClientRole>() {

                @Override
                public void onFailure(final Throwable throwable) {
                    final MessageBox box = new MessageBox();
                    box.setIcon(MessageBox.ERROR);
                    box.setTitle("Retrieve role.");
                    box.setMessage(throwable.getMessage());
                    box.setButtons(MessageBox.OK);
                    box.show();
                }

                @Override
                public void onSuccess(final ClientRole role) {
                    RoleDetailPanel.this.role = role;
                    bindModel(RoleDetailPanel.this.role);

                    roleUserPanel.setModel(role.getUsers());
                    rolePrivilegesPanel.setModel(role.getPrivileges());
                    rolePrivilegesPanel.setRole(RoleDetailPanel.this.role);
                }

            };

            final RoleServiceAsync roleService = Registry.get(AdmModule.ROLE_SERVICE);
            roleService.findRoleById(this.role.getId(), callback);
        }
        else {
            bindModel(this.role);

            roleUserPanel.setModel(role.getUsers());
            rolePrivilegesPanel.setModel(role.getPrivileges());
            rolePrivilegesPanel.setRole(role);
        }
    }

}
