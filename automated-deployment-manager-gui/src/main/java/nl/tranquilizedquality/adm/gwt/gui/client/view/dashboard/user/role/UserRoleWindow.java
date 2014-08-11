/*
 * @(#)UserRoleWindow.java 28 okt 2009 Copyright (c) 2009 Tranquilized Quality All rights
 * reserved. Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.user.role;

import java.util.List;

import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.scope.ClientScope;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUser;
import nl.tranquilizedquality.adm.gwt.gui.client.service.user.UserServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.user.UserDetailPanel;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * The {@link Window} that holds the panel where the roles are presented.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 26, 2012
 * 
 */
public class UserRoleWindow extends Window {

    /** The icons of the application. */
    private final AdmIcons icons;

    /** The {@link ClientUser} where roles will be added to. */
    private ClientUser user;

    /** The grid that initiated this window. */
    private final UserRolesTable table;

    /** The panel that contains the user role add functionality. */
    private final UserRolePanel userRolePanel;

    /**
     * List of scopes the user is managing containing all the roles he/she has
     * for the specific scope.
     */
    private List<ClientScope> managedScopes;

    /** The main panel where the user details are displayed on. */
    private final UserDetailPanel userDetailPanel;

    /**
     * Constructor taking the {@link ClientUser} where the roles will be added
     * to.
     * 
     * @param user
     *            The {@link ClientUser} where the roles will be added to.
     * @param table
     *            The {@link UserRolesTable} where the roles of the users are
     *            displayed in.
     */
    public UserRoleWindow(final ClientUser user, final UserRolesTable table, final UserDetailPanel userDetailPanel) {
        setLayout(new FitLayout());
        setSize(400, 500);

        this.user = user;
        this.table = table;
        this.userDetailPanel = userDetailPanel;
        this.icons = Registry.get(AdmModule.ICONS);

        setIcon(AbstractImagePrototype.create(icons.role()));

        final List<ClientScope> managedScopes = this.table.getManagedScopes();
        userRolePanel = new UserRolePanel(user, managedScopes);
        userRolePanel.setWindow(this);

        add(userRolePanel);
    }

    @Override
    public void hide() {
        if (this.user != null) {

            final AsyncCallback<ClientUser> callback = new AsyncCallback<ClientUser>() {

                @Override
                public void onFailure(final Throwable throwable) {
                    final MessageBox box = new MessageBox();
                    box.setIcon(MessageBox.ERROR);
                    box.setTitle("Retrieve user.");
                    box.setMessage(throwable.getMessage());
                    box.setButtons(MessageBox.OK);
                    box.show();
                }

                @Override
                public void onSuccess(final ClientUser user) {
                    UserRoleWindow.this.user = user;

                    UserRoleWindow.this.userDetailPanel.setModel(UserRoleWindow.this.user);
                }

            };

            final UserServiceAsync userService = Registry.get(AdmModule.USER_SERVICE);
            userService.findUserById(UserRoleWindow.this.user.getId(), callback);
        }

        final Viewport viewport = Registry.get(AdmModule.VIEW_PORT);
        viewport.unmask();

        super.hide();
    }

    /**
     * @param user
     *            the user to set
     */
    public void setUser(final ClientUser user) {
        this.user = user;
        userRolePanel.setClientUser(user);

        /*
         * Retrieve the manages scopes so it can be used as filter in the grid
         * view.
         */
        managedScopes = this.table.getManagedScopes();
        userRolePanel.setFilterScopes(managedScopes);
    }
}
