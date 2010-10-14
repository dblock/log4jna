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

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.sun.jna.platform.win32.Advapi32Util.EventLogIterator;
import com.sun.jna.platform.win32.Advapi32Util.EventLogRecord;
import com.sun.jna.platform.win32.Advapi32Util.EventLogType;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;

/**
 * 
 * Win32EventLogAppender tests.
 * 
 * @author Curt Arnold
 * @author <a href="mailto:dblock@dblock.org">Daniel Doubrovkine</a>
 */
public class Win32EventLogAppenderTest extends TestCase {

	private Logger _logger = null;
	
	public void setUp() {
		BasicConfigurator.configure(new Win32EventLogAppender());
		_logger = Logger.getLogger(Win32EventLogAppenderTest.class);		
	}
	
	/**
	 * Clean up configuration after each test.
	 */
	public void tearDown() {
		LogManager.shutdown();
	}

	public void testDebugEvent() {
		String message = "log4jna debug message @ " + Kernel32.INSTANCE.GetTickCount();		
		_logger.debug(message);
		expectEvent(message, Level.DEBUG, EventLogType.Informational);
	}
	
	public void testInfoEvent() {
		String message = "log4jna info message @ " + Kernel32.INSTANCE.GetTickCount();		
		_logger.info(message);
		expectEvent(message, Level.INFO, EventLogType.Informational);		
	}
	
	public void testWarnEvent() {
		String message = "log4jna warn message @ " + Kernel32.INSTANCE.GetTickCount();		
		_logger.warn(message);
		expectEvent(message, Level.WARN, EventLogType.Warning);				
	}
	
	public void testFatalEvent() {
		String message = "log4jna fatal message @ " + Kernel32.INSTANCE.GetTickCount();		
		_logger.log(Level.FATAL, message);
		expectEvent(message, Level.FATAL, EventLogType.Error);				
	}
	
	/*
	public void testException() {
		String message = "log4jna exception message @ " + Kernel32.INSTANCE.GetTickCount();		
		_logger.debug(message, new Exception("testing exception"));
		expectEvent(message, Level.DEBUG, EventLogType.Informational);		
	}
	*/
	
	private void expectEvent(String message, Level level, EventLogType eventLogType) {
		EventLogIterator iter = new EventLogIterator(null, "Application", WinNT.EVENTLOG_BACKWARDS_READ);
		try {
			assertTrue(iter.hasNext());
			EventLogRecord record = iter.next();
			assertEquals("Log4jna", record.getSource());
			assertEquals(eventLogType, record.getType());			
			assertEquals(1, record.getRecord().NumStrings.intValue());
			assertNull(record.getData());
			String fullMessage = level + " " + Win32EventLogAppenderTest.class.getCanonicalName() + " - " + message;
			String eventMessage = record.getStrings()[0].trim();
			int levelMarker = eventMessage.indexOf(level.toString());
			assertTrue("missing level marker in '" + eventMessage + "'", levelMarker >= 0);
			String eventMessageWithoutLocation = eventMessage.substring(levelMarker);
			assertEquals(fullMessage, eventMessageWithoutLocation);
			System.out.println(record.getStrings()[0]);
		} finally {
			iter.close();
		}
	}
}
