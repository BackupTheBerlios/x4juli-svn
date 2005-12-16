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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet implementation class for Servlet: Slf4jSample
 * 
 */
public class Slf4jSample extends javax.servlet.http.HttpServlet implements
		javax.servlet.Servlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8528917288681299703L;
	
	private static final Logger LOG = LoggerFactory
			.getLogger(Slf4jSample.class);

	/**
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public Slf4jSample() {
		super();
	}

	/**
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request,
	 *      HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Slf4jSample: Starting with sample, mode org.slf4j.Logger System.out");
		System.err.println("Slf4jSample: Starting with sample, mode org.slf4j.Logger System.err");
		super.log("Slf4jSample: Using method javax.servlet.GenericServlet.log(String msg)");
		System.out.println("Slf4jSample: Using class "+LOG.getClass().getName());
		
		LOG.debug("Slf4jSample: Using method LOG.debug(String msg)");
		LOG.info("Slf4jSample: Using method LOG.info(String msg)");
		LOG.warn("Slf4jSample: Using method LOG.warn(String msg)");
		LOG.error("Slf4jSample: Using method LOG.error(String msg)");

		LOG.info("Slf4jSample: Using method LOG.info(String msg, Object arg1) Object0[{}]",
				new Integer(4711));
		LOG.warn("Slf4jSample: Using method LOG.warn(String msg, Object arg1, Object arg2) Object0[{}] Object1[{}]",
					new Integer(4711), new Double("815.5"));
		LOG.info("Slf4jSample: Using method LOG.info(String msg, Throwable t",new Exception("Slf4j Sample Exception, don't care about it"));
		System.out.println("Slf4jSample: End of sample, mode org.slf4j.Logger System.out");
		System.err.println("Slf4jSample: End of sample, mode org.slf4j.Logger System.err");

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