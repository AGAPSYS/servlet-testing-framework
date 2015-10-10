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
import java.util.LinkedList;
import java.util.List;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Represents an HTTP client.
 * A Client can keep connection state managing cookies like a normal web browser does
 */
public class HttpClient {
	private final org.apache.http.client.HttpClient coreClient;
	
	private final List<HttpHeader> defaultHeaders = new LinkedList<>();
	
	public HttpClient() {
		coreClient = new DefaultHttpClient();
	}
	
	/**
	 * Adds given default header to be sent on each request using this client.
	 * @param name header name
	 * @param value header value
	 */
	public void addDefaultHeader(String name, String value) {
		defaultHeaders.add(new HttpHeader(name, value));
	}

	/**
	 * Adds default headers to be sent on each request using this client.
	 * @param headers Headers to be added
	 * @throws IllegalArgumentException if no headers are passed or a null header is given
	 */
	public void addDefaultHeaders(HttpHeader...headers) throws IllegalArgumentException {
		if (headers.length == 0)
			throw new IllegalArgumentException("Empty headers");
		
		int i = 0;
		for (HttpHeader header : headers) {
			if (header == null)
				throw new IllegalArgumentException("Null header on index " + i);
			
			addDefaultHeader(header.getName(), header.getValue());
			i++;
		}
	}
	
	/** Removes all registered default headers. */
	public void clearDefaultHeaders() {
		defaultHeaders.clear();
	}
	
	/** @return default headers associated with this client. */
	public List<HttpHeader> getDefaultHeaders() {
		return defaultHeaders;
	}
	
	org.apache.http.client.HttpClient getCoreClient() {
		return coreClient;
	}
}
