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

import java.util.EnumSet;
import java.util.EventListener;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.eclipse.jetty.servlet.ErrorPageErrorHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.log.StdErrLog;

/**
 * Represents a servlet container
 */
public class ServletContainer <SC extends ServletContainer> {

    // <editor-fold desc="STATIC SCOPE" defaultstate="collapsed">
    // =========================================================================
    public static final String ROOT_PATH = "/";

    private static class NoLogger implements Logger {
        private static NoLogger singletonInstance = null;

        public static NoLogger getSingletonInstance() {
            if (singletonInstance == null)
                singletonInstance = new NoLogger();

            return singletonInstance;
        }

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

    static {
        Map<String, Logger> loggers = org.eclipse.jetty.util.log.Log.getLoggers();
        for (Map.Entry<String, Logger> entry : loggers.entrySet()) {
            Logger logger = entry.getValue();

            ((StdErrLog)logger).setLevel(StdErrLog.LEVEL_OFF);
        }

        org.eclipse.jetty.util.log.Log.setLog(NoLogger.getSingletonInstance());
    }
    // =========================================================================
    // </editor-fold>

    private final Map<String, Class<? extends Filter>>         filterMap                 = new LinkedHashMap<>();
    private final Map<String, Class<? extends HttpServlet>>    servletMap                = new LinkedHashMap<>();
    private final Set<Class<? extends ServletContextListener>> servletContextListenerSet = new LinkedHashSet<>();

    private ErrorHandler errorHandler = null;
    private Server server;

    // <editor-fold desc="Private scope">
    // -------------------------------------------------------------------------
    private Server __buildServer() {
        Server mServer = new Server();

        // Connectors...
        Connector[] connectors = getConnectors(mServer);
        if (connectors == null || connectors.length == 0)
            throw new RuntimeException("Missing connectors");

        for (Connector connector : connectors) {
            mServer.addConnector(connector);
        }

        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setContextPath("/");
        handler.setResourceBase(System.getProperty("java.io.tmpdir"));

        // Filters...
        for (Map.Entry<String, Class<? extends Filter>> filterEntry : filterMap.entrySet()) {
            handler.addFilter(filterEntry.getValue(), filterEntry.getKey(), EnumSet.of(DispatcherType.REQUEST));
        }

        // Servlets...
        for (Map.Entry<String, Class<? extends HttpServlet>> servletEntry : servletMap.entrySet()) {
            handler.addServlet(servletEntry.getValue(), servletEntry.getKey());
        }

        // Event listeners...
        for (Class<? extends EventListener> eventListenerClass : servletContextListenerSet) {
            try {
                EventListener eventListener = eventListenerClass.newInstance();
                handler.addEventListener(eventListener);
            } catch (IllegalAccessException | InstantiationException ex) {
                throw new RuntimeException(ex);
            }
        }

        // Error handler...
        if (errorHandler != null)
            handler.setErrorHandler(errorHandler);

        mServer.setHandler(handler);
        return mServer;
    }

    private void __throwIfInitialized() throws IllegalStateException {
        if (isInitialized())
            throw new IllegalStateException("Container is already initialized");
    }

    private ErrorPageErrorHandler __getErrorPageErrorHandler() {
        if (errorHandler == null)
            errorHandler = new ErrorPageErrorHandler();

        if (!(errorHandler instanceof ErrorPageErrorHandler))
            throw new IllegalStateException("An custom error handler is already defined");

        return (ErrorPageErrorHandler) errorHandler;
    }
    // -------------------------------------------------------------------------
    // </editor-fold>

    // <editor-fold desc="Protected scope">
    // -------------------------------------------------------------------------
    /**
     * Called on instance initialization.
     *
     * This method will be called only once during instance life-cycle.
     * Default implementation does nothing.
     */
    protected void onInit() {}

    /**
     * Returns the connectors used by this ServletContainer.
     *
     * @param server interval server instance.
     * @return the connectors used by this servletContainer. Default implementaion retuns a default HTTP connector running on port 8080
     */
    protected Connector[] getConnectors(Server server) {
        ServerConnector http = new ServerConnector(server);
        http.setPort(8080);
        return new Connector[] { http };
    }
    // -------------------------------------------------------------------------
    // </editor-fold>

    // <editor-fold desc="Public scope">
    // -------------------------------------------------------------------------

    public ServletContainer(Class<? extends HttpServlet>...servlets) {
        for (Class<? extends HttpServlet> servletClass : servlets) {
            registerServlet(servletClass);
        }
    }

    /**
     * Returns a boolean indicating if this instance was initialized.
     *
     * @return a boolean indicating if this instance was initialized.
     */
    public boolean isInitialized() {
        return server != null;
    }

    /**
     * Returns a boolean indicating if server is running.
     *
     * @return a boolean indicating if server is running.
     */
    public boolean isRunning() {
        return server != null && server.isRunning();
    }

    /**
     * Stops the server.
     *
     * If server is not running, nothing happens.
     */
    public void stop() {
        if (isRunning()) {
            try {
                server.stop();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Starts server.
     *
     * If server is running, it will be restarted.
     */
    public void start() {
        stop(); // <-- If server is not running nothing happens.

        if (!isInitialized()) {
            onInit();
            server = __buildServer();
        }

        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the connectors used by this servlet container.
     *
     * @return the connectors used by this servlet container.
     * @throws IllegalStateException If this instance was not initialized yet.
     */
    public Connector[] getConnectors() throws IllegalStateException {
        if (!isInitialized())
            throw new IllegalStateException("This instance was not initialized yet");

        return server.getConnectors();
    }


    /**
     * Registers an EventListener.
     *
     * @param servletContextListener ServletContextListener subclass to be registered.
     * @return this
     */
    public SC registerServletContextListener(Class<? extends ServletContextListener> servletContextListener) {
        __throwIfInitialized();

        if (servletContextListener == null)
            throw new IllegalArgumentException("Event listener cannot be null");

        servletContextListenerSet.add(servletContextListener);

        return (SC) this;
    }


    /**
     * Registers a filter.
     *
     * @param filterClass class to be registered.
     * @param urlPattern url pattern to be associated with given class.
     * @return this
     */
    public SC registerFilter(Class<? extends Filter> filterClass, String urlPattern) {
        __throwIfInitialized();

        if (filterMap.containsKey(urlPattern) && filterMap.get(urlPattern) != filterClass)
            throw new IllegalStateException(String.format("URL pattern is already associated with another filter class: %s => %s", urlPattern, filterMap.get(urlPattern)));

        filterMap.put(urlPattern, filterClass);

        return (SC) this;
    }

    /**
     * Registers a filter.
     *
     * @param filterClass filter class to be registered. This class must be annotated with {@linkplain WebFilter}.
     * @return this.
     */
    public final SC registerFilter(Class<? extends Filter> filterClass) {
        WebFilter webFilter = filterClass.getAnnotation(WebFilter.class);

        if (webFilter == null)
            throw new IllegalArgumentException(String.format("Missing annotation '%s' for class '%s'", WebFilter.class.getName(), filterClass.getName()));

        String[] urlPatterns = webFilter.value();

        if (urlPatterns.length == 0)
            urlPatterns = webFilter.urlPatterns();

        if (urlPatterns.length == 0)
            throw new IllegalArgumentException(String.format("Missing pattern mapping for '%s'", filterClass.getName()));

        for (String urlPattern : urlPatterns) {
            registerFilter(filterClass, urlPattern);
        }

        return (SC) this;
    }


    /**
     * Registers a servlet.
     *
     * @param servletClass class to be registered.
     * @param urlPattern url pattern to be associated with given class.
     * @return this
     */
    public SC registerServlet(Class<? extends HttpServlet> servletClass, String urlPattern) {
        __throwIfInitialized();

        if (servletMap.containsKey(urlPattern) && servletMap.get(urlPattern) != servletClass)
            throw new IllegalArgumentException(String.format("URL pattern is already associated with another servlet class: %s => %s", urlPattern, servletMap.get(urlPattern)));

        servletMap.put(urlPattern, servletClass);
        return (SC) this;
    }

    /**
     * Registers a servlet.
     *
     * @param servletClass servlet class to be registered. This class must be annotated with {@linkplain WebServlet}.
     * @return this.
     */
    public final SC registerServlet(Class<? extends HttpServlet> servletClass) {
        WebServlet webServlet = servletClass.getAnnotation(WebServlet.class);

        if (webServlet == null)
            throw new IllegalArgumentException(String.format("Missing annotation '%s' for class '%s'", WebFilter.class.getName(), servletClass.getName()));

        String[] urlPatterns = webServlet.value();

        if (urlPatterns.length == 0)
            urlPatterns = webServlet.urlPatterns();

        if (urlPatterns.length == 0)
            throw new IllegalArgumentException(String.format("Missing pattern mapping for '%s'", servletClass.getName()));

        for (String urlPattern : urlPatterns) {
            registerServlet(servletClass, urlPattern);
        }

        return (SC) this;
    }


    public SC registerErrorPage(int fromCode, int toCode, String uri) {
        __throwIfInitialized();

        if (uri == null || uri.isEmpty())
            throw new IllegalArgumentException("Null/Empty URI");

        __getErrorPageErrorHandler().addErrorPage(fromCode, toCode, uri);

        return (SC) this;
    }

    public SC registerErrorPage(int code, String uri) {
        __throwIfInitialized();

        if (uri == null || uri.isEmpty())
            throw new IllegalArgumentException("Null/Empty URI");

        __getErrorPageErrorHandler().addErrorPage(code, uri);

        return (SC) this;
    }

    public SC registerErrorPage(Class<? extends Throwable> throwableClass, String uri) {
        __throwIfInitialized();

         if (uri == null || uri.isEmpty())
            throw new IllegalArgumentException("Null/Empty URI");

        __getErrorPageErrorHandler().addErrorPage(throwableClass, uri);

        return (SC) this;

    }


    public SC setErrorHandler(ErrorHandler errorHandler) {
        __throwIfInitialized();

        if (errorHandler == null)
            throw new IllegalArgumentException("Error handler cannot be null");

        if (this.errorHandler != null)
            throw new IllegalStateException("Error handler is already defined");

        this.errorHandler = errorHandler;

        return (SC) this;
    }
    // -------------------------------------------------------------------------
    // </editor-fold>



}
