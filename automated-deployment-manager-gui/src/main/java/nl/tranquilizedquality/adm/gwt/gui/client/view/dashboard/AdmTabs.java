package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard;

import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.TabEnum;

/**
 * Enumeration of all tabs in the application.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 */
public enum AdmTabs implements TabEnum {

    /** Tab where you can manage the releases. */
    RELEASE_MANAGEMENT_TAB,

    /** Tab where you can see the details of a release. */
    RELEASE_DETAILS_TAB,

    /** Tab where the release history is displayed in. */
    RELEASE_HISTORY_TAB,

    /** Tab where the release execution log is displayed in. */
    RELEASE_EXECUTION_LOG_DETAILS_TAB,

    /** Tab where you can manage the repositories. */
    REPOSITORY_MANAGEMENT_TAB,

    /** Tab where you can see the details of a repository. */
    REPOSITORY_DETAILS_TAB,

    /** Tab where you can manage the destinations. */
    DESTINATION_MANAGEMENT_TAB,

    /** Tab where you can see the details of a destination. */
    DESTINATION_DETAILS_TAB,

    /** Tab where you can manage the destination hosts. */
    DESTINATION_HOST_MANAGEMENT_TAB,

    /** Tab where you can see the details of a destination host. */
    DESTINATION_HOSTS_DETAILS_TAB,

    /** Tab where you can manage artifacts. */
    ARTIFACT_MANAGEMENT_TAB,

    /** Tab where you can see the details of an artifact. */
    ARTIFACT_DETAILS_TAB,

    /** Tab where you can manage Maven modules. */
    MAVEN_MODULE_MANAGEMENT_TAB,

    /** Tab where you can see the details of a Maven Module. */
    MAVEN_MODULE_DETAILS_TAB,

    /** Tab where you can manage environments. */
    ENVIRONMENT_MANAGEMENT_TAB,

    /** Tab where you can see the details of an environment. */
    ENVIRONMENT_DETAILS_TAB,

    /** Tab where you can manage user groups. */
    USER_GROUP_MANAGEMENT_TAB,

    /** Tab where you can see the details of a user group. */
    USER_GROUP_DETAILS_TAB,

    /** Tab where the details of a user are displayed on. */
    USER_DETAIL_TAB,

    /** Tab where the user history is displayed on. */
    USER_HISTORY_TAB,

    /** Tab where the details of a scope are displayed on. */
    SCOPE_DETAIL_TAB,

    /** Tab where the details of a role are displayed on. */
    ROLE_DETAIL_TAB,

    /** Tab where the details of a privilege are displayed on. */
    PRIVILEGE_DETAIL_TAB,

    /** Tab where scopes can be managed. */
    SCOPE_MANAGEMENT_TAB,

    /** Tab where users can be managed. */
    USER_MANAGEMENT_TAB,

    /** Tab where you can change your personal details. */
    PERSONAL_DETAIL_TAB;

}
