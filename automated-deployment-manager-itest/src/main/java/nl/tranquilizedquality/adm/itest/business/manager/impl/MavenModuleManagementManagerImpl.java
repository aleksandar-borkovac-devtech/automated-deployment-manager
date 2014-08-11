/*
 * @(#)MavenModuleManagementManagerImpl.java 12 feb. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.itest.business.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.tranquilizedquality.adm.commons.business.command.MavenModuleSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.business.domain.MavenModule;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.core.persistence.dao.MavenModuleDao;
import nl.tranquilizedquality.adm.itest.business.domain.MavenModuleDto;
import nl.tranquilizedquality.adm.itest.business.manager.MavenModuleManagementManager;
import nl.tranquilizedquality.adm.itest.selenium.page.AdmDashboard;
import nl.tranquilizedquality.adm.itest.selenium.page.PageType;
import nl.tranquilizedquality.adm.itest.selenium.page.module.MavenModuleDetailsPage;
import nl.tranquilizedquality.adm.itest.selenium.page.module.MavenModuleMangementPage;
import nl.tranquilizedquality.adm.security.persistence.db.dao.UserGroupDao;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbehave.web.selenium.SeleniumPage;
import org.springframework.beans.factory.annotation.Required;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.condition.ConditionRunner;

/**
 * Manager that manages {@link MavenModuleDto}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 12 feb. 2013
 */
public class MavenModuleManagementManagerImpl implements MavenModuleManagementManager {

    /** Logger for this class. */
    private static final Log LOGGER = LogFactory.getLog(MavenModuleManagementManagerImpl.class);

    /** ADM pages. */
    private Map<PageType, SeleniumPage> admPages;

    /** DAO used to manage maven modules. */
    private MavenModuleDao<MavenModule> mavenModuleDao;

    /** DAO used to retrieve user groups. */
    private UserGroupDao<UserGroup> userGroupDao;

    @Override
    public void cleanUp() {
        final List<MavenModule> modules = mavenModuleDao.findAll();
        for (final MavenModule mavenModule : modules) {
            final List<MavenModule> deploymentDependencies = mavenModule.getDeploymentDependencies();
            deploymentDependencies.clear();
            mavenModuleDao.save(mavenModule);
            mavenModuleDao.flush();
            mavenModuleDao.delete(mavenModule);
        }
    }

    @Override
    public void setUpPages(final Selenium selenium, final ConditionRunner conditionRunner) {
        admPages = new HashMap<PageType, SeleniumPage>();

        admPages.put(PageType.MAVEN_MODULE_DETAILS_TAB, new MavenModuleDetailsPage(selenium, conditionRunner));
        admPages.put(PageType.DASH_BOARD, new AdmDashboard(selenium, conditionRunner));
        admPages.put(PageType.MAVEN_MODULE_MANAGEMENT_TAB, new MavenModuleMangementPage(selenium, conditionRunner));

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ADM pages are initialized...");
        }
    }

    @Override
    public void addMavenModule(final MavenModuleDto mavenModule) {
        final MavenModuleDetailsPage mavenModuleDetailsPage = (MavenModuleDetailsPage) admPages
                .get(PageType.MAVEN_MODULE_DETAILS_TAB);
        mavenModuleDetailsPage.addMavenModule(mavenModule);
    }

    @Override
    public Integer countNumberOfModules() {
        final List<UserGroup> userGroups = userGroupDao.findAll();
        final MavenModuleSearchCommand sc = new MavenModuleSearchCommand();
        sc.setUserGroups(userGroups);
        return mavenModuleDao.findNumberOfMavenArtifacts(sc);
    }

    @Override
    public void addDestination(final Destination destination) {

    }

    @Override
    public void addDependency(final MavenModuleDto module, final MavenModuleDto dependency) {
        final AdmDashboard dashboard = (AdmDashboard) admPages.get(PageType.DASH_BOARD);
        dashboard.openDestinationManagementTab();

        final MavenModuleMangementPage mavenModuleManagementPage =
                (MavenModuleMangementPage) admPages.get(PageType.MAVEN_MODULE_MANAGEMENT_TAB);
        mavenModuleManagementPage.openDetailsPanelForModule(module);

        final MavenModuleDetailsPage mavenModuleDetailsPage = (MavenModuleDetailsPage) admPages
                .get(PageType.MAVEN_MODULE_DETAILS_TAB);
        mavenModuleDetailsPage.addDependency(dependency);
    }

    @Override
    public Integer countNumberOfDependenciesForModule(final MavenModuleDto module) {

        final String name = module.getName();
        final List<UserGroup> userGroups = userGroupDao.findAll();
        final MavenModuleSearchCommand sc = new MavenModuleSearchCommand();
        sc.setUserGroups(userGroups);
        sc.setName(name);

        int count = 0;
        final List<MavenModule> modules = mavenModuleDao.findBySearchCommand(sc);
        for (final MavenModule mavenModule : modules) {
            final String mavenModuleName = mavenModule.getName();
            if (StringUtils.equals(name, mavenModuleName)) {
                final List<MavenModule> deploymentDependencies = mavenModule.getDeploymentDependencies();
                count = deploymentDependencies.size();
                break;
            }
        }

        return count;
    }

    @Required
    public void setMavenModuleDao(final MavenModuleDao<MavenModule> mavenModuleDao) {
        this.mavenModuleDao = mavenModuleDao;
    }

    @Required
    public void setUserGroupDao(final UserGroupDao<UserGroup> userGroupDao) {
        this.userGroupDao = userGroupDao;
    }

}
