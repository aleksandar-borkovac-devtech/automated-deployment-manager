package nl.tranquilizedquality.adm.core.business.manager;

import java.io.File;
import java.io.Serializable;

/**
 * @author e-pragt (erik.pragt@Tranquilized Quality.com)
 * @since 10/26/12
 */
public interface LogTailManager extends Serializable {

    /**
     * Start monitoring a specific log file to get feedback.
     * 
     * @param logFile
     *        The log file to watch
     */
    void startLogTailing(File logFile);

    /**
     * @return Get the next output line.
     */
    String getOutputLines();

    /**
     * Stop tailing the log.
     */
    void stopLogTailing();

}
