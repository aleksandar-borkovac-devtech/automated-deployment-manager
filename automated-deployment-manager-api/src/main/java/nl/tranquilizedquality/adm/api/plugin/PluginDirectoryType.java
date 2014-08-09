/*
 * @(#)PluginDirectoryType.java 27 mrt. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.api.plugin;

/**
 * All available plugin related directories.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 27 mrt. 2013
 */
public enum PluginDirectoryType {

    /** The directory where all available ADM plugins are located. */
    PLUGIN_DIRECTORY,

    /** The directory that the plugins can use for temporary file storage. */
    PLUGIN_WORK_DIRECTORY;

}
