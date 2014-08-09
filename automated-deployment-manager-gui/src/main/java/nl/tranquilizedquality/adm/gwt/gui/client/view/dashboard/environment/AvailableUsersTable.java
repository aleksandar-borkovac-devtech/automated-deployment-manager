/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: Aug 24, 2012 File: fAvailableUsersTable.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.view.dashboard.security
 * 
 * Copyright (c) 2012 Tranquilized Quality www.Tranquilized Quality.nl All rights
 * reserved.
 * 
 * This software is the confidential and proprietary information of Tranquilized Quality
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Tranquilized Quality.
 * </pre>
 */
package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.environment;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractGridPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientEnvironment;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUser;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUserSearchCommand;
import nl.tranquilizedquality.adm.gwt.gui.client.service.user.UserServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.usergroup.UserGroupTable;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.RowNumberer;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Grid where all available users are displayed.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 24, 2012
 */
class AvailableUsersTable extends AbstractGridPanel {

	/** The icons of the application. */
	private final AdmIcons icons;

	/** The search criteria. */
	private final ClientUserSearchCommand sc;

	/** The repository service. */
	private UserServiceAsync userService;

	/** Button to add the selected user to a {@link Environment}. */
	private Button addButton;

	/** Panel where the environment details are displayed on. */
	private final EnvironmentDetailsPanel detailsPanel;

	/**
	 * Constructor that takes the search criteria to filter on.
	 * 
	 * @param sc
	 *            The search criteria.
	 */
	public AvailableUsersTable(final ClientUserSearchCommand sc, final EnvironmentDetailsPanel detailsPanel) {
		setHeading("Available Users");
		this.icons = Registry.get(AdmModule.ICONS);
		this.sc = sc;
		this.detailsPanel = detailsPanel;
		setIcon(AbstractImagePrototype.create(icons.userGroup()));
		initializeWidgets();
	}

	@Override
	protected void initializeWidgets() {
		userService = Registry.get(AdmModule.USER_SERVICE);

		proxy = new RpcProxy<PagingLoadResult<ClientUser>>() {

			@Override
			public void load(final Object loadConfig, final AsyncCallback<PagingLoadResult<ClientUser>> callback) {
				if (AvailableUsersTable.this.sc != null) {
					final ClientEnvironment environment = detailsPanel.getEnvironment();
					final List<ClientUser> filter = new ArrayList<ClientUser>();
					if (environment != null) {
						final List<User> users = environment.getUsers();
						for (final User user : users) {
							filter.add((ClientUser) user);
						}
					}

					userService.findUsers((PagingLoadConfig) loadConfig, AvailableUsersTable.this.sc, filter, callback);

					sc.setInitSearch(false);
				}
			}
		};

		this.panelStateId = UserGroupTable.class.getName();

		addButton = new Button("Add User as deployer");
		addButton.setIcon(AbstractImagePrototype.create(icons.addUserToGroup()));
		final SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(final ButtonEvent ce) {
				final ClientEnvironment environment = detailsPanel.getEnvironment();
				final List<User> users = environment.getUsers();

				final GridSelectionModel<BeanModel> selectionModel = grid.getSelectionModel();
				final List<BeanModel> selectedItems = selectionModel.getSelectedItems();

				if (!selectedItems.isEmpty()) {
					for (final BeanModel beanModel : selectedItems) {
						final ClientUser user = beanModel.getBean();
						users.add(user);
					}

					detailsPanel.saveEnvironment();

					refresh();
				}
			}
		};
		addButton.addSelectionListener(listener);

		menuBarButtons.add(addButton);

		super.initializeWidgets();
	}

	/**
	 * Refreshes the content of the table.
	 */
	public void refreshTable() {
		super.refresh();
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
		column.setId("userName");
		column.setHeader("Username");
		column.setWidth(100);
		column.setSortable(true);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("created");
		column.setHeader("Created");
		column.setWidth(60);
		column.setSortable(true);
		column.setDateTimeFormat(DateTimeFormat.getShortDateTimeFormat());
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
		column.setDateTimeFormat(DateTimeFormat.getShortDateTimeFormat());
		configs.add(column);

		column = new ColumnConfig();
		column.setId("alteredBy");
		column.setHeader("Altered By");
		column.setWidth(60);
		column.setSortable(false);
		configs.add(column);

		return configs;
	}

	public void initialize() {
		final ClientEnvironment environment = detailsPanel.getEnvironment();
		if (environment.isPersistent()) {
			addButton.enable();
		}
		else {
			addButton.disable();
		}
	}

}
