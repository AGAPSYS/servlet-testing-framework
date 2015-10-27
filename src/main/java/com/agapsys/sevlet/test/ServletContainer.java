/*
 * Copyright 2015 Agapsys Tecnologia Ltda-ME.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.agapsys.sevlet.test;

import com.agapsys.http.HttpClient;
import com.agapsys.http.HttpRequest;
import com.agapsys.http.HttpResponse;
import com.agapsys.http.HttpResponse.StringResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServlet;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.log.StdErrLog;

/** Represents a servlet container */
public class ServletContainer {
	// CLASS SCOPE =============================================================
	private static class NoLogger implements Logger {
		// CLASS SCOPE =========================================================
		private static NoLogger singletonInstance = null;
		
		public static NoLogger getSingletonInstance() {
			if (singletonInstance == null)
				singletonInstance = new NoLogger();
			
			return singletonInstance;
		}
		// =====================================================================
		
		// INSTANCE SCOPE ======================================================
		private NoLogger() {}
		
		@Override public String getName() { return ""; }
		@Override public void warn(String msg, Object... args) { }
		@Override public void warn(Throwable thrown) { }
		@Override public void warn(String msg, Throwable thrown) { }
		@Override public void info(String msg, Object... args) { }
		@Override public void info(Throwable thrown) { }
		@Override public void info(String msg, Throwable thrown) { }
		@Override public boolean isDebugEnabled() { return false; }
		@Override public void setDebugEnabled(boolean enabled) { }
		@Override public void debug(String msg, Object... args) { }
		@Override public void debug(Throwable thrown) { }
		@Override public void debug(String msg, Throwable thrown) { }
		@Override public Logger getLogger(String name) { return this; }
		@Override public void ignore(Throwable ignored) { }
		@Override public void debug(String arg0, long arg1) { }
		// =====================================================================
	}

	/** 
	 * Convenience method to create a container having only servlets.
	 * @param servlets servlets registered with the root context
	 * @return Servlet container with a root application having only given servlets
	 */
	public static ServletContainer getInstance(Class<? extends HttpServlet>...servlets) {
		if (servlets.length == 0) throw new IllegalArgumentException("Missing servlets");
		
		ApplicationContext context = new ApplicationContext();
		for (Class<? extends HttpServlet> servletClass : servlets) {
			context.registerServlet(servletClass);
		}
		
		ServletContainer sc = new ServletContainer();
		sc.registerContext(context);
		return sc;
	}
	
	static {
		Map<String, Logger> loggers = org.eclipse.jetty.util.log.Log.getLoggers();
		for (Map.Entry<String, Logger> entry : loggers.entrySet()) {
			Logger logger = entry.getValue();
			
			((StdErrLog)logger).setLevel(StdErrLog.LEVEL_OFF);
		}
		
		org.eclipse.jetty.util.log.Log.setLog(NoLogger.getSingletonInstance());
	}
	// =========================================================================
	
	// INSTANCE SCOPE ==========================================================
	private final Server server;
	private final ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();
	private final Map<String, ApplicationContext> contextMap = new HashMap<>();
	
	/** Constructor. */
	public ServletContainer() {
		server = new Server(0);
	}
	
	/** 
	 * Returns the local TCP port used by the server.
	 * @return the local TCP port used by the server.
	 */
	public int getLocalPort() {
		if (!isRunning())
			throw new IllegalStateException("Server is not running");
		
		return ((ServerConnector) server.getConnectors()[0]).getLocalPort();
	}
	
	/**
	 * Register an application {@link ApplicationContext context}
	 * @param context application context
	 * @param contextPath associated URL
	 */
	public void registerContext(ApplicationContext context, String contextPath) {
		if (contextPath == null || contextPath.isEmpty())
			throw new IllegalArgumentException("Null/empty contextPath");
		
		if (context == null)
			throw new IllegalArgumentException("Null context");
		
		if (contextMap.containsKey(contextPath))
			throw new IllegalArgumentException("Context path is already registered: " + contextPath);

		contextMap.put(contextPath, context);
		context.getContextHandler().setContextPath(contextPath);
	}
	
	/**
	 * Registers an application in root contextPath
	 * @param context application context
	 */
	public void registerContext(ApplicationContext context) {
		registerContext(context, "/");
	}
	
	/**  Starts server. */
	public void startServer() {
		// Register contexts with the server...
		for(Map.Entry<String, ApplicationContext> entry : contextMap.entrySet()) {
			
			String             contextPath = entry.getKey();
			ApplicationContext context     = entry.getValue();
			
			context.getContextHandler().setContextPath(contextPath);
		}
		
		Handler[] handlers = new Handler[contextMap.size()];
		
		int i = 0;
		for(ApplicationContext contextMock : contextMap.values()) {
			handlers[i] = contextMock.getContextHandler();
			i++;
		}
		
		contextHandlerCollection.setHandlers(handlers);
		server.setHandler(contextHandlerCollection);
		try {
			server.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/** Stops the server. */
	public void stopServer() {
		try {
			server.stop();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Returns a boolean indicating if server is running.
	 * @return a boolean indicating if server is running.
	 */
	public boolean isRunning() {
		return server.isRunning();
	}
	
	/**
	 * Perform a request against this servlet container.
	 * @param client {@linkplain HttpClient} instance
	 * @param request {@linkplain HttpRequest} instance
	 * @return response
	 */
	public StringResponse doRequest(HttpClient client, HttpRequest request) {
		// Change URI to use servlet container
		String oldUri = request.getUri();
		
		if (oldUri == null || oldUri.isEmpty())
			throw new IllegalArgumentException("Null/Empty uri");
		
		if (oldUri.contains(":") || oldUri.contains(" ") || !oldUri.startsWith("/"))
			throw new IllegalArgumentException("Invalid uri: " + oldUri);
		
		request.setUri(String.format("http://localhost:%d%s", getLocalPort(), oldUri));
		
		StringResponse resp;
		try {
			resp = HttpResponse.getStringResponse(client, request, "utf-8", -1);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		// Restore servlet URI
		request.setUri(oldUri);
		
		return resp;
	}
	
	/** 
	 * Perform a request against this servlet container.
	 * @param request {@linkplain HttpRequest} instance
	 * @return response
	 */
	public StringResponse doRequest(HttpRequest request) {
		try {
			HttpClient client = new HttpClient();
			StringResponse response = doRequest(client, request);
			client.close();
			return response;
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
	// =========================================================================
}