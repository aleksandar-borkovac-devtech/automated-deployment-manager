package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.role;

import java.util.List;

import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.privilege.ClientPrivilege;
import nl.tranquilizedquality.adm.gwt.gui.client.model.role.ClientRole;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * {@link Window} where the available privileges are being displayed.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 26, 2012
 * 
 */
public class RolePrivilegeWindow extends Window {

	/** The icons of the application. */
	private final AdmIcons icons;

	/** The {@link ClientRole} where privileges will be added to. */
	private ClientRole role;

	/** The grid that initiated this window. */
	private final RolePrivilegesTable table;

	/** The panel that contains the privilege add functionality. */
	private final RolePrivilegePanel rolePrivilegePanel;

	/**
	 * Constructor taking the {@link ClientRole} that is being altered and the
	 * grid that initiated this window.
	 * 
	 * @param role
	 *            The {@link ClientRole} that will be used.
	 * @param table
	 *            The grid that will be used.
	 */
	public RolePrivilegeWindow(final ClientRole role, final RolePrivilegesTable table) {
		setLayout(new FitLayout());
		setSize(400, 300);

		this.table = table;
		this.role = role;
		this.icons = Registry.get(AdmModule.ICONS);

		setIcon(AbstractImagePrototype.create(icons.privilege()));

		final List<ClientPrivilege> privileges = this.table.getRolePrivileges();
		rolePrivilegePanel = new RolePrivilegePanel(this.role, privileges);
		rolePrivilegePanel.setWindow(this);

		add(rolePrivilegePanel);
	}

	@Override
	public void hide() {
		table.setModel(role.getPrivileges());

		final Viewport viewport = Registry.get(AdmModule.VIEW_PORT);
		viewport.unmask();

		super.hide();
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public void setRole(final ClientRole role) {
		this.role = role;

		this.rolePrivilegePanel.setRole(role);

		final List<ClientPrivilege> rolePrivileges = this.table.getRolePrivileges();
		this.rolePrivilegePanel.setFilterPrivileges(rolePrivileges);
	}

}
