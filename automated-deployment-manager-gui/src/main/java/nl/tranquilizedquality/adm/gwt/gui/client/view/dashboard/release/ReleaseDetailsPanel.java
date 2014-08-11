/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 1 okt. 2011 File: ReleaseDetailsPanel.java
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

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.Release;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseStatus;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.converter.BeanModelConverter;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.converter.EnumConverter;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.converter.EnumWrapper;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.AbstractServiceException;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.security.AuthorizationServiceAsync;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractDetailPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.artifact.ClientMavenArtifact;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientEnvironment;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientProgress;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientRelease;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUserGroup;
import nl.tranquilizedquality.adm.gwt.gui.client.service.deployment.DeploymentServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.service.release.ReleaseServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.service.security.UserGroupServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.view.AdmViewPort;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.logtail.ProgressPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.release.artifact.ArtifactsTable;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.release.environment.EnvironmentSelectionWindow;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.release.history.ReleaseHistoryTable;

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
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Panel where the details of a {@link Release} are displayed in.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 1 okt. 2011
 */
public class ReleaseDetailsPanel extends AbstractDetailPanel<ClientRelease> {

    /** ID to use to retrieve a details panel. */
    public static final String RELEASE_DETAIL_PANEL = ReleaseDetailsPanel.class.getName();

    /** The icons of the application. */
    private final AdmIcons icons;

    /** The name of the release. */
    private TextField<String> name;

    /** The date of the actual release to production. */
    private DateField releaseDate;

    /** The number of times a release was released. */
    private LabelField releaseCount;

    /** The save button. */
    private Button saveButton;

    /** The {@link ComboBox} containing a list of release statuses. */
    private ComboBox<EnumWrapper<ReleaseStatus>> releaseStatus;

    /** The number of times a release failed to deploy successfully. */
    private LabelField releaseFailureCount;

    /** The date on which this release was released the last time. */
    private LabelField lastReleasedDate;

    /** Table that contains the artifacts in this release. */
    private ArtifactsTable artifactsTable;

    /** Table that displays the release history. */
    private ReleaseHistoryTable historyTable;

    /** Button used to release the release. */
    private Button releaseButton;

    /** Window where you can select the environment you want to deploy to. */
    private EnvironmentSelectionWindow window;

    /** Releases the selected artifacts. */
    private Button releaseArtifactsButton;

    /**
     * Determines if the whole release should be deployed or just the selected
     * artifacts.
     */
    private boolean wholeRelease;

    /** The {@link ComboBox} containing a list of user groups. */
    private ComboBox<ModelData> userGroup;

    /**
     * Default constructor.
     */
    public ReleaseDetailsPanel() {
        this.icons = Registry.get(AdmModule.ICONS);

        initializeWidgets();

        refreshData();

        performPrivilegeCheck();

        Registry.register(RELEASE_DETAIL_PANEL, this);
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
        FieldBinding binding = new FieldBinding(this.releaseStatus, "status");
        binding.setConverter(new EnumConverter<ReleaseStatus>());
        fieldBindings.add(binding);

        binding = new FieldBinding(this.userGroup, "userGroup");
        binding.setConverter(new BeanModelConverter());
        fieldBindings.add(binding);

        /*
         * Create relations panel.
         */
        final LayoutContainer relationsPanel = createRelationsPanel();

        add(this.formPanel, new BorderLayoutData(LayoutRegion.NORTH, 310, 310, 310));
        add(relationsPanel, new BorderLayoutData(LayoutRegion.CENTER));
    }

    private LayoutContainer createRelationsPanel() {
        final LayoutContainer container = new LayoutContainer();
        final RowLayout layout = new RowLayout(Orientation.HORIZONTAL);
        container.setLayout(layout);

        artifactsTable = new ArtifactsTable(this);
        historyTable = new ReleaseHistoryTable();

        container.add(artifactsTable, new RowData(0.6, 1, new Margins(4, 0, 4, 4)));
        container.add(historyTable, new RowData(0.4, 1, new Margins(4, 4, 4, 4)));

        return container;
    }

    @Override
    public void setModel(final ClientRelease model) {
        this.model = model;

        if (this.model.isPersistent()) {
            final AsyncCallback<ClientRelease> callback = new AsyncCallback<ClientRelease>() {

                @Override
                public void onFailure(final Throwable throwable) {
                    final MessageBox box = new MessageBox();
                    box.setIcon(MessageBox.ERROR);
                    box.setTitle("Retrieve release.");
                    box.setMessage(throwable.getMessage());
                    box.setButtons(MessageBox.OK);
                    box.show();
                }

                @Override
                public void onSuccess(final ClientRelease release) {
                    ReleaseDetailsPanel.this.model = release;
                    bindModel(ReleaseDetailsPanel.this.model);

                    final List<MavenArtifact> artifacts = release.getArtifacts();
                    artifactsTable.setModel(artifacts);
                    artifactsTable.setRelease(ReleaseDetailsPanel.this.model);
                    final ReleaseStatus status = release.getStatus();

                    if (artifacts.isEmpty() || ReleaseStatus.DRAFT.equals(status)) {
                        releaseButton.disable();
                        releaseArtifactsButton.disable();
                    } else {
                        releaseButton.enable();
                        releaseArtifactsButton.enable();
                    }

                    artifactsTable.enableButtons();
                    setReleaseHistory(release);
                }

            };

            final ReleaseServiceAsync releaseService = Registry.get(AdmModule.RELEASE_SERVICE);
            releaseService.findReleaseById(this.model.getId(), callback);
        } else {
            bindModel(this.model);

            artifactsTable.setModel(new ArrayList<MavenArtifact>());
            artifactsTable.setRelease(ReleaseDetailsPanel.this.model);
            setReleaseHistory(model);

            releaseButton.disable();
            releaseArtifactsButton.disable();
        }
    }

    private void setReleaseHistory(final ClientRelease release) {
        historyTable.setRelease(release);
    }

    @Override
    protected void performPrivilegeCheck() {
        final AuthorizationServiceAsync authorizationService = Registry.get(AdmModule.AUTHORIZATION_SERVICE);
        authorizationService.isLoggedInUserAuthorized("ADD_RELEASE", new AsyncCallback<Boolean>() {

            @Override
            public void onFailure(final Throwable throwable) {
                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.ERROR);
                box.setTitle("Create release check.");
                box.setMessage(throwable.getMessage());
                box.setButtons(MessageBox.OK);
                box.show();
            }

            @Override
            public void onSuccess(final Boolean authorized) {
                if (authorized) {
                    name.setReadOnly(false);
                    releaseDate.setReadOnly(false);
                    userGroup.setReadOnly(false);
                    saveButton.show();
                } else {
                    name.setReadOnly(true);
                    releaseDate.setReadOnly(true);
                    userGroup.setReadOnly(true);
                    saveButton.hide();
                }
            }

        });

        authorizationService.isLoggedInUserAuthorized("DEPLOY_RELEASE", new AsyncCallback<Boolean>() {

            @Override
            public void onFailure(final Throwable throwable) {
                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.ERROR);
                box.setTitle("Create release check.");
                box.setMessage(throwable.getMessage());
                box.setButtons(MessageBox.OK);
                box.show();
            }

            @Override
            public void onSuccess(final Boolean authorized) {
                if (authorized) {
                    releaseArtifactsButton.show();
                    releaseButton.show();
                } else {
                    releaseArtifactsButton.hide();
                    releaseButton.hide();
                }
            }

        });
    }

    @Override
    protected FormPanel createDetailPanel() {
        final FormPanel formPanel = new FormPanel();
        formPanel.setHeading("Details");
        formPanel.setFrame(true);
        formPanel.setIcon(AbstractImagePrototype.create(icons.releaseDetails()));
        formPanel.setButtonAlign(HorizontalAlignment.LEFT);

        /*
         * Create field set for release related information.
         */
        final FieldSet releaseInfoFieldSet = new FieldSet();
        final FormLayout releaseInfoFieldSetLayout = new FormLayout();
        releaseInfoFieldSetLayout.setLabelWidth(150);
        releaseInfoFieldSet.setLayout(releaseInfoFieldSetLayout);
        releaseInfoFieldSet.setCollapsible(true);
        releaseInfoFieldSet.setAutoHeight(true);
        releaseInfoFieldSet.setHeading("Release Information");

        /*
         * Add release name.
         */
        name = new TextField<String>();
        name.setId("release-details-pnl-name");
        name.setAllowBlank(false);
        name.setName("name");
        name.setFieldLabel("Name");
        name.setReadOnly(false);
        name.setRegex("^[\\S]*$");
        name.getMessages()
                .setRegexText(
                        "Invalid name specified. The name should not contain any spaces or tab characters. Please check the name and try again.");
        releaseInfoFieldSet.add(name);

        /*
         * Add release date.
         */
        releaseDate = new DateField();
        releaseDate.setId("release-details-pnl-release-date");
        releaseDate.setAllowBlank(false);
        releaseDate.setName("releaseDate");
        releaseDate.setFieldLabel("Release Date");
        releaseDate.setReadOnly(false);
        releaseInfoFieldSet.add(releaseDate);

        /*
         * Add release status.
         */
        releaseStatus = new ComboBox<EnumWrapper<ReleaseStatus>>();
        releaseStatus.setId("release-details-pnl-status");
        releaseStatus.setName("status");
        releaseStatus.setFieldLabel("Status");
        releaseStatus.setDisplayField("value");
        releaseStatus.setEmptyText("Select type..");
        releaseStatus.setTriggerAction(TriggerAction.ALL);
        releaseStatus.setForceSelection(true);
        releaseStatus.setEditable(false);
        releaseStatus.setReadOnly(false);

        /*
         * Add enums.
         */
        final ListStore<EnumWrapper<ReleaseStatus>> store = new ListStore<EnumWrapper<ReleaseStatus>>();
        final ReleaseStatus[] values = ReleaseStatus.values();
        for (final ReleaseStatus releaseStatus : values) {
            final EnumWrapper<ReleaseStatus> wrapper = new EnumWrapper<ReleaseStatus>(releaseStatus);
            store.add(wrapper);
        }

        releaseStatus.setStore(store);

        releaseInfoFieldSet.add(releaseStatus);

        /*
         * Add user group.
         */
        userGroup = new ComboBox<ModelData>();
        userGroup.setId("release-details-pnl-user-group");
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
        releaseInfoFieldSet.add(userGroup);

        /*
         * Add release count.
         */
        releaseCount = new LabelField();
        releaseCount.setId("release-details-pnl-count");
        releaseCount.setName("releaseCount");
        releaseCount.setFieldLabel("Release count");
        releaseCount.setReadOnly(true);
        releaseInfoFieldSet.add(releaseCount);

        /*
         * Add release failure count.
         */
        releaseFailureCount = new LabelField();
        releaseFailureCount.setId("release-details-pnl-fcount");
        releaseFailureCount.setName("releaseFailureCount");
        releaseFailureCount.setFieldLabel("Release failure count");
        releaseFailureCount.setReadOnly(true);
        releaseInfoFieldSet.add(releaseFailureCount);

        /*
         * Add last release date.
         */
        lastReleasedDate = new LabelField();
        lastReleasedDate.setId("release-details-pnl-last-released-date");
        lastReleasedDate.setName("lastReleasedDateFormatted");
        lastReleasedDate.setFieldLabel("Last released date");
        lastReleasedDate.setReadOnly(true);
        releaseInfoFieldSet.add(lastReleasedDate);

        formPanel.add(releaseInfoFieldSet);

        saveButton = new Button();
        saveButton.setId("save-release-btn");
        saveButton.setText("Save");

        final SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                saveRelease();
            }

        };
        saveButton.addSelectionListener(listener);

        formPanel.addButton(saveButton);

        final FormButtonBinding binding = new FormButtonBinding(formPanel);
        binding.addButton(saveButton);

        /*
         * Add release button.
         */
        releaseButton = new Button();
        releaseButton.setId("deploy-release-btn");
        releaseButton.setText("Deploy Release");
        releaseButton.setIcon(AbstractImagePrototype.create(icons.deployRelease()));
        final SelectionListener<ButtonEvent> deployListener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                wholeRelease = true;

                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.QUESTION);
                box.setTitle("Deploy release");
                box.setMessage("Are you sure you want to deploy all artifacts in the release?");
                box.setButtons(MessageBox.YESNO);
                box.show();
                box.addCallback(new Listener<MessageBoxEvent>() {

                    @Override
                    public void handleEvent(final MessageBoxEvent be) {

                        final Button buttonClicked = be.getButtonClicked();
                        final String itemId = buttonClicked.getItemId();
                        if (Dialog.YES.equals(itemId)) {
                            if (window == null) {
                                window = new EnvironmentSelectionWindow(ReleaseDetailsPanel.this);
                            }
                            window.show();

                            final AdmViewPort viewPort = Registry.get(AdmModule.VIEW_PORT);
                            viewPort.mask("Deploying Release...", "x-mask-loading");
                        }
                    }
                });
            }

        };
        releaseButton.addSelectionListener(deployListener);

        formPanel.addButton(releaseButton);

        /*
         * Add release artifacts button.
         */
        releaseArtifactsButton = new Button();
        releaseArtifactsButton.setId("deploy-artifacts-btn");
        releaseArtifactsButton.setText("Deploy Selected Artifacts");
        releaseArtifactsButton.setIcon(AbstractImagePrototype.create(icons.releaseArtifacts()));
        final SelectionListener<ButtonEvent> deployAtifactsListener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                /*
                 * Get selected artifacts.
                 */
                final List<ClientMavenArtifact> artifacts = artifactsTable.getSelectedArtifacts();

                if (artifacts.isEmpty()) {
                    final MessageBox box = new MessageBox();
                    box.setIcon(MessageBox.WARNING);
                    box.setTitle("Deploy artifacts");
                    box.setMessage("No artifacts selected. Please select artifacts and try again.");
                    box.setButtons(MessageBox.OK);
                    box.show();
                } else {
                    wholeRelease = false;

                    if (window == null) {
                        window = new EnvironmentSelectionWindow(ReleaseDetailsPanel.this);
                    }
                    window.show();

                    final AdmViewPort viewPort = Registry.get(AdmModule.VIEW_PORT);
                    viewPort.mask("Deploying Artifacts...");
                }
            }

        };
        releaseArtifactsButton.addSelectionListener(deployAtifactsListener);

        formPanel.addButton(releaseArtifactsButton);

        return formPanel;
    }

    /**
     * Deploys the release that is displayed on this panel to the specified
     * environment.
     * 
     * @param environment
     *            The environment the release will be deployed to.
     */
    public void deployRelease(final ClientEnvironment environment) {
        final DeploymentServiceAsync deploymentService = Registry.get(AdmModule.DEPLOYMENT_SERVICE);

        final Window window = new Window();
        window.setLayout(new FitLayout());
        window.setTitle("Please wait");
        window.setHeading("Please wait");

        final ProgressPanel progressPanel = new ProgressPanel();
        progressPanel.setMessage("Deploying artifacts...");
        progressPanel.setProgressText("Initializing...");

        final Button closeButton = new Button("Close");
        closeButton.setEnabled(false);
        closeButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(final ButtonEvent ce) {
                window.hide();
            }
        });
        window.addButton(closeButton);

        window.add(progressPanel);
        window.setWidth(600);
        window.setHeight(500);
        window.show();
        window.center();

        final Timer t = new Timer() {
            float i;

            @Override
            public void run() {

                deploymentService.getProgress(new AsyncCallback<ClientProgress>() {

                    @Override
                    public void onFailure(final Throwable throwable) {
                        cancel();
                        closeButton.setEnabled(true);
                        progressPanel.stopTailing();
                        progressPanel.setProgress(1, "100% Complete");

                        Info.display("Message", "Failed to deploy artifacts!", "");
                    }

                    @Override
                    public void onSuccess(final ClientProgress progress) {
                        final String description = progress.getDescription();
                        progressPanel.setProgress(i / 100, (int) i + "% Complete");
                        progressPanel.setMessage(description);

                        i = progress.getProgress();

                        if (i >= 100) {
                            cancel();
                            progressPanel.setProgress(1, "100% Complete");
                            closeButton.setEnabled(true);
                            progressPanel.stopTailing();

                            Info.display("Message", "Deployment finished. Check release history for more details.", "");

                            final AdmViewPort viewPort = Registry.get(AdmModule.VIEW_PORT);
                            viewPort.unmask();

                            setModel(model);
                        }
                    }

                });

            }
        };
        t.scheduleRepeating(1000);

        if (wholeRelease) {

            deploymentService.deployRelease(model, environment, new AsyncCallback<ClientRelease>() {

                @Override
                public void onSuccess(final ClientRelease release) {

                }

                @Override
                public void onFailure(final Throwable throwable) {

                }
            });

        } else {
            /*
             * Get selected artifacts.
             */
            final List<ClientMavenArtifact> artifacts = artifactsTable.getSelectedArtifacts();

            /*
             * Deploy artifacts that were selected.
             */
            deploymentService.deployArtifacts(artifacts, model, environment, new AsyncCallback<ClientRelease>() {

                @Override
                public void onSuccess(final ClientRelease release) {

                }

                @Override
                public void onFailure(final Throwable throwable) {

                }
            });
        }
    }

    /**
     * Saves the release.
     */
    private void saveRelease() {
        for (final FieldBinding fieldBinding : fieldBindings) {
            fieldBinding.updateModel();
        }

        final ReleaseServiceAsync releaseService = Registry.get(AdmModule.RELEASE_SERVICE);
        final AsyncCallback<ClientRelease> callback = new AsyncCallback<ClientRelease>() {

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
                box.setTitle("Failed to save release.");
                box.setMessage(builder.toString());
                box.setButtons(MessageBox.OK);
                box.show();
            }

            @Override
            public void onSuccess(final ClientRelease release) {
                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.INFO);
                box.setTitle("Save release.");
                box.setMessage("Successfully saved " + release.getName() + ".");
                box.setButtons(MessageBox.OK);
                box.show();

                setModel(release);
            }

        };

        releaseService.saveRelease(model, callback);
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
