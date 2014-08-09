/*
 * @(#)AdmContainerUtil.java 24 jan. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.itest.cargo;

import java.io.File;

import nl.tranquilizedquality.itest.cargo.AbstractTomcatContainerUtil;

import org.apache.commons.io.FileUtils;

/**
 * Implementation of the {@link AbstractTomcatContainerUtil} that makes sure all
 * configuration files are copied to the correct location before tomcat is
 * started.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 24 jan. 2013
 */
public class AdmContainerUtil extends AbstractTomcatContainerUtil {

    @Override
    protected void setupConfiguration() throws Exception {
        /*
         * Copy log4j
         */
        File srcFile = new File("src/test/resources/conf/adm-log4j.xml");
        File destFile = new File(containerHome + "shared/classes/adm-log4j.xml");
        FileUtils.copyFile(srcFile, destFile);

        /*
         * Copy the log4j configuration.
         */
        srcFile = new File("src/test/resources/log4j.xml");
        destFile = new File(containerHome + "shared/classes/log4j.xml");
        FileUtils.copyFile(srcFile, destFile);

        /*
         * Copy applicaiton properties
         */
        srcFile = new File("src/test/resources/conf/adm.properties");
        destFile = new File(containerHome + "shared/classes/adm.properties");
        FileUtils.copyFile(srcFile, destFile);

        /*
         * Copy the tomcat context file for the JNDI data source.
         */
        srcFile = new File("src/test/resources/jndi/adm-gui.xml");
        destFile = new File(containerHome + "conf/Catalina/localhost/adm-gui.xml");
        FileUtils.copyFile(srcFile, destFile);

        /*
         * Copy the JDBC driver.
         */
        srcFile = new File("src/test/resources/jdbc/postgresql-8.3-604.jdbc3.jar");
        destFile = new File(containerHome + "/lib/postgresql-8.3-604.jdbc3.jar");
        FileUtils.copyFile(srcFile, destFile);
    }

}
