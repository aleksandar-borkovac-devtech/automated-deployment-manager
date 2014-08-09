/*
 * @(#)LoginSteps.java 25 jan. 2013
 *
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.itest.gui.step;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import nl.tranquilizedquality.adm.itest.business.domain.AdmUserDto;
import nl.tranquilizedquality.adm.itest.business.manager.DashboardManager;
import nl.tranquilizedquality.adm.itest.business.manager.LoginManager;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Required;

/**
 * JBehave steps used to login a user.
 *
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 25 jan. 2013
 */
public class LoginSteps extends AbstractBasicSeleniumSteps {

    /** Manager used to login. */
    protected LoginManager loginManager;

    /** Manager used to manage the dashboard. */
    protected DashboardManager dashboardManager;

    /** User used for logging into ADM. */
    protected AdmUserDto user;

    /**
     * Initializes the managers.
     */
    public void init() {
        loginManager.setUpPages(getSelenium(), getConditionRunner());
        dashboardManager.setUpPages(getSelenium(), getConditionRunner());
    }

    @Given("an administrator")
    public void setupAdminUser() {
        user = new AdmUserDto();
        user.setUserName("adm-itest");
        user.setPassword("H1JINQY5FM");
    }

    @Given("a deployer")
    public void setupDeployerUser() {

    }

    @Given("a developer")
    public void setupDeveloperUser() {
        user = new AdmUserDto();
        user.setUserName("m-meijer");
        user.setPassword("726AFDWGQ7");
    }

    @Given("a developer with host management rights")
    public void setupDeveloperUserWithHostManagementRights() {
        user = new AdmUserDto();
        user.setUserName("m-meijer");
        user.setPassword("726AFDWGQ7");
    }

    @When("the user logs in")
    public void login() {
        try {
            loginManager.login(user);
        } catch (final Exception e) {
            final String message = e.getMessage();
            fail(message);
        }
    }

    @When("the user logs out")
    public void logout() {
        loginManager.logout();
    }

    @Then("the ADM dashboard should be visible")
    public void validateDashboardIsVisible() {
        final boolean visible = dashboardManager.isDashboardVisible();
        assertTrue("Dashboard is not visible!", visible);
    }

    @Then("the login page should be visible")
    public void validateLoginPageIsVisible() {
        final boolean loginPageVisible = loginManager.isLoginPageVisible();
        assertTrue("Login page is not visible!", loginPageVisible);
    }

    @Required
    public void setLoginManager(final LoginManager loginManager) {
        this.loginManager = loginManager;
    }

    @Required
    public void setDashboardManager(final DashboardManager dashboardManager) {
        this.dashboardManager = dashboardManager;
    }

}
