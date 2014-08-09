package nl.tranquilizedquality.adm.gwt.gui.server.service.logtail;

import nl.tranquilizedquality.adm.core.business.manager.LogTailManager;
import nl.tranquilizedquality.adm.gwt.gui.client.service.logtail.LogTailService;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Required;

import java.io.File;

/**
 * Wrapper for the manager responsible for tailing the log.
 *
 * @author e-pragt (erik.pragt@Tranquilized Quality.com)
 * @since 10/30/12
 */
public class LogTailServiceImpl implements LogTailService {

    /** Log tail manager. */
    private LogTailManager logTailManager;

    /** The system log to tail. */
    private File systemLogFile;

    @Override
    public void startTailing(String file) {
        logTailManager.startLogTailing(new File(file));
    }

    @Override
    public void startSystemLogTailing() {
        logTailManager.startLogTailing(systemLogFile);
    }

    @Override
    public String getContent() {
        return logTailManager.getOutputLines();
    }

    @Override
    public void stopTailing() {
        logTailManager.stopLogTailing();
    }

    @Required
    public void setLogTailManager(LogTailManager logTailManager) {
        this.logTailManager = logTailManager;
    }

    @Required
    public void setSystemLogFile(String systemLogFile) {
        this.systemLogFile = new File(systemLogFile);

        Validate.isTrue(this.systemLogFile.exists(), "The file does not exist:", systemLogFile);
    }
}
