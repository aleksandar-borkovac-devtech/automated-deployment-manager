package nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.Release;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseStatus;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.hibernate.bean.AbstractUpdatableDomainObject;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUserGroup;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;

import com.google.code.simplestuff.annotation.BusinessField;

/**
 * Hibernate implementation of a release.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jun. 2011
 */
@Entity()
@Table(name = "RELEASES", schema = "ADM")
public class HibernateRelease extends AbstractUpdatableDomainObject<Long> implements Release {

    /**
     * Unique identifier for serialization.
     */
    private static final long serialVersionUID = -8356307641818529320L;

    /** The name of the release. */
    @BusinessField
    private String name;

    /** The release date. */
    @BusinessField
    private Date releaseDate;

    /** The status of the release. */
    @BusinessField
    private ReleaseStatus status;

    /** The number of times the release was released. */
    @BusinessField
    private Integer releaseCount;

    /** The number of times the release failed to release. */
    @BusinessField
    private Integer releaseFailureCount;

    /** Date on which this release was released the last time. */
    @BusinessField
    private Date lastReleasedDate;

    /** The group where this release belongs to */
    @BusinessField
    private UserGroup userGroup;

    /** Determines if the release is archived or not. */
    @BusinessField
    private boolean archived;

    /** The artifacts that should be deployed. */
    private List<MavenArtifact> artifacts;

    /**
     * Default constructor.
     */
    public HibernateRelease() {
        status = ReleaseStatus.DRAFT;
        releaseCount = 0;
        releaseFailureCount = 0;
        artifacts = new ArrayList<MavenArtifact>();
    }

    @Override
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "RELEASES_SEQ_GEN")
    @SequenceGenerator(name = "RELEASES_SEQ_GEN", initialValue = 1, allocationSize = 1, sequenceName = "ADM.RELEASES_SEQ")
    public Long getId() {
        return id;
    }

    /**
     * @see nl.tranquilizedquality.adm.commons.business.domain.Release#getName()
     */
    @Override
    @Index(name = "RELEASE_NAME_IDX")
    @Column(name = "NAME", nullable = false, length = 200, unique = true)
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
     * @see nl.tranquilizedquality.adm.commons.business.domain.Release#getReleaseDate()
     */
    @Override
    @Index(name = "RELEASE_DATE_IDX")
    @Column(name = "RELEASE_DATE", nullable = false)
    public Date getReleaseDate() {
        return releaseDate;
    }

    /**
     * @param releaseDate
     *        the releaseDate to set
     */
    public void setReleaseDate(final Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    /**
     * @return the artifacts
     */
    @Override
    @OrderBy(value = "rank")
    @OneToMany(targetEntity = HibernateMavenArtifact.class, mappedBy = "release", cascade = CascadeType.REMOVE)
    public List<MavenArtifact> getArtifacts() {
        return artifacts;
    }

    /**
     * @param artifacts
     *        the artifacts to set
     */
    @Override
    public void setArtifacts(final List<MavenArtifact> artifacts) {
        this.artifacts = artifacts;
    }

    /**
     * @return the status
     */
    @Override
    @Enumerated(EnumType.STRING)
    @Index(name = "RELEASE_STATUS_IDX")
    @Column(name = "STATUS", nullable = false, length = 10)
    public ReleaseStatus getStatus() {
        return status;
    }

    /**
     * @param status
     *        the status to set
     */
    @Override
    public void setStatus(final ReleaseStatus status) {
        this.status = status;
    }

    /**
     * @return the releaseCount
     */
    @Override
    @Column(name = "RELEASE_COUNT", nullable = true)
    public Integer getReleaseCount() {
        return releaseCount;
    }

    /**
     * @param releaseCount
     *        the releaseCount to set
     */
    public void setReleaseCount(final Integer releaseCount) {
        this.releaseCount = releaseCount;
    }

    /**
     * @return the releaseFailureCount
     */
    @Override
    @Column(name = "RELEASE_FAILURE_COUNT", nullable = true)
    public Integer getReleaseFailureCount() {
        return releaseFailureCount;
    }

    /**
     * @param releaseFailureCount
     *        the releaseFailureCount to set
     */
    public void setReleaseFailureCount(final Integer releaseFailureCount) {
        this.releaseFailureCount = releaseFailureCount;
    }

    /**
     * @return the lastReleasedDate
     */
    @Override
    @Column(name = "LAST_RELEASED_DATE", nullable = true)
    public Date getLastReleasedDate() {
        return lastReleasedDate;
    }

    @Override
    public void addReleasedCount() {
        if (this.releaseCount == null) {
            this.releaseCount = 1;
        } else {
            this.releaseCount += 1;
        }
    }

    @Override
    public void addReleaseFailureCount() {
        if (this.releaseFailureCount == null) {
            this.releaseFailureCount = 1;
        } else {
            this.releaseFailureCount += 1;
        }
    }

    /**
     * @param lastReleasedDate
     *        the lastReleasedDate to set
     */
    @Override
    public void setLastReleasedDate(final Date lastReleasedDate) {
        this.lastReleasedDate = lastReleasedDate;
    }

    @Override
    @JoinColumn(name = "USER_GROUP_ID")
    @ManyToOne(targetEntity = HibernateUserGroup.class, optional = false)
    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(final UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    @Override
    @Type(type = "yes_no")
    @Column(name = "ARCHIVED", nullable = false)
    public boolean isArchived() {
        return archived;
    }

    public void setArchived(final boolean archived) {
        this.archived = archived;
    }

    @Override
    public void copy(final DomainObject<Long> object) {
        if (object instanceof Release) {
            super.copy(object);
            final Release release = (Release) object;
            this.lastReleasedDate = release.getLastReleasedDate();
            this.name = release.getName();
            this.releaseCount = release.getReleaseCount();
            this.releaseDate = release.getReleaseDate();
            this.releaseFailureCount = release.getReleaseFailureCount();
            this.status = release.getStatus();
            this.archived = release.isArchived();

            final UserGroup newUserGroup = release.getUserGroup();
            this.userGroup = new HibernateUserGroup();
            this.userGroup.copy(newUserGroup);

            this.artifacts = new ArrayList<MavenArtifact>();
            final List<MavenArtifact> artifacts = release.getArtifacts();
            for (final MavenArtifact mavenArtifact : artifacts) {
                final HibernateMavenArtifact artifact = new HibernateMavenArtifact();
                artifact.copy(mavenArtifact);

                this.artifacts.add(artifact);
            }
        }
    }

}
