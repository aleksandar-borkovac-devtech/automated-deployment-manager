package nl.tranquilizedquality.adm.gwt.gui.server.service.security;

import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.commons.domain.factory.AbstractFactory;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUserGroup;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUserGroup;

/**
 * Factory that transforms client beans into persistent beans and vice versa.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 24 aug. 2012
 */
public class UserGroupFactory extends AbstractFactory<ClientUserGroup, HibernateUserGroup, UserGroup> {

	@Override
	protected ClientUserGroup createNewClientBean() {
		return new ClientUserGroup();
	}

	@Override
	protected HibernateUserGroup createNewPersistentBean() {
		return new HibernateUserGroup();
	}

}
