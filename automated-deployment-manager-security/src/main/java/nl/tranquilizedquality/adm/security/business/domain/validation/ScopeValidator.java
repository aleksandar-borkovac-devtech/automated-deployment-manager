package nl.tranquilizedquality.adm.security.business.domain.validation;

import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateScope;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * {@link Validator} that validates {@link HibernateScope} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 26, 2012
 * 
 */
public class ScopeValidator implements Validator {

    @Override
    @SuppressWarnings({"unchecked", "rawtypes" })
    public boolean supports(final Class clazz) {
        return clazz.isAssignableFrom(HibernateScope.class);
    }

    @Override
    public void validate(final Object target, final Errors errors) {

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "scope.name-empty", "No name filled in.");

    }

}
