/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 30 aug. 2011 File: ClientRepositorySearchCommand.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.model.repository
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
package nl.tranquilizedquality.adm.gwt.gui.client.model.repository;

import nl.tranquilizedquality.adm.commons.business.command.RepositorySearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.Repository;

import com.extjs.gxt.ui.client.data.BeanModelTag;

/**
 * Client side representation of the search criteria for a {@link Repository}
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 30 aug. 2011
 */
public class ClientRepositorySearchCommand extends RepositorySearchCommand implements BeanModelTag {

    /** Unique identifier used for serialization. */
    private static final long serialVersionUID = 6145048549798189987L;

    /**
     * @return the activeValue
     */
    public String getEnabledValue() {
        final Boolean active = getEnabled();
        if (active != null) {
            if (active) {
                return "Enabled";
            }
            else {
                return "Disabled";
            }
        }
        else {
            return "Any...";
        }
    }

    /**
     * @param activeValue
     *            the activeValue to set
     */
    public void setEnabledValue(final String activeValue) {
        if ("Enabled".equals(activeValue)) {
            setEnabled(true);
        }
        else if ("Disabled".equals(activeValue)) {
            setEnabled(false);
        }
        else {
            setEnabled(null);
        }
    }

}
