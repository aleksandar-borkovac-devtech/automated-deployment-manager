package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.user.history;

import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.NavigationalItem;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUser;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

/**
 * Panel that displays the user history.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public class UserHistoryPanel extends LayoutContainer implements NavigationalItem<ClientUser> {

	/** The {@link User} where the history is shown from. */
	private ClientUser clientUser;

	/** The panel where the user roles are displayed in. */
	private final UserRoleHistoryPanel userRoleHistoryPanel;

	/**
	 * Default constructor.
	 */
	public UserHistoryPanel(final ClientUser clientUser) {
		setLayout(new FitLayout());

		this.clientUser = clientUser;

		userRoleHistoryPanel = new UserRoleHistoryPanel(this.clientUser);

		add(userRoleHistoryPanel);
	}

	@Override
	public void setModel(final ClientUser model) {
		this.clientUser = model;
		userRoleHistoryPanel.setClientUser(clientUser);
	}

}
