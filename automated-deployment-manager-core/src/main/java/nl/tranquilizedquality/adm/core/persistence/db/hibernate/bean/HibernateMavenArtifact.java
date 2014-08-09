package nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.MavenModule;
import nl.tranquilizedquality.adm.commons.business.domain.Release;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.hibernate.bean.AbstractUpdatableDomainObject;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUserGroup;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

import com.google.code.simplestuff.annotation.BusinessField;

/**
 * Hibernate implementation of a Maven module instance.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jun. 2011
 */
@Entity()
@Table(name = "MAVEN_ARTIFACTS", schema = "ADM")
@Inheritance(strategy = InheritanceType.JOINED)
public class HibernateMavenArtifact extends AbstractUpdatableDomainObject<Long> implements MavenArtifact {

    /**
     * 
     */
    private static final long serialVersionUID = 8159541019397512652L;

    /** The version of the module. */
    @BusinessField
    private String version;

    /** The parent module. */
    @BusinessField
    private MavenModule parentModule;

    /** The release where this artifact is part of. */
    @BusinessField
    private Release release;

    @BusinessField
    /** Determines if the target system should be stopped before deployment. */
    private Boolean targetSystemShutdown;

    @BusinessField
    /** Determines if the target system should be started up after deployment. */
    private Boolean targetSystemStartup;

    /**
     * The rank in a release to determine in which order artifacts are deployed.
     */
    @BusinessField
    private Integer rank;

    /** The file name. */
    private String file;

    /** The group where this maven artifacts belongs to */
    @BusinessField
    private UserGroup userGroup;

    @Override
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "MAVEN_ARTIFACTS_SEQ_GEN")
    @SequenceGenerator(name = "MAVEN_ARTIFACTS_SEQ_GEN", initialValue = 1, allocationSize = 1, sequenceName = "ADM.MAVEN_ARTIFACTS_SEQ")
    public Long getId() {
        return id;
    }

    /**
     * @see nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact#getVersion()
     */
    @Override
    @Index(name = "MAR_VERSION_IDX")
    @Column(name = "MAVEN_VERSION", length = 100, nullable = false)
    public String getVersion() {
        return version;
    }

    /**
     * @param version
     *        the version to set
     */
    public void setVersion(final String version) {
        this.version = version;
    }

    /**
     * @see nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact#getParentModule()
     */
    @Override
    @ForeignKey(name = "FK_ARTIFACT_PARENT")
    @JoinColumn(name = "MAVEN_MODULE_ID")
    @ManyToOne(targetEntity = HibernateMavenModule.class, optional = false, cascade = { CascadeType.MERGE, CascadeType.PERSIST,
                                                                                       CascadeType.REFRESH })
    public MavenModule getParentModule() {
        return parentModule;
    }

    /**
     * @param parentModule
     *        the parentModule to set
     */
    public void setParentModule(final MavenModule parentModule) {
        this.parentModule = parentModule;
    }

    /**
     * @return the release
     */
    @Override
    @ForeignKey(name = "FK_ARTIFACT_RELEASE")
    @JoinColumn(name = "RELEASE_ID")
    @ManyToOne(targetEntity = HibernateRelease.class, optional = false)
    public Release getRelease() {
        return release;
    }

    /**
     * @param release
     *        the release to set
     */
    public void setRelease(final Release release) {
        this.release = release;
    }

    /**
     * @return the targetSystemShutdown
     */
    @Override
    @Column(name = "TARGET_SHUTDOWN", nullable = false)
    public Boolean getTargetSystemShutdown() {
        return targetSystemShutdown;
    }

    /**
     * @param targetSystemShutdown
     *        the targetSystemShutdown to set
     */
    public void setTargetSystemShutdown(final Boolean targetSystemShutdown) {
        this.targetSystemShutdown = targetSystemShutdown;
    }

    /**
     * @return the targetSystemStartup
     */
    @Override
    @Column(name = "TARGET_STARTUP", nullable = false)
    public Boolean getTargetSystemStartup() {
        return targetSystemStartup;
    }

    /**
     * @param targetSystemStartup
     *        the targetSystemStartup to set
     */
    public void setTargetSystemStartup(final Boolean targetSystemStartup) {
        this.targetSystemStartup = targetSystemStartup;
    }

    /**
     * @return the rank
     */
    @Override
    @Index(name = "MAR_RANK_IDX")
    @Column(name = "RANK", nullable = false)
    public Integer getRank() {
        return rank;
    }

    /**
     * @param rank
     *        the rank to set
     */
    @Override
    public void setRank(final Integer rank) {
        this.rank = rank;
    }

    /**
     * @return the file
     */
    @Override
    @Transient
    public String getFile() {
        return file;
    }

    /**
     * @param file
     *        the file to set
     */
    @Override
    public void setFile(final String file) {
        this.file = file;
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
    public void copy(final DomainObject<Long> object) {
        super.copy(object);

        if (object instanceof MavenArtifact) {
            final MavenArtifact artifact = (MavenArtifact) object;

            this.file = artifact.getFile();
            this.version = artifact.getVersion();
            this.targetSystemShutdown = artifact.getTargetSystemShutdown();
            this.targetSystemStartup = artifact.getTargetSystemStartup();
            this.rank = artifact.getRank();

            final MavenModule module = artifact.getParentModule();
            this.parentModule = new HibernateMavenModule();
            this.parentModule.copy(module);

            final Release release = artifact.getRelease();
            this.release = new HibernateRelease();
            this.release.copy(release);

            final UserGroup newUserGroup = artifact.getUserGroup();
            this.userGroup = new HibernateUserGroup();
            this.userGroup.copy(newUserGroup);
        }

    }

}
