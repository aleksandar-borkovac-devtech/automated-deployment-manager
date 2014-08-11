package nl.tranquilizedquality.adm.commons.business.domain;

/**
 * Status of a deploy attempt.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jun. 2011
 */
public enum DeployStatus {

    /** Determines that a release is still being deployed. */
    ONGOING,

    /** Determines that the deploy was successfully done. */
    SUCCESS,

    /** Determines that the deploy failed. */
    FAILED;

}
