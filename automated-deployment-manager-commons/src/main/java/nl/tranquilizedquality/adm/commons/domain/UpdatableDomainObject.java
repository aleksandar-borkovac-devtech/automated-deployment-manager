package nl.tranquilizedquality.adm.commons.domain;

import java.util.Date;

/**
 * Domain object that registers the last update that was made.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jun. 2011
 * @param <T>
 *        Primary key type
 */
public interface UpdatableDomainObject<T> extends InsertableDomainObject<T> {

    /**
     * Retrieves the date on which this object was last updated.
     * 
     * @return Returns a {@link Date}.
     */
    Date getAltered();

    /**
     * Retrieves the user which altered this object the last.
     * 
     * @return Returns a {@link String} representation of the user.
     */
    String getAlteredBy();

}
