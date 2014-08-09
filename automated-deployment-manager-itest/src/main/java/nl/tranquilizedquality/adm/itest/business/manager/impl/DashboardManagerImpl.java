/*
 * @(#)DashboardManagerImpl.java 25 jan. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.itest.business.manager.impl;

import java.util.HashMap;
import java.util.Map;

import nl.tranquilizedquality.adm.itest.business.manager.DashboardManager;
import nl.tranquilizedquality.adm.itest.selenium.page.AdmDashboard;
import nl.tranquilizedquality.adm.itest.selenium.page.PageType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbehave.web.selenium.SeleniumPage;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.condition.ConditionRunner;

/**
 * Manager that manages the dashboard of ADM.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 25 jan. 2013
 */
public class DashboardManagerImpl implements DashboardManager {

    /** Logger for this class. */
    private static final Log LOGGER = LogFactory.getLog(LoginManagerImpl.class);

    /** ADM pages. */
    private Map<PageType, SeleniumPage> admPages;

    @Override
    public void setUpPages(final Selenium selenium, final ConditionRunner conditionRunner) {
        admPages = new HashMap<PageType, SeleniumPage>();

        admPages.put(PageType.DASH_BOARD, new AdmDashboard(selenium, conditionRunner));

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ADM dashboard pages are initialized...");
        }
    }

    @Override
    public boolean isDashboardVisible() {
        final AdmDashboard dashboard = (AdmDashboard) admPages.get(PageType.DASH_BOARD);
        return dashboard.isDashboardVisible();
    }

    @Override
    public void openAddNewDestinationTab() {
        final AdmDashboard dashboard = (AdmDashboard) admPages.get(PageType.DASH_BOARD);
        dashboard.openAddDestinationTab();
    }

    @Override
    public void openAddNewMavenModuleTab() {
        final AdmDashboard dashboard = (AdmDashboard) admPages.get(PageType.DASH_BOARD);
        dashboard.openAddMavenModuleTab();
    }

    @Override
    public void openReleaseManagementTab() {
        final AdmDashboard dashboard = (AdmDashboard) admPages.get(PageType.DASH_BOARD);
        dashboard.openReleaseManagementTab();
    }

    @Override
    public void openAddNewDestinationHostTab() {
        final AdmDashboard dashboard = (AdmDashboard) admPages.get(PageType.DASH_BOARD);
        dashboard.openAddNewDestinationHostTab();
    }

}
