package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.scope;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import nl.tranquilizedquality.adm.commons.business.domain.Privilege;
import nl.tranquilizedquality.adm.commons.business.domain.Role;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.security.AuthorizationServiceAsync;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractRelationTreeTable;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.controller.navigation.AdmNavigationController;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.navigation.RoleTreeItem;
import nl.tranquilizedquality.adm.gwt.gui.client.model.role.ClientRole;
import nl.tranquilizedquality.adm.gwt.gui.client.model.scope.ClientScope;
import nl.tranquilizedquality.adm.gwt.gui.client.service.role.RoleServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.service.role.RoleServiceException;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.AdmTabs;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.TreeGridEvent;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.CheckColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGridCellRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Panel for managing relationship with {@link Role} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 26, 2012
 * 
 */
public class ScopeRolesTable extends AbstractRelationTreeTable<Role> {

	/** The icons of the application. */
	private final AdmIcons icons;

	/** Remote service. */
	private final RoleServiceAsync roleService = Registry.get(AdmModule.ROLE_SERVICE);

	/** The {@link ClientScope} where the roles belong to. */
	private ClientScope scope;

	/** The {@link Button} used to add roles to a scope. */
	private Button addButton;

	private Button deleteButton;

	/**
	 * Default constructor.
	 */
	public ScopeRolesTable() {
		/*
		 * Get the application icons.
		 */
		this.icons = Registry.get(AdmModule.ICONS);

		setIcon(AbstractImagePrototype.create(this.icons.role()));

		/*
		 * Initialize the widgets.
		 */
		initializeWidgets();

		/*
		 * Check authorization.
		 */
		checkAuthorization();

		setHeading("Roles");
	}

	@Override
	protected void initializeWidgets() {
		/*
		 * Add the add button.
		 */
		addButton = new Button();
		addButton.setText("Add");
		addButton.setIcon(AbstractImagePrototype.create(icons.add()));

		final SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(final ButtonEvent ce) {
				final AdmNavigationController controller = Registry.get(AdmModule.NAVIGATION_CONTROLLER);

				final ClientRole clientRole = new ClientRole();
				clientRole.setScope(scope);

				controller.selectTab(AdmTabs.ROLE_DETAIL_TAB, clientRole);
			}

		};
		addButton.addSelectionListener(listener);

		menuBarButtons.add(addButton);

		/*
		 * Add the delete button.
		 */
		deleteButton = new Button("Delete selected");
		deleteButton.setIcon(AbstractImagePrototype.create(icons.delete()));

		final SelectionListener<ButtonEvent> removeListener = new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(final ButtonEvent ce) {
				final RoleServiceAsync roleService = Registry.get(AdmModule.ROLE_SERVICE);
				final GridSelectionModel<ModelData> selectionModel = grid.getSelectionModel();
				final ModelData selectedItem = selectionModel.getSelectedItem();
				final RoleTreeItem treeItem = (RoleTreeItem) selectedItem;
				final DomainObject<Long> clientObject = treeItem.getClientObject();

				if (clientObject instanceof ClientRole) {
					final ClientRole clientRole = (ClientRole) clientObject;

					final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

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
							box.setTitle("Scope management.");
							box.setMessage(builder.toString());
							box.setButtons(MessageBox.OK);
							box.show();
						}

						@Override
						public void onSuccess(final Void arg0) {
							/*
							 * Remove the role.
							 */
							scope.removeRole(clientRole);

							/*
							 * Retrieve the remaining roles and set the model.
							 */
							final Set<Role> roles = scope.getRoles();

							setModel(new ArrayList<Role>(roles));
						}

					};

					roleService.deleteRole(clientRole, callback);
				}
			}

		};
		deleteButton.addSelectionListener(removeListener);

		this.menuBarButtons.add(deleteButton);

		super.initializeWidgets();
	}

	/**
	 * Do some authorization checks.
	 */
	private void checkAuthorization() {
		/*
		 * Check if the user is allowed to add roles.
		 */
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
		authorizationService.isLoggedInUserAuthorized("ADD_ROLE", callback);
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

		column = new CheckColumnConfig();
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

	@Override
	protected void handleDoubleClick(final TreeGridEvent<ModelData> be) {
		final TreeGrid<ModelData> treeGrid = be.getTreeGrid();

		final GridSelectionModel<ModelData> selectionModel = treeGrid.getSelectionModel();
		final RoleTreeItem treeItem = (RoleTreeItem) selectionModel.getSelectedItem();
		if (treeItem != null) {
			final DomainObject<Long> clientObject = treeItem.getClientObject();

			if (clientObject != null && clientObject instanceof Role) {
				final Role role = (Role) clientObject;

				final AdmNavigationController controller = Registry.get(AdmModule.NAVIGATION_CONTROLLER);

				controller.selectTab(AdmTabs.ROLE_DETAIL_TAB, role);
			}
			else if (clientObject != null && clientObject instanceof Privilege) {
				final Privilege privilege = (Privilege) clientObject;

				final AdmNavigationController controller = Registry.get(AdmModule.NAVIGATION_CONTROLLER);

				controller.selectTab(AdmTabs.PRIVILEGE_DETAIL_TAB, privilege);
			}
		}
	}

	/**
	 * @param scope
	 *            the scope to set
	 */
	public void setScope(final ClientScope scope) {
		this.scope = scope;
	}

}
