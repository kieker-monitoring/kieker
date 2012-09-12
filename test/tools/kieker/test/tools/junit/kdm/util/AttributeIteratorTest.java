/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.tools.junit.kdm.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.gmt.modisco.omg.kdm.code.InterfaceUnit;
import org.junit.Assert;
import org.junit.Test;

import kieker.tools.kdm.manager.KDMModelManager;
import kieker.tools.kdm.manager.exception.InvalidClassException;
import kieker.tools.kdm.manager.util.AttributeIterator;
import kieker.tools.kdm.manager.util.descriptions.AttributeDescription;

/**
 * @author Nils Christian Ehmke, Benjamin Harms
 */
public class AttributeIteratorTest {

	public AttributeIteratorTest() {
		// Not necessary
	}

	@Test
	public void testAttributesFromClass() {
		final String path = "../examples/JavaEEServletContainerExample/JPetStore-KDM.xmi";
		final KDMModelManager modelManager = new KDMModelManager(path);

		try {
			final String className = "org.mybatis.jpetstore.domain.Signon";
			final Iterator<AttributeDescription> it = modelManager.iterateAttributesFromClass(className);
			final List<String> l = new LinkedList<String>();
			while (it.hasNext()) {
				final String desc = this.print(it.next());
				l.add(desc);
			}

			final String[] values = new String[] { "private static long serialVersionUID = 5054687760345121163L", "CLASS private java.lang.String username", };

			Assert.assertArrayEquals(values, l.toArray());
		} catch (final InvalidClassException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testAttributesFromInterface() {
		final String key = "test.zwei.zehn.ISetable";
		final InterfaceUnit interfaze = TestPackageStructure.getInterfaces().get(key);
		final Iterator<AttributeDescription> it = new AttributeIterator(interfaze);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			final String desc = this.print(it.next());
			l.add(desc);
		}

		final String[] values = new String[] { "private int length", "protected char[] letter", "CLASS public static test.zwei.sieben.Foo dings", };

		Assert.assertArrayEquals(values, l.toArray());
	}

	@Test
	public void testAttributeFromCSharpClassOnlyStorableUnits() {
		final String path = "tmp/SharpDevelop.xmi"; // FIXME: correct file?
		final KDMModelManager modelManager = new KDMModelManager(path);
		final String key = "ICSharpCode.CodeAnalysis.SuppressMessageCommand";
		try {
			final Iterator<AttributeDescription> it = modelManager.iterateAttributesFromClass(key);
			final List<String> l = new LinkedList<String>();

			while (it.hasNext()) {
				final String p = this.print(it.next());
				l.add(p);
			}

			final String[] values = new String[] { "unknown string NamespaceName", "unknown string AttributeName", };

			Assert.assertArrayEquals(values, l.toArray());
		} catch (final InvalidClassException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testAttributeFromCSharpClassOnlyMemberUnits() {
		final String path = "tmp/SharpDevelop.xmi"; // FIXME: correct file?
		final KDMModelManager modelManager = new KDMModelManager(path);
		final String key = "Mono.Cecil.EventReference";
		try {
			final Iterator<AttributeDescription> it = modelManager.iterateAttributesFromClass(key);
			final List<String> l = new LinkedList<String>();

			while (it.hasNext()) {
				final String p = this.print(it.next());
				l.add(p);
			}

			final String[] values = new String[] {
				"CLASS public Mono.Cecil.TypeReference event_type",
				"CLASS public Mono.Cecil.TypeReference EventType",
				"public string FullName",
			};

			Assert.assertArrayEquals(values, l.toArray());
		} catch (final InvalidClassException e) {
			e.printStackTrace();
		}
	}

	private String print(final AttributeDescription description) {
		final StringBuilder result = new StringBuilder();

		if (description != null) {
			if (!description.isPrimitiveType()) {
				result.append(description.getElementType()).append(' ');
			}
			result.append(description.getVisibilityModifier()).append(' ');
			if (description.isStatic()) {
				result.append("static ");
			}
			result.append(description.getTypeName());
			if (description.isArrayType()) {
				result.append("[] ");
			} else {
				result.append(' ');
			}
			result.append(description.getName());
			if (description.hasDefaultValue()) {
				result.append(" = ").append(description.getDefaultValue());
			}
		}

		return result.toString();
	}
}
