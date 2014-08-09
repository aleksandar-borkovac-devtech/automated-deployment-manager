/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 3 okt. 2011 File: ArtifactDetailsPanel.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.view.dashboard.release.artifact
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
package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.release.artifact;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.ArtifactType;
import nl.tranquilizedquality.adm.commons.business.domain.MavenModule;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.converter.EnumConverter;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.converter.EnumWrapper;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.AbstractServiceException;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractDetailPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.artifact.ClientMavenArtifact;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientRelease;
import nl.tranquilizedquality.adm.gwt.gui.client.service.artifact.ArtifactServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.view.AdmViewPort;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.release.ReleaseDetailsPanel;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Details panel where a new artifact can be created.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 okt. 2011
 */
public class ArtifactDetailsPanel extends AbstractDetailPanel<ClientMavenArtifact> {

    /** The icons of the application. */
    private final AdmIcons icons;

    /** The name of the Maven module. */
    private TextField<String> name;

    /** The group of the Maven module. */
    private TextField<String> group;

    /** The artifact id. */
    private TextField<String> artifactId;

    /** The save button. */
    private Button saveButton;

    /** The {@link ComboBox} containing a list of artifact types. */
    private ComboBox<EnumWrapper<ArtifactType>> artifactType;

    /** The version of the artifact. */
    private TextField<String> version;

    /** The name of the release this artifact is part of. */
    private TextField<String> release;

    /**
     * Determines if the target system should be started up after deployment or
     * not.
     */
    private CheckBox targetSystemStartup;

    /**
     * Determines if the target system should be shutdown before deployment or
     * not.
     */
    private CheckBox targetSystemShutdown;

    /** Window where this panel is displayed in. */
    private final Window window;

    /**
     * Constructor taking the window where it's displayed in.
     * 
     * @param window
     *        The parent window.
     */
    public ArtifactDetailsPanel(final Window window) {
        this.icons = Registry.get(AdmModule.ICONS);
        this.window = window;

        initializeWidgets();
    }

    @Override
    protected void initializeWidgets() {
        setLayout(new FitLayout());

        formPanel = createDetailPanel();

        final FieldBinding binding = new FieldBinding(this.artifactType, "type");
        binding.setConverter(new EnumConverter<ArtifactType>());
        fieldBindings.add(binding);

        add(this.formPanel);
    }

    @Override
    public void setModel(final ClientMavenArtifact model) {
        this.model = model;

        final MavenModule module = this.model.getParentModule();
        final Boolean shutdown = module.getTargetSystemShutdown();
        final Boolean startup = module.getTargetSystemStartup();

        this.model.setTargetSystemShutdown(shutdown);
        this.model.setTargetSystemStartup(startup);

        if (this.model.isPersistent()) {
            final AsyncCallback<ClientMavenArtifact> callback = new AsyncCallback<ClientMavenArtifact>() {

                @Override
                public void onFailure(final Throwable throwable) {
                    final MessageBox box = new MessageBox();
                    box.setIcon(MessageBox.ERROR);
                    box.setTitle("Retrieve artifact.");
                    box.setMessage(throwable.getMessage());
                    box.setButtons(MessageBox.OK);
                    box.show();
                }

                @Override
                public void onSuccess(final ClientMavenArtifact artifact) {
                    ArtifactDetailsPanel.this.model = artifact;
                    bindModel(ArtifactDetailsPanel.this.model);
                }

            };

            final ArtifactServiceAsync artifactService = Registry.get(AdmModule.ARTIFACT_SERVICE);
            artifactService.findMavenArtifactById(this.model.getId(), callback);
        } else {
            bindModel(this.model);
        }
    }

    @Override
    protected void performPrivilegeCheck() {

    }

    @Override
    protected FormPanel createDetailPanel() {
        final FormPanel formPanel = new FormPanel();
        formPanel.setHeading("Details");
        formPanel.setHeaderVisible(false);
        formPanel.setFrame(true);
        formPanel.setLabelWidth(110);
        formPanel.setIcon(AbstractImagePrototype.create(icons.addArtifact()));
        formPanel.setButtonAlign(HorizontalAlignment.RIGHT);

        /*
         * Create field set for maven module related information.
         */
        final FieldSet mavenModuleInfoFieldSet = new FieldSet();
        mavenModuleInfoFieldSet.setLayout(new FormLayout());
        mavenModuleInfoFieldSet.setCollapsible(true);
        mavenModuleInfoFieldSet.setAutoHeight(true);
        mavenModuleInfoFieldSet.setHeading("Maven Module Information");

        /*
         * Add Maven Module name.
         */
        name = new TextField<String>();
        name.setId("artifact-selection-details-pnl-name");
        name.setAllowBlank(false);
        name.setName("moduleName");
        name.setFieldLabel("Name");
        name.setReadOnly(true);
        mavenModuleInfoFieldSet.add(name);

        /*
         * Add Maven Module group.
         */
        group = new TextField<String>();
        group.setId("artifact-selection-details-pnl-group");
        group.setAllowBlank(false);
        group.setName("group");
        group.setFieldLabel("Group");
        group.setReadOnly(true);
        mavenModuleInfoFieldSet.add(group);

        /*
         * Add Maven Artifact Id.
         */
        artifactId = new TextField<String>();
        artifactId.setId("artifact-selection-details-pnl-artifact-id");
        artifactId.setAllowBlank(false);
        artifactId.setName("artifactId");
        artifactId.setFieldLabel("Artifact Id");
        artifactId.setReadOnly(true);
        mavenModuleInfoFieldSet.add(artifactId);

        /*
         * Add artifact types.
         */
        artifactType = new ComboBox<EnumWrapper<ArtifactType>>();
        artifactType.setId("artifact-selection-details-pnl-type");
        artifactType.setName("type");
        artifactType.setFieldLabel("Artifact Type");
        artifactType.setDisplayField("value");
        artifactType.setEmptyText("Select type..");
        artifactType.setTriggerAction(TriggerAction.ALL);
        artifactType.setForceSelection(true);
        artifactType.setEditable(false);
        artifactType.setReadOnly(true);

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

        mavenModuleInfoFieldSet.add(artifactType);

        formPanel.add(mavenModuleInfoFieldSet);

        /*
         * Add Maven Artifact details
         */
        final FieldSet mavenArtifactInfoFieldSet = new FieldSet();
        mavenArtifactInfoFieldSet.setLayout(new FormLayout());
        mavenArtifactInfoFieldSet.setCollapsible(true);
        mavenArtifactInfoFieldSet.setAutoHeight(true);
        mavenArtifactInfoFieldSet.setHeading("Maven Artifact Information");

        /*
         * Add version
         */
        version = new TextField<String>();
        version.setId("artifact-selection-details-pnl-version");
        version.setAllowBlank(false);
        version.setName("version");
        version.setFieldLabel("Version");
        mavenArtifactInfoFieldSet.add(version);

        /*
         * Add release
         */
        release = new TextField<String>();
        release.setId("artifact-selection-details-pnl-release");
        release.setAllowBlank(false);
        release.setName("releaseName");
        release.setFieldLabel("Release");
        release.setReadOnly(true);
        mavenArtifactInfoFieldSet.add(release);

        /*
         * Add target system shutdown
         */
        targetSystemShutdown = new CheckBox();
        targetSystemShutdown.setId("artifact-selection-details-pnl-shutdown");
        targetSystemShutdown.setName("targetSystemShutdown");
        targetSystemShutdown.setFieldLabel("Shutdown target system");
        mavenArtifactInfoFieldSet.add(targetSystemShutdown, new FormData(15, 20));

        /*
         * Add target system startup
         */
        targetSystemStartup = new CheckBox();
        targetSystemStartup.setId("artifact-selection-details-pnl-startup");
        targetSystemStartup.setName("targetSystemStartup");
        targetSystemStartup.setFieldLabel("Startup target system");
        mavenArtifactInfoFieldSet.add(targetSystemStartup, new FormData(15, 20));

        formPanel.add(mavenArtifactInfoFieldSet);

        /*
         * Add cancel button.
         */
        final Button cancel = new Button("Cancel");
        cancel.setId("artifact-selection-cancel-btn");
        cancel.setIcon(AbstractImagePrototype.create(icons.delete()));

        final SelectionListener<ButtonEvent> cancelListener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                final AdmViewPort viewPort = Registry.get(AdmModule.VIEW_PORT);
                viewPort.unmask();

                window.hide();
            }

        };
        cancel.addSelectionListener(cancelListener);

        formPanel.addButton(cancel);

        /*
         * Add save button.
         */
        saveButton = new Button();
        saveButton.setId("artifact-selection-save-artifact-btn");
        saveButton.setText("Save");

        final SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                for (final FieldBinding fieldBinding : fieldBindings) {
                    fieldBinding.updateModel();
                }

                final MavenModule parentModule = model.getParentModule();
                final UserGroup userGroup = parentModule.getUserGroup();
                model.setUserGroup(userGroup);

                final ArtifactServiceAsync artifactService = Registry.get(AdmModule.ARTIFACT_SERVICE);
                final AsyncCallback<ClientMavenArtifact> callback = new AsyncCallback<ClientMavenArtifact>() {

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

                            final String string = builder.toString();
                            if (string.isEmpty()) {
                                builder.append(throwable.getMessage());
                            }
                        } else {
                            builder.append(throwable.getMessage());
                        }

                        final MessageBox box = new MessageBox();
                        box.setIcon(MessageBox.ERROR);
                        box.setTitle("Failed to save artifact.");
                        box.setMessage(builder.toString());
                        box.setButtons(MessageBox.OK);
                        box.show();
                    }

                    @Override
                    public void onSuccess(final ClientMavenArtifact artifact) {
                        final MessageBox box = new MessageBox();
                        box.setIcon(MessageBox.INFO);
                        box.setTitle("Save artifact.");
                        box.setMessage("Successfully saved " + artifact.getModuleName() + ".");
                        box.setButtons(MessageBox.OK);
                        box.show();

                        final Listener<MessageBoxEvent> closeListener = new Listener<MessageBoxEvent>() {

                            @Override
                            public void handleEvent(final MessageBoxEvent be) {
                                final AdmViewPort viewPort = Registry.get(AdmModule.VIEW_PORT);
                                viewPort.unmask();

                                window.hide();

                                final ReleaseDetailsPanel pnl = Registry.get(ReleaseDetailsPanel.RELEASE_DETAIL_PANEL);
                                pnl.setModel((ClientRelease) model.getRelease());
                            }
                        };
                        box.addListener(Events.Hide, closeListener);
                    }

                };

                artifactService.saveMavenArtifact(model, callback);
            }

        };
        saveButton.addSelectionListener(listener);

        formPanel.addButton(saveButton);

        return formPanel;
    }

}
