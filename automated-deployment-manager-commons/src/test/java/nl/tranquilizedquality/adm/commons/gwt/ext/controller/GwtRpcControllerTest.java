package nl.tranquilizedquality.adm.commons.gwt.ext.controller;

import static org.junit.Assert.assertEquals;

import nl.tranquilizedquality.adm.commons.gwt.ext.controller.GwtRpcController;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.mvc.Controller;

/**
 * Test for {@link GwtRpcController}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 */
public class GwtRpcControllerTest {

	/** The {@link Controller} that will be tested. */
	private GwtRpcController controller;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		controller = new GwtRpcController();
	}

	@Test
	public void testProcessCallString() throws Exception {

		final String processCall = controller.processCall("badcall");

		assertEquals("//EX[2,1,[\"com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException/3936916533\",\"This application is out of date, please click the refresh button on your browser. ( Malformed or old RPC message received - expecting version 5 )\"],0,5]", processCall);
	}

}
