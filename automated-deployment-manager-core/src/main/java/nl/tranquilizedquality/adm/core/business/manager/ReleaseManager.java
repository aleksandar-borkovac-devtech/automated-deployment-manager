package nl.tranquilizedquality.adm.core.business.manager;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.command.ReleaseSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.Release;

import org.springframework.validation.Errors;

/**
 * Manager that manages releases.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 17 sep. 2011
 */
public interface ReleaseManager {

    /**
     * Removes the specified release.
     * 
     * @param release
     *            The release that will be removed.
     */
    void removeRelease(Release release);

    /**
     * Finds releases based on the specified search criteria.
     * 
     * @param sc
     *            The search criteria to be used.
     * @return Returns a {@link List} of {@link Release} objects or an empty one
     *         if none could be found.
     */
    List<Release> findReleases(ReleaseSearchCommand sc);

    /**
     * Count the number of releases that should be returned based on the
     * specified search criteria.
     * 
     * @param sc
     *            The search criteria that will be used.
     * @return Returns an integer value of the number of releases.
     */
    int findNumberOfReleases(ReleaseSearchCommand sc);

    /**
     * Retrieves a {@link Release} with the specified identifier.
     * 
     * @param id
     *            The unique identifier of the release.
     * @return Returns a {@link Release} or null if none could be found.
     */
    Release findReleaseById(Long id);

    /**
     * Stores the specified release and reports back issues through the
     * {@link Errors} object.
     * 
     * @param release
     *            The release that will be stored.
     * @param errors
     *            {@link Errors} object that will be populated when something
     *            goes wrong during storage.
     * @return Returns the stored {@link Release}.
     */
    Release storeRelease(Release release, Errors errors);

}
