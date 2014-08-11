package nl.tranquilizedquality.adm.commons.business.command;

import nl.tranquilizedquality.adm.commons.business.domain.Scope;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.domain.PagingSearchCommand;

/**
 * Search criteria for searching on {@link Role} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public class RoleSearchCommand extends PagingSearchCommand {

    /**
     * Unique identifier used for serialization.
     */
    private static final long serialVersionUID = -4215425652211722321L;

    /** The {@link Scope} where the roles will be retrieved for. */
    private Scope scope;

    /** Determines if valid or non valid roles should be retrieved. */
    private Boolean valid;

    /** The user that is allowed to assign the role. */
    private User user;

    /**
     * @return the scope
     */
    public Scope getScope() {
        return scope;
    }

    /**
     * @param scope
     *            the scope to set
     */
    public void setScope(final Scope scope) {
        this.scope = scope;
    }

    /**
     * @return the valid
     */
    public Boolean getValid() {
        return valid;
    }

    /**
     * @param valid
     *            the valid to set
     */
    public void setValid(final Boolean active) {
        this.valid = active;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user
     *            the user to set
     */
    public void setUser(final User user) {
        this.user = user;
    }

}
