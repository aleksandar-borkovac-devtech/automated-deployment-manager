package nl.tranquilizedquality.adm.commons.util.file;

import java.io.File;

import nl.tranquilizedquality.adm.commons.util.file.FileExtractorUtil;

import org.junit.Test;

/**
 * Test for the {@link FileExtractorUtil}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 5 nov. 2011
 */
public class FileExtractorUtilTest {

	@Test
	public void testExtractTarGz() throws Exception {
		final File tarGzipFile = new File("src/test/resources/dist.tar.gz");
		final File dest = new File("target/extracted/");

		FileExtractorUtil.extractTarGz(tarGzipFile, dest);

	}

}
