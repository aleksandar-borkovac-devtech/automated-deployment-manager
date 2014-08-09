package nl.tranquilizedquality.adm.security.business.manager.impl;

import java.util.List;

import javax.mail.internet.InternetAddress;

import nl.tranquilizedquality.adm.security.business.exception.EmailManagerException;
import nl.tranquilizedquality.adm.security.business.manager.EmailManager;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Required;

/**
 * Manager that manages email.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 */
public class EmailManagerImpl implements EmailManager {

    /** Logger for this class. */
    private static final Log LOGGER = LogFactory.getLog(EmailManagerImpl.class);

    /** The host server that can actually send the email message. */
    private String host;

    /** The from address. */
    private String from;

    /** The username to use when authenticating to the SMTP server. */
    private String userName;

    /** The password to use when authenticating to the SMTP server. */
    private String password;

    /** Determines if a SSL connection will be used to send emails. */
    private boolean useSSL;

    /** Determines if a TLS connection will be used to send emails. */
    private boolean useTLS;

    private boolean enabled = true;

    @Override
    public void sendEmail(final List<InternetAddress> recipients, final String toName, final String subject, final String message)
            throws EmailManagerException {
        try {
            final HtmlEmail email = new HtmlEmail();
            email.setHostName(host);
            if (StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(password)) {
                email.setAuthentication(userName, password);
            }
            email.setSSL(useSSL);
            email.setTLS(useTLS);
            email.setTo(recipients);
            email.setFrom(from, from);
            email.setSubject(subject);
            email.setMsg(message);

            if(enabled) {
                email.send();
            } else {
                LOGGER.info("Not sending email. Email sending disabled.");
            }

        } catch (final EmailException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Failed to send email!", e);
            }
        }

    }

    /**
     * @param host
     *        the host to set
     */
    @Required
    public void setHost(final String host) {
        this.host = host;
    }

    /**
     * @param from
     *        the from to set
     */
    @Required
    public void setFrom(final String from) {
        this.from = from;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public void setUseSSL(final boolean useSSL) {
        this.useSSL = useSSL;
    }

    public void setUseTLS(final boolean useTLS) {
        this.useTLS = useTLS;
    }

}
