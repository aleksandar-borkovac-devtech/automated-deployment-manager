package nl.tranquilizedquality.adm.commons.gwt.ext.controller;

import java.lang.reflect.Method;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.SerializationPolicy;

/**
 * mplementation of a {@link Controller} that will intercept GWT RPC calls and
 * handle them.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 */
public class GwtRpcController extends RemoteServiceServlet implements Controller,
		ServletContextAware {

	/**
	 * Unique identifier used for serialization.
	 */
	private static final long serialVersionUID = -2331371548698868301L;

	/** The servlet application context. */
	private ServletContext servletContext;

	/** The {@link RemoteService} that will be used to do the GWT RPC call. */
	private RemoteService remoteService;

	/** The class name of the remote service class. */
	@SuppressWarnings({ "rawtypes" })
	private Class remoteServiceClass;

	public ModelAndView handleRequest(final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		/*
		 * Call the post on the RemoteServiceServlet so the processCall can be
		 * called.
		 */
		super.doPost(request, response);

		return null;
	}

	@Override
	public String processCall(final String payload) throws SerializationException {
		try {
			// Extract request information from the payload
			final RPCRequest rpcRequest = RPC.decodeRequest(payload, this.remoteServiceClass, this);
			final Method targetMethod = rpcRequest.getMethod();
			final Object[] parameters = rpcRequest.getParameters();
			final SerializationPolicy policy = rpcRequest.getSerializationPolicy();

			// Delegate work to the spring injected service
			return RPC.invokeAndEncodeResponse(this.remoteService, targetMethod, parameters, policy);
		}
		catch (final IncompatibleRemoteServiceException e) {
			return RPC.encodeResponseForFailure(null, e);
		}
	}

	@Override
	public ServletContext getServletContext() {
		return servletContext;
	}

	public void setServletContext(final ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@Required
	public void setRemoteService(final RemoteService remoteService) {
		this.remoteService = remoteService;
		this.remoteServiceClass = this.remoteService.getClass();
	}

}
