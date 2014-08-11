package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.logtail;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.ProgressBar;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;

/**
 * Enhanced panel for keeping track of progress.
 *
 * @author e-pragt (erik.pragt@Tranquilized Quality.com)
 * @since 10/30/12
 */
public class ProgressPanel extends LayoutContainer {

    /** Progress message. */
    private Text label;

    /** Progress bar. */
    private ProgressBar progressBar;

    /** The log tail panel. */
    private LogTailPanel logTailPanel;

    /**
     * Creates a new progress panel.
     */
    public ProgressPanel() {
        initializeWidgets();
    }

    private void initializeWidgets() {
        setLayout(new FitLayout());

        LayoutContainer panel = new LayoutContainer();
        panel.setLayout(new RowLayout(Style.Orientation.VERTICAL));

        label = new Text("Progress text");
        progressBar = new ProgressBar();

        logTailPanel = new LogTailPanel();
        logTailPanel.startTailingSystemLog();

        panel.add(label, new RowData(1, Style.DEFAULT, new Margins(4)));
        panel.add(progressBar, new RowData(1, Style.DEFAULT, new Margins(0, 4, 0, 4)));
        panel.add(logTailPanel, new RowData(1, 1, new Margins(4)));

        add(panel, new FitData(4));
    }

    /**
     * Stop tailing of the log file.
     */
    public void stopTailing() {
        logTailPanel.stopTailing();
    }

    /**
     * Sets the message for the current activity.
     *
     * @param message
     *            The message to set
     */
    public void setMessage(String message) {
        label.setText(message);
    }

    /**
     * Set text in the progress bar.
     * 
     * @param text
     *            The text to set
     */
    public void setProgressText(String text) {
        progressBar.updateText(text);
    }

    /**
     * Set text and progress in the progress bar.
     *
     * @param increment
     *            A value between 0 and 1 (e.g., .5, defaults to 0)
     * @param text
     *            The string to display in the progress text element
     */
    public void setProgress(float increment, String text) {
        progressBar.updateProgress(increment, text);
    }
}
