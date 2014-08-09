package nl.tranquilizedquality.adm.security.persistence.db.hibernate.dao;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.command.RoleSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.Role;
import nl.tranquilizedquality.adm.commons.business.domain.Scope;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.hibernate.dao.AbstractHibernateBaseDao;
import nl.tranquilizedquality.adm.security.persistence.db.dao.RoleDao;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateRole;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * Hibernate DAO that manages {@link HibernateRole} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 26, 2012
 */
public class HibernateRoleDao extends AbstractHibernateBaseDao<HibernateRole, Long> implements RoleDao<HibernateRole> {

    @Override
    protected Class<HibernateRole> getDomainClass() {
        return HibernateRole.class;
    }

    @Override
    public HibernateRole newDomainObject() {
        return new HibernateRole();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Role> findRoles(final RoleSearchCommand sc) {
        final Criteria criteria = getRoleSearchCriteria(sc);

        configurePagingAndSorting(sc, criteria);

        return criteria.list();
    }

    @Override
    public int findNumberOfRoles(final RoleSearchCommand sc) {
        final Criteria criteria = getRoleSearchCriteria(sc);
        criteria.setProjection(Projections.rowCount());

        return ((Long) criteria.uniqueResult()).intValue();
    }

    private Criteria getRoleSearchCriteria(final RoleSearchCommand searchCommand) {
        final Criteria criteria = getDefaultCriteria();

        final Boolean valid = searchCommand.getValid();
        if (valid != null) {
            criteria.add(Restrictions.eq("valid", valid));
        }

        final Scope scope = searchCommand.getScope();
        if (scope != null) {
            criteria.add(Restrictions.eq("scope", scope));
        }

        final User user = searchCommand.getUser();
        if (user != null) {
            final Criteria userRolesCriteria = criteria.createCriteria("userRoles");
            userRolesCriteria.add(Restrictions.eq("user", user));
        }

        return criteria;
    }

}
