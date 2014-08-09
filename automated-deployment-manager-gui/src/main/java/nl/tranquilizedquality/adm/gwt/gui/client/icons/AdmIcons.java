package nl.tranquilizedquality.adm.gwt.gui.client.icons;

import nl.tranquilizedquality.adm.commons.gwt.ext.client.icons.ApplicationIcons;

import com.google.gwt.resources.client.ImageResource;

/**
 * AdmIcons used in the Automated deployment manager.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 */
public interface AdmIcons extends ApplicationIcons {

    @Source("application_form_view.png")
    ImageResource view();

    @Source("application_form_add.png")
    ImageResource add();

    @Source("application_form_delete.png")
    ImageResource delete();

    @Source("application_form_edit.png")
    ImageResource edit();

    @Source("application_put.png")
    ImageResource resubmit();

    @Source("application_go.png")
    ImageResource resubmitNow();

    @Source("funnel.png")
    ImageResource funnel();

    @Source("find.png")
    ImageResource find();

    @Source("dashboard.png")
    ImageResource dashboard();

    @Source("reset.png")
    ImageResource reset();

    @Source("excel.png")
    ImageResource excel();

    @Source("release-management.png")
    ImageResource releaseManagement();

    @Source("find-releases.png")
    ImageResource findReleases();

    @Source("add-release.png")
    ImageResource addReleases();

    @Source("logout.png")
    ImageResource logout();

    @Source("release-details.png")
    ImageResource releaseDetails();

    @Source("releases.png")
    ImageResource releases();

    @Source("release-history.png")
    ImageResource releaseHistory();

    @Source("artifacts.png")
    ImageResource artifacts();

    @Source("steps.png")
    ImageResource steps();

    @Source("destination-management.png")
    ImageResource destinationManagement();

    @Source("find-destinations.png")
    ImageResource findDestinations();

    @Source("destination.png")
    ImageResource destination();

    @Source("destination-location.png")
    ImageResource destinationLocation();

    @Source("artifact-management.png")
    ImageResource artifactManagement();

    @Source("find-artifacts.png")
    ImageResource findArtifacts();

    @Source("add-maven-module.png")
    ImageResource addMavenModule();

    @Source("repository-management.png")
    ImageResource repositoryManagement();

    @Source("find-repositories.png")
    ImageResource findRepositories();

    @Source("add-repository.png")
    ImageResource addRepository();

    @Source("deploy-artifacts.png")
    ImageResource releaseArtifacts();

    @Source("deploy-release.png")
    ImageResource deployRelease();

    @Source("add-artifact.png")
    ImageResource addArtifact();

    @Source("delete-artifact.png")
    ImageResource removeArtifact();

    @Source("find-hosts.png")
    ImageResource findHosts();

    @Source("host-management.png")
    ImageResource hostManagement();

    @Source("new-host.png")
    ImageResource addHost();

    @Source("new-environment.png")
    ImageResource addEnvironment();

    @Source("environment-management.png")
    ImageResource environmentManagement();

    @Source("find-environment.png")
    ImageResource findEnvironment();

    @Source("page_white_stack.png")
    ImageResource showLogs();

    @Source("group_link.png")
    ImageResource usersInGroup();

    @Source("group.png")
    ImageResource userGroup();

    @Source("group_add.png")
    ImageResource addUserGroup();

    @Source("group_edit.png")
    ImageResource editUserGroup();

    @Source("group_delete.png")
    ImageResource deleteUserGroup();

    @Source("group_gear.png")
    ImageResource userGroupManagement();

    @Source("group_go.png")
    ImageResource findUserGroups();

    @Source("member-management.png")
    ImageResource userManagement();

    @Source("find-members.png")
    ImageResource findMembers();

    @Source("add-user-icon.png")
    ImageResource addUser();

    @Source("user_business_boss.png")
    ImageResource scopeManager();

    @Source("user.png")
    ImageResource user();

    @Source("trophy.png")
    ImageResource privilege();

    @Source("zone.png")
    ImageResource scope();

    @Source("vcard.png")
    ImageResource userDetails();

    @Source("user_thief.png")
    ImageResource role();

    @Source("status_online.png")
    ImageResource grantableRoles();

    @Source("zones.png")
    ImageResource managedScopes();

    @Source("user_add.png")
    ImageResource addUserToGroup();

    @Source("user_delete.png")
    ImageResource removeUserFromGroup();

    @Source("package-icon.png")
    ImageResource archiveArtifact();

    @Source("package-green-icon.png")
    ImageResource unArchiveArtifact();

}
