package nl.tranquilizedquality.adm.gwt.gui.client;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

import nl.tranquilizedquality.adm.commons.gwt.ext.client.AbstractModule;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.ApplicationService;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.ApplicationServiceAsync;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.SessionService;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.SessionServiceAsync;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.security.AuthorizationService;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.security.AuthorizationServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.controller.navigation.AdmNavigationController;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.service.artifact.ArtifactService;
import nl.tranquilizedquality.adm.gwt.gui.client.service.artifact.ArtifactServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.service.deployment.DeploymentService;
import nl.tranquilizedquality.adm.gwt.gui.client.service.deployment.DeploymentServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.service.environment.EnvironmentService;
import nl.tranquilizedquality.adm.gwt.gui.client.service.environment.EnvironmentServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.service.logtail.LogTailService;
import nl.tranquilizedquality.adm.gwt.gui.client.service.logtail.LogTailServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.service.privilege.PrivilegeService;
import nl.tranquilizedquality.adm.gwt.gui.client.service.privilege.PrivilegeServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.service.release.ReleaseService;
import nl.tranquilizedquality.adm.gwt.gui.client.service.release.ReleaseServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.service.repository.RepositoryService;
import nl.tranquilizedquality.adm.gwt.gui.client.service.repository.RepositoryServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.service.role.RoleService;
import nl.tranquilizedquality.adm.gwt.gui.client.service.role.RoleServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.service.scope.ScopeService;
import nl.tranquilizedquality.adm.gwt.gui.client.service.scope.ScopeServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.service.security.UserGroupService;
import nl.tranquilizedquality.adm.gwt.gui.client.service.security.UserGroupServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.service.user.UserService;
import nl.tranquilizedquality.adm.gwt.gui.client.service.user.UserServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.view.AdmViewPort;

import java.util.List;


/**
 * The {@link EntryPoint} of the ADM application.
 *
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 */
public class AdmModule extends AbstractModule {

    /** The key used to retrieve the scope service from the {@link Registry}. */
    public static final String SCOPE_SERVICE = "scope_service";

    /** The key used to retrieve the role service from the {@link Registry}. */
    public static final String ROLE_SERVICE = "role_service";

    /** The key used to retrieve the privilege service from the {@link Registry}. */
    public static final String PRIVILEGE_SERVICE = "privilege_service";

    /** The key used to retrieve the repository service from the {@link Registry} . */
    public static final String REPOSITORY_SERVICE = "repository_service";

    /** The key used to retrieve the session service from the {@link Registry} . */
    public static final String SESSION_SERVICE = "session_service";

    /** The key used to retrieve the environment service from the {@link Registry}. */
    public static final String ENVIRONMENT_SERVICE = "environment_service";

    /** The key used to retrieve the artifact service from the {@link Registry}. */
    public static final String ARTIFACT_SERVICE = "artifact_service";

    /** The key used to retrieve the release service from the {@link Registry}. */
    public static final String RELEASE_SERVICE = "release_service";

    /** The key used to retrieve the deployment service from the {@link Registry} . */
    public static final String DEPLOYMENT_SERVICE = "deployment_service";

    /** The key used to retrieve the user group service from the {@link Registry} . */
    public static final String USER_GROUP_SERVICE = "user_group_service";

    /** The key used to retrieve the user service from the {@link Registry} . */
    public static final String USER_SERVICE = "user_service";

    /** The key used to retrieve the log tail service from the {@link Registry} . */
    public static final String LOG_TAIL_SERVICE = "log_tail_service";

    /** The key used to retrieve the user privileges from the {@link Registry}. */
    public static final String USER_PRIVILEGES = "user_privileges";

    /** Added to the first session timeout check to allow for startup time */
    private static final int INITIAL_TIMEOUT_PAD = 60000;

    /** Added to the session timeout check timer. */
    private static final int TIMEOUT_PAD = 15000;

    /** Session timeout timer. */
    private Timer sessionTimeoutResponseTimer;

    @Override
    public void onModuleLoad() {
        /*
         * Get the root panel
         */
        final RootPanel rootPanel = RootPanel.get();

        /*
         * Create the remote services.
         */
        final AuthorizationServiceAsync authorizationService = AuthorizationService.Util.getInstance();
        final ApplicationServiceAsync applicationService = ApplicationService.Util.getInstance();
        final SessionServiceAsync sessionService = SessionService.Util.getInstance();
        final RepositoryServiceAsync repositoryService = RepositoryService.Util.getInstance();
        final EnvironmentServiceAsync environmentService = EnvironmentService.Util.getInstance();
        final ArtifactServiceAsync artifactService = ArtifactService.Util.getInstance();
        final ReleaseServiceAsync releaseService = ReleaseService.Util.getInstance();
        final DeploymentServiceAsync deploymentService = DeploymentService.Util.getInstance();
        final UserGroupServiceAsync userGroupService = UserGroupService.Util.getInstance();
        final UserServiceAsync userService = UserService.Util.getInstance();
        final LogTailServiceAsync logTailService = LogTailService.Util.getInstance();
        final RoleServiceAsync roleService = RoleService.Util.getInstance();
        final ScopeServiceAsync scopeService = ScopeService.Util.getInstance();
        final PrivilegeServiceAsync privilegeService = PrivilegeService.Util.getInstance();

        /*
         * Create the application icons.
         */
        final AdmIcons icons = GWT.create(AdmIcons.class);

        /*
         * Register all remote services so they can be used throughout the
         * application.
         */
        Registry.register(AUTHORIZATION_SERVICE, authorizationService);
        Registry.register(APPLICATION_SERVICE, applicationService);
        Registry.register(SESSION_SERVICE, sessionService);
        Registry.register(REPOSITORY_SERVICE, repositoryService);
        Registry.register(ENVIRONMENT_SERVICE, environmentService);
        Registry.register(ARTIFACT_SERVICE, artifactService);
        Registry.register(RELEASE_SERVICE, releaseService);
        Registry.register(DEPLOYMENT_SERVICE, deploymentService);
        Registry.register(USER_GROUP_SERVICE, userGroupService);
        Registry.register(USER_SERVICE, userService);
        Registry.register(LOG_TAIL_SERVICE, logTailService);
        Registry.register(ROLE_SERVICE, roleService);
        Registry.register(SCOPE_SERVICE, scopeService);
        Registry.register(PRIVILEGE_SERVICE, privilegeService);

        /*
         * Register the icons so they can be used throughout the application.
         */
        Registry.register(ICONS, icons);

        /*
         * Retrieve the version information.
         */
        applicationService.getProperty("version", new AsyncCallback<String>() {

            @Override
            public void onFailure(final Throwable throwable) {
                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.ERROR);
                box.setTitle("Retrieve application version.");
                box.setMessage(throwable.getMessage());
                box.setButtons(MessageBox.OK);
                box.show();
            }

            @Override
            public void onSuccess(final String value) {
                Registry.register(VERSION, value);

                /*
                 * Retrieve privilege information for the logged in user so it can be used to
                 * configure the menu to show only items that are allowed for the logged in user.
                 */
                authorizationService.findLoggedInUserPrivileges(new AsyncCallback<List<String>>() {

                    @Override
                    public void onSuccess(final List<String> privileges) {
                        Registry.register(USER_PRIVILEGES, privileges);

                        /*
                         * Setup the view port.
                         */
                        final Viewport viewport = new AdmViewPort();
                        Registry.register(VIEW_PORT, viewport);

                        rootPanel.add(viewport);

                        /*
                         * Register the navigation controller. This needs to be done
                         * after the GUI initialization since it needs to be aware of
                         * the GUI.
                         */
                        Registry.register(NAVIGATION_CONTROLLER, new AdmNavigationController());

                        /**
                         * Initialize session timers.
                         */
                        initSessionTimer();
                    }

                    @Override
                    public void onFailure(final Throwable throwable) {
                        final MessageBox box = new MessageBox();
                        box.setIcon(MessageBox.ERROR);
                        box.setTitle("Retrieve privileges.");
                        box.setMessage(throwable.getMessage());
                        box.setButtons(MessageBox.OK);
                        box.show();
                    }

                });

            }
        });

    }

    private void initSessionTimer() {

        final SessionServiceAsync service = Registry.get(SESSION_SERVICE);

        service.getUserSessionTimeout(new AsyncCallback<Integer>() {
            public void onSuccess(Integer timeout) {
                sessionTimeoutResponseTimer = new Timer() {
                    @Override
                    public void run() {
                        checkUserSessionAlive();
                    }
                };
                sessionTimeoutResponseTimer.schedule(timeout + INITIAL_TIMEOUT_PAD);
            }

            public void onFailure(Throwable caught) {
                displaySessionTimedOut();
            }
        });
    }

    private void checkUserSessionAlive() {

        final SessionServiceAsync service = Registry.get(SESSION_SERVICE);

        service.getUserSessionTimeout(new AsyncCallback<Integer>() {
            public void onSuccess(Integer timeout) {
                sessionTimeoutResponseTimer.cancel();
                sessionTimeoutResponseTimer.schedule(timeout + TIMEOUT_PAD);
            }

            public void onFailure(Throwable caught) {
                displaySessionTimedOut();
            }
        });

    }

    private void displaySessionTimedOut() {
        MessageBox.alert("Session Timeout", "Your session has timed out.",
                new Listener<MessageBoxEvent>() {

                    public void handleEvent(MessageBoxEvent be) {
                        Window.Location.reload();
                    }
                });
    }
}
