package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.privilege;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.Role;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractRelationListTable;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.role.ClientRole;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.widget.grid.CheckColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Table that displays roles that use the parent privilege.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 26, 2012
 * 
 */
public class PrivilegeRoleTable extends AbstractRelationListTable<Role, ClientRole> {

	/** The icons of the application. */
	private final AdmIcons icons;

	/**
	 * Default constructor.
	 */
	public PrivilegeRoleTable() {
		setHeading("Roles");

		icons = Registry.get(AdmModule.ICONS);

		setIcon(AbstractImagePrototype.create(icons.role()));

		initializeWidgets();
	}

	@Override
	protected List<ColumnConfig> createColumns() {
		final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

		ColumnConfig column = new ColumnConfig();
		column.setId("name");
		column.setHeader("Name");
		column.setWidth(200);
		column.setSortable(true);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("scope");
		column.setHeader("Scope");
		column.setWidth(100);
		column.setSortable(true);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("description");
		column.setHeader("Description");
		column.setWidth(200);
		column.setSortable(true);
		configs.add(column);

		column = new CheckColumnConfig();
		column.setId("valid");
		column.setHeader("Valid");
		column.setWidth(50);
		column.setSortable(true);
		configs.add(column);

		return configs;
	}

	@Override
	protected Class<ClientRole> getBeanModelClass() {
		return ClientRole.class;
	}

	@Override
	protected String getPanelStateId() {
		return PrivilegeRoleTable.class.getName();
	}

}
