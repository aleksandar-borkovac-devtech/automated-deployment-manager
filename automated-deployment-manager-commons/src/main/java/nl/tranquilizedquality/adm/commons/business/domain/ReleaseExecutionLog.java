/**
 * <pre>
 * Project: automated-deployment-manager-core 
 * Created on: Aug 3, 2012
 * File: ReleaseExecutionLog.java
 * Package: nl.Tranquilized Quality.adm.core.persistence.db.hibernate.bean
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
package nl.tranquilizedquality.adm.commons.business.domain;

import nl.tranquilizedquality.adm.commons.domain.InsertableDomainObject;

/**
 * Representation of the log of a release execution.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 3, 2012
 */
public interface ReleaseExecutionLog extends InsertableDomainObject<Long> {

    /**
     * @return the releaseExecution
     */
    ReleaseExecution getReleaseExecution();

    /**
     * @return the logs
     */
    String getLogs();

    /**
     * @return the mavenArtifact
     */
    MavenArtifactSnapshot getMavenArtifact();

}
