/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 29 sep. 2011 File: MavenModuleDetailsPanel.java
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

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.ArtifactType;
import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.business.domain.MavenModule;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.converter.BeanModelConverter;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.converter.EnumConverter;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.converter.EnumWrapper;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.AbstractServiceException;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.security.AuthorizationServiceAsync;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractDetailPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.artifact.ClientMavenModule;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientDestination;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUserGroup;
import nl.tranquilizedquality.adm.gwt.gui.client.service.artifact.ArtifactServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.service.environment.EnvironmentServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.service.security.UserGroupServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.module.dependency.AvailableDependenciesTable;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.module.dependency.DependenciesTable;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.module.destination.AvailableDestinationTable;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.module.destination.DestinationTable;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Details panel where the details of a {@link MavenModule} are displayed.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 29 sep. 2011
 */
public class MavenModuleDetailsPanel extends AbstractDetailPanel<ClientMavenModule> {

    /** The icons of the application. */
    private final AdmIcons icons;

    /** The name of the Maven module. */
    private TextField<String> name;

    /** The group of the Maven module. */
    private TextField<String> group;

    /** The artifact id. */
    private TextField<String> artifactId;

    /** The identifier of the module. */
    private TextField<String> identifier;

    /** The save button. */
    private Button saveButton;

    /** The {@link ComboBox} containing a list of artifact types. */
    private ComboBox<EnumWrapper<ArtifactType>> artifactType;

    /** The {@link ComboBox} containing a list of user groups. */
    private ComboBox<ModelData> userGroup;

    /**
     * Determines if the target system should be started up after deployment or
     * not.
     */
    private CheckBox targetSystemStartup;

    /**
     * Determines if the target system should be shutdown before deployement or
     * not.
     */
    private CheckBox targetSystemShutdown;

    /**
     * Table where all destinations are displayed where the module can be
     * deployed to.
     */
    private DestinationTable destinationTable;

    /** Table where all available destinations are displayed. */
    private AvailableDestinationTable availableDestinationsTable;

    /** Table where the dependencies of this module are displayed in. */
    private DependenciesTable dependenciesTable;

    /**
     * Table where all available modules are displayed in so you can select one
     * as a dependency.
     */
    private AvailableDependenciesTable availableDependenciesTable;

    /**
     * Default constructor.
     */
    public MavenModuleDetailsPanel() {
        this.icons = Registry.get(AdmModule.ICONS);

        initializeWidgets();
        refreshData();
        performPrivilegeCheck();
    }

    @Override
    protected void initializeWidgets() {

        /*
         * Set layout.
         */
        final BorderLayout layout = new BorderLayout();
        setLayout(layout);

        /*
         * Create details panel.
         */
        formPanel = createDetailPanel();

        /*
         * Add field binding.
         */
        FieldBinding binding = new FieldBinding(this.artifactType, "type");
        binding.setConverter(new EnumConverter<ArtifactType>());
        fieldBindings.add(binding);

        binding = new FieldBinding(this.userGroup, "userGroup");
        binding.setConverter(new BeanModelConverter());
        fieldBindings.add(binding);

        /*
         * Create relations panel.
         */
        final LayoutContainer relationsPanel = createRelationsPanel();

        /*
         * Add panels.
         */
        add(this.formPanel, new BorderLayoutData(LayoutRegion.NORTH, 260, 340, 340));
        add(relationsPanel, new BorderLayoutData(LayoutRegion.CENTER));

    }

    /**
     * Create the panel with the relation tables.
     * 
     * @return the container containing the tables.
     */
    private LayoutContainer createRelationsPanel() {
        final LayoutContainer container = new LayoutContainer();
        final RowLayout layout = new RowLayout(Orientation.HORIZONTAL);
        container.setLayout(layout);

        destinationTable = new DestinationTable(this);
        availableDestinationsTable = new AvailableDestinationTable(this);
        dependenciesTable = new DependenciesTable(this);
        availableDependenciesTable = new AvailableDependenciesTable(this);

        final LayoutContainer destinationContainer = new LayoutContainer();
        final RowLayout verticalLayout = new RowLayout(Orientation.VERTICAL);
        destinationContainer.setLayout(verticalLayout);
        destinationContainer.add(destinationTable, new RowData(1, 0.5, new Margins(2, 0, 2, 2)));
        destinationContainer.add(availableDestinationsTable, new RowData(1, 0.5, new Margins(2, 0, 2, 2)));

        final LayoutContainer dependencyContainer = new LayoutContainer();
        final RowLayout dependencyVerticalLayout = new RowLayout(Orientation.VERTICAL);
        dependencyContainer.setLayout(dependencyVerticalLayout);
        dependencyContainer.add(dependenciesTable, new RowData(1, 0.5, new Margins(2, 0, 2, 2)));
        dependencyContainer.add(availableDependenciesTable, new RowData(1, 0.5, new Margins(2, 0, 2, 2)));

        container.add(destinationContainer, new RowData(0.5, 1, new Margins(2, 0, 2, 2)));
        container.add(dependencyContainer, new RowData(0.5, 1, new Margins(2, 2, 2, 2)));

        return container;
    }

    @Override
    public void setModel(final ClientMavenModule model) {
        this.model = model;

        if (this.model.isPersistent()) {
            final AsyncCallback<ClientMavenModule> callback = new AsyncCallback<ClientMavenModule>() {

                @Override
                public void onFailure(final Throwable throwable) {
                    final MessageBox box = new MessageBox();
                    box.setIcon(MessageBox.ERROR);
                    box.setTitle("Retrieve module.");
                    box.setMessage(throwable.getMessage());
                    box.setButtons(MessageBox.OK);
                    box.show();
                }

                @Override
                public void onSuccess(final ClientMavenModule module) {
                    MavenModuleDetailsPanel.this.model = module;
                    bindModel(MavenModuleDetailsPanel.this.model);

                    final List<Destination> destinations = module.getDestinations();
                    destinationTable.setModel(destinations);
                    retrieveAvailableDestinations();

                    final List<MavenModule> deploymentDependencies = module.getDeploymentDependencies();
                    dependenciesTable.setModel(deploymentDependencies);
                    retrieveAvailableDependencies();
                }

            };

            final ArtifactServiceAsync artifactService = Registry.get(AdmModule.ARTIFACT_SERVICE);
            artifactService.findMavenModuleById(this.model.getId(), callback);
        } else {
            bindModel(this.model);

            final List<Destination> destinations = model.getDestinations();
            destinationTable.setModel(destinations);

            /*
             * Retrieve available destinations.
             */
            retrieveAvailableDestinations();
        }
    }

    /**
     * Retrieves the available destinations filtered out by the destinations
     * that the module already has.
     */
    private void retrieveAvailableDestinations() {
        final EnvironmentServiceAsync environmentService = Registry.get(AdmModule.ENVIRONMENT_SERVICE);
        final AsyncCallback<List<ClientDestination>> callback = new AsyncCallback<List<ClientDestination>>() {

            @Override
            public void onSuccess(final List<ClientDestination> destinations) {
                final List<Destination> modelDestinations = model.getDestinations();

                /*
                 * Remove all destinations that are already connected to the
                 * model.
                 */
                for (final Destination destination : modelDestinations) {
                    destinations.remove(destination);
                }

                /*
                 * Create compatible collection.
                 */
                final List<Destination> availableDestinations = new ArrayList<Destination>();
                availableDestinations.addAll(destinations);

                availableDestinationsTable.setModel(availableDestinations);
            }

            @Override
            public void onFailure(final Throwable throwable) {
                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.ERROR);
                box.setTitle("Retrieve available destinations.");
                box.setMessage(throwable.getMessage());
                box.setButtons(MessageBox.OK);
                box.show();
            }
        };
        environmentService.findAvailableDestinations(callback);
    }

    /**
     * Retrieves the available destinations filtered out by the dependencies
     * that the module already has.
     */
    private void retrieveAvailableDependencies() {
        final Long mavenModuleId = this.model.getId();

        final ArtifactServiceAsync artifactService = Registry.get(AdmModule.ARTIFACT_SERVICE);
        artifactService.findAvailableMavenModules(mavenModuleId, new AsyncCallback<List<ClientMavenModule>>() {

            @Override
            public void onSuccess(final List<ClientMavenModule> modules) {
                final List<MavenModule> modelDependencies = model.getDeploymentDependencies();

                /*
                 * Remove all dependencies that are already connected to the
                 * model.
                 */
                for (final MavenModule mavenModule : modelDependencies) {
                    modules.remove(mavenModule);
                }

                /*
                 * Create compatible collection.
                 */
                final List<MavenModule> availableDependencies = new ArrayList<MavenModule>();
                availableDependencies.addAll(modules);

                availableDependenciesTable.setModel(availableDependencies);
            }

            @Override
            public void onFailure(final Throwable throwable) {
                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.ERROR);
                box.setTitle("Retrieve available dependencies.");
                box.setMessage(throwable.getMessage());
                box.setButtons(MessageBox.OK);
                box.show();
            }
        });
    }

    @Override
    protected void performPrivilegeCheck() {
        final AuthorizationServiceAsync authorizationService = Registry.get(AdmModule.AUTHORIZATION_SERVICE);
        final AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

            @Override
            public void onFailure(final Throwable throwable) {
                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.ERROR);
                box.setTitle("Create maven module check.");
                box.setMessage(throwable.getMessage());
                box.setButtons(MessageBox.OK);
                box.show();
            }

            @Override
            public void onSuccess(final Boolean authorized) {
                name.setReadOnly(!authorized);
                group.setReadOnly(!authorized);
                artifactId.setReadOnly(!authorized);
                artifactType.setReadOnly(!authorized);
                targetSystemShutdown.setReadOnly(!authorized);
                targetSystemStartup.setReadOnly(!authorized);
                userGroup.setReadOnly(!authorized);

                if (authorized) {
                    saveButton.show();
                } else {
                    saveButton.hide();
                }
            }

        };
        authorizationService.isLoggedInUserAuthorized("ADD_MAVEN_MODULE", callback);
    }

    @Override
    protected FormPanel createDetailPanel() {
        final FormPanel formPanel = new FormPanel();
        formPanel.setHeading("Details");
        formPanel.setFrame(true);
        formPanel.setIcon(AbstractImagePrototype.create(icons.artifacts()));
        formPanel.setButtonAlign(HorizontalAlignment.RIGHT);

        /*
         * Create left panel.
         */
        final LayoutContainer leftPanel = new LayoutContainer();
        final FormLayout leftFormLayout = new FormLayout();
        leftFormLayout.setLabelWidth(150);
        leftPanel.setLayout(leftFormLayout);
        final FormData leftFormData = new FormData("90%");

        /*
         * Create field set for maven module related information.
         */
        final FieldSet mavenModuleInfoFieldSet = new FieldSet();
        final ColumnLayout fieldSetLayout = new ColumnLayout();
        mavenModuleInfoFieldSet.setLayout(fieldSetLayout);
        mavenModuleInfoFieldSet.setCollapsible(true);
        mavenModuleInfoFieldSet.setAutoHeight(true);
        mavenModuleInfoFieldSet.setHeading("Maven Module Information");

        /*
         * Add Maven Module name.
         */
        name = new TextField<String>();
        name.setId("maven-module-details-pnl-name");
        name.setAllowBlank(false);
        name.setName("name");
        name.setFieldLabel("Name");
        leftPanel.add(name, leftFormData);

        /*
         * Add Maven Module group.
         */
        group = new TextField<String>();
        group.setId("maven-module-details-pnl-group");
        group.setAllowBlank(false);
        group.setName("group");
        group.setFieldLabel("Group");
        leftPanel.add(group, leftFormData);

        /*
         * Add Maven Artifact Id.
         */
        artifactId = new TextField<String>();
        artifactId.setId("maven-module-details-pnl-artifact-id");
        artifactId.setAllowBlank(false);
        artifactId.setName("artifactId");
        artifactId.setFieldLabel("Artifact Id");
        leftPanel.add(artifactId, leftFormData);

        /*
         * Add target system shutdown
         */
        targetSystemShutdown = new CheckBox();
        targetSystemShutdown.setId("maven-module-details-pnl-shutdown");
        targetSystemShutdown.setName("targetSystemShutdown");
        targetSystemShutdown.setFieldLabel("Shutdown target system");
        leftPanel.add(targetSystemShutdown, new FormData(15, 20));

        /*
         * Add target system startup
         */
        targetSystemStartup = new CheckBox();
        targetSystemStartup.setId("maven-module-details-pnl-startup");
        targetSystemStartup.setName("targetSystemStartup");
        targetSystemStartup.setFieldLabel("Startup target system");
        leftPanel.add(targetSystemStartup, new FormData(15, 20));

        /*
         * Create right panel.
         */
        final LayoutContainer rightPanel = new LayoutContainer();
        final FormLayout rightLayout = new FormLayout();
        rightLayout.setLabelWidth(150);
        rightPanel.setLayout(rightLayout);
        final FormData rightFormData = new FormData("90%");

        identifier = new TextField<String>();
        identifier.setId("maven-module-details-pnl-identifier");
        identifier.setAllowBlank(true);
        identifier.setName("identifier");
        identifier.setFieldLabel("Suffix");
        identifier.setToolTip("Maven classifier used as suffix when retrieving artifacts from a Maven repository.");
        rightPanel.add(identifier, rightFormData);

        /*
         * Add artifact types.
         */
        artifactType = new ComboBox<EnumWrapper<ArtifactType>>();
        artifactType.setId("maven-module-details-pnl-type");
        artifactType.setName("type");
        artifactType.setFieldLabel("Artifact Type");
        artifactType.setDisplayField("value");
        artifactType.setEmptyText("Select type..");
        artifactType.setTriggerAction(TriggerAction.ALL);
        artifactType.setForceSelection(true);
        artifactType.setEditable(false);
        artifactType.setAllowBlank(false);

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

        rightPanel.add(artifactType, rightFormData);

        /*
         * Add user group.
         */
        userGroup = new ComboBox<ModelData>();
        userGroup.setId("maven-module-details-pnl-user-group");
        userGroup.setName("userGroup");
        userGroup.setFieldLabel("Group");
        userGroup.setDisplayField("name");
        userGroup.setEmptyText("Select group..");
        userGroup.setTriggerAction(TriggerAction.ALL);
        userGroup.setForceSelection(true);
        userGroup.setEditable(false);
        userGroup.setAllowBlank(false);

        /*
         * Add enums.
         */
        final ListStore<ModelData> userGroupStore = new ListStore<ModelData>();
        userGroup.setStore(userGroupStore);
        rightPanel.add(userGroup, rightFormData);

        mavenModuleInfoFieldSet.add(leftPanel, new ColumnData(400));
        mavenModuleInfoFieldSet.add(rightPanel, new ColumnData(400));

        formPanel.add(mavenModuleInfoFieldSet);

        saveButton = new Button();
        saveButton.setId("save-artifact-btn");
        saveButton.setText("Save");

        final SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                saveMavenModule();
            }

        };
        saveButton.addSelectionListener(listener);

        formPanel.addButton(saveButton);

        final FormButtonBinding binding = new FormButtonBinding(formPanel);
        binding.addButton(saveButton);

        return formPanel;
    }

    /**
     * Saves the maven module.
     */
    public void saveMavenModule() {
        for (final FieldBinding fieldBinding : fieldBindings) {
            fieldBinding.updateModel();
        }

        final ArtifactServiceAsync artifactService = Registry.get(AdmModule.ARTIFACT_SERVICE);
        final AsyncCallback<ClientMavenModule> callback = new AsyncCallback<ClientMavenModule>() {

            @Override
            public void onFailure(final Throwable throwable) {
                final StringBuilder builder = new StringBuilder();

                if (throwable instanceof AbstractServiceException) {
                    final AbstractServiceException ex = (AbstractServiceException) throwable;
                    final List<String> errors = ex.getErrors();

                    for (final String string : errors) {
                        builder.append(string);
                        builder.append("<br>");
                    }
                } else {
                    builder.append(throwable.getMessage());
                }

                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.ERROR);
                box.setTitle("Failed to save Maven module.");
                box.setMessage(builder.toString());
                box.setButtons(MessageBox.OK);
                box.show();
            }

            @Override
            public void onSuccess(final ClientMavenModule module) {
                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.INFO);
                box.setTitle("Save Maven Module.");
                box.setMessage("Successfully saved " + module.getName() + ".");
                box.setButtons(MessageBox.OK);
                box.show();

                setModel(module);
            }

        };

        artifactService.saveMavenModule(model, callback);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ClientMavenModule getModel() {
        return model;
    }

    /**
     * Retrieves current data from the server.
     */
    private void refreshData() {
        final AsyncCallback<List<ClientUserGroup>> callback = new AsyncCallback<List<ClientUserGroup>>() {

            @Override
            public void onFailure(final Throwable throwable) {
                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.ERROR);
                box.setTitle("Retrieve user groups.");
                box.setMessage(throwable.getMessage());
                box.setButtons(MessageBox.OK);
                box.show();
            }

            @Override
            public void onSuccess(final List<ClientUserGroup> userGroups) {
                final ListStore<ModelData> store = userGroup.getStore();
                store.removeAll();

                final BeanModelFactory factory = BeanModelLookup.get().getFactory(ClientUserGroup.class);

                for (final ClientUserGroup userGroup : userGroups) {

                    final BeanModel model = factory.createModel(userGroup);

                    store.add(model);
                }

                userGroup.setStore(store);
            }
        };

        final UserGroupServiceAsync userGroupService = Registry.get(AdmModule.USER_GROUP_SERVICE);
        userGroupService.findUserGroups(callback);
    }

}
