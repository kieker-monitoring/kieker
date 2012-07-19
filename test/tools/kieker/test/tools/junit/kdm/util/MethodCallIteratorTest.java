package kieker.test.tools.junit.kdm.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.gmt.modisco.omg.kdm.code.MethodUnit;
import org.junit.Assert;
import org.junit.Test;

import kieker.tools.kdm.manager.util.MethodCallIterator;

public class MethodCallIteratorTest {
	private final Map<String, MethodUnit> methodCalls = TestPackageStructure.getMethods();

	/**
	 * Default constructor.
	 */
	public MethodCallIteratorTest() {
		// No code necessary
	}

	@Test
	public void testLoad() {
		// Just to load the data and correct the runtime for the next method.
	}

	@Test
	public void testNoMethodCalls() {
		final String key = "org.emf.kdm.Demo.showA()";
		final MethodUnit methodUnit = this.methodCalls.get(key);
		final Iterator<String> it = new MethodCallIterator(methodUnit);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(it.next());
		}

		Assert.assertArrayEquals(l.toArray(), new String[] {});
	}

	@Test
	public void testSingleMethodCall() {
		final String key = "org.emf.kdm.Demo.showB()";
		final MethodUnit methodUnit = this.methodCalls.get(key);
		final Iterator<String> it = new MethodCallIterator(methodUnit);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(it.next());
		}

		Assert.assertArrayEquals(l.toArray(), new String[] { "org.emf.kdm.Demo.showB() org.emf.abc.Input.Stream.open()" });
	}

	@Test
	public void testMultipleMethodCalls() {
		final String key = "test.zwei.fuenf.Main.main()";
		final MethodUnit methodUnit = this.methodCalls.get(key);
		final Iterator<String> it = new MethodCallIterator(methodUnit);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(it.next());
		}

		Assert.assertArrayEquals(l.toArray(), new String[] { "test.zwei.fuenf.Main.main() org.emf.kdm.Demo.showA()",
			"test.zwei.fuenf.Main.main() org.emf.abc.Input.Stream.open()", "test.zwei.fuenf.Main.main() globalMethod()" });
	}

	@Test
	public void testMultiHasNextWithoutNext() {
		final String key = "test.zwei.fuenf.Main.main()";
		final MethodUnit methodUnit = this.methodCalls.get(key);
		final Iterator<String> it = new MethodCallIterator(methodUnit);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			// A simple call of it.hasNext() may be useful sometimes, so test it
			it.hasNext();
			l.add(it.next());
		}

		Assert.assertArrayEquals(l.toArray(), new String[] { "test.zwei.fuenf.Main.main() org.emf.kdm.Demo.showA()",
			"test.zwei.fuenf.Main.main() org.emf.abc.Input.Stream.open()", "test.zwei.fuenf.Main.main() globalMethod()" });
	}
}
