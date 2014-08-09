package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.release.history;

import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractDetailPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientReleaseStepExecution;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Details panel where the step execution details are displayed on.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 16 okt. 2012
 */
public class StepExecutionDetailsPanel extends AbstractDetailPanel<ClientReleaseStepExecution> {

    /** The icons of the application. */
    private final AdmIcons icons;

    /** The name of the step. */
    private LabelField name;

    /** The status of the step. */
    private LabelField deployStatus;

    /** The error message. */
    private TextArea errorMessage;

    /**
     * Default constructor.
     */
    public StepExecutionDetailsPanel() {
        icons = Registry.get(AdmModule.ICONS);

        initializeWidgets();
    }

    @Override
    protected void initializeWidgets() {
        /*
         * Set layout.
         */
        final BorderLayout layout = new BorderLayout();
        setLayout(layout);

        /*
         * Create details panel.
         */
        formPanel = createDetailPanel();

        add(this.formPanel, new BorderLayoutData(LayoutRegion.CENTER, 170, 170, 170));
    }

    @Override
    public void setModel(final ClientReleaseStepExecution model) {
        this.model = model;
        bindModel(this.model);
    }

    @Override
    protected void performPrivilegeCheck() {

    }

    @Override
    protected FormPanel createDetailPanel() {
        final FormPanel formPanel = new FormPanel();
        formPanel.setHeading("Details");
        formPanel.setFrame(true);
        formPanel.setLabelWidth(90);
        formPanel.setIcon(AbstractImagePrototype.create(icons.releaseHistory()));
        formPanel.setButtonAlign(HorizontalAlignment.LEFT);

        /*
         * Create field set for release related information.
         */
        final FieldSet releaseInfoFieldSet = new FieldSet();
        releaseInfoFieldSet.setLayout(new FormLayout());
        releaseInfoFieldSet.setCollapsible(false);
        releaseInfoFieldSet.setAutoHeight(true);
        releaseInfoFieldSet.setHeading("Step Information");

        /*
         * Add release name.
         */
        name = new LabelField();
        name.setId("release-step-execution-history-details-pnl-name");
        name.setName("name");
        name.setFieldLabel("Step");
        name.setReadOnly(true);
        releaseInfoFieldSet.add(name);

        /*
         * Add deploy status.
         */
        deployStatus = new LabelField();
        deployStatus.setId("release-step-execution-history-details-pnl-status");
        deployStatus.setName("status");
        deployStatus.setFieldLabel("Status");
        deployStatus.setReadOnly(true);
        releaseInfoFieldSet.add(deployStatus);

        /*
         * Add error message.
         */
        errorMessage = new TextArea();
        errorMessage.setId("release-step-execution-history-details-pnl-error-message");
        errorMessage.setName("errorMessage");
        errorMessage.setFieldLabel("Message");
        errorMessage.setWidth(50);
        errorMessage.setEnabled(true);
        errorMessage.setReadOnly(true);
        releaseInfoFieldSet.add(errorMessage, new FormData("100%"));

        formPanel.add(releaseInfoFieldSet);

        return formPanel;
    }

}
