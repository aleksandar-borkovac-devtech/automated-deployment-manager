/*
 * @(#)LoginPage.java 25 jan. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.itest.selenium.page;

import static junit.framework.Assert.assertTrue;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.condition.ConditionRunner;

/**
 * Representation of the login page of ADM.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 25 jan. 2013
 */
public class LoginPage extends AbstractAdmSeleniumPage {

    /** Locator for the login button. */
    private static final String LOGIN_BTN = "login-btn";

    /**
     * Constructor taking the selenium object so we can perform actions on the page and the
     * condition runner that drives the test case.
     * 
     * @param selenium
     *        The selenium object used to perform action on the login page.
     * @param conditionRunner
     *        The condition runner used to run the test case.
     */
    public LoginPage(final Selenium selenium, final ConditionRunner conditionRunner) {
        super(selenium, conditionRunner);
    }

    /**
     * Logs into ADM with the specified username and password.
     * 
     * @param userName
     *        The username to use for login.
     * @param password
     *        The password that will be used for login.
     * @throws Exception
     *         Is thrown if something goes wrong during login.
     */
    public void login(final String userName, final String password) throws Exception {
        selenium.open("/adm-gui/login.html");
        selenium.windowMaximize();

        /*
         * Press the login button.
         */
        Thread.sleep(3000);
        waitForElementToBePresent("Login button not found!!", LOGIN_BTN);
        selenium.type("j_username", userName);
        selenium.type("j_password", password);

        final boolean elementPresent = selenium.isElementPresent(LOGIN_BTN);
        assertTrue(elementPresent);

        Thread.sleep(2000);

        selenium.focus(LOGIN_BTN);
        Thread.sleep(1000);
        selenium.click(LOGIN_BTN);

        Thread.sleep(2000);
    }

    /**
     * Validates if the login page is visible by checking if the login button is present.
     * 
     * @return Returns true if the page is visible otherwise it returns false.
     */
    public boolean isVisible() {
        final boolean elementPresent = selenium.isElementPresent(LOGIN_BTN);
        return elementPresent;
    }

}
