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

import org.junit.Test;

public class HttpRequestTest {
	private final ServletContainer sc = new ServletContainer();
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidUriParsingDueToProtocol() {
		HttpGet get = new HttpGet(sc, "http://localhost/test");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidUriParsingDueToPort() {
		HttpGet get = new HttpGet(sc, "localhost:8080/test");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidUriParsingDueToSpaces() {
		HttpGet get = new HttpGet(sc, "localhost:8080/tes t");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidUriParsingDueToMissingStartingSlash() {
		HttpGet get = new HttpGet(sc, "context/path");
	}
	
	@Test
	public void testValidGetUri() {
		new HttpGet(sc, "/context?var=1");
	}
	
	@Test
	public void testValidPostUri() {
		new HttpPost(sc, "/context");
	}
}
