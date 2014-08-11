package nl.tranquilizedquality.adm.gwt.gui.client.model.role;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.tranquilizedquality.adm.commons.business.domain.Privilege;
import nl.tranquilizedquality.adm.commons.business.domain.Role;
import nl.tranquilizedquality.adm.commons.business.domain.Scope;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.business.domain.UserRole;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.AbstractUpdatableBeanModel;
import nl.tranquilizedquality.adm.gwt.gui.client.model.privilege.ClientPrivilege;
import nl.tranquilizedquality.adm.gwt.gui.client.model.scope.ClientScope;

/**
 * Client side representation of a {@link Role}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 */
public class ClientRole extends AbstractUpdatableBeanModel<Long> implements Role {

    /**
     * Unique identifier used for serialization.
     */
    private static final long serialVersionUID = 6894409477332218039L;

    /** The name of the role. */
    private String name;

    /** The description of the role. */
    private String description;

    /** Determines if the role is valid or not. */
    private Boolean valid;

    /** Determines if this role is editable or not. */
    private Boolean frozen;

    /** The {@link Scope} where this role is bound to. */
    private Scope scope;

    /** The user roles associated with this role. */
    private List<UserRole> userRoles;

    /** The privileges this role has. */
    private Set<Privilege> privileges;

    /**
     * Default constructor.
     */
    public ClientRole() {
        valid = true;
        frozen = false;
        privileges = new HashSet<Privilege>();
        userRoles = new ArrayList<UserRole>();
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
    public void setPrivileges(final Set<Privilege> privileges) {
        this.privileges = privileges;
    }

    public void addPrivilege(final Privilege privilege) {
        this.privileges.add(privilege);
    }

    public void removePrivilege(final Privilege privilege) {
        this.privileges.remove(privilege);
    }

    @Override
    public Scope getScope() {
        return scope;
    }

    @Override
    public List<UserRole> getUserRoles() {
        return Collections.unmodifiableList(userRoles);
    }

    @Override
    public List<User> getUsers() {
        final List<User> users = new ArrayList<User>();
        for (final UserRole userRole : getUserRoles()) {
            users.add(userRole.getUser());
        }

        return users;
    }

    @Override
    public Boolean isValid() {
        return valid;
    }

    @Override
    public void setDescription(final String description) {
        this.description = description;
    }

    @Override
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public void setScope(final Scope scope) {
        this.scope = scope;
    }

    @Override
    public void setUserRoles(final List<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    @Override
    public void setValid(final Boolean valid) {
        this.valid = valid;
    }

    @Override
    public Boolean isFrozen() {
        return frozen;
    }

    @Override
    public void setFrozen(final Boolean frozen) {
        this.frozen = frozen;
    }

    /**
     * Copy the fields of the given {@link Role} object into this object. For
     * each collection field it will add an empty collection.
     * 
     * @param role
     *            the origin object.
     */
    public void shallowCopy(final Role role) {
        super.copy(role);

        this.name = role.getName();
        this.valid = role.isValid();
        this.frozen = role.isFrozen();
        this.description = role.getDescription();

        final ClientScope clientScope = new ClientScope();
        clientScope.shallowCopy(role.getScope());
        this.scope = clientScope;

        this.userRoles = new ArrayList<UserRole>();
        this.privileges = new HashSet<Privilege>();
    }

    @Override
    public void copy(final DomainObject<Long> object) {
        if (object instanceof Role) {
            final Role role = (Role) object;
            shallowCopy(role);

            /*
             * Create client side user role objects.
             */
            final List<UserRole> originalUserRoles = role.getUserRoles();
            if (originalUserRoles != null) {
                for (final UserRole userRole : originalUserRoles) {
                    final ClientUserRole clientUserRole = new ClientUserRole();
                    clientUserRole.shallowCopy(userRole);

                    userRoles.add(clientUserRole);
                }
            }

            /*
             * Create client side privilege objects.
             */
            final Set<Privilege> originalPrivileges = role.getPrivileges();
            if (originalPrivileges != null) {
                for (final Privilege privilege : originalPrivileges) {
                    final ClientPrivilege clientPrivilege = new ClientPrivilege();
                    clientPrivilege.shallowCopy(privilege);

                    privileges.add(clientPrivilege);
                }
            }

        }
    }

    @Override
    public boolean equals(final Object obj) {

        if (obj instanceof ClientRole) {
            final ClientRole role = (ClientRole) obj;

            if (this.id != null && !this.id.equals(role.getId())) {
                return false;
            } else if (this.id == null && role.getId() != null) {
                return false;
            }

            if (this.scope != null && !this.scope.equals(role.getScope())) {
                return false;
            } else if (this.scope == null && role.getScope() != null) {
                return false;
            }

            if (this.name != null && !this.name.equals(role.getName())) {
                return false;
            } else if (this.name == null && role.getName() != null) {
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

        result = prime * result + (scope == null ? 0 : scope.hashCode());
        result = prime * result + (name == null ? 0 : name.hashCode());
        result = prime * result + (id == null ? 0 : id.intValue());

        return result;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
