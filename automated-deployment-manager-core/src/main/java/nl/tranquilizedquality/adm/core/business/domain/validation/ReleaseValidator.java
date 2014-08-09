/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 11 sep. 2011 File: ReleaseValidator.java
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

import nl.tranquilizedquality.adm.commons.business.domain.Release;
import nl.tranquilizedquality.adm.commons.util.validation.AdmValidationUtils;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateRelease;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validator that validates {@link Release} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 11 sep. 2011
 */
public class ReleaseValidator implements Validator {

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean supports(final Class clazz) {
		return clazz.isAssignableFrom(HibernateRelease.class);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "release.name-empty", "No name filled in.");
		AdmValidationUtils.rejectIfNull(errors, "releaseDate", "release.release-date-empty", "No release date specified.");
		AdmValidationUtils.rejectIfNull(errors, "status", "release.status-empty", "No status specified.");
	}

}
