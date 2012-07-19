package kieker.test.tools.junit.kdm.util;

import java.io.InvalidClassException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.gmt.modisco.omg.kdm.code.InterfaceUnit;
import org.junit.Assert;
import org.junit.Test;

import kieker.tools.kdm.manager.KDMModelManager;
import kieker.tools.kdm.manager.util.AttributeIterator;
import kieker.tools.kdm.manager.util.descriptions.AttributeDescription;

public class AttributeIteratorTest {
	Map<String, InterfaceUnit> interfaces = TestPackageStructure.getInterfaces();

	public AttributeIteratorTest() {
		// Not necessary
	}

	@Test
	public void testAttributesFromClass() {
		final String path = "..\\testdata\\outJPetStore.xmi";
		final KDMModelManager modelManager = new KDMModelManager(path);

		try {
			final String className = "org.mybatis.jpetstore.domain.Signon";
			final Iterator<AttributeDescription> it = modelManager.iterateAttributesFromClass(className);
			final List<String> l = new LinkedList<String>();
			while (it.hasNext()) {
				final String desc = this.print(it.next());
				l.add(desc);
			}

			final String[] values = new String[] { "private static long serialVersionUID = 5054687760345121163L", "CLASS private java.lang.String username" };

			Assert.assertArrayEquals(values, l.toArray());
		} catch (final InvalidClassException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testAttributesFromInterface() {
		final String key = "test.zwei.zehn.ISetable";
		final InterfaceUnit interfaze = this.interfaces.get(key);
		final Iterator<AttributeDescription> it = new AttributeIterator(interfaze);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			final String desc = this.print(it.next());
			l.add(desc);
		}

		final String[] values = new String[] { "private int length", "protected char[] letter", "CLASS public static test.zwei.sieben.Foo dings" };

		Assert.assertArrayEquals(values, l.toArray());
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
