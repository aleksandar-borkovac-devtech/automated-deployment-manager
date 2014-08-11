package nl.tranquilizedquality.adm.itest.business.manager;

import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.itest.business.domain.MavenModuleDto;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.condition.ConditionRunner;

/**
 * Manager that provides services to manage Maven modules.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 12 feb. 2013
 */
public interface MavenModuleManagementManager {

    /**
     * Cleans up all the maven module related data.
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
     * Adds a maven module to ADM using the passed in data as input.
     * 
     * @param mavenModule
     *            The input data used for creating a new maven module.
     */
    void addMavenModule(MavenModuleDto mavenModule);

    /**
     * Adds a destination to a maven module in ADM using the passed in data as
     * input.
     * 
     * @param destination
     *            The input data used for adding a destination to the maven
     *            module.
     */
    void addDestination(Destination destination);

    /**
     * Adds a dependency to a maven module in ADM using the passed in data as
     * input.
     * 
     * @param module
     *            The module where the dependency should be added for.
     * @param dependencies
     *            The input data used for adding a dependency to the maven
     *            module.
     */
    void addDependency(MavenModuleDto module, MavenModuleDto dependencies);

    /**
     * Counts the number of modules that are currently available in ADM.
     * 
     * @return Returns an {@link Integer} value of 0 or greater.
     */
    Integer countNumberOfModules();

    /**
     * Counts the number of dependencies that are created for the specified
     * module.
     * 
     * @param module
     *            The module that will be validated.
     * @return Returns an {@link Integer} value of 0 or greater.
     */
    Integer countNumberOfDependenciesForModule(final MavenModuleDto module);

}
