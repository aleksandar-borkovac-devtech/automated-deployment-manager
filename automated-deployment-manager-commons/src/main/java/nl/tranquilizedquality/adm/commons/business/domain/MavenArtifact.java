package nl.tranquilizedquality.adm.commons.business.domain;

import nl.tranquilizedquality.adm.commons.domain.UpdatableDomainObject;

/**
 * Representation of a Maven artifact.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jun. 2011
 */
public interface MavenArtifact extends UpdatableDomainObject<Long> {

    /**
     * Retrieves the version of the artifact.
     * 
     * @return Returns a {@link String} representation of the version.
     */
    String getVersion();

    /**
     * Retrieves the parent module.
     * 
     * @return Returns a {@link MavenModule}.
     */
    MavenModule getParentModule();

    /**
     * Retrieves the release where this artifact is part of.
     * 
     * @return Returns the {@link Release}.
     */
    Release getRelease();

    /**
     * Retrieves the name of the file on the file system.
     * 
     * @return Returns a {@link String} representation of the path to the file.
     */
    String getFile();

    /**
     * Sets the file.
     * 
     * @param file
     *        The file that will be set.
     */
    void setFile(String file);

    /**
     * Determines if the target system should be shutdown before deployment of
     * this module.
     * 
     * @return Returns true if the target system should be shutdown before
     *         deployment otherwise it will return false.
     */
    Boolean getTargetSystemShutdown();

    /**
     * Determines if the target system should be started up after deployment of
     * this module.
     * 
     * @return Returns true if the target system should be started up otherwise
     *         it will return false.
     */
    Boolean getTargetSystemStartup();

    /**
     * Retrieves the rank of this artifact in a release to determine when it
     * should be deployed.
     * 
     * @return Returns an {@link Integer} value greater than 0.
     */
    Integer getRank();

    /**
     * Sets the rank of an artifact.
     * 
     * @param rank
     *        The rank that will be given to this artifact.
     */
    void setRank(Integer rank);

    /**
     * Retrieves the user group that this maven module belongs to.
     * 
     * @return Returns the {@link UserGroup}.
     */
    UserGroup getUserGroup();

    /**
     * Sets the user group this artifact belongs to.
     * 
     * @param userGroup
     *        The user group that will be set.
     */
    void setUserGroup(UserGroup userGroup);

}
