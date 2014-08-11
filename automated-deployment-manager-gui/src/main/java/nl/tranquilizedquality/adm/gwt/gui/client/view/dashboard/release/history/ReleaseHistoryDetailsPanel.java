/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 15 okt. 2011 File: ReleaseHistoryDetailsPanel.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.view.dashboard.release.history
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
package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.release.history;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.DeployStatus;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifactSnapshot;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseStepExecution;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractDetailPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientReleaseExecution;
import nl.tranquilizedquality.adm.gwt.gui.client.service.release.ReleaseServiceAsync;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Details panel where the details of a release execution is displayed in.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 15 okt. 2011
 */
public class ReleaseHistoryDetailsPanel extends AbstractDetailPanel<ClientReleaseExecution> {

    /** The icons of the application. */
    private final AdmIcons icons;

    /** The name of the release. */
    private LabelField name;

    /** The status of the release. */
    private LabelField releaseStatus;

    /** The date when the release was done. */
    private LabelField releaseDate;

    /** Table where the artifacts that were released are displayed in. */
    private SnapshotArtifactsTable artifactsTable;

    /** Table where the steps of a release are displayed in. */
    private StepExecutionTable stepsTable;

    /**
     * Default constructor.
     */
    public ReleaseHistoryDetailsPanel() {
        icons = Registry.get(AdmModule.ICONS);

        initializeWidgets();
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
         * Create relations panel.
         */
        final LayoutContainer relationsPanel = createRelationsPanel();

        add(this.formPanel, new BorderLayoutData(LayoutRegion.NORTH, 170, 170, 170));
        add(relationsPanel, new BorderLayoutData(LayoutRegion.CENTER, 600));
    }

    /**
     * Creates the relations panel.
     * 
     * @return Returns a {@link LayoutContainer}.
     */
    private LayoutContainer createRelationsPanel() {
        final LayoutContainer container = new LayoutContainer();
        final RowLayout layout = new RowLayout(Orientation.VERTICAL);
        container.setLayout(layout);

        artifactsTable = new SnapshotArtifactsTable();
        stepsTable = new StepExecutionTable();

        container.add(artifactsTable, new RowData(1, 0.5, new Margins(4, 0, 4, 4)));
        container.add(stepsTable, new RowData(1, 0.5, new Margins(4, 4, 4, 4)));

        return container;
    }

    @Override
    public void setModel(final ClientReleaseExecution model) {
        this.model = model;

        if (this.model.isPersistent()) {
            final AsyncCallback<ClientReleaseExecution> callback = new AsyncCallback<ClientReleaseExecution>() {

                @Override
                public void onFailure(final Throwable throwable) {
                    final MessageBox box = new MessageBox();
                    box.setIcon(MessageBox.ERROR);
                    box.setTitle("Retrieve release history.");
                    box.setMessage(throwable.getMessage());
                    box.setButtons(MessageBox.OK);
                    box.show();
                }

                @Override
                public void onSuccess(final ClientReleaseExecution releaseExecution) {
                    ReleaseHistoryDetailsPanel.this.model = releaseExecution;
                    bindModel(ReleaseHistoryDetailsPanel.this.model);

                    final List<MavenArtifactSnapshot> artifacts = releaseExecution.getArtifacts();
                    artifactsTable.setModel(artifacts);

                    final List<ReleaseStepExecution> stepExecutions = releaseExecution.getStepExecutions();
                    stepsTable.setModel(stepExecutions);

                    final DeployStatus deployStatus = model.getReleaseStatus();
                    switch (deployStatus) {
                        case FAILED:
                            releaseStatus.setStyleAttribute("color", "red");
                            break;

                        case ONGOING:
                            releaseStatus.setStyleAttribute("color", "orange");
                            break;

                        case SUCCESS:
                            releaseStatus.setStyleAttribute("color", "green");
                            break;
                    }
                }

            };

            final ReleaseServiceAsync releaseService = Registry.get(AdmModule.RELEASE_SERVICE);
            releaseService.findReleaseExecutionById(this.model.getId(), callback);
        }
        else {
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
        formPanel.setFrame(true);
        formPanel.setLabelWidth(110);
        formPanel.setIcon(AbstractImagePrototype.create(icons.releaseHistory()));
        formPanel.setButtonAlign(HorizontalAlignment.LEFT);

        /*
         * Create field set for release related information.
         */
        final FieldSet releaseInfoFieldSet = new FieldSet();
        releaseInfoFieldSet.setLayout(new FormLayout());
        releaseInfoFieldSet.setCollapsible(true);
        releaseInfoFieldSet.setAutoHeight(true);
        releaseInfoFieldSet.setHeading("Release Information");

        /*
         * Add release name.
         */
        name = new LabelField();
        name.setId("release-history-details-pnl-name");
        name.setName("name");
        name.setFieldLabel("Release");
        name.setReadOnly(true);
        releaseInfoFieldSet.add(name);

        /*
         * Add release status.
         */
        releaseStatus = new LabelField();
        releaseStatus.setId("release-history-details-pnl-status");
        releaseStatus.setName("releaseStatus");
        releaseStatus.setFieldLabel("Status");
        releaseStatus.setReadOnly(true);
        releaseInfoFieldSet.add(releaseStatus);

        /*
         * Add release date.
         */
        releaseDate = new LabelField();
        releaseDate.setId("release-history-details-release-date");
        releaseDate.setName("releaseDateFormatted");
        releaseDate.setFieldLabel("Released on");
        releaseDate.setWidth(50);
        releaseDate.setEnabled(true);
        releaseDate.setReadOnly(true);
        releaseInfoFieldSet.add(releaseDate);

        formPanel.add(releaseInfoFieldSet);

        return formPanel;
    }

}
