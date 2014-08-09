/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 30 aug. 2011 File: RepositoryValidator.java
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

import nl.tranquilizedquality.adm.commons.business.domain.Repository;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateRepository;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validator for a {@link Repository}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 30 aug. 2011
 */
public class RepositoryValidator implements Validator {

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean supports(final Class clazz) {
		return clazz.isAssignableFrom(HibernateRepository.class);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "repository.name-empty", "No name filled in.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "repositoryUrl", "repository.url-empty", "No URL specified.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "enabled", "repository.enabled-empty", "No enabled value specified.");
	}

}
