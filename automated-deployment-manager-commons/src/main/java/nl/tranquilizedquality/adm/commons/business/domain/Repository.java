package nl.tranquilizedquality.adm.commons.business.domain;

import nl.tranquilizedquality.adm.commons.domain.UpdatableDomainObject;

/**
 * Representation of a Maven repository where artifacts can be downloaded from.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jun. 2011
 */
public interface Repository extends UpdatableDomainObject<Long> {

    /**
     * Retrieves the name of the repository.
     * 
     * @return Returns a {@link String} representation of the name.
     */
    String getName();

    /**
     * Retrieves the URL where the repository resides.
     * 
     * @return Returns a {@link String} representation of the repository URL.
     */
    String getRepositoryUrl();

    /**
     * Determines if this repository is to be used or not.
     * 
     * @return Return true if it is to be used otherwise it will return false.
     */
    Boolean isEnabled();

    /**
     * Retrieves id that is used in Sonatype Nexus to identify a repository. e.g. public-snapshots,
     * releases, snapshots etc.
     * 
     * @return Returns a {@link String} value of the repository id.
     */
    String getRepositoryId();

}
