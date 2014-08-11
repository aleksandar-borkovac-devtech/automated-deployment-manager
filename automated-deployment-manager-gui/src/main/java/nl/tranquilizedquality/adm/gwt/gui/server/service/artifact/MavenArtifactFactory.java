/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 24 sep. 2011 File: MavenArtifactFactory.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.server.service.artifact
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
package nl.tranquilizedquality.adm.gwt.gui.server.service.artifact;

import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.domain.factory.AbstractFactory;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateMavenArtifact;
import nl.tranquilizedquality.adm.gwt.gui.client.model.artifact.ClientMavenArtifact;

/**
 * Factory that transforms client beans into persistent beans and vice versa.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 24 sep. 2011
 */
class MavenArtifactFactory extends
        AbstractFactory<ClientMavenArtifact, HibernateMavenArtifact, MavenArtifact> {

    @Override
    protected ClientMavenArtifact createNewClientBean() {
        return new ClientMavenArtifact();
    }

    @Override
    protected HibernateMavenArtifact createNewPersistentBean() {
        return new HibernateMavenArtifact();
    }

}
