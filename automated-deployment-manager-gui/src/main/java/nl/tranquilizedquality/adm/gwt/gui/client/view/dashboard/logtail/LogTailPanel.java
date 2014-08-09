package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.logtail;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.service.logtail.LogTailServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.util.StringUtils;

import java.util.Date;

/**
 * Panel for tailing log files.
 *
 * @author e-pragt (erik.pragt@Tranquilized Quality.com)
 * @since 10/30/12
 */
public class LogTailPanel extends LayoutContainer {

    /** Delay which will be used in case of a failure. */
    public static final int FAILURE_DELAY_MILLIS = 5000;

    public static final String LAST_UPDATED_AT = "Last updated at: ";

    /** The text area which will contain the output. */
    private TextArea textArea;

    /** Timer for refreshing the ui. */
    private Timer timer;

    /** Last modified time label. */
    private Label statusLabel;

    public LogTailPanel() {
        initializeWidgets();
    }

    protected void initializeWidgets() {
        final BorderLayout layout = new BorderLayout();
        setLayout(layout);

        BorderLayoutData centerData = new BorderLayoutData(Style.LayoutRegion.CENTER);

        BorderLayoutData southData = new BorderLayoutData(Style.LayoutRegion.SOUTH, 15);
        southData.setMargins(new Margins(4));

        statusLabel = new Label(LAST_UPDATED_AT);

        textArea = new TextArea();
        statusLabel = new Label();

        add(textArea, centerData);
        add(statusLabel, southData);
    }

    /**
     * Tail the system log.
     */
    public void startTailingSystemLog() {
        final LogTailServiceAsync logTailServiceAsync = Registry.get(AdmModule.LOG_TAIL_SERVICE);

        logTailServiceAsync.startSystemLogTailing(new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                Info.display("Error", "Tailing system log file failed: " + throwable.getMessage());

                throwable.printStackTrace();
            }

            @Override
            public void onSuccess(Void aVoid) {
                Info.display("Message", "Start tailing of system log file");

                startTailing();
            }
        });
    }

    /**
     * Stop tailing the log file.
     */
    public void stopTailing() {
        final LogTailServiceAsync logTailServiceAsync = Registry.get(AdmModule.LOG_TAIL_SERVICE);

        logTailServiceAsync.stopTailing(new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                Info.display("Error", "Stop tailing  failed." + throwable.getMessage());
            }

            @Override
            public void onSuccess(Void aVoid) {
            }
        });
    }

    /**
     * Starts tailing for new log lines.
     */
    private void startTailing() {
        timer = new Timer() {

            @Override
            public void run() {
                appendLog();
            }
        };

        appendLog();
    }

    /**
     * Appends the recieved log lines to the textarea.
     */
    private void appendLog() {
        final LogTailServiceAsync logTailServiceAsync = Registry.get(AdmModule.LOG_TAIL_SERVICE);

        logTailServiceAsync.getContent(new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable throwable) {
                Info.display("Message", "An error occured: " + throwable.getMessage());

                timer.schedule(FAILURE_DELAY_MILLIS);
            }

            @Override
            public void onSuccess(String logLines) {
                if (StringUtils.isNotEmpty(logLines)) {
                    // Append the log line
                    final String value = StringUtils.defaultString(textArea.getValue());
                    textArea.setValue(value + logLines);

                    // Tail the textarea
                    final Element element = textArea.getElement();
                    final com.google.gwt.dom.client.Element firstChildElement = element.getFirstChildElement();
                    final int scrollHeight = firstChildElement.getScrollHeight();
                    firstChildElement.setScrollTop(scrollHeight);

                    // Update the last updated timestamp
                    final String lastUpdatedAt = LAST_UPDATED_AT + new Date();
                    statusLabel.setText(lastUpdatedAt);
                }
                timer.schedule(500);
            }
        });
    }
}
