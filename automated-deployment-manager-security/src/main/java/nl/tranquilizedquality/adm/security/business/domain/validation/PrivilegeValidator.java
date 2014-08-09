package nl.tranquilizedquality.adm.security.business.domain.validation;

import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernatePrivilege;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * {@link Validator} that validates {@link HibernatePrivilege} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 26, 2012
 * 
 */
public class PrivilegeValidator implements Validator {

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean supports(final Class clazz) {
		return clazz.isAssignableFrom(HibernatePrivilege.class);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "privilege.name-empty", "No name filled in.");
		ValidationUtils.rejectIfEmpty(errors, "valid", "privilege.valid-empty", "No valid state filled in.");
		ValidationUtils.rejectIfEmpty(errors, "scope", "privilege.scope-empty", "No scope filled in.");

	}

}
