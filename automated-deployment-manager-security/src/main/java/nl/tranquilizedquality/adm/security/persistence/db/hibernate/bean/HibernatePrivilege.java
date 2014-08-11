package nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import nl.tranquilizedquality.adm.commons.business.domain.Privilege;
import nl.tranquilizedquality.adm.commons.business.domain.Role;
import nl.tranquilizedquality.adm.commons.business.domain.Scope;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.hibernate.bean.AbstractUpdatableDomainObject;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;

import com.google.code.simplestuff.annotation.BusinessField;

/**
 * Hibernate implementation of a privilege.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 24 nov. 2011
 */
@Entity
@Table(name = "ADM_PRIVILEGES", schema = "SEC")
public class HibernatePrivilege extends AbstractUpdatableDomainObject<Long> implements Privilege {
    /**
	 * 
	 */
    private static final long serialVersionUID = -2833572827686745304L;

    /** The name of the privilege. */
    @BusinessField
    private String name;

    /** The description of this privilege. */
    @BusinessField
    private String description;

    /** Deterimines if this privilege is still valid. */
    @BusinessField
    private Boolean valid;

    /** The scope this privilege is part of. */
    @BusinessField
    private Scope scope;

    /** The roles where this privilege is used in. */
    private Set<Role> roles;

    /**
     * @return the id
     */
    @Override
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "PRIVILEGES_SEQUENCE_GEN", initialValue = 1, allocationSize = 1, sequenceName = "SEC.PRIVILEGES_SEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "PRIVILEGES_SEQUENCE_GEN")
    public Long getId() {
        return id;
    }

    @Override
    @Index(name = "IDX_PRIVILEGE_NAME")
    @Column(name = "NAME", unique = false, nullable = false)
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
    @Type(type = "yes_no")
    @Index(name = "IDX_PRIVILEGE_VALID")
    @Column(name = "VALID", unique = false, nullable = false)
    public Boolean isValid() {
        return valid;
    }

    @Transient
    // Used by equals()
    public Boolean getValid() {
        return valid;
    }

    @Override
    public void setValid(final Boolean valid) {
        this.valid = valid;
    }

    @Override
    @ManyToOne(targetEntity = HibernateScope.class)
    @JoinColumn(name = "SCP_ID", nullable = false)
    @ForeignKey(name = "FK_SCOPE_PRIVILEGE")
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
    @ManyToMany(mappedBy = "privileges", targetEntity = HibernateRole.class)
    @ForeignKey(name = "FK_PRIVILEGE_ROLE")
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

    @Override
    public void copy(final DomainObject<Long> object) {
        super.copy(object);

        if (object instanceof Privilege) {
            final Privilege privilege = (Privilege) object;
            this.name = privilege.getName();
            this.description = privilege.getDescription();
            this.valid = privilege.isValid();

            final Scope originalScope = privilege.getScope();
            if (originalScope != null) {
                this.scope = new HibernateScope();
                this.scope.copy(originalScope);
            }
            else {
                this.scope = null;
            }

            final Set<Role> originalRoles = privilege.getRoles();
            this.roles = new HashSet<Role>();
            if (originalRoles != null) {
                for (final Role role : originalRoles) {
                    final HibernateRole hibernateRole = new HibernateRole();
                    hibernateRole.copy(role);

                    this.roles.add(hibernateRole);
                }
            }
        }
    }

}
