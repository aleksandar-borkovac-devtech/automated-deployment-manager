/*
 * @(#)PostReleaseCheckPlugin.java 20 mrt. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.api.plugin.postrelease;

import java.util.Map;

import nl.tranquilizedquality.adm.api.plugin.Plugin;

/**
 * Representation of a post release check plugin that can execute post release checks.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 20 mrt. 2013
 */
public interface PostReleaseCheckPlugin extends Plugin {

    /**
     * Executes post release checks using the passed in parameters.
     * 
     * @param parameters
     *        The parameters to use for execution.
     * @return Returns a {@link PostReleaseCheckResults} object containing the check results.
     */
    PostReleaseCheckResults executePostReleaseChecks(Map<String, String> parameters);

    /**
     * Initializes the plugin and eventually should register itself with the ADM plugin manager. If
     * not the plug-in will not be accessible through ADM.
     */
    void intialize();

}
