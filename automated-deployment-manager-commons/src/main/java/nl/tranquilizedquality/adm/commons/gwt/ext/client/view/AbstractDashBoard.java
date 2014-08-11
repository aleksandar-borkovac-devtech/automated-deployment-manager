package nl.tranquilizedquality.adm.commons.gwt.ext.client.view;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

/**
 * Abstract base class for a basic dashboard holding a {@link TabPanel}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 */
public abstract class AbstractDashBoard extends ContentPanel {

    /** The key used to retrieve the tab panel from the {@link Registry}. */
    public static final String TAB_PANEL = "TAB_PANEL";

    /**
     * Default constructor.
     */
    public AbstractDashBoard() {
        setLayout(new FitLayout());
        setBorders(false);
        setEnabled(true);

        initializeWidgets();

        Registry.register("DASH_BOARD", this);
    }

    /**
     * Initializes widgets.
     */
    protected void initializeWidgets() {
        final TabPanel tabPanel = new TabPanel();
        tabPanel.setBorders(false);
        tabPanel.setTabScroll(true);
        tabPanel.setResizeTabs(false);
        tabPanel.setAnimScroll(true);
        tabPanel.setCloseContextMenu(true);

        Registry.register(TAB_PANEL, tabPanel);

        add(tabPanel);
    }
}
