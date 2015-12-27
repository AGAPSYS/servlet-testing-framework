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

import java.lang.reflect.InvocationTargetException;
import java.util.EventListener;
import javax.servlet.Filter;
import javax.servlet.http.HttpServlet;
import org.eclipse.jetty.server.handler.ErrorHandler;

/**
 *
 * @author leandro-agapsys
 */
public class ServletContainerBuilder {
	// CLASS SCOPE =============================================================
	private static <T extends EventListener> T getEventListenerInstance(Class<T> eventListenerClass) {
		try {
			return eventListenerClass.getConstructor().newInstance();
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			throw new RuntimeException(ex);
		}
	}
	// =========================================================================

	// INSTANCE SCOPE ==========================================================
	private final ApplicationContext ctx = new ApplicationContext();
	
	private ErrorHandler errorHandler = null;


	public ServletContainerBuilder registerEventListener(Class<? extends EventListener>...eventListenerClasses) {
		for (Class<? extends EventListener> eventListenerClass : eventListenerClasses) {
			ctx.registerEventListener(getEventListenerInstance(eventListenerClass));
		}
		
		return this;
	}
	
	public ServletContainerBuilder registerEventListener(EventListener listener) {
		ctx.registerEventListener(listener);
		return this;
	}

	
	public ServletContainerBuilder registerFilter(Class<? extends Filter> filterClass, String urlPattern) {
		ctx.registerFilter(filterClass, urlPattern);
		ctx.registerEventListener(null);
		return this;
	}
	
	public ServletContainerBuilder registerFilters(Class<? extends Filter>...filterClasses) {
		for (Class<? extends Filter> filterClass : filterClasses) {
			ctx.registerFilter(filterClass);
		}
		
		return this;
	}
	
	
	public ServletContainerBuilder registerServlet(Class<? extends HttpServlet>...servlets) {
		for (Class<? extends HttpServlet> servlet : servlets) {
			ctx.registerServlet(servlet);
		}

		return this;
	}
	
	public ServletContainerBuilder registerServlet(Class<? extends HttpServlet> servlet, String urlPattern) {
		ctx.registerServlet(servlet, urlPattern);
		return this;
	}

	
	public ServletContainerBuilder setErrorHandler(ErrorHandler errorHandler) {
		if (errorHandler == null)
			throw new IllegalArgumentException("Null error handler");
		
		if (this.errorHandler != null)
			throw new IllegalStateException("Error handler is already set");
		
		this.errorHandler = errorHandler;
		
		return this;
	}
	
	public ServletContainer build() {
		ServletContainer sc = new ServletContainer();
		
		if (errorHandler != null)
			ctx.setErrorHandler(errorHandler);
		
		sc.registerContext(ctx);
		return sc;
	}
	// =========================================================================
}
