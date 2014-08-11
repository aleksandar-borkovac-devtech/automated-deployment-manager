/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: Aug 3, 2012 File: fReleaseExecutionLogFactory.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.server.service.release
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
package nl.tranquilizedquality.adm.gwt.gui.server.service.release;

import nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecutionLog;
import nl.tranquilizedquality.adm.commons.domain.factory.AbstractFactory;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateReleaseExecutionLog;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientReleaseExecutionLog;

/**
 * Factory that transforms {@link ClientReleaseExecutionLog} objects into
 * {@link HibernateReleaseExecutionLog} objects and vice versa.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 3, 2012
 * 
 */
class ReleaseExecutionLogFactory extends
        AbstractFactory<ClientReleaseExecutionLog, HibernateReleaseExecutionLog, ReleaseExecutionLog> {

    @Override
    protected ClientReleaseExecutionLog createNewClientBean() {
        return new ClientReleaseExecutionLog();
    }

    @Override
    protected HibernateReleaseExecutionLog createNewPersistentBean() {
        return new HibernateReleaseExecutionLog();
    }

}
