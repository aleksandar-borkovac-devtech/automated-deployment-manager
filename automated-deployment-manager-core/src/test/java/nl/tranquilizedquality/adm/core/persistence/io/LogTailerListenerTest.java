package nl.tranquilizedquality.adm.core.persistence.io;

import nl.tranquilizedquality.adm.core.persistence.io.LogTailerListener;

import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author e-pragt (erik.pragt@Tranquilized Quality.com)
 * @since 10/29/12
 */
public class LogTailerListenerTest {

    private static final String line1 = "line1";
    private static final String line2 = "line2";

    @Test
    public void testAddOne() {
        LogTailerListener listener = new LogTailerListener();

        listener.handle(line1);

        assertThat(listener.takeAll(), equalTo(line1 + "\n"));
    }

    @Test
    public void testAddMultipleLines() {
        LogTailerListener listener = new LogTailerListener();

        listener.handle(line1);
        listener.handle(line2);

        assertThat(listener.takeAll(), equalTo(line1 + "\n" + line2 + "\n"));
    }

    @Test
    public void testAddMultipleTakes() {
        LogTailerListener listener = new LogTailerListener();

        listener.handle(line1);

        assertThat(listener.takeAll(), equalTo(line1 + "\n"));

        listener.handle(line2);

        assertThat(listener.takeAll(), equalTo(line2 + "\n"));
    }
}
