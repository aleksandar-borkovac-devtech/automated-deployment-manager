/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 29 sep. 2011 File: MavenModuleValidator.java
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

import nl.tranquilizedquality.adm.commons.business.domain.MavenModule;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateMavenModule;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validator that validates {@link MavenModule} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 29 sep. 2011
 */
public class MavenModuleValidator implements Validator {

    @Override
    @SuppressWarnings({"unchecked", "rawtypes" })
    public boolean supports(final Class clazz) {
        return clazz.isAssignableFrom(HibernateMavenModule.class);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "type", "maven-module.type-empty", "No type filled in.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "group", "maven-module.group-empty", "No group filled in.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "artifactId", "maven-module.artifact-id-empty",
                "No artifact id filled in.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "maven-module.name-empty", "No name filled in.");

    }

}
