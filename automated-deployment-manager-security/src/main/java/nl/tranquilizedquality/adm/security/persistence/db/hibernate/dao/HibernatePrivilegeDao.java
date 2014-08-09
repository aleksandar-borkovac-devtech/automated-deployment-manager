/**
 * <pre>
 * Project: automated-deployment-manager-security Created on: 24 nov. 2011 File: fHibernatePrivilegeDao.java
 * Package: nl.Tranquilized Quality.adm.security.persistence.db.hibernate.dao
 * 
 * Copyright (c) 2011 Tranquilized Quality www.Tranquilized Quality.nl All rights
 * reserved.
 * 
 * This software is the confidential and proprietary information of Tranquilized Quality
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Tranquilized Quality.
 * </pre>
 */
package nl.tranquilizedquality.adm.security.persistence.db.hibernate.dao;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.command.PrivilegeSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.Privilege;
import nl.tranquilizedquality.adm.commons.business.domain.Scope;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.hibernate.dao.AbstractHibernateBaseDao;
import nl.tranquilizedquality.adm.security.persistence.db.dao.PrivilegeDao;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernatePrivilege;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUserRole;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

/**
 * Hibernate DAO that manages {@link HibernatePrivilege} objects.
 * 
 * @author Salomo Petrus
 * @since 24 nov. 2011
 * 
 */
public class HibernatePrivilegeDao extends AbstractHibernateBaseDao<HibernatePrivilege, Long> implements PrivilegeDao<HibernatePrivilege> {

	@Override
	protected Class<HibernatePrivilege> getDomainClass() {
		return HibernatePrivilege.class;
	}

	@Override
	public HibernatePrivilege newDomainObject() {
		return new HibernatePrivilege();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Privilege> findByUserAndScope(final User user, final Scope scope) {
		final Session session = getCurrentSession();
		final Criteria criteria = session.createCriteria(domainClass, "prv");
		criteria.add(Restrictions.eq("scope", scope));
		criteria.add(Restrictions.eq("valid", true));

		final DetachedCriteria dc = DetachedCriteria.forClass(HibernateUserRole.class, "url");
		dc.add(Restrictions.eq("url.user", user));
		dc.add(Restrictions.eq("url.active", true));
		final DetachedCriteria roleCriteria = dc.createCriteria("role", "rol");
		roleCriteria.add(Restrictions.eq("rol.valid", true));
		roleCriteria.add(Restrictions.eq("rol.scope", scope));

		final DetachedCriteria privilegeCriteria = roleCriteria.createCriteria("privileges", "rpr");
		privilegeCriteria.add(Property.forName("rpr.id").eqProperty("prv.id"));

		criteria.add(Subqueries.exists(dc.setProjection(Projections.property("url.id"))));

		return criteria.list();
	}

	@Override
	public int findNumberOfPrivileges(final PrivilegeSearchCommand searchCommand) {
		final Session currentSession = getCurrentSession();
		final Criteria criteria = getPrivilegeSearchCriteria(searchCommand, currentSession);
		criteria.setProjection(Projections.rowCount());

		final Long count = (Long) criteria.uniqueResult();
		return count.intValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Privilege> findPrivileges(final PrivilegeSearchCommand sc) {

		final Session currentSession = getCurrentSession();
		final Criteria criteria = getPrivilegeSearchCriteria(sc, currentSession);

		configurePagingAndSorting(sc, criteria);

		return criteria.list();
	}

	/**
	 * Creates the {@link Criteria} from the passed in search command.
	 * 
	 * @param searchCommand
	 *            The search criteria.
	 * @return Returns the {@link Criteria}.
	 */
	private Criteria getPrivilegeSearchCriteria(final PrivilegeSearchCommand searchCommand, final Session session) {
		final Criteria criteria = session.createCriteria(this.domainClass);

		final Boolean valid = searchCommand.getValid();
		if (valid != null) {
			criteria.add(Restrictions.eq("valid", valid));
		}

		final Scope scope = searchCommand.getScope();
		if (scope != null) {
			criteria.add(Restrictions.eq("scope", scope));
		}

		return criteria;
	}

}
