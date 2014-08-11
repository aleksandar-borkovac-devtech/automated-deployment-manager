package nl.tranquilizedquality.adm.gwt.gui.client.model.role;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.command.RoleSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.Role;

import com.extjs.gxt.ui.client.data.BeanModelTag;

/**
 * Client side representation of the search criteria for searching for
 * {@link ClientRole} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public class ClientRoleSearchCommand extends RoleSearchCommand implements BeanModelTag {

    /**
     * 
     */
    private static final long serialVersionUID = 3701103177600533437L;

    /**
     * List of {@link Role} object that will be excluded from the search
     * results.
     */
    private List<Role> excludedRoles;

    /**
     * @return the excludedRoles
     */
    public List<Role> getExcludedRoles() {
        return excludedRoles;
    }

    /**
     * @param excludedRoles
     *            the excludedRoles to set
     */
    public void setExcludedRoles(final List<Role> excludedRoles) {
        this.excludedRoles = excludedRoles;
    }

}
