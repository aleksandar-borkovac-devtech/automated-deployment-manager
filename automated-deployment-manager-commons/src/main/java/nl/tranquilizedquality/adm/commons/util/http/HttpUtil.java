package nl.tranquilizedquality.adm.commons.util.http;

import java.io.File;
import java.io.IOException;

/**
 * HTTP utility to download a file from a source URL.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 11 okt. 2011
 */
public interface HttpUtil {

    /**
     * Downloads a file from the specified source URL.
     * 
     * @param sourceUrl
     *            The download URL.
     * @param destinationPath
     *            The destination directory.
     * @return Returns the downloaded {@link File}.
     * @throws IOException
     *             Is thrown when something goes wrong when downloading the
     *             file.
     */
    File downloadFile(String sourceUrl, String destinationPath, String destinationFileName)
            throws IOException;

}
