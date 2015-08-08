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
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
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
	private String uriBase;
	private final Map<String, String> parameters = new LinkedHashMap<>();
	
	private void parseUri(String uri) throws IllegalArgumentException {
		int paramDelimiterIndex = uri.indexOf("?");
		
		String baseUri;
	
		if (paramDelimiterIndex != -1) {
			baseUri = uri.substring(0, paramDelimiterIndex);
		} else {
			baseUri = uri;
		}
		
		if (paramDelimiterIndex != -1) {
			String paramsStr = uri.substring(paramDelimiterIndex + 1);
			String[] paramTokens = paramsStr.split("&");
			
			for (String paramToken : paramTokens) {
				String[] paramTokenPair = paramToken.split("=");
				
				if (paramTokenPair.length != 2) {
					if (paramTokenPair.length > 0)
						throw new IllegalArgumentException("Malformed parameter: " + paramTokenPair[0]);
					else
						throw new IllegalArgumentException("Malformed uri: " + uri);
				}
				
				try {
					String name = URLDecoder.decode(paramTokenPair[0], "UTF-8");
					String value = URLDecoder.decode(paramTokenPair[1], "UTF-8");
					parameters.put(name, value);

				} catch (UnsupportedEncodingException ex) {
					throw new RuntimeException(ex);
				}
			}
		}
		
		this.uriBase = baseUri;
	}
	
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
		
		parseUri(uri);
	}
	
	/** Returns wrapped {@linkplain HttpRequestBase}. */
	abstract HttpRequestBase getCoreRequest();
	
	/** Performs required actions with wrapped {@linkplain HttpRequestBase}. */
	void doPreSend() {
		try {
			setCoreParameters(getCoreRequest(), parameters);
			getCoreRequest().setURI(new URI(String.format("http://localhost:%d%s", servletContainter.getLocalPort(), getUri())));
		} catch (URISyntaxException ignore) {}
	}
	
	/** Set request parameters into coreRequest object. Default implementation does nothing */
	void setCoreParameters(HttpRequestBase coreRequest, Map<String, String> parameters) {}
	
	/** @return The name of HTTP method associated with this request */
	public String getMethod() {
		return getCoreRequest().getMethod();
	}

	/** @return Associated servlet container. */
	public ServletContainer getServletContainter() {
		return servletContainter;
	}

	/** @return The URI-base (URI without any parameter information). */
	String getUriBase() {
		return uriBase;
	}
	
	/** @return Request URI. */
	public String getUri() {
		StringBuilder sb = new StringBuilder(uriBase + "?");
		try {
			int i = 0;
			for (Map.Entry<String, String> parameter : parameters.entrySet()) {
				if (i > 0)
					sb.append("&");
				
				sb.append(URLEncoder.encode(parameter.getKey(), "UTF-8"));
				sb.append("=");
				sb.append(URLEncoder.encode(parameter.getValue(), "UTF-8"));
				i++;
			}
			return sb.toString();
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public String toString() {
		return getUri();
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
	
	/**
	 * @return The parameter with given name. If there is no such parameter, returns null.
	 * @param name name of the parameter to be retrieved
	 */
	public HttpParameter getParameter(String name) {
		if (parameters.containsKey(name)) {
			String value = parameters.get(name);
			return new HttpParameter(name, value);
		} else {
			return null;
		}
	}
	
	/** @return All parameters associated with this request. */
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
