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
package com.agapsys.sevlet.container;

import java.util.EventListener;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.http.HttpServlet;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.log.StdErrLog;

/**
 *
 * @author leandro-agapsys
 */
public class ServletContainerBuilder {

	// CLASS SCOPE =============================================================
	public static final String ROOT_PATH = "/";
	
	private static class NoLogger implements Logger {
		// CLASS SCOPE =========================================================
		private static NoLogger singletonInstance = null;
		
		public static NoLogger getSingletonInstance() {
			if (singletonInstance == null)
				singletonInstance = new NoLogger();
			
			return singletonInstance;
		}
		// =====================================================================
		
		// INSTANCE SCOPE ======================================================
		private NoLogger() {}
		
		@Override public String getName() { return ""; }
		@Override public void warn(String msg, Object... args) { }
		@Override public void warn(Throwable thrown) { }
		@Override public void warn(String msg, Throwable thrown) { }
		@Override public void info(String msg, Object... args) { }
		@Override public void info(Throwable thrown) { }
		@Override public void info(String msg, Throwable thrown) { }
		@Override public boolean isDebugEnabled() { return false; }
		@Override public void setDebugEnabled(boolean enabled) { }
		@Override public void debug(String msg, Object... args) { }
		@Override public void debug(Throwable thrown) { }
		@Override public void debug(String msg, Throwable thrown) { }
		@Override public Logger getLogger(String name) { return this; }
		@Override public void ignore(Throwable ignored) { }
		@Override public void debug(String arg0, long arg1) { }
		// =====================================================================
	}
	
	public static ServletContainer getServletContainer(Class<? extends HttpServlet>...servletClasses) {
		ServletContainerBuilder containerBuilder = new ServletContainerBuilder();
		for (Class<? extends HttpServlet> servletClass : servletClasses) {
			containerBuilder.registerServlet(servletClass);
		}
		return containerBuilder.build();
	}
	
	static {
		Map<String, Logger> loggers = org.eclipse.jetty.util.log.Log.getLoggers();
		for (Map.Entry<String, Logger> entry : loggers.entrySet()) {
			Logger logger = entry.getValue();
			
			((StdErrLog)logger).setLevel(StdErrLog.LEVEL_OFF);
		}
		
		org.eclipse.jetty.util.log.Log.setLog(NoLogger.getSingletonInstance());
	}
	// =========================================================================

	// INSTANCE SCOPE ==========================================================
	private final ServletContextHandlerBuilder contextHandlerBuilder;
	final Map<String, ServletContextHandlerBuilder> contextBuilders = new LinkedHashMap<>();

	private Integer localPort = null;
	
	public ServletContainerBuilder() {
		contextHandlerBuilder = addRootContext();
	}
	
	private ServletContextHandlerBuilder addRootContext() {
		return addContext(ROOT_PATH);
	}

	protected ServletContextHandlerBuilder addContext(String contextPath) {
		if (contextPath == null)
			throw new IllegalArgumentException("Null context path");

		contextPath = contextPath.trim();

		if (!contextPath.startsWith("/")) {
			contextPath = "/" + contextPath;
		}

		if (contextBuilders.containsKey(contextPath)) {
			throw new IllegalStateException("Context already defined: " + contextPath);
		}

		contextBuilders.put(contextPath, null);
		return new ServletContextHandlerBuilder(this, contextPath);
	}

	/**
	 * Registers an event listener with this context handler builder
	 * @param eventListener event listener to be registered
	 * @param append boolean indicating if given listener shall be appended. If false, given listener will be prepended.
	 * @return this
	 */
	public ServletContainerBuilder registerEventListener(Class<? extends EventListener> eventListener, boolean append) {
		contextHandlerBuilder.registerEventListener(eventListener, append);
		return this;
	}
	
	/**
	 * Convenience method for registerEventListener(eventListener, true).
	 * @param eventListener event listener to be registered
	 * @return this
	 */
	public final ServletContainerBuilder registerEventListener(Class<? extends EventListener> eventListener) {
		return registerEventListener(eventListener, true);
	}
	
	
	/**
	 * Registers a filter with this context handler builder
	 * @param filterClass filter class to be registered.
	 * @param urlPattern URL pattern associated with given filter
	 * @param append boolean indicating if given filter shall be appended. If false, given filter will be prepended.
	 * @return this
	 */
	public ServletContainerBuilder registerFilter(Class<? extends Filter> filterClass, String urlPattern, boolean append) {
		contextHandlerBuilder.registerFilter(filterClass, urlPattern, append);
		return this;
	}
	
	/**
	 * Convenience method for registerFilter(filterClass, urlPattern, true)
	 * @param filterClass filter class to be registered
	 * @param urlPattern URL pattern associated with given filter
	 * @return this
	 */
	public final ServletContainerBuilder registerFilter(Class<? extends Filter> filterClass, String urlPattern) {
		return registerFilter(filterClass, urlPattern, true);
	}
	
	/**
	 * Convenience method for registerFilter(filterClass).
	 * @param filterClass filter class to be registered. Informed class must be annotated with {@linkplain WebFilter}.
	 * @return this
	 */
	public final ServletContainerBuilder registerFilter(Class<?extends Filter> filterClass) {
		contextHandlerBuilder.registerFilter(filterClass);
		return this;
	}
	
	
	/**
	 * Registers a servlet with this context handler builder.
	 * @param servletClass servlet class to be registered.
	 * @param urlPattern URL pattern associated with given servlet
	 * @return this
	 */
	public ServletContainerBuilder registerServlet(Class<? extends HttpServlet> servletClass, String urlPattern) {
		contextHandlerBuilder.registerServlet(servletClass, urlPattern);
		return this;
	}
	
	/**
	 * Convenience method for registerServlet(servletClass).
	 * @param servletClass servlet class to be registered. Informed class must be annotated with {@linkplain WebServlet}.
	 * @return this
	 */
	public final ServletContainerBuilder registerServlet(Class<? extends HttpServlet> servletClass) {
		contextHandlerBuilder.registerServlet(servletClass);
		return this;
	}
	
	public ServletContainerBuilder registerErrorPage(int code, String url) {
		contextHandlerBuilder.registerErrorPage(code, url);
		return this;
	}
	
	public ServletContainerBuilder setErrorHandler(ErrorHandler errorHandler) {
		contextHandlerBuilder.setErrorHandler(errorHandler);
		return this;
	}
	
	public ServletContainerBuilder setLocalPort(int localPort) {
		if (this.localPort != null)
			throw new IllegalStateException("Local port is already set");
		
		if (localPort < 1 || localPort > 65535)
			throw new IllegalArgumentException("Invalid port: " + localPort);
		
		this.localPort = localPort;
		return this;
	}
	
	public ServletContainer build() {
		
		contextHandlerBuilder.endContext();

		if (localPort == null)
			localPort = 0;
		
		Server server = new Server(localPort);
		
		Handler[] handlers = new Handler[contextBuilders.size()];
		
		int i = 0;
		for (Map.Entry<String, ServletContextHandlerBuilder> entry : contextBuilders.entrySet()) {
			ServletContextHandler servletContextHandler = entry.getValue().build();
			servletContextHandler.setContextPath(entry.getKey());
			handlers[i] = servletContextHandler;
			i++;
		}
		
		ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();
		contextHandlerCollection.setHandlers(handlers);
		
		server.setHandler(contextHandlerCollection);

		return new ServletContainer(server);
	}
	// =========================================================================
}
