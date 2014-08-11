package nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import nl.tranquilizedquality.adm.commons.business.domain.Deployer;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameter;
import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.business.domain.DestinationHost;
import nl.tranquilizedquality.adm.commons.business.domain.Environment;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.hibernate.bean.AbstractUpdatableDomainObject;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUserGroup;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

import com.google.code.simplestuff.annotation.BusinessField;

/**
 * Hibernate implementation of a destination for a deployment.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jun. 2011
 */
@Entity()
@Table(name = "DESTINATIONS", schema = "ADM")
public class HibernateDestination extends AbstractUpdatableDomainObject<Long> implements Destination {

    /**
     * 
     */
    private static final long serialVersionUID = -4784468128179706512L;

    /** The name of the destination. */
    @BusinessField
    private String name;

    /**
     * The unique identifier of a the deployer that should be used to deploy an
     * artifact to this destination.
     */
    @BusinessField
    private Deployer deployer;

    /** The environment this destination represents. */
    @BusinessField
    private Environment environment;

    /** The server to deploy to. */
    @BusinessField
    private DestinationHost destinationHost;

    /** The group where this destination belongs to */
    @BusinessField
    private UserGroup userGroup;

    /**
     * The prefix that will be used when retrieving configuration files from a
     * distribution module.
     */
    @BusinessField
    private String prefix;

    /**
     * Parameters that should be used for the deployer that is configured for
     * this destination.
     */
    private List<DeployerParameter> deployerParameters;

    /**
     * Default constructor.
     */
    public HibernateDestination() {
        deployerParameters = new ArrayList<DeployerParameter>();
    }

    @Override
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "DESTINATIONS_SEQ_GEN")
    @SequenceGenerator(name = "DESTINATIONS_SEQ_GEN", initialValue = 1, allocationSize = 1, sequenceName = "ADM.DESTINATIONS_SEQ")
    public Long getId() {
        return id;
    }

    @Override
    @Index(name = "DESTINATION_NAME_IDX")
    @Column(name = "NAME", nullable = false, length = 256)
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the deployerId
     */
    @Override
    @ForeignKey(name = "FK_DESTINATION_DEPLOYER")
    @JoinColumn(name = "DEPLOYER_ID")
    @ManyToOne(targetEntity = HibernateDeployer.class, optional = false)
    public Deployer getDeployer() {
        return deployer;
    }

    /**
     * @param deployerId
     *            the deployerId to set
     */
    public void setDeployer(final Deployer deployer) {
        this.deployer = deployer;
    }

    /**
     * @return the environment
     */
    @Override
    @ForeignKey(name = "FK_DESTINATION_ENVIRONMENT")
    @JoinColumn(name = "ENVIRONMENT_ID")
    @ManyToOne(targetEntity = HibernateEnvironment.class, optional = false)
    public Environment getEnvironment() {
        return environment;
    }

    /**
     * @param environment
     *            the environment to set
     */
    public void setEnvironment(final Environment environment) {
        this.environment = environment;
    }

    /**
     * @return the locations
     */
    @Override
    @ForeignKey(name = "FK_DEPLOYER_PARAMETERS_DESTINATIONS")
    @JoinColumn(name = "DESTINATION_ID", insertable = true, updatable = true, nullable = false)
    @OrderBy(value = "rank")
    @OneToMany(targetEntity = HibernateDeployerParameter.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<DeployerParameter> getDeployerParameters() {
        return deployerParameters;
    }

    /**
     * @param parameters
     *            the locations to set
     */
    @Override
    public void setDeployerParameters(final List<DeployerParameter> parameters) {
        this.deployerParameters = parameters;
    }

    /**
     * @return the destinationHost
     */
    @Override
    @ForeignKey(name = "FK_DESTINATION_HOSTS_DESTINATIONS")
    @JoinColumn(name = "DESTINATION_HOST_ID", insertable = true, updatable = true, nullable = false)
    @ManyToOne(targetEntity = HibernateDestinationHost.class, optional = false)
    public DestinationHost getDestinationHost() {
        return destinationHost;
    }

    /**
     * @param destinationHost
     *            the destinationHost to set
     */
    public void setDestinationHost(final DestinationHost destinationHost) {
        this.destinationHost = destinationHost;
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
    @Column(name = "ENVIRONMENT_PREFIX", nullable = true, unique = false)
    public String getPrefix() {
        return prefix;
    }

    /**
     * @param prefix
     *            the prefix to set
     */
    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void copy(final DomainObject<Long> object) {
        super.copy(object);

        if (object instanceof Destination) {
            final Destination destination = (Destination) object;

            final Deployer originalDeployer = destination.getDeployer();
            this.deployer = new HibernateDeployer();
            this.deployer.copy(originalDeployer);

            this.name = destination.getName();
            this.prefix = destination.getPrefix();

            this.deployerParameters = new ArrayList<DeployerParameter>();
            final List<DeployerParameter> origanalsLocations = destination.getDeployerParameters();
            for (final DeployerParameter destinationLocation : origanalsLocations) {
                final HibernateDeployerParameter location = new HibernateDeployerParameter();
                location.copy(destinationLocation);

                this.deployerParameters.add(location);
            }

            final DestinationHost destinationHost = destination.getDestinationHost();
            this.destinationHost = new HibernateDestinationHost();
            this.destinationHost.copy(destinationHost);

            final UserGroup newUserGroup = destination.getUserGroup();
            this.userGroup = new HibernateUserGroup();
            this.userGroup.copy(newUserGroup);

            final Environment originalEnvironment = destination.getEnvironment();
            this.environment = new HibernateEnvironment();
            this.environment.copy(originalEnvironment);
        }
    }
}
