/**
 * <pre>
 * Project: automated-deployment-manager-security Created on: 12 aug. 2014 File: fPasswordEncoderTest.java
 * Package: nl.tranquilizedquality.adm.security.business.manager.impl
 *
 * Copyright (c) 2014 Tranquilized Quality www.tr-quality.com All rights
 * reserved.
 *
 * This software is the confidential and proprietary information of Dizizid
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Tranquilized Quality.
 * </pre>
 */
package nl.tranquilizedquality.adm.security.business.manager.impl;

import org.junit.Test;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

/**
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 12 aug. 2014
 *
 */
public class PasswordEncoderTest {

    private final ShaPasswordEncoder passwordEncoder = new ShaPasswordEncoder(512);

    @Test
    public void test() {

        final String encodePassword = passwordEncoder.encodePassword("H1JINQY5FM", "adm-itest");
        System.out.println(encodePassword);
        System.out.println(encodePassword.length());

    }

}
