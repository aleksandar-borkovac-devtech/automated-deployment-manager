/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 1 okt. 2011 File: ArtifactsTable.java
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

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.AbstractServiceException;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.security.AuthorizationServiceAsync;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractRelationListTable;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.controller.navigation.AdmNavigationController;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.artifact.ClientMavenArtifact;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientRelease;
import nl.tranquilizedquality.adm.gwt.gui.client.service.artifact.ArtifactServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.view.AdmViewPort;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.AdmTabs;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.release.ReleaseDetailsPanel;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.dnd.DND.Feedback;
import com.extjs.gxt.ui.client.dnd.GridDragSource;
import com.extjs.gxt.ui.client.dnd.GridDropTarget;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.event.DNDListener;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Table where the artifacts are displayed in the current displayed release.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 1 okt. 2011
 */
public class ArtifactsTable extends AbstractRelationListTable<MavenArtifact, ClientMavenArtifact> {

    /** The icons of the application. */
    private final AdmIcons icons;

    /** The release that is being edited. */
    private ClientRelease release;

    /** Window that contains the maven module selection. */
    private MavenModuleSelectionWindow window;

    /** Button to add an artifact to a release. */
    private Button addArtifactButton;

    /** Button to delete an artifact from a release. */
    private Button deleteArtifactButton;

    /** Panel where the details of a release are displayed on. */
    private final ReleaseDetailsPanel detailPanel;

    /**
     * Constructor taking the detail panel where the release details are displayed on.
     * 
     * @param detailPanel
     *        The detail panel where the release details are displayed on.
     */
    public ArtifactsTable(final ReleaseDetailsPanel detailPanel) {
        setHeading("Artifacts");

        this.detailPanel = detailPanel;
        this.icons = Registry.get(AdmModule.ICONS);

        setIcon(AbstractImagePrototype.create(icons.artifacts()));

        initializeWidgets();
        performPrivilegeCheck();

        /*
         * Set single selection mode.
         */
        final GridSelectionModel<BeanModel> selectionModel = grid.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.MULTI);
    }

    /**
     * Performs some checks if the logged in user is allowed to perform certain actions within the
     * application.
     */
    private void performPrivilegeCheck() {
        final AuthorizationServiceAsync authorizationService = Registry.get(AdmModule.AUTHORIZATION_SERVICE);
        authorizationService.isLoggedInUserAuthorized("ADD_ARTIFACT", new AsyncCallback<Boolean>() {

            @Override
            public void onFailure(final Throwable throwable) {
                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.ERROR);
                box.setTitle("Add artifact check.");
                box.setMessage(throwable.getMessage());
                box.setButtons(MessageBox.OK);
                box.show();
            }

            @Override
            public void onSuccess(final Boolean authorized) {
                if (authorized) {
                    addArtifactButton.enable();
                } else {
                    addArtifactButton.disable();
                }

                final ClientRelease release = detailPanel.getModelObject();
                if (!release.isPersistent()) {
                    addArtifactButton.disable();
                }
            }

        });

        authorizationService.isLoggedInUserAuthorized("DELETE_ARTIFACT", new AsyncCallback<Boolean>() {

            @Override
            public void onFailure(final Throwable throwable) {
                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.ERROR);
                box.setTitle("Delete artifact check.");
                box.setMessage(throwable.getMessage());
                box.setButtons(MessageBox.OK);
                box.show();
            }

            @Override
            public void onSuccess(final Boolean authorized) {
                if (authorized) {
                    deleteArtifactButton.enable();
                } else {
                    deleteArtifactButton.disable();
                }

                final ClientRelease release = detailPanel.getModelObject();
                if (!release.isPersistent()) {
                    deleteArtifactButton.disable();
                }
            }

        });
    }

    @Override
    protected String getPanelStateId() {
        return ArtifactsTable.class.getName();
    }

    @Override
    protected Class<ClientMavenArtifact> getBeanModelClass() {
        return ClientMavenArtifact.class;
    }

    @Override
    protected List<ColumnConfig> createColumns() {
        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        ColumnConfig column = new ColumnConfig();
        column.setId("rank");
        column.setHeader("Rank");
        column.setWidth(40);
        column.setSortable(false);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("moduleName");
        column.setHeader("Name");
        column.setWidth(100);
        column.setSortable(false);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("group");
        column.setHeader("Group");
        column.setWidth(100);
        column.setSortable(false);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("artifactId");
        column.setHeader("Artifact Id");
        column.setWidth(120);
        column.setSortable(false);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("type");
        column.setHeader("Type");
        column.setWidth(100);
        column.setSortable(false);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("version");
        column.setHeader("Version");
        column.setWidth(100);
        column.setSortable(false);
        configs.add(column);

        return configs;
    }

    /**
     * Retrieves the selected item and displays it in the release details
     * screen.
     */
    private void view() {
        final GridSelectionModel<BeanModel> selectionModel = grid.getSelectionModel();
        final BeanModel selectedItem = selectionModel.getSelectedItem();

        if (selectedItem != null) {
            final ClientMavenArtifact artifact = selectedItem.getBean();
            final AdmNavigationController controller = Registry.get(AdmModule.NAVIGATION_CONTROLLER);
            controller.selectTab(AdmTabs.ARTIFACT_DETAILS_TAB, artifact);
        }
    }

    @Override
    protected void handleDoubleClick(final GridEvent<BeanModel> gridEvent) {
        view();
    }

    @Override
    protected void initializeWidgets() {
        addArtifactButton = new Button("Add Artifact");
        addArtifactButton.setId("release-details-add-artifact-btn");
        addArtifactButton.setIcon(AbstractImagePrototype.create(icons.addArtifact()));
        final SelectionListener<ButtonEvent> addArtifactListener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                addArtifact();
            }
        };
        addArtifactButton.addSelectionListener(addArtifactListener);

        menuBarButtons.add(addArtifactButton);

        deleteArtifactButton = new Button("Delete Artifact");
        deleteArtifactButton.setId("release-details-delete-artifact-btn");
        deleteArtifactButton.setIcon(AbstractImagePrototype.create(icons.removeArtifact()));
        final SelectionListener<ButtonEvent> deleteArtifactListener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                deleteArtifact();
            }
        };
        deleteArtifactButton.addSelectionListener(deleteArtifactListener);

        menuBarButtons.add(deleteArtifactButton);

        /*
         * Initialize the widgets.
         */
        super.initializeWidgets();

        new GridDragSource(grid);

        final GridDropTarget target = new GridDropTarget(grid);
        target.setAllowSelfAsSource(true);
        target.setFeedback(Feedback.INSERT);
        final DNDListener listener = new DNDListener() {

            @Override
            public void dragDrop(final DNDEvent e) {
                super.dragDrop(e);

                /*
                 * Reorder collection.
                 */
                final ListStore<BeanModel> store = grid.getStore();
                final List<BeanModel> models = store.getModels();
                int i = 1;
                for (final BeanModel beanModel : models) {
                    /*
                     * Retrieve bean and update rank value.
                     */
                    final ClientMavenArtifact artifact = beanModel.getBean();
                    artifact.setRank(i);

                    /*
                     * Go to next rank.
                     */
                    i++;

                    /*
                     * Save artifact.
                     */
                    saveArtifact(artifact);

                    /*
                     * Notify store to update models in the GUI.
                     */
                    store.update(beanModel);
                }

            }

        };

        target.addDNDListener(listener);
    }

    /**
     * Stores the specified artifact by calling the GWT rpc service.
     * 
     * @param artifact
     *        The artifact that will be saved.
     */
    private void saveArtifact(final ClientMavenArtifact artifact) {
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
                final AdmViewPort viewPort = Registry.get(AdmModule.VIEW_PORT);
                viewPort.unmask();

                final ReleaseDetailsPanel pnl = Registry.get(ReleaseDetailsPanel.RELEASE_DETAIL_PANEL);
                pnl.setModel((ClientRelease) artifact.getRelease());
            }

        };

        artifactService.saveMavenArtifact(artifact, callback);
    }

    /**
     * Adds a new artifact by opening a selection window where you can select
     * the Maven module where an artifact should be created from.
     */
    private void addArtifact() {
        if (window == null) {
            window = new MavenModuleSelectionWindow();
        }

        window.setRelease(release);
        window.show();

        final AdmViewPort viewPort = Registry.get(AdmModule.VIEW_PORT);
        viewPort.mask();
    }

    /**
     * Deletes the selected artifact.
     */
    private void deleteArtifact() {
        final ArtifactServiceAsync artifactService = Registry.get(AdmModule.ARTIFACT_SERVICE);

        final GridSelectionModel<BeanModel> selectionModel = grid.getSelectionModel();
        final BeanModel selectedItem = selectionModel.getSelectedItem();
        if (selectedItem != null) {
            final ClientMavenArtifact artifact = selectedItem.getBean();

            final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                @Override
                public void onSuccess(final Void value) {
                    detailPanel.setModel(release);

                    deleteArtifactButton.enable();
                }

                @Override
                public void onFailure(final Throwable throwable) {
                    deleteArtifactButton.enable();

                }
            };
            artifactService.deleteMavenArtifact(artifact, callback);
        }

    }

    /**
     * Sets the release where the artifacts are displayed for.
     * 
     * @param release
     *        the release to set
     */
    public void setRelease(final ClientRelease release) {
        this.release = release;
    }

    /**
     * Retrieves the selected artifacts in the grid.
     * 
     * @return Returns a {@link List} containing {@link ClientMavenArtifact} objects or an empty one
     *         if none are selected.
     */
    public List<ClientMavenArtifact> getSelectedArtifacts() {
        final GridSelectionModel<BeanModel> selectionModel = grid.getSelectionModel();
        final List<BeanModel> selectedItems = selectionModel.getSelectedItems();

        final List<ClientMavenArtifact> artifacts = new ArrayList<ClientMavenArtifact>();
        for (final BeanModel beanModel : selectedItems) {
            final ClientMavenArtifact artifact = beanModel.getBean();

            artifacts.add(artifact);
        }

        return artifacts;
    }

    /**
     * Enables the add and delete buttons so you can modify the release artifacts.
     */
    public void enableButtons() {
        addArtifactButton.enable();
        deleteArtifactButton.enable();
    }

}
