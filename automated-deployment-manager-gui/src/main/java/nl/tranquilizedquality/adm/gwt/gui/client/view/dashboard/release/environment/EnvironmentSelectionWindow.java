/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 7 okt. 2011 File: EnvironmentSelectionWindow.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.view.dashboard.release.environment
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
package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.release.environment;

import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.view.AdmViewPort;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.release.ReleaseDetailsPanel;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Window where you can select the environment where you want to deploy to.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 7 okt. 2011
 */
public class EnvironmentSelectionWindow extends Window {

    /** The icons of the application. */
    private final AdmIcons icons;

    /** The selection panel. */
    private final EnvironmentSelectionPanel selectionPanel;

    /** Determines if it should unmask the view. */
    private boolean unmask = true;

    public void setUnmask(boolean unmask) {
        this.unmask = unmask;
    }

    /**
     * Default constructor.
     * 
     * @param detailsPanel
     *            Panel where the release is displayed on.
     */
    public EnvironmentSelectionWindow(final ReleaseDetailsPanel detailsPanel) {
        setHeading("Environment Selection");
        setClosable(true);
        setLayout(new FitLayout());
        setSize(500, 600);

        this.icons = Registry.get(AdmModule.ICONS);

        setIcon(AbstractImagePrototype.create(icons.destination()));

        this.selectionPanel = new EnvironmentSelectionPanel(detailsPanel, this);

        add(selectionPanel);
    }

    @Override
    public void hide() {
        super.hide();

        if (unmask) {
            final AdmViewPort viewPort = Registry.get(AdmModule.VIEW_PORT);
            viewPort.unmask();
        }

        unmask = true;
    }

}
