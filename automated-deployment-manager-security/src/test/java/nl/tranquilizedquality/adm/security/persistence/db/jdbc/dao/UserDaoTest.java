package nl.tranquilizedquality.adm.security.persistence.db.jdbc.dao;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import java.util.Date;

import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.security.persistence.db.dao.UserDao;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test for the {@link UserDao}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 */
public class UserDaoTest extends AbstractDaoTest {

	/** DAO that will be tested. */
	@Autowired
	private UserDao<User> apiUserDao;

	/**
	 * Cleans up the database and sets up the test data.
	 */
	@Before
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void setUp() {
		executeSqlScript("adm-test-data-cleanup.sql", false);
		executeSqlScript("adm-test-data.sql", false);
		
//		final HibernateUser user = new HibernateUser();
//		user.setActive(true);
//		user.setActiveFrom(new Date());
//		user.setCreated(new Date());
//		user.setCreatedBy("s-petrus");
//		user.setName("Salomo Petrus");
//		user.setPassword("asfdasdfas");
//		user.setUserName("s-petrus");
//
//		apiUserDao.save(user);
//		apiUserDao.flush();
	}

	/**
	 * Cleans up the database after the test.
	 */
	@After
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void cleanUp() {
		executeSqlScript("adm-test-data-cleanup.sql", false);
	}

	@Test
	public void testFindActiveUserByUserName() {
		User user = apiUserDao.findActiveUserByUserName("s-petrus");

		assertNotNull("No user found!", user);

		user = apiUserDao.findActiveUserByUserName("apinonuser");

		assertNull("User found!", user);
	}

	@Test
	public void testUpdateLastLoginDate() {
		User user = apiUserDao.findActiveUserByUserName("s-petrus");

		assertNotNull("No user found!", user);
		assertNull("User was already logged in!", user.getLastLoginDate());

		apiUserDao.updateLastLoginDate(user);
		apiUserDao.flush();

		user = apiUserDao.findActiveUserByUserName("s-petrus");
		apiUserDao.refresh(user);

		assertNotNull("No login date!", user.getLastLoginDate());
	}

}
