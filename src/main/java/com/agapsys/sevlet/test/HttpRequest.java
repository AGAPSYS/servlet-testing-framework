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

import com.agapsys.sevlet.test.http.util.NameValuePair;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.params.BasicHttpParams;

/** Represents an HTTP request. */
public abstract class HttpRequest {
	// CLASS SCOPE =============================================================
	/** Represents a HTTP request parameter. */
	public static class HttpParameter extends NameValuePair {
		
		public HttpParameter(String name, String value) throws IllegalArgumentException {
			super(name, value);
		}		
	}
	
	/** Represents a HTTP message header. */
	public static class HttpHeader extends NameValuePair {

		public HttpHeader(String name, String value) throws IllegalArgumentException {
			super(name, value);
		}
	}
	// =========================================================================
	
	// INSTANCE SCOPE ==========================================================
	private final ServletContainter servletContainter;
	private final String uri;
	private final Map<String, String> parameters = new LinkedHashMap<>();
	
	/** 
	 * Constructor.
	 * @param servletContainer the {@linkplain ServletContainter} associated to this request
	 * @param uri request URI (request will be performed against given {@linkplain ServletContainter}.
	 * @throws IllegalArgumentException if given URI is invalid
	 */
	public HttpRequest(ServletContainter servletContainer, String uri) throws IllegalArgumentException {
		if (servletContainer == null)
			throw new IllegalArgumentException("Null servletContainer");
		
		this.servletContainter = servletContainer;
		
		if (uri == null || uri.isEmpty())
			throw new IllegalArgumentException("Null/Empty uri");
		
		if (uri.contains(":"))
			throw new IllegalArgumentException("Invalid uri: " + uri);
		
		this.uri = uri;
	}
	
	/** Returns wrapped {@linkplain HttpRequestBase}. */
	abstract HttpRequestBase getCoreRequest();
	
	/** Performs required actions with wrapped {@linkplain HttpRequestBase}. */
	void doPreSend() {
		try {
			BasicHttpParams params = new BasicHttpParams();
			for (Map.Entry<String, String> entry : parameters.entrySet()) {
				params.setParameter(entry.getKey(), entry.getValue());
			}
			
			getCoreRequest().setParams(params);
			getCoreRequest().setURI(new URI(String.format("http://localhost:%d%s", servletContainter.getLocalPort(), uri)));
		} catch (URISyntaxException ignore) {}
	}
	
	/** returns the name of the method. */
	public String getMethod() {
		return getCoreRequest().getMethod();
	}

	/** Returns associated servlet container. */
	public ServletContainter getServletContainter() {
		return servletContainter;
	}

	/** Returns request URI. */
	public String getUri() {
		return uri;
	}
	
	// Parameters --------------------------------------------------------------
	/**
	 * Adds a parameter to this request.
	 * @param name name of the parameter. Parameter name must be unique in the request. If a parameter with the same name is already associated to this request, its value will be overwritten.
	 * @param value parameter value
	 * @throws IllegalArgumentException if either parameter name is null or empty
	 */
	public void addParameter(String name, String value) throws IllegalArgumentException {
		if (name == null || name.isEmpty())
			throw new IllegalArgumentException("Null/Empty name");
		parameters.put(name, value);
	}

	/**
	 * Adds given parameters to this request.
	 * @param parameters parameters to be added. Parameter names must be unique in the request. If a parameter with the same name is already associated to this request, its value will be overwritten.
	 * @throws IllegalArgumentException if no parameters are passed or any of given parameters is null
	 */
	public void addParameters(HttpParameter...parameters) throws IllegalArgumentException {
		if (parameters.length == 0)
			throw new IllegalArgumentException("Empty parameters");
		
		int i = 0;
		for (HttpParameter parameter : parameters) {
			if (parameter == null)
				throw new IllegalArgumentException("Null parameter on index " + i);
			
			addParameter(parameter.getName(), parameter.getValue());
			i++;
		}
	}
	
	/** Returns the parameter with given name. If there is no such parameter, returns null. */
	public HttpParameter getParameter(String name) {
		if (parameters.containsKey(name)) {
			String value = parameters.get(name);
			return new HttpParameter(name, value);
		} else {
			return null;
		}
	}
	
	/** Returns all parameters associated to this request. */
	public Set<HttpParameter> getParameters() {
		Set<HttpParameter> paramSet = new LinkedHashSet<>();
		for (Map.Entry<String, String> entry : parameters.entrySet()) {
			paramSet.add(new HttpParameter(entry.getKey(), entry.getValue()));
		}
		
		return Collections.unmodifiableSet(paramSet);
	}

	/**
	 * Removes an associated parameter. If there is no such parameter, nothing happens.
	 * @param name name of the parameter to be removed
	 * @throws IllegalArgumentException if name is null/empty
	 */
	public void removeParameter(String name) throws IllegalArgumentException {
		if (name == null || name.isEmpty())
			throw new IllegalArgumentException("Null/Empty name");
		
		parameters.remove(name);
	}
	
	/** Removes all associated parameters */
	public void clearParameters() {
		parameters.clear();
	}
	// -------------------------------------------------------------------------
	
	// Headers -----------------------------------------------------------------
	/**
	 * @return boolean indicating if a header with given name was added to the request
	 * @param name header name
	 */
	public boolean containsHeader(String name) {
		return getCoreRequest().containsHeader(name); //To change body of generated methods, choose Tools | Templates.
	}
	
	/**
	 * Adds given header to the request
	 * @param name header name
	 * @param value header value
	 */
	public void addHeader(String name, String value) {
		getCoreRequest().addHeader(name, value);
	}

	/**
	 *  Adds headers to this request
	 * @param headers Headers to be added
	 * @throws IllegalArgumentException if no headers are passed or a null header is given
	 */
	public void addHeaders(HttpHeader...headers) throws IllegalArgumentException {
		if (headers.length == 0)
			throw new IllegalArgumentException("Empty headers");
		
		int i = 0;
		for (HttpHeader header : headers) {
			if (header == null)
				throw new IllegalArgumentException("Null header on index " + i);
			
			addHeader(header.getName(), header.getValue());
			i++;
		}
	}

	/** Returns all headers associated to this request. */
	public Set<HttpHeader> getHeaders() {
		Set<HttpHeader> headerSet = new LinkedHashSet<>();
		
		Header[] headers = getCoreRequest().getAllHeaders();
		for (Header header : headers) {
			headerSet.add(new HttpHeader(header.getName(), header.getValue()));
		}
		
		return Collections.unmodifiableSet(headerSet);
	}
	
	/**
	 * Returns all headers with given name.
	 * @param name header name
	 * @return all registered headers with given name. If there are no such headers, an empty set is returned
	 * @throws IllegalArgumentException if name is null or empty
	 */
	public Set<HttpHeader> getHeaders(String name) throws IllegalArgumentException {
		if (name == null || name.isEmpty())
			throw new IllegalArgumentException("Null/Empty name");
		
		Set<HttpHeader> headerSet = new LinkedHashSet<>();
		
		Header[] headers = getCoreRequest().getHeaders(name);
		for (Header header : headers) {
			headerSet.add(new HttpHeader(header.getName(), header.getValue()));
		}
		
		return Collections.unmodifiableSet(headerSet);
	}
	
	/**
	 * Removes all headers with given name.
	 * @param name
	 * @throws IllegalArgumentException if name == null or name.isEmpty()
	 */
	public void removeHeaders(String name) throws IllegalArgumentException {
		if (name == null || name.isEmpty())
			throw new IllegalArgumentException("Null/Empty name");
		
		getCoreRequest().removeHeaders(name);
	}

	/** Removes all headers. */
	public void clearHeaders() {
		Header[] coreHeaders = getCoreRequest().getAllHeaders();
		for (Header header : coreHeaders) {
			getCoreRequest().removeHeader(header);
		}
	}
	// -------------------------------------------------------------------------
}
