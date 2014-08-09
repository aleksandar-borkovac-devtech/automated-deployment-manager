package nl.tranquilizedquality.adm.core.persistence.io;

import org.apache.commons.io.input.TailerListenerAdapter;

/**
 * Tails logs by adding them to a blocking deque
 *
 * @author e-pragt (erik.pragt@Tranquilized Quality.com)
 * @since 10/26/12
 */
public class LogTailerListener extends TailerListenerAdapter {

    private StringBuilder buffer = new StringBuilder();

    @Override
    public void handle(String line) {
        buffer.append(line).append("\n");
    }

    public String takeAll() {
        String result = buffer.toString();
        buffer = new StringBuilder();
        return result;
    }
}
