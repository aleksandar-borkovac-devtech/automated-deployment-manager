package nl.tranquilizedquality.adm.gwt.gui.client.controller.navigation;

import nl.tranquilizedquality.adm.commons.gwt.ext.client.controller.navigation.AbstractNavigationController;
import nl.tranquilizedquality.adm.gwt.gui.client.constants.NavigationConstants;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.artifact.ClientMavenArtifact;
import nl.tranquilizedquality.adm.gwt.gui.client.model.artifact.ClientMavenModule;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientDestination;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientDestinationHost;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientEnvironment;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientRelease;
import nl.tranquilizedquality.adm.gwt.gui.client.model.repository.ClientRepository;
import nl.tranquilizedquality.adm.gwt.gui.client.model.role.ClientRole;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUser;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUserGroup;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.AdmTabs;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.artifact.ArtifactDetailsPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.artifact.ArtifactManagementPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.environment.DestinationDetailsPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.environment.DestinationManagementPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.environment.EnvironmentDetailsPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.environment.EnvironmentManagementPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.host.DestinationHostDetailsPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.host.DestinationHostManagementPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.module.MavenModuleDetailsPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.module.MavenModuleManagementPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.personal.PersonalDetailPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.privilege.PrivilegeDetailPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.release.ReleaseDetailsPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.release.ReleaseManagementPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.release.history.ReleaseExecutionLogDetailsPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.release.history.ReleaseHistoryDetailsPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.repository.RepositoryDetailsPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.repository.RepositoryManagementPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.role.RoleDetailPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.scope.ScopeDetailPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.scope.ScopeManagementPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.user.UserDetailPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.user.UserManagementPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.user.history.UserHistoryPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.usergroup.UserGroupDetailsPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.usergroup.UserGroupManagementPanel;

import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Controller that is responsible for the application navigation.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 */
public class AdmNavigationController extends AbstractNavigationController<NavigationConstants, AdmIcons, AdmTabs> {

    @Override
    protected TabItem createNewTab(final AdmTabs tab) {
        TabItem tabItem = null;

        switch (tab) {
            case ROLE_DETAIL_TAB:
                tabItem = new TabItem("Role Details");
                tabItem.setId(tab.toString());
                tabItem.setClosable(true);
                tabItem.setLayout(new FitLayout());
                tabItem.setIcon(AbstractImagePrototype.create(icons.role()));

                final RoleDetailPanel roleDetailPanel = new RoleDetailPanel();
                tabItem.add(roleDetailPanel);
                roleDetailPanel.setModel(new ClientRole());
                break;

            case SCOPE_MANAGEMENT_TAB:
                tabItem = new TabItem("Scope Management");
                tabItem.setId(tab.toString());
                tabItem.setClosable(true);
                tabItem.setLayout(new FitLayout());
                tabItem.setIcon(AbstractImagePrototype.create(icons.scope()));

                final ScopeManagementPanel scopeManagementPanel = new ScopeManagementPanel();
                tabItem.add(scopeManagementPanel);
                break;

            case SCOPE_DETAIL_TAB:
                tabItem = new TabItem("Scope Details");
                tabItem.setId(tab.toString());
                tabItem.setClosable(true);
                tabItem.setLayout(new FitLayout());
                tabItem.setIcon(AbstractImagePrototype.create(icons.scope()));

                final ScopeDetailPanel scopeDetailPanel = new ScopeDetailPanel();
                tabItem.add(scopeDetailPanel);
                break;

            case USER_MANAGEMENT_TAB:
                tabItem = new TabItem("User Management");
                tabItem.setId(tab.toString());
                tabItem.setClosable(true);
                tabItem.setLayout(new FitLayout());
                tabItem.setIcon(AbstractImagePrototype.create(icons.userManagement()));

                tabItem.add(new UserManagementPanel());
                break;

            case USER_DETAIL_TAB:
                tabItem = new TabItem("User Details");
                tabItem.setId(tab.toString());
                tabItem.setClosable(true);
                tabItem.setLayout(new FitLayout());
                tabItem.setIcon(AbstractImagePrototype.create(icons.userDetails()));

                final UserDetailPanel userDetailPanel = new UserDetailPanel();
                userDetailPanel.setModel(new ClientUser());

                tabItem.add(userDetailPanel);
                break;

            case USER_HISTORY_TAB:
                tabItem = new TabItem("User History");
                tabItem.setId(tab.toString());
                tabItem.setClosable(true);
                tabItem.setLayout(new FitLayout());
                tabItem.setIcon(AbstractImagePrototype.create(icons.userDetails()));

                final ClientUser clientUser = new ClientUser();

                final UserHistoryPanel userHistoryPanel = new UserHistoryPanel(clientUser);
                tabItem.add(userHistoryPanel);

                userHistoryPanel.setModel(clientUser);
                break;

            case PRIVILEGE_DETAIL_TAB:
                tabItem = new TabItem("Privilege Details");
                tabItem.setId(tab.toString());
                tabItem.setClosable(true);
                tabItem.setLayout(new FitLayout());
                tabItem.setIcon(AbstractImagePrototype.create(icons.privilege()));

                final PrivilegeDetailPanel privilegeDetailPanel = new PrivilegeDetailPanel();
                tabItem.add(privilegeDetailPanel);
                break;

            case REPOSITORY_MANAGEMENT_TAB:
                tabItem = new TabItem("Repository Management");
                tabItem.setId(tab.toString());
                tabItem.setClosable(true);
                tabItem.setLayout(new FitLayout());
                tabItem.setIcon(AbstractImagePrototype.create(icons.repositoryManagement()));

                final RepositoryManagementPanel repositoryManagementPanel = new RepositoryManagementPanel();
                tabItem.add(repositoryManagementPanel);
                break;

            case REPOSITORY_DETAILS_TAB:
                tabItem = new TabItem("Repository Details");
                tabItem.setId(tab.toString());
                tabItem.setClosable(true);
                tabItem.setLayout(new FitLayout());
                tabItem.setIcon(AbstractImagePrototype.create(icons.addRepository()));

                final RepositoryDetailsPanel repositoryDetailsPanel = new RepositoryDetailsPanel();
                tabItem.add(repositoryDetailsPanel);

                repositoryDetailsPanel.setModel(new ClientRepository());
                break;

            case MAVEN_MODULE_MANAGEMENT_TAB:
                tabItem = new TabItem("Maven Module Management");
                tabItem.setId(tab.toString());
                tabItem.setClosable(true);
                tabItem.setLayout(new FitLayout());
                tabItem.setIcon(AbstractImagePrototype.create(icons.artifactManagement()));

                final MavenModuleManagementPanel moduleManagementPanel = new MavenModuleManagementPanel();
                tabItem.add(moduleManagementPanel);
                break;

            case MAVEN_MODULE_DETAILS_TAB:
                tabItem = new TabItem("Maven Module Details");
                tabItem.setId(tab.toString());
                tabItem.setClosable(true);
                tabItem.setLayout(new FitLayout());
                tabItem.setIcon(AbstractImagePrototype.create(icons.artifacts()));

                final MavenModuleDetailsPanel moduleDetailsPanel = new MavenModuleDetailsPanel();
                tabItem.add(moduleDetailsPanel);

                moduleDetailsPanel.setModel(new ClientMavenModule());
                break;

            case ARTIFACT_DETAILS_TAB:
                tabItem = new TabItem("Artifact Details");
                tabItem.setId(tab.toString());
                tabItem.setClosable(true);
                tabItem.setLayout(new FitLayout());
                tabItem.setIcon(AbstractImagePrototype.create(icons.addArtifact()));

                final ArtifactDetailsPanel artifactDetailsPanel = new ArtifactDetailsPanel();
                tabItem.add(artifactDetailsPanel);

                artifactDetailsPanel.setModel(new ClientMavenArtifact());
                break;

            case ARTIFACT_MANAGEMENT_TAB:
                tabItem = new TabItem("Artifact Management");
                tabItem.setId(tab.toString());
                tabItem.setClosable(true);
                tabItem.setLayout(new FitLayout());
                tabItem.setIcon(AbstractImagePrototype.create(icons.artifactManagement()));

                final ArtifactManagementPanel artifactManager = new ArtifactManagementPanel();
                tabItem.add(artifactManager);
                break;

            case RELEASE_MANAGEMENT_TAB:
                tabItem = new TabItem("Release Management");
                tabItem.setId(tab.toString());
                tabItem.setClosable(true);
                tabItem.setLayout(new FitLayout());
                tabItem.setIcon(AbstractImagePrototype.create(icons.releaseManagement()));

                final ReleaseManagementPanel releaseManagementPanel = new ReleaseManagementPanel();
                tabItem.add(releaseManagementPanel);
                break;

            case RELEASE_DETAILS_TAB:
                tabItem = new TabItem("Release Details");
                tabItem.setId(tab.toString());
                tabItem.setClosable(true);
                tabItem.setLayout(new FitLayout());
                tabItem.setIcon(AbstractImagePrototype.create(icons.releaseDetails()));

                final ReleaseDetailsPanel releaseDetailsPanel = new ReleaseDetailsPanel();
                tabItem.add(releaseDetailsPanel);

                releaseDetailsPanel.setModel(new ClientRelease());
                break;

            case DESTINATION_DETAILS_TAB:
                tabItem = new TabItem("Destination Details");
                tabItem.setId(tab.toString());
                tabItem.setClosable(true);
                tabItem.setLayout(new FitLayout());
                tabItem.setIcon(AbstractImagePrototype.create(icons.destination()));

                final DestinationDetailsPanel destinationDetailsPanel = new DestinationDetailsPanel();
                tabItem.add(destinationDetailsPanel);

                destinationDetailsPanel.setModel(new ClientDestination());
                break;

            case DESTINATION_MANAGEMENT_TAB:
                tabItem = new TabItem("Environment Management");
                tabItem.setId(tab.toString());
                tabItem.setClosable(true);
                tabItem.setLayout(new FitLayout());
                tabItem.setIcon(AbstractImagePrototype.create(icons.destinationManagement()));

                final DestinationManagementPanel destinationManagementPanel = new DestinationManagementPanel();
                tabItem.add(destinationManagementPanel);
                break;

            case DESTINATION_HOSTS_DETAILS_TAB:
                tabItem = new TabItem("Host Details");
                tabItem.setId(tab.toString());
                tabItem.setClosable(true);
                tabItem.setLayout(new FitLayout());
                tabItem.setIcon(AbstractImagePrototype.create(icons.addHost()));

                final DestinationHostDetailsPanel destinationHostDetailsPanel = new DestinationHostDetailsPanel();
                tabItem.add(destinationHostDetailsPanel);

                destinationHostDetailsPanel.setModel(new ClientDestinationHost());
                break;

            case DESTINATION_HOST_MANAGEMENT_TAB:
                tabItem = new TabItem("Host Management");
                tabItem.setId(tab.toString());
                tabItem.setClosable(true);
                tabItem.setLayout(new FitLayout());
                tabItem.setIcon(AbstractImagePrototype.create(icons.hostManagement()));

                final DestinationHostManagementPanel destinationHostManagementPanel = new DestinationHostManagementPanel();
                tabItem.add(destinationHostManagementPanel);
                break;

            case RELEASE_HISTORY_TAB:
                tabItem = new TabItem("Release History");
                tabItem.setId(tab.toString());
                tabItem.setClosable(true);
                tabItem.setLayout(new FitLayout());
                tabItem.setIcon(AbstractImagePrototype.create(icons.releaseHistory()));

                final ReleaseHistoryDetailsPanel releaseHistoryDetailsPanel = new ReleaseHistoryDetailsPanel();
                tabItem.add(releaseHistoryDetailsPanel);
                break;

            case ENVIRONMENT_DETAILS_TAB:
                tabItem = new TabItem("Environment Details");
                tabItem.setId(tab.toString());
                tabItem.setClosable(true);
                tabItem.setLayout(new FitLayout());
                tabItem.setIcon(AbstractImagePrototype.create(icons.addEnvironment()));

                final EnvironmentDetailsPanel environmentDetailsPanel = new EnvironmentDetailsPanel();
                tabItem.add(environmentDetailsPanel);
                break;

            case ENVIRONMENT_MANAGEMENT_TAB:
                tabItem = new TabItem("Environment Management");
                tabItem.setId(tab.toString());
                tabItem.setClosable(true);
                tabItem.setLayout(new FitLayout());
                tabItem.setIcon(AbstractImagePrototype.create(icons.environmentManagement()));

                final EnvironmentManagementPanel environmentManagementPanel = new EnvironmentManagementPanel();
                tabItem.add(environmentManagementPanel);
                break;

            case RELEASE_EXECUTION_LOG_DETAILS_TAB:
                tabItem = new TabItem("Release Execution Logs");
                tabItem.setId(tab.toString());
                tabItem.setClosable(true);
                tabItem.setLayout(new FitLayout());
                tabItem.setIcon(AbstractImagePrototype.create(icons.releaseHistory()));

                final ReleaseExecutionLogDetailsPanel releaseExecutionLogDetailsPanel = new ReleaseExecutionLogDetailsPanel();
                tabItem.add(releaseExecutionLogDetailsPanel);
                break;

            case USER_GROUP_DETAILS_TAB:
                tabItem = new TabItem("User Group");
                tabItem.setId(tab.toString());
                tabItem.setClosable(true);
                tabItem.setLayout(new FitLayout());
                tabItem.setIcon(AbstractImagePrototype.create(icons.userGroup()));

                final UserGroupDetailsPanel userGroupDetailPanel = new UserGroupDetailsPanel();
                tabItem.add(userGroupDetailPanel);
                break;

            case USER_GROUP_MANAGEMENT_TAB:
                tabItem = new TabItem("User Group Management");
                tabItem.setId(tab.toString());
                tabItem.setClosable(true);
                tabItem.setLayout(new FitLayout());
                tabItem.setIcon(AbstractImagePrototype.create(icons.userGroupManagement()));

                final UserGroupManagementPanel userGroupManagementPanel = new UserGroupManagementPanel();
                tabItem.add(userGroupManagementPanel);
                break;

            case PERSONAL_DETAIL_TAB:
                tabItem = new TabItem("Personal Details");
                tabItem.setId(tab.toString());
                tabItem.setClosable(true);
                tabItem.setLayout(new FitLayout());
                tabItem.setIcon(AbstractImagePrototype.create(icons.userDetails()));

                final PersonalDetailPanel personalDetailPanel = new PersonalDetailPanel();
                personalDetailPanel.setModel(new ClientUser());

                tabItem.add(personalDetailPanel);
                break;

            default:
                break;

        }

        return tabItem;
    }

    @Override
    protected void createNewModelOnTab(final AdmTabs tab, final TabItem tabItem) {

        switch (tab) {
            case ROLE_DETAIL_TAB: {
                final Component itemByItemId = tabItem.getItemByItemId(RoleDetailPanel.class.getName());
                final RoleDetailPanel roleDetailPanel = (RoleDetailPanel) itemByItemId;
                roleDetailPanel.setModel(new ClientRole());
                break;
            }

            case USER_DETAIL_TAB: {
                final Component itemByItemId = tabItem.getItemByItemId(UserDetailPanel.class.getName());
                final UserDetailPanel userDetailPanel = (UserDetailPanel) itemByItemId;
                userDetailPanel.setModel(new ClientUser());
                break;
            }

            case REPOSITORY_DETAILS_TAB:
                final RepositoryDetailsPanel repositoryDetailsPanel = (RepositoryDetailsPanel) tabItem.getWidget(0);
                repositoryDetailsPanel.setModel(new ClientRepository());
                break;

            case DESTINATION_DETAILS_TAB:
                final DestinationDetailsPanel destinationDetailsPanel = (DestinationDetailsPanel) tabItem.getWidget(0);
                destinationDetailsPanel.setModel(new ClientDestination());
                break;

            case DESTINATION_HOSTS_DETAILS_TAB:
                final DestinationHostDetailsPanel destinationHostDetailsPanel = (DestinationHostDetailsPanel) tabItem.getWidget(0);
                destinationHostDetailsPanel.setModel(new ClientDestinationHost());
                break;

            case MAVEN_MODULE_DETAILS_TAB:
                final MavenModuleDetailsPanel mavenModuleDetailsPanel = (MavenModuleDetailsPanel) tabItem.getWidget(0);
                mavenModuleDetailsPanel.setModel(new ClientMavenModule());
                break;

            case RELEASE_DETAILS_TAB:
                final ReleaseDetailsPanel releaseDetailsPanel = (ReleaseDetailsPanel) tabItem.getWidget(0);
                releaseDetailsPanel.setModel(new ClientRelease());
                break;

            case ENVIRONMENT_DETAILS_TAB:
                final EnvironmentDetailsPanel environmentDetailsPanel = (EnvironmentDetailsPanel) tabItem.getWidget(0);
                environmentDetailsPanel.setModel(new ClientEnvironment());
                break;

            case USER_GROUP_DETAILS_TAB:
                final UserGroupDetailsPanel userGroupDetailsPanel = (UserGroupDetailsPanel) tabItem.getWidget(0);
                userGroupDetailsPanel.setModel(new ClientUserGroup());
                break;

        }

    }

}
