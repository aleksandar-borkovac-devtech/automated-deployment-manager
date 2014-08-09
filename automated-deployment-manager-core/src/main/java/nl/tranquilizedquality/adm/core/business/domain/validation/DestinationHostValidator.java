/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 30 okt. 2011 File: DestinationHostValidator.java
 * Package: nl.tranquilizedquality.adm.core.business.domain.validation
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
package nl.tranquilizedquality.adm.core.business.domain.validation;

import nl.tranquilizedquality.adm.commons.business.domain.DestinationHost;
import nl.tranquilizedquality.adm.commons.util.validation.AdmValidationUtils;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDestinationHost;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validator for a {@link DestinationHost}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 30 okt. 2011
 */
public class DestinationHostValidator implements Validator {

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean supports(final Class clazz) {
		return clazz.isAssignableFrom(HibernateDestinationHost.class);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "hostName", "destination.host-name-empty", "No host name filled in.");
		AdmValidationUtils.rejectIfNull(errors, "port", "destination.port-empty", "No port specified.");
		AdmValidationUtils.rejectIfNull(errors, "protocol", "destination.protocol-empty", "No protocol specified.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "destination.username-empty", "No username filled in.");

		final DestinationHost host = (DestinationHost) target;
		final String password = host.getPassword();

		if (StringUtils.isEmpty(password)) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "privateKey", "destination.private-key-empty", "No private key filled in.");
		}
		else {
			AdmValidationUtils.rejectIfNotNull(errors, "privateKey", "destination.private-key-not-empty", "No private key should be filled in if you use password authentication.");
		}
	}

}
