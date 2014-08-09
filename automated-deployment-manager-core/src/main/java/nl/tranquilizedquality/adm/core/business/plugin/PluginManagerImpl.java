/*
 * @(#)PluginManagerImpl.java 19 mrt. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.core.business.plugin;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import nl.tranquilizedquality.adm.api.plugin.Plugin;
import nl.tranquilizedquality.adm.api.plugin.PluginManager;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Manager that installs plugins that can be used within ADM.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 19 mrt. 2013
 */
public class PluginManagerImpl implements PluginManager, ApplicationContextAware {

    /** Logger for this class. */
    private static final Log LOGGER = LogFactory.getLog(PluginManagerImpl.class);

    /** Buffer used for reading the JAR file. */
    private static final int BUFFER = 2048;

    /** Map containing all active plugins that registered themselved. */
    private final Map<String, Plugin> registeredPlugins;

    /** The directory where plugins are located. */
    private String pluginDirectory;

    /** The application context where this manager is loaded in. */
    private ApplicationContext applicationContext;

    /** The list containing all the application contexts where plugins are loaded in. */
    private final List<FileSystemXmlApplicationContext> pluginContexts;

    /**
     * Default constructor initializing the plugin contexts and registered plugins collections.
     */
    public PluginManagerImpl() {
        pluginContexts = new ArrayList<FileSystemXmlApplicationContext>();
        registeredPlugins = new HashMap<String, Plugin>();
    }

    @Override
    public void installPlugins() {
        final File file = new File(pluginDirectory);
        String[] fileList = file.list();

        for (final String fileName : fileList) {
            if (StringUtils.endsWith(fileName, ".zip")) {
                final String distributionFileName = pluginDirectory + fileName;
                extractPluginDistribution(distributionFileName);
            }
        }

        fileList = file.list();
        for (final String fileName : fileList) {
            final File detectedFile = new File(pluginDirectory + fileName);
            final boolean isDirectory = detectedFile.isDirectory();

            if (isDirectory) {
                final String[] detectedFileList = detectedFile.list();

                for (final String detectedFileName : detectedFileList) {
                    if (StringUtils.endsWith(detectedFileName, ".jar")) {
                        final String contextFileName = pluginDirectory + detectedFile.getName() + "/" + detectedFileName;
                        final File contextFile = extractJarFile(contextFileName);
                        startupPluginContext(contextFile);
                    }
                }

            }
        }
    }

    @Override
    public List<Plugin> findRegisteredPlugins() {
        final List<Plugin> plugins = new ArrayList<Plugin>();
        final Set<Entry<String, Plugin>> entrySet = registeredPlugins.entrySet();
        for (final Entry<String, Plugin> entry : entrySet) {
            final Plugin plugin = entry.getValue();
            plugins.add(plugin);
        }

        return plugins;
    }

    @Override
    public Plugin findPluginByName(final String name) {
        return registeredPlugins.get(name);
    }

    /**
     * Extracts the plugin distribution ZIP file to the plugin directory so the resources can be
     * accessed on the classpath.
     * 
     * @param fileName
     *        The name of the ZIP file.
     */
    private void extractPluginDistribution(final String fileName) {
        try {
            BufferedOutputStream dest = null;
            final File distributionFile = new File(fileName);
            final FileInputStream fis = new FileInputStream(distributionFile);
            final ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry;

            final String pluginExtractDirectory = pluginDirectory + "/";
            final File extractDir = new File(pluginExtractDirectory);
            extractDir.mkdirs();

            while ((entry = zis.getNextEntry()) != null) {
                final String entryName = entry.getName();

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Extracting file " + entry);
                }

                int count;
                final byte data[] = new byte[BUFFER];

                if (entry.isDirectory()) {
                    final File newDirectory = new File(pluginExtractDirectory + entryName);
                    newDirectory.mkdirs();
                } else {
                    // write the files to the disk
                    final String destinationLocation = pluginDirectory + entryName;
                    final File file = new File(destinationLocation);
                    final FileOutputStream fos = new FileOutputStream(file);
                    dest = new BufferedOutputStream(fos, BUFFER);

                    while ((count = zis.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, count);
                    }

                    dest.flush();
                    dest.close();
                }

            }
            zis.close();
        } catch (final Exception e) {
            final String msg = "Failed to extract plugin from distribution " + fileName;
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }
            throw new FailedToExtractContextFileException(msg, e);
        }
    }

    /**
     * Extracts the plugin-application-context.xml file from the plugin JAR file so it can be used
     * to load the plugin.
     * 
     * @param fileName
     *        The name of the JAR file name.
     * @return Returns a {@link File} pointing to the spring application context file of the plugin.
     */
    private File extractJarFile(final String fileName) {
        try {
            File contextFile = null;
            BufferedOutputStream dest = null;
            final File jarFile = new File(fileName);
            final FileInputStream fis = new FileInputStream(jarFile);
            final ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry;

            final String pluginExtractDirectory = pluginDirectory + "/" + StringUtils.chomp(jarFile.getName(), ".jar") + "/";
            final File extractDir = new File(pluginExtractDirectory);
            extractDir.mkdirs();

            while ((entry = zis.getNextEntry()) != null) {
                final String entryName = entry.getName();

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Extracting file " + entry);
                }

                int count;
                final byte data[] = new byte[BUFFER];

                if (entry.isDirectory()) {
                    final File newDirectory = new File(pluginExtractDirectory + entryName);
                    newDirectory.mkdirs();
                } else {
                    // write the files to the disk
                    final String destinationLocation =
                            pluginDirectory + "/" + StringUtils.chomp(jarFile.getName(), ".jar") + "/" + entryName;
                    if (StringUtils.equals("plugin-application-context.xml", entryName)) {
                        contextFile = new File(destinationLocation);
                    }
                    final File file = new File(destinationLocation);
                    final FileOutputStream fos = new FileOutputStream(file);
                    dest = new BufferedOutputStream(fos, BUFFER);

                    while ((count = zis.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, count);
                    }

                    dest.flush();
                    dest.close();
                }

            }
            zis.close();

            if (contextFile != null) {
                return contextFile;
            } else {
                throw new FailedToExtractContextFileException("No context file found in JAR! " + fileName);
            }
        } catch (final Exception e) {
            final String msg = "Failed to extract plugin context file from jar " + fileName;
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }
            throw new FailedToExtractContextFileException(msg, e);
        }
    }

    /**
     * Starts up the application context for the plugin using the current application context as
     * parent so the plugin can register itself calling this manager.
     * 
     * @param contextFile
     *        The context file of the plugin.
     */
    private void startupPluginContext(final File contextFile) {

        for (final FileSystemXmlApplicationContext context : pluginContexts) {
            context.close();
        }
        pluginContexts.clear();

        final FileSystemXmlApplicationContext pluginContext =
                new FileSystemXmlApplicationContext(new String[] { "/" + contextFile.getAbsolutePath() }, applicationContext);
        pluginContext.start();
        pluginContexts.add(pluginContext);
    }

    @Override
    public void registerPlugin(final Plugin plugin) {
        final String name = plugin.getName();
        registeredPlugins.put(name, plugin);
    }

    @Override
    public void unregisterPlugin(final Plugin plugin) {
        final String name = plugin.getName();
        registeredPlugins.remove(name);
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Required
    public void setPluginDirectory(final String pluginDirectory) {
        this.pluginDirectory = pluginDirectory;
    }

}
