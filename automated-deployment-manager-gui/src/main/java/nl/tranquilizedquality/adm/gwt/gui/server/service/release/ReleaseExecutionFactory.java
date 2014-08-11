/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 15 okt. 2011 File: ReleaseExecutionFactory.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.server.service.release
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
package nl.tranquilizedquality.adm.gwt.gui.server.service.release;

import nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecution;
import nl.tranquilizedquality.adm.commons.domain.factory.AbstractFactory;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateReleaseExecution;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientReleaseExecution;

/**
 * Factory that transforms {@link ClientReleaseExecution} objects into
 * {@link HibernateReleaseExecution} objects and vice versa.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 15 okt. 2011
 */
public class ReleaseExecutionFactory extends
        AbstractFactory<ClientReleaseExecution, HibernateReleaseExecution, ReleaseExecution> {

    @Override
    protected ClientReleaseExecution createNewClientBean() {
        return new ClientReleaseExecution();
    }

    @Override
    protected HibernateReleaseExecution createNewPersistentBean() {
        return new HibernateReleaseExecution();
    }

}
