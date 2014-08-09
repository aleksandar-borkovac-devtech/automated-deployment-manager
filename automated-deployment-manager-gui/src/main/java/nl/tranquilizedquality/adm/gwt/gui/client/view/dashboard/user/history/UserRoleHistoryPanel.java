package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.user.history;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractGridPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.model.role.ClientUserRole;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUser;
import nl.tranquilizedquality.adm.gwt.gui.client.service.user.UserServiceAsync;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.widget.grid.CheckColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Implementation of an {@link AbstractGridPanel} where all roles a user has or
 * had are being presented in a {@link Grid}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public class UserRoleHistoryPanel extends AbstractGridPanel {

	/** The model object. */
	private ClientUser clientUser;

	/** The asynchronous user service. */
	private final UserServiceAsync userService;

	/**
	 * Constructor that takes the {@link ClientUser} where the roles are being
	 * displayed for.
	 * 
	 * @param clientUser
	 *            The {@link ClientUser} where the roles are being displayed
	 *            for.
	 */
	public UserRoleHistoryPanel(final ClientUser clientUser) {
		setLayout(new FitLayout());
		setHeading("Roles");

		this.clientUser = clientUser;

		this.userService = Registry.get(AdmModule.USER_SERVICE);

		this.proxy = new RpcProxy<PagingLoadResult<ClientUserRole>>() {

			@Override
			public void load(final Object loadConfig, final AsyncCallback<PagingLoadResult<ClientUserRole>> callback) {
				if (UserRoleHistoryPanel.this.clientUser != null && UserRoleHistoryPanel.this.clientUser.isPersistent()) {
					userService.findUserRolesByUser(UserRoleHistoryPanel.this.clientUser, callback);
				}
			}
		};

		initializeWidgets();
	}

	@Override
	protected List<ColumnConfig> createColumns() {
		final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

		ColumnConfig column = new ColumnConfig();
		column.setId("role");
		column.setHeader("Name");
		column.setWidth(100);
		column.setSortable(true);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("scopeName");
		column.setHeader("Scope");
		column.setWidth(100);
		column.setSortable(false);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("activeFrom");
		column.setHeader("Active from");
		column.setWidth(200);
		column.setSortable(false);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("activeUntil");
		column.setHeader("Active until");
		column.setWidth(200);
		column.setSortable(false);
		configs.add(column);

		column = new CheckColumnConfig();
		column.setId("active");
		column.setHeader("Active");
		column.setWidth(200);
		column.setSortable(false);
		configs.add(column);

		return configs;
	}

	/**
	 * @param clientUser
	 *            the clientUser to set
	 */
	public void setClientUser(final ClientUser clientUser) {
		this.clientUser = clientUser;
		refresh();
	}

}
