/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: Aug 3, 2012 File: fReleaseExecutionLogDetailsPanel.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.view.dashboard.release.history
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
package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.release.history;

import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractDetailPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientReleaseExecutionLog;
import nl.tranquilizedquality.adm.gwt.gui.client.service.release.ReleaseServiceAsync;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Panel where the logs of a specific release execution are displayed on.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 3, 2012
 */
public class ReleaseExecutionLogDetailsPanel extends AbstractDetailPanel<ClientReleaseExecutionLog> {

    /** The icons of the application. */
    private final AdmIcons icons;

    /** The name of the release. */
    private TextField<String> artifactName;

    /** The logs of the release. */
    private TextArea logs;

    /**
     * Default constructor.
     */
    public ReleaseExecutionLogDetailsPanel() {
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
         * Create panel where the logs are displayed in.
         */
        final LayoutContainer logPanel = new LayoutContainer();
        logPanel.setLayout(new FillLayout());

        logs = new TextArea();
        logs.setId("release-history-details-release-logs");
        logs.setName("logs");
        logs.setFieldLabel("Logs");
        logs.setAllowBlank(true);
        logs.setReadOnly(true);
        logPanel.add(logs);

        final BorderLayoutData northLayoutData = new BorderLayoutData(LayoutRegion.NORTH);
        northLayoutData.setSize(120);
        northLayoutData.setCollapsible(true);

        add(this.formPanel, northLayoutData);
        add(logPanel, new BorderLayoutData(LayoutRegion.CENTER));
    }

    @Override
    public void setModel(final ClientReleaseExecutionLog model) {
        this.model = model;

        if (this.model.isPersistent()) {
            final AsyncCallback<ClientReleaseExecutionLog> callback = new AsyncCallback<ClientReleaseExecutionLog>() {

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
                public void onSuccess(final ClientReleaseExecutionLog releaseExecution) {
                    ReleaseExecutionLogDetailsPanel.this.model = releaseExecution;
                    bindModel(ReleaseExecutionLogDetailsPanel.this.model);

                    final String logValue = model.getLogs();
                    logs.setValue(logValue);
                }

            };

            final ReleaseServiceAsync releaseService = Registry.get(AdmModule.RELEASE_SERVICE);
            releaseService.findReleaseExecutionLogById(this.model.getId(), callback);
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
        releaseInfoFieldSet.setHeading("Release Execution Log");

        /*
         * Add release name.
         */
        artifactName = new TextField<String>();
        artifactName.setAllowBlank(false);
        artifactName.setName("artifactName");
        artifactName.setFieldLabel("Artifact");
        artifactName.setReadOnly(true);
        releaseInfoFieldSet.add(artifactName);

        formPanel.add(releaseInfoFieldSet, new FormData("100%"));

        return formPanel;
    }

}
