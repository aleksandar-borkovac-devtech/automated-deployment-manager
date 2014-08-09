package nl.tranquilizedquality.adm.itest.business.domain;

import java.util.List;

/**
 * Representation of a user in ADM.
 *
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 25 jan. 2013
 */
public class AdmUserDto {

    /** The user name to use. */
    private String userName;

    /** The password used to login. */
    private String password;

    /** The roles the user has. */
    private List<String> roles;

    public String getUserName() {
        return userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(final List<String> roles) {
        this.roles = roles;
    }

}
