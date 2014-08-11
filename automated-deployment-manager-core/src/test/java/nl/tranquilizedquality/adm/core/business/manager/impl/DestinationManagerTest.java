/**
 * 
 */
package nl.tranquilizedquality.adm.core.business.manager.impl;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.command.DestinationHostSearchCommand;
import nl.tranquilizedquality.adm.commons.business.command.DestinationSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameter;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameterTemplate;
import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.business.domain.DestinationHost;
import nl.tranquilizedquality.adm.commons.business.domain.Environment;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.core.business.manager.exception.InvalidDestinationException;
import nl.tranquilizedquality.adm.core.business.manager.impl.DestinationManagerImpl;
import nl.tranquilizedquality.adm.core.persistence.dao.DeployerParameterDao;
import nl.tranquilizedquality.adm.core.persistence.dao.DestinationDao;
import nl.tranquilizedquality.adm.core.persistence.dao.DestinationHostDao;
import nl.tranquilizedquality.adm.core.persistence.dao.EnvironmentDao;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDeployer;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDeployerParameter;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDestination;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDestinationHost;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateEnvironment;
import nl.tranquilizedquality.adm.security.business.manager.SecurityContextManager;
import nl.tranquilizedquality.adm.security.persistence.db.dao.UserGroupDao;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUser;

import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Test for {@link DestinationManagerImpl}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Jul 30, 2012
 */
public class DestinationManagerTest extends EasyMockSupport {

    /** Manager that will be tested. */
    private DestinationManagerImpl destinationManager;

    /** Mocked DAO. */
    private DestinationDao<Destination> destinationDao;

    /** Mocked DAO. */
    private DestinationHostDao<DestinationHost> destinationHostDao;

    /** Mocked Validator. */
    private Validator destinationHostValidator;

    /** Mocked DAO. */
    private DeployerParameterDao<DeployerParameter> deployerParameterDao;

    /** Mocked Validator. */
    private Validator destinationValidator;

    /** Mocked DAO. */
    private EnvironmentDao<Environment> environmentDao;

    /** Mocked DAO. */
    private UserGroupDao<UserGroup> userGroupDao;

    /** Mocked manager. */
    private SecurityContextManager securityContextManager;

    /** Mocked Validator. */
    private Validator environmentValidator;

    /**
     * @throws java.lang.Exception
     */
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        destinationManager = new DestinationManagerImpl();

        destinationDao = createMock(DestinationDao.class);
        destinationHostDao = createMock(DestinationHostDao.class);
        destinationHostValidator = createMock(Validator.class);
        deployerParameterDao = createMock(DeployerParameterDao.class);
        destinationValidator = createMock(Validator.class);
        environmentDao = createMock(EnvironmentDao.class);
        environmentValidator = createMock(Validator.class);
        userGroupDao = createMock(UserGroupDao.class);
        securityContextManager = createMock(SecurityContextManager.class);

        destinationManager.setDestinationDao(destinationDao);
        destinationManager.setDestinationHostDao(destinationHostDao);
        destinationManager.setDestinationHostValidator(destinationHostValidator);
        destinationManager.setDeployerParameterDao(deployerParameterDao);
        destinationManager.setDestinationValidator(destinationValidator);
        destinationManager.setEnvironmentDao(environmentDao);
        destinationManager.setEnvironmentValidator(environmentValidator);
        destinationManager.setSecurityContextManager(securityContextManager);
        destinationManager.setUserGroupDao(userGroupDao);
    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.manager.impl.DestinationManagerImpl#findAvailableDestinationHosts()}
     * .
     */
    @Test
    public void testFindAvailableDestinationHosts() {

        final HibernateUser user = new HibernateUser();
        expect(securityContextManager.findLoggedInUser()).andReturn(user);
        expect(userGroupDao.findUserGroupsByUser(user)).andReturn(new ArrayList<UserGroup>());
        expect(destinationHostDao.findBySearchCommand(isA(DestinationHostSearchCommand.class))).andReturn(
                new ArrayList<DestinationHost>());

        replayAll();

        final List<DestinationHost> availableDestinationHosts = destinationManager.findAvailableDestinationHosts();

        verifyAll();

        assertNotNull("No hosts found!", availableDestinationHosts);
    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.manager.impl.DestinationManagerImpl#findAvailableDestinations()}
     * .
     */
    @Test
    public void testFindAvailableDestinations() {

        final HibernateUser user = new HibernateUser();
        expect(securityContextManager.findLoggedInUser()).andReturn(user);
        expect(userGroupDao.findUserGroupsByUser(user)).andReturn(new ArrayList<UserGroup>());
        expect(destinationDao.findBySearchCommand(isA(DestinationSearchCommand.class))).andReturn(new ArrayList<Destination>());

        replayAll();

        final List<Destination> availableDestination = destinationManager.findAvailableDestinations();

        verifyAll();

        assertNotNull("No destinations found!", availableDestination);
    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.manager.impl.DestinationManagerImpl#findAvailableEnvironments()}
     * .
     */
    @Test
    public void testFindAvailableEnvironments() {
        expect(environmentDao.findAll()).andReturn(new ArrayList<Environment>());

        replayAll();

        final List<Environment> availableEnvironments = destinationManager.findAvailableEnvironments();

        verifyAll();

        assertNotNull("No environments found!", availableEnvironments);
    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.manager.impl.DestinationManagerImpl#findDestinationById(java.lang.Long)}
     * .
     */
    @Test
    public void testFindDestinationById() {
        final HibernateDeployer deployer = new HibernateDeployer();
        deployer.setParameters(new ArrayList<DeployerParameterTemplate>());
        final HibernateDestination hibernateDestination = new HibernateDestination();
        hibernateDestination.setDeployer(deployer);

        expect(destinationDao.findById(1L)).andReturn(hibernateDestination);

        replayAll();

        final Destination destination = destinationManager.findDestinationById(1L);

        verifyAll();

        assertNotNull("No destination found!", destination);
    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.manager.impl.DestinationManagerImpl#findDestinationHostById(java.lang.Long)}
     * .
     */
    @Test
    public void testFindDestinationHostById() {
        expect(destinationHostDao.findById(1L)).andReturn(new HibernateDestinationHost());

        replayAll();

        final DestinationHost destinationHost = destinationManager.findDestinationHostById(1L);

        verifyAll();

        assertNotNull("No destination host found!", destinationHost);
    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.manager.impl.DestinationManagerImpl#findDestinationHosts(nl.tranquilizedquality.adm.commons.business.command.DestinationHostSearchCommand)}
     * .
     */
    @Test
    public void testFindDestinationHosts() {
        final HibernateUser user = new HibernateUser();
        expect(securityContextManager.findLoggedInUser()).andReturn(user);
        expect(userGroupDao.findUserGroupsByUser(user)).andReturn(new ArrayList<UserGroup>());
        final DestinationHostSearchCommand sc = new DestinationHostSearchCommand();
        expect(destinationHostDao.findBySearchCommand(sc)).andReturn(new ArrayList<DestinationHost>());

        replayAll();

        final List<DestinationHost> destinationHosts = destinationManager.findDestinationHosts(sc);

        verifyAll();

        assertNotNull("No destination hosts found!", destinationHosts);
    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.manager.impl.DestinationManagerImpl#findDestinationLocationById(java.lang.Long)}
     * .
     */
    @Test
    public void testFindDestinationLocationById() {
        expect(deployerParameterDao.findById(1L)).andReturn(new HibernateDeployerParameter());

        replayAll();

        final DeployerParameter destinationLocation = destinationManager.findDestinationLocationById(1L);

        verifyAll();

        assertNotNull("No destination location found!", destinationLocation);
    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.manager.impl.DestinationManagerImpl#findDestinations(nl.tranquilizedquality.adm.commons.business.command.DestinationSearchCommand)}
     * .
     */
    @Test
    public void testFindDestinations() {
        final DestinationSearchCommand sc = new DestinationSearchCommand();
        final HibernateUser user = new HibernateUser();

        expect(securityContextManager.findLoggedInUser()).andReturn(user);
        expect(userGroupDao.findUserGroupsByUser(user)).andReturn(new ArrayList<UserGroup>());
        expect(destinationDao.findBySearchCommand(sc)).andReturn(new ArrayList<Destination>());

        replayAll();

        final List<Destination> destinations = destinationManager.findDestinations(sc);

        verifyAll();

        assertNotNull("No destinations found!", destinations);
    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.manager.impl.DestinationManagerImpl#findEnvironmentById(java.lang.Long)}
     * .
     */
    @Test
    public void testFindEnvironmentById() {
        expect(environmentDao.findById(1L)).andReturn(new HibernateEnvironment());

        replayAll();

        final Environment environment = destinationManager.findEnvironmentById(1L);

        verifyAll();

        assertNotNull("No environment found!", environment);
    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.manager.impl.DestinationManagerImpl#findNumberOfDestinationHosts(nl.tranquilizedquality.adm.commons.business.command.DestinationHostSearchCommand)}
     * .
     */
    @Test
    public void testFindNumberOfDestinationHosts() {

        final HibernateUser user = new HibernateUser();
        expect(securityContextManager.findLoggedInUser()).andReturn(user);
        expect(userGroupDao.findUserGroupsByUser(user)).andReturn(new ArrayList<UserGroup>());
        final DestinationHostSearchCommand sc = new DestinationHostSearchCommand();
        expect(destinationHostDao.findNumberOfDestinationHosts(sc)).andReturn(1);

        replayAll();

        final int numberOfDestinationHosts = destinationManager.findNumberOfDestinationHosts(sc);

        verifyAll();

        assertEquals("Invalid number of destination hosts found!", 1, numberOfDestinationHosts);
    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.manager.impl.DestinationManagerImpl#findNumberOfDestinations(nl.tranquilizedquality.adm.commons.business.command.DestinationSearchCommand)}
     * .
     */
    @Test
    public void testFindNumberOfDestinations() {
        final HibernateUser user = new HibernateUser();
        expect(securityContextManager.findLoggedInUser()).andReturn(user);
        expect(userGroupDao.findUserGroupsByUser(user)).andReturn(new ArrayList<UserGroup>());
        final DestinationSearchCommand sc = new DestinationSearchCommand();
        expect(destinationDao.findNumberOfDestinations(sc)).andReturn(1);

        replayAll();

        final int numberOfDestinations = destinationManager.findNumberOfDestinations(sc);

        verifyAll();

        assertEquals("Invalid number of destinations found!", 1, numberOfDestinations);
    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.manager.impl.DestinationManagerImpl#removeDestinationLocation(nl.tranquilizedquality.adm.commons.business.domain.DeployerParameter)}
     * .
     */
    @Test
    public void testRemoveDestinationLocation() {
        final HibernateDeployerParameter location = new HibernateDeployerParameter();
        location.setId(1L);

        deployerParameterDao.delete(location);
        expectLastCall().once();

        replayAll();

        destinationManager.removeDestinationLocation(location);

        verifyAll();
    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.manager.impl.DestinationManagerImpl#removeDestinationLocation(nl.tranquilizedquality.adm.commons.business.domain.DeployerParameter)}
     * .
     */
    @Test
    public void testRemoveDestinationLocationFailure() {
        final HibernateDeployerParameter location = new HibernateDeployerParameter();

        replayAll();

        try {
            destinationManager.removeDestinationLocation(location);
            fail("Exception should be thrown!");
        } catch (final Exception e) {
            assertTrue("Invalid exception thrown!", e instanceof InvalidDestinationException);
        }

        verifyAll();
    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.manager.impl.DestinationManagerImpl#storeDestination(nl.tranquilizedquality.adm.commons.business.domain.Destination, org.springframework.validation.Errors)}
     * .
     */
    @Test
    public void testStoreDestination() {

        final HibernateDestination destination = new HibernateDestination();
        final Errors errors = new BindException(destination, destination.getClass().getName());

        destinationValidator.validate(destination, errors);
        expectLastCall().once();
        expect(destinationDao.save(destination)).andReturn(destination);

        replayAll();

        destinationManager.storeDestination(destination, errors);

        verifyAll();
    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.manager.impl.DestinationManagerImpl#storeDestinationHost(nl.tranquilizedquality.adm.commons.business.domain.DestinationHost, org.springframework.validation.Errors)}
     * .
     */
    @Test
    public void testStoreDestinationHost() {

        final HibernateDestinationHost destinationHost = new HibernateDestinationHost();
        final Errors errors = new BindException(destinationHost, destinationHost.getClass().getName());

        destinationHostValidator.validate(destinationHost, errors);
        expectLastCall().once();
        expect(destinationHostDao.save(destinationHost)).andReturn(destinationHost);

        replayAll();

        destinationManager.storeDestinationHost(destinationHost, errors);

        verifyAll();
    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.manager.impl.DestinationManagerImpl#storeEnvironment(nl.tranquilizedquality.adm.commons.business.domain.Environment, org.springframework.validation.Errors)}
     * .
     */
    @Test
    public void testStoreEnvironment() {
        final HibernateEnvironment environment = new HibernateEnvironment();
        final Errors errors = new BindException(environment, environment.getClass().getName());

        environmentValidator.validate(environment, errors);
        expectLastCall().once();
        expect(environmentDao.newDomainObject()).andReturn(environment);
        expect(environmentDao.save(environment)).andReturn(environment);

        replayAll();

        destinationManager.storeEnvironment(environment, errors);

        verifyAll();
    }

}
