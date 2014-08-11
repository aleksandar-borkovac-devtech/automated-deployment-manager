package nl.tranquilizedquality.adm.commons.gwt.ext.client.view;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.domain.DomainObject;

import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;

/**
 * An abstract panel that can be used to create a form panel binded to a
 * {@link DomainObject}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 * @param <ModelType>
 *            The domain object that this detail panel is displaying.
 */
@SuppressWarnings({"rawtypes" })
public abstract class AbstractDetailPanel<ModelType extends DomainObject> extends LayoutContainer implements
        NavigationalItem<ModelType> {

    /** The {@link FormPanel} that is used to put all the editable fields on. */
    protected FormPanel formPanel;

    /** {@link FormBinding} object used to bind a form to a bean. */
    protected FormBinding binding;

    /** Collection of custom field bindings. */
    protected List<FieldBinding> fieldBindings;

    /** The model that is being displayed. */
    protected ModelType model;

    /**
     * Default constructor.
     */
    public AbstractDetailPanel() {
        fieldBindings = new ArrayList<FieldBinding>();
    }

    /**
     * Sets the model object.
     * 
     * @param model
     *            The model that will be set.
     */
    @Override
    public abstract void setModel(ModelType model);

    /**
     * Initializes the widgets on the detail panel.
     */
    protected void initializeWidgets() {
        final BorderLayout layout = new BorderLayout();
        setLayout(layout);

        formPanel = createDetailPanel();
    }

    /**
     * Checks the privileges of the logged in user to enable and disable certain
     * GUI functionality.
     */
    protected abstract void performPrivilegeCheck();

    /**
     * Creates the details panel where the details of the {@link Organization}
     * are displayed in.
     * 
     * @return Returns a {@link FormPanel} with all the appropriate controls on
     *         it.
     */
    protected abstract FormPanel createDetailPanel();

    /**
     * Binds the bean model to the form.
     */
    protected void bindModel(final ModelType model) {
        /*
         * Check if the binding of the form was already done if so unbind.
         */
        if (binding == null) {
            binding = new FormBinding(this.formPanel);
        } else {
            binding.unbind();
        }

        /*
         * Create the BeanModel.
         */
        final BeanModel beanModel = createBindModel(model);

        /*
         * Add custom field bindings.
         */
        if (fieldBindings != null && !fieldBindings.isEmpty()) {
            for (final FieldBinding fieldBinding : fieldBindings) {
                binding.addFieldBinding(fieldBinding);
            }
        }

        /*
         * Bind the model.
         */
        binding.bind(beanModel);

        /*
         * Initiate the auto binding.
         */
        binding.autoBind();
    }

    /**
     * Create the {@link BeanModel} of the {@link ModelType} object.
     * 
     * @param model
     *            the model that will be transformed into a {@link BeanModel}.
     * @return the resulting model object.
     */
    private BeanModel createBindModel(final ModelType model) {
        final BeanModelFactory factory = BeanModelLookup.get().getFactory(model.getClass());
        final BeanModel beanModel = factory.createModel(model);

        return beanModel;
    }

    /**
     * Retrieves the model that is being managed on this panel.
     * 
     * @return Returns the model itself.
     */
    public ModelType getModelObject() {
        return model;
    }

}
