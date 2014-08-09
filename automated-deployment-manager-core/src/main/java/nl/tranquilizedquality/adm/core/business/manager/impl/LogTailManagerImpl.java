package nl.tranquilizedquality.adm.core.business.manager.impl;

import nl.tranquilizedquality.adm.core.business.manager.LogTailManager;
import nl.tranquilizedquality.adm.core.persistence.io.LogTailerListener;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Required;

import java.io.File;

/**
 * Implementation for tailing logs using commons io.
 *
 * @author e-pragt (erik.pragt@Tranquilized Quality.com)
 * @since 10/26/12
 */
public class LogTailManagerImpl implements LogTailManager {

    /** Listener for responding to log events. */
    private LogTailerListener listener;

    /** Manager for the tailer listener. */
    private Tailer tailer;

    @Override
    public void startLogTailing(File logFile) {
        tailer = Tailer.create(logFile, listener, 1000, true);
    }

    @Override
    public String getOutputLines() {
        Validate.notNull(tailer, "Trying to get output line while tailer hasn't started.");

        return listener.takeAll();
    }

    @Override
    public void stopLogTailing() {
        Validate.notNull(tailer, "Trying to stop tailer while tailer hasn't started.");

        tailer.stop();
    }

    @Required
    public void setLogTailerListener(LogTailerListener listener) {
        this.listener = listener;
    }

}
