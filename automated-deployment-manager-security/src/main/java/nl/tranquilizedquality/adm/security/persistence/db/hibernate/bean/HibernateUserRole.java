package nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import nl.tranquilizedquality.adm.commons.business.domain.Role;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.business.domain.UserRole;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.hibernate.bean.AbstractUpdatableDomainObject;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;

import com.google.code.simplestuff.annotation.BusinessField;

/**
 * HibernateUserRole
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 24 nov. 2011
 */
@Entity
@Table(name = "ADM_USER_ROLES", schema = "SEC")
public class HibernateUserRole extends AbstractUpdatableDomainObject<Long> implements UserRole {
	private static final long serialVersionUID = 4532674247347926098L;

	/** Determines if a user role is still active. */
	@BusinessField
	private Boolean active;

	/** Determines from which date the user role is active. */
	@BusinessField
	private Date activeFrom;

	/**
	 * Determines until which date the user role is active. If null it's active
	 * forever.
	 */
	@BusinessField
	private Date activeUntil;

	/** The user where this role will belong to. */
	@BusinessField
	private User user;

	/** The role which is granted to the user. */
	@BusinessField
	private Role role;

	/**
	 * @return the id
	 */
	@Override
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "USER_ROLES_SEQ_GEN", initialValue = 1, allocationSize = 1, sequenceName = "SEC.USER_ROLES_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "USER_ROLES_SEQ_GEN")
	public Long getId() {
		return id;
	}

	@Override
	@Type(type = "yes_no")
	@Index(name = "IDX_USER_ROLE_ACTIVE")
	@Column(name = "ACTIVE", nullable = false)
	public Boolean isActive() {
		return active;
	}

	@Transient
	// Used by equals()
	public Boolean getActive() {
		return active;
	}

	@Override
	public void setActive(final Boolean active) {
		this.active = active;
	}

	@Override
	@Type(type = "timestamp")
	@Index(name = "IDX_USER_ROLE_ACTIVE_FROM")
	@Column(name = "ACTIVE_FROM", unique = false, nullable = false)
	public Date getActiveFrom() {
		return activeFrom;
	}

	@Override
	public void setActiveFrom(final Date activeFrom) {
		this.activeFrom = activeFrom;
	}

	@Override
	@Type(type = "timestamp")
	@Column(name = "ACTIVE_UNTIL", unique = false, nullable = true)
	public Date getActiveUntil() {
		return activeUntil;
	}

	@Override
	public void setActiveUntil(final Date activeUntil) {
		this.activeUntil = activeUntil;
	}

	@Override
	@ManyToOne(targetEntity = HibernateUser.class)
	@JoinColumn(name = "USR_ID", nullable = false)
	@ForeignKey(name = "FK_USER_ROLE_USER")
	public User getUser() {
		return user;
	}

	@Override
	public void setUser(final User user) {
		this.user = user;
	}

	@Override
	@ManyToOne(targetEntity = HibernateRole.class)
	@JoinColumn(name = "ROL_ID", nullable = false)
	@ForeignKey(name = "FK_USER_ROLE_ROLE")
	public Role getRole() {
		return role;
	}

	@Override
	public void setRole(final Role role) {
		this.role = role;
	}

	@Override
	public void copy(final DomainObject<Long> object) {
		super.copy(object);

		if (object instanceof UserRole) {
			final UserRole userRole = (UserRole) object;
			this.active = userRole.isActive();
			this.activeFrom = userRole.getActiveFrom();
			this.activeUntil = userRole.getActiveUntil();

			final User originalUser = userRole.getUser();
			if (originalUser != null) {
				this.user = new HibernateUser();
				this.user.copy(originalUser);
			}
			else {
				this.user = null;
			}

			final Role originalRole = userRole.getRole();
			if (originalRole != null) {
				this.role = new HibernateRole();
				this.role.copy(originalRole);
			}
			else {
				this.role = null;
			}
		}
	}
}
