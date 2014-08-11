package nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import nl.tranquilizedquality.adm.commons.business.domain.ArtifactType;
import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.business.domain.MavenModule;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.hibernate.bean.AbstractUpdatableDomainObject;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUserGroup;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

import com.google.code.simplestuff.annotation.BusinessField;

/**
 * Hibernate implementation of a maven module.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jun. 2011
 */
@Entity()
@Table(name = "MAVEN_MODULES", schema = "ADM")
public class HibernateMavenModule extends AbstractUpdatableDomainObject<Long> implements MavenModule {

    /**
     * 
     */
    private static final long serialVersionUID = 8355418337665490568L;

    /** The name of the Maven module. */
    @BusinessField
    private String name;

    /** The type of artifact. */
    @BusinessField
    private ArtifactType type;

    /** The maven group of the module. */
    @BusinessField
    private String group;

    /** The maven artifact id. */
    @BusinessField
    private String artifactId;

    /**
     * Is used for distribution packages since they have a suffix to uniquely
     * identify the distribution package.
     */
    @BusinessField
    private String identifier;

    /** The destination where the artifact should be deployed to. */
    @BusinessField
    private List<Destination> destinations;

    @BusinessField
    /** Determines if the target system should be stopped before deployment. */
    private Boolean targetSystemShutdown;

    @BusinessField
    /** Determines if the target system should be started up after deployment. */
    private Boolean targetSystemStartup;

    /** The group where this maven module belongs to */
    @BusinessField
    private UserGroup userGroup;

    /** The modules where this module depend on for succesful deployment. */
    private List<MavenModule> deploymentDependencies;

    /**
     * Default constructor.
     */
    public HibernateMavenModule() {
        deploymentDependencies = new ArrayList<MavenModule>();
    }

    @Override
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "MAVEN_MODULES_SEQ_GEN")
    @SequenceGenerator(name = "MAVEN_MODULES_SEQ_GEN", initialValue = 1, allocationSize = 1, sequenceName = "ADM.MAVEN_MODULES_SEQ")
    public Long getId() {
        return id;
    }

    /**
     * @see nl.tranquilizedquality.adm.commons.business.domain.MavenModule#getName()
     */
    @Override
    @Index(name = "MMO_NAME_IDX")
    @Column(name = "NAME", length = 200, nullable = false, unique = false)
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the type
     */
    @Override
    @Enumerated(EnumType.STRING)
    @Index(name = "MMO_ARTIFACT_TYPE_IDX")
    @Column(name = "ARTIFACT_TYPE", nullable = false, length = 10)
    public ArtifactType getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(final ArtifactType type) {
        this.type = type;
    }

    /**
     * @see nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact#getGroup()
     */
    @Override
    @Index(name = "MMO_MAVEN_GROUP_IDX")
    @Column(name = "MAVEN_GROUP", nullable = false, length = 200)
    public String getGroup() {
        return group;
    }

    /**
     * @param group
     *            the group to set
     */
    public void setGroup(final String group) {
        this.group = group;
    }

    /**
     * @see nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact#getArtifactId()
     */
    @Override
    @Index(name = "MMO_ARTIFACT_ID_IDX")
    @Column(name = "MAVEN_ARTIFACT_ID", nullable = false, length = 200)
    public String getArtifactId() {
        return artifactId;
    }

    /**
     * @param artifactId
     *            the artifactId to set
     */
    public void setArtifactId(final String artifactId) {
        this.artifactId = artifactId;
    }

    /**
     * @return the destination
     */
    @Override
    @ForeignKey(name = "FK_MAVEN_MODULE_DESTINATIONS", inverseName = "FK_DESTINATION_MODULES")
    @JoinTable(name = "ADM.MAVEN_MODULE_DESTINATIONS", joinColumns = {@JoinColumn(referencedColumnName = "ID") }, inverseJoinColumns = {@JoinColumn(referencedColumnName = "ID") }, uniqueConstraints = {@UniqueConstraint(columnNames = {
            "DESTINATIONS_ID",
            "MAVEN_MODULES_ID" }) })
    @ManyToMany(targetEntity = HibernateDestination.class, cascade = CascadeType.ALL)
    public List<Destination> getDestinations() {
        return destinations;
    }

    /**
     * @param destination
     *            the destination to set
     */
    @Override
    public void setDestinations(final List<Destination> destinations) {
        this.destinations = destinations;
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
     *            the targetSystemShutdown to set
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
     *            the targetSystemStartup to set
     */
    public void setTargetSystemStartup(final Boolean targetSystemStartup) {
        this.targetSystemStartup = targetSystemStartup;
    }

    /**
     * @return the identifier
     */
    @Override
    @Column(name = "SUFFIX", nullable = true, length = 100)
    public String getIdentifier() {
        return identifier;
    }

    /**
     * @param identifier
     *            the identifier to set
     */
    public void setIdentifier(final String identifier) {
        this.identifier = identifier;
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
    @JoinTable(name = "DEPLOYMENT_DEPENDENCIES", schema = "ADM", joinColumns = @JoinColumn(name = "MAVEN_MODULE_ID"), inverseJoinColumns = @JoinColumn(name = "DEPENDENCY_MODULE_ID"))
    @ManyToMany(targetEntity = HibernateMavenModule.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<MavenModule> getDeploymentDependencies() {
        return deploymentDependencies;
    }

    @Override
    public void setDeploymentDependencies(final List<MavenModule> deploymentDependencies) {
        this.deploymentDependencies = deploymentDependencies;
    }

    @Override
    public void copy(final DomainObject<Long> object) {
        super.copy(object);

        if (object instanceof MavenModule) {
            final MavenModule module = (MavenModule) object;
            this.name = module.getName();
            this.type = module.getType();
            this.artifactId = module.getArtifactId();
            this.group = module.getGroup();
            this.targetSystemShutdown = module.getTargetSystemShutdown();
            this.targetSystemStartup = module.getTargetSystemStartup();
            this.identifier = module.getIdentifier();

            final UserGroup newUserGroup = module.getUserGroup();
            this.userGroup = new HibernateUserGroup();
            this.userGroup.copy(newUserGroup);

            final List<Destination> destinations = module.getDestinations();
            if (destinations != null) {
                this.destinations = new ArrayList<Destination>();
                for (final Destination destination : destinations) {
                    final HibernateDestination newDestination = new HibernateDestination();
                    newDestination.copy(destination);

                    this.destinations.add(newDestination);
                }
            }

            final List<MavenModule> dependencies = module.getDeploymentDependencies();
            this.deploymentDependencies = new ArrayList<MavenModule>();
            if (dependencies != null) {
                for (final MavenModule mavenModule : dependencies) {
                    final HibernateMavenModule newMavenModule = new HibernateMavenModule();
                    newMavenModule.copy(mavenModule);
                    this.deploymentDependencies.add(newMavenModule);
                }
            }
        }
    }

}
