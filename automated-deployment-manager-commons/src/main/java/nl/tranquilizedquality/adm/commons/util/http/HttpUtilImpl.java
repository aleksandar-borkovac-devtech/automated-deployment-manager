/**
 * <pre>
 * Project: automated-deployment-manager-commons Created on: 3 jul. 2011 File: HttpUtilImpl.java
 * Package: nl.tranquilizedquality.adm.commons.util.http
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
package nl.tranquilizedquality.adm.commons.util.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * Utility class to download files.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jul. 2011
 */
public class HttpUtilImpl implements HttpUtil {

	@Override
	public File downloadFile(final String sourceUrl, final String destinationPath, final String destinationFileName) throws IOException {
		final HttpClient client = new HttpClient();
		final GetMethod getMethod = new GetMethod();
		final URI uri = new URI(sourceUrl, false);
		getMethod.setURI(uri);

		final int result = client.executeMethod(getMethod);

		if (result != 200) {
			final String msg = "Failed to download file! -> " + sourceUrl;

			throw new RuntimeException(msg);
		}

		/*
		 * Download the file.
		 */
		final InputStream inputStream = getMethod.getResponseBodyAsStream();
		final byte[] rawBytes = IOUtils.toByteArray(inputStream);

		/*
		 * Write the file to disk.
		 */
		final File destinationDirectory = new File(destinationPath);
		FileUtils.forceMkdir(destinationDirectory);

		final File destination = new File(destinationDirectory + "/" + destinationFileName);
		final FileOutputStream fileOutputStream = new FileOutputStream(destination);
		IOUtils.write(rawBytes, fileOutputStream);

		/*
		 * Release the HTTP connection.
		 */
		getMethod.releaseConnection();

		return destination;
	}

}
