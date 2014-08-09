package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.user.role;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import nl.tranquilizedquality.adm.commons.business.domain.Scope;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.business.domain.UserRole;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.converter.BeanModelConverter;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.AbstractServiceException;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractGridPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.role.ClientRole;
import nl.tranquilizedquality.adm.gwt.gui.client.model.role.ClientRoleSearchCommand;
import nl.tranquilizedquality.adm.gwt.gui.client.model.role.ClientUserRole;
import nl.tranquilizedquality.adm.gwt.gui.client.model.scope.ClientScope;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUser;
import nl.tranquilizedquality.adm.gwt.gui.client.service.role.RoleServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.service.user.UserServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.service.user.UserServiceException;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.user.ScopeListUtil;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.data.ModelData;
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
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Implementation of an {@link AbstractGridPanel} where the available roles are
 * being presented in a {@link Grid} filtered by {@link Scope}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 26, 2012
 */
public class UserRolePanel extends AbstractGridPanel {

	/** The icons of the application. */
	private final AdmIcons icons;

	/** Panel where the scope filter will be put on. */
	private FormPanel formPanel;

	/** The model object. */
	private ClientUser clientUser;

	/** Filter list. */
	private List<ClientScope> filterScopes;

	/** A {@link Window} where this panel is displayed in if applicable. */
	private Window window;

	/** The asynchronous user service. */
	private final UserServiceAsync userService;

	/** The asynchronous role service. */
	private final RoleServiceAsync roleService;

	/** The search criteria used to search for roles. */
	private final ClientRoleSearchCommand sc;

	/** {@link FormBinding} object used to bind a form to a bean. */
	private FormBinding binding;

	/** {@link ComboBox} containing a list of scopes. */
	private ComboBox<ModelData> scopes;

	/**
	 * Constructor that takes the {@link ClientUser} where the roles are being
	 * managed for and a list of {@link ClientScope} objects where the user is
	 * scope manager from.
	 * 
	 * @param clientUser
	 *            The {@link ClientUser} where the roles are being managed.
	 * @param filterScopes
	 *            The filter list.
	 */
	public UserRolePanel(final ClientUser clientUser, final List<ClientScope> filterScopes) {
		setLayout(new BorderLayout());
		setHeading("Roles");

		this.clientUser = clientUser;
		this.filterScopes = filterScopes;

		this.userService = Registry.get(AdmModule.USER_SERVICE);
		this.roleService = Registry.get(AdmModule.ROLE_SERVICE);
		this.icons = Registry.get(AdmModule.ICONS);

		sc = new ClientRoleSearchCommand();
		sc.setUser(clientUser);

		if (!filterScopes.isEmpty()) {
			sc.setScope(this.filterScopes.get(0));
		}

		this.proxy = new RpcProxy<PagingLoadResult<ClientRole>>() {

			@Override
			public void load(final Object loadConfig, final AsyncCallback<PagingLoadResult<ClientRole>> callback) {
				if (sc.getScope() != null) {
					roleService.findGrantableRoles((PagingLoadConfig) loadConfig, sc, callback);
				}
			}
		};

		initializeWidgets();
	}

	@Override
	protected void initializeWidgets() {
		/*
		 * Add add button.
		 */
		final Button addUserRoleButton = new Button("Add role to user");
		addUserRoleButton.setIcon(AbstractImagePrototype.create(icons.add()));

		final SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(final ButtonEvent ce) {
				final GridSelectionModel<BeanModel> selectionModel = grid.getSelectionModel();
				final List<BeanModel> selectedItems = selectionModel.getSelectedItems();

				for (final BeanModel beanModel : selectedItems) {
					final ClientRole role = beanModel.getBean();

					final ClientUserRole userRole = new ClientUserRole();
					userRole.setActive(true);
					userRole.setActiveFrom(new Date());
					userRole.setCreated(new Date());
					userRole.setUser(clientUser);
					userRole.setRole(role);

					final AsyncCallback<ClientUserRole> callback = new AsyncCallback<ClientUserRole>() {

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
							box.setTitle("User role management.");
							box.setMessage(builder.toString());
							box.setButtons(MessageBox.OK);
							box.show();
						}

						@Override
						public void onSuccess(final ClientUserRole clientUserRole) {
							clientUser.getUserRoles().add(clientUserRole);

							final MessageBox box = new MessageBox();
							box.setIcon(MessageBox.INFO);
							box.setTitle("User role management.");
							box.setMessage("Successfully added a user role.");
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

					// save the user role.
					userService.saveUserRole(userRole, callback);
				}

			}

		};
		addUserRoleButton.addSelectionListener(listener);

		menuBarButtons.add(addUserRoleButton);

		super.initializeWidgets();

		/*
		 * Since there is another kind of layout we need to remove the grid and
		 * add it again with some layout data.
		 */
		remove(grid);

		final BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);

		add(grid, centerData);
	}

	/**
	 * Binds the bean model to the form.
	 * 
	 * @param sc
	 *            The {@link ClientRoleSearchCommand} object to bind to.
	 */
	protected void bindModel(final ClientRoleSearchCommand sc) {
		if (binding == null) {
			binding = new FormBinding(this.formPanel);
		}
		else {
			binding.unbind();
		}

		/*
		 * Create the BeanModel.
		 */
		final BeanModel model = createBindModel(sc);

		/*
		 * Add binding for Scope using a bean model converter.
		 */
		final FieldBinding fieldBinding = new FieldBinding(this.scopes, "scope");
		fieldBinding.setConverter(new BeanModelConverter());

		binding.addFieldBinding(fieldBinding);

		binding.bind(model);

		binding.autoBind();
	}

	/**
	 * Create the {@link BeanModel} of the {@link User} object.
	 * 
	 * @param user
	 *            the user object.
	 * @return the resulting model object.
	 */
	protected BeanModel createBindModel(final ClientRoleSearchCommand sc) {
		final BeanModelFactory factory = BeanModelLookup.get().getFactory(ClientRoleSearchCommand.class);
		final BeanModel model = factory.createModel(sc);

		return model;
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

	@Override
	protected void handleDoubleClick() {

	}

	/**
	 * @param clientUser
	 *            the clientUser to set
	 */
	public void setClientUser(final ClientUser clientUser) {
		this.clientUser = clientUser;
	}

	/**
	 * @param window
	 *            the window to set
	 */
	public void setWindow(final Window window) {
		this.window = window;
	}

	/**
	 * @param filterScopes
	 *            the filterScopes to set
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setFilterScopes(final List<ClientScope> filterScopes) {
		this.filterScopes = filterScopes;

		if (clientUser != null && sc != null) {
			final Set<UserRole> userRoles = clientUser.getUserRoles();
			final List<Scope> scopeList = ScopeListUtil.createScopeList(userRoles);
			for (final Scope updatedScope : scopeList) {
				if (updatedScope.equals(sc.getScope())) {
					sc.setExcludedRoles(new ArrayList(updatedScope.getRoles()));
					break;
				}
			}
		}
	}
}
