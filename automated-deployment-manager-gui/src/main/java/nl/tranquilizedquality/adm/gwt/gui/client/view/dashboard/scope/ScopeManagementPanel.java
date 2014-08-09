package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.scope;

import nl.tranquilizedquality.adm.commons.business.domain.Scope;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractGridPanel;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractSearchPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.scope.ClientScopeSearchCommand;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Management panel that lists {@link Scope} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 26, 2012
 * 
 */
public class ScopeManagementPanel extends AbstractSearchPanel<ClientScopeSearchCommand> {

	/** The icons of the application. */
	private final AdmIcons icons;

	/** The name field. */
	private TextField<String> name;

	/** The description field. */
	private TextField<String> description;

	/**
	 * Default constructor.
	 */
	public ScopeManagementPanel() {
		this.icons = Registry.get(AdmModule.ICONS);

		sc = new ClientScopeSearchCommand();

		initializeWidgets();
	}

	@Override
	protected void initializeWidgets() {
		setLayout(new BorderLayout());

		/*
		 * Create the filter panel.
		 */
		formPanel = createFilterPanel();

		/*
		 * Create table.
		 */
		gridPanel = createGridPanel();

		/*
		 * Do binding.
		 */
		bindModel(this.sc);

		/*
		 * Setup layout.
		 */
		final BorderLayoutData northData = new BorderLayoutData(LayoutRegion.NORTH);
		northData.setCollapsible(true);
		northData.setFloatable(true);
		northData.setSplit(true);
		northData.setMaxSize(300);
		northData.setSize(160);
		northData.setSplit(true);
		northData.setMargins(new Margins(1, 1, 1, 1));

		final BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);
		centerData.setMargins(new Margins(0, 1, 1, 1));
		centerData.setSplit(true);

		add(formPanel, northData);
		add(gridPanel, centerData);
	}

	/**
	 * Creates the panel where you define your filter criteria.
	 * 
	 * @return Returns the {@link FormPanel} containing the filter criteria.
	 */
	@Override
	protected FormPanel createFilterPanel() {
		final FormPanel formPanel = new FormPanel();
		formPanel.setHeading("Search criteria");
		formPanel.setFrame(true);
		formPanel.setLabelWidth(200);
		formPanel.setButtonAlign(HorizontalAlignment.LEFT);
		formPanel.setIcon(AbstractImagePrototype.create(icons.funnel()));
		formPanel.setHeight(160);

		/*
		 * Add name.
		 */
		name = new TextField<String>();
		name.setName("name");
		name.setFieldLabel("Name");
		name.setAllowBlank(true);
		formPanel.add(name);

		/*
		 * Add house number.
		 */
		description = new TextField<String>();
		description.setName("description");
		description.setFieldLabel("Description");
		description.setAllowBlank(true);
		formPanel.add(description);

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

	@Override
	protected AbstractGridPanel createGridPanel() {
		return new ScopeTable(this.sc);
	}

}
