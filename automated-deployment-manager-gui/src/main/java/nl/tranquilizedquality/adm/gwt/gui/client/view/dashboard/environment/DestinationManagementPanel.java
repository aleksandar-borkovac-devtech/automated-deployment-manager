/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 11 sep. 2011 File: DestinationManagementPanel.java
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

import nl.tranquilizedquality.adm.commons.business.domain.Protocol;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.converter.EnumConverter;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.converter.EnumWrapper;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractGridPanel;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractSearchPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientDestinationSearchCommand;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Management panel where you can search for destinations.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 11 sep. 2011
 */
public class DestinationManagementPanel extends AbstractSearchPanel<ClientDestinationSearchCommand> {

	/** The icons of the application. */
	private final AdmIcons icons;

	/** The name to search on. */
	private TextField<String> name;

	/** The {@link ComboBox} containing a list of protocols. */
	private ComboBox<EnumWrapper<Protocol>> protocol;

	/**
	 * Default constructor.
	 */
	public DestinationManagementPanel() {
		icons = Registry.get(AdmModule.ICONS);

		sc = new ClientDestinationSearchCommand();

		northHeight = 170;

		initializeWidgets();
	}

	@Override
	protected AbstractGridPanel createGridPanel() {
		return new DestinationTable(sc);
	}

	@Override
	protected FormPanel createFilterPanel() {
		final FormPanel formPanel = new FormPanel();
		formPanel.setHeading("Search criteria");
		formPanel.setFrame(true);
		formPanel.setLabelWidth(200);
		formPanel.setButtonAlign(HorizontalAlignment.LEFT);
		formPanel.setIcon(AbstractImagePrototype.create(icons.destinationManagement()));
		formPanel.setHeight(160);

		/*
		 * Add name.
		 */
		name = new TextField<String>();
		name.setId("destination-management-host-name");
		name.setName("hostName");
		name.setFieldLabel("Host Name");
		name.setAllowBlank(true);
		formPanel.add(name);

		/*
		 * Add environment name.
		 */
		name = new TextField<String>();
		name.setId("destination-management-environment-name");
		name.setName("environment");
		name.setFieldLabel("Environment Name");
		name.setAllowBlank(true);
		formPanel.add(name);

		/*
		 * Add protocol.
		 */
		protocol = new ComboBox<EnumWrapper<Protocol>>();
		protocol.setId("destination-management-protocol");
		protocol.setName("protocol");
		protocol.setFieldLabel("Protocol");
		protocol.setDisplayField("value");
		protocol.setEmptyText("Select protocol..");
		protocol.setTriggerAction(TriggerAction.ALL);
		protocol.setForceSelection(true);
		protocol.setEditable(false);

		/*
		 * Add enums.
		 */
		final ListStore<EnumWrapper<Protocol>> store = new ListStore<EnumWrapper<Protocol>>();
		final Protocol[] values = Protocol.values();
		store.add(new EnumWrapper<Protocol>(null));
		for (final Protocol protocol : values) {
			final EnumWrapper<Protocol> wrapper = new EnumWrapper<Protocol>(protocol);
			store.add(wrapper);
		}

		protocol.setStore(store);

		formPanel.add(protocol);

		/*
		 * Add search button.
		 */
		final Button search = new Button("Search");
		search.setIcon(AbstractImagePrototype.create(icons.find()));

		final SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(final ButtonEvent ce) {
				for (final FieldBinding fieldBinding : fieldBindings) {
					fieldBinding.updateModel();
				}

				search();
			}

		};
		search.addSelectionListener(listener);

		formPanel.addButton(search);

		return formPanel;
	}

	@Override
	protected void initializeWidgets() {
		/*
		 * Setup layout.
		 */
		final BorderLayout layout = new BorderLayout();
		setLayout(layout);

		/*
		 * Create the filter panel.
		 */
		formPanel = createFilterPanel();

		/*
		 * Create grid panel.
		 */
		gridPanel = createGridPanel();

		final FieldBinding binding = new FieldBinding(this.protocol, "protocol");
		binding.setConverter(new EnumConverter<Protocol>());
		fieldBindings.add(binding);

		/*
		 * Bind model.
		 */
		bindModel(this.sc);

		/*
		 * Setup layout.
		 */
		final BorderLayoutData northData = new BorderLayoutData(LayoutRegion.NORTH);
		northData.setCollapsible(true);
		northData.setFloatable(true);
		northData.setSplit(true);
		northData.setMaxSize(northMaxHeight);
		northData.setSize(northHeight);
		northData.setSplit(true);
		northData.setMargins(new Margins(1, 1, 1, 1));

		final BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);
		centerData.setMargins(new Margins(0, 1, 1, 1));
		centerData.setSplit(true);

		add(formPanel, northData);
		add(gridPanel, centerData);
	}

}
