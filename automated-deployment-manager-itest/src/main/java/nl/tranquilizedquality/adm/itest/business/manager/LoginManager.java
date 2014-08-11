package nl.tranquilizedquality.adm.itest.business.manager;

import nl.tranquilizedquality.adm.itest.business.domain.AdmUserDto;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.condition.ConditionRunner;

/**
 * Manager used to manage login and logout in ADM.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 25 jan. 2013
 */
public interface LoginManager {

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
     * Log into ADM with the specified user.
     * 
     * @param user
     *            The user that will be used to login.
     * @throws Exception
     *             Throws an exception if login fails.
     */
    void login(AdmUserDto user) throws Exception;

    /**
     * Logsout the current user.
     */
    void logout();

    /**
     * Determines if the login page is visible or not.
     * 
     * @return Returns true if the login page is visible otherwise it will
     *         return false.
     */
    boolean isLoginPageVisible();

}
