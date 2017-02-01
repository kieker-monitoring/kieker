/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.common.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;
import java.util.Properties;

/**
 * @author Christian Wulf
 *
 * @since 1.13
 */
public class ReadOnlyConfiguration extends Configuration {

	private static final long serialVersionUID = 3692243455682718596L;

	public ReadOnlyConfiguration(final Properties properties) {
		super(properties);
	}

	@Override
	public synchronized Object setProperty(final String key, final String value) {
		throw new UnsupportedOperationException("This is a read-only configuration. Changes are not permitted.");
	}

	@Override
	public void setDefaultConfiguration(final Configuration defaultConfiguration) {
		throw new UnsupportedOperationException("This is a read-only configuration. Changes are not permitted.");
	}

	@Override
	public void setStringArrayProperty(final String key, final String[] value) {
		throw new UnsupportedOperationException("This is a read-only configuration. Changes are not permitted.");
	}

	@Override
	public synchronized void clear() {
		throw new UnsupportedOperationException("This is a read-only configuration. Changes are not permitted.");
	}

	@Override
	public synchronized void load(final InputStream inStream) throws IOException {
		throw new UnsupportedOperationException("This is a read-only configuration. Changes are not permitted.");
	}

	@Override
	public synchronized void load(final Reader reader) throws IOException {
		throw new UnsupportedOperationException("This is a read-only configuration. Changes are not permitted.");
	}

	@Override
	public synchronized void loadFromXML(final InputStream in) throws IOException {
		throw new UnsupportedOperationException("This is a read-only configuration. Changes are not permitted.");
	}

	@Override
	public synchronized void putAll(final Map<? extends Object, ? extends Object> t) {
		throw new UnsupportedOperationException("This is a read-only configuration. Changes are not permitted.");
	}

	@Override
	public synchronized Object remove(final Object key) {
		throw new UnsupportedOperationException("This is a read-only configuration. Changes are not permitted.");
	}

}
