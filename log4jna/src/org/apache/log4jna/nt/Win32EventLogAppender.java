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

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.Priority;
import org.apache.log4j.TTCCLayout;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.LoggingEvent;

import com.sun.jna.platform.win32.Advapi32;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.WinReg;

/**
 * Append to the NT event log system.
 * 
 * <p>
 * <b>WARNING</b> This appender can only be installed and used on a Windows
 * system.
 * 
 * <p>
 * Do not forget to place jna.jar and platform.jar in the CLASSPATH.
 * </p>
 * 
 * @author <a href="mailto:cstaylor@pacbell.net">Chris Taylor</a>
 * @author <a href="mailto:jim_cakalic@na.biomerieux.com">Jim Cakalic</a>
 * @author <a href="mailto:dblock@dblock.org">Daniel Doubrovkine</a>
 */
public class Win32EventLogAppender extends AppenderSkeleton {
	private String _source = null;
	private String _server = null;
	private String _log = null;
	private HANDLE _handle = null;

	public Win32EventLogAppender() {
		this(null, null, null, null);
	}

	public Win32EventLogAppender(String source) {
		this(null, source, null, null);
	}

	public Win32EventLogAppender(String server, String source) {
		this(server, source, null, null);
	}

	public Win32EventLogAppender(String server, String source, String log) {
		this(server, source, null, null);
	}
	
	public Win32EventLogAppender(Layout layout) {
		this(null, null, null, layout);
	}

	public Win32EventLogAppender(String source, Layout layout) {
		this(null, source, null, layout);
	}

	public Win32EventLogAppender(String source, String log, Layout layout) {
		this(null, source, log, layout);
	}
	
	public Win32EventLogAppender(String server, String source, String log, Layout layout) {
		if (source == null || source.length() == 0) {
			source = "Log4jna";
		}
		
		if (log == null || log.length() == 0){
			log = "Application";
		}
		
		if (layout == null) {
			layout = new TTCCLayout();
		}
		
		this.layout = layout;
		this._server = server;
		this._source = source;
		this._log = log;
	}

	public void close() {
		if (_handle != null) {
			if (!Advapi32.INSTANCE.DeregisterEventSource(_handle)) {
				Exception e = new Win32Exception(Kernel32.INSTANCE.GetLastError());
				getErrorHandler().error("Could not close appender.", e, ErrorCode.CLOSE_FAILURE);
			}
			_handle = null;
		}
	}
	
	private void registerEventSource() {
		close();
		
		try {
			_handle = registerEventSource(_server, _source, _log);
		} catch (Exception e) {
			LogLog.error("Could not register event source.", e);
			close();
		}
	}

	public void activateOptions() {
		registerEventSource();
	}

	public void append(LoggingEvent event) {

		if (_handle == null) {
			registerEventSource();
		}

		StringBuffer sbuf = new StringBuffer();

		sbuf.append(layout.format(event));
		if (layout.ignoresThrowable()) {
			String[] s = event.getThrowableStrRep();
			if (s != null) {
				int len = s.length;
				for (int i = 0; i < len; i++) {
					sbuf.append(s[i]);
				}
			}
		}
		// Normalize the log message level into the supported categories
		int nt_category = event.getLevel().toInt();

		// Anything above FATAL or below DEBUG is labeled as INFO.
		// if (nt_category > FATAL || nt_category < DEBUG) {
		// nt_category = INFO;
		// }
		reportEvent(sbuf.toString(), nt_category);
	}

	public void finalize() {
		close();
	}

	/**
	 * The <b>Source</b> option which names the source of the event. The current
	 * value of this constant is <b>Source</b>.
	 */
	public void setSource(String source) {
		_source = source.trim();		
	}

	public String getSource() {
		return _source;
	}

	public String getLog() {
		return _log;
	}

	public void setLog(String log) {
		_log = log.trim();
	}

	/**
	 * The <code>Win32EventLogAppender</code> requires a layout. Hence, this method
	 * always returns <code>true</code>.
	 */
	public boolean requiresLayout() {
		return true;
	}

	private static HANDLE registerEventSource(String server, String source, String log) {
		String eventSourceKeyPath = "SYSTEM\\CurrentControlSet\\Services\\EventLog\\"
				+ log + "\\"
				+ source;
		if (Advapi32Util.registryCreateKey(WinReg.HKEY_LOCAL_MACHINE,
				eventSourceKeyPath)) {
			// TODO: set event message file and source
			// EventMessageFile
			// CategoryMessageFile
			Advapi32Util.registrySetIntValue(WinReg.HKEY_LOCAL_MACHINE,
					eventSourceKeyPath, "TypesSupported", 7);
			Advapi32Util.registrySetIntValue(WinReg.HKEY_LOCAL_MACHINE,
					eventSourceKeyPath, "CategoryCount", 6);
		}

		HANDLE h = Advapi32.INSTANCE.RegisterEventSource(server, source);
		if (h == null) {
			throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
		}

		return h;
	}

	private void reportEvent(String message, int priority) {
		// This is the only message supported by the package. It is backed by
		// a message resource which consists of just '%1' which is replaced
		// by the string we just created.
		final int messageID = 0x1000;

		String[] buffer = { message };
		if (!Advapi32.INSTANCE.ReportEvent(_handle, getEventLogType(priority),
				getEventLogCategory(priority), messageID, null, 1, 0, buffer,
				null)) {
			Exception e =  new Win32Exception(Kernel32.INSTANCE.GetLastError());
			getErrorHandler().error("Failed to report event ["+message+"].", e, ErrorCode.WRITE_FAILURE);
		}
	}

	/**
	 * Convert log4j Priority to an EventLog type. The log4j package supports 8
	 * defined priorities, but the NT EventLog only knows 3 event types of
	 * interest to us: ERROR, WARNING, and INFO.
	 * @param priority
	 *  Log4j priority.
	 * @return EventLog type.
	 */
	private static int getEventLogType(int priority) {
		int type = WinNT.EVENTLOG_SUCCESS;
		if (priority >= Priority.INFO_INT) {
			type = WinNT.EVENTLOG_INFORMATION_TYPE;
			if (priority >= Priority.WARN_INT) {
				type = WinNT.EVENTLOG_WARNING_TYPE;
				if (priority >= Priority.ERROR_INT) {
					type = WinNT.EVENTLOG_ERROR_TYPE;
				}
			}
		}
		return type;
	}

	/**
	 * Convert log4j Priority to an EventLog category. Each category is backed
	 * by a message resource so that proper category names will be displayed in
	 * the NT Event Viewer.
	 * @param priority
	 *  Log4J priority.
	 * @return EventLog category.
	 */
	private static int getEventLogCategory(int priority) {
		int category = 1;
		if (priority >= Priority.DEBUG_INT) {
			category = 2;
			if (priority >= Priority.INFO_INT) {
				category = 3;
				if (priority >= Priority.WARN_INT) {
					category = 4;
					if (priority >= Priority.ERROR_INT) {
						category = 5;
						if (priority >= Priority.FATAL_INT) {
							category = 6;
						}
					}
				}
			}
		}
		return category;
	}
}
