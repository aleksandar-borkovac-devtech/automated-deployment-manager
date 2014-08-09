/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 30 okt. 2011 File: DestinationHostManagementPanel.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.view.dashboard.host
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
package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.host;

import nl.tranquilizedquality.adm.commons.business.domain.Protocol;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.converter.EnumConverter;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.converter.EnumWrapper;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractGridPanel;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractSearchPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientDestinationHost;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientDestinationHostSearchCommand;

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
 * Panel where {@link ClientDestinationHost} are managed.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 30 okt. 2011
 */
public class DestinationHostManagementPanel extends AbstractSearchPanel<ClientDestinationHostSearchCommand> {

    /** The icons of the application. */
    private final AdmIcons icons;

    /** The name to search on. */
    private TextField<String> hostName;

    /** The {@link ComboBox} containing a list of protocols. */
    private ComboBox<EnumWrapper<Protocol>> protocol;

    private TextField<String> environmentName;

    /**
     * Default constructor.
     */
    public DestinationHostManagementPanel() {
        setId("destination-host-management-panel");
        icons = Registry.get(AdmModule.ICONS);

        sc = new ClientDestinationHostSearchCommand();

        northHeight = 200;

        initializeWidgets();
    }

    @Override
    protected AbstractGridPanel createGridPanel() {
        return new DestinationHostTable(sc);
    }

    @Override
    protected FormPanel createFilterPanel() {
        final FormPanel formPanel = new FormPanel();
        formPanel.setHeading("Search criteria");
        formPanel.setFrame(true);
        formPanel.setLabelWidth(200);
        formPanel.setButtonAlign(HorizontalAlignment.LEFT);
        formPanel.setIcon(AbstractImagePrototype.create(icons.findHosts()));
        formPanel.setHeight(160);

        /*
         * Add name.
         */
        hostName = new TextField<String>();
        hostName.setId("destination-host-management-host-name");
        hostName.setName("hostName");
        hostName.setFieldLabel("Host Name");
        hostName.setAllowBlank(true);
        formPanel.add(hostName);

        environmentName = new TextField<String>();
        environmentName.setId("destination-host-management-environment-name");
        environmentName.setName("environment");
        environmentName.setFieldLabel("Environment Name");
        environmentName.setAllowBlank(true);
        formPanel.add(environmentName);

        /*
         * Add protocol.
         */
        protocol = new ComboBox<EnumWrapper<Protocol>>();
        protocol.setId("destination-host-management-protocol");
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
