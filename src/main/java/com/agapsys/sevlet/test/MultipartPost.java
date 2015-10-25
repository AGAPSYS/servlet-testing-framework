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

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

public class MultipartPost extends HttpEntityRequest {
	private final MultipartEntityBuilder builder = MultipartEntityBuilder.create();

	public MultipartPost(ServletContainer servletContainer, String uri) throws IllegalArgumentException {
		super(servletContainer, uri);
	}

	public void addFile(String name, File file, String mimeType) {
		builder.addBinaryBody(name, file, ContentType.create(mimeType), name);
	}
	
	public void addFile(String name, File file) {
		builder.addBinaryBody(name, file);
	}
	
	public void addFile(File file) {
		builder.addBinaryBody(file.getName(), file);
	}
	
	public void addFile(File file, String contentType) {
		addFile(file.getName(), file, contentType);
	}
	
	public void addString(String name, String value) {
		builder.addTextBody(name, value);
	}
	
	public void addString(String name, String value, String mimeType) {
		builder.addTextBody(name, value, ContentType.create(mimeType));
	}
	
	@Override
	protected final HttpEntity getEntity() {
		return builder.build();
	}
	
	@Override
	protected HttpRequestBase getCoreRequest(String uri) {
		return new org.apache.http.client.methods.HttpPost(uri);
	}
}
