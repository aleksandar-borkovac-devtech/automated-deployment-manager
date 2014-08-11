package nl.tranquilizedquality.adm.security.business.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Manager that manages files.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public interface ImageFileRepositoryManager {

    /**
     * Searches for the image with the specified unique identifier.
     * 
     * @param id
     *            The unique identifier of the image.
     * @return Returns a {@link File}.
     * @throws IOException
     *             Is thrown when something went wrong during file access.
     */
    File findImageById(Long id) throws IOException;

    /**
     * Stores an image with the specified unique identifier.
     * 
     * @param id
     *            The unique identifier of the image.
     * @param image
     *            The image itself.
     * @throws IOException
     *             Is thrown when something goes wrong during file storage.
     */
    void storeImage(Long id, byte[] image) throws IOException;

    /**
     * Retrieves the default picture that is displayed for a user if he/she
     * doesn't have a picture. Also used if the office that is doing
     * administration is not allowed to see the member picture.
     * 
     * @return Returns the default image for a user.
     * @throws FileNotFoundException
     *             Is thrown if the image is not found.
     */
    File findUnknownUserImage() throws FileNotFoundException;

}
