/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 26 sep. 2011 File: ReleaseTable.java
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
import java.util.Date;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.Release;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseStatus;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.security.AuthorizationServiceAsync;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractGroupingGridPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.controller.navigation.AdmNavigationController;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.artifact.ClientMavenArtifact;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientRelease;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientReleaseSearchCommand;
import nl.tranquilizedquality.adm.gwt.gui.client.service.release.ReleaseServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.service.release.ReleaseServiceException;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.AdmTabs;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.repository.RepositoryTable;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.GridGroupRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.GroupColumnData;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.extjs.gxt.ui.client.widget.grid.RowNumberer;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Table where the releases are displayed in using grouping headers.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 26 sep. 2011
 */
public class ReleaseTable extends AbstractGroupingGridPanel {

    /** The icons of the application. */
    private final AdmIcons icons;

    /** The search criteria. */
    private final ClientReleaseSearchCommand sc;

    /** Service used to retrieve the releases. */
    private ReleaseServiceAsync releaseService;

    /** The add button. */
    private Button addButton;

    /** Button that removes selected releases. */
    private Button removeButton;

    /** Button used to archive selected releases. */
    private Button archiveButton;

    /** Button used to unarchive selected releases. */
    private Button unArchiveButton;

    /** The edit menu item. */
    private MenuItem editMenuItem;

    /** Menu item used to archive selected release. */
    private MenuItem archiveMenuItem;

    /**
     * Constructor that takes the search criteria to filter on.
     * 
     * @param sc
     *        The search criteria.
     */
    public ReleaseTable(final ClientReleaseSearchCommand sc) {
        setHeading("Releases");
        setId("release-search-result-table");

        this.sc = sc;
        this.groupingField = "release";

        this.icons = Registry.get(AdmModule.ICONS);

        setIcon(AbstractImagePrototype.create(icons.releases()));

        initializeWidgets();

        performPrivilegeCheck();
    }

    private void performPrivilegeCheck() {
        final AuthorizationServiceAsync authorizationService = Registry.get(AdmModule.AUTHORIZATION_SERVICE);
        authorizationService.isLoggedInUserAuthorized("ADD_RELEASE", new AsyncCallback<Boolean>() {

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
                    addButton.enable();
                    editMenuItem.enable();
                } else {
                    addButton.disable();
                    editMenuItem.disable();
                }
            }

        });

        authorizationService.isLoggedInUserAuthorized("DELETE_RELEASE", new AsyncCallback<Boolean>() {

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
                    removeButton.enable();
                } else {
                    removeButton.disable();
                }
            }

        });
    }

    @Override
    protected void initializeWidgets() {
        releaseService = Registry.get(AdmModule.RELEASE_SERVICE);

        proxy = new RpcProxy<PagingLoadResult<ClientMavenArtifact>>() {

            @Override
            public void load(final Object loadConfig, final AsyncCallback<PagingLoadResult<ClientMavenArtifact>> callback) {
                releaseService.findReleasesAndArtifacts((PagingLoadConfig) loadConfig, ReleaseTable.this.sc, callback);
            }
        };

        this.panelStateId = RepositoryTable.class.getName();

        addButton = new Button("Add");
        addButton.setId("add-new-release-btn");
        addButton.setIcon(AbstractImagePrototype.create(icons.addArtifact()));

        final SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                add();
            }

        };
        addButton.addSelectionListener(listener);
        this.menuBarButtons.add(addButton);

        removeButton = new Button("Remove");
        removeButton.setId("remove-release-btn");
        removeButton.setIcon(AbstractImagePrototype.create(icons.removeArtifact()));
        removeButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                remove();
            }

        });
        this.menuBarButtons.add(removeButton);

        archiveButton = new Button("Archive");
        archiveButton.setId("archive-release-btn");
        archiveButton.setIcon(AbstractImagePrototype.create(icons.archiveArtifact()));
        archiveButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                archive();
            }

        });
        this.menuBarButtons.add(archiveButton);

        unArchiveButton = new Button("Unarchive");
        unArchiveButton.setId("unarchive-release-btn");
        unArchiveButton.setIcon(AbstractImagePrototype.create(icons.unArchiveArtifact()));
        unArchiveButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                unArchive();
            }

        });
        this.menuBarButtons.add(unArchiveButton);

        editMenuItem = new MenuItem();
        editMenuItem.setId("edit-release-menu-item");
        editMenuItem.setIcon(AbstractImagePrototype.create(icons.edit()));
        editMenuItem.setText("Edit");
        editMenuItem.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(final MenuEvent ce) {
                view();
            }
        });
        menuItems.add(editMenuItem);

        archiveMenuItem = new MenuItem();
        archiveMenuItem.setId("archive-release-menu-item");
        archiveMenuItem.setIcon(AbstractImagePrototype.create(icons.archiveArtifact()));
        archiveMenuItem.setText("Archive");
        archiveMenuItem.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(final MenuEvent ce) {
                archive();
            }
        });
        menuItems.add(archiveMenuItem);

        super.initializeWidgets();

        final GroupingView view = (GroupingView) grid.getView();

        view.setGroupRenderer(new GridGroupRenderer() {
            @Override
            public String render(final GroupColumnData data) {
                final ModelData modelData = data.models.get(0);
                final String releaseName = modelData.get("releaseName");
                final Date releaseDate = modelData.get("releaseDate");
                final ReleaseStatus status = modelData.get("releaseStatus");
                String releaseDateValue = "";

                if (releaseDate != null) {
                    final DateTimeFormat format = DateTimeFormat.getFormat("dd-MM-yyyy");
                    releaseDateValue = format.format(releaseDate);
                }

                return releaseName + " [ Release date: " + releaseDateValue + "] [Status: " + status + " ]";
            }
        });

    }

    /**
     * Refreshes the content of the table.
     */
    public void refreshTable() {
        super.refresh();
    }

    /**
     * Creates a fresh {@link ClientMavenArtifact} and navigates to the details
     * panel.
     */
    private void add() {
        final ClientRelease artifact = new ClientRelease();

        final AdmNavigationController controller = Registry.get(AdmModule.NAVIGATION_CONTROLLER);
        controller.selectTab(AdmTabs.RELEASE_DETAILS_TAB, artifact);
    }

    /**
     * Removes the selected release if that is allowed. Releases can only be
     * removed if they aren't
     */
    private void remove() {
        final GridSelectionModel<BeanModel> selectionModel = grid.getSelectionModel();
        final BeanModel selectedItem = selectionModel.getSelectedItem();
        final ClientRelease release = selectedItem.getBean();

        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            @Override
            public void onFailure(final Throwable throwable) {

                final StringBuilder builder = new StringBuilder();
                if (throwable instanceof ReleaseServiceException) {
                    final ReleaseServiceException exception = (ReleaseServiceException) throwable;
                    final List<String> errors = exception.getErrors();
                    if (!errors.isEmpty()) {
                        for (final String msg : errors) {
                            builder.append(msg);
                        }
                    } else {
                        builder.append(throwable.getMessage());
                    }

                } else {
                    builder.append(throwable.getMessage());
                }

                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.ERROR);
                box.setTitle("Remove release.");
                box.setMessage(builder.toString());
                box.setButtons(MessageBox.OK);
                box.show();
            }

            @Override
            public void onSuccess(final Void result) {
                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.INFO);
                box.setTitle("Remove release.");
                box.setMessage("Release removed succesfully.");
                box.setButtons(MessageBox.OK);
                box.show();
            }

        };
        this.releaseService.removeRelease(release, callback);
    }

    @Override
    protected List<ColumnConfig> createColumns() {
        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        configs.add(new RowNumberer());

        ColumnConfig column = new ColumnConfig();
        column.setId("group");
        column.setHeader("Group");
        column.setWidth(100);
        column.setSortable(true);
        configs.add(column);

        column.setId("release");
        column.setHeader("Release");
        column.setWidth(100);
        column.setSortable(true);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("moduleName");
        column.setHeader("Module Name");
        column.setWidth(100);
        column.setSortable(true);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("artifactId");
        column.setHeader("Artifact Id");
        column.setWidth(60);
        column.setSortable(false);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("type");
        column.setHeader("Type");
        column.setWidth(100);
        column.setSortable(true);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("version");
        column.setHeader("Version");
        column.setWidth(100);
        column.setSortable(true);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("releaseName");
        column.setHeader("Release");
        column.setWidth(100);
        column.setSortable(true);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("created");
        column.setHeader("Created");
        column.setWidth(100);
        column.setSortable(true);
        column.setDateTimeFormat(DateTimeFormat.getShortDateFormat());
        configs.add(column);

        column = new ColumnConfig();
        column.setId("createdBy");
        column.setHeader("Created By");
        column.setWidth(60);
        column.setSortable(false);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("altered");
        column.setHeader("Altered");
        column.setWidth(100);
        column.setSortable(true);
        column.setDateTimeFormat(DateTimeFormat.getShortDateFormat());
        configs.add(column);

        column = new ColumnConfig();
        column.setId("alteredBy");
        column.setHeader("Altered By");
        column.setWidth(60);
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
            final Release release = artifact.getRelease();

            final AdmNavigationController controller = Registry.get(AdmModule.NAVIGATION_CONTROLLER);
            controller.selectTab(AdmTabs.RELEASE_DETAILS_TAB, release);
        }
    }

    /**
     * Archived the selected release.
     */
    private void archive() {
        final GridSelectionModel<BeanModel> selectionModel = grid.getSelectionModel();
        final BeanModel selectedItem = selectionModel.getSelectedItem();
        final ClientMavenArtifact artifact = selectedItem.getBean();
        final ClientRelease release = (ClientRelease) artifact.getRelease();

        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            @Override
            public void onFailure(final Throwable throwable) {

                final StringBuilder builder = new StringBuilder();
                if (throwable instanceof ReleaseServiceException) {
                    final ReleaseServiceException exception = (ReleaseServiceException) throwable;
                    final List<String> errors = exception.getErrors();
                    if (!errors.isEmpty()) {
                        for (final String msg : errors) {
                            builder.append(msg);
                        }
                    } else {
                        builder.append(throwable.getMessage());
                    }

                } else {
                    builder.append(throwable.getMessage());
                }

                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.ERROR);
                box.setTitle("Archive release.");
                box.setMessage(builder.toString());
                box.setButtons(MessageBox.OK);
                box.show();

                refresh();
            }

            @Override
            public void onSuccess(final Void result) {
                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.INFO);
                box.setTitle("Archive release.");
                box.setMessage("Release archived succesfully.");
                box.setButtons(MessageBox.OK);
                box.show();

                refresh();
            }

        };
        this.releaseService.archiveRelease(release, callback);
    }

    /**
     * Unarchived the selected release.
     */
    private void unArchive() {
        final GridSelectionModel<BeanModel> selectionModel = grid.getSelectionModel();
        final BeanModel selectedItem = selectionModel.getSelectedItem();
        final ClientMavenArtifact artifact = selectedItem.getBean();
        final ClientRelease release = (ClientRelease) artifact.getRelease();

        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            @Override
            public void onFailure(final Throwable throwable) {

                final StringBuilder builder = new StringBuilder();
                if (throwable instanceof ReleaseServiceException) {
                    final ReleaseServiceException exception = (ReleaseServiceException) throwable;
                    final List<String> errors = exception.getErrors();
                    if (!errors.isEmpty()) {
                        for (final String msg : errors) {
                            builder.append(msg);
                        }
                    } else {
                        builder.append(throwable.getMessage());
                    }

                } else {
                    builder.append(throwable.getMessage());
                }

                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.ERROR);
                box.setTitle("Unarchive release.");
                box.setMessage(builder.toString());
                box.setButtons(MessageBox.OK);
                box.show();

                refresh();
            }

            @Override
            public void onSuccess(final Void result) {
                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.INFO);
                box.setTitle("Unarchive release.");
                box.setMessage("Release unarchived succesfully.");
                box.setButtons(MessageBox.OK);
                box.show();

                refresh();
            }

        };
        this.releaseService.unArchiveRelease(release, callback);
    }

    @Override
    protected void handleDoubleClick() {
        view();
    }

}
