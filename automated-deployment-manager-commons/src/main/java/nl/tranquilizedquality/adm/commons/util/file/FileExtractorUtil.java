/**
 * <pre>
 * Project: automated-deployment-manager-commons Created on: 5 nov. 2011 File: FileExtractorUtil.java
 * Package: nl.tranquilizedquality.adm.commons.util.file
 * 
 * Copyright (c) 2011 Tranquilized Quality www.Tranquilized Quality.nl All rights
 * reserved.
 * 
 * This software is the confidential and proprietary information of Tranquilized Quality
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Tranquilized Quality.
 * </pre>
 */
package nl.tranquilizedquality.adm.commons.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPInputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;

/**
 * Utility that can extract TAR and TAR.GZ files.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 5 nov. 2011
 */
public class FileExtractorUtil {

    /**
     * Extracts the specified tar.gz file to the specified destination
     * directory.
     * 
     * @param tarGzipFile
     *        The tar.gz file that will be extracted.
     * @param destinationDirectory
     *        The destination directory.
     * @throws Exception
     *         Is thrown when something goes wrong during extraction of the
     *         file.
     */
    public static void extractTarGz(final File tarGzipFile, final File destinationDirectory) throws Exception {
        destinationDirectory.mkdirs();
        TarInputStream tin = new TarInputStream(new GZIPInputStream(new FileInputStream(tarGzipFile)));
        TarEntry tarEntry = tin.getNextEntry();

        while (tarEntry != null) {
            final String destinationFileName = destinationDirectory.toString() + File.separatorChar + tarEntry.getName();
            final File destPath = new File(destinationFileName);

            /*
             * FIXME: For some reason this returns true if we extract a *.rsh file on Linux.
             */
            if (tarEntry.isDirectory() && !StringUtils.contains(tarEntry.getName(), ".rsh")) {
                destPath.mkdirs();
            } else {
                final String name = tarEntry.getName();
                final String directory = StringUtils.substringBeforeLast(name, "/");
                final String destinationDirectoryName = destinationDirectory.toString() + File.separatorChar + directory;
                final File dir = new File(destinationDirectoryName);
                dir.mkdirs();

                final FileOutputStream fout = new FileOutputStream(destPath);

                tin.copyEntryContents(fout);

                fout.close();

                /*
                 * Don't know for sure what this mode means but now it's used to
                 * make the file executable.
                 */
                destPath.setExecutable(true);
            }
            tarEntry = tin.getNextEntry();
        }

        tin.close();
        tin = null;
    }

}
