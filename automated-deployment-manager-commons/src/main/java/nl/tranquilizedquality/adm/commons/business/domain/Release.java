package nl.tranquilizedquality.adm.commons.business.domain;

import java.util.Date;
import java.util.List;

import nl.tranquilizedquality.adm.commons.domain.UpdatableDomainObject;

/**
 * Representation of a release.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jun. 2011
 */
public interface Release extends UpdatableDomainObject<Long> {

    /**
     * Retrieves the name of the name of the release.
     * 
     * @return Returns a {@link String} representation of the name.
     */
    String getName();

    /**
     * Retrieves the release date.
     * 
     * @return Returns a {@link Date}.
     */
    Date getReleaseDate();

    /**
     * The status of the release.
     * 
     * @return Returns a {@link ReleaseStatus}.
     */
    ReleaseStatus getStatus();

    /**
     * Sets the status of a release.
     * 
     * @param status
     *            The status that will be set.
     */
    void setStatus(ReleaseStatus status);

    /**
     * The number of times this release was performed.
     * 
     * @return Returns an {@link Integer} value greater than 0 or null if it
     *         hasn't been released yet.
     */
    Integer getReleaseCount();

    /**
     * Adds 1 to the current release count.
     */
    void addReleasedCount();

    /**
     * Retrieves the number of times the release failed.
     * 
     * @return Returns an {@link Integer} value of 0 or greater or null if it
     *         hasn't failed.
     */
    Integer getReleaseFailureCount();

    /**
     * Adds 1 to the current failure count.
     */
    void addReleaseFailureCount();

    /**
     * Retrieves the date on which this release was last released.
     * 
     * @return Returns a {@link Date} or null if it wasn't released yet.
     */
    Date getLastReleasedDate();

    /**
     * Sets the released date.
     * 
     * @param date
     *            The date on which the release was performed.
     */
    void setLastReleasedDate(Date date);

    /**
     * Retrieves the artifacts that need to be released.
     * 
     * @return Returns a {@link List} containing the {@link MavenArtifact}
     *         objects that need to be released.
     */
    List<MavenArtifact> getArtifacts();

    /**
     * Sets the artifacts of a release.
     * 
     * @param artifacts
     *            The artifacts that will be set.
     */
    void setArtifacts(List<MavenArtifact> artifacts);

    /**
     * Retrieves the user group this release belongs to.
     * 
     * @return Returns {@link UserGroup}.
     */
    UserGroup getUserGroup();

    /**
     * Determines if the release is archived or not.
     * 
     * @return Returns true if the release is archived otherwise it will return
     *         false.
     */
    boolean isArchived();

}
