package nl.tranquilizedquality.adm.gwt.gui.client.view.navigation;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.gwt.ext.client.AbstractModule;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.navigation.TreeMenuItem;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractNavigationPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.constants.NavigationConstants;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.AdmTabs;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * The {@link ContentPanel} that will show the navigation menu.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 */
public class AdmNavigationPanel extends AbstractNavigationPanel<NavigationConstants, AdmIcons> {

    /** Icons used within the application. */
    private AdmIcons icons;

    /** Icon provider used to display icons in the menu. */
    private ModelIconProvider<ModelData> iconProvider;

    /**
     * Default constructor.
     */
    public AdmNavigationPanel() {
        super();

        icons = Registry.get(AbstractModule.ICONS);

        iconProvider = new ModelIconProvider<ModelData>() {

            @Override
            public AbstractImagePrototype getIcon(final ModelData model) {
                final String name = model.get("name");

                if ("Repository Management".equals(name)) {
                    return AbstractImagePrototype.create(icons.repositoryManagement());
                }
                else if ("Find repositories".equals(name)) {
                    return AbstractImagePrototype.create(icons.findRepositories());
                }
                else if ("Add new repository".equals(name)) {
                    return AbstractImagePrototype.create(icons.addRepository());
                }
                else if ("Host Management".equals(name)) {
                    return AbstractImagePrototype.create(icons.hostManagement());
                }
                else if ("Find hosts".equals(name)) {
                    return AbstractImagePrototype.create(icons.findHosts());
                }
                else if ("Add new host".equals(name)) {
                    return AbstractImagePrototype.create(icons.addHost());
                }
                else if ("Destination Management".equals(name)) {
                    return AbstractImagePrototype.create(icons.destinationManagement());
                }
                else if ("Find destinations".equals(name)) {
                    return AbstractImagePrototype.create(icons.findDestinations());
                }
                else if ("Add new destination".equals(name)) {
                    return AbstractImagePrototype.create(icons.destination());
                }
                else if ("Maven Module Management".equals(name)) {
                    return AbstractImagePrototype.create(icons.artifactManagement());
                }
                else if ("Find Maven Modules".equals(name)) {
                    return AbstractImagePrototype.create(icons.findArtifacts());
                }
                else if ("Add new module".equals(name)) {
                    return AbstractImagePrototype.create(icons.addMavenModule());
                }
                else if ("Artifact Management".equals(name)) {
                    return AbstractImagePrototype.create(icons.artifactManagement());
                }
                else if ("Find artifacts".equals(name)) {
                    return AbstractImagePrototype.create(icons.findArtifacts());
                }
                else if ("Release Management".equals(name)) {
                    return AbstractImagePrototype.create(icons.releaseManagement());
                }
                else if ("Find releases".equals(name)) {
                    return AbstractImagePrototype.create(icons.findReleases());
                }
                else if ("Add new release".equals(name)) {
                    return AbstractImagePrototype.create(icons.addReleases());
                }
                else if ("Add new environment".equals(name)) {
                    return AbstractImagePrototype.create(icons.addEnvironment());
                }
                else if ("Environment Management".equals(name)) {
                    return AbstractImagePrototype.create(icons.environmentManagement());
                }
                else if ("Find environments".equals(name)) {
                    return AbstractImagePrototype.create(icons.findEnvironment());
                }
                else if ("User Group Management".equals(name)) {
                    return AbstractImagePrototype.create(icons.userGroupManagement());
                }
                else if ("Find user groups".equals(name)) {
                    return AbstractImagePrototype.create(icons.findUserGroups());
                }
                else if ("Add new user group".equals(name)) {
                    return AbstractImagePrototype.create(icons.addUserGroup());
                }
                else if ("User Management".equals(name)) {
                    return AbstractImagePrototype.create(icons.userManagement());
                }
                else if ("Add new user".equals(name)) {
                    return AbstractImagePrototype.create(icons.addUser());
                }
                else if ("Find users".equals(name)) {
                    return AbstractImagePrototype.create(icons.findMembers());
                }
                else if ("Scope Management".equals(name)) {
                    return AbstractImagePrototype.create(icons.scope());
                }
                else if ("Find scopes".equals(name)) {
                    return AbstractImagePrototype.create(icons.managedScopes());
                }
                else if ("Personal Details".equals(name)) {
                    return AbstractImagePrototype.create(icons.userDetails());
                }
                else if ("Logout".equals(name)) {
                    return AbstractImagePrototype.create(icons.logout());
                }

                return null;
            }

        };

        tree.setIconProvider(iconProvider);
    }

    @Override
    public TreeMenuItem getLogoutTreeMenuItem() {
        /*
         * Create the menu item.
         */
        final TreeMenuItem logoutItem = new TreeMenuItem("Logout", true);

        return logoutItem;
    }

    /**
     * Checks if the logged in user has the specified privilege.
     * 
     * @param privilege
     *            The privilege that will be checked.
     * @return Returns true if the user is authorized otherwise it will return
     *         false.
     */
    private boolean isUserAuthorized(final String privilege) {
        final List<String> privileges = Registry.get(AdmModule.USER_PRIVILEGES);
        for (final String grantedPrivilege : privileges) {
            if (grantedPrivilege.equals(privilege)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<TreeMenuItem> getRootTreeMenuItems() {

        /*
         * Add the items to the store.
         */
        final List<TreeMenuItem> items = new ArrayList<TreeMenuItem>();

        addUserManagementItem(items);
        addUserGroupItem(items);
        addRepositoryManagementItem(items);
        addHostManagementItem(items);
        addEnvironmentManagementItem(items);
        addDestinationManagementItem(items);
        addMavenModuleManagementItem(items);
        addArtifactManagementItem(items);
        addReleaseManagementItem(items);
        addPersonalDetailsItem(items);

        return items;
    }

    /**
     * Add the personal details menu item.
     * 
     * @param items
     *            The item list to add the menu item to so it will be displayed.
     */
    private void addPersonalDetailsItem(final List<TreeMenuItem> items) {
        final TreeMenuItem personalDetailsMenuItem = createMenuItem("LOGIN", "Personal Details", AdmTabs.PERSONAL_DETAIL_TAB);
        items.add(personalDetailsMenuItem);
    }

    /**
     * Add the release management menu items.
     * 
     * @param items
     *            The item list to add the menu item to so it will be displayed.
     */
    private void addReleaseManagementItem(final List<TreeMenuItem> items) {
        final TreeMenuItem findReleaseMenuItem = createMenuItem("SEARCH_RELEASE", "Find releases", AdmTabs.RELEASE_MANAGEMENT_TAB);
        final TreeMenuItem addNewReleaseMenuItem = createMenuItem("ADD_RELEASE", "Add new release", AdmTabs.RELEASE_DETAILS_TAB);

        if (findReleaseMenuItem != null || addNewReleaseMenuItem != null) {
            final TreeMenuItem userManagementItem = new TreeMenuItem("Release Management", AdmTabs.RELEASE_MANAGEMENT_TAB,
                    new BaseTreeModel[] {findReleaseMenuItem });
            items.add(userManagementItem);
        }
    }

    /**
     * Add the artifact management menu items.
     * 
     * @param items
     *            The item list to add the menu item to so it will be displayed.
     */
    private void addArtifactManagementItem(final List<TreeMenuItem> items) {
        final TreeMenuItem findMavenModuleMenuItem = createMenuItem("SEARCH_ARTIFACT", "Find artifacts",
                AdmTabs.ARTIFACT_MANAGEMENT_TAB);

        if (findMavenModuleMenuItem != null) {
            final TreeMenuItem userManagementItem = new TreeMenuItem("Artifact Management", AdmTabs.ARTIFACT_MANAGEMENT_TAB,
                    new BaseTreeModel[] {findMavenModuleMenuItem });
            items.add(userManagementItem);
        }
    }

    /**
     * Add the maven module management menu items.
     * 
     * @param items
     *            The item list to add the menu item to so it will be displayed.
     */
    private void addMavenModuleManagementItem(final List<TreeMenuItem> items) {
        final TreeMenuItem findMavenModuleMenuItem = createMenuItem("SEARCH_MAVEN_MODULE", "Find Maven Modules",
                AdmTabs.MAVEN_MODULE_MANAGEMENT_TAB);
        final TreeMenuItem addNewMavenModuleMenuItem = createMenuItem("ADD_MAVEN_MODULE", "Add new module",
                AdmTabs.MAVEN_MODULE_DETAILS_TAB);

        if (findMavenModuleMenuItem != null || addNewMavenModuleMenuItem != null) {
            final TreeMenuItem userManagementItem = new TreeMenuItem("Maven Module Management",
                    AdmTabs.MAVEN_MODULE_MANAGEMENT_TAB, new BaseTreeModel[] {findMavenModuleMenuItem,
                            addNewMavenModuleMenuItem });
            items.add(userManagementItem);
        }
    }

    /**
     * Add the destination management menu items.
     * 
     * @param items
     *            The item list to add the menu item to so it will be displayed.
     */
    private void addDestinationManagementItem(final List<TreeMenuItem> items) {
        final TreeMenuItem findDestinationMenuItem = createMenuItem("SEARCH_DESTINATION", "Find destinations",
                AdmTabs.DESTINATION_MANAGEMENT_TAB);
        final TreeMenuItem addNewDestinationMenuItem = createMenuItem("ADD_DESTINATION", "Add new destination",
                AdmTabs.DESTINATION_DETAILS_TAB);

        if (findDestinationMenuItem != null || addNewDestinationMenuItem != null) {
            final TreeMenuItem userManagementItem = new TreeMenuItem("Destination Management", AdmTabs.DESTINATION_MANAGEMENT_TAB,
                    new BaseTreeModel[] {findDestinationMenuItem,
                            addNewDestinationMenuItem });
            items.add(userManagementItem);
        }
    }

    /**
     * Add the environment management menu items.
     * 
     * @param items
     *            The item list to add the menu item to so it will be displayed.
     */
    private void addEnvironmentManagementItem(final List<TreeMenuItem> items) {
        final TreeMenuItem findEnvironmentMenuItem = createMenuItem("SEARCH_ENVIRONMENT", "Find environments",
                AdmTabs.ENVIRONMENT_MANAGEMENT_TAB);
        final TreeMenuItem addNewEnvironmentMenuItem = createMenuItem("ADD_ENVIRONMENT", "Add new environment",
                AdmTabs.ENVIRONMENT_DETAILS_TAB);

        if (findEnvironmentMenuItem != null || addNewEnvironmentMenuItem != null) {
            final TreeMenuItem userManagementItem = new TreeMenuItem("Environment Management", AdmTabs.ENVIRONMENT_MANAGEMENT_TAB,
                    new BaseTreeModel[] {findEnvironmentMenuItem,
                            addNewEnvironmentMenuItem });
            items.add(userManagementItem);
        }
    }

    /**
     * Add the host management menu items.
     * 
     * @param items
     *            The item list to add the menu item to so it will be displayed.
     */
    private void addHostManagementItem(final List<TreeMenuItem> items) {
        final TreeMenuItem findHostMenuItem = createMenuItem("SEARCH_HOST", "Find hosts", AdmTabs.DESTINATION_HOST_MANAGEMENT_TAB);
        final TreeMenuItem addNewHostMenuItem = createMenuItem("ADD_HOST", "Add new host", AdmTabs.DESTINATION_HOSTS_DETAILS_TAB);

        if (findHostMenuItem != null || addNewHostMenuItem != null) {
            final TreeMenuItem userManagementItem = new TreeMenuItem("Host Management", AdmTabs.DESTINATION_HOST_MANAGEMENT_TAB,
                    new BaseTreeModel[] {findHostMenuItem, addNewHostMenuItem });
            items.add(userManagementItem);
        }
    }

    /**
     * Add the repository management menu items.
     * 
     * @param items
     *            The item list to add the menu item to so it will be displayed.
     */
    private void addRepositoryManagementItem(final List<TreeMenuItem> items) {
        final TreeMenuItem findRepositoryMenuItem = createMenuItem("SEARCH_REPOSITORY", "Find repositories",
                AdmTabs.REPOSITORY_MANAGEMENT_TAB);
        final TreeMenuItem addNewRepositoryMenuItem = createMenuItem("ADD_REPOSITORY", "Add new repository",
                AdmTabs.REPOSITORY_DETAILS_TAB);

        if (findRepositoryMenuItem != null || addNewRepositoryMenuItem != null) {
            final TreeMenuItem userManagementItem = new TreeMenuItem("Repository Management", AdmTabs.REPOSITORY_MANAGEMENT_TAB,
                    new BaseTreeModel[] {findRepositoryMenuItem, addNewRepositoryMenuItem });
            items.add(userManagementItem);
        }
    }

    /**
     * Add the user management menu items.
     * 
     * @param items
     *            The item list to add the menu item to so it will be displayed.
     */
    private void addUserManagementItem(final List<TreeMenuItem> items) {
        final TreeMenuItem findUsersMenuItem = createMenuItem("SEARCH_USER", "Find users", AdmTabs.USER_MANAGEMENT_TAB);
        final TreeMenuItem addNewUserMenuItem = createMenuItem("SEARCH_USER", "Add new user", AdmTabs.USER_DETAIL_TAB);

        if (findUsersMenuItem != null || addNewUserMenuItem != null) {
            final TreeMenuItem userManagementItem = new TreeMenuItem("User Management", AdmTabs.USER_MANAGEMENT_TAB,
                    new BaseTreeModel[] {findUsersMenuItem, addNewUserMenuItem });
            items.add(userManagementItem);
        }

    }

    /**
     * Add the user group management menu items.
     * 
     * @param items
     *            The item list to add the menu item to so it will be displayed.
     */
    private void addUserGroupItem(final List<TreeMenuItem> items) {
        final TreeMenuItem findUserGroupsMenuItem = createMenuItem("SEARCH_USER_GROUP", "Find user groups",
                AdmTabs.USER_GROUP_MANAGEMENT_TAB);
        final TreeMenuItem addNewUserGroupMenuItem = createMenuItem("ADD_USER_GROUP", "Add new user group",
                AdmTabs.USER_GROUP_DETAILS_TAB);

        if (findUserGroupsMenuItem != null || addNewUserGroupMenuItem != null) {
            final TreeMenuItem userManagementItem = new TreeMenuItem("User Group Management", AdmTabs.USER_GROUP_MANAGEMENT_TAB,
                    new BaseTreeModel[] {findUserGroupsMenuItem, addNewUserGroupMenuItem });
            items.add(userManagementItem);
        }
    }

    /**
     * Creates a menu item if the logged in user is authorized to access that
     * part of the application.
     * 
     * @param privilege
     *            The privilege that is need to use that part of the system.
     * @param itemText
     *            The text to display on the menu item.
     * @param tab
     *            The tab to use for navigation within the application.
     * @return Returns the {@link TreeMenuItem} if the logged in user is allowed
     *         to access that part of the system otherwise it will return null.
     */
    private TreeMenuItem createMenuItem(final String privilege, final String itemText, final AdmTabs tab) {
        TreeMenuItem menuItem = null;
        final boolean authorized = isUserAuthorized(privilege);
        if (authorized) {
            menuItem = new TreeMenuItem(itemText, tab);
        }
        return menuItem;
    }

}
