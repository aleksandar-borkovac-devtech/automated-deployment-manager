package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.user;

import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractGridPanel;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractSearchPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUserSearchCommand;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Management panel listing users.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public class UserManagementPanel extends AbstractSearchPanel<ClientUserSearchCommand> {

	/** The icons of the application. */
	private final AdmIcons icons;

	/** The member id to search on. */
	private TextField<String> userName;

	private TextField<String> name;

	private UserManagementTable memberManagementTable;

	/**
	 * Default constructor.
	 */
	public UserManagementPanel() {
		this.icons = Registry.get(AdmModule.ICONS);

		/*
		 * Set the logged in user on the search command so the initial search
		 * will be done based on this organization.
		 */
		sc = new ClientUserSearchCommand();

		/*
		 * Creates the GUI.
		 */
		initializeWidgets();
	}

	/**
	 * Initializes the GUI widgets.
	 */
	@Override
	protected void initializeWidgets() {
		final BorderLayout layout = new BorderLayout();
		setLayout(layout);

		formPanel = createFilterPanel();

		gridPanel = createGridPanel();

		/*
		 * Do binding.
		 */
		binding = new FormBinding(formPanel);

		bindModel(this.sc);

		/*
		 * Setup layout.
		 */
		final BorderLayoutData northData = new BorderLayoutData(LayoutRegion.NORTH);
		northData.setCollapsible(true);
		northData.setFloatable(true);
		northData.setSplit(true);
		northData.setMaxSize(150);
		northData.setSize(150);
		northData.setSplit(true);
		northData.setMargins(new Margins(1, 1, 1, 1));

		final BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);
		centerData.setMargins(new Margins(0, 1, 1, 1));
		centerData.setSplit(true);

		add(formPanel, northData);
		add(gridPanel, centerData);
	}

	/**
	 * Binds the model to the {@link FormBinding} object.
	 */
	@Override
	protected void bindModel(final ClientUserSearchCommand sc) {
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
		final BeanModel beanModel = createBindModel(this.sc);

		/*
		 * Bind the model object.
		 */
		binding.bind(beanModel);

		/*
		 * Initiate the auto binding.
		 */
		binding.autoBind();
	}

	/**
	 * Creates the panel where you define your filter criteria.
	 * 
	 * @return Returns the {@link FormPanel} containing the filter cirtieria.
	 */
	@Override
	protected FormPanel createFilterPanel() {
		/*
		 * Create the form panel.
		 */
		formPanel = new FormPanel();
		formPanel.setHeading("Search criteria");
		formPanel.setHeaderVisible(true);
		formPanel.setFrame(true);
		formPanel.setLabelWidth(100);
		formPanel.setButtonAlign(HorizontalAlignment.LEFT);
		formPanel.setIcon(AbstractImagePrototype.create(icons.funnel()));
		formPanel.setHeight(300);

		/*
		 * Create the left panel.
		 */
		final LayoutContainer personalDetailsContainer = new LayoutContainer();
		personalDetailsContainer.setStyleAttribute("paddingRight", "10px");
		final FormLayout layout = new FormLayout();
		layout.setLabelWidth(110);
		personalDetailsContainer.setLayout(layout);

		/*
		 * Add first name
		 */
		name = new TextField<String>();
		name.setName("name");
		name.setFieldLabel("Name");
		name.setAllowBlank(true);
		personalDetailsContainer.add(name);

		/*
		 * Add member id
		 */
		userName = new TextField<String>();
		userName.setName("userName");
		userName.setFieldLabel("User Name");
		userName.setAllowBlank(true);
		personalDetailsContainer.add(userName);

		/*
		 * Add left and right panel to the main panel.
		 */
		final LayoutContainer main = new LayoutContainer();
		main.setLayout(new ColumnLayout());
		main.add(personalDetailsContainer, new ColumnData(350));

		formPanel.add(main);

		/*
		 * Add search button.
		 */
		final Button search = new Button("Search");
		search.setIcon(AbstractImagePrototype.create(icons.find()));

		final SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(final ButtonEvent ce) {
				search();
			}

		};
		search.addSelectionListener(listener);

		formPanel.addButton(search);

		return formPanel;
	}

	/**
	 * Does the search for users.
	 */
	@Override
	protected void search() {
		/*
		 * Refresh the table by actually do the search query again.
		 */
		super.search();
	}

	@Override
	protected AbstractGridPanel createGridPanel() {
		memberManagementTable = new UserManagementTable(this.sc);
		return memberManagementTable;
	}

}
