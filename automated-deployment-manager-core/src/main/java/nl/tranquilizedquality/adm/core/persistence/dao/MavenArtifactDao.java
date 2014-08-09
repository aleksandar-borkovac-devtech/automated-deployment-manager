package nl.tranquilizedquality.adm.core.persistence.dao;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.command.MavenArtifactSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.Release;
import nl.tranquilizedquality.adm.commons.hibernate.dao.BaseDao;

/**
 * DAO that manages {@link MavenArtifact} beans.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jun. 2011
 * @param <T>
 *            The implementation type.
 */
public interface MavenArtifactDao<T extends MavenArtifact> extends BaseDao<T, Long> {

	/**
	 * Searches for {@link MavenArtifact} objects based on the specified search
	 * criteria.
	 * 
	 * @param sc
	 *            The search criteria.
	 * @return Returns a {@link List} of {@link MavenArtifact} objects or an
	 *         empty one if none could be found.
	 */
	List<MavenArtifact> findBySearchCommand(MavenArtifactSearchCommand sc);

	/**
	 * Counts the number of Maven artifacts based on the specified search
	 * criteria.
	 * 
	 * @param sc
	 *            The search criteria.
	 * @return Returns a value of 0 or greater.
	 */
	int findNumberOfMavenArtifacts(MavenArtifactSearchCommand sc);

	/**
	 * Retrieves the current maximum rank in the specified release.
	 * 
	 * @param release
	 *            The release where the maximum rank should be calculated for.
	 * @return Returns an integer value of 0 or greater.
	 */
	int findMaximumRankInRelease(Release release);

}
