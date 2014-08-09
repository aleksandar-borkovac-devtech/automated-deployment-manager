package nl.tranquilizedquality.adm.gwt.gui.server.service.role;

import nl.tranquilizedquality.adm.commons.business.domain.UserRole;
import nl.tranquilizedquality.adm.commons.domain.factory.AbstractFactory;
import nl.tranquilizedquality.adm.gwt.gui.client.model.role.ClientUserRole;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUserRole;

/**
 * Factory that can transform {@link UserRole} objects into
 * {@link ClientUserRole} objects or {@link HibernateUserRole} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public class UserRoleFactory extends AbstractFactory<ClientUserRole, HibernateUserRole, UserRole> {

	@Override
	protected ClientUserRole createNewClientBean() {
		return new ClientUserRole();
	}

	@Override
	protected HibernateUserRole createNewPersistentBean() {
		return new HibernateUserRole();
	}

}
