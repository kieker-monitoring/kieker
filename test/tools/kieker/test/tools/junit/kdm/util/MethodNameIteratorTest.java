package kieker.test.tools.junit.kdm.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeModel;
import org.eclipse.gmt.modisco.omg.kdm.code.InterfaceUnit;
import org.junit.Assert;
import org.junit.Test;

import kieker.tools.kdm.manager.KDMModelManager;
import kieker.tools.kdm.manager.util.MethodNameIterator;
import kieker.tools.kdm.manager.util.descriptions.MethodDescription;

/**
 * 
 * @author Benjamin Harms
 * 
 */
public class MethodNameIteratorTest {
	private final CodeModel codeModel = TestPackageStructure.getCodeModel();
	private final Map<String, ClassUnit> classes = TestPackageStructure.getClasses();
	private final Map<String, InterfaceUnit> interfaces = TestPackageStructure.getInterfaces();

	/**
	 * Default constructor.
	 */
	public MethodNameIteratorTest() {
		// No code necessary.
	}

	@Test
	public void testLoad() {
		// Just to load the data and correct the runtime for the next method.
	}

	@Test
	public void testNoMethods() {
		final String key = "test.zwei.sieben.Foo.Bar";
		final ClassUnit clazz = this.classes.get(key);
		final Iterator<MethodDescription> it = new MethodNameIterator(clazz, key);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(this.print(it.next()));
		}

		Assert.assertArrayEquals(new Object[] {}, l.toArray());
	}

	@Test
	public void testNormalMethod() {
		final String key = "test.zwei.fuenf.Main";
		final ClassUnit clazz = this.classes.get(key);
		final Iterator<MethodDescription> it = new MethodNameIterator(clazz, key);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(this.print(it.next()));
		}

		Assert.assertArrayEquals(new Object[] { "public void main()" }, l.toArray());
	}

	@Test
	public void testMultiMethod() {
		final String key = "org.emf.kdm.Demo";
		final ClassUnit clazz = this.classes.get(key);
		final Iterator<MethodDescription> it = new MethodNameIterator(clazz, key);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(this.print(it.next()));
		}

		Assert.assertArrayEquals(new Object[] { "public void showA()", "public void showB()", "public int showC()", "public void set()" },
				l.toArray());
	}

	@Test
	public void testNestedClassesMethod() {
		final String key = "org.emf.abc.Input";
		final ClassUnit clazz = this.classes.get(key);
		final Iterator<MethodDescription> it = new MethodNameIterator(clazz, key);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(this.print(it.next()));
		}

		Assert.assertArrayEquals(new Object[] {}, l.toArray());
	}

	@Test
	public void testInterfaceMethod() {
		final String key = "test.zwei.sechs.IIterator";
		final InterfaceUnit clazz = this.interfaces.get(key);
		final Iterator<MethodDescription> it = new MethodNameIterator(clazz, key);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(this.print(it.next()));
		}

		Assert.assertArrayEquals(new Object[] { "public int iterate()" }, l.toArray());
	}

	@Test
	public void testGlobalMethod() {
		final Iterator<MethodDescription> it = new MethodNameIterator(this.codeModel.getCodeElement());
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(this.print(it.next()));
		}

		Assert.assertArrayEquals(new Object[] { "public void globalMethod()", "protected void globalMethodNext()" }, l.toArray());
	}

	@Test
	public void testConstructor() {
		final String key = "org.emf.kdm.Manager";
		final ClassUnit classUnit = this.classes.get(key);
		final Iterator<MethodDescription> it = new MethodNameIterator(classUnit, key);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(this.print(it.next()));
		}

		Assert.assertArrayEquals(new Object[] { "public Manager()" }, l.toArray());
	}

	@Test
	public void testWithJPetStore() {
		final KDMModelManager modelManager = new KDMModelManager("..\\testdata\\outJPetStore.xmi");
		try {
			// Get some methods
			Iterator<MethodDescription> methodIterator = modelManager.iterateMethodsFromInterface("org.mybatis.jpetstore.persistence.AccountMapper");
			Assert.assertTrue(methodIterator.hasNext());
			Assert.assertEquals("public org.mybatis.jpetstore.domain.SubAccount getSubAccountByUsername", this.print(methodIterator.next()));
			Assert.assertTrue(methodIterator.hasNext());
			Assert.assertEquals("public org.mybatis.jpetstore.domain.Profile getProfile", this.print(methodIterator.next()));
			Assert.assertTrue(methodIterator.hasNext());
			Assert.assertEquals("public org.mybatis.jpetstore.domain.Banner getBannerData", this.print(methodIterator.next()));
			Assert.assertTrue(methodIterator.hasNext());
			Assert.assertEquals("public org.mybatis.jpetstore.domain.Signon getSignon", this.print(methodIterator.next()));
			Assert.assertTrue(methodIterator.hasNext());
			Assert.assertEquals("public org.mybatis.jpetstore.domain.Account getAccountByUsername", this.print(methodIterator.next()));
			Assert.assertTrue(methodIterator.hasNext());
			Assert.assertEquals("public org.mybatis.jpetstore.domain.Account getAccountByUsernameAndPassword", this.print(methodIterator.next()));
			// ...
			// Get a constructor
			methodIterator = modelManager.iterateMethodsFromClass("org.mybatis.jpetstore.service.AccountService");
			Assert.assertTrue(methodIterator.hasNext());
			final MethodDescription description = methodIterator.next();
			Assert.assertTrue(description.isConstructor());
			Assert.assertEquals("protected AccountService", this.print(description));
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testMultiHasNexWithoutNext() {
		final String key = "org.emf.kdm.Demo";
		final ClassUnit clazz = this.classes.get(key);
		final Iterator<MethodDescription> it = new MethodNameIterator(clazz, key);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			// A simple call of it.hasNext() may be useful sometimes, so test it
			it.hasNext();
			l.add(this.print(it.next()));
		}

		Assert.assertArrayEquals(new Object[] { "public void showA()", "public void showB()", "public int showC()", "public void set()" },
				l.toArray());
	}

	@Test
	public void testMethodsFromCSharpClass() {
		final KDMModelManager modelManager = new KDMModelManager("..\\testdata\\SharpDevelop.xmi");
		try {
			// Get some methods
			final String key = "ICSharpCode.FormsDesigner.Services.ImageResourceEditor";
			final Iterator<MethodDescription> it = modelManager.iterateMethodsFromClass(key);
			final List<String> l = new LinkedList<String>();
			while (it.hasNext()) {
				final String p = this.print(it.next());
				l.add(p);
			}

			final String[] values = new String[] { "public System.Drawing.Desig.UITypeEditorEditStyle GetEditStyle", "public object EditValue",
				"public bool GetPaintValueSupported", "public void PaintValue" };
			Assert.assertArrayEquals(values, l.toArray());
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testMethodsFromCSharpInterface() {
		// More then one million lines of code and no InterfaceUnit containing some attributes or methods...so test another class...
		final KDMModelManager modelManager = new KDMModelManager("..\\testdata\\SharpDevelop.xmi");
		try {
			final String key = "ICSharpCode.XamlBinding.PowerToys.Dialogs.DragDropMarkerAdorner";
			// Get some methods
			final Iterator<MethodDescription> it = modelManager.iterateMethodsFromClass(key);
			final List<String> l = new LinkedList<String>();
			while (it.hasNext()) {
				final String p = this.print(it.next());
				l.add(p);
			}

			final String[] values = new String[] { "protected void OnRender", "protected System.Window.Size MeasureOverride",
				"protected System.Window.Size ArrangeOverride", "public ICSharpCode.XamlBinding.PowerToys.Dialog.DragDropMarkerAdorner CreateAdornerContentMove",
				"public ICSharpCode.XamlBinding.PowerToys.Dialog.DragDropMarkerAdorner CreateAdornerCellMove" };
			Assert.assertArrayEquals(values, l.toArray());
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private String print(final MethodDescription description) {
		final StringBuilder line = new StringBuilder();
		line.append(description.getVisibilityModifier()).append(' ');
		if (!description.isConstructor()) {
			line.append(description.getReturnType()).append(' ');
		}
		line.append(description.getName());

		return line.toString();
	}
}
