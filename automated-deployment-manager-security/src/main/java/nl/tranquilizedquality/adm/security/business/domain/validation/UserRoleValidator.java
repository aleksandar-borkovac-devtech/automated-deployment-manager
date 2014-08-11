package nl.tranquilizedquality.adm.security.business.domain.validation;

import nl.tranquilizedquality.adm.commons.business.domain.UserRole;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUserRole;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * {@link Validator} that validates a {@link UserRole} object.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 26, 2012
 */
public class UserRoleValidator implements Validator {

    @Override
    @SuppressWarnings({"unchecked", "rawtypes" })
    public boolean supports(final Class clazz) {
        return clazz.isAssignableFrom(HibernateUserRole.class);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "user", "user-role.user-empty", "No user filled in.");
        ValidationUtils.rejectIfEmpty(errors, "role", "user-role.role-empty", "No role filled in.");
        ValidationUtils.rejectIfEmpty(errors, "activeFrom", "user-role.active-from-empty", "No active from filled in.");
    }

}
