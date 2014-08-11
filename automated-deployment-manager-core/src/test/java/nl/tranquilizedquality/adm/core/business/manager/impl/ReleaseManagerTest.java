/**
 * 
 */
package nl.tranquilizedquality.adm.core.business.manager.impl;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import nl.tranquilizedquality.adm.commons.business.domain.Release;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseStatus;
import nl.tranquilizedquality.adm.core.business.manager.exception.ReleaseAlreadyInUseException;
import nl.tranquilizedquality.adm.core.business.manager.impl.ReleaseManagerImpl;
import nl.tranquilizedquality.adm.core.persistence.dao.ReleaseDao;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateRelease;

import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.springframework.validation.Validator;

/**
 * Test for {@link ReleaseManagerImpl}.
 * 
 * @author Salomo Petrus salomo.petrus@tr-quality.com.
 * 
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class ReleaseManagerTest extends EasyMockSupport {

    /** Manager that will be tested */
    private ReleaseManagerImpl releaseManager;

    /** Mocked DAO */
    private ReleaseDao<Release> releaseDao;

    /** Mocked DAO */
    private Validator releaseValidator;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        releaseManager = new ReleaseManagerImpl();

        releaseDao = createMock(ReleaseDao.class);
        releaseValidator = createMock(Validator.class);

        releaseManager.setReleaseDao(releaseDao);
        releaseManager.setReleaseValidator(releaseValidator);
    }

    @Test
    public void testRemoveRelease() {
        final Long releaseId = 1L;

        final HibernateRelease release = new HibernateRelease();
        release.setId(releaseId);
        release.setStatus(ReleaseStatus.DRAFT);

        expect(releaseDao.findById(releaseId)).andReturn(release);
        releaseDao.delete(release);
        expectLastCall().once();

        replayAll();

        releaseManager.removeRelease(release);

        verifyAll();
    }

    @Test
    public void testRemoveReleaseFailed() {
        final Long releaseId = 1L;

        final HibernateRelease release = new HibernateRelease();
        release.setId(releaseId);
        release.setStatus(ReleaseStatus.DRAFT);
        release.setReleaseCount(22);

        expect(releaseDao.findById(releaseId)).andReturn(release);

        replayAll();

        try {
            releaseManager.removeRelease(release);
            fail("Exception should have been thrown!");
        } catch (final Exception e) {
            assertTrue("Invalid exception thrown!", e instanceof ReleaseAlreadyInUseException);
        }

        verifyAll();
    }

}
