package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.privilege;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.Privilege;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.security.AuthorizationServiceAsync;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractDetailPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.model.privilege.ClientPrivilege;
import nl.tranquilizedquality.adm.gwt.gui.client.service.privilege.PrivilegeServiceAsync;
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
 * Panel that will display the details of a {@link Privilege}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 26, 2012
 * 
 */
public class PrivilegeDetailPanel extends AbstractDetailPanel<ClientPrivilege> {

    /** The model object. */
    private ClientPrivilege privilege;

    /** The name of the privilege. */
    private TextField<String> name;

    /** The valid or not checkbox. */
    private CheckBox valid;

    /** The name of the scope where this privilege belongs to. */
    private TextField<String> scopeName;

    /** The description of the privilege. */
    private TextArea description;

    /** The save button to save the role. */
    private Button saveButton;

    /** The table that displays the roles where this privilege is used in. */
    private PrivilegeRoleTable privilegeRolePanel;

    /**
     * Default constructor.
     */
    public PrivilegeDetailPanel() {
        initializeWidgets();

        performPrivilegeCheck();
    }

    /**
     * Checks the privileges of the logged in user to enable and disable certain
     * GUI functionality.
     */
    @Override
    protected void performPrivilegeCheck() {
        final AuthorizationServiceAsync authorizationService = Registry.get(AdmModule.AUTHORIZATION_SERVICE);

        final AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

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

                name.setReadOnly(true);
                scopeName.setReadOnly(true);
            }

        };
        authorizationService.isLoggedInUserAuthorized("EDIT_PRIVILEGE", callback);
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

        add(detailPanel, new RowData(1, 210, new Margins(0)));

        final LayoutContainer relationsPanel = createRelationsPanel();

        add(relationsPanel, new RowData(1, 1, new Margins(0)));
    }

    /**
     * Creates a {@link LayoutContainer} that displays the relations of a
     * privilege.
     * 
     * @return Returns the {@link LayoutContainer} with the relations on it.
     */
    private LayoutContainer createRelationsPanel() {
        final LayoutContainer container = new LayoutContainer();
        final RowLayout layout = new RowLayout(Orientation.HORIZONTAL);
        container.setLayout(layout);

        this.privilegeRolePanel = new PrivilegeRoleTable();

        container.add(privilegeRolePanel, new RowData(1, 1, new Margins(4)));

        return container;
    }

    /**
     * Creates the details panel where the details of the {@link Privilege} are
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
        formPanel.setId("privilege-form-panel");
        formPanel.setHeading("Details");
        formPanel.setFrame(true);
        formPanel.setLabelWidth(110);
        formPanel.setHeight(250);
        formPanel.setButtonAlign(HorizontalAlignment.RIGHT);

        name = new TextField<String>();
        name.setId("privilege-name");
        name.setAllowBlank(false);
        name.setName("name");
        name.setFieldLabel("Name");
        left.add(name, formData);

        valid = new CheckBox();
        valid.setId("privilege-valid");
        valid.setName("valid");
        valid.setFieldLabel("Valid");
        valid.setEnabled(true);
        left.add(valid, new FormData(15, 20));

        scopeName = new TextField<String>();
        scopeName.setId("privilege-scope-name");
        scopeName.setAllowBlank(false);
        scopeName.setName("scope");
        scopeName.setFieldLabel("Scope");
        left.add(scopeName, formData);

        main.add(left, new ColumnData(350));
        formPanel.add(main);

        description = new TextArea();
        description.setId("privilege-description");
        description.setAllowBlank(true);
        description.setName("description");
        description.setFieldLabel("Description");
        description.setHeight(50);
        formPanel.add(description, new FormData("100%"));

        saveButton = new Button();
        saveButton.setId("save-privilege-btn");
        saveButton.setText("Save");

        final SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                for (final FieldBinding fieldBinding : fieldBindings) {
                    fieldBinding.updateModel();
                }

                final PrivilegeServiceAsync privilegeService = Registry.get(AdmModule.PRIVILEGE_SERVICE);
                final AsyncCallback<ClientPrivilege> callback = new AsyncCallback<ClientPrivilege>() {

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
                        box.setTitle("Failed to save privilege.");
                        box.setMessage(builder.toString());
                        box.setButtons(MessageBox.OK);
                        box.show();
                    }

                    @Override
                    public void onSuccess(final ClientPrivilege privilege) {
                        final MessageBox box = new MessageBox();
                        box.setIcon(MessageBox.INFO);
                        box.setTitle("Save privilege.");
                        box.setMessage("Successfully saved " + privilege.getName() + ".");
                        box.setButtons(MessageBox.OK);
                        box.show();
                    }

                };

                privilegeService.savePrivilege(privilege, callback);
            }

        };
        saveButton.addSelectionListener(listener);

        formPanel.addButton(saveButton);

        return formPanel;
    }

    @Override
    public void setModel(final ClientPrivilege model) {
        this.privilege = model;

        if (privilege.isPersistent()) {
            final AsyncCallback<ClientPrivilege> callback = new AsyncCallback<ClientPrivilege>() {

                @Override
                public void onFailure(final Throwable throwable) {
                    final MessageBox box = new MessageBox();
                    box.setIcon(MessageBox.ERROR);
                    box.setTitle("Retrieve privilege.");
                    box.setMessage(throwable.getMessage());
                    box.setButtons(MessageBox.OK);
                    box.show();
                }

                @Override
                public void onSuccess(final ClientPrivilege privilege) {
                    PrivilegeDetailPanel.this.privilege = privilege;
                    bindModel(PrivilegeDetailPanel.this.privilege);

                    privilegeRolePanel.setModel(PrivilegeDetailPanel.this.privilege.getRoles());
                }

            };

            final PrivilegeServiceAsync privilegeService = Registry.get(AdmModule.PRIVILEGE_SERVICE);
            privilegeService.findPrivilegeById(this.privilege.getId(), callback);
        }
    }

}
