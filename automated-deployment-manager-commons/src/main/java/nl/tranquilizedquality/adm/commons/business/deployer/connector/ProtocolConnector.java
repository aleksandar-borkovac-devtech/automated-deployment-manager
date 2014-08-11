package nl.tranquilizedquality.adm.commons.business.deployer.connector;

import java.io.File;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.Protocol;

/**
 * Connector that uses a specific protocol to connecto to a host machine.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 22 okt. 2012
 */
public interface ProtocolConnector {

    /**
     * Connects to a host using passed in information.
     * 
     * @param terminal
     *            The terminal type to use when connecting to the host. e.g.
     *            "gogrid"
     * @param host
     *            The host to connect to.
     * @param port
     *            The port to connect to on the host.
     * @param username
     *            The user name to use to connect.
     * @param password
     *            The password to use to connect.
     * @param privateKey
     *            The private key to use for logging into the target host.
     */
    void connectToHost(String terminal, String host, int port, String username, String password, byte[] privateKey);

    /**
     * Disconnects from the host.
     */
    void disconnectFromHost();

    /**
     * Performs a command on the host.
     * 
     * @param Command
     *            The {@link Command} that will be executed.
     * @param parameters
     *            the parameters to use when executing the command if
     *            applicable.
     */
    void performCommand(Command Command, String parameters);

    /**
     * Performs a custom command on the host.
     * 
     * @param sshCommand
     *            The SSH command to perform. This can be ls -all e.g.
     */
    void performCustomCommand(String sshCommand);

    /**
     * Retrieves the protocol used by this connector.
     * 
     * @return Returns the {@link Protocol}.
     */
    Protocol getProtocol();

    /**
     * Transfers the specified file to the destination path.
     * 
     * @param file
     *            The file that will be transferred.
     * @param destinationPath
     *            The destination path the file will be transferred to.
     */
    void transferFileToHost(File file, String destinationPath);

    /**
     * Retrieves the output data as a {@link String}.
     * 
     * @return Returns a {@link String} containing the output data.
     */
    String getOutputBuffer();

    /**
     * Retrieves the output data as a {@link List} containing lines of
     * {@link String} data.
     * 
     * @return Returns a {@link List} containing the lines or an empty one if
     *         there are none.
     */
    List<String> getOutputBufferLines();

    /**
     * Flushes the output buffer.
     */
    void flushOutputBuffer();

    /**
     * Retrieves all the information from the active session. If the session was
     * already ended no data will be returned.
     * 
     * @return Returns a {@link String} value of the session output information.
     */
    String getSessionOutput();

    /**
     * Retrieves all the information from the active session. If the session was
     * already ended no data will be returned.
     * 
     * @return Returns a {@link List} containing all the lines of the session
     *         output information.
     */
    List<String> getSessionOutputLines();

    /**
     * Retrieves the logs of the last session.
     * 
     * @return Returns a {@link String} containing the logs or an empty one if
     *         the session is still active.
     */
    String getSessionLogs();

}
