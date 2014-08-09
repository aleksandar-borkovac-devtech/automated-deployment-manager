package nl.tranquilizedquality.adm.security.business.domain.validation;

import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUser;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * {@link Validator} implementation that validates {@link User} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 26, 2012
 */
public class UserValidator implements Validator {

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public boolean supports(final Class clazz) {
        return clazz.isAssignableFrom(HibernateUser.class);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        final User user = (User) target;
        final String name = user.getName();
        if (StringUtils.isBlank(name)) {
            errors.rejectValue("name", "user.first-name-empty", "No first name filled in.");
        }

        final String email = user.getEmail();
        if (StringUtils.isBlank(email)) {
            errors.rejectValue("email", "user.email-empty", "No email filled in.");
        }

        final String password = user.getPassword();
        if (StringUtils.isBlank(password)) {
            errors.rejectValue("password", "user.password-empty", "No password filled in.");
        }
    }

}
