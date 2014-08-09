/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 11 sep. 2011 File: MavenArtifactManagerImpl.java
 * Package: nl.tranquilizedquality.adm.core.business.manager.impl
 * 
 * Copyright (c) 2011 Tranquilized Quality www.Tranquilized Quality.nl All rights
 * reserved.
 * 
 * This software is the confidential and proprietary information of Tranquilized Quality
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Tranquilized Quality.
 * </pre>
 */
package nl.tranquilizedquality.adm.core.business.manager.impl;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.command.MavenArtifactSearchCommand;
import nl.tranquilizedquality.adm.commons.business.command.MavenModuleSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameter;
import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.MavenModule;
import nl.tranquilizedquality.adm.commons.business.domain.Release;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.core.business.manager.MavenArtifactManager;
import nl.tranquilizedquality.adm.core.business.manager.exception.InvalidArtifactException;
import nl.tranquilizedquality.adm.core.business.manager.exception.NoParentModuleFoundException;
import nl.tranquilizedquality.adm.core.persistence.dao.MavenArtifactDao;
import nl.tranquilizedquality.adm.core.persistence.dao.MavenModuleDao;
import nl.tranquilizedquality.adm.core.persistence.dao.ReleaseDao;
import nl.tranquilizedquality.adm.security.business.manager.impl.UserGroupFilteringManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

/**
 * Manager that manages {@link MavenArtifact} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 11 sep. 2011
 */
public class MavenArtifactManagerImpl extends UserGroupFilteringManager implements MavenArtifactManager {

    /** Logger for this class. */
    private static final Log LOGGER = LogFactory.getLog(MavenArtifactManagerImpl.class);

    /** DAO that manages {@link MavenArtifact} objects. */
    private MavenArtifactDao<MavenArtifact> mavenArtifactDao;

    /** DAO that manages {@link MavenModule} objects. */
    private MavenModuleDao<MavenModule> mavenModuleDao;

    /** DAO that manages {@link Release} objects. */
    private ReleaseDao<Release> releaseDao;

    /** Validator that validates a {@link MavenArtifact}. */
    private Validator mavenArtifactValidator;

    /** Validator that validates a {@link MavenModule}. */
    private Validator mavenModuleValidator;

    @Override
    public List<MavenModule> findAllMavenModules() {
        final MavenModuleSearchCommand sc = new MavenModuleSearchCommand();
        addUserGroupsFromLoggedInUser(sc);

        return mavenModuleDao.findBySearchCommand(sc);
    }

    public List<MavenModule> findAvailableDependencies(final Long excludeMavenModuleId) {
        final MavenModuleSearchCommand sc = new MavenModuleSearchCommand();
        addUserGroupsFromLoggedInUser(sc);
        sc.setExcludeMavenModuleId(excludeMavenModuleId);

        return mavenModuleDao.findBySearchCommand(sc);
    }

    @Override
    public List<MavenModule> findMavenModules(final MavenModuleSearchCommand sc) {
        addUserGroupsFromLoggedInUser(sc);

        final List<MavenModule> modules = mavenModuleDao.findBySearchCommand(sc);

        for (final MavenModule mavenModule : modules) {
            final List<Destination> destinations = mavenModule.getDestinations();
            for (final Destination destination : destinations) {
                final List<DeployerParameter> deployerParameters = destination.getDeployerParameters();
                for (final DeployerParameter parameter : deployerParameters) {
                    parameter.getId();
                }
            }
        }

        return modules;
    }

    @Override
    public int findNumberOfMavenModules(final MavenModuleSearchCommand sc) {
        addUserGroupsFromLoggedInUser(sc);
        return mavenModuleDao.findNumberOfMavenArtifacts(sc);
    }

    @Override
    public MavenArtifact findArtifactById(final Long id) {
        final MavenArtifact artifact = mavenArtifactDao.findById(id);

        final MavenModule parentModule = artifact.getParentModule();
        final List<Destination> destinations = parentModule.getDestinations();
        for (final Destination destination : destinations) {
            final List<DeployerParameter> getDeployerParameters = destination.getDeployerParameters();
            for (final DeployerParameter parameter : getDeployerParameters) {
                parameter.getId();
            }
        }

        return artifact;
    }

    @Override
    public MavenArtifact storeMavenArtifact(final MavenArtifact artifact, final Errors errors) {
        if (artifact == null) {
            final String msg = "No artifact specified!";
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }

            throw new InvalidArtifactException(msg);
        }

        /*
         * Validate the artifact.
         */
        final MavenModule parentModule = artifact.getParentModule();

        /*
         * Check if there is a parent module. If not we can't store the
         * specified artifact.
         */
        if (parentModule == null) {
            final String msg = "No parent module specified!";
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }

            throw new NoParentModuleFoundException(msg);
        }

        mavenArtifactValidator.validate(artifact, errors);

        /*
         * Check for errors.
         */
        if (errors.hasErrors()) {
            final String msg = "Invalid artifact.. " + parentModule.getArtifactId();

            if (LOGGER.isDebugEnabled()) {
                final List<ObjectError> allErrors = errors.getAllErrors();
                for (final ObjectError objectError : allErrors) {
                    LOGGER.debug(objectError.getDefaultMessage());
                }
            }

            throw new InvalidArtifactException(msg);
        }

        /*
         * Check if we are doing an insert or an update.
         */
        if (artifact.isPersistent()) {
            /*
             * Create supported domain object
             */
            final MavenArtifact original = mavenArtifactDao.findById(artifact.getId());
            original.copy(artifact);

            final MavenArtifact savedMavenArtifact = mavenArtifactDao.save(original);
            return savedMavenArtifact;
        } else {
            /*
             * Determine rank
             */
            final Release release = artifact.getRelease();
            releaseDao.refresh(release);

            final List<MavenArtifact> artifacts = release.getArtifacts();

            int maxRank = 0;
            if (artifacts != null && !artifacts.isEmpty()) {
                maxRank = mavenArtifactDao.findMaximumRankInRelease(release);
            }
            final int artifactRank = maxRank + 1;
            artifact.setRank(artifactRank);

            final UserGroup parentUserGroup = parentModule.getUserGroup();
            artifact.setUserGroup(parentUserGroup);

            final MavenArtifact savedMavenArtifact = mavenArtifactDao.save(artifact);
            return savedMavenArtifact;
        }
    }

    private void initializeUserGroup(final UserGroup userGroup) {
        final List<User> users = userGroup.getUsers();
        for (final User user : users) {
            user.getId();
        }
    }

    @Override
    public void removeMavenArtifact(final MavenArtifact artifact) {
        if (artifact == null) {
            final String msg = "No artifact specified!";
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }

            throw new InvalidArtifactException(msg);
        }

        if (!artifact.isPersistent()) {
            final String msg = "Can't delete a transient artifact!";
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }

            throw new InvalidArtifactException(msg);
        }

        mavenArtifactDao.delete(artifact);
    }

    @Override
    public MavenModule storeMavenModule(final MavenModule module, final Errors errors) {
        if (module == null) {
            final String msg = "No module specified!";
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }

            throw new InvalidArtifactException(msg);
        }

        /*
         * Validate the artifact.
         */
        mavenModuleValidator.validate(module, errors);

        /*
         * Check for errors.
         */
        if (errors.hasErrors()) {
            final String msg = "Invalid maven modole.. " + module.getArtifactId();

            if (LOGGER.isDebugEnabled()) {
                final List<ObjectError> allErrors = errors.getAllErrors();
                for (final ObjectError objectError : allErrors) {
                    LOGGER.debug(objectError.getDefaultMessage());
                }
            }

            throw new InvalidArtifactException(msg);
        }

        /*
         * Check if we are doing an insert or an update.
         */
        if (module.isPersistent()) {
            /*
             * Create supported domain object
             */
            final MavenModule original = mavenModuleDao.findById(module.getId());
            original.copy(module);

            final MavenModule savedMavenModule = mavenModuleDao.save(original);
            final UserGroup userGroup = savedMavenModule.getUserGroup();
            initializeUserGroup(userGroup);
            return savedMavenModule;
        } else {
            final MavenModule savedMavenModule = mavenModuleDao.save(module);
            final UserGroup userGroup = savedMavenModule.getUserGroup();
            initializeUserGroup(userGroup);
            return savedMavenModule;
        }

    }

    @Override
    public List<MavenArtifact> findArtifacts(final MavenArtifactSearchCommand sc) {
        addUserGroupsFromLoggedInUser(sc);

        final List<MavenArtifact> artifacts = mavenArtifactDao.findBySearchCommand(sc);

        for (final MavenArtifact mavenArtifact : artifacts) {
            final MavenModule parentModule = mavenArtifact.getParentModule();
            final List<Destination> destinations = parentModule.getDestinations();
            for (final Destination destination : destinations) {
                destination.setDeployerParameters(new ArrayList<DeployerParameter>());
            }
        }

        return artifacts;
    }

    @Override
    public int findNumberOfArtifacts(final MavenArtifactSearchCommand sc) {
        addUserGroupsFromLoggedInUser(sc);
        return mavenArtifactDao.findNumberOfMavenArtifacts(sc);
    }

    @Override
    public MavenModule findMavenModuleById(final Long id) {
        final MavenModule mavenModule = mavenModuleDao.findById(id);

        final List<Destination> destinations = mavenModule.getDestinations();
        for (final Destination destination : destinations) {
            final List<DeployerParameter> deployerParameters = destination.getDeployerParameters();
            for (final DeployerParameter parameter : deployerParameters) {
                parameter.getId();
            }
        }

        final List<MavenModule> deploymentDependencies = mavenModule.getDeploymentDependencies();
        for (final MavenModule module : deploymentDependencies) {
            module.getId();
        }

        return mavenModule;
    }

    /**
     * @param mavenArtifactDao
     *        the mavenArtifactDao to set
     */
    @Required
    public void setMavenArtifactDao(final MavenArtifactDao<MavenArtifact> mavenArtifactDao) {
        this.mavenArtifactDao = mavenArtifactDao;
    }

    /**
     * @param mavenModuleDao
     *        the mavenModuleDao to set
     */
    @Required
    public void setMavenModuleDao(final MavenModuleDao<MavenModule> mavenModuleDao) {
        this.mavenModuleDao = mavenModuleDao;
    }

    /**
     * @param mavenArtifactValidator
     *        the mavenArtifactValidator to set
     */
    @Required
    public void setMavenArtifactValidator(final Validator mavenArtifactValidator) {
        this.mavenArtifactValidator = mavenArtifactValidator;
    }

    /**
     * @param mavenModuleValidator
     *        the mavenModuleValidator to set
     */
    @Required
    public void setMavenModuleValidator(final Validator mavenModuleValidator) {
        this.mavenModuleValidator = mavenModuleValidator;
    }

    /**
     * @param releaseDao
     *        the releaseDao to set
     */
    @Required
    public void setReleaseDao(final ReleaseDao<Release> releaseDao) {
        this.releaseDao = releaseDao;
    }

}
