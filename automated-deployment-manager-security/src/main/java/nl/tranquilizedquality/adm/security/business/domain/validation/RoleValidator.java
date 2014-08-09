package nl.tranquilizedquality.adm.security.business.domain.validation;

import nl.tranquilizedquality.adm.commons.business.domain.Role;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateRole;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validates a {@link Role} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 26, 2012
 * 
 */
public class RoleValidator implements Validator {

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean supports(final Class clazz) {
		return clazz.isAssignableFrom(HibernateRole.class);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "role.name-empty", "No name filled in.");
		ValidationUtils.rejectIfEmpty(errors, "valid", "role.valid-empty", "No valid state filled in.");
		ValidationUtils.rejectIfEmpty(errors, "scope", "role.scope-empty", "No scope filled in.");
	}

}
