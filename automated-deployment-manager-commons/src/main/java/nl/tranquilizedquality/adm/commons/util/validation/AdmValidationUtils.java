/**
 * <pre>
 * Project: automated-deployment-manager-commons Created on: 11 sep. 2011 File: AdmValidationUtils.java
 * Package: nl.tranquilizedquality.adm.commons.util.validation
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
package nl.tranquilizedquality.adm.commons.util.validation;

import java.util.Collection;

import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

/**
 * Validation utility.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 11 sep. 2011
 */
public abstract class AdmValidationUtils extends ValidationUtils {

    /**
     * Reject the given field with the given error code if the value is empty.
     * <p>
     * An 'empty' value in this context means either <code>null</code>.
     * <p>
     * The object whose field is being validated does not need to be passed in
     * because the {@link Errors} instance can resolve field values by itself
     * (it will usually hold an internal reference to the target object).
     * 
     * @param errors
     *            the <code>Errors</code> instance to register errors on
     * @param field
     *            the field name to check
     * @param errorCode
     *            the error code, interpretable as message key
     */
    public static void rejectIfNull(final Errors errors, final String field, final String errorCode) {
        rejectIfNull(errors, field, errorCode, null, null);
    }

    /**
     * Reject the given field with the given error code and default message if
     * the value is empty.
     * <p>
     * An 'empty' value in this context means either <code>null</code>.
     * <p>
     * The object whose field is being validated does not need to be passed in
     * because the {@link Errors} instance can resolve field values by itself
     * (it will usually hold an internal reference to the target object).
     * 
     * @param errors
     *            the <code>Errors</code> instance to register errors on
     * @param field
     *            the field name to check
     * @param errorCode
     *            error code, interpretable as message key
     * @param defaultMessage
     *            fallback default message
     */
    public static void rejectIfNull(final Errors errors, final String field,
            final String errorCode, final String defaultMessage) {
        rejectIfNull(errors, field, errorCode, null, defaultMessage);
    }

    /**
     * Reject the given field with the given error codea nd error arguments if
     * the value is empty.
     * <p>
     * An 'empty' value in this context means either <code>null</code>.
     * <p>
     * The object whose field is being validated does not need to be passed in
     * because the {@link Errors} instance can resolve field values by itself
     * (it will usually hold an internal reference to the target object).
     * 
     * @param errors
     *            the <code>Errors</code> instance to register errors on
     * @param field
     *            the field name to check
     * @param errorCode
     *            the error code, interpretable as message key
     * @param errorArgs
     *            the error arguments, for argument binding via MessageFormat
     *            (can be <code>null</code>)
     */
    public static void rejectIfNull(final Errors errors, final String field,
            final String errorCode, final Object[] errorArgs) {
        rejectIfNull(errors, field, errorCode, errorArgs, null);
    }

    /**
     * Reject the given field with the given error code, error arguments and
     * default message if the value is empty.
     * <p>
     * An 'empty' value in this context means either <code>null</code>.
     * <p>
     * The object whose field is being validated does not need to be passed in
     * because the {@link Errors} instance can resolve field values by itself
     * (it will usually hold an internal reference to the target object).
     * 
     * @param errors
     *            the <code>Errors</code> instance to register errors on
     * @param field
     *            the field name to check
     * @param errorCode
     *            the error code, interpretable as message key
     * @param errorArgs
     *            the error arguments, for argument binding via MessageFormat
     *            (can be <code>null</code>)
     * @param defaultMessage
     *            fallback default message
     */
    public static void rejectIfNull(final Errors errors, final String field,
            final String errorCode, final Object[] errorArgs, final String defaultMessage) {

        Assert.notNull(errors, "Errors object must not be null");
        final Object value = errors.getFieldValue(field);
        if (value == null) {
            errors.rejectValue(field, errorCode, errorArgs, defaultMessage);
        }
    }

    /**
     * Reject the given field with the given error code if the value is empty.
     * <p>
     * An 'empty' value in this context means either <code>null</code>.
     * <p>
     * The object whose field is being validated does not need to be passed in
     * because the {@link Errors} instance can resolve field values by itself
     * (it will usually hold an internal reference to the target object).
     * 
     * @param errors
     *            the <code>Errors</code> instance to register errors on
     * @param field
     *            the field name to check
     * @param errorCode
     *            the error code, interpretable as message key
     */
    public static void rejectIfEmptyCollection(final Errors errors, final String field,
            final String errorCode) {
        rejectIfEmptyCollection(errors, field, errorCode, null, null);
    }

    /**
     * Reject the given field with the given error code and default message if
     * the value is empty.
     * <p>
     * An 'empty' value in this context means either <code>null</code>.
     * <p>
     * The object whose field is being validated does not need to be passed in
     * because the {@link Errors} instance can resolve field values by itself
     * (it will usually hold an internal reference to the target object).
     * 
     * @param errors
     *            the <code>Errors</code> instance to register errors on
     * @param field
     *            the field name to check
     * @param errorCode
     *            error code, interpretable as message key
     * @param defaultMessage
     *            fallback default message
     */
    public static void rejectIfEmptyCollection(final Errors errors, final String field,
            final String errorCode, final String defaultMessage) {
        rejectIfEmptyCollection(errors, field, errorCode, null, defaultMessage);
    }

    /**
     * Reject the given field with the given error codea nd error arguments if
     * the value is empty.
     * <p>
     * An 'empty' value in this context means either <code>null</code>.
     * <p>
     * The object whose field is being validated does not need to be passed in
     * because the {@link Errors} instance can resolve field values by itself
     * (it will usually hold an internal reference to the target object).
     * 
     * @param errors
     *            the <code>Errors</code> instance to register errors on
     * @param field
     *            the field name to check
     * @param errorCode
     *            the error code, interpretable as message key
     * @param errorArgs
     *            the error arguments, for argument binding via MessageFormat
     *            (can be <code>null</code>)
     */
    public static void rejectIfEmptyCollection(final Errors errors, final String field,
            final String errorCode, final Object[] errorArgs) {
        rejectIfEmptyCollection(errors, field, errorCode, errorArgs, null);
    }

    /**
     * Reject the given field with the given error code, error arguments and
     * default message if the collection is empty or null.
     * 
     * @param errors
     *            the <code>Errors</code> instance to register errors on
     * @param field
     *            the field name to check
     * @param errorCode
     *            the error code, interpretable as message key
     * @param errorArgs
     *            the error arguments, for argument binding via MessageFormat
     *            (can be <code>null</code>)
     * @param defaultMessage
     *            fallback default message
     */
    @SuppressWarnings({"rawtypes" })
    public static void rejectIfEmptyCollection(final Errors errors, final String field,
            final String errorCode, final Object[] errorArgs, final String defaultMessage) {

        Assert.notNull(errors, "Errors object must not be null");
        final Collection value = (Collection) errors.getFieldValue(field);

        if (value == null || value.isEmpty()) {
            errors.rejectValue(field, errorCode, errorArgs, defaultMessage);
        }
    }

    /**
     * Reject the given field with the given error code if the value is not
     * empty.
     * <p>
     * An 'empty' value in this context means either <code>null</code>.
     * <p>
     * The object whose field is being validated does not need to be passed in
     * because the {@link Errors} instance can resolve field values by itself
     * (it will usually hold an internal reference to the target object).
     * 
     * @param errors
     *            the <code>Errors</code> instance to register errors on
     * @param field
     *            the field name to check
     * @param errorCode
     *            the error code, interpretable as message key
     */
    public static void rejectIfNotNull(final Errors errors, final String field,
            final String errorCode) {
        rejectIfNotNull(errors, field, errorCode, null, null);
    }

    /**
     * Reject the given field with the given error code and default message if
     * the value is not empty.
     * <p>
     * An 'empty' value in this context means either <code>null</code>.
     * <p>
     * The object whose field is being validated does not need to be passed in
     * because the {@link Errors} instance can resolve field values by itself
     * (it will usually hold an internal reference to the target object).
     * 
     * @param errors
     *            the <code>Errors</code> instance to register errors on
     * @param field
     *            the field name to check
     * @param errorCode
     *            error code, interpretable as message key
     * @param defaultMessage
     *            fallback default message
     */
    public static void rejectIfNotNull(final Errors errors, final String field,
            final String errorCode, final String defaultMessage) {
        rejectIfNotNull(errors, field, errorCode, null, defaultMessage);
    }

    /**
     * Reject the given field with the given error codea nd error arguments if
     * the value is not empty.
     * <p>
     * An 'empty' value in this context means either <code>null</code>.
     * <p>
     * The object whose field is being validated does not need to be passed in
     * because the {@link Errors} instance can resolve field values by itself
     * (it will usually hold an internal reference to the target object).
     * 
     * @param errors
     *            the <code>Errors</code> instance to register errors on
     * @param field
     *            the field name to check
     * @param errorCode
     *            the error code, interpretable as message key
     * @param errorArgs
     *            the error arguments, for argument binding via MessageFormat
     *            (can be <code>null</code>)
     */
    public static void rejectIfNotNull(final Errors errors, final String field,
            final String errorCode, final Object[] errorArgs) {
        rejectIfNotNull(errors, field, errorCode, errorArgs, null);
    }

    /**
     * Reject the given field with the given error code, error arguments and
     * default message if the value is not empty.
     * <p>
     * An 'empty' value in this context means either <code>null</code>.
     * <p>
     * The object whose field is being validated does not need to be passed in
     * because the {@link Errors} instance can resolve field values by itself
     * (it will usually hold an internal reference to the target object).
     * 
     * @param errors
     *            the <code>Errors</code> instance to register errors on
     * @param field
     *            the field name to check
     * @param errorCode
     *            the error code, interpretable as message key
     * @param errorArgs
     *            the error arguments, for argument binding via MessageFormat
     *            (can be <code>null</code>)
     * @param defaultMessage
     *            fallback default message
     */
    public static void rejectIfNotNull(final Errors errors, final String field,
            final String errorCode, final Object[] errorArgs, final String defaultMessage) {

        Assert.notNull(errors, "Errors object must not be null");
        final Object value = errors.getFieldValue(field);
        if (value != null) {
            errors.rejectValue(field, errorCode, errorArgs, defaultMessage);
        }
    }

}
