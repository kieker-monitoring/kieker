/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.tools.junit.writeRead.jms;

import java.util.Hashtable;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/**
 * This class is part of a very basic fake JMS message broker. It uses a very simple design to deliver messages synchronously from a singleton producer to a
 * singleton consumer. It has only been designed for test purposes ({@link BasicJMSWriterReaderTest}) and should <b>not</b> be used outside this test.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.8
 */
public class FakeContext implements Context {

	/**
	 * Default constructor.
	 */
	public FakeContext() {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	public Object addToEnvironment(final String propName, final Object propVal) throws NamingException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void bind(final Name name, final Object obj) throws NamingException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	public void bind(final String name, final Object obj) throws NamingException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	public void close() throws NamingException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	public Name composeName(final Name name, final Name prefix) throws NamingException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public String composeName(final String name, final String prefix) throws NamingException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Context createSubcontext(final Name name) throws NamingException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Context createSubcontext(final String name) throws NamingException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void destroySubcontext(final Name name) throws NamingException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	public void destroySubcontext(final String name) throws NamingException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	public Hashtable<?, ?> getEnvironment() throws NamingException { // NOPMD (Hashtable)
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getNameInNamespace() throws NamingException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public NameParser getNameParser(final Name name) throws NamingException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public NameParser getNameParser(final String name) throws NamingException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public NamingEnumeration<NameClassPair> list(final Name name) throws NamingException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public NamingEnumeration<NameClassPair> list(final String name) throws NamingException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public NamingEnumeration<Binding> listBindings(final Name name) throws NamingException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public NamingEnumeration<Binding> listBindings(final String name) throws NamingException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object lookup(final Name name) throws NamingException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object lookup(final String name) throws NamingException {
		if ("ConnectionFactory".equals(name)) {
			return new FakeConnectionFactory();
		} else {
			// lookup of the destination
			return new FakeDestination();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Object lookupLink(final Name name) throws NamingException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object lookupLink(final String name) throws NamingException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void rebind(final Name name, final Object obj) throws NamingException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	public void rebind(final String name, final Object obj) throws NamingException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	public Object removeFromEnvironment(final String propName) throws NamingException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void rename(final Name oldName, final Name newName) throws NamingException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	public void rename(final String oldName, final String newName) throws NamingException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	public void unbind(final Name name) throws NamingException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	public void unbind(final String name) throws NamingException {
		// No code necessary
	}

}
