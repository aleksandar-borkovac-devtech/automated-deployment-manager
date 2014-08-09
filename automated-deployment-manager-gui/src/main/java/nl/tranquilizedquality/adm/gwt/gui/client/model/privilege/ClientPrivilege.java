package nl.tranquilizedquality.adm.gwt.gui.client.model.privilege;

import java.util.HashSet;
import java.util.Set;

import nl.tranquilizedquality.adm.commons.business.domain.Privilege;
import nl.tranquilizedquality.adm.commons.business.domain.Role;
import nl.tranquilizedquality.adm.commons.business.domain.Scope;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.AbstractUpdatableBeanModel;
import nl.tranquilizedquality.adm.gwt.gui.client.model.role.ClientRole;
import nl.tranquilizedquality.adm.gwt.gui.client.model.scope.ClientScope;

/**
 * Client side representation of a {@link Privilege}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public class ClientPrivilege extends AbstractUpdatableBeanModel<Long> implements Privilege {

	/**
	 * Unique identifier used for serialization.
	 */
	private static final long serialVersionUID = -5715879221586781071L;

	/** The name of the privilege. */
	private String name;

	/** The description of the privilege. */
	private String description;

	/** Determines if the privilege is valid. */
	private Boolean valid;

	/** The {@link Scope} where this privilege belongs to. */
	private Scope scope;

	/** The roles this privilege belongs to. */
	private Set<Role> roles;

	@Override
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@Override
	public Boolean isValid() {
		return valid;
	}

	@Override
	public void setValid(final Boolean valid) {
		this.valid = valid;
	}

	@Override
	public Scope getScope() {
		return scope;
	}

	public void setScope(final Scope scope) {
		this.scope = scope;
	}

	/**
	 * @return the roles
	 */
	@Override
	public Set<Role> getRoles() {
		return roles;
	}

	/**
	 * @param roles
	 *            the roles to set
	 */
	@Override
	public void setRoles(final Set<Role> roles) {
		this.roles = roles;
	}

	/**
	 * Copy the fields of the given {@link Privilege} object into this object.
	 * For each collection field it will add an empty collection.
	 * 
	 * @param privilege
	 *            the origin object.
	 */
	public void shallowCopy(final Privilege privilege) {
		super.copy(privilege);

		this.name = privilege.getName();
		this.description = privilege.getDescription();
		this.valid = privilege.isValid();

		final ClientScope clientScope = new ClientScope();
		clientScope.shallowCopy(privilege.getScope());
		this.scope = clientScope;

		this.roles = new HashSet<Role>();
	}

	@Override
	public void copy(final DomainObject<Long> object) {
		if (object instanceof Privilege) {
			final Privilege privilege = (Privilege) object;
			shallowCopy(privilege);

			/*
			 * Create client side role objects.
			 */
			final Set<Role> originalRoles = privilege.getRoles();
			if (originalRoles != null) {
				for (final Role role : originalRoles) {
					final ClientRole clientRole = new ClientRole();
					clientRole.shallowCopy(role);

					roles.add(clientRole);
				}
			}
		}
	}

	@Override
	public boolean equals(final Object obj) {

		if (obj instanceof ClientPrivilege) {
			final ClientPrivilege privilege = (ClientPrivilege) obj;

			if (this.id != null && !this.id.equals(privilege.getId())) {
				return false;
			}
			else if (this.id == null && privilege.getId() != null) {
				return false;
			}

			if (this.scope != null && !this.scope.equals(privilege.getScope())) {
				return false;
			}
			else if (this.scope == null && privilege.getScope() != null) {
				return false;
			}

			if (this.name != null && !this.name.equals(privilege.getName())) {
				return false;
			}
			else if (this.name == null && privilege.getName() != null) {
				return false;
			}

			if (this.description != null && !this.description.equals(privilege.getDescription())) {
				return false;
			}
			else if (this.description == null && privilege.getName() != null) {
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

		result = prime * result + (scope == null ? 0 : scope.hashCode());
		result = prime * result + (name == null ? 0 : name.hashCode());
		result = prime * result + (description == null ? 0 : description.hashCode());
		result = prime * result + (id == null ? 0 : id.intValue());

		return result;
	}
}
