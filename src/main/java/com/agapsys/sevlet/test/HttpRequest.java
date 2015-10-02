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

import com.agapsys.sevlet.test.util.NameValuePair;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpRequestBase;

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
	private final ServletContainer servletContainter;
	private final String uri;
	
	private HttpRequestBase coreRequest = null;
	
	/** 
	 * Constructor.
	 * @param servletContainer the {@link ServletContainer} associated to this request
	 * @param uri request URI (request will be performed against given {@link ServletContainer}.
	 * @throws IllegalArgumentException if given URI is invalid
	 */
	public HttpRequest(ServletContainer servletContainer, String uri) throws IllegalArgumentException {
		if (servletContainer == null)
			throw new IllegalArgumentException("Null servletContainer");
		
		this.servletContainter = servletContainer;
		
		if (uri == null || uri.isEmpty())
			throw new IllegalArgumentException("Null/Empty uri");
		
		if (uri.contains(":") || uri.contains(" ") || !uri.startsWith("/"))
			throw new IllegalArgumentException("Invalid uri: " + uri);
		
		this.uri = uri;
	}
	
	abstract HttpRequestBase getCoreRequest(String uri);
	
	/** Returns wrapped {@linkplain HttpRequestBase}. */
	final HttpRequestBase getCoreRequest() {
		if (coreRequest == null) {
			coreRequest = getCoreRequest(getUri());
		}
		
		return coreRequest;
	}
		
	/** Performs required actions with wrapped {@linkplain HttpRequestBase}. */
	void beforeSend() {
		try {
			getCoreRequest().setURI(new URI(String.format("http://localhost:%d%s", servletContainter.getLocalPort(), getUri())));
		} catch (URISyntaxException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	/** @return The name of HTTP method associated with this request */
	public String getMethod() {
		return getCoreRequest().getMethod();
	}

	/** @return Associated servlet container. */
	public ServletContainer getServletContainter() {
		return servletContainter;
	}
	
	/** @return Request URI. */
	public String getUri() {
		return uri;
	}

	@Override
	public String toString() {
		return String.format("%s %s", getMethod(), getUri());
	}
	
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

	/** @return All headers associated to this request. */
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
	 * @return All registered headers with given name. If there are no such headers, an empty set is returned
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
	 * @param name name of the headers to be removed.
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
