/*
 * Copyright 2005, x4juli.org.
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
package org.x4juli.sample.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Servlet implementation class for Servlet: JclSample
 *
 */
 public class JclSample extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
	 
	 private static final Log LOG = LogFactory.getLog(JclSample.class);
	 
    /**
	 * 
	 */
	private static final long serialVersionUID = 2607545894775990607L;

	/**
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public JclSample() {
		super();
	}   	
	
	/**
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("JclSample: Starting with sample, mode org.apache.commons.logging.Log System.out");
		System.err.println("JclSample: Starting with sample, mode org.apache.commons.logging.Log System.err");
		super.log("JclSample: Using method javax.servlet.GenericServlet.log(String msg)");
		System.out.println("JclSample: Using class "+LOG.getClass().getName());

        LOG.trace("JclSample: Using method LOG.trace(Object arg)");
		LOG.debug("JclSample: Using method LOG.debug(Object arg)");
		LOG.info("JclSample: Using method LOG.info(Object arg)");
		LOG.error("JclSample: Using method LOG.error(Object arg)");
		LOG.fatal("JclSample: Using method LOG.fatal(Object arg)");
		LOG.fatal("JclSample: Using method LOG.fatal(Object arg, Throwable t)", 
				new Exception("JclSample Exception, don't care about it"));
		
		System.out.println("JclSample: End of sample, mode org.apache.commons.logging.Log System.out");
		System.err.println("JclSample: End of sample, mode org.apache.commons.logging.Log System.err");

	}  	
	
	/**
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}   	  	    
}