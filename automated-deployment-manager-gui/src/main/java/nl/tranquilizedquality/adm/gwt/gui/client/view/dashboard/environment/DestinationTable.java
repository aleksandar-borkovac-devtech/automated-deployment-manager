/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 11 sep. 2011 File: DestinationTable.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.view.dashboard.environment
 * 
 * Copyright (c) 2011 Tranquilized Quality www.Tranquilized Quality.nl All rights
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

import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractGridPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.controller.navigation.AdmNavigationController;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientDestination;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientDestinationSearchCommand;
import nl.tranquilizedquality.adm.gwt.gui.client.service.environment.EnvironmentServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.AdmTabs;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.repository.RepositoryTable;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.RowNumberer;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Grid where {@link Destination} objects are displayed in.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 11 sep. 2011
 */
public class DestinationTable extends AbstractGridPanel {

	/** The icons of the application. */
	private final AdmIcons icons;

	/** The search criteria. */
	private final ClientDestinationSearchCommand sc;

	/** The repository service. */
	private EnvironmentServiceAsync environmentService;

	/** The add button. */
	private Button addButton;

	/** The edit menu item. */
	private MenuItem editMenuItem;

	/**
	 * Constructor that takes the search criteria to filter on.
	 * 
	 * @param sc
	 *            The search criteria.
	 */
	public DestinationTable(final ClientDestinationSearchCommand sc) {
		setHeading("Destinations");

		this.sc = sc;

		this.icons = Registry.get(AdmModule.ICONS);

		initializeWidgets();
	}

	@Override
	protected void initializeWidgets() {
		environmentService = Registry.get(AdmModule.ENVIRONMENT_SERVICE);

		proxy = new RpcProxy<PagingLoadResult<ClientDestination>>() {

			@Override
			public void load(final Object loadConfig, final AsyncCallback<PagingLoadResult<ClientDestination>> callback) {
				environmentService.findDestinations((PagingLoadConfig) loadConfig, DestinationTable.this.sc, callback);
			}
		};

		this.panelStateId = RepositoryTable.class.getName();

		addButton = new Button("Add");
		addButton.setIcon(AbstractImagePrototype.create(icons.destination()));

		final SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(final ButtonEvent ce) {
				add();
			}

		};
		addButton.addSelectionListener(listener);

		this.menuBarButtons.add(addButton);

		editMenuItem = new MenuItem();
		editMenuItem.setIcon(AbstractImagePrototype.create(icons.edit()));
		editMenuItem.setText("Edit");
		editMenuItem.addSelectionListener(new SelectionListener<MenuEvent>() {

			@Override
			public void componentSelected(final MenuEvent ce) {
				view();
			}
		});
		menuItems.add(editMenuItem);

		super.initializeWidgets();
	}

	/**
	 * Refreshes the content of the table.
	 */
	public void refreshTable() {
		super.refresh();
	}

	/**
	 * Creates a fresh {@link ClientDestination} and navigates to the details
	 * panel.
	 */
	private void add() {
		final ClientDestination destination = new ClientDestination();
		final AdmNavigationController controller = Registry.get(AdmModule.NAVIGATION_CONTROLLER);
		controller.selectTab(AdmTabs.DESTINATION_DETAILS_TAB, destination);
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
		column.setId("hostName");
		column.setHeader("Host");
		column.setWidth(100);
		column.setSortable(true);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("environmentName");
		column.setHeader("Environment");
		column.setWidth(60);
		column.setSortable(false);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("protocol");
		column.setHeader("Protocol");
		column.setWidth(100);
		column.setSortable(true);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("deployerId");
		column.setHeader("Deployer");
		column.setWidth(100);
		column.setSortable(true);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("created");
		column.setHeader("Created");
		column.setWidth(100);
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
		column.setWidth(100);
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

	/**
	 * Retrieves the selected item and displays it in the repository details
	 * screen.
	 */
	private void view() {
		final GridSelectionModel<BeanModel> selectionModel = grid.getSelectionModel();
		final BeanModel selectedItem = selectionModel.getSelectedItem();

		if (selectedItem != null) {
			final ClientDestination destination = selectedItem.getBean();
			final AdmNavigationController controller = Registry.get(AdmModule.NAVIGATION_CONTROLLER);
			controller.selectTab(AdmTabs.DESTINATION_DETAILS_TAB, destination);
		}
	}

	@Override
	protected void handleDoubleClick() {
		view();
	}

}
