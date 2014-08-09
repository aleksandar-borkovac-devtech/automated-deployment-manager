package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.Privilege;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.security.AuthorizationServiceAsync;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractRelationListTable;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.privilege.ClientPrivilege;
import nl.tranquilizedquality.adm.gwt.gui.client.model.role.ClientRole;
import nl.tranquilizedquality.adm.gwt.gui.client.service.privilege.PrivilegeServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.service.role.RoleServiceAsync;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.CheckColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * List containing the privileges from a specific role.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 26, 2012
 * 
 */
public class RolePrivilegesTable extends AbstractRelationListTable<Privilege, ClientPrivilege> {

	/** The icons of the application. */
	private final AdmIcons icons;

	/** The {@link Window} that will be used to add privileges. */
	private RolePrivilegeWindow window;

	/** The asynchronous role service. */
	private final RoleServiceAsync roleService;

	/** The model object. */
	private ClientRole clientRole;

	/** Remote privilege service. */
	private final PrivilegeServiceAsync privilegeService;

	/** The button to be able to add a privilege to a role. */
	private Button addButton;

	/** The button to be able to delete a privilege from a role. */
	private Button deleteButton;

	/**
	 * Default constructor.
	 */
	public RolePrivilegesTable() {
		setHeading("Privileges");

		privilegeService = Registry.get(AdmModule.PRIVILEGE_SERVICE);
		roleService = Registry.get(AdmModule.ROLE_SERVICE);
		icons = Registry.get(AdmModule.ICONS);

		setIcon(AbstractImagePrototype.create(icons.privilege()));

		initializeWidgets();

		/*
		 * Perform the privilege checks.
		 */
		performPrivilegeCheck();
	}

	/**
	 * Performs the privilege checks need to secure this widget.
	 */
	private void performPrivilegeCheck() {
		/*
		 * Retrieve the authorization service to perform privilege checks with.
		 */
		final AuthorizationServiceAsync authorizationService = Registry.get(AdmModule.AUTHORIZATION_SERVICE);

		/*
		 * Check if the user is allowed to edit a frozen role. If not the add
		 * and remove button will be disabled so the user can't edit the role.
		 */
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

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
					addButton.setEnabled(true);
					deleteButton.setEnabled(true);
				}
				else {
					addButton.setEnabled(false);
					deleteButton.setEnabled(false);
				}
			}

		};
		authorizationService.isLoggedInUserAuthorized("EDIT_FROZEN_ROLE", callback);

		/*
		 * Check if the user is allowed to add a role or not. If not the add and
		 * delete buttons are disabled.
		 */
		callback = new AsyncCallback<Boolean>() {

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
	protected void initializeWidgets() {
		/*
		 * Add the add button.
		 */
		addButton = new Button("Add");
		addButton.setIcon(AbstractImagePrototype.create(icons.add()));

		final SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(final ButtonEvent ce) {
				if (clientRole.isPersistent()) {
					if (window == null) {
						window = new RolePrivilegeWindow(clientRole, RolePrivilegesTable.this);
					}
					else {
						window.setRole(clientRole);
					}

					window.setShim(true);
					window.setShadow(true);
					window.show();

					final Viewport viewport = Registry.get(AdmModule.VIEW_PORT);
					viewport.mask();
				}
				else {
					final String msg = "You can't assign privileges to a role that isn't saved yet. Save the privilege first and than try again.";

					final MessageBox box = new MessageBox();
					box.setIcon(MessageBox.WARNING);
					box.setTitle("Privilege management.");
					box.setMessage(msg);
					box.setButtons(MessageBox.OK);
					box.show();
				}
			}

		};
		addButton.addSelectionListener(listener);

		this.menuBarButtons.add(addButton);

		/*
		 * Add the delete button.
		 */
		deleteButton = new Button("Delete selected");
		deleteButton.setIcon(AbstractImagePrototype.create(icons.delete()));

		final SelectionListener<ButtonEvent> removeListener = new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(final ButtonEvent ce) {
				final GridSelectionModel<BeanModel> selectionModel = grid.getSelectionModel();
				final BeanModel selectedItem = selectionModel.getSelectedItem();

				if (selectedItem == null) {
					return;
				}

				final ClientPrivilege clientPrivilege = selectedItem.getBean();

				final AsyncCallback<ClientPrivilege> callback = new AsyncCallback<ClientPrivilege>() {

					@Override
					public void onFailure(final Throwable throwable) {
						final MessageBox box = new MessageBox();
						box.setIcon(MessageBox.ERROR);
						box.setTitle("Privilege management.");
						box.setMessage(throwable.getMessage());
						box.setButtons(MessageBox.OK);
						box.show();
					}

					@Override
					public void onSuccess(final ClientPrivilege clientPrivilege) {
						clientRole.removePrivilege(clientPrivilege);

						final AsyncCallback<ClientRole> saveCallBack = new AsyncCallback<ClientRole>() {

							@Override
							public void onFailure(final Throwable throwable) {
								final MessageBox box = new MessageBox();
								box.setIcon(MessageBox.ERROR);
								box.setTitle("Privilege management.");
								box.setMessage(throwable.getMessage());
								box.setButtons(MessageBox.OK);
								box.show();
							}

							@Override
							public void onSuccess(final ClientRole clientRole) {
								final ListStore<BeanModel> store = grid.getStore();
								store.remove(selectedItem);

								final MessageBox box = new MessageBox();
								box.setIcon(MessageBox.INFO);
								box.setTitle("Privilege management.");
								box.setMessage("Successfully removed a privilege.");
								box.setButtons(MessageBox.OK);
								box.show();
							}

						};
						roleService.saveRole(clientRole, saveCallBack);
					}

				};

				privilegeService.findPrivilegeById(clientPrivilege.getId(), callback);
			}

		};
		deleteButton.addSelectionListener(removeListener);

		this.menuBarButtons.add(deleteButton);

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

		column = new CheckColumnConfig();
		column.setId("valid");
		column.setHeader("Valid");
		column.setWidth(50);
		column.setSortable(true);
		configs.add(column);

		return configs;
	}

	@Override
	protected Class<ClientPrivilege> getBeanModelClass() {
		return ClientPrivilege.class;
	}

	@Override
	protected String getPanelStateId() {
		return RolePrivilegesTable.class.getName();
	}

	public List<ClientPrivilege> getRolePrivileges() {
		final ListStore<BeanModel> store = grid.getStore();
		final List<BeanModel> models = store.getModels();
		final List<ClientPrivilege> privileges = new ArrayList<ClientPrivilege>();

		for (final BeanModel beanModel : models) {
			final ClientPrivilege privilege = beanModel.getBean();

			privileges.add(privilege);
		}

		return privileges;
	}

	public void setRole(final ClientRole role) {
		this.clientRole = role;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.Tranquilized Quality.boaam.gwt.gui.client.view.dashboard.AbstractRelationListTable
	 * #setModel(java.util.Collection)
	 */
	@Override
	public void setModel(final Collection<Privilege> model) {
		super.setModel(model);

		performPrivilegeCheck();
	}

}
