package nl.tranquilizedquality.adm.core.business.deployer.connector.ssh;

import java.io.File;
import java.io.StringReader;
import java.util.List;

import nl.tranquilizedquality.adm.core.business.deployer.connector.ssh.SshProtocolConnector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test for {@link SshProtocolConnector}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 6 mrt. 2013
 */
public class SshProtocolConnectorTest {

    /** Connector that will be tested. */
    private SshProtocolConnector connector;

    @Before
    public void setUp() throws Exception {
        connector = new SshProtocolConnector();
    }

    @Test
    @Ignore
    public void testConnectToHost() throws Exception {

        final File file = new File("src/test/resources/id_rsa");
        final byte[] privateKey = FileUtils.readFileToByteArray(file);

        connector.connectToHost("xterm", "iwp-windps", 22, "svc-admdev", null, privateKey);

        // connector.connectToHost("t2nl-crmwebdev", 22, "s-petrus",
        // "", null);

        // connector.performCommand(SshCommand.CHANGE_DIRECTORY,
        // "/srv/tomcat/instances");
        // connector.performCommand(SshCommand.LIST, "");
        connector.performCustomCommand("mkdir -p /Temp/testdir");

        connector.performCustomCommand("mkdir -p /Temp/testdir4");

        connector.performCustomCommand("mkdir -p /Temp/testdir2");
        // connector.performCustomCommand("mkdir -p /Temp/testdir3");
        connector.performCustomCommand("rmdir -r /Temp/testdir");

        connector.performCustomCommand("rmdir -r /Temp/testdir2");
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

        // final List<String> lines = IOUtils.readLines(new
        // StringReader(sessionOutput));
        //
        // for (final String value : lines) {
        // System.out.println(value);
        // }

        connector.disconnectFromHost();

        final String sessionLogs = connector.getSessionLogs();
        System.out.println(sessionLogs);

    }

    @Test
    public void testIo() throws Exception {
        final String test = "asfdasdf\nasdfasdf\nsadfasdfasdf";

        final List<String> lines = IOUtils.readLines(new StringReader(test));

        for (final String value : lines) {
            System.out.println(value);
        }

    }

}
