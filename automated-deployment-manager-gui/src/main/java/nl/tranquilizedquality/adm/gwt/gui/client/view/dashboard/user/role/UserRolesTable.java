package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.user.role;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import nl.tranquilizedquality.adm.commons.business.domain.Scope;
import nl.tranquilizedquality.adm.commons.business.domain.UserRole;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.security.AuthorizationServiceAsync;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractRelationTreeTable;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.navigation.RoleTreeItem;
import nl.tranquilizedquality.adm.gwt.gui.client.model.role.ClientRole;
import nl.tranquilizedquality.adm.gwt.gui.client.model.role.ClientUserRole;
import nl.tranquilizedquality.adm.gwt.gui.client.model.scope.ClientScope;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUser;
import nl.tranquilizedquality.adm.gwt.gui.client.service.role.RoleServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.service.user.UserServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.user.ScopeListUtil;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.user.UserDetailPanel;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.CheckColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGridCellRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Table where the roles of a user are displayed in a tree.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 26, 2012
 * 
 */
public class UserRolesTable extends AbstractRelationTreeTable<Scope> {

	/** The icons of the application. */
	private final AdmIcons icons;

	/** The role service. */
	private final RoleServiceAsync roleService = Registry.get(AdmModule.ROLE_SERVICE);

	/** The asynchronous user service. */
	private final UserServiceAsync userService = Registry.get(AdmModule.USER_SERVICE);

	/** The model object. */
	private ClientUser clientUser;

	/** The {@link Window} that will be used to add roles. */
	private UserRoleWindow window;

	/** The {@link Button} that will be used to add roles. */
	private Button addButton;

	/** The {@link Button} that will be used to remove roles. */
	private Button deleteButton;

	/** The main panel where the user details are displayed on. */
	private final UserDetailPanel userDetailPanel;

	/**
	 * Default constructor.
	 * 
	 * @param userDetailPanel
	 *            The main panel where the user details are displayed on.
	 */
	public UserRolesTable(final UserDetailPanel boaamUserDetailPanel) {
		setHeading("Roles");

		this.icons = Registry.get(AdmModule.ICONS);
		this.userDetailPanel = boaamUserDetailPanel;

		setIcon(AbstractImagePrototype.create(icons.role()));

		/*
		 * Initialize the widgets.
		 */
		initializeWidgets();

		/*
		 * Check the authorization.
		 */
		checkAuthorization();
	}

	@Override
	protected void initializeWidgets() {
		addButton = new Button("Add");

		final SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(final ButtonEvent ce) {
				if (clientUser != null && clientUser.isPersistent()) {
					if (window == null) {
						window = new UserRoleWindow(clientUser, UserRolesTable.this, userDetailPanel);
					}
					else {
						window.setUser(clientUser);
					}

					window.setShim(true);
					window.setShadow(true);
					window.show();

					final Viewport viewport = Registry.get(AdmModule.VIEW_PORT);
					viewport.mask();
				}
				else {
					final String msg = "Cannot add roles to a user that isn't saved yet! Save the user first before adding roles.";

					final MessageBox box = new MessageBox();
					box.setIcon(MessageBox.WARNING);
					box.setTitle("User roles.");
					box.setMessage(msg);
					box.setButtons(MessageBox.OK);
					box.show();
				}
			}

		};
		addButton.addSelectionListener(listener);
		addButton.setIcon(AbstractImagePrototype.create(icons.add()));

		this.menuBarButtons.add(addButton);

		deleteButton = new Button("Delete selected");
		deleteButton.setIcon(AbstractImagePrototype.create(icons.delete()));

		final SelectionListener<ButtonEvent> removeListener = new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(final ButtonEvent ce) {
				final GridSelectionModel<ModelData> selectionModel = grid.getSelectionModel();
				final ModelData selectedItem = selectionModel.getSelectedItem();
				final RoleTreeItem treeItem = (RoleTreeItem) selectedItem;
				final DomainObject<Long> clientObject = treeItem.getClientObject();

				if (clientObject instanceof ClientRole) {
					final ClientRole clientRole = (ClientRole) clientObject;

					final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

						@Override
						public void onFailure(final Throwable throwable) {
							final MessageBox box = new MessageBox();
							box.setIcon(MessageBox.ERROR);
							box.setTitle("User role management.");
							box.setMessage(throwable.getMessage());
							box.setButtons(MessageBox.OK);
							box.show();
						}

						@Override
						public void onSuccess(final Void arg0) {
							final Set<UserRole> userRoles = clientUser.getUserRoles();
							final List<Scope> scopeList = ScopeListUtil.createScopeList(userRoles);

							setModel(scopeList);
						}

					};

					final Set<UserRole> userRoles = clientUser.getUserRoles();

					ClientUserRole toBeRemovedRole = null;
					for (final UserRole role : userRoles) {
						if (role.getRole().equals(clientRole)) {
							toBeRemovedRole = (ClientUserRole) role;
						}
					}
					userRoles.remove(toBeRemovedRole);

					userService.revokeUserRole(toBeRemovedRole, callback);
				}
			}

		};
		deleteButton.addSelectionListener(removeListener);

		this.menuBarButtons.add(deleteButton);

		super.initializeWidgets();
	}

	/**
	 * Check authorization.
	 */
	private void checkAuthorization() {
		final AuthorizationServiceAsync authorizationService = Registry.get(AdmModule.AUTHORIZATION_SERVICE);
		final AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(final Throwable throwable) {
				final MessageBox box = new MessageBox();
				box.setIcon(MessageBox.ERROR);
				box.setTitle("Add roles.");
				box.setMessage(throwable.getMessage());
				box.setButtons(MessageBox.OK);
				box.show();
			}

			@Override
			public void onSuccess(final Boolean authorized) {
				if (authorized) {
					addButton.setEnabled(true);
					deleteButton.setEnabled(true);
				}
				else {
					addButton.setEnabled(false);
					deleteButton.setEnabled(false);
				}
			}

		};
		authorizationService.isLoggedInUserAuthorized("GRANT_ROLE", callback);
	}

	@Override
	protected List<ColumnConfig> createColumns() {
		final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

		ColumnConfig column = new ColumnConfig();
		column.setId("name");
		column.setHeader("Name");
		column.setWidth(200);
		column.setSortable(true);
		column.setRenderer(new TreeGridCellRenderer<BeanModel>());
		configs.add(column);

		// Use a subclassed CheckColumnConfig to prevent the rendering of the
		// checkbox for Scope items
		column = new CheckColumnConfig() {

			@Override
			protected String onRender(final ModelData model, final String property, final ColumnData config, final int rowIndex, final int colIndex, final ListStore<ModelData> store) {
				if (model.get(property) == null) {
					return "";
				}
				return super.onRender(model, property, config, rowIndex, colIndex, store);
			}
		};
		column.setId("valid");
		column.setHeader("Valid");
		column.setWidth(35);
		column.setSortable(true);
		configs.add(column);

		return configs;
	}

	@Override
	protected boolean isParent(final ModelData data) {
		return data instanceof RoleTreeItem && ((RoleTreeItem) data).isParent();
	}

	@Override
	protected RpcProxy<List<ModelData>> getProxy() {
		// Data proxy
		final RpcProxy<List<ModelData>> proxy = new RpcProxy<List<ModelData>>() {

			@Override
			protected void load(final Object loadConfig, final AsyncCallback<List<ModelData>> callback) {
				roleService.getRoleChildren((RoleTreeItem) loadConfig, model, callback);
			}
		};
		return proxy;
	}

	public List<ClientScope> getManagedScopes() {

		final List<Scope> models = getModel();

		final List<ClientScope> scopes = new ArrayList<ClientScope>();

		for (final Scope scope : models) {
			scopes.add((ClientScope) scope);
		}

		return scopes;
	}

	/**
	 * @param clientUser
	 *            the clientUser to set
	 */
	public void setClientUser(final ClientUser clientUser) {
		this.clientUser = clientUser;
	}
}
