/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: Aug 23, 2012 File: fUserGroupValidator.java
 * Package: nl.Tranquilized Quality.adm.core.business.domain.validation
 * 
 * Copyright (c) 2012 Tranquilized Quality www.Tranquilized Quality.nl All rights
 * reserved.
 * 
 * This software is the confidential and proprietary information of Tranquilized Quality
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Tranquilized Quality.
 * </pre>
 */
package nl.tranquilizedquality.adm.security.business.domain.validation;

import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUserGroup;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validator that validated {@link UserGroup} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 23, 2012
 * 
 */
public class UserGroupValidator implements Validator {

	@Override
	public boolean supports(final Class<?> clazz) {
		return clazz.isAssignableFrom(HibernateUserGroup.class);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "usergroup.name-empty", "No name filled in.");
	}

}
