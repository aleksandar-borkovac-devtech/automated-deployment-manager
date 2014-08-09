/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 11 sep. 2011 File: MavenArtifactValidator.java
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

import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.util.validation.AdmValidationUtils;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateMavenArtifact;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validator that validates {@link MavenArtifact} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 11 sep. 2011
 */
public class MavenArtifactValidator implements Validator {

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean supports(final Class clazz) {
		return clazz.isAssignableFrom(HibernateMavenArtifact.class);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "version", "maven-artifact.version-empty", "No version filled in.");
		AdmValidationUtils.rejectIfNull(errors, "parentModule", "maven-artifact.parent-module-empty", "No parent module specified.");
		AdmValidationUtils.rejectIfNull(errors, "release", "maven-artifact.release-empty", "No release specified.");

	}

}
