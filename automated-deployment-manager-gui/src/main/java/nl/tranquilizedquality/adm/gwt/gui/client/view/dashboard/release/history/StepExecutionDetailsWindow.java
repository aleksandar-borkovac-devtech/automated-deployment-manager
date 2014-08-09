package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.release.history;

import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientReleaseStepExecution;
import nl.tranquilizedquality.adm.gwt.gui.client.view.AdmViewPort;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Window where the details of a step execution is displayed in.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 16 okt. 2012
 */
public class StepExecutionDetailsWindow extends Window {

    /** The icons of the application. */
    private final AdmIcons icons;

    /** The selection panel. */
    private final StepExecutionDetailsPanel selectionPanel;

    /** Determines if it should unmask the view. */
    private boolean unmask = true;

    public void setUnmask(final boolean unmask) {
        this.unmask = unmask;
    }

    /**
     * Default constructor.
     */
    public StepExecutionDetailsWindow() {
        setHeading("Step Execution");
        setClosable(true);
        setLayout(new FillLayout());
        setSize(600, 260);
        setResizable(false);

        this.icons = Registry.get(AdmModule.ICONS);

        setIcon(AbstractImagePrototype.create(icons.releaseHistory()));

        this.selectionPanel = new StepExecutionDetailsPanel();

        add(selectionPanel);
    }

    @Override
    public void hide() {
        super.hide();

        if (unmask) {
            final AdmViewPort viewPort = Registry.get(AdmModule.VIEW_PORT);
            viewPort.unmask();
        }

        unmask = true;
    }

    public void setModel(final ClientReleaseStepExecution execution) {
        this.selectionPanel.setModel(execution);
    }

}
