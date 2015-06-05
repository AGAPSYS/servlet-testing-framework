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

import org.apache.http.client.methods.HttpRequestBase;

/** Represents a HTTP 'PUT' request. */
public class HttpPut extends HttpEntityRequest {
	private HttpRequestBase coreRequest = null;

	/** @see HttpRequest#HttpRequest(ServletContainter, String). */
	public HttpPut(ServletContainter servletContainer, String uri) throws IllegalArgumentException {
		super(servletContainer, uri);
	}

	@Override
	HttpRequestBase getCoreRequest() {
		if (coreRequest == null) {
			coreRequest = new org.apache.http.client.methods.HttpPut(getUri());
		}
		
		return coreRequest;
	}
}
