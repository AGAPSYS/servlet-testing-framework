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

import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Represents an HTTP client.
 * A Client can keep connection state managing cookies like a normal web browser does
 */
public class HttpClient {
	private final org.apache.http.client.HttpClient coreClient;
	
	public HttpClient() {
		coreClient = new DefaultHttpClient();
	}
	
	org.apache.http.client.HttpClient getCoreClient() {
		return coreClient;
	}
}
