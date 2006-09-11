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
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class for Servlet: X4JuliSample.
 * 
 */
public class JulSample extends javax.servlet.http.HttpServlet implements
		javax.servlet.Servlet {
	
	private static final Logger LOG = Logger.getLogger(JulSample.class.getName());

	/**
	 * 
	 */
	private static final long serialVersionUID = -4521042120673107227L;

	/**
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public JulSample() {
		super();
	}

	/**
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request,
	 *      HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("JulSample: Starting with sample, mode java.util.logging.Logger System.out");
		System.err.println("JulSample: Starting with sample, mode java.util.logging.Logger System.err");
		super.log("JulSample: Using method javax.servlet.GenericServlet.log(String msg)");
		System.out.println("JulSample: Using class "+LOG.getClass().getName());
        System.out.println("JulSample: Using logger "+LOG);
        Handler[] myHandler = Logger.getLogger("org.x4juli").getHandlers();
		for (int i = 0; i < myHandler.length; i++) {
            Handler handler = myHandler[i];
            System.out.println("JulSample org.x4juli: " + handler);
        }
        myHandler = Logger.getLogger("org.x4juli.sample").getHandlers();
        for (int i = 0; i < myHandler.length; i++) {
            Handler handler = myHandler[i];
            System.out.println("JulSample org.x4juli.sample: " + handler);
        }

        LOG.entering(JulSample.class.getName(),"doGet",new Object[]{request, response});
		
		LOG.log(Level.FINEST, "JulSample: Using method LOG.log(Level.FINEST, String msg)");
		LOG.log(Level.INFO, "JulSample: Using method LOG.log(Level.INFO, String msg)");
		LOG.log(Level.SEVERE, "JulSample: Using method LOG.log(Level.SEVERE, String msg)");
		LOG.log(Level.ALL, "JulSample: Using method LOG.log(Level.ALL, String msg)");

		LOG.config("JulSample: Using method LOG.config(String msg)");
		LOG.finer("JulSample: Using method LOG.finer(String msg)");
		LOG.warning("JulSample: Using method LOG.warning(String msg)");
		
		LOG.log(Level.SEVERE, "JulSample: Using method LOG.log(Level.SEVERE, String msg)", 
				 new Exception("JulSample Exception, don't care about it"));
		
		LOG.log(Level.INFO, "JulSample: Using method LOG.log(Level.INFO, String msg, Object arg) Integer[{0}]",
				new Integer(4711));
		LOG.log(Level.WARNING, "JulSample: Using method LOG.log(Level.WARNING, String msg, Object[] params) Integer[{0}]Double[{1}]",
				new Object[]{new Integer(4711), new Double("815.5")});

		LOG.exiting(JulSample.class.getName(),"doGet");
		
		System.out.println("JulSample: End of sample, mode java.util.logging.Logger System.out");
		System.err.println("JulSample: End of sample, mode java.util.logging.Logger System.err");
	}

	/**
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request,
	 *      HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
}