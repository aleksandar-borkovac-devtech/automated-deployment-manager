/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 15 sep. 2011 File: DestinationLocationWindow.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.view.dashboard.environment
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
package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.environment.location;

import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameter;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientDeployerParameter;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientDestination;
import nl.tranquilizedquality.adm.gwt.gui.client.service.environment.EnvironmentServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.environment.DestinationDetailsPanel;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Window where you can edit a {@link DeployerParameter}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 15 sep. 2011
 */
public class DeployerParameterWindow extends Window {

    /** The icons of the application. */
    private final AdmIcons icons;

    /** The {@link ClientDestination} where locations will be added to. */
    private ClientDestination clientDestination;

    /** The panel that contains the grantable roles add functionality. */
    private final DeployerParameterDetailsPanel destinationLocationDetailsPanel;

    /** The main panel where the details of a user are displayed on. */
    private final DestinationDetailsPanel destinationDetailsPanel;

    /**
     * @param destination
     * @param destinationDetailsPanel
     */
    public DeployerParameterWindow(final ClientDestination destination, final DestinationDetailsPanel destinationDetailsPanel) {
        setHeading("Deployer parameter");

        setLayout(new FitLayout());
        setSize(400, 200);

        this.clientDestination = destination;
        this.destinationDetailsPanel = destinationDetailsPanel;

        this.icons = Registry.get(AdmModule.ICONS);

        setIcon(AbstractImagePrototype.create(icons.destinationLocation()));

        this.destinationLocationDetailsPanel = new DeployerParameterDetailsPanel();
        destinationLocationDetailsPanel.setDestination(destination);
        destinationLocationDetailsPanel.setWindow(this);

        add(destinationLocationDetailsPanel);
    }

    @Override
    public void hide() {
        if (this.clientDestination != null) {

            final AsyncCallback<ClientDestination> callback = new AsyncCallback<ClientDestination>() {

                @Override
                public void onFailure(final Throwable throwable) {
                    final MessageBox box = new MessageBox();
                    box.setIcon(MessageBox.ERROR);
                    box.setTitle("Retrieve parameter.");
                    box.setMessage(throwable.getMessage());
                    box.setButtons(MessageBox.OK);
                    box.show();
                }

                @Override
                public void onSuccess(final ClientDestination destination) {
                    setClientDestination(destination);

                    /*
                     * Set the model on the details panel.
                     */
                    destinationDetailsPanel.setModel(destination);
                }

            };

            final EnvironmentServiceAsync environmentService = Registry.get(AdmModule.ENVIRONMENT_SERVICE);
            environmentService.findDestinationById(this.clientDestination.getId(), callback);
        }

        final Viewport viewport = Registry.get(AdmModule.VIEW_PORT);
        viewport.unmask();

        super.hide();
    }

    /**
     * @param clientDestination
     *            the clientDestination to set
     */
    public void setClientDestination(final ClientDestination clientDestination) {
        this.clientDestination = clientDestination;
        destinationLocationDetailsPanel.setDestination(this.clientDestination);
    }

    public void setModel(final ClientDeployerParameter location) {
        destinationLocationDetailsPanel.setModel(location);
    }

    public void refreshParameterTypes() {
        destinationLocationDetailsPanel.refreshParameterTypes();
    }

}
