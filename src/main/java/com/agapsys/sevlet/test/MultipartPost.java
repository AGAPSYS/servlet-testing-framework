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
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;

public class MultipartPost extends HttpEntityRequest {
	private final Map<String, ContentBody> contentBodyMap = new LinkedHashMap<>();

	public MultipartPost(ServletContainer servletContainer, String uri) throws IllegalArgumentException {
		super(servletContainer, uri);
	}

	private void addPart(String name, ContentBody body) {
		if (contentBodyMap.put(name, body) != null) throw new RuntimeException("Duplicate name: " + name);
	}
	
	public void addFile(String name, File file, String contentType) {
		FileBody body = new FileBody(file, contentType);
		addPart(name, body);
	}
	
	public void addFile(String name, File file) {
		FileBody body = new FileBody(file);
		addPart(name, body);
	}
	
	public void addFile(File file) {
		addFile(file.getName(), file);
	}
	
	public void addFile(File file, String contentType) {
		addFile(file.getName(), file, contentType);
	}
	
	@Override
	protected HttpEntity getEntity() {
		MultipartEntity entity = new MultipartEntity();
		for (Map.Entry<String, ContentBody> entry : contentBodyMap.entrySet()) {
			String name = entry.getKey();
			ContentBody body = entry.getValue();
			entity.addPart(name, body);
		}
		
		return entity;
	}
	
	@Override
	protected HttpRequestBase getCoreRequest(String uri) {
		return new org.apache.http.client.methods.HttpPost(getUri());
	}
}
