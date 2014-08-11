package nl.tranquilizedquality.adm.gwt.gui.client.service.logtail;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Service responsible for managing the log tailing.
 *
 * @author e-pragt (erik.pragt@Tranquilized Quality.com)
 * @since 10/30/12
 */
@RemoteServiceRelativePath("LogTailService.rpc")
public interface LogTailService extends RemoteService {

    /**
     * Utility class for simplifying access to the instance of async service.
     */
    public static class Util {

        /** The async service. */
        private static LogTailServiceAsync instance;

        /**
         * Retrieves an instance of the {@link LogTailServiceAsync}.
         *
         * @return Returns a {@link LogTailServiceAsync}.
         */
        public static LogTailServiceAsync getInstance() {
            if (instance == null) {
                instance = GWT.create(LogTailService.class);
            }
            return instance;
        }
    }

    /**
     * Start tailing the log file.
     * 
     * @param logFile
     *            The log file to tail
     */
    void startTailing(String logFile);

    /**
     * Start tailing the system log file.
     */
    void startSystemLogTailing();

    /**
     * @return The content of the read file
     */
    String getContent();

    /**
     * Stop tailing the current file.
     */
    void stopTailing();
}
