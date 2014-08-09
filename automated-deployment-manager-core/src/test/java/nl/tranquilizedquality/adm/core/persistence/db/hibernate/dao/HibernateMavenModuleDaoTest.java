/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 27 jul. 2011 File: HibernateMavenModuleDaoTest.java
 * Package: nl.tranquilizedquality.adm.core.persistence.db.hibernate.dao
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
package nl.tranquilizedquality.adm.core.persistence.db.hibernate.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.ArtifactType;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateMavenModule;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.dao.HibernateMavenModuleDao;
import nl.tranquilizedquality.adm.security.persistence.db.dao.UserDao;
import nl.tranquilizedquality.adm.security.persistence.db.dao.UserGroupDao;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUser;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUserGroup;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test for {@link HibernateMavenModuleDao}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 27 jul. 2011
 */
public class HibernateMavenModuleDaoTest extends AbstractDaoTest {

	/** DAO that will be tested. */
	@Autowired
	private HibernateMavenModuleDao mavenModuleDao;

	@Autowired
	private UserDao<User> userDao;

	@Autowired
	private UserGroupDao<UserGroup> userGroupDao;

	@Before
	public void setUp() {

		final HibernateUser user = new HibernateUser();
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

		final HibernateUserGroup userGroup = new HibernateUserGroup();
		userGroup.setCreated(new Date());
		userGroup.setCreatedBy("s-petrus");
		userGroup.setName("adminsitrators");
		userGroup.setUsers(users);

		userGroupDao.save(userGroup);
		userGroupDao.flush();

		final HibernateMavenModule module = new HibernateMavenModule();
		module.setName("adm-gwt-gui");
		module.setType(ArtifactType.WAR);
		module.setCreated(new Date());
		module.setCreatedBy("s-petrus");
		module.setGroup("nl.Tranquilized Quality");
		module.setArtifactId("adm-gwt-gui");
		module.setTargetSystemShutdown(true);
		module.setTargetSystemStartup(true);
		module.setUserGroup(userGroup);

		mavenModuleDao.save(module);
		mavenModuleDao.flush();
	}

	@Test
	public void testFindAll() {
		final List<HibernateMavenModule> modules = mavenModuleDao.findAll();

		assertNotNull("No modules!", modules);
		assertEquals("Invalid number of modules!", 1, modules.size());

	}

}
