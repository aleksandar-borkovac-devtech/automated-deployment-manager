package nl.tranquilizedquality.adm.core.business.manager;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.command.MavenArtifactSearchCommand;
import nl.tranquilizedquality.adm.commons.business.command.MavenModuleSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.MavenModule;

import org.springframework.validation.Errors;

/**
 * Manager that manages {@link MavenArtifact} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 11 sep. 2011
 */
public interface MavenArtifactManager {

    /**
     * Retrieves all available {@link MavenModule} objects.
     * 
     * @return Returns a {@link List} containing {@link MavenModule} objects or
     *         an empty one if none could be found.
     */
    List<MavenModule> findAllMavenModules();

    /**
     * Searches for the {@link MavenArtifact} with the specified unique
     * identifier.
     * 
     * @param id
     *            The unique identifier of the {@link MavenArtifact}.
     * @return Returns a {@link MavenArtifact} or null if none could be found.
     */
    MavenArtifact findArtifactById(Long id);

    /**
     * Saves the specified {@link MavenArtifact}.
     * 
     * @param artifact
     *            The artifact that will be saved.
     * @param errors
     *            Errors object that will be populated when something goes
     *            wrong.
     * @return Returns the saved {@link MavenArtifact}.
     */
    MavenArtifact storeMavenArtifact(MavenArtifact artifact, Errors errors);

    /**
     * Retrieves all available {@link MavenArtifact} objects.
     * 
     * @param sc
     *            The search criteria that will be used.
     * @return Returns a {@link List} containing {@link MavenArtifact} objects.
     */
    List<MavenArtifact> findArtifacts(MavenArtifactSearchCommand sc);

    /**
     * Counts the number of artifacts based on the specified search criteria.
     * 
     * @param sc
     *            The search criteria that will be used.
     * @return Returns an integer value of 0 or greater.
     */
    int findNumberOfArtifacts(MavenArtifactSearchCommand sc);

    /**
     * Searches for {@link MavenModule} objects based on the specified search
     * criteria.
     * 
     * @param sc
     *            The search criteria to search on.
     * @return Returns a {@link List} of {@link MavenModule} objects or an empty
     *         one if none could be found.
     */
    List<MavenModule> findMavenModules(MavenModuleSearchCommand sc);

    /**
     * Counts the total number of {@link MavenModule} objects that should be
     * returned based on the specified search criteria.
     * 
     * @param sc
     *            The search criteria to search on.
     * @return Returns an integer value of 0 or higher.
     */
    int findNumberOfMavenModules(MavenModuleSearchCommand sc);

    /**
     * Retrieves a {@link MavenModule} with the specified id.
     * 
     * @param id
     *            The unique identifier of the {@link MavenModule}.
     * @return Returns a {@link MavenModule} or null if none could be found.
     */
    MavenModule findMavenModuleById(Long id);

    /**
     * Stores the specified {@link MavenModule}.
     * 
     * @param module
     *            The module that will be stored.
     * @param errors
     *            Errors object that will be populated when something goes
     *            wrong.
     * @return Returns the stored {@link MavenModule}.
     */
    MavenModule storeMavenModule(MavenModule module, Errors errors);

    /**
     * Removes the specified artifact.
     * 
     * @param artifact
     *            The artifact that will be removed.
     */
    void removeMavenArtifact(MavenArtifact artifact);

    /**
     * Searches for all available maven modules excluding the one that has the
     * specified unique identifier since you cannot have a dependency on
     * yourself.
     * 
     * @param excludeMavenModuleId
     *            The unique identifier of the maven module that should not be
     *            included.
     * @return Returns a {@link List} containing {@link MavenModule} objects or
     *         an empty one if there are no dependencies available.
     */
    List<MavenModule> findAvailableDependencies(Long excludeMavenModuleId);

}
