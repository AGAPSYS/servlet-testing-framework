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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.util.log.Logger;

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
		
		@Override public String getName() { return "no"; }
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
	
	static {
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
	 * @return the local TCP port used by the server
	 * @throws IllegalStateException if server is not running
	 */
	public int getLocalPort() throws IllegalStateException {
		if (!isRunning())
			throw new IllegalStateException("Server is not running");
		
		return ((ServerConnector) server.getConnectors()[0]).getLocalPort();
	}
	
	/**
	 * Register an application {@link ApplicationContext context}
	 * @param context application context
	 * @param contextPath associated URL
	 * @throws IllegalArgumentException if any of the following conditions occurs:
	 * <ul>
	 *		<li>contextPath == null || contextPath.isEmpty()</li>
	 *		<li>context == null</li>
	 *		<li>contextPath is already registered</li>
	 * </ul>
	 */
	public void registerContext(ApplicationContext context, String contextPath) throws IllegalArgumentException {
		if (contextPath == null || contextPath.isEmpty())
			throw new IllegalArgumentException("Null/empty contextPath");
		
		if (context == null)
			throw new IllegalArgumentException("Null context");
		
		if (contextMap.containsKey(contextPath))
			throw new IllegalArgumentException("Context path is already registered: " + contextPath);

		contextMap.put(contextPath, context);
	}
	
	/**
	 * Registers an application in root contextPath
	 * @param context application context
	 * @throws IllegalArgumentException if context == null
	 */
	public void registerContext(ApplicationContext context) throws IllegalArgumentException {
		registerContext(context, "/");
	}
	
	/**
	 * Starts server
	 * @throws RuntimeException if server fails to start
	 */
	public void startServer() throws RuntimeException {
		// Register contexts with the server...
		for(Map.Entry<String, ApplicationContext> entry : contextMap.entrySet()) {
			ApplicationContext context = entry.getValue();
			String contextPath = entry.getKey();
			
			context.getContextHandler().setContextPath(contextPath);
		}
		
		Handler[] handlers = new Handler[contextMap.size()];
		
		int i = 0;
		for(ApplicationContext contextMock : contextMap.values()) {
			handlers[i] = contextMock.getContextHandler();
		}
		
		contextHandlerCollection.setHandlers(handlers);
		server.setHandler(contextHandlerCollection);
		try {
			server.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/** 
	 * Stops the server.
	 * @throws RuntimeException if server fails to stop
	 */
	public void stopServer() throws RuntimeException{
		try {
			server.stop();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/** @return a boolean indicating if server is running. */
	public boolean isRunning() {
		return server.isRunning();
	}
	
	/**
	 * Generic request method when a client instance is available
	 * @param client {@linkplain HttpClient} instance
	 * @param request {@linkplain HttpRequest} instance
	 * @return response
	 * @throws IllegalArgumentException if client == null  or request == null
	 * @throws RuntimeException if request fails
	 */
	protected HttpResponse doRequest(HttpClient client, HttpRequest request) throws IllegalArgumentException, RuntimeException {
		if (client == null)
			throw new IllegalArgumentException("Null client");
		
		if (request == null)
			throw new IllegalArgumentException("Null request");
		
		request.beforeSend();
		
		try {
			return new HttpResponse(client.getCoreClient().execute(request.getCoreRequest()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/** 
	 * Generic request method
	 * @param request {@linkplain HttpRequest} instance
	 * @return response
	 * @throws RuntimeException if request fails
	 */
	protected HttpResponse doRequest(HttpRequest request) throws RuntimeException {
		HttpClient httpclient = new HttpClient();
		return doRequest(httpclient, request);
	}
	
	/**
	 * Performs a GET request to this server
	 * @param client {@linkplain HttpClient} instance
	 * @param request {@linkplain HttpGet GET request} instance
	 * @return server {@link HttpResponse response}
	 * @throws IllegalArgumentException if client == null or request == null
	 * @throws RuntimeException if request fails
	 */
	public HttpResponse doGet(HttpClient client, HttpGet request) throws IllegalArgumentException, RuntimeException {
		return doRequest(client, request);
	}
	
	/**
	 * Performs a GET request to this server.
	 * This is a convenience method for doGet(client, new HttpGet(this, uri))
	 * @see ServletContainer#doGet(HttpClient, HttpGet)
	 * @param client {@linkplain HttpClient} instance
	 * @param uri request URI
	 */
	public HttpResponse doGet(HttpClient client, String uri) throws IllegalArgumentException, RuntimeException {
		return doGet(client, new HttpGet(this, uri));
	}
	
	/**
	 * Performs a GET request to this server
	 * @param request {@linkplain HttpGet GET request} instance
	 * @return server {@link HttpResponse response}
	 * @throws IllegalArgumentException if request == null
	 * @throws RuntimeException if request fails
	 */
	public HttpResponse doGet(HttpGet request) throws IllegalArgumentException, RuntimeException {
		return doRequest(request);
	}
	
	/**
	 * Performs a GET request to this server.
	 * This is a convenience method for doGet(new HttpGet(this, uri))
	 * @see ServletContainer#doGet(HttpGet)
	 * @param uri request URI
	 */
	public HttpResponse doGet(String uri) throws IllegalArgumentException, RuntimeException {
		return doGet(new HttpGet(this, uri));
	}
	
	/**
	 * Performs a POST request to this server
	 * @param client {@linkplain HttpClient} instance
	 * @param request {@linkplain HttpPost POST request} instance
	 * @return server {@link HttpResponse response}
	 * @throws IllegalArgumentException if client == null or request == null
	 * @throws RuntimeException if request fails
	 */
	public HttpResponse doPost(HttpClient client, HttpPost request) throws IllegalArgumentException, RuntimeException {
		return doRequest(client, request);
	}

	/**
	 * Performs a POST request to this server
	 * @param request {@linkplain HttpPost POST request} instance
	 * @return server {@link HttpResponse response}
	 * @throws IllegalArgumentException if request == null
	 * @throws RuntimeException if request fails
	 */
	public HttpResponse doPost(HttpPost request) throws IllegalArgumentException, RuntimeException {
		return doRequest(request);
	}

	/**
	 * Performs a DELETE request to this server
	 * @param client {@linkplain HttpClient} instance
	 * @param request {@linkplain HttpDelete DELETE request} instance
	 * @return server {@link HttpResponse response}
	 * @throws IllegalArgumentException if client == null or request == null
	 * @throws RuntimeException if request fails
	 */
	public HttpResponse doDelete(HttpClient client, HttpDelete request) throws IllegalArgumentException, RuntimeException {
		return doRequest(client, request);
	}

	/**
	 * Performs a DELETE request to this server
	 * @param request {@linkplain HttpDelete DELETE request} instance
	 * @return server {@link HttpResponse response}
	 * @throws IllegalArgumentException if client == null or request == null
	 * @throws RuntimeException if request fails
	 */
	public HttpResponse doDelete(HttpDelete request) throws IllegalArgumentException, RuntimeException {
		return doRequest(request);
	}
	
	/**
	 * Performs a HEAD request to this server
	 * @param client {@linkplain HttpClient} instance
	 * @param request {@linkplain HttpHead HEAD request} instance
	 * @return server {@link HttpResponse response}
	 * @throws IllegalArgumentException if client == null or request == null
	 * @throws RuntimeException if request fails
	 */
	public HttpResponse doHead(HttpClient client, HttpHead request) throws IllegalArgumentException, RuntimeException {
		return doRequest(client, request);
	}
	
	/**
	 * Performs a HEAD request to this server
	 * @param request {@linkplain HttpHead HEAD request} instance
	 * @return server {@link HttpResponse response}
	 * @throws IllegalArgumentException if request == null
	 * @throws RuntimeException if request fails
	 */
	public HttpResponse doHead(HttpHead request) throws IllegalArgumentException, RuntimeException {
		return doRequest(request);
	}
	
	/**
	 * Performs a OPTIONS request to this server
	 * @param client {@linkplain HttpClient} instance
	 * @param request {@linkplain HttpOptions OPTIONS request} instance
	 * @return server {@link HttpResponse response}
	 * @throws IllegalArgumentException if client == null or request == null
	 * @throws RuntimeException if request fails
	 */
	public HttpResponse doOptions(HttpClient client, HttpOptions request) throws IllegalArgumentException, RuntimeException {
		return doRequest(client, request);
	}
	
	/**
	 * Performs a OPTIONS request to this server
	 * @param request {@linkplain HttpOptions OPTIONS request} instance
	 * @return server {@link HttpResponse response}
	 * @throws IllegalArgumentException if request == null
	 * @throws RuntimeException if request fails
	 */
	public HttpResponse doOptions(HttpOptions request) throws IllegalArgumentException, RuntimeException {
		return doRequest(request);
	}
	
	/**
	 * Performs a PUT request to this server
	 * @param client {@linkplain HttpClient} instance
	 * @param request {@linkplain HttpPut PUT request} instance
	 * @return server {@link HttpResponse response}
	 * @throws IllegalArgumentException if client == null or request == null
	 * @throws RuntimeException if request fails
	 */
	public HttpResponse doPut(HttpClient client, HttpPut request) throws IllegalArgumentException, RuntimeException {
		return doRequest(client, request);
	}
	
	/**
	 * Performs a PUT request to this server
	 * @param request {@linkplain HttpPut PUT request} instance
	 * @return server {@link HttpResponse response}
	 * @throws IllegalArgumentException if request == null
	 * @throws RuntimeException if request fails
	 */
	public HttpResponse doPut(HttpPut request) throws IllegalArgumentException, RuntimeException {
		return doRequest(request);
	}

	/**
	 * Performs a TRACE request to this server
	 * @param client {@linkplain HttpClient} instance
	 * @param request {@linkplain HttpTrace TRACE request} instance
	 * @return server {@link HttpResponse response}
	 * @throws IllegalArgumentException if client == null or request == null
	 * @throws RuntimeException if request fails
	 */
	public HttpResponse doTrace(HttpClient client, HttpTrace request) throws IllegalArgumentException, RuntimeException {
		return doRequest(client, request);
	}
	
	/**
	 * Performs a TRACE request to this server
	 * @param request {@linkplain HttpTrace TRACE request} instance
	 * @return server {@link HttpResponse response}
	 * @throws IllegalArgumentException if request == null
	 * @throws RuntimeException if request fails
	 */
	public HttpResponse doTrace(HttpTrace request) throws IllegalArgumentException, RuntimeException {
		return doRequest(request);
	}
	
	
	/**
	 * Performs a PATCH request to this server
	 * @param client {@linkplain HttpClient} instance
	 * @param request {@linkplain HttpPatch PATCH request} instance
	 * @return server {@link HttpResponse response}
	 * @throws IllegalArgumentException if client == null or request == null
	 * @throws RuntimeException if request fails
	 */
	public HttpResponse doPut(HttpClient client, HttpPatch request) throws IllegalArgumentException, RuntimeException {
		return doRequest(client, request);
	}
	
	/**
	 * Performs a PATCH request to this server
	 * @param request {@linkplain HttpPatch PATCH request} instance
	 * @return server {@link HttpResponse response}
	 * @throws IllegalArgumentException if request == null
	 * @throws RuntimeException if request fails
	 */
	public HttpResponse doPut(HttpPatch request) throws IllegalArgumentException, RuntimeException {
		return doRequest(request);
	}
	// =========================================================================
}