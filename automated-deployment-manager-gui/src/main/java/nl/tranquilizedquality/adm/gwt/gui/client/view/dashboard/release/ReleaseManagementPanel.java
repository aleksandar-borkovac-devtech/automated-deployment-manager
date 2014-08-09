/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 27 sep. 2011 File: ReleaseManagementPanel.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.view.dashboard.release
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
package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.release;

import nl.tranquilizedquality.adm.commons.business.domain.ReleaseStatus;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.converter.EnumConverter;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.converter.EnumWrapper;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractGridPanel;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractSearchPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientReleaseSearchCommand;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Management panel where you can
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 27 sep. 2011
 */
public class ReleaseManagementPanel extends AbstractSearchPanel<ClientReleaseSearchCommand> {

    /** The icons of the application. */
    private final AdmIcons icons;

    /** The name to search on. */
    private TextField<String> name;

    /** The release date to search on. */
    private DateField releaseStart;

    /** The release date to search on. */
    private DateField releaseEnd;

    /** The artifact id to search on. */
    private TextField<String> artifactId;

    /** The {@link ComboBox} containing a list of artifact types. */
    private ComboBox<EnumWrapper<ReleaseStatus>> releaseStatus;

    /** Checkbox used to search for archived releases. */
    private CheckBox archived;

    /**
     * Default constructor.
     */
    public ReleaseManagementPanel() {
        icons = Registry.get(AdmModule.ICONS);

        sc = new ClientReleaseSearchCommand();

        northHeight = 170;

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

        final FieldBinding binding = new FieldBinding(this.releaseStatus, "status");
        binding.setConverter(new EnumConverter<ReleaseStatus>());
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
        return new ReleaseTable(sc);
    }

    @Override
    protected FormPanel createFilterPanel() {
        final FormPanel formPanel = new FormPanel();
        formPanel.setHeading("Search criteria");
        formPanel.setFrame(true);
        formPanel.setIcon(AbstractImagePrototype.create(icons.findReleases()));
        formPanel.setHeight(140);
        formPanel.setButtonAlign(HorizontalAlignment.LEFT);

        final FormData formData = new FormData("90%");

        /*
         * Main panel
         */
        final LayoutContainer main = new LayoutContainer();
        main.setLayout(new ColumnLayout());

        /*
         * Left panel
         */
        final LayoutContainer left = new LayoutContainer();
        left.setStyleAttribute("paddingRight", "10px");
        final FormLayout formLayout = new FormLayout();
        formLayout.setLabelWidth(110);
        left.setLayout(formLayout);

        /*
         * Add artifact name.
         */
        name = new TextField<String>();
        name.setId("release-management-release-name");
        name.setName("releaseName");
        name.setFieldLabel("Release Name");
        name.setAllowBlank(true);
        left.add(name, formData);

        /*
         * Add release status.
         */
        releaseStatus = new ComboBox<EnumWrapper<ReleaseStatus>>();
        releaseStatus.setId("release-management-status");
        releaseStatus.setName("status");
        releaseStatus.setFieldLabel("Release Status");
        releaseStatus.setDisplayField("value");
        releaseStatus.setEmptyText("Select type..");
        releaseStatus.setTriggerAction(TriggerAction.ALL);
        releaseStatus.setForceSelection(true);
        releaseStatus.setEditable(false);

        /*
         * Add artifact id.
         */
        artifactId = new TextField<String>();
        artifactId.setId("release-management-artifact-artifact-id");
        artifactId.setName("artifactId");
        artifactId.setFieldLabel("Artifact Id");
        artifactId.setAllowBlank(true);
        left.add(artifactId, formData);

        /*
         * Add enums.
         */
        final ListStore<EnumWrapper<ReleaseStatus>> store = new ListStore<EnumWrapper<ReleaseStatus>>();
        final ReleaseStatus[] values = ReleaseStatus.values();
        store.add(new EnumWrapper<ReleaseStatus>(null));
        for (final ReleaseStatus status : values) {
            final EnumWrapper<ReleaseStatus> wrapper = new EnumWrapper<ReleaseStatus>(status);
            store.add(wrapper);
        }

        releaseStatus.setStore(store);

        left.add(releaseStatus, formData);

        /*
         * Right panel
         */
        final LayoutContainer right = new LayoutContainer();
        right.setStyleAttribute("paddingRight", "10px");
        final FormLayout rightFormLayout = new FormLayout();
        rightFormLayout.setLabelWidth(110);
        right.setLayout(rightFormLayout);
        final FormData rightFormData = new FormData("90%");

        /*
         * Add release date range.
         */
        releaseStart = new DateField();
        releaseStart.setId("release-management-start");
        releaseStart.setName("releaseDateStart");
        releaseStart.setFieldLabel("Release Date Start");
        releaseStart.setWidth(50);
        releaseStart.setAllowBlank(true);
        releaseStart.setEnabled(true);
        right.add(releaseStart, rightFormData);

        releaseEnd = new DateField();
        releaseEnd.setId("release-management-end");
        releaseEnd.setName("releaseDateEnd");
        releaseEnd.setFieldLabel("Release Date End");
        releaseEnd.setWidth(50);
        releaseEnd.setAllowBlank(true);
        releaseEnd.setEnabled(true);
        right.add(releaseEnd, rightFormData);

        archived = new CheckBox();
        archived.setId("release-management-archived");
        archived.setName("archived");
        archived.setFieldLabel("Archived");
        archived.setWidth(50);
        archived.setEnabled(true);
        right.add(archived, new FormData(15, 20));

        /*
         * Add panels to form panel.
         */
        main.add(left, new ColumnData(350));
        main.add(right, new ColumnData(350));

        formPanel.add(main);

        /*
         * Add search button.
         */
        final Button search = new Button("Search");
        search.setId("search-release-btn");
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
