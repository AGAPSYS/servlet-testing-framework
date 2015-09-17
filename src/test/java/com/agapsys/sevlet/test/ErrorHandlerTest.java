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

import com.agapsys.sevlet.test.utils.ErrorPage;
import com.agapsys.sevlet.test.utils.ExceptionServlet;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;
import org.junit.Ignore;

public class ErrorHandlerTest {
	private ServletContainer sc;
	
	@After
	public void after() {
		sc.stopServer();
		sc = null;
	}
	
	@Test
	public void testErrorPage() {
		sc = new ServletContainer();
		
		ApplicationContext context = new ApplicationContext();
		context.registerServlet(ExceptionServlet.class);
		context.registerServlet(ErrorPage.class);
		
		context.registerErrorPage(500, ErrorPage.URL);
		sc.registerContext(context, "/");
		
		sc.startServer();
				
		HttpResponse resp = sc.doGet(ExceptionServlet.URL);
		assertEquals(ErrorPage.RESPONSE_MESSAGE, resp.getResponseBody());
	}
	
	@Test
	public void testErrorHandler() {
		sc = new ServletContainer();
		
		ApplicationContext context = new ApplicationContext();
		context.registerServlet(ExceptionServlet.class);
		
		context.setErrorHandler(new StacktraceErrorPageHandler());
		sc.registerContext(context, "/");
		
		sc.startServer();
				
		HttpResponse resp = sc.doGet(ExceptionServlet.URL);
		String responseBody = resp.getResponseBody();
		assertTrue(responseBody.startsWith("java.lang.RuntimeException"));
	}
}
