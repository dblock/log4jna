/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License.  You may obtain a copy 
 * of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dnlock.log4jna.nt.test;

import static org.junit.Assert.*;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.message.SimpleMessage;
import org.dblock.log4jna.nt.Win32EventLogAppender;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Advapi32Util.EventLogIterator;
import com.sun.jna.platform.win32.Advapi32Util.EventLogRecord;
import com.sun.jna.platform.win32.Advapi32Util.EventLogType;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinReg;

/**
 * Test case for {@link Win32EventLogAppender}.
 * 
 * @author Curt Arnold
 * @author <a href="mailto:dblock@dblock.org">Daniel Doubrovkine</a>
 * @author <a href="mailto:tony@niemira.com">Tony Niemira</a>
 * @author <a href="mailto:claudio.trajtenberg@cgtca.ca">Claudio Trajtenberg</a>
 * 
 */
public class Win32EventLogAppenderTest {

	/**
	 * This must exist in the windows registry.
	 * <p>
	 * Key:
	 * </p>
	 * <p>
	 * <code>HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\EventLog\Application\Log4jnaTest</code>
	 * </p>
	 * <p>
	 * With entries:
	 * </p>
	 * 
	 * <pre>
	 * Name: <code>TypesSupported</code> 
	 * Type: <code>REG_DWORD</code> 
	 * Data: <code>0x7</code>
	 * 
	 * Name: <code>CategoryCount</code> 
	 * Type: <code>REG_DWORD</code> 
	 * Data: <code>0x6</code>
	 * </pre>
	 */
	private static final String TEST_LOGGER_NAME = "Log4jnaTest";

	/**
	 * The dll file location from the <code>src/test/resources</code> folder.
	 */
	private static String _eventLogAppenderDLL = Paths.get("src/test/resources/Win32EventlogAppender.dll")
			.toAbsolutePath().toString();

	/**
	 * Class under test
	 */
	private Win32EventLogAppender _eventLogAppender = null;

/**
 * Waits for the test event source to be usable. On a fresh machine, the first
 * use of a newly registered source can briefly fail with "Access is denied"
 * while the Event Log service picks up its registry key.
 */
@BeforeClass
public static void waitForEventSource() throws InterruptedException {
	long deadline = System.currentTimeMillis() + 30000;
	while (true) {
		String message = "log4jna warm-up message @ " + Kernel32.INSTANCE.GetTickCount();
		Win32EventLogAppender appender = newAppender();
		try {
			appender.append(asLogEvent(appender, message, Level.INFO));
			EventLogIterator iter = new EventLogIterator(null, TEST_LOGGER_NAME, WinNT.EVENTLOG_BACKWARDS_READ);
			try {
				if (iter.hasNext() && String.join("", iter.next().getStrings()).contains(message)) {
					return;
				}
			} finally {
				iter.close();
			}
		} catch (RuntimeException e) {
			if (System.currentTimeMillis() > deadline) {
				throw e;
			}
		} finally {
			appender.close();
		}
		if (System.currentTimeMillis() > deadline) {
			fail("The " + TEST_LOGGER_NAME + " event source did not become usable");
		}
		Thread.sleep(500);
	}
}

@Before
public void setUp() {
	_eventLogAppender = newAppender();
}

@After
public void tearDown() {
	if (_eventLogAppender != null) {
		_eventLogAppender.close();
	}
}

private static Win32EventLogAppender newAppender() {
	Layout<? extends Serializable> layout = PatternLayout.newBuilder()
			.withPattern(PatternLayout.TTCC_CONVERSION_PATTERN).build();
	Win32EventLogAppender appender = Win32EventLogAppender.createAppender("appenderName", null, null,
			Win32EventLogAppenderTest.class.getName(), _eventLogAppenderDLL, _eventLogAppenderDLL, layout, null);
	appender.setSource(TEST_LOGGER_NAME);
	appender.setApplication("Application");
	return appender;
}

	/**
	 * The first event to a newly created event log and source must be logged,
	 * even while the Event Log service briefly denies access to the new source.
	 */
	@Test
	public void testFirstEventToNewEventSource() {
		int tick = Kernel32.INSTANCE.GetTickCount();
		String application = "Log4jnaNewLog" + tick;
		String source = "Log4jnaNewSource" + tick;
		String applicationKeyPath = "SYSTEM\\CurrentControlSet\\Services\\EventLog\\" + application;
		String sourceKeyPath = applicationKeyPath + "\\" + source;
		Win32EventLogAppender appender = newAppender();
		appender.setSource(source);
		appender.setApplication(application);
		try {
			appender.append(asLogEvent(appender, "log4jna first message to a new event source", Level.INFO));
			assertTrue(Advapi32Util.registryKeyExists(WinReg.HKEY_LOCAL_MACHINE, sourceKeyPath));
		} finally {
			appender.close();
			deleteKeyTree(applicationKeyPath);
		}
	}

	/**
	 * Deletes a registry key and its subkeys, ignoring failures so that cleanup
	 * does not hide the outcome of a test.
	 */
	private static void deleteKeyTree(String keyPath) {
		try {
			if (!Advapi32Util.registryKeyExists(WinReg.HKEY_LOCAL_MACHINE, keyPath)) {
				return;
			}
			for (String subKey : Advapi32Util.registryGetKeys(WinReg.HKEY_LOCAL_MACHINE, keyPath)) {
				deleteKeyTree(keyPath + "\\" + subKey);
			}
			Advapi32Util.registryDeleteKey(WinReg.HKEY_LOCAL_MACHINE, keyPath);
		} catch (RuntimeException e) {
			System.err.println("Could not delete " + keyPath + ": " + e.getMessage());
		}
	}

	/**
	 * Test case for {@link Win32EventLogAppender#append(LogEvent)} with debug
	 * level.
	 */
	@Test
	public void testDebugEvent() {
		String message = "log4jna debug message @ " + Kernel32.INSTANCE.GetTickCount();
		_eventLogAppender.append(asLogEvent(_eventLogAppender, message, Level.DEBUG));
		expectEvent(message, Level.DEBUG, EventLogType.Informational);
	}

	/**
	 * Test case for {@link Win32EventLogAppender#append(LogEvent)} with info
	 * level.
	 */
	@Test
	public void testInfoEvent() {
		String message = "log4jna info message @ " + Kernel32.INSTANCE.GetTickCount();
		_eventLogAppender.append(asLogEvent(_eventLogAppender, message, Level.INFO));
		expectEvent(message, Level.INFO, EventLogType.Informational);
	}

	/**
	 * Test case for {@link Win32EventLogAppender#append(LogEvent)} with warn
	 * level.
	 */
	@Test
	public void testWarnEvent() {
		String message = "log4jna warn message @ " + Kernel32.INSTANCE.GetTickCount();
		_eventLogAppender.append(asLogEvent(_eventLogAppender, message, Level.WARN));
		expectEvent(message, Level.WARN, EventLogType.Warning);
	}

	/**
	 * Test case for {@link Win32EventLogAppender#append(LogEvent)} with fatal level.
	 */
	@Test
	public void testFatalEvent() {
		String message = "log4jna fatal message @ " + Kernel32.INSTANCE.GetTickCount();
		_eventLogAppender.append(asLogEvent(_eventLogAppender, message, Level.FATAL));
		expectEvent(message, Level.FATAL, EventLogType.Error);
	}

	public void donttestRegistryValues() {
		String eventSourceKeyPath = "SYSTEM\\CurrentControlSet\\Services\\EventLog\\"
				+ _eventLogAppender.getApplication() + "\\" + _eventLogAppender.getSource();

		String eventMessageFileInRegistry = Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE,
				eventSourceKeyPath, "EventMessageFile");

		Path eventMessageFileGiven = Paths.get(_eventLogAppenderDLL);
		assertEquals(eventMessageFileInRegistry, eventMessageFileGiven.toString());

		String categoryMessageFileInRegistry = Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE,
				eventSourceKeyPath, "CategoryMessageFile");

		Path categoryMessageFileGiven = Paths.get(_eventLogAppenderDLL);
		assertEquals(categoryMessageFileInRegistry, categoryMessageFileGiven.toString());
	}

	private static LogEvent asLogEvent(Win32EventLogAppender appender, String message, Level level) {
		return new Log4jLogEvent.Builder().setLoggerName(TEST_LOGGER_NAME).setMarker(null)
				.setLoggerFqcn(appender.getClass().getName()).setLevel(level)
				.setMessage(new SimpleMessage(message)).setTimeMillis(System.currentTimeMillis()).build();
	}

	/*
	 * public void testException() { String message =
	 * "log4jna exception message @ " + Kernel32.INSTANCE.GetTickCount();
	 * _logger.debug(message, new Exception("testing exception"));
	 * expectEvent(message, Level.DEBUG, EventLogType.Informational); }
	 */

	private void expectEvent(String message, Level level, EventLogType eventLogType) {
		EventLogIterator iter = new EventLogIterator(null, TEST_LOGGER_NAME, WinNT.EVENTLOG_BACKWARDS_READ);
		try {
			assertTrue(iter.hasNext());
			EventLogRecord record = iter.next();
			assertEquals(TEST_LOGGER_NAME, record.getSource());

			assertEquals(eventLogType, record.getType());
			assertEquals(1, record.getRecord().NumStrings.intValue());
			assertNull(record.getData());

			// The full message includes a level and the full class name
			String fullMessage = level + " " + TEST_LOGGER_NAME + " - " + message;

			// The event message has the location tacked on the front
			StringBuilder eventMessage = new StringBuilder();
			for (int i = 0; i < record.getStrings().length; i++) {
				eventMessage.append(record.getStrings()[i].trim());
			}

			int levelMarker = eventMessage.indexOf(level.toString());
			assertTrue("missing level marker in '" + eventMessage + "'", levelMarker >= 0);
			String eventMessageWithoutLocation = eventMessage.substring(levelMarker);

			assertEquals(fullMessage, eventMessageWithoutLocation);
		} finally {
			iter.close();
		}
	}
}