package nl.tranquilizedquality.adm.security.business.manager.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import nl.tranquilizedquality.adm.security.business.manager.ImageFileRepositoryManager;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Required;

/**
 * Repository manager that handle image files.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public class ImageFileRepositoryManagerImpl implements ImageFileRepositoryManager {

    private static final String JPG = ".jpg";

    /** Path on the server where member images are stored. */
    private String imageLocation;

    @Override
    public File findImageById(final Long id) throws IOException {

        File imageFile = null;
        String location = null;

        location = imageLocation + id + JPG;

        imageFile = new File(location);
        final boolean exists = imageFile.exists();

        if (exists) {
            return imageFile;
        }
        else {
            return findUnknownUserImage();
        }
    }

    @Override
    public File findUnknownUserImage() throws FileNotFoundException {
        boolean exists = false;
        final String location = imageLocation + "unknown-user.png";
        final File imageFile = new File(location);
        exists = imageFile.exists();

        if (!exists) {
            throw new FileNotFoundException("Couldn't find file: " + location);
        }

        return imageFile;
    }

    @Override
    public void storeImage(final Long id, final byte[] image) throws IOException {
        File imageFile = null;
        String location = null;

        location = imageLocation + id + JPG;

        imageFile = new File(location);

        final OutputStream output = new FileOutputStream(imageFile);
        IOUtils.write(image, output);

        output.close();
    }

    @Required
    public void setImageLocation(final String imageLocation) {
        this.imageLocation = imageLocation;
    }

}
