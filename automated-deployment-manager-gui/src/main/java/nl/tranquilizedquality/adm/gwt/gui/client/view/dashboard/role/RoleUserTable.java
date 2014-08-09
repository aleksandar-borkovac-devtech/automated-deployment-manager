package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.role;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractRelationListTable;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.controller.navigation.AdmNavigationController;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUser;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.AdmTabs;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * List containing users that belong to a specific role.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 26, 2012
 * 
 */
public class RoleUserTable extends AbstractRelationListTable<User, ClientUser> {

	/** The icons of the application. */
	private final AdmIcons icons;

	/**
	 * Default constructor.
	 */
	public RoleUserTable() {
		setHeading("Users");

		icons = Registry.get(AdmModule.ICONS);

		setIcon(AbstractImagePrototype.create(icons.user()));

		initializeWidgets();
	}

	@Override
	protected List<ColumnConfig> createColumns() {
		final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

		ColumnConfig column = new ColumnConfig();
		column.setId("name");
		column.setHeader("Name");
		column.setWidth(100);
		column.setSortable(true);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("username");
		column.setHeader("Username");
		column.setWidth(100);
		column.setSortable(true);
		configs.add(column);

		return configs;
	}

	@Override
	protected Class<ClientUser> getBeanModelClass() {
		return ClientUser.class;
	}

	@Override
	protected String getPanelStateId() {
		return RoleUserTable.class.getName();
	}

	@Override
	protected void handleDoubleClick(final GridEvent<BeanModel> gridEvent) {
		final Grid<BeanModel> grid = gridEvent.getGrid();
		final GridSelectionModel<BeanModel> selectionModel = grid.getSelectionModel();
		final BeanModel selectedItem = selectionModel.getSelectedItem();

		if (selectedItem != null) {
			final ClientUser user = (ClientUser) selectedItem.getBean();

			final AdmNavigationController controller = Registry.get(AdmModule.NAVIGATION_CONTROLLER);

			controller.selectTab(AdmTabs.USER_DETAIL_TAB, user);
		}
	}

}
