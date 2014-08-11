package nl.tranquilizedquality.adm.itest.business.manager;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.condition.ConditionRunner;

/**
 * Manager used to perform actions regarding destination management.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 1 feb. 2013
 */
public interface DestinationManagementManager {

    /**
     * Cleans up all destination related data.
     */
    void cleanUp();

    /**
     * Constructor taking the selenium object so we can perform actions on the
     * page and the condition runner that drives the test case.
     * 
     * @param selenium
     *            The selenium object used to perform action on the login page.
     * @param conditionRunner
     *            The condition runner used to run the test case.
     */
    void setUpPages(Selenium selenium, ConditionRunner conditionRunner);

    /**
     * Creates a destination which is shell script based.
     * 
     * @param destinationName
     *            The name of the destination.
     */
    void addNewShellScriptBasedDestination(String destinationName);

    /**
     * Adds a deployer parameter.
     * 
     * @param parameterValue
     *            The value that will be used for the parameter.
     */
    void addDeployerParameter(String parameterValue);

    /**
     * Determines if the expected number of parameters are created for the
     * desintation with the specified name.
     * 
     * @param expectedNumberOfParameters
     *            The number of parameters that are expected.
     * @param destinationName
     *            The name of the destination.
     * @return Returns true if the expected number of parameters are created
     *         otherwise it returns false.
     */
    boolean areExpectedNumberOfParametersCreated(int expectedNumberOfParameters, String destinationName);

}
