package nl.tranquilizedquality.adm.gwt.gui.server.service.scope;

import nl.tranquilizedquality.adm.commons.business.domain.Scope;
import nl.tranquilizedquality.adm.commons.domain.factory.AbstractFactory;
import nl.tranquilizedquality.adm.gwt.gui.client.model.scope.ClientScope;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateScope;

/**
 * Factory that can transform {@link Scope} objects into {@link ClientScope}
 * objects or {@link HibernateScope} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public class ScopeFactory extends AbstractFactory<ClientScope, HibernateScope, Scope> {

	@Override
	protected ClientScope createNewClientBean() {
		return new ClientScope();
	}

	@Override
	protected HibernateScope createNewPersistentBean() {
		return new HibernateScope();
	}

}
