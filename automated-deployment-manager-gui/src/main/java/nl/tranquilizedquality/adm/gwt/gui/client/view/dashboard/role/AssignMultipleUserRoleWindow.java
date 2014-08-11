package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.role;

import java.util.List;

import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUser;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * The {@link Window} that holds the panel where the roles are presented.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 26, 2012
 * 
 */
public class AssignMultipleUserRoleWindow extends Window {

    /** The icons of the application. */
    private final AdmIcons icons;

    /** The {@link ClientUser} where roles will be added to. */
    private List<ClientUser> users;

    /** The panel that contains the user role add functionality. */
    private final AssignMultipleUserRolePanel boaamUserRolePanel;

    /**
     * Constructor taking the {@link ClientUser} where the roles will be added
     * to.
     * 
     * @param user
     *            The {@link ClientUser} where the roles will be added to.
     */
    public AssignMultipleUserRoleWindow(final List<ClientUser> users) {
        setLayout(new FitLayout());
        setSize(400, 500);

        this.users = users;
        this.icons = Registry.get(AdmModule.ICONS);

        setIcon(AbstractImagePrototype.create(icons.role()));

        boaamUserRolePanel = new AssignMultipleUserRolePanel(users);
        boaamUserRolePanel.setWindow(this);

        add(boaamUserRolePanel);
    }

    @Override
    public void hide() {
        final Viewport viewport = Registry.get(AdmModule.VIEW_PORT);
        viewport.unmask();

        super.hide();
    }

    /**
     * @param user
     *            the user to set
     */
    public void setUsers(final List<ClientUser> users) {
        this.users = users;
        boaamUserRolePanel.setUsers(this.users);
    }
}
