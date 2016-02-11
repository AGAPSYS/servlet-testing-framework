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

import com.agapsys.sevlet.container.ServletContainerBuilder;
import com.agapsys.sevlet.container.ServletContainer;
import com.agapsys.sevlet.container.StacktraceErrorHandler;
import com.agapsys.http.HttpGet;
import com.agapsys.http.HttpResponse.StringResponse;
import com.agapsys.sevlet.test.utils.ErrorPage;
import com.agapsys.sevlet.test.utils.ExceptionServlet;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ErrorHandlerTest {
	private ServletContainer sc;
	
	@After
	public void after() {
		sc.stopServer();
		sc = null;
	}
	
	@Test
	public void testErrorPage() {
		sc = new ServletContainerBuilder()
			.addRootContext()
				.registerServlet(ExceptionServlet.class)
				.registerServlet(ErrorPage.class)
				.registerErrorPage(500, ErrorPage.URL)
			.endContext()
			.build();
		
		sc.startServer();
				
		StringResponse resp = sc.doRequest(new HttpGet(ExceptionServlet.URL));
		assertEquals(ErrorPage.RESPONSE_MESSAGE, resp.getContentString());
	}
	
	@Test
	public void testErrorHandler() {
		sc = new ServletContainerBuilder()
			.addRootContext()
				.registerServlet(ExceptionServlet.class)
				.setErrorHandler(new StacktraceErrorHandler())
			.endContext()
			.build();
		
		sc.startServer();
				
		StringResponse resp = sc.doRequest(new HttpGet(ExceptionServlet.URL));
		String content = resp.getContentString();
		assertTrue(content.startsWith("java.lang.RuntimeException"));
	}
}
