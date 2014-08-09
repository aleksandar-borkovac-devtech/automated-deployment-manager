package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.scope;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.Scope;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.security.AuthorizationServiceAsync;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractGridPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.controller.navigation.AdmNavigationController;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.scope.ClientScope;
import nl.tranquilizedquality.adm.gwt.gui.client.model.scope.ClientScopeSearchCommand;
import nl.tranquilizedquality.adm.gwt.gui.client.service.scope.ScopeServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.AdmTabs;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.RowNumberer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Management panel for managing {@link Scope} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 26, 2012
 * 
 */
public class ScopeTable extends AbstractGridPanel {

	/** The icons of the application. */
	private final AdmIcons icons;

	/** Search criteria bean. */
	private final ClientScopeSearchCommand sc;

	/** Remote service. */
	private ScopeServiceAsync scopeService;

	/** Import window */
	private Window importWindow;

	/** The button where you can import a scope with. */
	private Button importButton;

	/**
	 * Default constructor.
	 */
	public ScopeTable(final ClientScopeSearchCommand sc) {
		setHeading("Scopes");

		this.sc = sc;
		this.icons = Registry.get(AdmModule.ICONS);
		setIcon(AbstractImagePrototype.create(icons.managedScopes()));

		/*
		 * Setup the menu bars.
		 */
		setupMenuBarButtons();

		/*
		 * Initialize the widgets.
		 */
		initializeWidgets();

		/*
		 * Check logged in user privileges.
		 */
		performPrivilegeCheck();
	}

	/**
	 * Checks the privileges of the logged in user to enable and disable certain
	 * GUI functionality.
	 */
	protected void performPrivilegeCheck() {
		final AuthorizationServiceAsync authorizationService = Registry.get(AdmModule.AUTHORIZATION_SERVICE);

		final AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(final Throwable throwable) {
				final MessageBox box = new MessageBox();
				box.setIcon(MessageBox.ERROR);
				box.setTitle("Create organization check.");
				box.setMessage(throwable.getMessage());
				box.setButtons(MessageBox.OK);
				box.show();
			}

			@Override
			public void onSuccess(final Boolean authorized) {
				if (authorized) {
					importButton.enable();
				}
				else {
					importButton.disable();
				}
			}

		};
		authorizationService.isLoggedInUserAuthorized("IMPORT_SCOPE", callback);
	}

	/**
	 * Creates the appropriate menu bars.
	 */
	private void setupMenuBarButtons() {
		importButton = new Button("Import scope");
		final SelectionListener<ButtonEvent> selectionListener = new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(final ButtonEvent ce) {
				if (importWindow == null) {
					importWindow = new ScopeImportWindow(ScopeTable.this);
				}

				importWindow.show();
			}
		};

		importButton.addSelectionListener(selectionListener);

		menuBarButtons.add(importButton);
	}

	public void refreshTable() {
		super.refresh();
	}

	@Override
	protected void initializeWidgets() {
		scopeService = Registry.get(AdmModule.SCOPE_SERVICE);

		proxy = new RpcProxy<PagingLoadResult<ClientScope>>() {

			@Override
			public void load(final Object loadConfig, final AsyncCallback<PagingLoadResult<ClientScope>> callback) {
				scopeService.findScopes((PagingLoadConfig) loadConfig, sc, callback);
			}
		};

		this.panelStateId = ScopeTable.class.getName();

		super.initializeWidgets();
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
		column.setId("description");
		column.setHeader("Description");
		column.setWidth(200);
		column.setSortable(true);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("created");
		column.setHeader("Created");
		column.setWidth(100);
		column.setSortable(true);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("createdBy");
		column.setHeader("Created By");
		column.setWidth(60);
		column.setSortable(false);
		configs.add(column);

		return configs;
	}

	protected void view() {
		final GridSelectionModel<BeanModel> selectionModel = grid.getSelectionModel();
		final List<BeanModel> selectedItems = selectionModel.getSelectedItems();

		if (selectedItems.size() == 1) {
			final BeanModel beanModel = selectedItems.get(0);
			final ClientScope scope = beanModel.getBean();
			final AdmNavigationController controller = Registry.get(AdmModule.NAVIGATION_CONTROLLER);
			controller.selectTab(AdmTabs.SCOPE_DETAIL_TAB, scope);
		}
		else if (selectedItems.size() > 1) {
			selectionModel.deselect(selectedItems);
		}
	}

	@Override
	protected void handleDoubleClick() {
		view();
	}
}
