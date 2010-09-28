/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.log4jna.nt;

import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import org.apache.log4j.Level;
import org.apache.log4j.BasicConfigurator;

/**
 * 
 * Win32EventLogAppender tests.
 * 
 * @author Curt Arnold
 * @author <a href="mailto:dblock@dblock.org">Daniel Doubrovkine</a>
 */
public class Win32EventLogAppenderTest extends TestCase {

	/**
	 * Clean up configuration after each test.
	 */
	public void tearDown() {
		LogManager.shutdown();
	}

	/**
	 * Simple test of Win32EventLogAppender.
	 */
	public void testSimple() {
		BasicConfigurator.configure(new Win32EventLogAppender());
		Logger logger = Logger
				.getLogger("org.apache.log4j.win32.Win32EventLogAppenderTest");
		int i = 0;
		logger.debug("Message " + i++);
		logger.info("Message " + i++);
		logger.warn("Message " + i++);
		logger.error("Message " + i++);
		logger.log(Level.FATAL, "Message " + i++);
		logger.debug("Message " + i++, new Exception("Just testing."));
	}
}
