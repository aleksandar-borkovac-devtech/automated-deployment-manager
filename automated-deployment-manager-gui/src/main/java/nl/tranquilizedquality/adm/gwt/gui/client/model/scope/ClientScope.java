package nl.tranquilizedquality.adm.gwt.gui.client.model.scope;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import nl.tranquilizedquality.adm.commons.business.domain.Privilege;
import nl.tranquilizedquality.adm.commons.business.domain.Role;
import nl.tranquilizedquality.adm.commons.business.domain.Scope;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.AbstractUpdatableBeanModel;
import nl.tranquilizedquality.adm.gwt.gui.client.model.privilege.ClientPrivilege;
import nl.tranquilizedquality.adm.gwt.gui.client.model.role.ClientRole;

/**
 * Client side representation of a {@link Scope}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 */
public class ClientScope extends AbstractUpdatableBeanModel<Long> implements Scope {

    private static final long serialVersionUID = 6177065120744981278L;

    /** The unique name of the scope. */
    private String name;

    /** The description of the scope. */
    private String description;

    /** The available privileges tied to this scope. */
    private Set<Privilege> privileges;

    /** The roles tied to this scope. */
    protected Set<Role> roles;

    /**
     * Default constructor.
     */
    public ClientScope() {
        roles = new HashSet<Role>();
        privileges = new HashSet<Privilege>();
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<Privilege> getPrivileges() {
        return Collections.unmodifiableSet(privileges);
    }

    @Override
    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    public void removeRole(final ClientRole role) {
        roles.remove(role);
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * @param roles
     *            the roles to set
     */
    public void setRoles(final Set<Role> roles) {
        this.roles = roles;
    }

    /**
     * Copy the fields of the given {@link Scope} object into this object. For
     * each collection field it will add an empty collection.
     * 
     * @param scope
     *            the origin object.
     */
    public void shallowCopy(final Scope scope) {
        super.copy(scope);

        this.name = scope.getName();
        this.description = scope.getDescription();

        this.roles = new HashSet<Role>();
        this.privileges = new HashSet<Privilege>();
    }

    @Override
    public void copy(final DomainObject<Long> object) {
        if (object instanceof Scope) {
            final Scope scope = (Scope) object;
            super.copy(scope);

            this.name = scope.getName();
            this.description = scope.getDescription();

            /*
             * Create client side privilege objects.
             */
            final Set<Privilege> originalPrivileges = scope.getPrivileges();
            if (originalPrivileges != null) {
                for (final Privilege privilege : originalPrivileges) {
                    final ClientPrivilege clientPrivilege = new ClientPrivilege();
                    clientPrivilege.shallowCopy(privilege);

                    privileges.add(clientPrivilege);
                }
            }

            /*
             * Create client side role objects.
             */
            final Set<Role> originalRoles = scope.getRoles();
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

        if (obj instanceof ClientScope) {
            final ClientScope scope = (ClientScope) obj;

            if (this.id != null && !this.id.equals(scope.getId())) {
                return false;
            } else if (this.id == null && scope.getId() != null) {
                return false;
            }

            if (this.name != null && !this.name.equals(scope.getName())) {
                return false;
            } else if (this.name == null && scope.getName() != null) {
                return false;
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + (name == null ? 0 : name.hashCode());
        result = prime * result + (id == null ? 0 : id.intValue());

        return result;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
