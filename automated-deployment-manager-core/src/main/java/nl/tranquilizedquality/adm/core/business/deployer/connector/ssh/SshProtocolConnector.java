/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 4 jun. 2011 File: SshProtocolConnector.java
 * Package: nl.tranquilizedquality.adm.core.business.deployer.connector
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
package nl.tranquilizedquality.adm.core.business.deployer.connector.ssh;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.deployer.connector.Command;
import nl.tranquilizedquality.adm.commons.business.deployer.connector.ProtocolConnector;
import nl.tranquilizedquality.adm.commons.business.domain.Protocol;
import nl.tranquilizedquality.adm.core.business.deployer.connector.exception.FailedToConnectException;
import nl.tranquilizedquality.adm.core.business.deployer.connector.exception.FailedToExecuteCommandException;
import nl.tranquilizedquality.adm.core.business.deployer.connector.exception.InvalidCommandException;
import nl.tranquilizedquality.adm.core.business.deployer.connector.exception.OutputException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sshtools.j2ssh.ScpClient;
import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.AuthenticationProtocolState;
import com.sshtools.j2ssh.authentication.PasswordAuthenticationClient;
import com.sshtools.j2ssh.authentication.PublicKeyAuthenticationClient;
import com.sshtools.j2ssh.connection.Channel;
import com.sshtools.j2ssh.connection.ChannelEventListener;
import com.sshtools.j2ssh.connection.ChannelInputStream;
import com.sshtools.j2ssh.connection.ChannelOutputStream;
import com.sshtools.j2ssh.session.SessionChannelClient;
import com.sshtools.j2ssh.transport.HostKeyVerification;
import com.sshtools.j2ssh.transport.IgnoreHostKeyVerification;
import com.sshtools.j2ssh.transport.publickey.SshPrivateKey;
import com.sshtools.j2ssh.transport.publickey.SshPrivateKeyFile;

/**
 * Connector that speaks SSH. Make sure you wire this bean as a
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 4 jun. 2011
 */
public class SshProtocolConnector implements ProtocolConnector {

    /** logger for this class */
    private static final Log LOGGER = LogFactory.getLog(SshProtocolConnector.class);

    /** Enter string used for executing commands. */
    private static final String ENTER = "\n";

    /** The SSH client to use for SSH connection with a server. */
    private SshClient sshClient;

    /** Session channel that will be used to perform shell commands. */
    private SessionChannelClient sessionChannel;

    /** The protocol of this connector. */
    private final Protocol protocol;

    /** Buffer that will be used to store all the response data. */
    private StringBuilder outputBuffer;

    /**
     * All the logs from the SSH session so you can validate if something went
     * wrong.
     */
    private String sessionLogs;

    /**
     * Default constructor.
     */
    public SshProtocolConnector() {
        sshClient = new SshClient();
        protocol = Protocol.SSH;
        outputBuffer = new StringBuilder();
    }

    @Override
    public void connectToHost(String terminal, final String host, final int port, final String username, final String password,
            final byte[] privateKey) {
        try {
            final HostKeyVerification verification = new IgnoreHostKeyVerification();
            /*
             * To avoid a memory leak we have to create a new SSH client every
             * time we connect.
             */
            sshClient = new SshClient();
            sshClient.connect(host, port, verification);

            int result = 0;
            if (privateKey == null) {
                final PasswordAuthenticationClient passwordAuthenticationClient = new PasswordAuthenticationClient();
                passwordAuthenticationClient.setUsername(username);
                passwordAuthenticationClient.setPassword(password);

                result = sshClient.authenticate(passwordAuthenticationClient);
            } else {
                final PublicKeyAuthenticationClient pkaClient = new PublicKeyAuthenticationClient();
                pkaClient.setUsername(username);

                final SshPrivateKeyFile privateKeyFile = SshPrivateKeyFile.parse(privateKey);
                final SshPrivateKey pkey = privateKeyFile.toPrivateKey(null);
                pkaClient.setKey(pkey);

                result = sshClient.authenticate(pkaClient);
            }

            if (result != AuthenticationProtocolState.COMPLETE) {
                final String message = "Login to " + host + ":" + port + " " + username + "/" + password + " failed";

                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error(message);
                }

                throw new FailedToConnectException(message);
            }

            sessionChannel = sshClient.openSessionChannel();

            if (StringUtils.isBlank(terminal)) {
                terminal = "gogrid";
            }
            sessionChannel.requestPseudoTerminal(terminal, 1920, 1024, 0, 0, "");
            final boolean executed = sessionChannel.startShell();

            if (!executed) {
                final String msg = "Failed to start shell!";

                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error(msg);
                }

                throw new FailedToExecuteCommandException(msg);
            }

            /*
             * Add channel listener.
             */
            final ChannelEventListener listener = new ChannelEventListener() {

                @Override
                public void onDataSent(final Channel channel, final byte[] data) {
                }

                @Override
                public void onDataReceived(final Channel channel, final byte[] data) {
                    outputBuffer.append(new String(data));
                }

                @Override
                public void onChannelOpen(final Channel channel) {

                }

                @Override
                public void onChannelEOF(final Channel channel) {

                }

                @Override
                public void onChannelClose(final Channel channel) {

                }
            };
            sessionChannel.addEventListener(listener);
        } catch (final IOException e) {
            final String msg = "Failed to connect to host!";

            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg, e);
            }

            throw new FailedToConnectException(msg, e);
        }
    }

    @Override
    public String getSessionOutput() {
        try {
            final ChannelInputStream inputStream = sessionChannel.getInputStream();
            final String sessionOutput = IOUtils.toString(inputStream);
            return sessionOutput;
        } catch (final IOException e) {
            final String msg = "Failed to retrieve session output lines.";
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg, e);
            }
            throw new OutputException(msg, e);
        }
    }

    @Override
    public List<String> getSessionOutputLines() {
        try {
            final String sessionOutput = getSessionOutput();
            final List<String> lines = IOUtils.readLines(new StringReader(sessionOutput));

            return lines;
        } catch (final IOException e) {
            final String msg = "Failed to retrieve session output lines.";
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg, e);
            }
            throw new OutputException(msg, e);
        }
    }

    @Override
    public void disconnectFromHost() {
        final boolean connected = sshClient.isConnected();

        if (connected) {
            final boolean open = sessionChannel.isOpen();

            if (open) {
                performCustomCommand("exit" + ENTER);

                final Runnable runnable = new Runnable() {

                    @Override
                    public void run() {
                        final String sessionOutput = getSessionOutput();
                        sessionLogs = sessionOutput;
                    }
                };
                final Thread thread = new Thread(runnable);
                thread.start();
            }

            try {
                /*
                 * Sleep for a while to make sure commands that were sent are
                 * received before we exit.
                 */
                Thread.sleep(2000);
            } catch (final InterruptedException e) {
                final String msg = "Failed to sleep!";
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error(msg, e);
                }
            }

            sshClient.disconnect();
        }
    }

    @Override
    public void performCommand(final Command command, final String parameters) {
        if (!sshClient.isConnected()) {
            final String msg = "Can't perform command if not connected to a host!";

            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }

            throw new FailedToExecuteCommandException(msg);
        }

        if (command == null) {
            final String msg = "No command specified!";
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }

            throw new InvalidCommandException(msg);
        }

        try {
            final SshCommand sshCommand = (SshCommand) command;

            final String commandLine = sshCommand.getCommand() + " " + parameters + ENTER;

            performCustomCommand(commandLine);
        } catch (final Exception e) {
            final String msg = "Unsupported command specified!";
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }

            throw new InvalidCommandException(msg, e);
        }

    }

    @Override
    public void performCustomCommand(final String sshCommand) {
        try {
            final ChannelOutputStream outputStream = sessionChannel.getOutputStream();

            final String command = sshCommand + ENTER;

            outputStream.write(command.getBytes());

            if (LOGGER.isInfoEnabled()) {
                final List<String> lines = getOutputBufferLines();
                if (!lines.isEmpty()) {
                    if (lines.size() >= 2) {
                        LOGGER.info(lines.get(lines.size() - 2));
                    } else {
                        LOGGER.info(lines.get(0));
                    }
                }
            }
        } catch (final Exception e) {
            final String msg = "Failed to execute command: " + sshCommand;

            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg, e);
            }

            throw new FailedToExecuteCommandException(msg, e);
        }
    }

    @Override
    public void transferFileToHost(final File file, final String destinationPath) {
        try {
            final ScpClient scpClient = sshClient.openScpClient();

            /*
             * Send the file
             */
            final String filePath = file.getAbsolutePath();
            final String destination = destinationPath;
            scpClient.put(filePath, destination, true);
        } catch (final IOException e) {
            final String destination = destinationPath + file.getName();
            final String msg = "Failed to transfer file to host. File: " + file.getAbsolutePath() + " Destination: " + destination;

            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg, e);
            }

            throw new FailedToExecuteCommandException(msg, e);
        }
    }

    @Override
    public String getOutputBuffer() {
        return outputBuffer.toString();
    }

    @Override
    public List<String> getOutputBufferLines() {
        try {
            final InputStream input = new ByteArrayInputStream(outputBuffer.toString().getBytes());
            final List<String> lines = IOUtils.readLines(input);
            return lines;
        } catch (final IOException e) {
            final String msg = "Failed to retrieve output buffer lines!";

            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }
            throw new OutputException(msg, e);
        }
    }

    @Override
    public void flushOutputBuffer() {
        outputBuffer = new StringBuilder();
    }

    @Override
    public Protocol getProtocol() {
        return protocol;
    }

    /**
     * Retrieves the complete log of the last SSH session.
     * 
     * @return the sessionLogs The
     */
    @Override
    public String getSessionLogs() {
        return sessionLogs;
    }

}
