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
package org.dblock.log4jna.nt.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A new more extended demo application based on Demo.java by Ceri Storey.
 * 
 * <p>
 * <b>Note:</b> To run for the first time you must be Administrator in Windows.
 * This application needs to write the registry when run for the first time
 * </p>
 * 
 * @author <a href="claudiow.trajtenberg@cgtca.ca">Claudio Trajtenberg</a>
 * @author <a href="mailto:db@dblock.org">Daniel Doubrovkine</a>
 * @author Ceri Storey
 */
public class NewDemo {

	private static Logger LOGGER = LogManager.getLogger();

	private static final String MSG = "NewDemo: %s";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NewDemo d = new NewDemo();

		d.trace();
		d.debug();
		d.info();
		d.warn();
		d.error();
		d.fatal();
		d.warnWithException();
		d.errorWithException();
		d.fatalWithException();
	}

	/**
	 * 
	 */
	public void trace() {
		LOGGER.trace(String.format(MSG, "Enter trace"));
		LOGGER.trace(String.format(MSG, "Exit trace"));
	}

	/**
	 * 
	 */
	public void debug() {
		LOGGER.debug(String.format(MSG, "Enter debug"));
		LOGGER.debug(String.format(MSG, "Exit debug"));
	}

	/**
	 * 
	 */
	public void info() {
		LOGGER.info(String.format(MSG, "Enter info"));
		LOGGER.info(String.format(MSG, "Exit info"));
	}

	/**
	 * 
	 */
	public void warn() {
		LOGGER.warn(String.format(MSG, "Enter warn"));
		LOGGER.warn(String.format(MSG, "Exit warn"));
	}

	/**
	 * 
	 */
	public void error() {
		LOGGER.error(String.format(MSG, "Enter error"));
		LOGGER.error(String.format(MSG, "Exit error"));
	}

	/**
	 * 
	 */
	public void fatal() {
		LOGGER.fatal(String.format(MSG, "Enter fatal"));
		LOGGER.fatal(String.format(MSG, "Exit fatal"));
	}

	/**
	 * 
	 */
	public void warnWithException() {
		try {
			throwException();
		} catch (Exception e) {
			LOGGER.warn(String.format(MSG, "In warn with exception"), e);
		}
	}

	/**
	 * @throws Exception
	 */
	private void throwException() throws Exception {
		throw new Exception("Pouporselly thrown for demo");

	}

	/**
	 * 
	 */
	public void errorWithException() {
		try {
			throwException();
		} catch (Throwable e) {
			LOGGER.error(String.format(MSG, "In error with exception"), e);
		}
	}

	/**
	 * 
	 */
	public void fatalWithException() {
		try {
			throwException();
		} catch (Throwable e) {
			LOGGER.fatal(String.format(MSG, "In fatal with exception"), e);
		}
	}
}
