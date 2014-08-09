package nl.tranquilizedquality.adm.gwt.gui.server.service.user;

import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.domain.factory.AbstractFactory;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUser;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUser;

/**
 * Factory that can transform {@link User} objects into {@link ClientUser}
 * objects or {@link HibernateUser} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public class UserFactory extends AbstractFactory<ClientUser, HibernateUser, User> {

	@Override
	protected ClientUser createNewClientBean() {
		return new ClientUser();
	}

	@Override
	protected HibernateUser createNewPersistentBean() {
		return new HibernateUser();
	}

}
