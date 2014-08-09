/*
 * @(#)PluginType.java 20 mrt. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.api.plugin;

/**
 * Type of available plugins.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 20 mrt. 2013
 */
public enum PluginType {

    /** Post release check plugin that performs validations to see if an application works properly. */
    POST_RELEASE_CHECK,

    /** A custom deployer that can be used to deploy artifacts. */
    DEPLOYER;

}
