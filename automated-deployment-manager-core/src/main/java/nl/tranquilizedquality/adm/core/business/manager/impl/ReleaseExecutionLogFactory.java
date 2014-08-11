/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: Aug 3, 2012 File: fReleaseExecutionLogFactory.java
 * Package: nl.tranquilizedquality.adm.core.business.manager.impl
 * 
 * Copyright (c) 2012 Tranquilized Quality www.Tranquilized Quality.nl All rights
 * reserved.
 * 
 * This software is the confidential and proprietary information of Tranquilized Quality
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Tranquilized Quality.
 * </pre>
 */
package nl.tranquilizedquality.adm.core.business.manager.impl;

import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifactSnapshot;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecution;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecutionLog;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateReleaseExecutionLog;

import org.apache.commons.lang.StringUtils;

/**
 * Factory that creates a {@link ReleaseExecutionLog}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 3, 2012
 */
abstract class ReleaseExecutionLogFactory {

    /**
     * Creates a {@link ReleaseExecutionLog} based on the passed in parameters.
     * 
     * @param releaseExecution
     *            The {@link ReleaseExecution} where this log is for.
     * @param artifact
     *            The {@link MavenArtifact} where this log is from.
     * @param logs
     *            The logs them selves.
     * @return Returns the {@link ReleaseExecutionLog}.
     */
    public static ReleaseExecutionLog createReleaseExecutionLog(final ReleaseExecution releaseExecution,
            final MavenArtifactSnapshot artifact, final String logs) {
        final HibernateReleaseExecutionLog log = new HibernateReleaseExecutionLog();

        if (StringUtils.isBlank(logs)) {
            log.setLogs("No logs available..");
        } else {
            log.setLogs(logs);
        }
        log.setMavenArtifact(artifact);
        log.setReleaseExecution(releaseExecution);

        return log;
    }

}
