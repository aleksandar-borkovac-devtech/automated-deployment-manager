package nl.tranquilizedquality.adm.gwt.gui.server.service.privilege;

import nl.tranquilizedquality.adm.commons.business.domain.Privilege;
import nl.tranquilizedquality.adm.commons.domain.factory.AbstractFactory;
import nl.tranquilizedquality.adm.gwt.gui.client.model.privilege.ClientPrivilege;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernatePrivilege;

/**
 * Factory for transforming {@link ClientPrivilege} to
 * {@link HibernatePrivilege} and visa versa.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public class PrivilegeFactory extends AbstractFactory<ClientPrivilege, HibernatePrivilege, Privilege> {

    @Override
    protected ClientPrivilege createNewClientBean() {
        return new ClientPrivilege();
    }

    @Override
    protected HibernatePrivilege createNewPersistentBean() {
        return new HibernatePrivilege();
    }

}
