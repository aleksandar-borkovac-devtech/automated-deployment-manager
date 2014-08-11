package nl.tranquilizedquality.adm.commons.gwt.ext.client.view;

import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

/**
 * Abstract class representing a management window to edit a bean through a
 * {@link FormPanel} using {@link FormBinding} included in GWT-EXT.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 */
public abstract class AbstractManagementWindow extends Window {

    /** The {@link FormPanel}. */
    protected FormPanel formPanel;

    /** {@link FormBinding} object used to bind a form to a bean. */
    protected FormBinding binding;

    /**
     * Default constructor.
     */
    public AbstractManagementWindow() {
        this.setPlain(true);
        this.setModal(true);
        this.setBlinkModal(true);
        this.setLayout(new FitLayout());
        this.setResizable(false);
        this.setSize(400, 300);
    }

    /**
     * Initializes all widgets on the {@link Window}.
     */
    protected void initializeWidgets() {
        initializeForm();

        final Button cancelButton = new Button("Cancel");
        final SelectionListener<ButtonEvent> cancelListener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                hide();
            }
        };
        cancelButton.addSelectionListener(cancelListener);
        addButton(cancelButton);

        final Button saveButton = new Button("Save");
        final SelectionListener<ButtonEvent> saveListener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                save();
            }
        };
        saveButton.addSelectionListener(saveListener);
        addButton(saveButton);
    }

    /**
     * Initializes the widgets on the {@link FormPanel}.
     */
    protected abstract void initializeForm();

    /**
     * Saves the bean object that is bound to the form.
     */
    protected abstract void save();

    /**
     * Binds the bean model to the form.
     */
    protected void bindModel() {
        if (binding == null) {
            binding = new FormBinding(this.formPanel);
        }
        else {
            binding.unbind();
        }

        /*
         * Create the BeanModel.
         */
        final BeanModel model = createBindModel();

        binding.bind(model);

        binding.autoBind();
    }

    /**
     * Creates the {@link BeanModel} that can be used by the {@link FormBinding}
     * .
     * 
     * @return Returns a {@link BeanModel} object.
     */
    protected abstract BeanModel createBindModel();
}
