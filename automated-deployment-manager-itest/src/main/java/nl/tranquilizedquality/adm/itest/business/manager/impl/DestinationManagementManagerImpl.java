/*
 * @(#)DestinationManagementManagerImpl.java 1 feb. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.itest.business.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.tranquilizedquality.adm.commons.business.command.DestinationSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameter;
import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.core.persistence.dao.DeployerParameterDao;
import nl.tranquilizedquality.adm.core.persistence.dao.DestinationDao;
import nl.tranquilizedquality.adm.itest.business.manager.DestinationManagementManager;
import nl.tranquilizedquality.adm.itest.selenium.page.PageType;
import nl.tranquilizedquality.adm.itest.selenium.page.destination.DestinationDetailsPage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbehave.web.selenium.SeleniumPage;
import org.springframework.beans.factory.annotation.Required;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.condition.ConditionRunner;

/**
 * Manager used to manage destinations within ADM.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 1 feb. 2013
 */
public class DestinationManagementManagerImpl implements DestinationManagementManager {

    /** Logger for this class. */
    private static final Log LOGGER = LogFactory.getLog(DestinationManagementManagerImpl.class);

    /** ADM pages. */
    private Map<PageType, SeleniumPage> admPages;

    /** DAO used to validate destinations. */
    private DestinationDao<Destination> destinationDao;

    /** DAO used to validate parameters. */
    private DeployerParameterDao<DeployerParameter> deployerParameterDao;

    /**
     * Cleans up all the destination related data.
     */
    @Override
    public void cleanUp() {
        deployerParameterDao.deleteAll();
        destinationDao.deleteAll();
    }

    @Override
    public void setUpPages(final Selenium selenium, final ConditionRunner conditionRunner) {
        admPages = new HashMap<PageType, SeleniumPage>();

        admPages.put(PageType.DESTINATION_DETAILS_TAB, new DestinationDetailsPage(selenium, conditionRunner));

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Destination Management pages are initialized...");
        }
    }

    @Override
    public void addNewShellScriptBasedDestination(final String destinationName) {
        final DestinationDetailsPage destinationDetailsPage = (DestinationDetailsPage) admPages
                .get(PageType.DESTINATION_DETAILS_TAB);

        destinationDetailsPage.addDefaultShellScriptDesitnation(destinationName);
    }

    @Override
    public void addDeployerParameter(final String parameterValue) {
        final DestinationDetailsPage destinationDetailsPage = (DestinationDetailsPage) admPages
                .get(PageType.DESTINATION_DETAILS_TAB);

        destinationDetailsPage.addParameter(parameterValue);
    }

    @Override
    public boolean areExpectedNumberOfParametersCreated(final int expectedNumberOfParameters, final String destinationName) {
        final DestinationSearchCommand sc = new DestinationSearchCommand();
        sc.setName(destinationName);

        final List<Destination> destinations = destinationDao.findBySearchCommand(sc);
        if (!destinations.isEmpty()) {
            final Destination destination = destinations.get(0);
            final List<DeployerParameter> deployerParameters = destination.getDeployerParameters();
            final int numberOfParameters = deployerParameters.size();
            if (expectedNumberOfParameters == numberOfParameters) {
                return true;
            }
        }
        return false;
    }

    @Required
    public void setDestinationDao(final DestinationDao<Destination> destinationDao) {
        this.destinationDao = destinationDao;
    }

    @Required
    public void setDeployerParameterDao(final DeployerParameterDao<DeployerParameter> deployerParameterDao) {
        this.deployerParameterDao = deployerParameterDao;
    }

}
