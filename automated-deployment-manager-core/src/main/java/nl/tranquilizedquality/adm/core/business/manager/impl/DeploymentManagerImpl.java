package nl.tranquilizedquality.adm.core.business.manager.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.tranquilizedquality.adm.commons.business.deployer.ArtifactDeployer;
import nl.tranquilizedquality.adm.commons.business.domain.DeployStatus;
import nl.tranquilizedquality.adm.commons.business.domain.Deployer;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameter;
import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.business.domain.Environment;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifactSnapshot;
import nl.tranquilizedquality.adm.commons.business.domain.MavenModule;
import nl.tranquilizedquality.adm.commons.business.domain.Release;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecution;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseStatus;
import nl.tranquilizedquality.adm.core.business.deployer.exception.DeployException;
import nl.tranquilizedquality.adm.core.business.manager.ArtifactManager;
import nl.tranquilizedquality.adm.core.business.manager.DeploymentManager;
import nl.tranquilizedquality.adm.core.business.manager.MavenArtifactManager;
import nl.tranquilizedquality.adm.core.business.manager.NotificationManager;
import nl.tranquilizedquality.adm.core.business.manager.ReleaseHistoryManager;
import nl.tranquilizedquality.adm.core.business.manager.exception.NoDeployerFoundException;
import nl.tranquilizedquality.adm.core.persistence.dao.EnvironmentDao;
import nl.tranquilizedquality.adm.core.persistence.dao.ReleaseDao;
import nl.tranquilizedquality.adm.core.util.AdmValidate;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Deployment manager that can deploy multiple artifacts to multiple
 * destinations.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jun. 2011
 */
public class DeploymentManagerImpl extends AbstractProgressManager implements DeploymentManager {

    /** Logger for this class. */
    private static final Log LOGGER = LogFactory.getLog(DeploymentManagerImpl.class);

    /** DAO used to retrieve a release. */
    private ReleaseDao<Release> releaseDao;

    /** DAO used to retrieve the environment to deploy to. */
    private EnvironmentDao<Environment> environmentDao;

    /** Manager to manage the release history when deploying a release. */
    private ReleaseHistoryManager releaseHistoryManager;

    /** Map containing all registered deployers. */
    private Map<String, ArtifactDeployer> deployers;

    /** Manager that manages artifacts. */
    private ArtifactManager artifactManager;

    /** Manager that manages maven artifacts. */
    private MavenArtifactManager mavenArtifactManager;

    /** Manager used to send notifications when a deployment was done. */
    private NotificationManager notificationManager;

    /**
     * Default constructor.
     */
    public DeploymentManagerImpl() {
        deployers = new HashMap<String, ArtifactDeployer>();
    }

    @Override
    public void deployRelease(final Release release, final Environment environment) {
        final List<MavenArtifact> artifacts = release.getArtifacts();
        deployArtifacts(artifacts, release, environment);
    }

    @Override
    public void deployArtifact(final MavenArtifact artifact, final Environment environment) {
        final Release release = artifact.getRelease();
        final List<MavenArtifact> artifacts = new ArrayList<MavenArtifact>(1);
        artifacts.add(artifact);

        deployArtifacts(artifacts, release, environment);
    }

    @Override
    public Release deployArtifacts(final List<MavenArtifact> artifacts, final Release release, final Environment environment) {

        final ReleaseStatus releaseStatus = release.getStatus();
        AdmValidate.isTrue(releaseStatus != ReleaseStatus.DRAFT, "Trying to release a DRAFT release!");

        final boolean archived = release.isArchived();
        AdmValidate.isTrue(!archived, "Trying to release an archived release!");

        /*
         * Retrieve the release.
         */
        final Long releaseId = release.getId();
        final Release activeRelease = releaseDao.findById(releaseId);
        AdmValidate.notNull(activeRelease, "Trying to release a release that doesn't excist! Release ID: " + releaseId);

        /*
         * Retrieve the environment.
         */
        final Long environmentId = environment.getId();
        final Environment activeEnvironment = environmentDao.findById(environmentId);
        AdmValidate.notNull(activeEnvironment, "Trying to deploy a release to an environment that doesn't excist! Environment ID: "
            + environmentId);

        activeRelease.addReleasedCount();
        activeRelease.setLastReleasedDate(new Date());
        final ReleaseExecution execution = releaseHistoryManager.createHistory(activeRelease, activeEnvironment, artifacts);

        /*
         * Register step execution.
         */
        releaseHistoryManager.registerActivity(execution, "Deploying release...");

        /*
         * Register step execution.
         */
        releaseHistoryManager.registerActivity(execution, "Release found...");

        final int stepProgress = calculateStepProgress(artifacts);

        final List<MavenModule> failedToDeployMavenModules = new ArrayList<MavenModule>();

        /*
         * Deploy the artifacts by choosing the appropriate deployer.
         */
        for (final MavenArtifact mavenArtifact : artifacts) {

            addProgress(stepProgress);

            /*
             * Synchronize with database
             */
            final Long id = mavenArtifact.getId();
            final MavenArtifact releaseArtifact = mavenArtifactManager.findArtifactById(id);
            final MavenModule parentModule = releaseArtifact.getParentModule();

            /*
             * Register step execution.
             */
            String msg = "Deploying artifact " + parentModule.getName() + " version " + releaseArtifact.getVersion();
            releaseHistoryManager.registerActivity(execution, msg);
            registerActivity(msg);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(msg);
            }

            /*
             * Validate if we are allowed to deploy the artifact.
             */
            boolean dependencyFailedToDeploy = false;
            final List<MavenModule> deploymentDependencies = parentModule.getDeploymentDependencies();
            for (final MavenModule mavenModule : deploymentDependencies) {
                dependencyFailedToDeploy = failedToDeployMavenModules.contains(mavenModule);
                if (dependencyFailedToDeploy) {
                    msg = "Cannot deploy artifact because the dependent module " + mavenModule.getName() + " failed to deploy before.";

                    if (LOGGER.isErrorEnabled()) {
                        LOGGER.error(msg);
                    }

                    releaseHistoryManager.registerActivity(execution, "Deploy artifact to destination...", msg);
                    registerActivity(msg);
                    break;
                }
            }

            /*
             * Collect the deployed destinations.
             */
            List<Destination> deployedDestinations = new ArrayList<Destination>();
            if (!dependencyFailedToDeploy) {
                deployedDestinations =
                        deployArtifactToDestinations(releaseArtifact, execution, environment, activeRelease, failedToDeployMavenModules);
            }

            addProgress(stepProgress);

            /*
             * Check if artifact is deployed or not.
             */
            final boolean notDeployed = deployedDestinations.isEmpty();
            final ReleaseStatus status = activeRelease.getStatus();

            if (notDeployed || ReleaseStatus.FAILED.equals(status)) {
                msg = "Couldn't deploy artifact to " + environment.getName();
                if (LOGGER.isWarnEnabled()) {
                    LOGGER.warn(msg);
                }

                releaseHistoryManager.registerActivity(execution, "Failed to deploy artifact " + parentModule.getName(), msg);
                registerActivity(msg);

                /*
                 * Set release status.
                 */
                activeRelease.setStatus(ReleaseStatus.FAILED);
            }
        }

        final String msg = "Finishing up deployment...";
        registerActivity(msg);

        finalizeDeployment(environment, activeRelease, execution);

        notificationManager.sendDeploymentNotification(activeRelease, artifacts, environment);

        return activeRelease;
    }

    /**
     * Finalizes the deployment of the artifacts.
     * 
     * @param environment
     *        The environment where the release is being deployed to.
     * @param activeRelease
     *        The release that was deployed.
     * @param execution
     *        The release execution.
     */
    private void finalizeDeployment(final Environment environment, final Release activeRelease, final ReleaseExecution execution) {
        if (ReleaseStatus.FAILED.equals(activeRelease.getStatus())) {
            activeRelease.addReleaseFailureCount();
        }

        releaseDao.save(activeRelease);
        releaseDao.flush();

        /*
         * Update execution status.
         */
        final ReleaseStatus status = activeRelease.getStatus();

        switch (status) {
            case FAILED:
                releaseHistoryManager.registerActivity(execution, "Finished deploying", DeployStatus.FAILED);
                break;

            default:
                releaseHistoryManager.registerActivity(execution, "Finished deploying", DeployStatus.SUCCESS);
                break;
        }

        complete();

        initializeRelease(activeRelease);

    }

    /**
     * Calculates the amount to use per artifact for keeping track of the progress of the
     * deployment.
     * 
     * @param artifacts
     *        The artifacts that are being deployed.
     * @return Returns an integer value greater than 0.
     */
    private int calculateStepProgress(final Collection<MavenArtifact> artifacts) {
        final int numberOfArtifacts = artifacts.size();
        int progressPerArtifact = 100;
        if (numberOfArtifacts > 0) {
            progressPerArtifact = 80 / numberOfArtifacts;
        }

        final int stepProgress = progressPerArtifact / 2;
        return stepProgress;
    }

    /**
     * Deploys the specified artifact to its configured destinations.
     * 
     * @param releaseArtifact
     *        The artifact that will be deployed.
     * @param execution
     *        The release execution that is need to keep track of the release.
     * @param environment
     *        The environment where this artifact should be deployed to.
     * @param activeRelease
     *        The release that is currently being deployed.
     * @param failedToDeployMavenModules
     *        Collection containing the {@link MavenModule} objects that failed to deploy.
     * @return Returns a {@link List} containing the {@link Destination} objects where the artifact
     *         was deployed to.
     */
    private List<Destination> deployArtifactToDestinations(final MavenArtifact releaseArtifact, final ReleaseExecution execution,
            final Environment environment, final Release activeRelease, final List<MavenModule> failedToDeployMavenModules) {

        final MavenModule parentModule = releaseArtifact.getParentModule();
        final MavenArtifactSnapshot artifactSnapshot = retrieveCurrentSnapshot(execution, parentModule, releaseArtifact);
        final List<Destination> destinations = parentModule.getDestinations();
        final int numberOfAvailableDestinations = destinations.size();
        String msg = "Deploying to " + numberOfAvailableDestinations + " destination(s).";

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(msg);
        }

        if (destinations.isEmpty()) {
            releaseHistoryManager.registerActivity(execution, "Retrieved " + numberOfAvailableDestinations + " available destination(s) ",
                    "There are no destinations configured!");
            failedToDeployMavenModules.add(parentModule);
        }

        final List<Destination> deployedDestinations = new ArrayList<Destination>();
        for (final Destination destination : destinations) {

            /*
             * Check if we are supposed to deploy to the specific
             * environment or not.
             */
            final Environment targetEnvironment = destination.getEnvironment();
            if (environment.getId().equals(targetEnvironment.getId())) {

                /*
                 * Choose deployer.
                 */
                final Deployer destinationDeployer = destination.getDeployer();
                final String deployerId = destinationDeployer.getName();
                final ArtifactDeployer deployer = deployers.get(deployerId);

                msg = "Deploying to " + environment.getName() + "...";
                registerActivity(msg);

                if (deployer == null) {
                    msg = "No deployer found! -> " + deployerId;

                    if (LOGGER.isErrorEnabled()) {
                        LOGGER.error(msg);
                    }

                    releaseHistoryManager.registerActivity(execution, "Deploy artifact to destination...", msg);
                    registerActivity(msg);

                    throw new NoDeployerFoundException(msg);
                }

                try {
                    /*
                     * Retrieve artifact.
                     */
                    final File file = artifactManager.retrieveArtifact(releaseArtifact, activeRelease.getName());
                    releaseArtifact.setFile(file.getAbsolutePath());

                    /*
                     * Register step execution.
                     */
                    msg = "Retrieved artifact " + file.getName();
                    releaseHistoryManager.registerActivity(execution, msg);
                    registerActivity(msg);

                    /*
                     * Deploy the artifact.
                     */
                    final String logs = deployer.deploy(releaseArtifact, destination);

                    msg = "Finished deploying " + parentModule.getName();
                    registerActivity(msg);

                    /*
                     * Store the logs of an execution.
                     */
                    releaseHistoryManager.registerLogs(execution, artifactSnapshot, logs);

                    /*
                     * Register step execution.
                     */
                    msg = "Artifact " + parentModule.getName() + " deployed to " + environment.getName();
                    releaseHistoryManager.registerActivity(execution, msg);
                    registerActivity(msg);

                    /*
                     * Set release status.
                     */
                    activeRelease.setStatus(ReleaseStatus.SUCCESS);

                    deployedDestinations.add(destination);
                } catch (final Exception e) {
                    msg = "Failed to deploy artifact!!";
                    if (LOGGER.isErrorEnabled()) {
                        LOGGER.error(msg, e);
                    }

                    String errorMessage = e.getMessage();
                    final Throwable cause = e.getCause();
                    if (cause != null) {
                        errorMessage = cause.getMessage();
                    }

                    releaseHistoryManager.registerActivity(execution, msg, errorMessage);
                    registerActivity(msg);

                    if (e instanceof DeployException) {
                        final DeployException deployException = (DeployException) e;
                        final String logs = deployException.getLogs();

                        /*
                         * Store the logs of an execution.
                         */
                        releaseHistoryManager.registerLogs(execution, artifactSnapshot, logs);
                    }

                    /*
                     * Set release status.
                     */
                    activeRelease.setStatus(ReleaseStatus.FAILED);

                    failedToDeployMavenModules.add(parentModule);

                    /*
                     * Stop deployment.
                     */
                    break;
                }
            } else {
                /*
                 * Register step execution.
                 */
                msg = "Artifact " + parentModule.getName() + " will NOT be deployed to " + targetEnvironment.getName();
                releaseHistoryManager.registerActivity(execution, msg);

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(msg);
                }
            }
        }

        return deployedDestinations;
    }

    /**
     * Initializes the specified release so all information can be shown by the calling code.
     * 
     * @param activeRelease
     *        The release that will be initialized.
     */
    private void initializeRelease(final Release activeRelease) {
        final List<MavenArtifact> releaseArtifacts = activeRelease.getArtifacts();
        for (final MavenArtifact mavenArtifact : releaseArtifacts) {
            final MavenModule parentModule = mavenArtifact.getParentModule();
            final List<Destination> destinations = parentModule.getDestinations();
            for (final Destination destination : destinations) {
                final List<DeployerParameter> deployerParameters = destination.getDeployerParameters();
                for (final DeployerParameter destinationLocation : deployerParameters) {
                    destinationLocation.getId();
                }
            }
        }
    }

    /**
     * Retrieves the current snapshot that is being released.
     * 
     * @param execution
     *        The release execution.
     * @param releaseArtifactParentModule
     *        The parent module where some data will be retrieved from to identify the snapshot
     *        artifact.
     * @param releaseArtifact
     *        The artifact that is going to be released.
     * @return Returns the {@link MavenArtifactSnapshot}.
     */
    private MavenArtifactSnapshot retrieveCurrentSnapshot(final ReleaseExecution execution, final MavenModule releaseArtifactParentModule,
            final MavenArtifact releaseArtifact) {
        MavenArtifactSnapshot artifactSnapshot = null;
        final List<MavenArtifactSnapshot> snapshots = execution.getArtifacts();
        for (final MavenArtifactSnapshot mavenArtifactSnapshot : snapshots) {
            final String snapshotArtifactId = mavenArtifactSnapshot.getArtifactId();
            final String snapshotGroup = mavenArtifactSnapshot.getGroup();
            final String snapshotVersion = mavenArtifactSnapshot.getVersion();
            final String snapshotIdentifier = mavenArtifactSnapshot.getIdentifier();

            final String artifactId = releaseArtifactParentModule.getArtifactId();
            final String group = releaseArtifactParentModule.getGroup();
            final String version = releaseArtifact.getVersion();
            final String identifier = releaseArtifactParentModule.getIdentifier();
            if (StringUtils.equals(snapshotArtifactId, artifactId) && StringUtils.equals(snapshotGroup, group)
                && StringUtils.equals(snapshotVersion, version) && StringUtils.equals(snapshotIdentifier, identifier)) {
                artifactSnapshot = mavenArtifactSnapshot;
                break;
            }
        }

        return artifactSnapshot;
    }

    /**
     * @param releaseDao
     *        the releaseDao to set
     */
    @Required
    public void setReleaseDao(final ReleaseDao<Release> releaseDao) {
        this.releaseDao = releaseDao;
    }

    /**
     * @param deployers
     *        the deployers to set
     */
    @Required
    public void setDeployers(final Map<String, ArtifactDeployer> deployers) {
        this.deployers = deployers;
    }

    /**
     * @param artifactManager
     *        the artifactManager to set
     */
    @Required
    public void setArtifactManager(final ArtifactManager artifactManager) {
        this.artifactManager = artifactManager;
    }

    /**
     * @param releaseHistoryManager
     *        the releaseHistoryManager to set
     */
    @Required
    public void setReleaseHistoryManager(final ReleaseHistoryManager releaseHistoryManager) {
        this.releaseHistoryManager = releaseHistoryManager;
    }

    /**
     * @param mavenArtifactManager
     *        the mavenArtifactManager to set
     */
    @Required
    public void setMavenArtifactManager(final MavenArtifactManager mavenArtifactManager) {
        this.mavenArtifactManager = mavenArtifactManager;
    }

    @Required
    public void setEnvironmentDao(final EnvironmentDao<Environment> environmentDao) {
        this.environmentDao = environmentDao;
    }

    @Required
    public void setNotificationManager(final NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
    }

}
