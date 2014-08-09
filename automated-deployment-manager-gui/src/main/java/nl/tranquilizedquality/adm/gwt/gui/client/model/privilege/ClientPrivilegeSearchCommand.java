package nl.tranquilizedquality.adm.gwt.gui.client.model.privilege;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.command.PrivilegeSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.Privilege;

import com.extjs.gxt.ui.client.data.BeanModelTag;

/**
 * Client side representation of the search criteria for a {@link Privilege}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public class ClientPrivilegeSearchCommand extends PrivilegeSearchCommand implements BeanModelTag {

	/**
	 * Unique identifier used for serialization.
	 */
	private static final long serialVersionUID = 4410364074637948973L;

	/**
	 * List of {@link Privilege} object that will be excluded from the search
	 * results.
	 */
	private List<? extends Privilege> excludedPrivileges;

	/**
	 * @return the excludedRoles
	 */
	public List<? extends Privilege> getExcludedPrivileges() {
		return excludedPrivileges;
	}

	/**
	 * @param excludedPrivileges
	 *            the excludedPrivileges to set
	 */
	public void setExcludedPrivileges(final List<? extends Privilege> excludedPrivileges) {
		this.excludedPrivileges = excludedPrivileges;
	}

}
