/*
 * @(#)LoginManagerImpl.java 25 jan. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.itest.business.manager.impl;

import java.util.HashMap;
import java.util.Map;

import nl.tranquilizedquality.adm.itest.business.domain.AdmUserDto;
import nl.tranquilizedquality.adm.itest.business.manager.LoginManager;
import nl.tranquilizedquality.adm.itest.selenium.page.AdmDashboard;
import nl.tranquilizedquality.adm.itest.selenium.page.LoginPage;
import nl.tranquilizedquality.adm.itest.selenium.page.PageType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbehave.web.selenium.SeleniumPage;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.condition.ConditionRunner;

/**
 * Manager used to login and logout to ADM.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 25 jan. 2013
 */
public class LoginManagerImpl implements LoginManager {

    /** Logger for this class. */
    private static final Log LOGGER = LogFactory.getLog(LoginManagerImpl.class);

    /** ADM pages. */
    private Map<PageType, SeleniumPage> admPages;

    @Override
    public void setUpPages(final Selenium selenium, final ConditionRunner conditionRunner) {
        admPages = new HashMap<PageType, SeleniumPage>();

        admPages.put(PageType.LOGIN_PAGE, new LoginPage(selenium, conditionRunner));
        admPages.put(PageType.DASH_BOARD, new AdmDashboard(selenium, conditionRunner));

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ADM pages are initialized...");
        }
    }

    @Override
    public void login(final AdmUserDto user) throws Exception {
        final LoginPage loginPage = (LoginPage) admPages.get(PageType.LOGIN_PAGE);

        final String userName = user.getUserName();
        final String password = user.getPassword();
        loginPage.login(userName, password);
    }

    @Override
    public void logout() {
        final AdmDashboard dashboard = (AdmDashboard) admPages.get(PageType.DASH_BOARD);
        dashboard.logout();
    }

    @Override
    public boolean isLoginPageVisible() {
        final LoginPage loginPage = (LoginPage) admPages.get(PageType.LOGIN_PAGE);
        return loginPage.isVisible();
    }

}
