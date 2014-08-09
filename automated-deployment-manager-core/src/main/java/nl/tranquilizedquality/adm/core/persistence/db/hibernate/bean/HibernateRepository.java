package nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import nl.tranquilizedquality.adm.commons.business.domain.Repository;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.hibernate.bean.AbstractUpdatableDomainObject;

import org.hibernate.annotations.Index;

import com.google.code.simplestuff.annotation.BusinessField;

/**
 * Hibernate implementation of a repository.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jun. 2011
 */
@Entity()
@Table(name = "REPOSITORIES", schema = "ADM")
public class HibernateRepository extends AbstractUpdatableDomainObject<Long> implements Repository {

    /**
     * Unique identifier used for serialization.
     */
    private static final long serialVersionUID = 1365511299700579578L;

    /** The name of the repository. */
    @BusinessField
    private String name;

    /** The URL that points to the repository or the Sonatype Nexus API URL. */
    @BusinessField
    private String repositoryUrl;

    /**
     * The unique name that is used in Sonatype Nexus to identify a repository e.g. publi-snapshots,
     * releases, snapshots etc.
     */
    @BusinessField
    private String repositoryId;

    /** Determines if this repository is to be used or not. */
    @BusinessField
    private Boolean enabled;

    @Override
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "REPOSITORIES_SEQ_GEN")
    @SequenceGenerator(name = "REPOSITORIES_SEQ_GEN", initialValue = 1, allocationSize = 1, sequenceName = "ADM.REPOSITORIES_SEQ")
    public Long getId() {
        return id;
    }

    /**
     * @see nl.tranquilizedquality.adm.commons.business.domain.Repository#getName()
     */
    @Override
    @Index(name = "REPOSITORY_NAME_IDX")
    @Column(name = "NAME", nullable = false, length = 100)
    public String getName() {
        return name;
    }

    /**
     * @param name
     *        the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @see nl.tranquilizedquality.adm.commons.business.domain.Repository#getRepositoryUrl()
     */
    @Override
    @Column(name = "REPOSITORY_URL", nullable = false, length = 255)
    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public void setRepositoryUrl(final String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    @Override
    @Index(name = "REPOSITORY_ENABLED_IDX")
    @Column(name = "ENABLED", nullable = false)
    public Boolean isEnabled() {
        return enabled;
    }

    /**
     * @return the enabled
     */
    @Transient
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * @param enabled
     *        the enabled to set
     */
    public void setEnabled(final Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    @Column(name = "REPOSITORY_ID", nullable = true)
    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(final String repositoryId) {
        this.repositoryId = repositoryId;
    }

    @Override
    public void copy(final DomainObject<Long> object) {
        super.copy(object);

        if (object instanceof Repository) {
            final Repository repo = (Repository) object;

            this.enabled = repo.isEnabled();
            this.name = repo.getName();
            this.repositoryUrl = repo.getRepositoryUrl();
            this.repositoryId = repo.getRepositoryId();
        }
    }

}
