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

import java.io.Serializable;

import org.apache.logging.log4j.*;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

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
 * @author <a href="mailto:tony@niemira.com">Tony Niemira</a>
 */
@SuppressWarnings("serial")
@Plugin(name = "Win32EventLog", category = "Core",
		elementType = "appender", printObject=true)
public class Win32EventLogAppender extends AbstractAppender {
	/**
	 * 
	 */
	private String _source = null;
	private String _server = null;
	private String _application = "Application";
	private String _eventMessageFile = "";
	private String _categoryMessageFile = "";

	private HANDLE _handle = null;
	
	@PluginFactory
	public static Win32EventLogAppender createAppender(
			@PluginAttribute("name") String name,
			@PluginAttribute("server") String server,
			@PluginAttribute("source") String source,
			@PluginAttribute("log") String log,
			@PluginElement("Layout") Layout<? extends Serializable> layout,
			@PluginElement("Filters") Filter filter) {
		return new Win32EventLogAppender(name, server, source, log, layout, filter);
	}
	public Win32EventLogAppender(String name,
			String server,
			String source,
			String log, 
			Layout<? extends Serializable> layout,
			Filter filter) {
		super(name, filter, layout);
		if (source == null || source.length() == 0) {
			source = "Log4jna";
		}

		if (log == null || log.length() == 0) {
			log = "Application";
		}

		this._server = server;
		setSource(source);
		setApplication(log);
	}

	/**
	 * The <b>Source</b> option which names the source of the event. The current
	 * value of this constant is <b>Source</b>.
	 */
	public void setSource(String source) {

		if (source == null || source.length() == 0) {
			source = "Log4jna";
		}

		_source = source.trim();
	}

	public String getSource() {
		return _source;
	}

	/**
	 * The <b>Application</b> option which names the subsection of the
	 * 'Applications and Services Log'. The default value of this constant is
	 * <b>Application</b>.
	 */
	public void setApplication(String application) {

		if (application == null || application.length() == 0) {
			application = "Application";
		}

		_application = application.trim();
	}

	public String getApplication() {
		return _application;
	}

	public void close() {
		if (_handle != null) {
			if (!Advapi32.INSTANCE.DeregisterEventSource(_handle)) {
				throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
			}
			_handle = null;
		}
	}

	/**
	 * The <b>EventMessageFile</b> option which sets file location of the Event
	 * Messages
	 */
	public void setEventMessageFile(String eventMessageFile) {
		_eventMessageFile = eventMessageFile.trim();
	}

	public String getEventMessageFile() {
		return _eventMessageFile;
	}

	/**
	 * The <b>CategoryMessageFile</b> option which sets file location of the
	 * Catagory Messages
	 */
	public void setCategoryMessageFile(String categoryMessageFile) {
		_categoryMessageFile = categoryMessageFile.trim();
	}

	public String getCategoryMessageFile() {
		return _categoryMessageFile;
	}

	private void registerEventSource() {
		close();

		try {
			System.err.println(String.format("Server: %s; source:%s; application:%s; eventMessageFile:%s;categoryFile:%s",
					_server, _source, _application,
					_eventMessageFile, _categoryMessageFile));
			_handle = registerEventSource(_server, _source, _application,
					_eventMessageFile, _categoryMessageFile);
		} catch (Exception e) {
			close();
			throw new RuntimeException("Could not register event source.", e);
		}
	}

	public void activateOptions() {
		registerEventSource();
	}
	
	@Override
	public void append(LogEvent event) {

		if (_handle == null) {
			registerEventSource();
		}

		
		String s = new String(getLayout().toByteArray(event));
		// Normalize the log message level into the supported categories
		// Anything above FATAL or below DEBUG is labeled as INFO.
		// if (nt_category > FATAL || nt_category < DEBUG) {
		// 	nt_category = INFO;
		// }
		// This is the only message supported by the package. It is backed by
		// a message resource which consists of just '%1' which is replaced
		// by the string we just created.
		final int messageID = 0x1000;
		
		String[] buffer = { s };
		
		if (Advapi32.INSTANCE.ReportEvent(_handle, getEventLogType(event.getLevel()),
				getEventLogCategory(event.getLevel()), messageID, null, buffer.length, 0, buffer,
				null) == false) {
			Exception e = new Win32Exception(Kernel32.INSTANCE.GetLastError());
			getHandler().error(
					"Failed to report event [" + s + "].", event, e);
		}
	}

	public void finalize() {
		close();
	}

	/**
	 * The <code>Win32EventLogAppender</code> requires a layout. Hence, this
	 * method always returns <code>true</code>.
	 */
	public boolean requiresLayout() {
		return true;
	}

	private static HANDLE registerEventSource(String server, String source,
			String application, String eventMessageFile,
			String categoryMessageFile) {
		String eventSourceKeyPath = "SYSTEM\\CurrentControlSet\\Services\\EventLog\\"
				+ application + "\\" + source;
		if (Advapi32Util.registryCreateKey(WinReg.HKEY_LOCAL_MACHINE,
				eventSourceKeyPath)) {
			Advapi32Util.registrySetIntValue(WinReg.HKEY_LOCAL_MACHINE,
					eventSourceKeyPath, "TypesSupported", 7);
			Advapi32Util.registrySetIntValue(WinReg.HKEY_LOCAL_MACHINE,
					eventSourceKeyPath, "CategoryCount", 6);
			Advapi32Util.registrySetStringValue(WinReg.HKEY_LOCAL_MACHINE,
					eventSourceKeyPath, "EventMessageFile", eventMessageFile);
			Advapi32Util.registrySetStringValue(WinReg.HKEY_LOCAL_MACHINE,
					eventSourceKeyPath, "CategoryMessageFile",
					categoryMessageFile);
		}

		HANDLE h = Advapi32.INSTANCE.RegisterEventSource(server, source);
		if (h == null) {
			throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
		}

		return h;
	}

	/**
	 * Convert log4j Priority to an EventLog type. The log4j package supports 8
	 * defined priorities, but the NT EventLog only knows 3 event types of
	 * interest to us: ERROR, WARNING, and INFO.
	 * 
	 * @param level
	 *            Log4j priority.
	 * @return EventLog type.
	 */
	private static int getEventLogType(Level level) {
		int type = WinNT.EVENTLOG_SUCCESS;
		
		if (level.intLevel() <= Level.INFO.intLevel()) {
			type = WinNT.EVENTLOG_INFORMATION_TYPE;
			if (level.intLevel() <= Level.WARN.intLevel()) {
				type = WinNT.EVENTLOG_WARNING_TYPE;
				if (level.intLevel() <=  Level.ERROR.intLevel()) {
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
	 * 
	 * @param level.intLevel()
	 *            Log4J priority.
	 * @return EventLog category.
	 */
	private static int getEventLogCategory(Level level) {
		int category = 1;
		if (level.intLevel() >= Level.DEBUG.intLevel()) {
			category = 2;
			if (level.intLevel() >= Level.INFO.intLevel()) {
				category = 3;
				if (level.intLevel() >= Level.WARN.intLevel()) {
					category = 4;
					if (level.intLevel() >= Level.ERROR.intLevel()) {
						category = 5;
						if (level.intLevel() >= Level.FATAL.intLevel()) {
							category = 6;
						}
					}
				}
			}
		}
		return category;
	}

}
