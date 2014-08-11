/*
 * @(#)DestinationHostSteps.java 8 mrt. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.itest.gui.step;

import static junit.framework.Assert.assertTrue;
import nl.tranquilizedquality.adm.commons.business.domain.Protocol;
import nl.tranquilizedquality.adm.itest.business.domain.DestinationHostDto;
import nl.tranquilizedquality.adm.itest.business.manager.HostManagementManager;

import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Required;

/**
 * Steps used to perform actions regarding destination host management.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 8 mrt. 2013
 */
public class DestinationHostSteps extends LoginSteps {

    /** Manager used to perform actions regarding host management. */
    private HostManagementManager hostManagementManager;

    /**
     * Initializes the managers.
     */
    @Override
    public void init() {
        super.init();
        hostManagementManager.setUpPages(getSelenium(), getConditionRunner());
        hostManagementManager.cleanUp();
    }

    @When("the user adds a host with the name $hostName on port $port using $protocol protocol logging in with $username and a private key $privateKey")
    public void addHostWithPrivateKeyAuthentication(@Named("hostName") final String hostName,
            @Named("protocol") final String protocol,
            @Named("port") final int port, @Named("username") final String username, @Named("privateKey") final String privateKey) {

        dashboardManager.openAddNewDestinationHostTab();

        final DestinationHostDto host = new DestinationHostDto();
        host.setHostName(hostName);
        host.setPort(port);
        host.setPrivateKey(privateKey);
        host.setProtocol(Protocol.valueOf(protocol));
        host.setUsername(username);

        hostManagementManager.addHost(host);
    }

    @Then("there should be a host created with the name $hostName with the specified private key $privateKey")
    public void validateIfPrivateKeyIsStored(@Named("hostName") final String hostName, @Named("privateKey") final String privateKey) {
        final DestinationHostDto host = new DestinationHostDto();
        host.setHostName(hostName);
        host.setPrivateKey(privateKey);

        final boolean privateKeyStoredForHost = hostManagementManager.isPrivateKeyStoredForHost(host);
        assertTrue("No private key was stored for host " + hostName, privateKeyStoredForHost);
    }

    @Required
    public void setHostManagementManager(final HostManagementManager hostManagementManager) {
        this.hostManagementManager = hostManagementManager;
    }

}
