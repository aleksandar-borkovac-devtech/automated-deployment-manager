/*
 * @(#)AdmDashboard.java 25 jan. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.itest.selenium.page;

import nl.tranquilizedquality.adm.itest.selenium.exception.LoginException;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.condition.ConditionRunner;

/**
 * Representation of the dashboard page that is displayed when logged into ADM.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 25 jan. 2013
 */
public class AdmDashboard extends AbstractAdmSeleniumPage {

    /** XPath definition for the menu item to logout with. */
    private static final String ADMIN_LOGOUT_MENU_ITEM = "//div[@id='navigation-tree-panel']/div[11]/div[1]";

    private static final String DESTINATION_MANAGEMENT_COLLAPSE_ITEM = "//div[@id='navigation-tree-panel']/div[4]/div[1]/img[2]";

    private static final String DESTINATION_MANAGEMENT_MENU_ITEM = "//div[@id='navigation-tree-panel']/div[4]";

    private static final String ADD_DESTINATION_MENU_ITEM = "//div[@id='navigation-tree-panel']/div[4]/div[2]/div[2]";

    private static final String MAVEN_MODULE_MANAGEMENT_COLLAPSE_ITEM = "//div[@id='navigation-tree-panel']/div[5]/div[1]/img[2]";

    private static final String ADD_MAVEN_MODULE_MENU_ITEM = "//div[@id='navigation-tree-panel']/div[5]/div[2]/div[2]";

    private static final String SEARCH_MAVEN_MODULE_MENU_ITEM = "//div[@id='navigation-tree-panel']/div[5]/div[2]/div[1]";

    private static final String RELEASE_MANAGEMENT_COLLAPSE_ITEM = "//div[@id='navigation-tree-panel']/div[7]/div[1]/img[2]";

    private static final String RELEASE_MANAGEMENT_MENU_ITEM = "//div[@id='navigation-tree-panel']/div[7]/div[2]/div[1]";

    private static final String HOST_MANAGEMENT_COLLAPSE_ITEM = "//div[@id='navigation-tree-panel']/div[2]/div[1]/img[2]";

    private static final String ADD_HOST_MANAGEMENT_MENU_ITEM = "//div[@id='navigation-tree-panel']/div[2]/div[2]/div[2]";

    /**
     * Constructor taking the selenium object so we can perform actions on the
     * page and the condition runner that drives the test case.
     * 
     * @param selenium
     *            The selenium object used to perform action on the login page.
     * @param conditionRunner
     *            The condition runner used to run the test case.
     */
    public AdmDashboard(final Selenium selenium, final ConditionRunner conditionRunner) {
        super(selenium, conditionRunner);
    }

    /**
     * Determines if the dashboard is displayed or not.
     * 
     * @return Returns true if the dashboard is visible otherwise it will return
     *         false.
     */
    public boolean isDashboardVisible() {
        return isTextPresent("ADM Dashboard");
    }

    /**
     * Logs out a customer.
     */
    public void logout() {
        try {
            /*
             * Press on the logout menu item.
             */
            waitForElementToBePresent("Logout item not found!!", ADMIN_LOGOUT_MENU_ITEM);
            mouseDown(ADMIN_LOGOUT_MENU_ITEM);

            Thread.sleep(2000);
        } catch (final Exception e) {
            throw new LoginException("Failed to login!", e);
        }
    }

    /**
     * Opens the tab where you can add a new destination.
     */
    public void openAddDestinationTab() {
        waitForElementToBePresent("Destination management collapse item not found!!", DESTINATION_MANAGEMENT_COLLAPSE_ITEM);
        click(DESTINATION_MANAGEMENT_COLLAPSE_ITEM);

        waitForElementToBePresent("Add Destination item not found!!", ADD_DESTINATION_MENU_ITEM);
        mouseDown(ADD_DESTINATION_MENU_ITEM);
    }

    /**
     * Opens the tab where you can add a new maven module.
     */
    public void openAddMavenModuleTab() {
        /*
         * Refresh so the GUI will be reset to it's initial state.
         */
        refresh();

        waitForElementToBePresent("Maven Module Management collapse item not found!!", MAVEN_MODULE_MANAGEMENT_COLLAPSE_ITEM);
        click(MAVEN_MODULE_MANAGEMENT_COLLAPSE_ITEM);

        waitForElementToBePresent("Add Maven Module item not found!!", ADD_MAVEN_MODULE_MENU_ITEM);
        mouseDown(ADD_MAVEN_MODULE_MENU_ITEM);
    }

    /**
     * Opens the tab where you can search for maven modules.
     */
    public void openDestinationManagementTab() {
        /*
         * Refresh so the GUI will be reset to it's initial state.
         */
        refresh();

        waitForElementToBePresent("Maven Module Management collapse item not found!!", MAVEN_MODULE_MANAGEMENT_COLLAPSE_ITEM);
        click(MAVEN_MODULE_MANAGEMENT_COLLAPSE_ITEM);

        waitForElementToBePresent("Search Maven Module item not found!!", SEARCH_MAVEN_MODULE_MENU_ITEM);
        mouseDown(SEARCH_MAVEN_MODULE_MENU_ITEM);
    }

    /**
     * Opens the tab where you can add a new release.
     */
    public void openReleaseManagementTab() {
        /*
         * Refresh so the GUI will be reset to it's initial state.
         */
        refresh();

        waitForElementToBePresent("Release Management collapse item not found!!", RELEASE_MANAGEMENT_COLLAPSE_ITEM);
        click(RELEASE_MANAGEMENT_COLLAPSE_ITEM);

        waitForElementToBePresent("Add Release item not found!!", RELEASE_MANAGEMENT_MENU_ITEM);
        mouseDown(RELEASE_MANAGEMENT_MENU_ITEM);
    }

    /**
     * Opens the tab where you can add a new host.
     */
    public void openAddNewDestinationHostTab() {
        /*
         * Refresh so the GUI will be reset to it's initial state.
         */
        refresh();

        waitForElementToBePresent("Host Management collapse item not found!!", HOST_MANAGEMENT_COLLAPSE_ITEM);
        click(HOST_MANAGEMENT_COLLAPSE_ITEM);

        waitForElementToBePresent("Add Host item not found!!", ADD_HOST_MANAGEMENT_MENU_ITEM);
        mouseDown(ADD_HOST_MANAGEMENT_MENU_ITEM);
    }

}
