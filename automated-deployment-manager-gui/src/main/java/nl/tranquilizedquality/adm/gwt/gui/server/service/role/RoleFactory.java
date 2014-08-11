package nl.tranquilizedquality.adm.gwt.gui.server.service.role;

import nl.tranquilizedquality.adm.commons.business.domain.Role;
import nl.tranquilizedquality.adm.commons.domain.factory.AbstractFactory;
import nl.tranquilizedquality.adm.gwt.gui.client.model.role.ClientRole;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateRole;

/**
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public class RoleFactory extends AbstractFactory<ClientRole, HibernateRole, Role> {

    @Override
    protected ClientRole createNewClientBean() {
        return new ClientRole();
    }

    @Override
    protected HibernateRole createNewPersistentBean() {
        return new HibernateRole();
    }

}
