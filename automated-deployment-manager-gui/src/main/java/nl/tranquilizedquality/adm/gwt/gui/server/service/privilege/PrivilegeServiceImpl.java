package nl.tranquilizedquality.adm.gwt.gui.server.service.privilege;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.Privilege;
import nl.tranquilizedquality.adm.commons.gwt.ext.server.service.AbstractService;
import nl.tranquilizedquality.adm.gwt.gui.client.model.privilege.ClientPrivilege;
import nl.tranquilizedquality.adm.gwt.gui.client.model.privilege.ClientPrivilegeSearchCommand;
import nl.tranquilizedquality.adm.gwt.gui.client.service.privilege.PrivilegeService;
import nl.tranquilizedquality.adm.gwt.gui.client.service.privilege.PrivilegeServiceException;
import nl.tranquilizedquality.adm.security.business.exception.PrivilegeManagerException;
import nl.tranquilizedquality.adm.security.business.manager.PrivilegeManager;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernatePrivilege;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;

/**
 * Service managing {@link Privilege} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public class PrivilegeServiceImpl extends AbstractService implements PrivilegeService {

	/**
	 * The factory that transforms the client side models into domain objects
	 * and visa versa.
	 */
	private static final PrivilegeFactory PRIVILEGE_FACTORY = new PrivilegeFactory();

	/** Manager that manages {@link Privilege} objects. */
	private PrivilegeManager privilegeManager;

	@Override
	public PagingLoadResult<ClientPrivilege> findPrivileges(final PagingLoadConfig config, final ClientPrivilegeSearchCommand sc) {

		/*
		 * Search for privileges.
		 */
		final List<Privilege> privileges = privilegeManager.findPrivileges(sc);

		/*
		 * Create the client beans.
		 */
		final List<ClientPrivilege> clientBeans = PRIVILEGE_FACTORY.createClientBeans(privileges);

		int count = privilegeManager.findNumberOfPrivileges(sc);

		/*
		 * Filter the list if there is a filter defined.
		 */
		final List<? extends Privilege> excludedPrivileges = sc.getExcludedPrivileges();
		if (excludedPrivileges != null) {
			clientBeans.removeAll(excludedPrivileges);
			count -= excludedPrivileges.size();
		}

		return new BasePagingLoadResult<ClientPrivilege>(clientBeans, config.getOffset(), count);
	}

	@Override
	public ClientPrivilege findPrivilegeById(final Long id) {
		final Privilege privilege = privilegeManager.findPrivilegeById(id);

		final ClientPrivilege clientPrivilege = PRIVILEGE_FACTORY.createClientBean(privilege);

		return clientPrivilege;
	}

	@Override
	public ClientPrivilege savePrivilege(final ClientPrivilege privilege) throws PrivilegeServiceException {

		final HibernatePrivilege hibernatePrivilege = PRIVILEGE_FACTORY.createPersistentBean(privilege);

		final Errors errors = new BindException(hibernatePrivilege, hibernatePrivilege.getClass().getName());

		try {
			final Privilege storedPrivilege = privilegeManager.storePrivilege(hibernatePrivilege, errors);

			return PRIVILEGE_FACTORY.createClientBean(storedPrivilege);
		}
		catch (final PrivilegeManagerException e) {
			final List<String> errorList = createErrorList(errors);
			throw new PrivilegeServiceException("Failed to store privilege!", e, errorList);
		}
	}

	/**
	 * @param privilegeManager
	 *            the privilegeManager to set
	 */
	@Required
	public void setPrivilegeManager(final PrivilegeManager privilegeManager) {
		this.privilegeManager = privilegeManager;
	}
}
