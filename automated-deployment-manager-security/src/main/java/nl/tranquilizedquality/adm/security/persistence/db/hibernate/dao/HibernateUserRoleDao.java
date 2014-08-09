package nl.tranquilizedquality.adm.security.persistence.db.hibernate.dao;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.business.domain.UserRole;
import nl.tranquilizedquality.adm.commons.hibernate.dao.AbstractHibernateBaseDao;
import nl.tranquilizedquality.adm.security.persistence.db.dao.UserRoleDao;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUserRole;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * Hibernate DAO that manages {@link HibernateUserRole} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 26, 2012
 * 
 */
public class HibernateUserRoleDao extends AbstractHibernateBaseDao<HibernateUserRole, Long> implements UserRoleDao<HibernateUserRole> {

	@Override
	protected Class<HibernateUserRole> getDomainClass() {
		return HibernateUserRole.class;
	}

	@Override
	public HibernateUserRole newDomainObject() {
		return new HibernateUserRole();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<UserRole> findByUser(final User user) {
		final Session currentSession = getCurrentSession();
		final Criteria criteria = currentSession.createCriteria(domainClass);

		criteria.add(Restrictions.eq("user", user));

		return criteria.list();

	}
}
