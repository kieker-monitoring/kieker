/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package kieker.tools.bridge.cli;

import java.net.URL;
import java.net.URLClassLoader;
import java.security.PrivilegedAction;

/**
 * @author Reiner Jung, Nils Christian Ehmke
 *
 * @since 1.8
 *
 * @deprecated since 1.15 removed in 1.16 replaced by collector
 */
@Deprecated
public class PrivilegedClassLoaderAction implements PrivilegedAction<URLClassLoader> {

	/**
	 * The list of libraries used to create the class loader.
	 */
	private final URL[] urls;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param urls
	 *            The list of libraries used to create the class loader.
	 */
	public PrivilegedClassLoaderAction(final URL[] urls) { // NOPMD
		this.urls = urls;
	}

	/**
	 * Runs the action.
	 *
	 * @return The class loader.
	 */
	@Override
	public URLClassLoader run() {
		return new URLClassLoader(this.urls, CLIServerMain.class.getClassLoader());
	}

}
