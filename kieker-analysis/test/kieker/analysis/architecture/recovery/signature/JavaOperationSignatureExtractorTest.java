/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.architecture.recovery.signature;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.model.analysismodel.type.OperationType;
import kieker.model.analysismodel.type.TypeFactory;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class JavaOperationSignatureExtractorTest {

	private static final String RETURN_TYPE = "ReturnType";
	private static final String METHOD_NAME = "methodName";
	private static final String RETURN_TYPE_VOID = "void";
	private static final String RETURN_TYPE_FULLY_QUALIFIED = "org.organi.sation.SimpleClass";
	private static final String PUBLIC = "public";
	private static final String STATIC = "static";
	private static final String FINAL = "final";

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
	 * {@link kieker.analysis.architecture.recovery.signature.JavaOperationSignatureExtractor#extract(kieker.model.analysismodel.type.OperationType)}.
	 */
	@Test
	public void testExtractWithoutModifiersAndParameters() { // NOPMD (assert is placed in separate method)
		final List<String> modifiers = Collections.emptyList();

		final List<String> parameterTypes = Collections.emptyList();

		this.testExtraction(modifiers, JavaOperationSignatureExtractorTest.RETURN_TYPE_VOID, JavaOperationSignatureExtractorTest.METHOD_NAME, parameterTypes);
	}

	/**
	 * Test method for
	 * {@link kieker.analysis.architecture.recovery.signature.JavaOperationSignatureExtractor#extract(kieker.model.analysismodel.type.OperationType)}.
	 */
	@Test
	public void testExtractWithoutModifiersWithParameRETURN_TYPEters() { // NOPMD (assert is placed in separate method)
		final List<String> modifiers = Collections.emptyList();
		final List<String> parameterTypes = Arrays.asList("FirstParameterType", "SecondParameterType");

		this.testExtraction(modifiers, JavaOperationSignatureExtractorTest.RETURN_TYPE_VOID, JavaOperationSignatureExtractorTest.METHOD_NAME, parameterTypes);
	}

	/**
	 * Test method for
	 * {@link kieker.analysis.architecture.recovery.signature.JavaOperationSignatureExtractor#extract(kieker.model.analysismodel.type.OperationType)}.
	 */
	@Test
	public void testExtractWithoutModifiersWithFullQualifiedParameters() { // NOPMD (assert is placed in separate method)
		final List<String> modifiers = Collections.emptyList();
		final List<String> parameterTypes = Arrays.asList("org.organisation.package.FirstParameterType", "com.compancy.pack.age.SecondParameterType");

		this.testExtraction(modifiers, JavaOperationSignatureExtractorTest.RETURN_TYPE, JavaOperationSignatureExtractorTest.METHOD_NAME, parameterTypes);
	}

	/**
	 * Test method for
	 * {@link kieker.analysis.architecture.recovery.signature.JavaOperationSignatureExtractor#extract(kieker.model.analysismodel.type.OperationType)}.
	 */
	@Test
	public void testExtractWithMultipleModifiersWithoutParameters() { // NOPMD (assert is placed in separate method)
		final List<String> modifiers = Arrays.asList(JavaOperationSignatureExtractorTest.PUBLIC, JavaOperationSignatureExtractorTest.STATIC,
				JavaOperationSignatureExtractorTest.FINAL);
		final List<String> parameterTypes = Collections.emptyList();

		this.testExtraction(modifiers, JavaOperationSignatureExtractorTest.RETURN_TYPE, JavaOperationSignatureExtractorTest.METHOD_NAME, parameterTypes);
	}

	/**
	 * Test method for
	 * {@link kieker.analysis.architecture.recovery.signature.JavaOperationSignatureExtractor#extract(kieker.model.analysismodel.type.OperationType)}.
	 */
	@Test
	public void testExtractFullQualifiedReturnType() { // NOPMD (assert is placed in separate method)
		final List<String> modifiers = Arrays.asList(JavaOperationSignatureExtractorTest.PUBLIC);
		final List<String> parameterTypes = Collections.emptyList();

		this.testExtraction(modifiers, JavaOperationSignatureExtractorTest.RETURN_TYPE_FULLY_QUALIFIED, JavaOperationSignatureExtractorTest.METHOD_NAME,
				parameterTypes);
	}

	/**
	 * Test method for
	 * {@link kieker.analysis.architecture.recovery.signature.JavaOperationSignatureExtractor#extract(kieker.model.analysismodel.type.OperationType)}.
	 */
	@Test
	public void testExtractNestedFullQualifiedReturnType() { // NOPMD (assert is placed in separate method)
		final List<String> modifiers = Arrays.asList(JavaOperationSignatureExtractorTest.PUBLIC);
		final String returnType = "org.organi.sation.OuterClass$InnerClass";
		final List<String> parameterTypes = Collections.emptyList();

		this.testExtraction(modifiers, returnType, JavaOperationSignatureExtractorTest.METHOD_NAME, parameterTypes);
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
