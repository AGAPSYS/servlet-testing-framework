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

import org.apache.http.client.methods.HttpRequestBase;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

/** Represents a servlet container */
public class ServletContainter {
	// INSTANCE SCOPE ==========================================================
	private final Server server;
	private final ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();
	private final Map<String, AppContext> contextMap = new HashMap<>();
	
	/** Constructor. */
	public ServletContainter() {
		server = new Server(0);
	}
	
	/** 
	 * Returns the local TCP port used by the server
	 * @throws IllegalStateException if server is not running
	 */
	public int getLocalPort() throws IllegalStateException {
		if (!isRunning())
			throw new IllegalStateException("Server is not running");
		
		return ((ServerConnector) server.getConnectors()[0]).getLocalPort();
	}
	
	/**
	 * Register an application {@link AppContext context}
	 * @param context application context
	 * @param contextPath associated URL
	 * @throws IllegalArgumentException if any of the following conditions occurs:
	 * <ul>
	 *		<li>contextPath == null || contextPath.isEmpty()</li>
	 *		<li>context == null</li>
	 *		<li>contextPath is already registered</li>
	 * </ul>
	 */
	public void registerContext(AppContext context, String contextPath) throws IllegalArgumentException {
		if (contextPath == null || contextPath.isEmpty())
			throw new IllegalArgumentException("Null/empty contextPath");
		
		if (context == null)
			throw new IllegalArgumentException("Null context");
		
		if (contextMap.containsKey(contextPath))
			throw new IllegalArgumentException("Context path is already registered: " + contextPath);

		contextMap.put(contextPath, context);
	}
	
	/**
	 * Starts server
	 * @throws RuntimeException if server fails to start
	 */
	public void startServer() throws RuntimeException {
		// Register contexts with the server...
		for(Map.Entry<String, AppContext> entry : contextMap.entrySet()) {
			AppContext context = entry.getValue();
			String contextPath = entry.getKey();
			
			context.getContextHandler().setContextPath(contextPath);
		}
		
		Handler[] handlers = new Handler[contextMap.size()];
		
		int i = 0;
		for(AppContext contextMock : contextMap.values()) {
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
	
	/** Returns a boolean indicating if server is running. */
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
	private HttpResponse doRequest(HttpClient client, HttpRequest request) throws IllegalArgumentException, RuntimeException {
		if (client == null)
			throw new IllegalArgumentException("Null client");
		
		if (request == null)
			throw new IllegalArgumentException("Null request");
		
		request.doPreSend();
		
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
	private HttpResponse doRequest(HttpRequest request) throws RuntimeException {
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
	// =========================================================================
}