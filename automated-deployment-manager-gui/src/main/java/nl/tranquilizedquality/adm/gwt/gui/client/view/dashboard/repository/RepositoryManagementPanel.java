/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 30 aug. 2011 File: RepositoryManagementPanel.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.view.dashboard.repository
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
package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.repository;

import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.converter.SimpleComboValueConverter;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractGridPanel;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractSearchPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.repository.ClientRepositorySearchCommand;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Management panel for repositories.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 30 aug. 2011
 */
public class RepositoryManagementPanel extends AbstractSearchPanel<ClientRepositorySearchCommand> {

	/** The icons of the application. */
	private final AdmIcons icons;

	/** The name to search on. */
	private TextField<String> name;

	/** The {@link SimpleComboBox} containing a list of enabled statuses. */
	private SimpleComboBox<String> enabled;

	/**
	 * Default constructor.
	 */
	public RepositoryManagementPanel() {
		icons = Registry.get(AdmModule.ICONS);

		sc = new ClientRepositorySearchCommand();

		initializeWidgets();
	}

	@Override
	protected AbstractGridPanel createGridPanel() {
		return new RepositoryTable(sc);
	}

	@Override
	protected FormPanel createFilterPanel() {
		final FormPanel formPanel = new FormPanel();
		formPanel.setHeading("Search criteria");
		formPanel.setFrame(true);
		formPanel.setLabelWidth(200);
		formPanel.setButtonAlign(HorizontalAlignment.LEFT);
		formPanel.setIcon(AbstractImagePrototype.create(icons.funnel()));
		formPanel.setHeight(160);

		/*
		 * Add name.
		 */
		name = new TextField<String>();
		name.setId("repository-management-name");
		name.setName("name");
		name.setFieldLabel("Name");
		name.setAllowBlank(true);
		formPanel.add(name);

		/*
		 * Add enabled.
		 */
		enabled = new SimpleComboBox<String>();
		enabled.setId("repository-management-enabled");
		enabled.setName("enabledValue");
		enabled.setFieldLabel("Enabled");
		enabled.setEmptyText("Select enabled state..");
		enabled.add("Any...");
		enabled.add("Enabled");
		enabled.add("Disabled");
		enabled.setTriggerAction(TriggerAction.ALL);
		enabled.setForceSelection(true);
		enabled.setEditable(false);
		formPanel.add(enabled);

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

		/*
		 * Add binding for Active state using a simple combo value converter.
		 */
		final FieldBinding fieldBinding = new FieldBinding(this.enabled, "enabledValue");
		fieldBinding.setConverter(new SimpleComboValueConverter<String>());
		fieldBindings.add(fieldBinding);

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
		northData.setMaxSize(200);
		northData.setSize(150);
		northData.setSplit(true);
		northData.setMargins(new Margins(1, 1, 1, 1));

		final BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);
		centerData.setMargins(new Margins(0, 1, 1, 1));
		centerData.setSplit(true);

		add(formPanel, northData);
		add(gridPanel, centerData);
	}

}
