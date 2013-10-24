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

import java.nio.file.Path;
import java.nio.file.Paths;

import junit.framework.TestCase;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.sun.jna.platform.win32.Advapi32Util.EventLogIterator;
import com.sun.jna.platform.win32.Advapi32Util.EventLogRecord;
import com.sun.jna.platform.win32.Advapi32Util.EventLogType;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinReg;

/**
 * 
 * Win32EventLogAppender tests.
 * 
 * @author Curt Arnold
 * @author <a href="mailto:dblock@dblock.org">Daniel Doubrovkine</a>
 * @author <a href="mailto:tony@niemira.com">Tony Niemira</a>
 * 
 */
public class Win32EventLogAppenderTest extends TestCase {

	private Logger _logger = null;
	private Win32EventLogAppender w32ela = null;
	
	public void setUp() {
		w32ela = new Win32EventLogAppender();
		w32ela.setSource("Log4jnaTest");
		w32ela.setApplication("Log4jnaApplicationTest");

		String categoryMessageFile = System.getProperty("categoryMessageFile");
		if (categoryMessageFile != null) {
			w32ela.setCategoryMessageFile(categoryMessageFile);
		}
		
		String eventMessageFile = System.getProperty("eventMessageFile");
		if (eventMessageFile != null) {
			w32ela.setEventMessageFile(eventMessageFile);
		}
		
		BasicConfigurator.configure(w32ela);
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
	
	public void testLongEvent() {
		String message = getLongString();
		_logger.info(message + "Truncated bit");
		expectEvent(message, Level.INFO, EventLogType.Informational);		
	}

	public void testRegistryValues() {
		
		String eventSourceKeyPath = "SYSTEM\\CurrentControlSet\\Services\\EventLog\\"
				+ w32ela.getApplication() + "\\" + w32ela.getSource();
		
		if (System.getProperty("eventMessageFile") != null) {
			String eventMessageFileInRegistry = Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE,
					eventSourceKeyPath, "EventMessageFile");
	
			Path eventMessageFileGiven = Paths.get(System.getProperty("eventMessageFile"));
			assertEquals(eventMessageFileInRegistry, eventMessageFileGiven.toString().replaceAll("\\\\\\\\", "\\\\"));
		}
		
		if (System.getProperty("categoryMessageFile") != null) {
			String categoryMessageFileInRegistry = Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE,
					eventSourceKeyPath, "CategoryMessageFile");

			Path categoryMessageFileGiven = Paths.get(System.getProperty("categoryMessageFile"));
			assertEquals(categoryMessageFileInRegistry, categoryMessageFileGiven.toString().replaceAll("\\\\\\\\", "\\\\"));
		}
	}
	
	/*
	public void testException() {
		String message = "log4jna exception message @ " + Kernel32.INSTANCE.GetTickCount();		
		_logger.debug(message, new Exception("testing exception"));
		expectEvent(message, Level.DEBUG, EventLogType.Informational);		
	}
	*/
	
	private void expectEvent(String message, Level level, EventLogType eventLogType) {
		EventLogIterator iter = new EventLogIterator(null, "Log4jnaTest", WinNT.EVENTLOG_BACKWARDS_READ);
		try {
			assertTrue(iter.hasNext());
			EventLogRecord record = iter.next();
			assertEquals("Log4jnaTest", record.getSource());

			//assertEquals("Log4jnaApplicationTest", record.get());
			assertEquals(eventLogType, record.getType());			
			assertEquals(1, record.getRecord().NumStrings.intValue());
			assertNull(record.getData());
			
			// The full message includes a level and the full class name
			String fullMessage = level + " " + Win32EventLogAppenderTest.class.getCanonicalName() + " - " + message;

			// The event message has the location tacked on the front
			String eventMessage = record.getStrings()[0].trim();

			int levelMarker = eventMessage.indexOf(level.toString());
			assertTrue("missing level marker in '" + eventMessage + "'", levelMarker >= 0);
			String eventMessageWithoutLocation = eventMessage.substring(levelMarker);

			// Truncated messages will have lost the length of the location, level and class name
			// Normal messages will be unaffected by this adjustment
			int eventMsgWL_len = eventMessageWithoutLocation.length();
			fullMessage = fullMessage.substring(0, eventMsgWL_len);
			
			assertEquals(fullMessage, eventMessageWithoutLocation);

			// Long strings will not be seen in eclipse
			System.out.println(record.getStrings()[0]);
		} finally {
			iter.close();
		}
	}
	
	private String getLongString(){
		int strSize = 31000;
		StringBuilder str = new StringBuilder(strSize);
		for (int i = 0; i < strSize; i++){
			str.append("X");
		}
		
		return str.toString();
	}
}
