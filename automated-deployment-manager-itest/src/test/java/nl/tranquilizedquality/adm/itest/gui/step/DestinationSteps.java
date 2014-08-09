/*
 * @(#)DestinationSteps.java 1 feb. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.itest.gui.step;

import nl.tranquilizedquality.adm.itest.business.manager.DestinationManagementManager;

import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Required;

/**
 * Steps used to perform actions regarding destination management.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 1 feb. 2013
 */
public class DestinationSteps extends LoginSteps {

    /** Manager used to perform actions regaring destinations. */
    private DestinationManagementManager destinationManagementManager;

    /** The name of the destination that is managed in these steps. */
    private String destinationName;

    /**
     * Initializes the managers.
     */
    @Override
    public void init() {
        super.init();
        destinationManagementManager.setUpPages(getSelenium(), getConditionRunner());
        destinationManagementManager.cleanUp();
    }

    @When("the user adds a destination called $destinationName")
    public void addShellScriptBasedDestination(@Named("destinationName") final String destinationName) {
        dashboardManager.openAddNewDestinationTab();

        this.destinationName = destinationName;
        destinationManagementManager.addNewShellScriptBasedDestination(destinationName);
    }

    @When("adds a parameter with value $parameterValue")
    public void addParameter(@Named("parameterValue") final String parameterValue) {
        destinationManagementManager.addDeployerParameter(parameterValue);
    }

    @Then("there should be $numberOfParameters parameters configured for the destination")
    public void validateNumberOfParameters(@Named("numberOfParameters") final int numberOfParameters) {
        destinationManagementManager.areExpectedNumberOfParametersCreated(numberOfParameters, this.destinationName);
    }

    @Required
    public void setDestinationManagementManager(final DestinationManagementManager destinationManagementManager) {
        this.destinationManagementManager = destinationManagementManager;
    }

}
