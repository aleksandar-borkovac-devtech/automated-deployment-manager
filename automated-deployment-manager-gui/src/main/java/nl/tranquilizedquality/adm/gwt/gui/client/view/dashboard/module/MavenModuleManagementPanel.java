/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 29 sep. 2011 File: MavenModuleManagementPanel.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.view.dashboard.module
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
package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.module;

import nl.tranquilizedquality.adm.commons.business.domain.ArtifactType;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.converter.EnumConverter;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.converter.EnumWrapper;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractGridPanel;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractSearchPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.artifact.ClientMavenModuleSearchCommand;

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
 * Management panel for Maven modules.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 29 sep. 2011
 */
public class MavenModuleManagementPanel extends AbstractSearchPanel<ClientMavenModuleSearchCommand> {

    /** The icons of the application. */
    private final AdmIcons icons;

    /** The name to search on. */
    private TextField<String> name;

    /** The group to search on. */
    private TextField<String> group;

    /** The artifact id to search on. */
    private TextField<String> artifactId;

    /** The {@link ComboBox} containing a list of artifact types. */
    private ComboBox<EnumWrapper<ArtifactType>> artifactType;

    /**
     * Default constructor.
     */
    public MavenModuleManagementPanel() {
        icons = Registry.get(AdmModule.ICONS);

        sc = new ClientMavenModuleSearchCommand();

        northHeight = 200;

        initializeWidgets();
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

        final FieldBinding binding = new FieldBinding(this.artifactType, "type");
        binding.setConverter(new EnumConverter<ArtifactType>());
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

    @Override
    protected AbstractGridPanel createGridPanel() {
        return new MavenModuleTable(sc);
    }

    @Override
    protected FormPanel createFilterPanel() {
        final FormPanel formPanel = new FormPanel();
        formPanel.setHeading("Search criteria");
        formPanel.setFrame(true);
        formPanel.setLabelWidth(200);
        formPanel.setButtonAlign(HorizontalAlignment.LEFT);
        formPanel.setIcon(AbstractImagePrototype.create(icons.findArtifacts()));
        formPanel.setHeight(160);

        /*
         * Add group.
         */
        group = new TextField<String>();
        group.setId("maven-module-management-artifact-group");
        group.setName("group");
        group.setFieldLabel("Artifact Group");
        group.setAllowBlank(true);
        formPanel.add(group);

        /*
         * Add artifact id.
         */
        artifactId = new TextField<String>();
        artifactId.setId("maven-module-management-artifact-id");
        artifactId.setName("artifactId");
        artifactId.setFieldLabel("Artifact Id");
        artifactId.setAllowBlank(true);
        formPanel.add(artifactId);

        /*
         * Add artifact name.
         */
        name = new TextField<String>();
        name.setId("maven-module-management-artifact-name");
        name.setName("name");
        name.setFieldLabel("Artifact Name");
        name.setAllowBlank(true);
        formPanel.add(name);

        /*
         * Add protocol.
         */
        artifactType = new ComboBox<EnumWrapper<ArtifactType>>();
        artifactType.setId("maven-module-management-type");
        artifactType.setName("type");
        artifactType.setFieldLabel("Artifact Type");
        artifactType.setDisplayField("value");
        artifactType.setEmptyText("Select type..");
        artifactType.setTriggerAction(TriggerAction.ALL);
        artifactType.setForceSelection(true);
        artifactType.setEditable(false);

        /*
         * Add enums.
         */
        final ListStore<EnumWrapper<ArtifactType>> store = new ListStore<EnumWrapper<ArtifactType>>();
        final ArtifactType[] values = ArtifactType.values();
        store.add(new EnumWrapper<ArtifactType>(null));
        for (final ArtifactType artifactType : values) {
            final EnumWrapper<ArtifactType> wrapper = new EnumWrapper<ArtifactType>(artifactType);
            store.add(wrapper);
        }

        artifactType.setStore(store);

        formPanel.add(artifactType);

        /*
         * Add search button.
         */
        final Button search = new Button("Search");
        search.setId("maven-module-management-search-btn");
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

}
