package nl.tranquilizedquality.adm.core.business.manager.impl;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nl.tranquilizedquality.adm.commons.business.domain.EnvironmentNotificationSetting;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.core.business.manager.NotificationManager;
import nl.tranquilizedquality.adm.core.business.manager.UserSettingsManager;
import nl.tranquilizedquality.adm.core.business.template.renderer.TemplateRenderer;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateEnvironment;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateEnvironmentNotificationSetting;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateMavenArtifact;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateMavenModule;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateRelease;
import nl.tranquilizedquality.adm.security.business.manager.EmailManager;
import nl.tranquilizedquality.adm.security.business.manager.SecurityContextManager;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUser;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUserGroup;

import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link NotificationManager}.
 *
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 19 okt. 2012
 */
public class NotificationManagerTest extends EasyMockSupport {

    /** Manager that will be tested. */
    private NotificationManagerImpl notificationManager;

    /** Mocked renderer. */
    private TemplateRenderer renderer;

    /** Mocked security context manager. */
    private SecurityContextManager securityContextManager;

    /** Mocked manager. */
    private EmailManager emailManager;

    /** Mocked manager. */
    private UserSettingsManager userSettingsManager;

    @Before
    public void setUp() throws Exception {
        notificationManager = new NotificationManagerImpl();

        renderer = createMock(TemplateRenderer.class);
        securityContextManager = createMock(SecurityContextManager.class);
        emailManager = createMock(EmailManager.class);
        userSettingsManager = createMock(UserSettingsManager.class);

        notificationManager.setRenderer(renderer);
        notificationManager.setSecurityContextManager(securityContextManager);
        notificationManager.setEmailManager(emailManager);
        notificationManager.setUserSettingsManager(userSettingsManager);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testSendEmail() {
        final List<User> users = new ArrayList<User>();

        final HibernateUserGroup userGroup = new HibernateUserGroup();
        userGroup.setName("junit");
        userGroup.setUsers(users);

        final HibernateRelease release = new HibernateRelease();
        release.setName("S12.14.Consumer");

        final HibernateMavenModule parentModule = new HibernateMavenModule();
        parentModule.setName("Automated Deployment Manager");

        final HibernateMavenArtifact artifact = new HibernateMavenArtifact();
        artifact.setParentModule(parentModule);
        artifact.setVersion("1.0.0");

        final List<MavenArtifact> artifacts = new ArrayList<MavenArtifact>();
        artifacts.add(artifact);
        release.setArtifacts(artifacts);
        release.setUserGroup(userGroup);

        final HibernateEnvironment environment = new HibernateEnvironment();
        environment.setName("INT");

        final HibernateUser loggedInUser = new HibernateUser();
        loggedInUser.setEmail("salomo.petrus@tr-quality.com");
        loggedInUser.setName("Salomo Petrus");
        users.add(loggedInUser);

        final List<EnvironmentNotificationSetting> settings = new ArrayList<EnvironmentNotificationSetting>();
        final HibernateEnvironmentNotificationSetting setting = new HibernateEnvironmentNotificationSetting();
        setting.setEmailNotification(true);
        setting.setUser(loggedInUser);
        setting.setEnvironment(environment);
        settings.add(setting);

        expect(securityContextManager.findLoggedInUser()).andReturn(loggedInUser);
        expect(renderer.merge(isA(String.class), isA(Map.class))).andReturn("renderedcontent");
        emailManager.sendEmail(isA(List.class), isA(String.class), isA(String.class), isA(String.class));
        expectLastCall().once();
        expect(userSettingsManager.findSettingsForUser(loggedInUser)).andReturn(settings);

        replayAll();

        notificationManager.sendDeploymentNotification(release, artifacts, environment);

        verifyAll();
    }

}
