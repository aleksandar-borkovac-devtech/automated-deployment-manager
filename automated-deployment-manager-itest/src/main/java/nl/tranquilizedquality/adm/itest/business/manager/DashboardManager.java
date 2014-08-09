package nl.tranquilizedquality.adm.itest.business.manager;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.condition.ConditionRunner;

/**
 * Manager that manages the ADM dashboard.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 25 jan. 2013
 */
public interface DashboardManager {

    /**
     * Constructor taking the selenium object so we can perform actions on the page and the
     * condition runner that drives the test case.
     * 
     * @param selenium
     *        The selenium object used to perform action on the login page.
     * @param conditionRunner
     *        The condition runner used to run the test case.
     */
    void setUpPages(Selenium selenium, ConditionRunner conditionRunner);

    /**
     * Determines if the dashboard is visible or not.
     * 
     * @return Returns true if it is displayed otherwise it will return false.
     */
    boolean isDashboardVisible();

    /**
     * Opens the tab where you can add a new destination.
     */
    void openAddNewDestinationTab();

    /**
     * Opens the tab where you can add a new maven module.
     */
    void openAddNewMavenModuleTab();

    /**
     * Opens the tab where you can add a new release.
     */
    void openReleaseManagementTab();

    /**
     * Opens the tab where you can add a new host.
     */
    void openAddNewDestinationHostTab();

}
