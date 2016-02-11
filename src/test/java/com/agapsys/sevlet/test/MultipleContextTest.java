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
import com.agapsys.http.HttpGet;
import com.agapsys.http.HttpResponse;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MultipleContextTest {
	// CLASS SCOPE =============================================================
	@WebServlet("/*")
	public static class Test1Servlet extends HttpServlet {

		@Override
		protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			resp.getWriter().print("test");
		}
	}
	// =========================================================================

	// INSTANCE SCOPE ==========================================================
	private ServletContainer sc;
	
	@Before
	public void before() {
		sc = new ServletContainerBuilder()
			.addContext("/context1")
				.registerServlet(Test1Servlet.class)
			.endContext()
			.addContext("/context2")
				.registerServlet(Test1Servlet.class)
			.endContext()
			.build();
				
		sc.startServer();
	}
	
	@After
	public void after() {
		sc.stopServer();
	}
	
	@Test
	public void test() {
		HttpResponse.StringResponse resp;
		
		resp = sc.doRequest(new HttpGet("/context1/test"));
		Assert.assertEquals("test", resp.getContentString());
		
		resp = sc.doRequest(new HttpGet("/context2/test"));
		Assert.assertEquals("test", resp.getContentString());
	}
	// =========================================================================
}
