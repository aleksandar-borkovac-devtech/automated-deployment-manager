/*
 * @(#)HostManagementManagerImpl.java 8 mrt. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.itest.business.manager.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nl.tranquilizedquality.adm.commons.business.command.DestinationHostSearchCommand;
import nl.tranquilizedquality.adm.commons.business.command.DestinationSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.business.domain.DestinationHost;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.core.persistence.dao.DestinationDao;
import nl.tranquilizedquality.adm.core.persistence.dao.DestinationHostDao;
import nl.tranquilizedquality.adm.itest.business.domain.DestinationHostDto;
import nl.tranquilizedquality.adm.itest.business.manager.HostManagementManager;
import nl.tranquilizedquality.adm.itest.selenium.page.PageType;
import nl.tranquilizedquality.adm.itest.selenium.page.destination.DestinationHostDetailsPage;
import nl.tranquilizedquality.adm.security.persistence.db.dao.UserGroupDao;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbehave.web.selenium.SeleniumPage;
import org.springframework.beans.factory.annotation.Required;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.condition.ConditionRunner;

/**
 * Manager that manages destination hosts using Selenium pages.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 8 mrt. 2013
 */
public class HostManagementManagerImpl implements HostManagementManager {

    /** Logger for this class. */
    private static final Log LOGGER = LogFactory.getLog(HostManagementManagerImpl.class);

    /** ADM pages. */
    private Map<PageType, SeleniumPage> admPages;

    /** DAO used to manage hosts. */
    private DestinationHostDao<DestinationHost> destinationHostDao;

    /** DAO used to clean up destination hosts. */
    private DestinationDao<Destination> destinationDao;

    /** DAO used to retrieve user groups. */
    private UserGroupDao<UserGroup> userGroupDao;

    @Override
    public void cleanUp() {
        /*
         * FIXME: Change other ITESTS to create a host within the story so they are not dependent on
         * the initial data.
         */
        final List<UserGroup> userGroups = userGroupDao.findAll();
        final List<DestinationHost> hosts = destinationHostDao.findAll();
        for (final DestinationHost destinationHost : hosts) {
            final String hostName = destinationHost.getHostName();

            final DestinationSearchCommand sc = new DestinationSearchCommand();
            sc.setHostName(hostName);
            sc.setUserGroups(userGroups);

            final int numberOfDestinations = destinationDao.findNumberOfDestinations(sc);
            if (numberOfDestinations <= 0) {
                destinationHostDao.delete(destinationHost);
                destinationHostDao.flush();
            }
        }
    }

    @Override
    public void setUpPages(final Selenium selenium, final ConditionRunner conditionRunner) {
        admPages = new HashMap<PageType, SeleniumPage>();

        admPages.put(PageType.DESTINATION_HOST_DETAILS_TAB, new DestinationHostDetailsPage(selenium, conditionRunner));

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Destination Host Management pages are initialized...");
        }
    }

    @Override
    public void addHost(final DestinationHostDto host) {
        final DestinationHostDetailsPage destinationHostDetailsPage =
                (DestinationHostDetailsPage) admPages.get(PageType.DESTINATION_HOST_DETAILS_TAB);

        destinationHostDetailsPage.addDestinationHost(host);
    }

    @Override
    public boolean isPrivateKeyStoredForHost(final DestinationHostDto host) {
        final List<UserGroup> userGroups = userGroupDao.findAll();
        final String hostName = host.getHostName();
        final String expectedPrivateKey = host.getPrivateKey();
        final DestinationHostSearchCommand sc = new DestinationHostSearchCommand();
        sc.setUserGroups(userGroups);
        sc.setHostName(hostName);
        sc.setMaxResults(1);

        final List<DestinationHost> hosts = destinationHostDao.findBySearchCommand(sc);

        if (!hosts.isEmpty()) {
            final Iterator<DestinationHost> iterator = hosts.iterator();
            final DestinationHost destinationHost = iterator.next();
            final String privateKey = destinationHost.getPrivateKey();
            if (StringUtils.equals(expectedPrivateKey, privateKey)) {
                return true;
            }
        } else {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("No host found with name " + hostName);
            }
        }

        return false;
    }

    @Required
    public void setDestinationHostDao(final DestinationHostDao<DestinationHost> destinationHostDao) {
        this.destinationHostDao = destinationHostDao;
    }

    @Required
    public void setUserGroupDao(final UserGroupDao<UserGroup> userGroupDao) {
        this.userGroupDao = userGroupDao;
    }

    @Required
    public void setDestinationDao(final DestinationDao<Destination> destinationDao) {
        this.destinationDao = destinationDao;
    }

}
