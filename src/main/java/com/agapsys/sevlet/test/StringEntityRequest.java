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

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

public abstract class StringEntityRequest extends HttpEntityRequest {
	private ContentType contentType = ContentType.TEXT_PLAIN;
	private String      contentBody = "";

	public StringEntityRequest(ServletContainer servletContainer, String uri) throws IllegalArgumentException {
		super(servletContainer, uri);
	}
	
	public final ContentType getContentType() {
		return contentType;
	}
	public final void setContentType(ContentType contentType) {
		if (contentType == null)
			throw new IllegalArgumentException("Null content type");
		
		this.contentType = contentType;
	}
	
	public final String getContentBody() {
		return contentBody;
	}
	public final void setContentBody(String contentBody) {
		if (contentType == null )
			contentBody = "";
		
		this.contentBody = contentBody;
	}

	@Override
	protected final HttpEntity getEntity() {
		return new StringEntity(contentBody, contentType);
	}
}
