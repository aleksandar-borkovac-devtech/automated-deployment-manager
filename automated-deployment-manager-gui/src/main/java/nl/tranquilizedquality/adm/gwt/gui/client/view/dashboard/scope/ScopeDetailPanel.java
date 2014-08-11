package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.scope;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.Role;
import nl.tranquilizedquality.adm.commons.business.domain.Scope;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.security.AuthorizationServiceAsync;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractDetailPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.model.scope.ClientScope;
import nl.tranquilizedquality.adm.gwt.gui.client.service.scope.ScopeServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.service.scope.ScopeServiceException;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Panel displaying the details of a {@link ClientScope}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 26, 2012
 */
public class ScopeDetailPanel extends AbstractDetailPanel<ClientScope> {

    /** Panel that displays all the roles for the scope. */
    private ScopeRolesTable rolesPanel;

    /** Panel that displays all the privileges in a scope. */
    private ScopePrivilegesTable privilegesPanel;

    /** The scope that is being edited. */
    private ClientScope scope;

    /** The save button. */
    private Button saveButton;

    /** The {@link TextField} where the name of the scope is displayed in. */
    private TextField<String> name;

    /**
     * The {@link TextField} where the description of the scope is displayed in.
     */
    private TextArea description;

    /** The asynchronous scope service. */
    private final ScopeServiceAsync scopeService;

    /**
     * Default constructor.
     */
    public ScopeDetailPanel() {
        this.scopeService = Registry.get(AdmModule.SCOPE_SERVICE);

        initializeWidgets();

        performPrivilegeCheck();
    }

    @Override
    protected void performPrivilegeCheck() {
        final AuthorizationServiceAsync authorizationService = Registry.get(AdmModule.AUTHORIZATION_SERVICE);
        final AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

            @Override
            public void onFailure(final Throwable throwable) {
                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.ERROR);
                box.setTitle("Edit scope check.");
                box.setMessage(throwable.getMessage());
                box.setButtons(MessageBox.OK);
                box.show();
            }

            @Override
            public void onSuccess(final Boolean authorized) {
                if (authorized) {
                    name.setReadOnly(true);
                    description.setReadOnly(false);
                    saveButton.show();
                } else {
                    name.setReadOnly(true);
                    description.setReadOnly(true);
                    saveButton.hide();
                }
            }

        };
        authorizationService.isLoggedInUserAuthorized("EDIT_SCOPE", callback);
    }

    @Override
    public void setModel(final ClientScope scope) {

        final AsyncCallback<ClientScope> callback = new AsyncCallback<ClientScope>() {

            @Override
            public void onFailure(final Throwable throwable) {
                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.ERROR);
                box.setTitle("Retrieve scope.");
                box.setMessage(throwable.getMessage());
                box.setButtons(MessageBox.OK);
                box.show();
            }

            @Override
            public void onSuccess(final ClientScope scope) {
                setScope(scope);
            }
        };

        final ScopeServiceAsync scopeService = Registry.get(AdmModule.SCOPE_SERVICE);
        scopeService.findScopeById(scope.getId(), callback);
    }

    /**
     * Initializes the widgets on the panel.
     */
    @Override
    protected void initializeWidgets() {
        final RowLayout layout = new RowLayout(Orientation.VERTICAL);
        setLayout(layout);

        /*
         * Create the detail panel.
         */
        this.formPanel = createDetailPanel();

        final LayoutContainer relationsPanel = createRelationsPanel();

        add(formPanel, new RowData(1, 160, new Margins(0)));
        add(relationsPanel, new RowData(1, 1, new Margins(0)));
    }

    /**
     * Creates the panel where you define your filter criteria.
     * 
     * @return Returns the {@link FormPanel} containing the filter criteria.
     */
    @Override
    protected FormPanel createDetailPanel() {
        /*
         * Create form panel.
         */
        final FormPanel formPanel = new FormPanel();
        formPanel.setHeading("Details");
        formPanel.setFrame(true);
        formPanel.setLabelWidth(100);

        /*
         * Create name field.
         */
        name = new TextField<String>();
        name.setId("scope-name");
        name.setName("name");
        name.setFieldLabel("Name");
        name.setEnabled(true);
        name.setReadOnly(true);
        formPanel.add(name);

        /*
         * Create description text area.
         */
        description = new TextArea();
        description.setId("scope-description");
        description.setName("description");
        description.setFieldLabel("Description");
        description.setAllowBlank(true);
        description.setEnabled(true);
        description.setReadOnly(true);
        description.setHeight(50);
        formPanel.add(description, new FormData("100%"));

        /*
         * Add the save button.
         */
        saveButton = new Button("Save");
        saveButton.setId("save-scope-btn");

        final SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                for (final FieldBinding fieldBinding : fieldBindings) {
                    fieldBinding.updateModel();
                }

                final AsyncCallback<ClientScope> callback = new AsyncCallback<ClientScope>() {

                    @Override
                    public void onFailure(final Throwable throwable) {
                        final StringBuilder builder = new StringBuilder();

                        if (throwable instanceof ScopeServiceException) {
                            final ScopeServiceException ex = (ScopeServiceException) throwable;
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
                        box.setTitle("Failed to save scope.");
                        box.setMessage(builder.toString());
                        box.setButtons(MessageBox.OK);
                        box.show();
                    }

                    @Override
                    public void onSuccess(final ClientScope scope) {
                        final MessageBox box = new MessageBox();
                        box.setIcon(MessageBox.INFO);
                        box.setTitle("Save scope.");
                        box.setMessage("Successfully saved " + scope.getName() + ".");
                        box.setButtons(MessageBox.OK);
                        box.show();
                    }

                };

                scopeService.saveScope(scope, callback);
            }

        };
        saveButton.addSelectionListener(listener);

        formPanel.addButton(saveButton);

        return formPanel;
    }

    /**
     * Creates the panel containing relational details of a {@link Scope}.
     * 
     * @return Returns a {@link LayoutContainer} containing all the panels.
     */
    private LayoutContainer createRelationsPanel() {
        final LayoutContainer container = new LayoutContainer();
        final RowLayout layout = new RowLayout(Orientation.HORIZONTAL);
        container.setLayout(layout);

        this.rolesPanel = new ScopeRolesTable();
        this.privilegesPanel = new ScopePrivilegesTable();

        container.add(rolesPanel, new RowData(0.5, 1, new Margins(4, 0, 4, 0)));
        container.add(privilegesPanel, new RowData(0.5, 1, new Margins(4)));

        return container;
    }

    /**
     * Sets the specified {@link ClientScope}.
     * 
     * @param scope
     *            The {@link ClientScope} that will be binded to the details
     *            form.
     */
    private void setScope(final ClientScope scope) {
        ScopeDetailPanel.this.scope = scope;

        bindModel(ScopeDetailPanel.this.scope);

        privilegesPanel.setModel(scope.getPrivileges());

        rolesPanel.setModel(new ArrayList<Role>(scope.getRoles()));
        rolesPanel.setScope(scope);
    }

}
