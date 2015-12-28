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

import com.agapsys.http.HttpClient;
import com.agapsys.http.HttpRequest;
import com.agapsys.http.HttpResponse;
import com.agapsys.http.HttpResponse.StringResponse;
import java.io.IOException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

/**
 * Represents a servlet container
 */
public class ServletContainer {

	private final Server server;

	ServletContainer(Server server) {
		this.server = server;
	}
	
	/**
	 * Returns a boolean indicating if server is running.
	 *
	 * @return a boolean indicating if server is running.
	 */
	public boolean isRunning() {
		return server.isRunning();
	}

	/**
	 * Returns the local TCP port used by the server.
	 *
	 * @return the local TCP port used by the server.
	 */
	public int getLocalPort() {
		if (!isRunning())
			throw new IllegalStateException("Server is not running");

		return ((ServerConnector) server.getConnectors()[0]).getLocalPort();
	}

	/**
	 * Stops the server.
	 */
	public void stopServer() {
		if (!isRunning())
			throw new IllegalStateException("Server is not running");

		try {
			server.stop();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Starts server.
	 */
	public void startServer() {
		if (isRunning())
			throw new IllegalStateException("Server is already running");

		try {
			server.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Perform a request against this servlet container.
	 *
	 * @param client {@linkplain HttpClient} instance
	 * @param request {@linkplain HttpRequest} instance
	 * @return response
	 */
	public StringResponse doRequest(HttpClient client, HttpRequest request) {
		if (!isRunning()) {
			throw new IllegalStateException("Server is not running");
		}

		// Change URI to use servlet container
		String oldUri = request.getUri();

		if (oldUri == null || oldUri.isEmpty()) {
			throw new IllegalArgumentException("Null/Empty uri");
		}

		if (oldUri.contains(":") || oldUri.contains(" ") || !oldUri.startsWith("/")) {
			throw new IllegalArgumentException("Invalid uri: " + oldUri);
		}

		request.setUri(String.format("http://localhost:%d%s", getLocalPort(), oldUri));

		HttpResponse.StringResponse resp;
		try {
			resp = HttpResponse.getStringResponse(client, request, "utf-8", -1);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		// Restore servlet URI
		request.setUri(oldUri);

		return resp;
	}

	/**
	 * Perform a request against this servlet container.
	 *
	 * @param request {@linkplain HttpRequest} instance
	 * @return response
	 */
	public StringResponse doRequest(HttpRequest request) {
		try {
			HttpClient client = new HttpClient();
			HttpResponse.StringResponse response = doRequest(client, request);
			client.close();
			return response;
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	// =========================================================================
}
