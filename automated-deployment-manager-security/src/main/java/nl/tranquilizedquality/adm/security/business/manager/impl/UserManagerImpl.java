package nl.tranquilizedquality.adm.security.business.manager.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import nl.tranquilizedquality.adm.commons.business.domain.Role;
import nl.tranquilizedquality.adm.commons.business.domain.Scope;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.business.domain.UserRole;
import nl.tranquilizedquality.adm.security.business.command.UserSearchCommand;
import nl.tranquilizedquality.adm.security.business.exception.EmailManagerException;
import nl.tranquilizedquality.adm.security.business.exception.UserManagerException;
import nl.tranquilizedquality.adm.security.business.generator.PasswordGenerator;
import nl.tranquilizedquality.adm.security.business.manager.EmailManager;
import nl.tranquilizedquality.adm.security.business.manager.ImageFileRepositoryManager;
import nl.tranquilizedquality.adm.security.business.manager.SecurityContextManager;
import nl.tranquilizedquality.adm.security.business.manager.UserManager;
import nl.tranquilizedquality.adm.security.persistence.db.dao.RoleDao;
import nl.tranquilizedquality.adm.security.persistence.db.dao.ScopeDao;
import nl.tranquilizedquality.adm.security.persistence.db.dao.UserDao;
import nl.tranquilizedquality.adm.security.persistence.db.dao.UserRoleDao;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUserRole;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Manager that manages {@link User} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 22 sep 2009
 */
public class UserManagerImpl implements UserManager {

    /** Logger for this class. */
    private static final Log LOGGER = LogFactory.getLog(UserManagerImpl.class);

    /** DAO to be used for managing {@link User} objects. */
    private UserDao<User> userDao;

    /** DAO to be used for managing {@link UserRole} objects. */
    private UserRoleDao<UserRole> userRoleDao;

    /** DOA to be used for managing {@link Scope} objects. */
    private ScopeDao<Scope> scopeDao;

    /** DAO to be used for managing {@link Role} objects. */
    private RoleDao<Role> roleDao;

    /** {@link Validator} used to validate a {@link User} object. */
    private Validator userValidator;

    /** {@link Validator} user to validate a {@link Role} */
    private Validator userRoleValidator;

    /** The {@link PasswordGenerator} used to generate a random password. */
    private PasswordGenerator passwordGenerator;

    /** The manager that can send an email. */
    private EmailManager emailManager;

    /** Manager that can retrieve images from the file system. */
    private ImageFileRepositoryManager imageFileRepositoryManager;

    /** Manager used to retrieve the logged in user. */
    private SecurityContextManager securityContextManager;

    @Override
    public List<UserRole> findUserRoles(final User user) {
        final User foundUser = userDao.findById(user.getId());

        return userRoleDao.findByUser(foundUser);
    }

    @Override
    public User resetPasswordForUser(final User user) {
        final String plainPassword = generatePassword(user);

        final User savedUser = userDao.save(user);

        sendMail(savedUser, plainPassword);

        return savedUser;
    }

    @Override
    public User updateUserAccount(final User user, final Errors errors) {
        final User loggedInUser = securityContextManager.findLoggedInUser();
        final Long loggedInUserId = loggedInUser.getId();
        final Long userId = user.getId();
        if (loggedInUserId.equals(userId)) {
            return storeUser(user, errors);
        }

        final String msg = "You are not allowed to update someonelses account!";
        if (LOGGER.isErrorEnabled()) {
            LOGGER.error(msg);
        }
        throw new UserManagerException(msg);
    }

    @Override
    public User storeUser(final User user, final Errors errors) throws UserManagerException {

        String plainPassword = null;

        /*
         * Check if there is a password specified. If not generate a random
         * password. If the password is specified make sure it will be hashed.
         */
        final String password = user.getPassword();
        if (StringUtils.isBlank(password) && !user.isPersistent()) {
            plainPassword = generatePassword(user);
            user.setPasswordSent(false);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Generated password: " + plainPassword);
            }
        } else if (StringUtils.isNotBlank(password) && password.length() != 40) {
            /*
             * The maximum length of a password is 20 so if the user changed
             * their password it will be smaller than or equal to 20. The hashed
             * password will be 40 so this way we can detect if a user has
             * changed his/hers password or not. Not that this is not water
             * tight since if for some reason the user password was set with a
             * value of length 40 which is not a hash it will go through.
             * Something to think about to improve if there is any solution for
             * this at all to actually detect a has correctly.
             */
            final ShaPasswordEncoder encoder = new ShaPasswordEncoder();
            plainPassword = password;

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Plain password: " + plainPassword);
            }

            /*
             * Check if the plain password is too long or not.
             */
            if (password.length() > 20) {
                errors.rejectValue("password", "user.password-too-long", "The password specified is too long!");
            } else {
                final String encodedPassword = encoder.encodePassword(plainPassword, null);

                user.setPassword(encodedPassword);
                user.setPasswordSent(false);

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Password encoded: " + encodedPassword);
                }

                final Long userId = user.getId();
                final User originalUser = userDao.findById(userId);
                final String currentPassword = originalUser.getPassword();
                if (currentPassword.equals(encodedPassword)) {

                    final String newPassword = user.getNewPassword();
                    final String verificationPassword = user.getVerificationPassword();

                    if (StringUtils.isNotBlank(newPassword)) {

                        if (StringUtils.equals(newPassword, verificationPassword)) {
                            final String newEncodedPassword = encoder.encodePassword(newPassword, null);
                            user.setPassword(newEncodedPassword);

                            originalUser.copy(user);

                            return userDao.save(originalUser);
                        } else {
                            final String msg = "Specified new password doesn't match your verification password.";

                            if (LOGGER.isErrorEnabled()) {
                                LOGGER.error(msg);
                            }

                            errors.reject("user.update-password.invalid-verification", msg);

                            /*
                             * No password specified so you are not allowed to
                             * store it.
                             */
                            throw new UserManagerException(msg);
                        }
                    } else {
                        user.setPassword(currentPassword);
                        originalUser.copy(user);

                        return userDao.save(originalUser);
                    }
                } else {
                    final String msg = "Specified password doesn't match your current password.";

                    if (LOGGER.isErrorEnabled()) {
                        LOGGER.error(msg);
                    }

                    errors.reject("member.update-password.invalid", msg);

                    /*
                     * No password specified so you are not allowed to store it.
                     */
                    throw new UserManagerException(msg);
                }
            }

        } else if (StringUtils.isBlank(password) && user.isPersistent()) {
            /*
             * If we reach this case this means an administrator is updating the
             * user so the password shouldn't be changed.
             */
            final Long userId = user.getId();
            final User originalUser = userDao.findById(userId);
            final String originalPassword = originalUser.getPassword();
            user.setPassword(originalPassword);
        }

        /*
         * Validate the user.
         */
        userValidator.validate(user, errors);

        if (errors.hasErrors()) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Invalid user: " + errors.getErrorCount());
            }
            throw new UserManagerException("User has validation errors!");
        }

        /*
         * Check if we are editing or creating a user.
         */
        User savedUser = null;

        if (user.isPersistent()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Find persistent user: " + user);
            }

            final User originalUser = userDao.findById(user.getId());
            originalUser.copy(user);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Copied values from found user..");
            }

            savedUser = userDao.save(originalUser);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Saved user..");
            }
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Storing new user..");
            }

            savedUser = userDao.save(user);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Saved new user..");
            }
        }

        /*
         * Send an email if it wasn't sent yet and if the user has an email.
         */
        if (savedUser.getPasswordSent() == null || savedUser.getPasswordSent() == false && StringUtils.isNotBlank(plainPassword)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Sending password email -> " + plainPassword);
            }

            sendMail(savedUser, plainPassword);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Password email sent..");
            }

        }

        /*
         * Initialize the lazy initialized collections of the user.
         */
        initialize(savedUser);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("User initialized..");
        }

        return savedUser;
    }

    @Override
    public void storeUserPicture(final Long id, final byte[] image) {

        try {
            final User user = userDao.findById(id);

            if (user == null) {
                final String msg = "No user found with the specified id " + id + ". Picture cannot be stored!";

                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error(msg);
                }

                throw new UserManagerException(msg);
            }

            userDao.save(user);

            imageFileRepositoryManager.storeImage(id, image);
        } catch (final IOException e) {
            final String msg = "Failed to store the user image!";

            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }

            throw new UserManagerException(msg, e);
        }

    }

    @Override
    public File findUserPicture(final Long userId, final Long officeId) {
        return findUserPicture(userId);
    }

    @Override
    public File findUserPicture(final Long userId) {
        final User user = userDao.findById(userId);

        if (user == null) {
            final String msg = "Unkown member with id " + userId;

            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }

            throw new UserManagerException(msg);
        }

        try {
            final File memberImage = imageFileRepositoryManager.findImageById(userId);

            return memberImage;
        } catch (final IOException e) {
            final String msg = "Failed to retrieve the user image for user " + userId;

            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }

            throw new UserManagerException(msg, e);
        }
    }

    @Override
    public void assignDefaultRoles(final User user, final Errors errors) {
        /*
         * Refresh user with the database.
         */
        userDao.refresh(user);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Refreshed user..");
        }

        /*
         * Assign default role so user can login into the application.
         */
        final Role role = roleDao.findById(2L);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Found default role: " + role);
        }

        /*
         * Create user role.
         */
        final HibernateUserRole userRole = new HibernateUserRole();
        userRole.setActive(true);
        userRole.setActiveFrom(new Date());
        userRole.setUser(user);
        userRole.setRole(role);
        userRole.setCreated(new Date());
        userRole.setCreatedBy(user.getCreatedBy());

        /*
         * Store the user role.
         */
        final Errors userRoleErrors = new BindException(userRole, userRole.getClass().getName());

        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Storing user role..");
            }

            storeUserRole(userRole, userRoleErrors);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("User role stored..");
            }
        } catch (final UserManagerException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Failed to save user role! -> " + userRoleErrors.getErrorCount());
            }

            errors.rejectValue("userRoles", "user.role-invalid", "Failed to store user role!");

            throw new UserManagerException("Failed to save user role!", e);
        }
    }

    /**
     * Generates a password and sets the encoded password on the user.
     * 
     * @param user
     *            The user where the password will be generated for.
     * @return Returns the plain generated password.
     */
    private String generatePassword(final User user) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Generating password for user: " + user);
        }

        final String generatedPassword = passwordGenerator.generate(10);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Generated password..");
        }

        final ShaPasswordEncoder encoder = new ShaPasswordEncoder();
        final String encodedPassword = encoder.encodePassword(generatedPassword, null);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Password encoded..");
        }

        user.setPassword(encodedPassword);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Password set on user..");
        }

        return generatedPassword;
    }

    /**
     * Sends an email containing the login credentials to the specified user.
     * 
     * @param user
     *            The {@link User} that will be email with his/hers credentials.
     * @param plainPassword
     *            The plain password to be sent to the user.
     * @throws UserManagerException
     *             Exception thrown when something fails during sending of an
     *             email.
     */
    private void sendMail(final User user, final String plainPassword) throws UserManagerException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Sending email to user..");
            LOGGER.debug("Creating email message -> " + plainPassword);
        }

        /*
         * Send email to the user.
         */
        final String message = createEmailMessage(user, plainPassword);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Email message created..");
        }

        /*
         * Refresh and initialize the collections. We need to retrieve the
         * password before synchronizing with the database otherwise the newly
         * generated password will be lost.
         */
        final String newPassword = user.getPassword();
        user.setPassword(newPassword);
        initialize(user);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("New password set.. -> " + newPassword);
        }

        /*
         * Send credentials to user.
         */
        sendEmailToUser(user, message);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Send email to contact succeeded..");
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Marking user as password sent..");
        }

        /*
         * Mark password sent.
         */
        user.setPasswordSent(true);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Marked user as password sent..");
        }

    }

    /**
     * Creates the password email to be sent to the specified user.
     * 
     * @param user
     *            The user where the email will be sent to.
     * @param plainPassword
     *            The plain password sent in the email.
     * @return Returns the email message that will be sent to the user.
     */
    private String createEmailMessage(final User user, final String plainPassword) {
        final StringBuilder message = new StringBuilder();
        message.append("Username: ");
        message.append(user.getUserName());
        message.append(IOUtils.LINE_SEPARATOR);
        message.append("Password: ");
        message.append(plainPassword);
        message.append(IOUtils.LINE_SEPARATOR);

        return message.toString();
    }

    private void sendEmailToUser(final User user, final String message) {

        try {
            final InternetAddress address = new InternetAddress(user.getEmail());
            final List<InternetAddress> recipients = new ArrayList<InternetAddress>();
            recipients.add(address);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Sending email..");
            }

            /*
             * Send the email to the email address of the user.
             */
            emailManager.sendEmail(recipients, user.getName(), "ADM Password information", message.toString());

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Email sent..");
            }
        } catch (final AddressException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Failed to send email because of invalid email address -> " + e.getMessage());
            }

            throw new UserManagerException("Invalid email address.", e);
        } catch (final EmailManagerException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Failed to send email!");
            }

            throw new UserManagerException("Failed to send email!", e);
        }
    }

    @Override
    public void assignRoles(final List<? extends Role> roles, final List<? extends User> users, final User loggedInUser) {
        /*
         * Synchronize the logged in user with the database.
         */
        final User foundLoggedInUser = userDao.findById(loggedInUser.getId());

        for (final User user : users) {
            /*
             * Synchronize the user with the database.
             */
            final User foundUser = userDao.findById(user.getId());

            for (final Role role : roles) {
                /*
                 * Synchronize the role with the database.
                 */
                final Role foundRole = roleDao.findById(role.getId());

                /*
                 * Determine if the user has the role or not.
                 */
                final Set<UserRole> userRoles = foundUser.getUserRoles();
                boolean hasRole = false;
                if (userRoles != null) {
                    for (final UserRole userRole : userRoles) {
                        if (userRole.getRole().getId().equals(foundRole.getId())) {
                            hasRole = true;
                            break;
                        }
                    }
                }

                /*
                 * Only add the role if the user doesn't have it.
                 */
                if (!hasRole) {
                    final HibernateUserRole userRole = new HibernateUserRole();
                    userRole.setActive(true);
                    userRole.setActiveFrom(new Date());
                    userRole.setCreated(new Date());
                    userRole.setCreatedBy(foundLoggedInUser.getUserName());
                    userRole.setRole(foundRole);
                    userRole.setUser(foundUser);

                    userRoleDao.save(userRole);
                }
            }

        }
    }

    @Override
    public UserRole storeUserRole(final UserRole userRole, final Errors errors) throws UserManagerException {

        userRoleValidator.validate(userRole, errors);

        if (errors.hasErrors()) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Invalid user role: " + errors.getErrorCount());
            }
            throw new UserManagerException("Invalid user role!");
        }

        if (userRole.isPersistent()) {
            final UserRole originalUserRole = userRoleDao.findById(userRole.getId());
            originalUserRole.copy(userRole);

            userRoleDao.save(originalUserRole);

            return originalUserRole;
        } else {
            final UserRole savedUserRole = userRoleDao.save(userRole);

            return savedUserRole;
        }

    }

    @Override
    public void revokeUserRole(final UserRole userRole, final Errors errors) throws UserManagerException {
        if (userRole == null || userRole.isPersistent() == false) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Null value passed as user role!");
            }

            errors.reject("user-role.null", "No user role to delete!");
            throw new UserManagerException("Can't delete a null object!");
        }

        final UserRole persistentUserRole = userRoleDao.findById(userRole.getId());

        persistentUserRole.setActive(false);
        persistentUserRole.setActiveUntil(new Date());

        userRoleDao.save(persistentUserRole);
    }

    @Override
    public List<User> findUsers(final UserSearchCommand searchCommand) {
        /*
         * Setup the scope to be a persistent one.
         */
        final Scope scope = searchCommand.getScope();

        if (scope != null && scope.isPersistent()) {
            final Scope retrievedScope = scopeDao.findById(scope.getId());
            searchCommand.setScope(retrievedScope);
        } else {
            searchCommand.setScope(null);
        }

        /*
         * Setup roles to be persistent roles.
         */
        final List<Role> userRoles = searchCommand.getUserRoles();
        final List<Role> persistentUserRoles = new ArrayList<Role>();

        if (userRoles != null && !userRoles.isEmpty()) {
            for (final Role role : userRoles) {
                if (role.isPersistent()) {
                    final Long id = role.getId();

                    final Role foundRole = roleDao.findById(id);
                    persistentUserRoles.add(foundRole);
                }
            }
        }
        searchCommand.setUserRoles(persistentUserRoles);

        /*
         * Perform search.
         */
        final List<User> users = userDao.findUsersBySearchCommand(searchCommand);

        /*
         * Initialize all the collections.
         */
        for (final User user : users) {
            initialize(user);
        }

        return users;
    }

    /**
     * Does lazy initialization of certain collections.
     * 
     * @param user
     *            The user that will be initialized.
     */
    public void initialize(final User user) {

        final Set<UserRole> userRoles = user.getUserRoles();
        if (userRoles != null) {
            for (final UserRole userRole : userRoles) {
                userRole.getId();
            }
        }

    }

    @Override
    public User findUserById(final Long id) {
        final User user = userDao.findById(id);

        initialize(user);

        return user;
    }

    @Override
    public int findNumberOfUsers(final UserSearchCommand searchCommand) {
        return userDao.findNumberOfUsers(searchCommand);
    }

    @Override
    public User findUserByUsername(final String username) {
        final User user = userDao.findActiveUserByUserName(username);

        initialize(user);

        return user;
    }

    @Required
    public void setUserDao(final UserDao<User> userDao) {
        this.userDao = userDao;
    }

    /**
     * @param userValidator
     *            the userValidator to set
     */
    @Required
    public void setUserValidator(final Validator userValidator) {
        this.userValidator = userValidator;
    }

    /**
     * @param userRoleDao
     *            the userRoleDao to set
     */
    @Required
    public void setUserRoleDao(final UserRoleDao<UserRole> userRoleDao) {
        this.userRoleDao = userRoleDao;
    }

    /**
     * @param userRoleValidator
     *            the userRoleValidator to set
     */
    @Required
    public void setUserRoleValidator(final Validator userRoleValidator) {
        this.userRoleValidator = userRoleValidator;
    }

    /**
     * @param scopeDao
     *            the scopeDao to set
     */
    @Required
    public void setScopeDao(final ScopeDao<Scope> scopeDao) {
        this.scopeDao = scopeDao;
    }

    /**
     * @param roleDao
     *            the roleDao to set
     */
    @Required
    public void setRoleDao(final RoleDao<Role> roleDao) {
        this.roleDao = roleDao;
    }

    /**
     * @param passwordGenerator
     *            the passwordGenerator to set
     */
    @Required
    public void setPasswordGenerator(final PasswordGenerator passwordGenerator) {
        this.passwordGenerator = passwordGenerator;
    }

    /**
     * @param emailManager
     *            the emailManager to set
     */
    @Required
    public void setEmailManager(final EmailManager emailManager) {
        this.emailManager = emailManager;
    }

    /**
     * @param imageFileRepositoryManager
     *            the imageFileRepositoryManager to set
     */
    @Required
    public void setImageFileRepositoryManager(final ImageFileRepositoryManager imageFileRepositoryManager) {
        this.imageFileRepositoryManager = imageFileRepositoryManager;
    }

    @Required
    public void setSecurityContextManager(final SecurityContextManager securityContextManager) {
        this.securityContextManager = securityContextManager;
    }

}
