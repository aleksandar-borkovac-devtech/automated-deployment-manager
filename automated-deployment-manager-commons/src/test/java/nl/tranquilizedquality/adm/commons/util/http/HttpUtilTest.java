package nl.tranquilizedquality.adm.commons.util.http;

import nl.tranquilizedquality.adm.commons.util.http.HttpUtil;
import nl.tranquilizedquality.adm.commons.util.http.HttpUtilImpl;

import org.junit.Before;
import org.junit.Test;

public class HttpUtilTest {

	private HttpUtil httpUtil;

	@Before
	public void setUp() throws Exception {
		httpUtil = new HttpUtilImpl();
	}

	@Test
	public void testDownloadFile() throws Exception {

		// httpUtil.downloadFile("http://cybertron/nexus/content/repositories/releases/nl/Tranquilized Quality/dam/dam-gwt-gui/1.0.0-M2/dam-gwt-gui-1.0.0-M2.war",
		// "target/release/", "dam-gwt-gui-1.0.0-M2.war");
	}

}
