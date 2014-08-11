package nl.tranquilizedquality.adm.commons.business.domain;

import nl.tranquilizedquality.adm.commons.domain.UpdatableDomainObject;

/**
 * Representation of a destination server to deploy an artifact to.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 30 okt. 2011
 */
public interface DestinationHost extends UpdatableDomainObject<Long> {

    /**
     * Retrieves the name of the host of the destination server.
     * 
     * @return Returns a {@link String} representation of the host name.
     */
    String getHostName();

    /**
     * Retrieves the protocol to communicate with the destination server.
     * 
     * @return Returns a {@link Protocol}.
     */
    Protocol getProtocol();

    /**
     * Retrieves the user name to use for login.
     * 
     * @return Returns a {@link String} representation of the user name.
     */
    String getUsername();

    /**
     * Retrieves the password to use for login.
     * 
     * @return Returns a {@link String} representation of the password.
     */
    String getPassword();

    /**
     * Retrieves the port to connect to.
     * 
     * @return Returns a {@link Integer} value greater than 0.
     */
    Integer getPort();

    /**
     * Retrieves the private key that will be used for authentication.
     * 
     * @return Returns a {@link String} value of the private key or null if none
     *         is specified.
     */
    String getPrivateKey();

    /**
     * Sets the private key for this destination host.
     * 
     * @param privateKey
     *            The key that will be used for authentication.
     */
    void setPrivateKey(String privateKey);

    /**
     * Retrieves the user group that this maven module belongs to.
     * 
     * @return Returns the {@link UserGroup}.
     */
    UserGroup getUserGroup();

    /**
     * Retrieves the terminal that should be used to connect to the host. If
     * empty the default "gogrid" terminal will be used.
     * 
     * @return Returns a {@link String} representation of the terminal.
     */
    String getTerminal();

}
