package nl.tranquilizedquality.adm.core.business.manager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import nl.tranquilizedquality.adm.commons.business.domain.Environment;
import nl.tranquilizedquality.adm.commons.business.domain.EnvironmentNotificationSetting;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.Release;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.core.business.manager.NotificationManager;
import nl.tranquilizedquality.adm.core.business.manager.UserSettingsManager;
import nl.tranquilizedquality.adm.core.business.template.renderer.TemplateRenderer;
import nl.tranquilizedquality.adm.security.business.manager.EmailManager;
import nl.tranquilizedquality.adm.security.business.manager.SecurityContextManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Manager that sends notifications.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 19 okt. 2012
 */
public class NotificationManagerImpl implements NotificationManager {

    /** Logger for this class. */
    private static final Log LOGGER = LogFactory.getLog(NotificationManagerImpl.class);

    /** Template renderer. */
    private TemplateRenderer renderer;

    /** Manager that can retrieve the logged in user. */
    private SecurityContextManager securityContextManager;

    /** Manager used to send emails. */
    private EmailManager emailManager;

    /** Manager that */
    private UserSettingsManager userSettingsManager;

    @Override
    public void sendDeploymentNotification(final Release release, final List<MavenArtifact> deployedArtifacts,
            final Environment environment) {

        final User loggedInUser = securityContextManager.findLoggedInUser();

        final Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("environment", environment);
        properties.put("release", release);
        properties.put("artifacts", deployedArtifacts);
        properties.put("user", loggedInUser);

        final String message = renderer.merge("templates/email-deployment-notification.vm", properties);

        final String releaseName = release.getName();
        final String subject = "ADM - Deployment notification for " + releaseName;

        final UserGroup userGroup = release.getUserGroup();
        final List<User> users = userGroup.getUsers();
        final List<InternetAddress> recipients = new ArrayList<InternetAddress>();
        for (final User user : users) {
            boolean sendEmail = true;
            final List<EnvironmentNotificationSetting> settingsForUser = userSettingsManager.findSettingsForUser(user);
            for (final EnvironmentNotificationSetting environmentNotificationSetting : settingsForUser) {
                final Environment settingEnvironment = environmentNotificationSetting.getEnvironment();
                if (settingEnvironment.equals(environment)) {
                    sendEmail = environmentNotificationSetting.isEmailNotification();
                    break;
                }
            }

            if (sendEmail) {
                final String email = user.getEmail();

                try {
                    final InternetAddress emailAddress = new InternetAddress(email);
                    recipients.add(emailAddress);
                } catch (final AddressException e) {
                    if (LOGGER.isErrorEnabled()) {
                        final String msg = "Failed to send email notification to " + email;
                        LOGGER.error(msg, e);
                    }
                }
            }
        }

        if (!recipients.isEmpty()) {
            try {
                emailManager.sendEmail(recipients, "ADM User", subject, message);
            } catch (final Exception e) {
                if (LOGGER.isErrorEnabled()) {
                    final String msg = "Failed to send email notification!";
                    LOGGER.error(msg, e);
                }
            }
        }
    }

    @Required
    public void setRenderer(final TemplateRenderer renderer) {
        this.renderer = renderer;
    }

    @Required
    public void setSecurityContextManager(final SecurityContextManager securityContextManager) {
        this.securityContextManager = securityContextManager;
    }

    @Required
    public void setEmailManager(final EmailManager emailManager) {
        this.emailManager = emailManager;
    }

    @Required
    public void setUserSettingsManager(final UserSettingsManager userSettingsManager) {
        this.userSettingsManager = userSettingsManager;
    }

}
