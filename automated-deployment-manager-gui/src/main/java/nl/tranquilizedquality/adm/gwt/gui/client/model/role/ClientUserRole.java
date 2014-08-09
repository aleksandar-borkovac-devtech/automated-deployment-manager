package nl.tranquilizedquality.adm.gwt.gui.client.model.role;

import java.util.Date;

import nl.tranquilizedquality.adm.commons.business.domain.Role;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.business.domain.UserRole;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.AbstractUpdatableBeanModel;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUser;

/**
 * 
 * Client side representation of a {@link UserRole}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 22 jan. 2011
 * 
 * 
 */
public class ClientUserRole extends AbstractUpdatableBeanModel<Long> implements UserRole {

	private static final long serialVersionUID = 2106759423535724019L;

	/** Determines if the user role is active. */
	private Boolean active;

	/** The date from which this user role was active. */
	private Date activeFrom;

	/** The date untill this user role was active. */
	private Date activeUntil;

	/** The {@link User} bound to a role. */
	private User user;

	/** The {@link Role} that is bound to the user. */
	private Role role;

	/**
	 * Default constructor.
	 */
	public ClientUserRole() {
		user = new ClientUser();
		role = new ClientRole();
	}

	@Override
	public Boolean isActive() {
		return active;
	}

	@Override
	public void setActive(final Boolean active) {
		this.active = active;
	}

	@Override
	public Date getActiveFrom() {
		return activeFrom;
	}

	@Override
	public void setActiveFrom(final Date activeFrom) {
		this.activeFrom = activeFrom;
	}

	@Override
	public Date getActiveUntil() {
		return activeUntil;
	}

	@Override
	public void setActiveUntil(final Date activeUntil) {
		this.activeUntil = activeUntil;
	}

	@Override
	public User getUser() {
		return user;
	}

	@Override
	public void setUser(final User user) {
		this.user = user;
	}

	@Override
	public Role getRole() {
		return role;
	}

	@Override
	public void setRole(final Role role) {
		this.role = role;
	}

	public String getScopeName() {
		return this.role.getScope().getName();
	}

	/**
	 * Copy the fields of the given {@link UserRole} object into this object.
	 * For each collection field it will add an empty collection.
	 * 
	 * @param userRole
	 *            the origin object.
	 */
	public void shallowCopy(final UserRole userRole) {
		super.copy(userRole);

		this.active = userRole.isActive();
		this.activeFrom = userRole.getActiveFrom();
		this.activeUntil = userRole.getActiveUntil();

		final ClientUser clientUser = new ClientUser();
		clientUser.shallowCopy(userRole.getUser());
		this.user = clientUser;

		final ClientRole clientRole = new ClientRole();
		clientRole.shallowCopy(userRole.getRole());
		this.role = clientRole;
	}

	@Override
	public void copy(final DomainObject<Long> object) {
		if (object instanceof UserRole) {
			final UserRole userRole = (UserRole) object;
			shallowCopy(userRole);
		}
	}

	@Override
	public boolean equals(final Object obj) {

		if (obj instanceof ClientUserRole) {
			final ClientUserRole userRole = (ClientUserRole) obj;

			if (this.id != null && !this.id.equals(userRole.getId())) {
				return false;
			}
			else if (this.id == null && userRole.getId() != null) {
				return false;
			}

			if (this.role != null && !this.role.equals(userRole.getRole())) {
				return false;
			}
			else if (this.role == null && userRole.getRole() != null) {
				return false;
			}

			if (this.user != null && !this.user.equals(userRole.getUser())) {
				return false;
			}
			else if (this.user == null && userRole.getUser() != null) {
				return false;
			}

			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		result = prime * result + (role == null ? 0 : role.hashCode());
		result = prime * result + (user == null ? 0 : user.hashCode());
		result = prime * result + (id == null ? 0 : id.intValue());

		return result;
	}
}
