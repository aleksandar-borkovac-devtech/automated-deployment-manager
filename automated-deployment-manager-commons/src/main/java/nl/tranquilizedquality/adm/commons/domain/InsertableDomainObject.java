package nl.tranquilizedquality.adm.commons.domain;

import java.util.Date;

/**
 * Domain object that registeres the created by and when history.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jun. 2011
 * @param <T>
 *        Primary key type.
 */
public interface InsertableDomainObject<T> extends DomainObject<T> {

    /**
     * Retrieves the date on which this object was created.
     * 
     * @return Returns a {@link Date}.
     */
    Date getCreated();

    /**
     * Retrieves the user this object was created by.
     * 
     * @return Returns a {@link String} representation of the user.
     */
    String getCreatedBy();
}
