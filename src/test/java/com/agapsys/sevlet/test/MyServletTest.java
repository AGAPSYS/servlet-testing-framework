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
import com.agapsys.http.HttpResponse.StringResponse;
import com.agapsys.sevlet.test.utils.MyServlet;
import java.io.IOException;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class MyServletTest {
	private static final String CONTEXT = "/context";
	
	private ServletContainer sc;
	
	@Before
	public void setUp() {
		sc = new ServletContainerBuilder()
			.addContext(CONTEXT)
				.registerServlet(MyServlet.class)
			.endContext()
			.build();
		
		sc.startServer();
	}
	
	@After
	public void tearDown() {
		sc.stopServer();
	}

	@Test
	public void testServletResponse() throws IOException {
		String testUrl = CONTEXT + MyServlet.URL1;
		
		StringResponse response = sc.doRequest(new HttpGet(testUrl));
		assertEquals(response.getStatusCode(), 200);

		String responseStr = response.getContentString();

		assertEquals(responseStr, MyServlet.URL1);
		
	}
}
