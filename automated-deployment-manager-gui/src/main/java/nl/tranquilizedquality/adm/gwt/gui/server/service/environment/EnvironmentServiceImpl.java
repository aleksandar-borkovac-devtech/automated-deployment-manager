/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 11 sep. 2011 File: EnvironmentServiceImpl.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.server.service.environment
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
package nl.tranquilizedquality.adm.gwt.gui.server.service.environment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameter;
import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.business.domain.DestinationHost;
import nl.tranquilizedquality.adm.commons.business.domain.Environment;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.commons.business.domain.UserRole;
import nl.tranquilizedquality.adm.commons.gwt.ext.server.service.AbstractService;
import nl.tranquilizedquality.adm.core.business.manager.DestinationManager;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDeployerParameter;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDestination;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDestinationHost;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateEnvironment;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientDeployerParameter;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientDestination;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientDestinationHost;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientDestinationHostSearchCommand;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientDestinationSearchCommand;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientEnvironment;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientEnvironmentSearchCommand;
import nl.tranquilizedquality.adm.gwt.gui.client.service.environment.EnvironmentService;
import nl.tranquilizedquality.adm.gwt.gui.client.service.environment.EnvironmentServiceException;
import nl.tranquilizedquality.adm.security.business.manager.AuthorizationManager;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;

/**
 * Servic that provides environment related services.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 11 sep. 2011
 */
public class EnvironmentServiceImpl extends AbstractService implements EnvironmentService {

    /**
     * Factory that transforms client side beans into persistent beans and vice
     * versa.
     */
    private static final DestinationFactory DESTINATION_FACTORY = new DestinationFactory();

    /**
     * Factory that transforms client side beans into persistent beans and vice
     * versa.
     */
    private static final EnvironmentFacotry ENVIRONMENT_FACTORY = new EnvironmentFacotry();

    /**
     * Factory that transforms client side beans into persistent beans and vice
     * versa.
     */
    private static final DestinationHostFactory DESTINATION_HOST_FACTORY = new DestinationHostFactory();

    /**
     * Factory that transforms client side beans into persistent beans and vice
     * versa.
     */
    private static final DestinationLocationFactory DESTINATION_LOCATION_FACTORY = new DestinationLocationFactory();

    /** Manager that manages destinations. */
    private DestinationManager destinationManager;

    private AuthorizationManager authorizationManager;

    @Override
    public ClientEnvironment findEnvironmentById(final Long id) {
        final Environment environment = destinationManager.findEnvironmentById(id);

        final List<User> users = environment.getUsers();
        for (final User user : users) {
            user.setUserRoles(new HashSet<UserRole>());
        }

        final ClientEnvironment clientBean = ENVIRONMENT_FACTORY.createClientBean(environment);

        return clientBean;
    }

    @Override
    public List<ClientEnvironment> findEnvironments() {
        final List<Environment> environments = destinationManager.findAvailableEnvironments();

        for (final Environment environment : environments) {
            environment.setUsers(new ArrayList<User>());
        }

        final List<ClientEnvironment> clientBeans = ENVIRONMENT_FACTORY.createClientBeans(environments);

        return clientBeans;
    }

    @Override
    public PagingLoadResult<ClientEnvironment> findEnvironments(final PagingLoadConfig loadConfig,
            final ClientEnvironmentSearchCommand sc) {
        final List<Environment> environments = destinationManager.findEnvironments(sc);

        for (final Environment environment : environments) {
            environment.setUsers(new ArrayList<User>());
        }

        final List<ClientEnvironment> clientBeans = ENVIRONMENT_FACTORY.createClientBeans(environments);

        return new BasePagingLoadResult<ClientEnvironment>(clientBeans, loadConfig.getOffset(), loadConfig.getLimit());
    }

    @Override
    public ClientEnvironment saveEnvironment(final ClientEnvironment environment) throws EnvironmentServiceException {
        final HibernateEnvironment persistentBean = ENVIRONMENT_FACTORY.createPersistentBean(environment);

        final Errors errors = new BindException(persistentBean, persistentBean.getClass().getName());

        try {
            final Environment storedEnvironment = destinationManager.storeEnvironment(persistentBean, errors);

            final ClientEnvironment storedClientBean = ENVIRONMENT_FACTORY.createClientBean(storedEnvironment);

            return storedClientBean;
        } catch (final Exception e) {
            final List<String> errorList = createErrorList(errors);

            throw new EnvironmentServiceException("Failed to save environment!", e, errorList);
        }
    }

    @Override
    public ClientDestination findDestinationById(final Long id) {
        final Destination destination = destinationManager.findDestinationById(id);

        final UserGroup userGroup = destination.getUserGroup();
        userGroup.setUsers(new ArrayList<User>());

        final Environment environment = destination.getEnvironment();
        environment.setUsers(new ArrayList<User>());

        final ClientDestination clientBean = DESTINATION_FACTORY.createClientBean(destination);

        return clientBean;
    }

    @Override
    public ClientDestination saveDestination(final ClientDestination destination) throws EnvironmentServiceException {
        final HibernateDestination hibernateDestination = DESTINATION_FACTORY.createPersistentBean(destination);

        final Errors errors = new BindException(hibernateDestination, hibernateDestination.getClass().getName());

        try {
            final Destination storedDestination = destinationManager.storeDestination(hibernateDestination, errors);

            final UserGroup userGroup = storedDestination.getUserGroup();
            userGroup.setUsers(new ArrayList<User>());

            final Environment environment = storedDestination.getEnvironment();
            environment.setUsers(new ArrayList<User>());

            final ClientDestination storedClientBeans = DESTINATION_FACTORY.createClientBean(storedDestination);

            return storedClientBeans;
        } catch (final Exception e) {
            final List<String> errorList = createErrorList(errors);

            throw new EnvironmentServiceException("Failed to save destination!", e, errorList);
        }
    }

    /**
     * Retrieves a location with the specified identifier.
     * 
     * @param id
     *            The unique identifier to use.
     * @return Returns a {@link ClientDeployerParameter} with the specified
     *         identifier.
     */
    @Override
    public ClientDeployerParameter findLocationById(final Long id) {
        final DeployerParameter location = destinationManager.findDestinationLocationById(id);

        final ClientDeployerParameter clientBean = DESTINATION_LOCATION_FACTORY.createClientBean(location);

        return clientBean;
    }

    @Override
    public void deleteDestinationLocation(final ClientDeployerParameter location) {

        /*
         * Create persistent bean.
         */
        final HibernateDeployerParameter destinationLocation = DESTINATION_LOCATION_FACTORY.createPersistentBean(location);

        /*
         * Remove the destination location.
         */
        destinationManager.removeDestinationLocation(destinationLocation);
    }

    @Override
    public PagingLoadResult<ClientDestination> findDestinations(final PagingLoadConfig config,
            final ClientDestinationSearchCommand sc) {
        /*
         * Setup search command.
         */
        final SortDir sortDir = config.getSortDir();
        if (sortDir.equals(SortDir.ASC)) {
            sc.setAsc(true);
        } else {
            sc.setAsc(false);
        }

        final String sortField = config.getSortField();
        if (sortField != null) {
            sc.setOrderBy(sortField);
        }

        final int limit = config.getLimit();
        sc.setMaxResults(limit);

        final int offset = config.getOffset();
        sc.setStart(offset);

        /*
         * Search for the destinations.
         */
        final List<Destination> destinations = destinationManager.findDestinations(sc);

        for (final Destination destination : destinations) {
            final UserGroup userGroup = destination.getUserGroup();
            userGroup.setUsers(new ArrayList<User>());

            final Environment environment = destination.getEnvironment();
            environment.setUsers(new ArrayList<User>());
        }

        /*
         * Create client beans.
         */
        final List<ClientDestination> clientBeans = DESTINATION_FACTORY.createClientBeans(destinations);

        /*
         * Retrieve the total count.
         */
        final int count = destinationManager.findNumberOfDestinations(sc);

        /*
         * Return the results for a grid.
         */
        return new BasePagingLoadResult<ClientDestination>(clientBeans, config.getOffset(), count);
    }

    @Override
    public List<ClientDestination> findAvailableDestinations() {
        final List<Destination> destinations = destinationManager.findAvailableDestinations();

        for (final Destination destination : destinations) {
            final UserGroup userGroup = destination.getUserGroup();
            userGroup.setUsers(new ArrayList<User>());

            final Environment environment = destination.getEnvironment();
            environment.setUsers(new ArrayList<User>());
        }

        final List<ClientDestination> clientBeans = DESTINATION_FACTORY.createClientBeans(destinations);

        return clientBeans;
    }

    @Override
    public PagingLoadResult<ClientEnvironment> findAvailableEnvironments(final PagingLoadConfig config) {
        /*
         * Retrieve environments.
         */
        final List<Environment> environments = destinationManager.findDeployEnvironments();

        for (final Environment environment : environments) {
            environment.setUsers(new ArrayList<User>());
        }

        /*
         * Create client beans.
         */
        final List<ClientEnvironment> clientBeans = ENVIRONMENT_FACTORY.createClientBeans(environments);

        /*
         * Return the results for a grid.
         */
        return new BasePagingLoadResult<ClientEnvironment>(clientBeans, config.getOffset(), clientBeans.size());
    }

    @Override
    public ClientDestinationHost findDestinationHostById(final Long id) {

        /*
         * Find the host.
         */
        final DestinationHost destinationHost = destinationManager.findDestinationHostById(id);
        final boolean authorized = authorizationManager.isLoggedInUserAuthorized("ADD_PRIVATE_KEY");
        final UserGroup userGroup = destinationHost.getUserGroup();
        userGroup.setUsers(new ArrayList<User>());

        /*
         * Transform to client bean.
         */
        final ClientDestinationHost clientBean = DESTINATION_HOST_FACTORY.createClientBean(destinationHost);
        if (authorized) {
            final String privateKey = destinationHost.getPrivateKey();
            clientBean.setAuthorizedPrivateKey(privateKey);
        }

        return clientBean;
    }

    @Override
    public ClientDestinationHost saveDestinationHost(final ClientDestinationHost host) {

        final boolean authorized = authorizationManager.isLoggedInUserAuthorized("ADD_PRIVATE_KEY");
        if (host.isPersistent()) {
            if (!authorized) {
                final Long hostId = host.getId();
                final DestinationHost destinationHost = destinationManager.findDestinationHostById(hostId);
                host.setAuthorizedPrivateKey(destinationHost.getPrivateKey());
            }
        }

        /*
         * Create the persistent
         */
        final HibernateDestinationHost persistentBean = DESTINATION_HOST_FACTORY.createPersistentBean(host);

        /*
         * Store the host.
         */
        final Errors errors = new BindException(persistentBean, persistentBean.getClass().getName());
        final DestinationHost storeDestinationHost = destinationManager.storeDestinationHost(persistentBean, errors);

        /*
         * Create client bean.
         */
        final ClientDestinationHost clientBean = DESTINATION_HOST_FACTORY.createClientBean(storeDestinationHost);
        if (authorized) {
            host.setAuthorizedPrivateKey(persistentBean.getPrivateKey());
        }

        return clientBean;
    }

    @Override
    public PagingLoadResult<ClientDestinationHost> findDestinationHosts(final PagingLoadConfig config,
            final ClientDestinationHostSearchCommand sc) {
        /*
         * Setup search command.
         */
        final SortDir sortDir = config.getSortDir();
        if (sortDir.equals(SortDir.ASC)) {
            sc.setAsc(true);
        } else {
            sc.setAsc(false);
        }

        final String sortField = config.getSortField();
        if (sortField != null) {
            sc.setOrderBy(sortField);
        }

        final int limit = config.getLimit();
        sc.setMaxResults(limit);

        final int offset = config.getOffset();
        sc.setStart(offset);

        /*
         * Search for the destinations.
         */
        final List<DestinationHost> hosts = destinationManager.findDestinationHosts(sc);
        for (final DestinationHost destinationHost : hosts) {
            final UserGroup userGroup = destinationHost.getUserGroup();
            userGroup.setUsers(new ArrayList<User>());
        }

        /*
         * Create client beans.
         */
        final List<ClientDestinationHost> clientBeans = DESTINATION_HOST_FACTORY.createClientBeans(hosts);

        /*
         * Retrieve the total count.
         */
        final int count = destinationManager.findNumberOfDestinationHosts(sc);

        /*
         * Return the results for a grid.
         */
        return new BasePagingLoadResult<ClientDestinationHost>(clientBeans, config.getOffset(), count);
    }

    @Override
    public List<ClientDestinationHost> findDestinationHosts() {

        /*
         * Find hosts.
         */
        final List<DestinationHost> hosts = destinationManager.findAvailableDestinationHosts();
        for (final DestinationHost destinationHost : hosts) {
            final UserGroup userGroup = destinationHost.getUserGroup();
            userGroup.setUsers(new ArrayList<User>());
        }

        /*
         * Create client beans.
         */
        final List<ClientDestinationHost> clientBeans = DESTINATION_HOST_FACTORY.createClientBeans(hosts);

        return clientBeans;
    }

    /**
     * @param destinationManager
     *            the destinationManager to set
     */
    @Required
    public void setDestinationManager(final DestinationManager destinationManager) {
        this.destinationManager = destinationManager;
    }

    @Required
    public void setAuthorizationManager(final AuthorizationManager authorizationManager) {
        this.authorizationManager = authorizationManager;
    }

}
