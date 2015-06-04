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

package com.agapsys.hsmock.test;

import com.agapsys.hsmock.test.utils.MyServlet;
import com.agapsys.sevlet.test.AppContext;
import com.agapsys.sevlet.test.HttpGet;
import com.agapsys.sevlet.test.HttpResponse;
import com.agapsys.sevlet.test.ServletContainter;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MyServletTest {
	private static final String CONTEXT = "/context";
	
	private ServletContainter sc;
	
	@Before
	public void setUp() {
		sc = new ServletContainter();
		
		AppContext context = new AppContext();
		context.registerServlet(MyServlet.class);
		
		sc.registerContext(context, CONTEXT);
		sc.startServer();
	}
	
	@After
	public void tearDown() {
		sc.stopServer();
	}

	@Test
	public void testServletResponse() throws IOException {
		String testUrl = CONTEXT + MyServlet.URL1;
		
		HttpResponse response = sc.doGet(new HttpGet(sc, testUrl));
		assertEquals(response.getStatusLine().getStatusCode(), 200);

		String responseStr = response.getResponseBody();

		assertEquals(responseStr, MyServlet.URL1);
		
	}
}
