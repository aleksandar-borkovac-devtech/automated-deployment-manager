package nl.tranquilizedquality.adm.commons.business.domain;

import java.util.Date;
import java.util.Set;

import nl.tranquilizedquality.adm.commons.domain.UpdatableDomainObject;

/**
 * A user of the automated deployment manager.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 */
public interface User extends UpdatableDomainObject<Long> {

	/**
	 * Retrieves the user name of the user.
	 * 
	 * @return Returns a {@link String} value of the user name.
	 */
	String getUserName();

	/**
	 * Retrieves the password of the user.
	 * 
	 * @return Returns a {@link String} value of the password.
	 */
	String getPassword();

	/**
	 * Determines if the user is active or not.
	 * 
	 * @return Returns true if the user is active otherwise it will return
	 *         false.
	 */
	Boolean isActive();

	/**
	 * Determines if the user is blocked or not.
	 * 
	 * @return Returns true if the user is blocked otherwise it will return
	 *         false.
	 */
	Boolean isBlocked();

	/**
	 * Retrieves the date from when the user is active.
	 * 
	 * @return Returns a {@link Date}.
	 */
	Date getActiveFrom();

	/**
	 * Retrieves the date until when the user is active.
	 * 
	 * @return Returns a {@link Date} or null if the user will be active
	 *         forever.
	 */
	Date getActiveUntil();

	/**
	 * Retrieves the last login date.
	 * 
	 * @return Returns a {@link Date} or null if the user hasn't logged in yet.
	 */
	Date getLastLoginDate();

	/**
	 * Sets the last login date.
	 * 
	 * @param lastLoginDate
	 *            the lastLoginDate to set
	 */
	void setLastLoginDate(Date lastLoginDate);

	/**
	 * Retrieves the comments on a user.
	 * 
	 * @return Returns a {@link String} or null if there are no comments.
	 */
	String getComments();

	/**
	 * Retrieves the name of the user.
	 * 
	 * @return Returns a {@link String} representation of the name of the user.
	 */
	String getName();

	/**
	 * @param active
	 */
	void setActive(Boolean active);

	/**
	 * @param blocked
	 */
	void setBlocked(Boolean blocked);

	/**
	 * @param activeFrom
	 */
	void setActiveFrom(Date activeFrom);

	/**
	 * @param activeUntil
	 */
	void setActiveUntil(Date activeUntil);

	/**
	 * Sets the comments of the user.
	 * 
	 * @param comments
	 */
	void setComments(String comments);

	/**
	 * Retrieves the roles the user has.
	 * 
	 * @return Returns a {@link Set} containing user roles.
	 */
	Set<Role> getRoles();

	/**
	 * Retrieves the roles the user has.
	 * 
	 * @return Returns a {@link Set} containing user roles.
	 */
	Set<UserRole> getUserRoles();

	/**
	 * Sets the roles the user has.
	 * 
	 * @param userRoles
	 *            The user roles that will be set.
	 */
	void setUserRoles(Set<UserRole> userRoles);

	/**
	 * Determines if the password was sent to the user or not.
	 * 
	 * @return
	 */
	Boolean getPasswordSent();

	/**
	 * Sets if the password was sent to the user or not.
	 * 
	 * @param passwordSent
	 *            Boolean value determining if the password was set.
	 */
	void setPasswordSent(Boolean passwordSent);

	/**
	 * Sets the password of the user.
	 * 
	 * @param password
	 *            The password that will be set.
	 */
	void setPassword(String password);

	/**
	 * Retrieves the email address the user can be reached on.
	 * 
	 * @return Returns a {@link String} value of the email address.
	 */
	String getEmail();

	/**
	 * Retrieves the mobile phone number where the user can be reached on.
	 * 
	 * @return Returns a {@link String} representation of the mobile phone
	 *         number.
	 */
	String getMobilePhoneNumber();

	/**
	 * @return
	 */
	String getNewPassword();

	/**
	 * @return
	 */
	String getVerificationPassword();

}
