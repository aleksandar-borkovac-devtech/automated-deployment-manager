package nl.tranquilizedquality.adm.security.business.manager;

import java.util.List;

import javax.mail.internet.InternetAddress;

/**
 * Manager that is responsible for sending emails.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 */
public interface EmailManager {

    /**
     * Sends an email to multiple email addresses.
     * 
     * @param recipients
     *            The email addresses the email will be sent to.
     * @param toName
     *            The name of the person you are sending the email to.
     * @param subject
     *            The subject of the email.
     * @param message
     *            The email message.
     */
    void sendEmail(List<InternetAddress> recipients, String toName, String subject, String message);

}
