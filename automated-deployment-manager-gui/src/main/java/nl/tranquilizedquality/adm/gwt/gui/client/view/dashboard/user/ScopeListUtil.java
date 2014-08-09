package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.tranquilizedquality.adm.commons.business.domain.Role;
import nl.tranquilizedquality.adm.commons.business.domain.Scope;
import nl.tranquilizedquality.adm.commons.business.domain.UserRole;
import nl.tranquilizedquality.adm.gwt.gui.client.model.scope.ClientScope;

/**
 * Utility class that creates scope lists.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public abstract class ScopeListUtil {

	/**
	 * Creates a list containing {@link Scope} objects based on a
	 * {@link Collection} of {@link ClientScope} objects.
	 * 
	 * @param userRoles
	 *            The {@link Collection} that where a list of {@link Scope}
	 *            objects will be created from.
	 * @return Returns a list containing {@link Scope} objects or an empty one.
	 */
	public static List<Scope> createScopeListFromScopes(final Collection<ClientScope> originalScopes) {
		final List<Scope> scopes = new ArrayList<Scope>();
		final Set<String> scopeNames = new HashSet<String>();
		if (originalScopes != null) {

			for (final Scope originalScope : originalScopes) {
				if (!scopeNames.contains(originalScope.getName())) {
					// Get the list of roles defined for this user
					// in this scope
					final Set<Role> roles = originalScope.getRoles();

					/*
					 * Create new hash set since for some weird reason if you
					 * don't the TreeGrid will not render and breaks for
					 * Mysterious reasons.
					 */
					final Set<Role> newRoles = new HashSet<Role>(roles);

					// Create a special version of a ClientScope
					// object for
					// which we can set the roles.
					final ClientScope scope = new ClientScope();
					scope.copy(originalScope);
					scope.setRoles(newRoles);
					scopes.add(scope);
					scopeNames.add(scope.getName());
				}
			}
		}

		return scopes;
	}

	/**
	 * Creates a list containing {@link Scope} objects based on a {@link Set} of
	 * {@link UserRole} objects.
	 * 
	 * @param userRoles
	 *            The {@link Set} that where a list of {@link Scope} objects
	 *            will be created from.
	 * @return Returns a list containing {@link Scope} objects or an empty one.
	 */
	public static List<Scope> createScopeList(final Set<UserRole> userRoles) {
		final List<Scope> scopes = new ArrayList<Scope>();
		final Set<String> scopeNames = new HashSet<String>();
		if (userRoles != null) {
			for (final UserRole userRole : userRoles) {
				final Role role = userRole.getRole();
				if (!scopeNames.contains(role.getScope().getName()) && userRole.isActive()) {
					final Scope originalScope = role.getScope();

					// Get the list of roles defined for this user
					// in this scope
					final Set<Role> roles = new HashSet<Role>();
					for (final UserRole userRoleOfScope : userRoles) {
						if (userRoleOfScope.getRole().getScope().getName().equals(originalScope.getName())) {
							roles.add(userRoleOfScope.getRole());
						}
					}
					// Create a special version of a ClientScope
					// object for
					// which we can set the roles.
					final ClientScope scope = new ClientScope();
					scope.copy(originalScope);
					scope.setRoles(roles);
					scopes.add(scope);
					scopeNames.add(scope.getName());
				}
			}
		}

		return scopes;
	}

	public static List<Scope> createScopeListFromRoles(final Set<Role> grantableRoles) {
		final List<Scope> scopes = new ArrayList<Scope>();
		final Set<String> scopeNames = new HashSet<String>();
		if (grantableRoles != null) {
			for (final Role role : grantableRoles) {

				if (!scopeNames.contains(role.getScope().getName())) {
					final Scope originalScope = role.getScope();

					// Get the list of roles defined for this user
					// in this scope
					final Set<Role> roles = new HashSet<Role>();
					for (final Role roleOfScope : grantableRoles) {
						if (roleOfScope.getScope().getName().equals(originalScope.getName())) {
							roles.add(roleOfScope);
						}
					}

					// Create a special version of a ClientScope
					// object for
					// which we can set the roles.
					final ClientScope scope = new ClientScope();
					scope.copy(originalScope);
					scope.setRoles(roles);
					scopes.add(scope);
					scopeNames.add(scope.getName());
				}
			}
		}

		return scopes;
	}
}
