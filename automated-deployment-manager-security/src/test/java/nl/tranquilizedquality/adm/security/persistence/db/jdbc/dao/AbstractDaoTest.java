package nl.tranquilizedquality.adm.security.persistence.db.jdbc.dao;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Base class for all DAO test classes.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:adm-security-dao-context.xml",
        "classpath:adm-security-annotated-classes-context.xml", "classpath:adm-security-transaction-context.xml",
        "classpath:adm-security-db-context.xml" })
@TransactionConfiguration(transactionManager = "apiTransactionManager", defaultRollback = true)
@Transactional()
public abstract class AbstractDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

}
