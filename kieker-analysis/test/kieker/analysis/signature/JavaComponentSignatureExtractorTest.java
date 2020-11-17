/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.signature;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysis.signature.JavaComponentSignatureExtractor;
import kieker.analysisteetime.model.analysismodel.type.ComponentType;
import kieker.analysisteetime.model.analysismodel.type.TypeFactory;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class JavaComponentSignatureExtractorTest {

	private JavaComponentSignatureExtractor signatureExtractor;

	public JavaComponentSignatureExtractorTest() {
		// empty default constructor
	}

	@Before
	public void setUp() throws Exception {
		this.signatureExtractor = new JavaComponentSignatureExtractor();
	}

	@After
	public void tearDown() throws Exception {
		this.signatureExtractor = null; // NOPMD (resetting to null intended)
	}

	/**
	 * Test method for
	 * {@link kieker.analysis.signature.JavaComponentSignatureExtractor#extract(kieker.analysisteetime.model.analysismodel.type.ComponentType)}.
	 */
	@Test
	public void testExtractWithNonNestedPackage() { // NOPMD (assert is placed in separate method)
		final String packageName = "package";
		final String componentName = "MyClass";

		this.testExtraction(packageName, componentName);
	}

	/**
	 * Test method for
	 * {@link kieker.analysis.signature.JavaComponentSignatureExtractor#extract(kieker.analysisteetime.model.analysismodel.type.ComponentType)}.
	 */
	@Test
	public void testExtractWithNestedPackage() { // NOPMD (assert is placed in separate method)
		final String packageName = "com.company.package";
		final String componentName = "MyClass";

		this.testExtraction(packageName, componentName);
	}

	/**
	 * Test method for
	 * {@link kieker.analysis.signature.JavaComponentSignatureExtractor#extract(kieker.analysisteetime.model.analysismodel.type.ComponentType)}.
	 */
	@Test
	public void testExtractWithoutPackage() { // NOPMD (assert is placed in separate method)
		final String packageName = "";
		final String componentName = "MyClass";

		this.testExtraction(packageName, componentName);
	}

	/**
	 * Test method for
	 * {@link kieker.analysis.signature.JavaComponentSignatureExtractor#extract(kieker.analysisteetime.model.analysismodel.type.ComponentType)}.
	 */
	@Test
	public void testExtractWithInnerClass() { // NOPMD (assert is placed in separate method)
		final String packageName = "com.company.package";
		final String componentName = "MyClass$InnerClass";

		this.testExtraction(packageName, componentName);
	}

	private void testExtraction(final String packageName, final String name) { // NOPMD (assert for other tests)
		final ComponentType componentType = this.buildComponentTypeByNames(packageName, name);

		this.signatureExtractor.extract(componentType);

		final String extractedPackage = componentType.getPackage();
		final String extractedName = componentType.getName();

		Assert.assertEquals(packageName, extractedPackage);
		Assert.assertEquals(name, extractedName);
	}

	private ComponentType buildComponentTypeBySiganture(final String signature) {
		final ComponentType componentType = TypeFactory.eINSTANCE.createComponentType();
		componentType.setSignature(signature);
		return componentType;
	}

	private ComponentType buildComponentTypeByNames(final String packageName, final String name) {
		return this.buildComponentTypeBySiganture(packageName + '.' + name);
	}

}
