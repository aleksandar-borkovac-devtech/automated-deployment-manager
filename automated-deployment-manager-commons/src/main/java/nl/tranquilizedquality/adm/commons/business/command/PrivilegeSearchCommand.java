package nl.tranquilizedquality.adm.commons.business.command;

import nl.tranquilizedquality.adm.commons.business.domain.Privilege;
import nl.tranquilizedquality.adm.commons.business.domain.Scope;
import nl.tranquilizedquality.adm.commons.domain.PagingSearchCommand;

/**
 * Search criteria for searching for a {@link Privilege}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 22 okt. 2012
 */
public class PrivilegeSearchCommand extends PagingSearchCommand {

    /**
     * Unique identifier used for serialization.
     */
    private static final long serialVersionUID = 1397022143929755435L;

    /** Determines if valid, invalid or all privileges should be retrieved. */
    private Boolean valid;

    /** Determines for which scope privileges need to be retrieved. */
    private Scope scope;

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
    public void setValid(final Boolean valid) {
        this.valid = valid;
    }

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

}
