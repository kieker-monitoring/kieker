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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysis.signature.JavaOperationSignatureExtractor;
import kieker.analysisteetime.model.analysismodel.type.OperationType;
import kieker.analysisteetime.model.analysismodel.type.TypeFactory;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class JavaOperationSignatureExtractorTest {

	private JavaOperationSignatureExtractor signatureExtractor;

	public JavaOperationSignatureExtractorTest() {
		// empty default constructor
	}

	@Before
	public void setUp() throws Exception {
		this.signatureExtractor = new JavaOperationSignatureExtractor();
	}

	@After
	public void tearDown() throws Exception {
		this.signatureExtractor = null; // NOPMD (resetting to null intended)
	}

	/**
	 * Test method for
	 * {@link kieker.analysis.signature.JavaOperationSignatureExtractor#extract(kieker.analysisteetime.model.analysismodel.type.OperationType)}.
	 */
	@Test
	public void testExtractWithoutModifiersAndParameters() { // NOPMD (assert is placed in separate method)
		final List<String> modifiers = Collections.emptyList();
		final String returnType = "void";
		final String name = "methodName";
		final List<String> parameterTypes = Collections.emptyList();

		this.testExtraction(modifiers, returnType, name, parameterTypes);
	}

	/**
	 * Test method for
	 * {@link kieker.analysis.signature.JavaOperationSignatureExtractor#extract(kieker.analysisteetime.model.analysismodel.type.OperationType)}.
	 */
	@Test
	public void testExtractWithoutModifiersWithParameters() { // NOPMD (assert is placed in separate method)
		final List<String> modifiers = Collections.emptyList();
		final String returnType = "void";
		final String name = "methodName";
		final List<String> parameterTypes = Arrays.asList("FirstParameterType", "SecondParameterType");

		this.testExtraction(modifiers, returnType, name, parameterTypes);
	}

	/**
	 * Test method for
	 * {@link kieker.analysis.signature.JavaOperationSignatureExtractor#extract(kieker.analysisteetime.model.analysismodel.type.OperationType)}.
	 */
	@Test
	public void testExtractWithoutModifiersWithFullQualifiedParameters() { // NOPMD (assert is placed in separate method)
		final List<String> modifiers = Collections.emptyList();
		final String returnType = "ReturnType";
		final String name = "methodName";
		final List<String> parameterTypes = Arrays.asList("org.organisation.package.FirstParameterType", "com.compancy.pack.age.SecondParameterType");

		this.testExtraction(modifiers, returnType, name, parameterTypes);
	}

	/**
	 * Test method for
	 * {@link kieker.analysis.signature.JavaOperationSignatureExtractor#extract(kieker.analysisteetime.model.analysismodel.type.OperationType)}.
	 */
	@Test
	public void testExtractWithMultipleModifiersWithoutParameters() { // NOPMD (assert is placed in separate method)
		final List<String> modifiers = Arrays.asList("public", "static", "final");
		final String returnType = "ReturnType";
		final String name = "methodName";
		final List<String> parameterTypes = Collections.emptyList();

		this.testExtraction(modifiers, returnType, name, parameterTypes);
	}

	/**
	 * Test method for
	 * {@link kieker.analysis.signature.JavaOperationSignatureExtractor#extract(kieker.analysisteetime.model.analysismodel.type.OperationType)}.
	 */
	@Test
	public void testExtractFullQualifiedReturnType() { // NOPMD (assert is placed in separate method)
		final List<String> modifiers = Arrays.asList("public");
		final String returnType = "org.organi.sation.SimpleClass";
		final String name = "methodName";
		final List<String> parameterTypes = Collections.emptyList();

		this.testExtraction(modifiers, returnType, name, parameterTypes);
	}

	/**
	 * Test method for
	 * {@link kieker.analysis.signature.JavaOperationSignatureExtractor#extract(kieker.analysisteetime.model.analysismodel.type.OperationType)}.
	 */
	@Test
	public void testExtractNestedFullQualifiedReturnType() { // NOPMD (assert is placed in separate method)
		final List<String> modifiers = Arrays.asList("public");
		final String returnType = "org.organi.sation.OuterClass$InnerClass";
		final String name = "methodName";
		final List<String> parameterTypes = Collections.emptyList();

		this.testExtraction(modifiers, returnType, name, parameterTypes);
	}

	private void testExtraction(final List<String> modifiers, final String returnType, final String name, final List<String> parameters) { // NOPMD (assert for other
																																			// tests)
		final OperationType operationType = this.buildComponentTypeByNames(modifiers, returnType, name, parameters);

		this.signatureExtractor.extract(operationType);

		final List<String> extractedModifiers = operationType.getModifiers();
		final String extractedReturnType = operationType.getReturnType();
		final String extractedName = operationType.getName();
		final List<String> extractedParameters = operationType.getParameterTypes();

		Assert.assertEquals(modifiers, extractedModifiers);
		Assert.assertEquals(returnType, extractedReturnType);
		Assert.assertEquals(name, extractedName);
		Assert.assertEquals(parameters, extractedParameters);
	}

	private OperationType buildOperationTypeBySiganture(final String signature) {
		final OperationType operationType = TypeFactory.eINSTANCE.createOperationType();
		operationType.setSignature(signature);
		return operationType;
	}

	private OperationType buildComponentTypeByNames(final List<String> modifiers, final String returnType, final String name, final List<String> parameters) {
		final StringBuilder signature = new StringBuilder();
		signature.append(modifiers.stream().collect(Collectors.joining(" ")))
				.append(' ')
				.append(returnType)
				.append(' ')
				.append(name)
				.append('(')
				.append(parameters.stream().collect(Collectors.joining(", ")))
				.append(')');
		return this.buildOperationTypeBySiganture(signature.toString());
	}

}
