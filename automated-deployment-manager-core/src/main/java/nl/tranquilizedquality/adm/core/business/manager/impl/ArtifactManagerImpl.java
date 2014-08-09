/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 3 jun. 2011 File: ArtifactManagerImpl.java
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

import java.io.File;
import java.net.UnknownHostException;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.ArtifactType;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.MavenModule;
import nl.tranquilizedquality.adm.commons.business.domain.Repository;
import nl.tranquilizedquality.adm.commons.util.http.HttpUtil;
import nl.tranquilizedquality.adm.core.business.manager.ArtifactManager;
import nl.tranquilizedquality.adm.core.business.manager.exception.ArtifactNotFoundException;
import nl.tranquilizedquality.adm.core.business.manager.exception.NoRepositoryFoundException;
import nl.tranquilizedquality.adm.core.persistence.dao.RepositoryDao;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Manager that downloads a {@link MavenArtifact} from a Maven repository.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jun. 2011
 */
public class ArtifactManagerImpl implements ArtifactManager {

    /** Logger for this class. */
    private static final Log LOG = LogFactory.getLog(ArtifactManagerImpl.class);

    /** DAO that retrieves repositories. */
    private RepositoryDao<Repository> repositoryDao;

    /** Path to the working directory of ADM. */
    private String workDirectory;

    /** HTTP utility. */
    private HttpUtil httpUtilImpl;

    @Override
    public File retrieveArtifact(final MavenArtifact artifact, final String release) {

        final List<Repository> all = repositoryDao.findAll();

        if (all.isEmpty()) {
            final String msg = "No repositories found to download from!";
            throw new NoRepositoryFoundException(msg);
        }

        for (final Repository repository : all) {
            if (repository.isEnabled()) {
                try {
                    final MavenModule parentModule = artifact.getParentModule();
                    final String version = artifact.getVersion();
                    final String artifactId = parentModule.getArtifactId();
                    final ArtifactType artifactType = parentModule.getType();
                    final String artifactUrl = generateDownloadUrl(repository, artifact);

                    /*
                     * Construct destination path.
                     */
                    final String artifactFileName = artifactId + "-" + version + artifactType.getExtention();
                    final String destinationPath = workDirectory + release + "/artifacts/";

                    /*
                     * Download artifact to work directory.
                     */
                    final File destination = httpUtilImpl.downloadFile(artifactUrl, destinationPath, artifactFileName);

                    return destination;
                } catch (final UnknownHostException e) {
                    final String msg = "Failed to retrieve artifact from repository!!";
                    if (LOG.isErrorEnabled()) {
                        LOG.error(msg, e);
                    }
                } catch (final Exception e) {
                    final String msg = "Failed to access repository! -> " + repository.getName() + " - " + repository.getRepositoryUrl();

                    if (LOG.isErrorEnabled()) {
                        LOG.error(msg, e);
                    }
                }
            }
        }

        final MavenModule parentModule = artifact.getParentModule();
        final String msg = "Artifact " + parentModule.getName() + " not found in any repository!";
        throw new ArtifactNotFoundException(msg);
    }

    /**
     * Generates the download URL to use for downloading an artifact based on the artifact and
     * repository that are passed.
     * 
     * @param repository
     *        The repository to download from.
     * @param artifact
     *        The artifact that needs to be downloaded.
     * @return Returns a {@link String} representation of the URL.
     */
    private String generateDownloadUrl(final Repository repository, final MavenArtifact artifact) {
        final MavenModule parentModule = artifact.getParentModule();
        final String version = artifact.getVersion();
        final String group = parentModule.getGroup();
        final String artifactId = parentModule.getArtifactId();
        final ArtifactType artifactType = parentModule.getType();
        final String identifier = parentModule.getIdentifier();

        /*
         * Construct download URL.
         */
        String artifactUrl = "";
        final String repositoryUrl = repository.getRepositoryUrl();
        final String repositoryId = repository.getRepositoryId();
        if (StringUtils.isNotBlank(repositoryId)) {
            final StringBuilder builder = new StringBuilder();
            builder.append(repositoryUrl);
            builder.append("?");
            builder.append("r=");
            builder.append(repositoryId);
            builder.append("&");
            builder.append("g=");
            builder.append(group);
            builder.append("&");
            builder.append("a=");
            builder.append(artifactId);
            builder.append("&");
            builder.append("v=");
            builder.append(version);

            if (StringUtils.isNotBlank(identifier)) {
                builder.append("&");
                builder.append("c=");
                builder.append(identifier);
            }

            switch (artifactType) {
                case TAR_GZIP:
                    builder.append("&");
                    builder.append("p=");
                    builder.append("tar.gz");
                    break;

                case WAR:
                    builder.append("&");
                    builder.append("p=");
                    builder.append("war");
                    break;

                case JAR:
                    builder.append("&");
                    builder.append("p=");
                    builder.append("jar");
                    break;

                case EAR:
                    builder.append("&");
                    builder.append("p=");
                    builder.append("ear");
                    break;

                case ZIP:
                    builder.append("&");
                    builder.append("p=");
                    builder.append("zip");
                    break;

                default:
                    // Do nothing
            }

            artifactUrl = builder.toString();
        } else {
            artifactUrl = group + "/" + artifactId;
            artifactUrl = StringUtils.replace(artifactUrl, ".", "/");
            artifactUrl = repositoryUrl + artifactUrl;

            /*
             * If the identifier is specified it should be added to the
             * URL so it can be retrieved properly.
             */
            final String extention = artifactType.getExtention();
            if (!StringUtils.isEmpty(identifier)) {
                artifactUrl += "/" + version + "/" + artifactId + "-" + version + "-" + identifier + extention;
            } else {
                artifactUrl += "/" + version + "/" + artifactId + "-" + version + extention;
            }
        }

        return artifactUrl;
    }

    /**
     * @param repositoryDao
     *        the repositoryDao to set
     */
    @Required
    public void setRepositoryDao(final RepositoryDao<Repository> repositoryDao) {
        this.repositoryDao = repositoryDao;
    }

    /**
     * @param workDirectory
     *        the workDirectory to set
     */
    @Required
    public void setWorkDirectory(final String workDirectory) {
        this.workDirectory = workDirectory;
    }

    /**
     * @param httpUtilImpl
     *        the httpUtilImpl to set
     */
    @Required
    public void setHttpUtil(final HttpUtil httpUtilImpl) {
        this.httpUtilImpl = httpUtilImpl;
    }

}
