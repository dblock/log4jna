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
package org.apache.log4jna.nt.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A new more extended demo application based on Demo.java by Ceri Storey.
 * 
 * @author <a href="claudiow.trajtenberg@cgtca.ca">Claudio Trajtenberg</a>
 *
 */
public class NewDemo {

	private static Logger LOGGER = LogManager.getLogger();

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
		LOGGER.trace("Enter trace");
		System.out.println("Trace");
		LOGGER.trace("Exit trace");
	}

	/**
	 * 
	 */
	public void debug() {
		LOGGER.debug("Enter debug");
		System.out.println("Debug");
		LOGGER.debug("Exit debug");
	}

	/**
	 * 
	 */
	public void info() {
		LOGGER.info("Enter info");
		System.out.println("Info");
		LOGGER.info("Exit info");
	}

	/**
	 * 
	 */
	public void warn() {
		LOGGER.warn("Enter warn");
		System.out.println("Warn");
		LOGGER.warn("Exit warn");
	}

	/**
	 * 
	 */
	public void error() {
		LOGGER.error("Enter error");
		System.out.println("Error");
		LOGGER.error("Exit error");
	}

	/**
	 * 
	 */
	public void fatal() {
		LOGGER.fatal("Enter fatal");
		System.out.println("Fatal");
		LOGGER.fatal("Exit fatal");
	}

	/**
	 * 
	 */
	public void warnWithException() {
		try {
			throwException();
		} catch (Exception e) {
			LOGGER.warn("In warn with exception", e);
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
			LOGGER.error("In error with exception", e);
		}
	}

	/**
	 * 
	 */
	public void fatalWithException() {
		try {
			throwException();
		} catch (Throwable e) {
			LOGGER.fatal("In fatal with exception", e);
		}
	}
}
