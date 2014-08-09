package nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import nl.tranquilizedquality.adm.commons.business.domain.Privilege;
import nl.tranquilizedquality.adm.commons.business.domain.Role;
import nl.tranquilizedquality.adm.commons.business.domain.Scope;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.hibernate.bean.AbstractInsertableDomainObject;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

import com.google.code.simplestuff.annotation.BusinessField;

/**
 * Hibernate implementation of a {@link Scope}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 24 nov. 2011
 */
@Entity
@Table(name = "ADM_SCOPES", schema = "SEC")
public class HibernateScope extends AbstractInsertableDomainObject<Long> implements Scope {

    private static final long serialVersionUID = 6418221155198286115L;

    /** The name of this scope. */
    @BusinessField
    private String name;

    /** Description of this scope. */
    @BusinessField
    private String description;

    /** The privileges of this scope. */
    private Set<Privilege> privileges;

    /** The roles this scope has. */
    private Set<Role> roles;

    /**
     * @return the id
     */
    @Override
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "SCOPES_SEQ_GEN", initialValue = 1, allocationSize = 1, sequenceName = "SEC.SCOPES_SEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SCOPES_SEQ_GEN")
    public Long getId() {
        return id;
    }

    @Override
    @Index(name = "IDX_SCOPE_NAME")
    @Column(name = "NAME", unique = true, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    @Column(name = "DESCRIPTION", unique = false, nullable = true)
    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    @Override
    @OneToMany(mappedBy = "scope", targetEntity = HibernatePrivilege.class)
    public Set<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(final Set<Privilege> privileges) {
        this.privileges = privileges;
    }

    @Override
    @OneToMany(mappedBy = "scope", targetEntity = HibernateRole.class)
    @ForeignKey(name = "FK_SCOPE_ROLE")
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(final Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public void copy(final DomainObject<Long> object) {
        if (object instanceof Scope) {
            super.copy(object);

            final Scope scope = (Scope) object;
            this.name = scope.getName();
            this.description = scope.getDescription();
        }
    }
}
