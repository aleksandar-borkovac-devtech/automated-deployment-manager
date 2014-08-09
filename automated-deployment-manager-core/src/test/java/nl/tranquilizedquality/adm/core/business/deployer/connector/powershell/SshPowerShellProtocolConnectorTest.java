/*
 * @(#)SshPowerShellProtocolConnectorTest.java 5 mrt. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.core.business.deployer.connector.powershell;

import java.io.File;

import nl.tranquilizedquality.adm.core.business.deployer.connector.powershell.SshPowerShellProtocolConnector;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 5 mrt. 2013
 */
public class SshPowerShellProtocolConnectorTest {

    private SshPowerShellProtocolConnector connector;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        connector = new SshPowerShellProtocolConnector();
    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.deployer.connector.powershell.SshPowerShellProtocolConnector#connectToHost(java.lang.String, java.lang.String, int, java.lang.String, java.lang.String, byte[])}
     * .
     */
    @Test
    @Ignore
    public void testConnectToHost() throws Exception {
        final File file = new File("src/test/resources/id_rsa");
        final byte[] privateKey = FileUtils.readFileToByteArray(file);

        // connector.connectToHost("gogrid", "localhost", 22, "s-petrus", "nohode*12%", null);


        connector.performCustomCommand("mkdir /Windows/Temp/2345235423");
        // connector.performCustomCommand("rc killprocesses excel");

        // connector.performCommand(SshCommand.CHANGE_DIRECTORY, "/srv/tomcat/instances");
        // connector.performCommand(SshCommand.LIST, "");
        // connector.performCustomCommand("mkdir -p /Temp/testdir");

        // connector.performCustomCommand("mkdir -p /Temp/testdir4");

        // connector.performCustomCommand("mkdir -p /Temp/testdir2");
        // connector.performCustomCommand("mkdir -p /Temp/testdir3");
        // connector.performCustomCommand("rmdir -r /Temp/testdir");

        // connector.performCustomCommand("rmdir -r /Temp/testdir2");
        // connector.performCustomCommand("unzip");

        System.out.println("--------------------------------------------------------");

        // final Runnable runnable = new Runnable() {
        //
        // @Override
        // public void run() {
        // final String sessionOutput = connector.getSessionOutput();
        // System.out.println(sessionOutput);
        // }
        // };
        // final Thread thread = new Thread(runnable);
        // thread.start();

        // final List<String> lines = IOUtils.readLines(new StringReader(sessionOutput));
        //
        // for (final String value : lines) {
        // System.out.println(value);
        // }

        connector.disconnectFromHost();

        final String sessionLogs = connector.getSessionLogs();
        System.out.println(sessionLogs);

    }

}
