/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 4 sep. 2011 File: DestinationManagerImpl.java
 * Package: nl.tranquilizedquality.adm.core.business.manager.impl
 * 
 * Copyright (c) 2011 Tranquilized Quality www.Tranquilized Quality.nl All rights
 * reserved.
 * 
 * This software is the confidential and proprietary information of Tranquilized Quality
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Tranquilized Quality.
 * </pre>
 */
package nl.tranquilizedquality.adm.core.business.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import nl.tranquilizedquality.adm.commons.business.command.DestinationHostSearchCommand;
import nl.tranquilizedquality.adm.commons.business.command.DestinationSearchCommand;
import nl.tranquilizedquality.adm.commons.business.command.EnvironmentSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.Deployer;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameter;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameterTemplate;
import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.business.domain.DestinationHost;
import nl.tranquilizedquality.adm.commons.business.domain.Environment;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.commons.business.domain.UserRole;
import nl.tranquilizedquality.adm.core.business.manager.DestinationManager;
import nl.tranquilizedquality.adm.core.business.manager.exception.InvalidDestinationException;
import nl.tranquilizedquality.adm.core.business.manager.exception.InvalidEnvironmentException;
import nl.tranquilizedquality.adm.core.persistence.dao.DeployerParameterDao;
import nl.tranquilizedquality.adm.core.persistence.dao.DestinationDao;
import nl.tranquilizedquality.adm.core.persistence.dao.DestinationHostDao;
import nl.tranquilizedquality.adm.core.persistence.dao.EnvironmentDao;
import nl.tranquilizedquality.adm.security.business.manager.impl.UserGroupFilteringManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

/**
 * Manager that manages destination configuration.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 4 sep. 2011
 */
public class DestinationManagerImpl extends UserGroupFilteringManager implements DestinationManager {

    /** Logger for this class. */
    private static final Log LOGGER = LogFactory.getLog(DestinationManagerImpl.class);

    /** DAO that manages destinations. */
    private DestinationDao<Destination> destinationDao;

    /** DAO that manages destination hosts. */
    private DestinationHostDao<DestinationHost> destinationHostDao;

    /** DAO that manages environments. */
    private EnvironmentDao<Environment> environmentDao;

    /** DAO that manages destination locations. */
    private DeployerParameterDao<DeployerParameter> deployerParameterDao;

    /** Validator that validates destinations. */
    private Validator destinationValidator;

    /** Validator that validates environments. */
    private Validator environmentValidator;

    /** Validator that validates hosts. */
    private Validator destinationHostValidator;

    @Override
    public Destination findDestinationById(final Long id) {
        final Destination destination = destinationDao.findById(id);

        /*
         * Initialize locations.
         */
        final List<DeployerParameter> deployerParameters = destination.getDeployerParameters();
        for (final DeployerParameter destinationLocation : deployerParameters) {
            destinationLocation.getId();
        }

        final Deployer deployer = destination.getDeployer();
        final List<DeployerParameterTemplate> parameters = deployer.getParameters();
        for (final DeployerParameterTemplate deployerParameterTemplate : parameters) {
            deployerParameterTemplate.getId();
        }

        return destination;
    }

    @Override
    public DestinationHost findDestinationHostById(final Long id) {
        final DestinationHost destinationHost = destinationHostDao.findById(id);

        return destinationHost;
    }

    @Override
    public Environment findEnvironmentById(final Long id) {
        final Environment environment = environmentDao.findById(id);
        final List<User> users = environment.getUsers();
        for (final User user : users) {
            final Set<UserRole> userRoles = user.getUserRoles();
            for (final UserRole userRole : userRoles) {
                userRole.getId();
            }
        }
        return environment;
    }

    @Override
    public Destination storeDestination(final Destination destination, final Errors errors) {
        if (destination == null) {
            final String msg = "No destination specified!";
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }

            throw new InvalidDestinationException(msg);
        }

        /*
         * Validate the destination.
         */
        destinationValidator.validate(destination, errors);

        /*
         * Check for errors.
         */
        if (errors.hasErrors()) {
            final DestinationHost destinationHost = destination.getDestinationHost();
            final String msg = "Invalid destination.. " + destinationHost.getHostName();

            if (LOGGER.isDebugEnabled()) {
                final List<ObjectError> allErrors = errors.getAllErrors();
                for (final ObjectError objectError : allErrors) {
                    LOGGER.debug(objectError.getDefaultMessage());
                }
            }

            throw new InvalidDestinationException(msg);
        }

        /*
         * Check if we are doing an insert or an update.
         */
        if (destination.isPersistent()) {
            /*
             * Create supported domain object
             */
            final Long destinationId = destination.getId();
            final Destination original = destinationDao.findById(destinationId);
            original.copy(destination);

            final Destination savedDestination = destinationDao.save(original);
            final UserGroup userGroup = destination.getUserGroup();
            initializeUserGroup(userGroup);
            return savedDestination;
        } else {
            final Destination savedDestination = destinationDao.save(destination);
            final UserGroup userGroup = savedDestination.getUserGroup();
            initializeUserGroup(userGroup);
            return savedDestination;
        }
    }

    private void initializeUserGroup(final UserGroup userGroup) {
        if (userGroup != null) {
            final List<User> users = userGroup.getUsers();
            for (final User user : users) {
                user.getId();
            }
        }
    }

    @Override
    public DestinationHost storeDestinationHost(final DestinationHost destinationHost, final Errors errors) {
        if (destinationHost == null) {
            final String msg = "No destination host specified!";
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }

            throw new InvalidDestinationException(msg);
        }

        /*
         * Validate the destination host.
         */
        destinationHostValidator.validate(destinationHost, errors);

        /*
         * Check for errors.
         */
        if (errors.hasErrors()) {
            final String msg = "Invalid destination host.. " + destinationHost.getHostName();

            if (LOGGER.isDebugEnabled()) {
                final List<ObjectError> allErrors = errors.getAllErrors();
                for (final ObjectError objectError : allErrors) {
                    LOGGER.debug(objectError.getDefaultMessage());
                }
            }

            throw new InvalidDestinationException(msg);
        }

        /*
         * Check if we are doing an insert or an update.
         */
        if (destinationHost.isPersistent()) {
            /*
             * Create supported domain object
             */
            final DestinationHost original = destinationHostDao.findById(destinationHost.getId());
            original.copy(destinationHost);

            final DestinationHost savedDestinationHost = destinationHostDao.save(original);
            final UserGroup userGroup = savedDestinationHost.getUserGroup();
            initializeUserGroup(userGroup);
            return savedDestinationHost;
        } else {
            final DestinationHost savedDestinationHost = destinationHostDao.save(destinationHost);
            final UserGroup userGroup = savedDestinationHost.getUserGroup();
            initializeUserGroup(userGroup);
            return savedDestinationHost;
        }
    }

    @Override
    public DeployerParameter findDestinationLocationById(final Long id) {
        return deployerParameterDao.findById(id);
    }

    @Override
    public void removeDestinationLocation(final DeployerParameter location) {
        if (location == null || !location.isPersistent()) {
            final String msg = "No location specified!";
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }

            throw new InvalidDestinationException(msg);
        }

        /*
         * Remove the location.
         */
        deployerParameterDao.delete(location);
    }

    @Override
    public Environment storeEnvironment(final Environment environment, final Errors errors) {
        if (environment == null) {
            final String msg = "No environment specified!";
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }

            throw new InvalidEnvironmentException(msg);
        }

        /*
         * Validate the environment.
         */
        environmentValidator.validate(environment, errors);

        /*
         * Check for errors.
         */
        if (errors.hasErrors()) {
            final String msg = "Invalid environment.. " + environment.getName();

            if (LOGGER.isDebugEnabled()) {
                final List<ObjectError> allErrors = errors.getAllErrors();
                for (final ObjectError objectError : allErrors) {
                    LOGGER.debug(objectError.getDefaultMessage());
                }
            }

            throw new InvalidEnvironmentException(msg);
        }

        /*
         * Check if we are doing an insert or an update.
         */
        if (environment.isPersistent()) {
            /*
             * Create supported domain object
             */
            final Long environmentId = environment.getId();
            final Environment original = environmentDao.findById(environmentId);
            original.copy(environment);

            final Environment savedEnvironment = environmentDao.save(original);
            return savedEnvironment;
        } else {
            final Environment newEnvironment = environmentDao.newDomainObject();
            newEnvironment.copy(environment);

            final Environment savedEnvironment = environmentDao.save(newEnvironment);
            return savedEnvironment;
        }
    }

    @Override
    public List<Destination> findDestinations(final DestinationSearchCommand sc) {
        addUserGroupsFromLoggedInUser(sc);

        final List<Destination> destinations = destinationDao.findBySearchCommand(sc);

        for (final Destination destination : destinations) {
            final List<DeployerParameter> deployerParameters = destination.getDeployerParameters();
            for (final DeployerParameter parameter : deployerParameters) {
                parameter.getId();
            }

            final Deployer deployer = destination.getDeployer();
            final List<DeployerParameterTemplate> parameters = deployer.getParameters();
            for (final DeployerParameterTemplate deployerParameterTemplate : parameters) {
                deployerParameterTemplate.getId();
            }
        }

        return destinations;
    }

    @Override
    public List<DestinationHost> findDestinationHosts(final DestinationHostSearchCommand sc) {
        addUserGroupsFromLoggedInUser(sc);

        final List<DestinationHost> hosts = destinationHostDao.findBySearchCommand(sc);

        return hosts;
    }

    @Override
    public int findNumberOfDestinationHosts(final DestinationHostSearchCommand sc) {
        addUserGroupsFromLoggedInUser(sc);
        return destinationHostDao.findNumberOfDestinationHosts(sc);
    }

    @Override
    public List<DestinationHost> findAvailableDestinationHosts() {
        final DestinationHostSearchCommand sc = new DestinationHostSearchCommand();
        addUserGroupsFromLoggedInUser(sc);
        return destinationHostDao.findBySearchCommand(sc);
    }

    @Override
    public int findNumberOfDestinations(final DestinationSearchCommand sc) {
        addUserGroupsFromLoggedInUser(sc);
        return destinationDao.findNumberOfDestinations(sc);
    }

    @Override
    public List<Environment> findAvailableEnvironments() {
        return environmentDao.findAll();
    }

    @Override
    public List<Environment> findDeployEnvironments() {
        final User loggedInUser = securityContextManager.findLoggedInUser();

        final List<Environment> environments = environmentDao.findAll();
        final List<Environment> deployEnvironments = new ArrayList<Environment>();
        for (final Environment environment : environments) {
            final List<User> users = environment.getUsers();
            if (users.contains(loggedInUser)) {
                deployEnvironments.add(environment);
            }
        }

        return deployEnvironments;
    }

    @Override
    public List<Environment> findEnvironments(final EnvironmentSearchCommand sc) {
        return environmentDao.findEnvironmentsBySearchCommand(sc);
    }

    @Override
    public List<Destination> findAvailableDestinations() {
        final DestinationSearchCommand sc = new DestinationSearchCommand();
        addUserGroupsFromLoggedInUser(sc);

        final List<Destination> availableDestinations = destinationDao.findBySearchCommand(sc);

        for (final Destination destination : availableDestinations) {
            final List<DeployerParameter> deployerParameters = destination.getDeployerParameters();
            for (final DeployerParameter parameter : deployerParameters) {
                parameter.getId();
            }

            final Deployer deployer = destination.getDeployer();
            final List<DeployerParameterTemplate> parameters = deployer.getParameters();
            for (final DeployerParameterTemplate deployerParameterTemplate : parameters) {
                deployerParameterTemplate.getId();
            }
        }

        return availableDestinations;
    }

    /**
     * @param destinationDao
     *        the destinationDao to set
     */
    @Required
    public void setDestinationDao(final DestinationDao<Destination> destinationDao) {
        this.destinationDao = destinationDao;
    }

    /**
     * @param environmentDao
     *        the environmentDao to set
     */
    @Required
    public void setEnvironmentDao(final EnvironmentDao<Environment> environmentDao) {
        this.environmentDao = environmentDao;
    }

    /**
     * @param destinationValidator
     *        the destinationValidator to set
     */
    @Required
    public void setDestinationValidator(final Validator destinationValidator) {
        this.destinationValidator = destinationValidator;
    }

    /**
     * @param environmentValidator
     *        the environmentValidator to set
     */
    @Required
    public void setEnvironmentValidator(final Validator environmentValidator) {
        this.environmentValidator = environmentValidator;
    }

    /**
     * @param destinationLocationDao
     *        the destinationLocationDao to set
     */
    @Required
    public void setDeployerParameterDao(final DeployerParameterDao<DeployerParameter> deployerParameterDao) {
        this.deployerParameterDao = deployerParameterDao;
    }

    /**
     * @param destinationHostDao
     *        the destinationHostDao to set
     */
    @Required
    public void setDestinationHostDao(final DestinationHostDao<DestinationHost> destinationHostDao) {
        this.destinationHostDao = destinationHostDao;
    }

    /**
     * @param destinationHostValidator
     *        the destinationHostValidator to set
     */
    @Required
    public void setDestinationHostValidator(final Validator destinationHostValidator) {
        this.destinationHostValidator = destinationHostValidator;
    }

}
