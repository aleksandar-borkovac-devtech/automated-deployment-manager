/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: Aug 23, 2012 File: fHibernateUserGroupDaoTest.java
 * Package: nl.Tranquilized Quality.adm.core.persistence.db.hibernate.dao
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
package nl.tranquilizedquality.adm.security.persistence.db.jdbc.dao;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.command.UserGroupSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.security.persistence.db.dao.UserDao;
import nl.tranquilizedquality.adm.security.persistence.db.dao.UserGroupDao;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUser;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUserGroup;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.dao.HibernateUserGroupDao;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test for {@link HibernateUserGroupDao}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 23, 2012
 */
public class HibernateUserGroupDaoTest extends AbstractDaoTest {

	/** DAO that will be tested. */
	@Autowired
	private UserDao<User> userDao;

	@Autowired
	private UserGroupDao<UserGroup> userGroupDao;

	private HibernateUser user;

	private HibernateUserGroup userGroup;

	@Before
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void setUp() {
		user = new HibernateUser();
		user.setActive(true);
		user.setActiveFrom(new Date());
		user.setCreated(new Date());
		user.setCreatedBy("s-petrus");
		user.setName("Salomo Petrus");
		user.setPassword("asfdasdfas");
		user.setUserName("s-petrus");
		user.setPasswordSent(true);
		user.setBlocked(false);

		userDao.save(user);
		userDao.flush();

		final List<User> users = new ArrayList<User>();
		users.add(user);

		userGroup = new HibernateUserGroup();
		userGroup.setCreated(new Date());
		userGroup.setCreatedBy("s-petrus");
		userGroup.setName("adminsitrators");
		userGroup.setUsers(users);

		userGroupDao.save(userGroup);
		userGroupDao.flush();
	}

	@Test
	public void testFindAll() {
		final List<UserGroup> all = userGroupDao.findAll();

		assertNotNull("No groups returned!", all);
		assertFalse("No groups returned!", all.isEmpty());

		for (final UserGroup userGroup : all) {
			final List<User> users = userGroup.getUsers();
			assertFalse("No users in group!", users.isEmpty());
		}
	}

	@Test
	public void testRemove() {
		final List<User> users = userGroup.getUsers();
		users.remove(user);

		userGroupDao.save(userGroup);
		userGroupDao.flush();

		final List<UserGroup> all = userGroupDao.findAll();

		assertNotNull("No groups returned!", all);

		for (final UserGroup userGroup : all) {
			final List<User> userGroupUsers = userGroup.getUsers();
			assertTrue("Users found!", userGroupUsers.isEmpty());
		}

	}

	@Test
	public void testFindUserGroupByUser() {
		final List<UserGroup> userGroups = userGroupDao.findUserGroupsByUser(user);

		assertEquals("Invalid number of groups found!", 1, userGroups.size());
	}

	@Test
	public void testFindUserGroupBySearchCommand() {
		final UserGroupSearchCommand sc = new UserGroupSearchCommand();
		final List<UserGroup> userGroups = new ArrayList<UserGroup>();
		userGroups.add(userGroup);
		sc.setUserGroups(userGroups);

		userGroupDao.findUserGroupsBySearchCommand(sc);
	}

}
