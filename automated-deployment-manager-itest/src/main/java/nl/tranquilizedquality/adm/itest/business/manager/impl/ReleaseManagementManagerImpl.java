/*
 * @(#)ReleaseManagementManagerImpl.java 15 feb. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.itest.business.manager.impl;

import static junit.framework.Assert.assertEquals;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nl.tranquilizedquality.adm.commons.business.command.ReleaseSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.Release;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecution;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseStatus;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.core.persistence.dao.MavenArtifactDao;
import nl.tranquilizedquality.adm.core.persistence.dao.ReleaseDao;
import nl.tranquilizedquality.adm.core.persistence.dao.ReleaseExecutionDao;
import nl.tranquilizedquality.adm.itest.business.domain.MavenArtifactDto;
import nl.tranquilizedquality.adm.itest.business.domain.ReleaseDto;
import nl.tranquilizedquality.adm.itest.business.manager.ReleaseManagementManager;
import nl.tranquilizedquality.adm.itest.selenium.page.AdmDashboard;
import nl.tranquilizedquality.adm.itest.selenium.page.PageType;
import nl.tranquilizedquality.adm.itest.selenium.page.release.ReleaseDetailsPage;
import nl.tranquilizedquality.adm.itest.selenium.page.release.ReleaseManagementPage;
import nl.tranquilizedquality.adm.security.persistence.db.dao.UserGroupDao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbehave.web.selenium.SeleniumPage;
import org.springframework.beans.factory.annotation.Required;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.condition.ConditionRunner;

/**
 * Manager used to manage and perform releases.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 15 feb. 2013
 */
public class ReleaseManagementManagerImpl implements ReleaseManagementManager {

    /** Logger for this class. */
    private static final Log LOGGER = LogFactory.getLog(ReleaseManagementManagerImpl.class);

    /** ADM pages. */
    private Map<PageType, SeleniumPage> admPages;

    /** DAO that manages releases. */
    private ReleaseDao<Release> releaseDao;

    /** DAO used to manage artifacts. */
    private MavenArtifactDao<MavenArtifact> mavenArtifactDao;

    /** DAO used to manage user groups. */
    private UserGroupDao<UserGroup> userGroupDao;

    /** DAO used to manage release executions. */
    private ReleaseExecutionDao<ReleaseExecution> releaseExecutionDao;

    @Override
    public void cleanUp() {
        releaseExecutionDao.deleteAll();
        mavenArtifactDao.deleteAll();
        releaseDao.deleteAll();
    }

    @Override
    public void setUpPages(final Selenium selenium, final ConditionRunner conditionRunner) {
        admPages = new HashMap<PageType, SeleniumPage>();

        admPages.put(PageType.RELEASE_MANAGEMENT_TAB, new ReleaseManagementPage(selenium, conditionRunner));
        admPages.put(PageType.RELEASE_DETAILS_TAB, new ReleaseDetailsPage(selenium, conditionRunner));
        admPages.put(PageType.DASH_BOARD, new AdmDashboard(selenium, conditionRunner));

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Release Management pages are initialized...");
        }
    }

    @Override
    public void addRelease(final ReleaseDto release) {
        final AdmDashboard dashboard = (AdmDashboard) admPages.get(PageType.DASH_BOARD);
        dashboard.openReleaseManagementTab();

        final ReleaseManagementPage releaseMangementPage = (ReleaseManagementPage) admPages.get(PageType.RELEASE_MANAGEMENT_TAB);
        releaseMangementPage.openAddReleasePanel();

        final ReleaseDetailsPage releaseDetailsPage = (ReleaseDetailsPage) admPages.get(PageType.RELEASE_DETAILS_TAB);
        releaseDetailsPage.addRelease(release);
    }

    @Override
    public void addArtifactToRelease(final ReleaseDto release, final MavenArtifactDto artifact) {
        final AdmDashboard dashboard = (AdmDashboard) admPages.get(PageType.DASH_BOARD);
        dashboard.openReleaseManagementTab();

        final ReleaseManagementPage releaseMangementPage = (ReleaseManagementPage) admPages.get(PageType.RELEASE_MANAGEMENT_TAB);
        releaseMangementPage.openDetailsPanelForRelease(release);

        final ReleaseDetailsPage releaseDetailsPage = (ReleaseDetailsPage) admPages.get(PageType.RELEASE_DETAILS_TAB);
        releaseDetailsPage.addArtifactToRelease(release, artifact);
    }

    @Override
    public void deployReleaseToEnvironment(final ReleaseDto release, final String environmentName) {
        final AdmDashboard dashboard = (AdmDashboard) admPages.get(PageType.DASH_BOARD);
        dashboard.openReleaseManagementTab();

        final ReleaseManagementPage releaseMangementPage = (ReleaseManagementPage) admPages.get(PageType.RELEASE_MANAGEMENT_TAB);
        releaseMangementPage.openDetailsPanelForRelease(release);

        final ReleaseDetailsPage releaseDetailsPage = (ReleaseDetailsPage) admPages.get(PageType.RELEASE_DETAILS_TAB);
        releaseDetailsPage.deployReleaseToEnvironment(release, environmentName);
    }

    @Override
    public ReleaseStatus findReleaseStatusForRelease(final ReleaseDto release) {
        final List<UserGroup> userGroups = userGroupDao.findAll();
        final String releaseName = release.getName();
        final ReleaseSearchCommand sc = new ReleaseSearchCommand();
        sc.setReleaseName(releaseName);
        sc.setUserGroups(userGroups);

        final List<Release> releases = releaseDao.findBySearchCommand(sc);
        assertEquals("Invalid number of releases found!", 1, releases.size());

        final Iterator<Release> releaseIterator = releases.iterator();
        final Release foundRelease = releaseIterator.next();
        final ReleaseStatus releaseStatus = foundRelease.getStatus();

        return releaseStatus;
    }

    @Required
    public void setReleaseDao(final ReleaseDao<Release> releaseDao) {
        this.releaseDao = releaseDao;
    }

    @Required
    public void setMavenArtifactDao(final MavenArtifactDao<MavenArtifact> mavenArtifactDao) {
        this.mavenArtifactDao = mavenArtifactDao;
    }

    @Required
    public void setUserGroupDao(final UserGroupDao<UserGroup> userGroupDao) {
        this.userGroupDao = userGroupDao;
    }

    @Required
    public void setReleaseExecutionDao(final ReleaseExecutionDao<ReleaseExecution> releaseExecutionDao) {
        this.releaseExecutionDao = releaseExecutionDao;
    }

}
