/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 3 okt. 2011 File: MavenModuleSelectionWindow.java
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
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientRelease;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Window that holds the selection panel.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 okt. 2011
 */
public class MavenModuleSelectionWindow extends Window {

    /** The icons of the application. */
    private final AdmIcons icons;

    /** Panel where the selection can be done on. */
    private final MavenModuleSelectionPanel selectionPanel;

    /**
     * Default constructor.
     */
    public MavenModuleSelectionWindow() {
        setHeading("Maven Module Selection");
        setClosable(false);
        setLayout(new FitLayout());
        setSize(500, 600);

        this.icons = Registry.get(AdmModule.ICONS);

        setIcon(AbstractImagePrototype.create(icons.addArtifact()));

        this.selectionPanel = new MavenModuleSelectionPanel(this);

        add(selectionPanel);
    }

    /**
     * Sets the release that is currently being edited.
     * 
     * @param release
     *            The release that will be set.
     */
    public void setRelease(final ClientRelease release) {
        this.selectionPanel.setRelease(release);
    }

}
