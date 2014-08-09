package nl.tranquilizedquality.adm.commons.business.domain;

import java.util.List;

import nl.tranquilizedquality.adm.commons.domain.UpdatableDomainObject;

/**
 * Representation of an environment e.g. DEV, INT, PROD etc.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 1 sep. 2011
 */
public interface Environment extends UpdatableDomainObject<Long> {

    /**
     * Retrieves the name of the environment.
     * 
     * @return Returns a {@link String} value of the name off the environment.
     */
    String getName();

    /**
     * Retrieves the description of an environment.
     * 
     * @return Returns a {@link String} value of the name off the environment.
     */
    String getDescription();

    /**
     * Retrieves all the users that are allowed to deploy to this environment.
     * 
     * @return Returns a {@link List} containing users or an empty one if no
     *         users are allowed to deploy to this environment.
     */
    List<User> getUsers();

    /**
     * Sets the users that are allowed to deploy to this environment.
     * 
     * @param users
     *        The users that will be set.
     */
    void setUsers(List<User> users);

    /**
     * Determines if this is a production environment.
     * 
     * @return Returns true if it's a production environment otherwise it's a none production
     *         environment.
     */
    boolean isProduction();

}
