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

import com.agapsys.sevlet.test.HttpRequest.HttpHeader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.ProtocolVersion;

/** Represents a response to a {@linkplain HttpRequest} */
public class HttpResponse {
	// CLASS SCOPE =============================================================
	/** Reads a string from given input stream. */
	private static String getStringFromInputStream(InputStream is) throws IOException {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		
		try {
			br = new BufferedReader(new InputStreamReader(is));
			
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} finally {
			if (br != null) {
				br.close();
			}
		}

		return sb.toString();
	}
	// =========================================================================
	
	// INSTANCE SCOPE ==========================================================
	private final org.apache.http.HttpResponse coreResponse;
	private final String responseBody;
	
	/** Wraps a {@linkplain HttpResponse} instance. */
	HttpResponse(org.apache.http.HttpResponse coreResponse) throws IOException {
		this.coreResponse = coreResponse;
		
		HttpEntity entity = coreResponse.getEntity();
		if (entity != null) {
			try (InputStream instream = entity.getContent()) {
				this.responseBody = getStringFromInputStream(instream);
			}
		} else {
			responseBody = null;
		}
	}
	
	/** Returns response status line. */
	public int getStatusCode() {
		return coreResponse.getStatusLine().getStatusCode();
	}

	/** Returns the response body. */
	public String getResponseBody() {
		return responseBody;
	}

	/** Returns the locale of the response. */
	public Locale getLocale() {
		return coreResponse.getLocale();
	}

	/** Returns the version of the protocol.  */
	public String getProtocolVersion() {
		ProtocolVersion pv = coreResponse.getProtocolVersion();
		return String.format("%s.%s", pv.getMajor(), pv.getMinor());
	}

	/** 
	 * Returns a boolean indicating if response contains given header.
	 * @param name header name
	 * @throws IllegalArgumentException  if given name == null or name.isEmpty()
	 */
	public boolean containsHeader(String name) throws IllegalArgumentException {
		if (name == null || name.isEmpty())
			throw new IllegalArgumentException("Null/Empty name");
		return coreResponse.containsHeader(name);
	}

	/** 
	 * Returns all response headers headers with given name
	 * @param name header name
	 * @throws IllegalArgumentException  if name == null or name.isEmpty()
	 */
	public Set<HttpHeader> getHeaders(String name) throws IllegalArgumentException {
		if (name == null || name.isEmpty())
			throw new IllegalArgumentException("Null/Empty name");
		
		Header[] headers = coreResponse.getHeaders(name);
		Set<HttpHeader> httpHeaders = new LinkedHashSet<>();
		
		for (Header header : headers) {
			httpHeaders.add(new HttpHeader(header.getName(), header.getValue()));
		}
		
		return httpHeaders;
	}

	/**
	 * Returns the first header with given name
	 * @param name header name
	 * @throws IllegalArgumentException  if name == null or name.isEmpty()
	 */
	public HttpHeader getFirstHeader(String name) throws IllegalArgumentException {
		if (name == null || name.isEmpty())
			throw new IllegalArgumentException("Null/Empty name");
		
		Header header = coreResponse.getFirstHeader(name);
		return new HttpHeader(header.getName(), header.getValue());
	}
	
	/** Returns all the headers of this message. */
	public Set<HttpHeader> getAllHeaders() {
		Header[] headers = coreResponse.getAllHeaders();
		Set<HttpHeader> httpHeaders = new LinkedHashSet<>();
		
		for (Header header : headers) {
			httpHeaders.add(new HttpHeader(header.getName(), header.getValue()));
		}
		
		return httpHeaders;
	}
	// =========================================================================
}
