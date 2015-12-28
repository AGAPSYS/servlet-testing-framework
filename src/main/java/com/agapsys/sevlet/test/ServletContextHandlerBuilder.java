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

import java.util.EnumSet;
import java.util.EventListener;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.eclipse.jetty.servlet.ErrorPageErrorHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 *
 * @author leandro-agapsys
 */
public class ServletContextHandlerBuilder {
	// CLASS SCOPE =============================================================
	private static class FilterMapping {
		public final Class<? extends Filter> filterClass;
		public final String urlPattern;
		
		public FilterMapping(Class<? extends Filter> filterClass, String urlPattern) {
			if (filterClass == null)
				throw new IllegalArgumentException("Filter class cannot be null");
			
			if (urlPattern == null || urlPattern.trim().isEmpty())
				throw new IllegalArgumentException("Null/Empty URL pattern");
			
			this.filterClass = filterClass;
			this.urlPattern = urlPattern;
		}
	}
	// =========================================================================
	
	// INSTANCE SCOPE ==========================================================
	private final List<EventListener> eventListeners = new LinkedList<>();
	private final List<FilterMapping> filterMappingList = new LinkedList<>();
	private final Map<String, Class<? extends HttpServlet>> servletMap = new LinkedHashMap<>();
	private final ServletContainerBuilder servletContainerBuilder;
	private final String contextPath;
	
	private ErrorHandler errorHandler = null;

	
	ServletContextHandlerBuilder(ServletContainerBuilder servletContainerBuilder, String contextPath) {
		this.servletContainerBuilder = servletContainerBuilder;
		this.contextPath = contextPath;
	}
	
	
	public ServletContextHandlerBuilder registerEventListener(EventListener eventListener) {
		if (eventListener == null)
			throw new IllegalArgumentException("Event listener cannot be null");
		
		if (eventListeners.contains(eventListener))
			throw new IllegalArgumentException("Event listener is already registered");
		
		eventListeners.add(eventListener);
		
		return this;
	}
	
	
	public ServletContextHandlerBuilder registerFilter(Class<? extends Filter> filterClass, String urlPattern) {
		filterMappingList.add(new FilterMapping(filterClass, urlPattern));
		return this;
	}
	
	public ServletContextHandlerBuilder registerFilter(Class<?extends Filter> filterClass) {
		WebFilter[] annotations = filterClass.getAnnotationsByType(WebFilter.class);
		
		if (annotations.length == 0)
			throw new IllegalArgumentException("Filter class does not have a WebFilter annotation");
		
		for (WebFilter annotation : annotations) {
			String[] urlPatterns = annotation.value();
			if (urlPatterns.length == 0)
				urlPatterns = annotation.urlPatterns();
			
			if (urlPatterns.length == 0)
				throw new IllegalArgumentException("Missing urlPatterns");
			
			for (String urlPattern : urlPatterns) {
				registerFilter(filterClass, urlPattern);
			}
		}
		
		return this;
	}
	
	
	public ServletContextHandlerBuilder registerServlet(Class<? extends HttpServlet> servletClass, String urlPattern) {
		if (servletClass == null)
			throw new IllegalArgumentException("Servlet class cannot be null");
		
		if (urlPattern == null || urlPattern.trim().isEmpty())
			throw new IllegalArgumentException("Null/Empty URL pattern");
		
		if (servletMap.containsKey(urlPattern))
			throw new IllegalArgumentException(String.format("URL pattern is already associated with a servlet: %s", urlPattern));
		
		servletMap.put(urlPattern, servletClass);
		
		return this;
	}
	
	public ServletContextHandlerBuilder registerServlet(Class<? extends HttpServlet> servletClass) {
		WebServlet[] annotations = servletClass.getAnnotationsByType(WebServlet.class);
		if (annotations.length == 0)
			throw new IllegalArgumentException("Servlet class does not have a WebServlet annotation");
		
		for (WebServlet annotation : annotations) {
			String[] urlPatterns = annotation.value();
			if (urlPatterns.length == 0)
				urlPatterns = annotation.urlPatterns();
			
			if (urlPatterns.length == 0)
				throw new IllegalArgumentException("Missing urlPatterns");
			
			for (String urlPattern : urlPatterns) {
				registerServlet(servletClass, urlPattern);
			}
		}
		
		return this;
	}
	
	
	public ServletContextHandlerBuilder registerErrorPage(int code, String url) {
		if (url == null || url.trim().isEmpty())
			throw new IllegalArgumentException("Null/Empty URL");
		
		if (errorHandler == null)
			errorHandler = new ErrorPageErrorHandler();
		
		if (!(errorHandler instanceof ErrorPageErrorHandler))
			throw new IllegalStateException("An custom error handler is already defined");
		
		((ErrorPageErrorHandler) errorHandler).addErrorPage(code, url);
		
		return this;
	}
	
	
	public ServletContextHandlerBuilder setErrorHandler(ErrorHandler errorHandler) {
		if (errorHandler == null)
			throw new IllegalArgumentException("Error handler cannot be null");
		
		if (this.errorHandler != null)
			throw new IllegalArgumentException("Error handler is already defined");
		
		this.errorHandler = errorHandler;
		return this;
	}
	
	
	public ServletContainerBuilder endContext() {
		servletContainerBuilder.contextBuilders.put(contextPath, this);
		return servletContainerBuilder;
	}
	
	
	ServletContextHandler build() {
		ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		
		for (FilterMapping filterMapping : filterMappingList) {
			Class<? extends Filter> filterClass = filterMapping.filterClass;
			String urlPattern = filterMapping.urlPattern;
			
			servletContextHandler.addFilter(filterClass, urlPattern, EnumSet.of(DispatcherType.REQUEST));
		}
		
		for (Map.Entry<String, Class<? extends HttpServlet>> entry : servletMap.entrySet()) {
			Class<? extends HttpServlet> servletClass = entry.getValue();
			String urlPattern = entry.getKey();
			
			servletContextHandler.addServlet(servletClass, urlPattern);
		}
		
		for (EventListener eventListener : eventListeners) {
			servletContextHandler.addEventListener(eventListener);
		}
		
		if (errorHandler != null)
			servletContextHandler.setErrorHandler(errorHandler);
		
		return servletContextHandler;
	}
}
