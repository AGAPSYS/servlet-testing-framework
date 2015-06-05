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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;

public abstract class HttpEntityRequest extends HttpRequest{

	public HttpEntityRequest(ServletContainter servletContainer, String uri) throws IllegalArgumentException {
		super(servletContainer, uri);
		
		if (uri.contains("?"))
			throw new IllegalArgumentException("Invalid uri: " + uri);
	}

	@Override
	void setCoreParameters(HttpRequestBase coreRequest, Map<String, String> parameters) {
		try {
			List<NameValuePair> urlParameters = new ArrayList<>();
			for (Map.Entry<String, String> parameter : parameters.entrySet()) {
				urlParameters.add(new BasicNameValuePair(parameter.getKey(), parameter.getValue()));
			}

			((HttpEntityEnclosingRequestBase)getCoreRequest()).setEntity(new UrlEncodedFormEntity(urlParameters));
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public String getUri() {
		return super.getUriBase();
	}
}
