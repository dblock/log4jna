/*
 * Licensed under the Apache license, Version 2.0 (the "License");
 *  you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */
package org.apache.log4jna.nt.demo.test;

import static org.junit.Assert.*;

import org.apache.log4jna.nt.demo.NewDemo;
import org.apache.logging.log4j.Level;
import org.junit.Before;
import org.junit.Test;

import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.Advapi32Util.EventLogIterator;
import com.sun.jna.platform.win32.Advapi32Util.EventLogRecord;
import com.sun.jna.platform.win32.Advapi32Util.EventLogType;

/**
 * Test cases for {@link NewDemo}
 * 
 * @author <a href="mailto:claudio.trajtenberg@cgtca.ca">Claudio Trajtenberg</a>
 *
 */
public class NewDemoTest {

	/*
	 * Use this configuration if using the Application log4j2.xml file
	 */
	// private static final String EVENT_SOURCE = "Log4jnaTest";

	/*
	 * Use this configuration if using the Win32LogApplication log4j2.xml file
	 */
	private static final String EVENT_SOURCE = "WinLogger";

	private static final String EXCEPTION = "java.lang.Exception: Pouporselly thrown for demo";

	private NewDemo classUnderTest;

	private long testStartedTime;

	private long testEndedTime;

	@Before
	public void setUp() throws Exception {
		this.classUnderTest = new NewDemo();
	}

	/**
	 * Test case for {@link NewDemo#trace()}.
	 */
	@Test
	public void testTrace() {
		this.testStartedTime = System.currentTimeMillis() / 1000;
		this.classUnderTest.trace();
		this.testEndedTime = System.currentTimeMillis() / 1000;
		expectEventNoException(Level.TRACE, EventLogType.Informational);
	}

	/**
	 * Test case for {@link NewDemo#debug()}.
	 */
	@Test
	public void testDebug() {
		this.testStartedTime = System.currentTimeMillis() / 1000;
		this.classUnderTest.debug();
		this.testEndedTime = System.currentTimeMillis() / 1000;
		expectEventNoException(Level.DEBUG, EventLogType.Informational);
	}

	/**
	 * Test case for {@link NewDemo#info()}.
	 */
	@Test
	public void testInfo() {
		this.testStartedTime = System.currentTimeMillis() / 1000;
		this.classUnderTest.info();
		this.testEndedTime = System.currentTimeMillis() / 1000;
		expectEventNoException(Level.INFO, EventLogType.Informational);
	}

	/**
	 * Test case for {@link NewDemo#warn()}.
	 */
	@Test
	public void testWarn() {
		this.testStartedTime = System.currentTimeMillis() / 1000;
		this.classUnderTest.warn();
		this.testEndedTime = System.currentTimeMillis() / 1000;
		expectEventNoException(Level.WARN, EventLogType.Warning);
	}

	/**
	 * Test case for {@link NewDemo#error()}.
	 */
	@Test
	public void testError() {
		this.testStartedTime = System.currentTimeMillis() / 1000;
		this.classUnderTest.error();
		this.testEndedTime = System.currentTimeMillis() / 1000;
		expectEventNoException(Level.ERROR, EventLogType.Error);
	}

	/**
	 * Test case for {@link NewDemo#fatal()}.
	 */
	@Test
	public void testFatal() {
		this.testStartedTime = System.currentTimeMillis() / 1000;
		this.classUnderTest.fatal();
		this.testEndedTime = System.currentTimeMillis() / 1000;
		expectEventNoException(Level.FATAL, EventLogType.Error);
	}

	/**
	 * Test case for {@link NewDemo#warnWithException()}.
	 */
	@Test
	public void testWarnWithException() {
		this.testStartedTime = System.currentTimeMillis() / 1000;
		this.classUnderTest.warnWithException();
		this.testEndedTime = System.currentTimeMillis() / 1000;
		expectEventException(Level.WARN, EventLogType.Warning);
	}

	/**
	 * Test case for {@link NewDemo#errorWithException()()}.
	 */
	@Test
	public void testErrorWithException() {
		this.testStartedTime = System.currentTimeMillis() / 1000;
		this.classUnderTest.errorWithException();
		this.testEndedTime = System.currentTimeMillis() / 1000;
		expectEventException(Level.ERROR, EventLogType.Error);
	}

	/**
	 * Test case for {@link NewDemo#fatalWithException()}.
	 */
	@Test
	public void testFatalWithException() {
		this.testStartedTime = System.currentTimeMillis() / 1000;
		this.classUnderTest.fatalWithException();
		this.testEndedTime = System.currentTimeMillis() / 1000;
		expectEventException(Level.FATAL, EventLogType.Error);
	}

	private void expectEventNoException(Level level, EventLogType eventLogType) {
		EventLogIterator iter = new EventLogIterator(null, EVENT_SOURCE, WinNT.EVENTLOG_BACKWARDS_READ);
		try {
			assertTrue("No event log records to process", iter.hasNext());
			int recordCount = 0;
			while (iter.hasNext() && recordCount < 2) {
				EventLogRecord record = iter.next();

				if (record.getRecord().TimeWritten.longValue() >= this.testStartedTime
						&& record.getRecord().TimeWritten.longValue() <= this.testEndedTime) {

					recordCount++;

					assertEquals(EVENT_SOURCE, record.getSource());

					assertEquals(eventLogType, record.getType());
					assertEquals(1, record.getRecord().NumStrings.intValue());
					assertNull(record.getData());

					// Build the message
					StringBuilder message = new StringBuilder();
					switch (recordCount) {
					case 1:
						message.append("Exit ");
						break;

					case 2:
						message.append("Enter ");
						break;

					default:
						break;
					}
					message.append(level.name().toLowerCase());
					String fullMessage = String.format("%-5s [main] %s", level, message);

					// The event message has the location tacked on the front
					StringBuilder eventMessage = new StringBuilder();
					for (int i = 0; i < record.getStrings().length; i++) {
						eventMessage.append(record.getStrings()[i].trim());
					}

					int levelMarker = eventMessage.indexOf(level.toString());
					assertTrue("missing level marker in '" + eventMessage + "'", levelMarker >= 0);
					String eventMessageWithoutLocation = eventMessage.substring(levelMarker);

					assertEquals(fullMessage, eventMessageWithoutLocation);
				}
			}
			assertTrue("No records to process", recordCount > 0);
		} finally {
			iter.close();
		}
	}

	private void expectEventException(Level level, EventLogType eventLogType) {
		EventLogIterator iter = new EventLogIterator(null, EVENT_SOURCE, WinNT.EVENTLOG_BACKWARDS_READ);
		try {
			assertTrue(iter.hasNext());
			EventLogRecord record = iter.next();

			if (record.getRecord().TimeWritten.longValue() >= this.testStartedTime
					&& record.getRecord().TimeWritten.longValue() <= this.testEndedTime) {

				assertEquals(EVENT_SOURCE, record.getSource());

				assertEquals(eventLogType, record.getType());
				assertEquals(1, record.getRecord().NumStrings.intValue());
				assertNull(record.getData());

				// Build the message
				String messageStart = String.format("%-5s [main] In %s with exception", level,
						level.name().toLowerCase());

				// The event message has the location tacked on the front
				StringBuilder eventMessage = new StringBuilder();
				for (int i = 0; i < record.getStrings().length; i++) {
					eventMessage.append(record.getStrings()[i].trim());
				}

				int levelMarker = eventMessage.indexOf(level.toString());
				assertTrue("missing level marker in '" + eventMessage + "'", levelMarker >= 0);
				String eventMessageWithoutLocation = eventMessage.substring(levelMarker);

				assertTrue(String.format("Mising %s", messageStart),
						eventMessageWithoutLocation.contains(messageStart));
				assertTrue(String.format("Mising %s", EXCEPTION), eventMessageWithoutLocation.contains(EXCEPTION));
			}
		} finally {
			iter.close();
		}
	}
}
