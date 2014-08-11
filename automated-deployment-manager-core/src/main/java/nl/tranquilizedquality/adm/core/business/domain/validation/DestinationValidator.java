/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 11 sep. 2011 File: DestinationValidator.java
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

import nl.tranquilizedquality.adm.commons.util.validation.AdmValidationUtils;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDestination;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validator that validates {@link Destination} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 11 sep. 2011
 */
public class DestinationValidator implements Validator {

    @Override
    @SuppressWarnings({"unchecked", "rawtypes" })
    public boolean supports(final Class clazz) {
        return clazz.isAssignableFrom(HibernateDestination.class);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        AdmValidationUtils.rejectIfNull(errors, "destinationHost", "destination.host-empty", "No host specified.");
        AdmValidationUtils.rejectIfNull(errors, "deployer", "destination.deployer-empty", "No deployer filled in.");
        AdmValidationUtils.rejectIfNull(errors, "environment", "destination.environment-empty", "No environment specified.");
    }

}
