package nl.tranquilizedquality.adm.commons.gwt.ext.client.view;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.data.ModelType;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;

/**
 * Abstract base class that can be used when creating a view panel that will
 * contain search criteria and the search results.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 27 jan. 2011
 * @param <SearchCommandType>
 *            The type of search criteria object model that is used on this
 *            search panel.
 */
public abstract class AbstractSearchPanel<SearchCommandType> extends LayoutContainer {

	/** The {@link FormPanel} that is used to put the search criteria fields on. */
	protected FormPanel formPanel;

	/** {@link FormBinding} object for binding a model to the form. */
	protected FormBinding binding;

	/** The search criteria bean. */
	protected SearchCommandType sc;

	/** The panel that will contain the search results. */
	protected AbstractGridPanel gridPanel;

	/** Collection of custom field bindings. */
	protected List<FieldBinding> fieldBindings;

	protected float northHeight;

	protected int northMaxHeight;

	/**
	 * Default constructor.
	 */
	public AbstractSearchPanel() {
		fieldBindings = new ArrayList<FieldBinding>();

		northHeight = 230;
		northMaxHeight = 300;
	}

	/**
	 * Initializes the GUI widgets.
	 */
	protected void initializeWidgets() {
		final BorderLayout layout = new BorderLayout();
		setLayout(layout);

		formPanel = createFilterPanel();

		gridPanel = createGridPanel();

		bindModel(this.sc);

		/*
		 * Setup layout.
		 */
		final BorderLayoutData northData = new BorderLayoutData(LayoutRegion.NORTH);
		northData.setCollapsible(true);
		northData.setFloatable(true);
		northData.setSplit(true);
		northData.setMaxSize(northMaxHeight);
		northData.setSize(northHeight);
		northData.setSplit(true);
		northData.setMargins(new Margins(1, 1, 1, 1));

		final BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);
		centerData.setMargins(new Margins(0, 1, 1, 1));
		centerData.setSplit(true);

		add(formPanel, northData);
		add(gridPanel, centerData);
	}

	/**
	 * Creates the search result panel.
	 * 
	 * @return Returns an {@link AbstractGridPanel} where the search results
	 *         will be displayed in.
	 */
	protected abstract AbstractGridPanel createGridPanel();

	/**
	 * Creates the panel where you define your filter criteria.
	 * 
	 * @return Returns the {@link FormPanel} containing the filter criteria.
	 */
	protected abstract FormPanel createFilterPanel();

	/**
	 * Binds the bean model to the form.
	 */
	protected void bindModel(final SearchCommandType model) {
		/*
		 * Check if the binding of the form was already done if so unbind.
		 */
		if (binding == null) {
			binding = new FormBinding(this.formPanel);
		}
		else {
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
	protected BeanModel createBindModel(final SearchCommandType model) {
		final BeanModelFactory factory = BeanModelLookup.get().getFactory(model.getClass());
		final BeanModel beanModel = factory.createModel(model);

		return beanModel;
	}

	/**
	 * By default there is no data to be refreshed. If some data to be refreshed
	 * in the search criteria part like combo boxes or lists you should put it
	 * here.
	 */
	protected void refreshData() {

	}

	/**
	 * By default does the query again and refreshes the search results in the
	 * table.
	 */
	protected void search() {
		this.gridPanel.refresh();
	}

}
