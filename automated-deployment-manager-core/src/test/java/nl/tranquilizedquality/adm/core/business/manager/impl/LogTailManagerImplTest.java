package nl.tranquilizedquality.adm.core.business.manager.impl;

import nl.tranquilizedquality.adm.core.business.manager.impl.LogTailManagerImpl;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

/**
 * Creates a file and writes to it, and checks that the output written is also the output received.
 *
 * @author e-pragt (erik.pragt@Tranquilized Quality.com)
 * @since 10/29/12
 */
public class LogTailManagerImplTest {

    public static final String OUTPUT = "output";

    public static final boolean APPEND = true;

    private File file;

    private LogTailManagerImpl logTailManager;

    @Before
    public void setUp() throws IOException {
        logTailManager = new LogTailManagerImpl();

        file = File.createTempFile("logappender", ".tmp");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionForGetOutputLineWhenTailerNotStarted() {

        logTailManager.getOutputLines();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionForStopLogTailingWhenTailerNotStarted() {
        logTailManager.stopLogTailing();
    }
}
