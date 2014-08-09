/*
 * @(#)Plugin.java 20 mrt. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.api.plugin;

import org.springframework.beans.factory.InitializingBean;

/**
 * Representation of an ADM plugin.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 20 mrt. 2013
 */
public interface Plugin extends InitializingBean {

    /**
     * Retrieves the name of the plugin.
     * 
     * @return Returns a {@link String} value of the name.
     */
    String getName();

    /**
     * Retrieves the version of the plugin.
     * 
     * @return Returns a {@link String} value of the version.
     */
    String getVersion();

    /**
     * Retrieves the type of plugin.
     * 
     * @return Returns the {@link PluginType}.
     */
    PluginType getType();

}
