/*
 * @(#)EmailManagerTest.java 22 okt. 2012
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.security.business.manager.impl;

import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.InternetAddress;

import nl.tranquilizedquality.adm.security.business.manager.impl.EmailManagerImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * Test for {@link EmailManagerImpl}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 22 okt. 2012
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class EmailManagerTest {

    /** Manager that will be tested. */
    private EmailManagerImpl emailManager;

    @Before
    public void setUp() {
        emailManager = new EmailManagerImpl();

        emailManager.setFrom("JUnit@Tranquilized Quality.com");
        emailManager.setHost("10.0.3.129");
    }

    @Test
    public void testSendEmail() throws Exception {
        final InternetAddress to = new InternetAddress("consumer.errors@gmail.com");
        final String toName = "Salomo Petrus";
        final String subject = "Subject";
        final String message = "email message";

        final List<InternetAddress> recipients = new ArrayList<InternetAddress>();
        recipients.add(to);

        emailManager.sendEmail(recipients, toName, subject, message);
    }

}
