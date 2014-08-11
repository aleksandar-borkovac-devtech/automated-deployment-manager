package nl.tranquilizedquality.adm.core.business.manager;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.Environment;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.Release;

/**
 * Manager that sends notifications.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 19 okt. 2012
 */
public interface NotificationManager {

    /**
     * Sends a notification for the release.
     * 
     * @param release
     *            The release that was deployed.
     * @param deployedArtifacts
     *            The artifacts that were actually deployed from the specified
     *            release.
     * @param environment
     *            The environment where the release was deployed to.
     */
    void sendDeploymentNotification(Release release, List<MavenArtifact> deployedArtifacts, Environment environment);

}
