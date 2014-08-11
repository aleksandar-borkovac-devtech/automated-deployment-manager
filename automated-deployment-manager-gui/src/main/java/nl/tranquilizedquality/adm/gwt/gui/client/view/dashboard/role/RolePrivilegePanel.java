package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.role;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.Scope;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractGridPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.privilege.ClientPrivilege;
import nl.tranquilizedquality.adm.gwt.gui.client.model.privilege.ClientPrivilegeSearchCommand;
import nl.tranquilizedquality.adm.gwt.gui.client.model.role.ClientRole;
import nl.tranquilizedquality.adm.gwt.gui.client.service.privilege.PrivilegeServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.service.role.RoleServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.service.role.RoleServiceException;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Panel where the privileges of a role are displayed in.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 26, 2012
 * 
 */
public class RolePrivilegePanel extends AbstractGridPanel {

    /** The icons of the application. */
    private final AdmIcons icons;

    /** The asynchronous role service. */
    private final RoleServiceAsync roleService;

    /** The asynchronous role service. */
    private final PrivilegeServiceAsync privilegeService;

    /** The {@link ClientRole} that will be edited. */
    private ClientRole role;

    /** A {@link Window} where this panel is displayed in if applicable. */
    private Window window;

    /** Filter list. */
    private List<ClientPrivilege> filterPrivileges;

    /** The search criteria. */
    private final ClientPrivilegeSearchCommand sc;

    /**
     * Constructor taking the {@link ClientRole} that will be altered and
     * {@link List} containing {@link ClientPrivilege} objects that the role
     * already has so they will not be displayed.
     * 
     * @param role
     *            The {@link ClientRole} that will be shown.
     * @param filterPrivileges
     *            A list of {@link ClientPrivilege} objects.
     */
    public RolePrivilegePanel(final ClientRole role, final List<ClientPrivilege> filterPrivileges) {
        this.role = role;
        this.filterPrivileges = filterPrivileges;
        this.icons = Registry.get(AdmModule.ICONS);
        this.roleService = Registry.get(AdmModule.ROLE_SERVICE);
        this.privilegeService = Registry.get(AdmModule.PRIVILEGE_SERVICE);

        sc = new ClientPrivilegeSearchCommand();

        if (!filterPrivileges.isEmpty()) {
            sc.setExcludedPrivileges(filterPrivileges);
        }

        final Scope scope = this.role.getScope();
        sc.setScope(scope);

        this.proxy = new RpcProxy<PagingLoadResult<ClientPrivilege>>() {

            @Override
            public void load(final Object loadConfig, final AsyncCallback<PagingLoadResult<ClientPrivilege>> callback) {
                privilegeService.findPrivileges((PagingLoadConfig) loadConfig, sc, callback);
            }
        };

        initializeWidgets();
    }

    @Override
    protected void initializeWidgets() {
        /*
         * Add add button.
         */
        final Button addPrivilege = new Button("Add privilege");
        addPrivilege.setIcon(AbstractImagePrototype.create(icons.add()));

        final SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                final GridSelectionModel<BeanModel> selectionModel = grid.getSelectionModel();
                final List<BeanModel> selectedItems = selectionModel.getSelectedItems();

                /*
                 * Add selected privileges.
                 */
                for (final BeanModel beanModel : selectedItems) {
                    final ClientPrivilege privilege = beanModel.getBean();

                    role.addPrivilege(privilege);
                }

                /*
                 * Save the role.
                 */
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
                        box.setTitle("Role privilege management.");
                        box.setMessage(builder.toString());
                        box.setButtons(MessageBox.OK);
                        box.show();
                    }

                    @Override
                    public void onSuccess(final ClientRole clientRole) {
                        final MessageBox box = new MessageBox();
                        box.setIcon(MessageBox.INFO);
                        box.setTitle("Role privilege management.");
                        box.setMessage("Successfully added a privilege.");
                        box.setButtons(MessageBox.OK);
                        box.show();

                        if (window != null) {
                            final Listener<MessageBoxEvent> messageBoxListener = new Listener<MessageBoxEvent>() {

                                @Override
                                public void handleEvent(final MessageBoxEvent be) {
                                    window.hide();
                                }

                            };
                            box.addCallback(messageBoxListener);
                        }
                    }

                };

                // save the role.
                roleService.saveRole(role, callback);
            }

        };
        addPrivilege.addSelectionListener(listener);

        menuBarButtons.add(addPrivilege);

        super.initializeWidgets();
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
        column.setId("description");
        column.setHeader("Description");
        column.setWidth(200);
        column.setSortable(false);
        configs.add(column);

        return configs;
    }

    /**
     * @param role
     *            the role to set
     */
    public void setRole(final ClientRole role) {
        this.role = role;

        final Scope scope = role.getScope();
        sc.setScope(scope);
    }

    /**
     * @param window
     *            the window to set
     */
    public void setWindow(final Window window) {
        this.window = window;
    }

    /**
     * @param filterPrivileges
     *            the filterPrivileges to set
     */
    public void setFilterPrivileges(final List<ClientPrivilege> filterPrivileges) {
        this.filterPrivileges = filterPrivileges;

        if (role != null && sc != null) {
            sc.setExcludedPrivileges(this.filterPrivileges);
        }
    }
}
