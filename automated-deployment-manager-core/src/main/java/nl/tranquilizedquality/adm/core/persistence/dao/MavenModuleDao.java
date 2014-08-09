package nl.tranquilizedquality.adm.core.persistence.dao;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.command.MavenModuleSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.MavenModule;
import nl.tranquilizedquality.adm.commons.hibernate.dao.BaseDao;

/**
 * DAO that manages {@link MavenModule} beans.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jun. 2011
 * @param <T>
 *            The implemenation type.
 */
public interface MavenModuleDao<T extends MavenModule> extends BaseDao<T, Long> {

	/**
	 * Searches for {@link MavenModule} objects based on the specified search
	 * criteria.
	 * 
	 * @param sc
	 *            The search criteria.
	 * @return Returns a {@link List} of {@link MavenModule} objects or an empty
	 *         one if none could be found.
	 */
	List<MavenModule> findBySearchCommand(MavenModuleSearchCommand sc);

	/**
	 * Counts the number of Maven modules based on the specified search
	 * criteria.
	 * 
	 * @param sc
	 *            The search criteria.
	 * @return Returns a value of 0 or greater.
	 */
	int findNumberOfMavenArtifacts(MavenModuleSearchCommand sc);

}
