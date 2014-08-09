/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 3 okt. 2011 File: ArtifactDetailsWindow.java
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

import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.artifact.ClientMavenArtifact;
import nl.tranquilizedquality.adm.gwt.gui.client.model.artifact.ClientMavenModule;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientRelease;
import nl.tranquilizedquality.adm.gwt.gui.client.view.AdmViewPort;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Window where the details of an artifact are displayed in.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 okt. 2011
 */
public class ArtifactDetailsWindow extends Window {

    /** The icons of the application. */
    private final AdmIcons icons;

    /** Panel where the details of an artifact are displayed in. */
    private final ArtifactDetailsPanel detailsPanel;

    /** Artifact that is managed. */
    private ClientMavenArtifact artifact;

    /**
     * Constructor taking the Maven module and the release where an artifact will be added to.
     * 
     * @param module
     *        The Maven module that will be used to display the common information of an artifact so
     *        you only have to specify the version information.
     * @param release
     *        The release where the artifact will be added to.
     */
    public ArtifactDetailsWindow(final ClientMavenModule module, final ClientRelease release) {
        setHeading("Artifact Creation");
        setClosable(false);
        setLayout(new FitLayout());
        setSize(400, 420);

        this.icons = Registry.get(AdmModule.ICONS);

        setIcon(AbstractImagePrototype.create(icons.addArtifact()));

        this.detailsPanel = new ArtifactDetailsPanel(this);

        artifact = new ClientMavenArtifact();
        artifact.setParentModule(module);
        artifact.setRelease(release);
        this.detailsPanel.setModel(artifact);

        add(detailsPanel);
    }

    /**
     * Sets the module on the new artifact.
     * 
     * @param module
     *        The module that will be set.
     * @param release
     *        The release where the artifact is part of.
     */
    public void setUpArtifact(final ClientMavenModule module, final ClientRelease release) {
        artifact = new ClientMavenArtifact();
        artifact.setParentModule(module);
        artifact.setRelease(release);
        this.detailsPanel.setModel(artifact);
    }

    @Override
    public void hide() {
        final AdmViewPort viewPort = Registry.get(AdmModule.VIEW_PORT);
        viewPort.unmask();

        super.hide();
    }

}
