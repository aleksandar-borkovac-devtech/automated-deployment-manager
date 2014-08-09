package nl.tranquilizedquality.adm.security.persistence.db.hibernate.dao;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.command.ScopeSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.Scope;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.hibernate.dao.AbstractHibernateBaseDao;
import nl.tranquilizedquality.adm.security.persistence.db.dao.ScopeDao;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateScope;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * Hibernate DAO that manages {@link HibernateScope} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public class HibernateScopeDao extends AbstractHibernateBaseDao<HibernateScope, Long> implements ScopeDao<HibernateScope> {

	@Override
	protected Class<HibernateScope> getDomainClass() {
		return HibernateScope.class;
	}

	@Override
	public HibernateScope newDomainObject() {
		return new HibernateScope();
	}

	@Override
	public Scope findByName(final String name) {
		final Criteria criteria = getDefaultCriteria();

		criteria.add(Restrictions.eq("name", name));

		return (Scope) criteria.uniqueResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Scope> findScopesBySearchCommand(final ScopeSearchCommand sc) {

		final StringBuffer builder = new StringBuffer();

		final User user = sc.getUser();

		/*
		 * Create query.
		 */
		builder.append(from);
		builder.append(" as scope where ");
		builder.append(" scope.name like :name and ");
		builder.append(" (scope.description is null or scope.description like :description) ");

		if (user != null) {
			builder.append(" and :userid in (select id from scope.scopeManagers) ");
		}

		final String orderBy = sc.getOrderBy();
		if (orderBy != null && !"".equals(orderBy)) {
			if (sc.isAsc()) {
				builder.append(" order by " + orderBy + " asc");
			}
			else {
				builder.append(" order by " + orderBy + " desc");
			}
		}

		final Session currentSession = getCurrentSession();
		final Query query = currentSession.createQuery(builder.toString());
		if (sc.getMaxResults() != null) {
			query.setMaxResults(sc.getMaxResults());
		}

		if (sc.getStart() != null) {
			query.setFirstResult(sc.getStart());
		}

		String name = sc.getName();
		if (name == null) {
			name = "";
		}
		query.setParameter("name", name + "%");

		String description = sc.getDescription();
		if (description == null) {
			description = "";
		}
		query.setParameter("description", description + "%");

		if (user != null) {
			final Long userId = user.getId();
			query.setParameter("userid", userId);
		}

		return query.list();
	}

	@Override
	public int findNumberOfScopesBySearchCommand(final ScopeSearchCommand sc) {

		final StringBuffer builder = new StringBuffer();
		final User user = sc.getUser();

		/*
		 * Create query.
		 */
		builder.append("select count(scope.id) ");
		builder.append(from);
		builder.append(" as scope where ");
		builder.append(" scope.name like :name and ");
		builder.append(" (scope.description is null or scope.description like :description) ");

		if (user != null) {
			builder.append(" and :userid in (select id from scope.scopeManagers)");
		}

		final Session currentSession = getCurrentSession();
		final Query query = currentSession.createQuery(builder.toString());
		query.setMaxResults(1);

		if (sc.getStart() != null) {
			query.setFirstResult(sc.getStart());
		}

		String name = sc.getName();
		if (name == null) {
			name = "";
		}
		query.setParameter("name", name + "%");

		String description = sc.getDescription();
		if (description == null) {
			description = "";
		}
		query.setParameter("description", description + "%");

		if (user != null) {
			final Long userId = user.getId();
			query.setParameter("userid", userId);
		}

		final Long count = (Long) query.uniqueResult();
		return count.intValue();
	}

}
