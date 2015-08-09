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
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import org.eclipse.jetty.servlet.ErrorPageErrorHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

/** Represents an application context. */
public class ApplicationContext  {
	// INSTANCE SCOPE ==========================================================
	private final ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
	private final ErrorPageErrorHandler errorHandler = new ErrorPageErrorHandler();

	public ApplicationContext() {
		contextHandler.setErrorHandler(errorHandler);
	}
	
	/**
	 * Register an event listener
	 * @param listener event listener
	 */
	public void registerEventListener(EventListener listener) {
		contextHandler.addEventListener(listener);
	}
	
	/**
	 * Registers a filter in this application context
	 * @param filterClass filter class to be registered
	 * @param urlPattern URL pattern according to servlet specification. Any 
	 * {@linkplain WebFilter} annotation will be ignored.
	 */
	public void registerFilter(Class<? extends Filter> filterClass, String urlPattern) {
		contextHandler.addFilter(filterClass, urlPattern, EnumSet.of(DispatcherType.REQUEST));
	}
	
	/**
	 * Registers a filter in this application context
	 * @param filterClass filter class to be registered. Given class must be annotated with {@linkplain WebFilter} annotation.
	 * @throws IllegalArgumentException if given class isn't annotated with a {@linkplain WebFilter} annotation and
	 * does not have URL mapping
	 */
	public void registerFilter(Class<? extends Filter> filterClass) throws IllegalArgumentException {
		WebFilter[] annotations = filterClass.getAnnotationsByType(WebFilter.class);
		
		if (annotations.length == 0)
			throw new IllegalArgumentException("Servlet class does not have a WebFilter annotation");
		
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
		
	}
	
	/**
	 * Registers a servlet in this application context
	 * @param servletClass servlet class to be registered
	 * @param urlPattern URL pattern according to servlet specification. Any 
	 * {@linkplain WebServlet} annotation will be ignored.	 */
	public void registerServlet(Class<? extends Servlet> servletClass, String urlPattern) {
		contextHandler.addServlet(servletClass, urlPattern);
	}
	
	/**
	 * Registers a servlet in this application context
	 * @param servletClass servlet class to be registered. Given class must be annotated with {@linkplain WebServlet} annotation.
	 * @throws IllegalArgumentException if given class isn't annotated with a {@linkplain WebServlet} annotation and
	 * does not have URL mapping
	 */
	public void registerServlet(Class<? extends Servlet> servletClass) throws IllegalArgumentException {
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
	}
	
	/**
	 * Registers a servlet with given error code
	 * @param code error code
	 * @param url url to be associated with error code
	 * @throws IllegalArgumentException if url == null || url.isEmtpy()
	 */
	public void registerErrorPage(int code, String url) throws IllegalArgumentException {
		if (url == null || url.isEmpty())
			throw new IllegalArgumentException("Null/Empty url");
		
		errorHandler.addErrorPage(code, url);
	}
	
	/** @return returns wrapped context handler */
	ServletContextHandler getContextHandler() {
		return this.contextHandler;
	}
	// =========================================================================
}
