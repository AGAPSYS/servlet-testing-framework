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

import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.http.HttpServlet;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.log.StdErrLog;

/**
 *
 * @author leandro-agapsys
 */
public class ServletContainerBuilder {

	// CLASS SCOPE =============================================================
	private static final String ROOT_PATH = "/";
	
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
		ServletContextHandlerBuilder contextBuilder = new ServletContainerBuilder().addRootContext();
		for (Class<? extends HttpServlet> servletClass : servletClasses) {
			contextBuilder.registerServlet(servletClass);
		}
		return contextBuilder.endContext().build();
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
	final Map<String, ServletContextHandlerBuilder> contextBuilders = new LinkedHashMap<>();

	public ServletContextHandlerBuilder addRootContext() {
		return addContext(ROOT_PATH);
	}

	public ServletContextHandlerBuilder addContext(String contextPath) {
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

	public ServletContainer build() {

		Server server = new Server(0);
		
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
