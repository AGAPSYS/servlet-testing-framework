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

public class ServletContainerBuilder<T extends ServletContainerBuilder> {

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
    final Map<String, ServletContextHandlerBuilder> _contextBuilders = new LinkedHashMap<>();

    private Integer localPort = null;

    public ServletContainerBuilder() {
        contextHandlerBuilder = addRootContext();
    }

    private ServletContextHandlerBuilder addRootContext() {
        return addContext(ROOT_PATH);
    }

    private ServletContextHandlerBuilder addContext(String contextPath) {
        if (contextPath == null)
            throw new IllegalArgumentException("Null context path");

        contextPath = contextPath.trim();

        if (!contextPath.startsWith("/")) {
            contextPath = "/" + contextPath;
        }

        if (_contextBuilders.containsKey(contextPath)) {
            throw new IllegalStateException("Context already defined: " + contextPath);
        }

        _contextBuilders.put(contextPath, null);
        return new ServletContextHandlerBuilder(this, contextPath);
    }

    /** Add a root context using {@linkplain ServletContainerBuilder#addRootContext()} and then call {@linkplain ServletContextHandlerBuilder#registerEventListener(java.lang.Class, boolean)}. */
    @Deprecated
    public T registerEventListener(Class<? extends EventListener> eventListener, boolean append) {
        contextHandlerBuilder.registerEventListener(eventListener, append);
        return (T) this;
    }

    /** Add a root context using {@linkplain ServletContainerBuilder#addRootContext()} and then call {@linkplain ServletContextHandlerBuilder#registerEventListener(java.lang.Class)}. */
    @Deprecated
    public final T registerEventListener(Class<? extends EventListener> eventListener) {
        return registerEventListener(eventListener, true);
    }

    /** Add a root context using {@linkplain ServletContainerBuilder#addRootContext()} and then call {@linkplain ServletContextHandlerBuilder#registerFilter(java.lang.Class, java.lang.String, boolean)}. */
    @Deprecated
    public T registerFilter(Class<? extends Filter> filterClass, String urlPattern, boolean append) {
        contextHandlerBuilder.registerFilter(filterClass, urlPattern, append);
        return (T) this;
    }

    /** Add a root context using {@linkplain ServletContainerBuilder#addRootContext()} and then call {@linkplain ServletContextHandlerBuilder#registerFilter(java.lang.Class, java.lang.String)}. */
    @Deprecated
    public final T registerFilter(Class<? extends Filter> filterClass, String urlPattern) {
        return registerFilter(filterClass, urlPattern, true);
    }

    /** Add a root context using {@linkplain ServletContainerBuilder#addRootContext()} and then call {@linkplain ServletContextHandlerBuilder#registerFilter(java.lang.Class)}. */
    @Deprecated
    public final T registerFilter(Class<?extends Filter> filterClass) {
        contextHandlerBuilder.registerFilter(filterClass);
        return (T) this;
    }


    /** Add a root context using {@linkplain ServletContainerBuilder#addRootContext()} and then call {@linkplain ServletContextHandlerBuilder#registerServlet(java.lang.Class, java.lang.String)}. */
    @Deprecated
    public T registerServlet(Class<? extends HttpServlet> servletClass, String urlPattern) {
        contextHandlerBuilder.registerServlet(servletClass, urlPattern);
        return (T) this;
    }

    /** Add a root context using {@linkplain ServletContainerBuilder#addRootContext()} and then call {@linkplain ServletContextHandlerBuilder#registerServlet(java.lang.Class)}. */
    @Deprecated
    public final T registerServlet(Class<? extends HttpServlet> servletClass) {
        contextHandlerBuilder.registerServlet(servletClass);
        return (T) this;
    }

    /** Add a root context using {@linkplain ServletContainerBuilder#addRootContext()} and then call {@linkplain ServletContextHandlerBuilder#registerErrorPage(int, java.lang.String)}. */
    @Deprecated
    public T registerErrorPage(int code, String url) {
        contextHandlerBuilder.registerErrorPage(code, url);
        return (T) this;
    }

    /** Add a root context using {@linkplain ServletContainerBuilder#addRootContext()} and then call {@linkplain ServletContextHandlerBuilder#setErrorHandler(org.eclipse.jetty.server.handler.ErrorHandler)}.*/
    @Deprecated
    public T setErrorHandler(ErrorHandler errorHandler) {
        contextHandlerBuilder.setErrorHandler(errorHandler);
        return (T) this;
    }

    public T setLocalPort(int localPort) {
        if (this.localPort != null)
            throw new IllegalStateException("Local port is already set");

        if (localPort < 1 || localPort > 65535)
            throw new IllegalArgumentException("Invalid port: " + localPort);

        this.localPort = localPort;
        return (T) this;
    }

    public ServletContainer build() {
        if (!contextHandlerBuilder._isDone())
            contextHandlerBuilder.done();

        if (localPort == null)
            localPort = 0;

        Server server = new Server(localPort);

        Handler[] handlers = new Handler[_contextBuilders.size()];

        int i = 0;
        for (Map.Entry<String, ServletContextHandlerBuilder> entry : _contextBuilders.entrySet()) {
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
