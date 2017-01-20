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

package com.agapsys.jee;

import com.agapsys.http.HttpGet;
import com.agapsys.http.HttpResponse.StringResponse;
import com.agapsys.jee.app.MyFilter;
import com.agapsys.jee.app.MyServlet;
import java.io.IOException;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class MyFilterTest {
    private static final String CONTEXT = "/";

    private final TestingContainer tc;

    public MyFilterTest() {
        tc = new TestingContainer()
            .registerServlet(MyServlet.class)
            .registerFilter(MyFilter.class);
    }

    @Before
    public void before() {
        tc.start();
    }

    @After
    public void after() {
        tc.stop();
    }

    @Test
    public void testFilterIntercept() throws IOException {
        String testUrl = CONTEXT + MyServlet.URL1;

        StringResponse response = tc.doRequest(new HttpGet(testUrl));
        assertEquals(response.getStatusCode(), 200);

        String responseStr = response.getContentString();

        assertEquals(responseStr, "Filtered" + MyServlet.URL1);
    }
}
