/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: Aug 25, 2012 File: fAvailableUsersSearchPanel.java
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

import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractGridPanel;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractSearchPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUserSearchCommand;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Search panel where you can search for available users that can be added as
 * deployers.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
class AvailableUsersSearchPanel extends AbstractSearchPanel<ClientUserSearchCommand> {

    /** The icons of the application. */
    private final AdmIcons icons;

    /** The name to search on. */
    private TextField<String> name;

    /** The username to search on. */
    private TextField<String> userName;

    /** Panel where the user group details are displayed on. */
    private final EnvironmentDetailsPanel detailsPanel;

    /**
     * Default constructor.
     */
    public AvailableUsersSearchPanel(final EnvironmentDetailsPanel detailsPanel) {
        icons = Registry.get(AdmModule.ICONS);
        sc = new ClientUserSearchCommand();
        this.detailsPanel = detailsPanel;
        initializeWidgets();
    }

    @Override
    protected AbstractGridPanel createGridPanel() {
        return new AvailableUsersTable(sc, detailsPanel);
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
        name.setId("users-search-name");
        name.setName("name");
        name.setFieldLabel("Name");
        name.setAllowBlank(true);
        formPanel.add(name);

        /*
         * Add user name.
         */
        userName = new TextField<String>();
        userName.setId("users-search-user-name");
        userName.setName("userName");
        userName.setFieldLabel("Username");
        userName.setAllowBlank(true);
        formPanel.add(userName);

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

    /**
     * Initializes the panel.
     */
    public void initialize() {
        final AvailableUsersTable usersTable = (AvailableUsersTable) gridPanel;
        usersTable.initialize();
    }

}
