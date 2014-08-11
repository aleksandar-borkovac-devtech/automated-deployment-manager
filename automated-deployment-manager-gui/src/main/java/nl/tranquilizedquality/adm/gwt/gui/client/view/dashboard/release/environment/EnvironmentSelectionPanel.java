/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 7 okt. 2011 File: EnvironmentSelectionPanel.java
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

import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.release.ReleaseDetailsPanel;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

/**
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 7 okt. 2011
 */
public class EnvironmentSelectionPanel extends LayoutContainer {

    /** Window where this panel is displayed in. */
    private final EnvironmentSelectionWindow window;

    /** Panel where the release details are displayed on. */
    private final ReleaseDetailsPanel detailsPanel;

    public EnvironmentSelectionPanel(final ReleaseDetailsPanel detailsPanel,
            final EnvironmentSelectionWindow window) {
        this.detailsPanel = detailsPanel;
        this.window = window;

        initializeWidgets();
    }

    private void initializeWidgets() {
        final FitLayout layout = new FitLayout();
        setLayout(layout);

        final ContentPanel gridPanel = createGridPanel();

        add(gridPanel);
    }

    private ContentPanel createGridPanel() {
        return new EnvironmentSelectionTable(detailsPanel, window);
    }

}
