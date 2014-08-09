/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: Aug 23, 2012 File: fUserGroupManagerTest.java
 * Package: nl.Tranquilized Quality.adm.core.business.manager.impl
 *
 * Copyright (c) 2012 Tranquilized Quality www.Tranquilized Quality.nl All rights
 * reserved.
 *
 * This software is the confidential and proprietary information of Tranquilized Quality
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Tranquilized Quality.
 * </pre>
 */
package nl.tranquilizedquality.adm.security.business.manager.impl;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.command.UserGroupSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.security.business.manager.SecurityContextManager;
import nl.tranquilizedquality.adm.security.persistence.db.dao.UserGroupDao;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUserGroup;

import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Test for {@link UserGroupManagerImpl}.
 *
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 23, 2012
 */
public class UserGroupManagerTest extends EasyMockSupport {

    /** Manager that will be tested. */
    private UserGroupManagerImpl userGroupManager;

    /** Mocked DAO. */
    private UserGroupDao<UserGroup> userGroupDao;

    /** Mocked DAO. */
    private Validator userGroupValidator;

    /** Mocked manager. */
    private SecurityContextManager securityContextManager;

    /**
     * @throws java.lang.Exception
     */
    @Before
    @SuppressWarnings("unchecked")
    public void setUp() throws Exception {
        userGroupManager = new UserGroupManagerImpl();

        userGroupDao = createMock(UserGroupDao.class);
        userGroupValidator = createMock(Validator.class);
        securityContextManager = createMock(SecurityContextManager.class);

        userGroupManager.setUserGroupDao(userGroupDao);
        userGroupManager.setUserGroupValidator(userGroupValidator);
        userGroupManager.setSecurityContextManager(securityContextManager);
    }

    /**
     * Test method for
     * {@link nl.Tranquilized Quality.adm.security.business.exception.UserGroupManagerImpl#storeUserGroup(nl.tranquilizedquality.adm.commons.business.domain.UserGroup, org.springframework.validation.Errors)}
     * .
     */
    @Test
    public void testStoreUserGroup() {
        final UserGroup userGroup = new HibernateUserGroup();
        final Errors errors = new BindException(userGroup, userGroup.getClass().getName());

        userGroupValidator.validate(userGroup, errors);
        expectLastCall().once();
        expect(userGroupDao.newDomainObject()).andReturn(userGroup);
        expect(userGroupDao.save(userGroup)).andReturn(userGroup);

        replayAll();

        userGroupManager.storeUserGroup(userGroup, errors);

        verifyAll();
    }

    /**
     * Test method for
     * {@link nl.Tranquilized Quality.adm.security.business.exception.UserGroupManagerImpl#storeUserGroup(nl.tranquilizedquality.adm.commons.business.domain.UserGroup, org.springframework.validation.Errors)}
     * .
     */
    @Test
    public void testStorePersistentUserGroup() {
        final HibernateUserGroup userGroup = new HibernateUserGroup();
        userGroup.setId(1L);

        final Errors errors = new BindException(userGroup, userGroup.getClass().getName());

        userGroupValidator.validate(userGroup, errors);
        expectLastCall().once();
        expect(userGroupDao.findById(1L)).andReturn(userGroup);
        expect(userGroupDao.save(userGroup)).andReturn(userGroup);

        replayAll();

        final UserGroup group = userGroupManager.storeUserGroup(userGroup, errors);

        verifyAll();

        assertNotNull("Group was not saved properly!", group);
    }

    /**
     * Test method for
     * {@link nl.Tranquilized Quality.adm.security.business.exception.UserGroupManagerImpl#findUserGroupsBySearchCommand(nl.tranquilizedquality.adm.commons.business.command.UserGroupSearchCommand)}
     * .
     */
    @Test
    public void testFindUserGroupsBySearchCommand() {

        final UserGroupSearchCommand sc = new UserGroupSearchCommand();
        expect(userGroupDao.findUserGroupsBySearchCommand(sc)).andReturn(new ArrayList<UserGroup>());

        replayAll();

        final List<UserGroup> userGroups = userGroupManager.findUserGroupsBySearchCommand(sc);

        verifyAll();

        assertNotNull("No groups found!", userGroups);
    }

    /**
     * Test method for
     * {@link nl.Tranquilized Quality.adm.security.business.exception.UserGroupManagerImpl#findNumberOfUserGroups(nl.tranquilizedquality.adm.commons.business.command.UserGroupSearchCommand)}
     * .
     */
    @Test
    public void testFindNumberOfUserGroups() {
        final UserGroupSearchCommand sc = new UserGroupSearchCommand();

        expect(userGroupDao.findNumberUserGroups(sc)).andReturn(1);

        replayAll();

        final int count = userGroupManager.findNumberOfUserGroups(sc);

        verifyAll();

        assertEquals("Invalid number of user groups found!", 1, count);
    }

}
