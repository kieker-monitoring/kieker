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

package kieker.test.common.junit.util;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.util.signature.ClassOperationSignaturePair;
import kieker.common.util.signature.Signature;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * 
 * @author Andre van Hoorn
 * 
 * @since 1.5
 */
public class TestClassOperationSignaturePair extends AbstractKiekerTest {

	/**
	 * Default constructor.
	 */
	public TestClassOperationSignaturePair() {
		// empty default constructor
	}

	/**
	 * A test for the class when using no modifiers.
	 */
	@Test
	public void testNoModifiers() {
		final String fqClassName = "a.b.c.D";
		final String opName = "op1";
		final String[] modifiers = {};
		final String returnType = Boolean.class.getName();
		final String[] paramTypes = { Boolean.class.getName(), Integer.class.getName() };
		final Signature inputSignature = new Signature(opName, modifiers, returnType, paramTypes);

		// Obtain operation signature string based on class name and signature and compare with final expected string.
		final String opSignatureString = ClassOperationSignaturePair.createOperationSignatureString(fqClassName, inputSignature);
		final String expectedOpSignatureString = returnType + " " + fqClassName + "." + opName + "(" + paramTypes[0] + ", " + paramTypes[1] + ")";
		Assert.assertEquals("Unexpected result", expectedOpSignatureString, opSignatureString);

		// Now split expectedOpSignatureString and compare class name and signature
		final ClassOperationSignaturePair compSigPair = ClassOperationSignaturePair.splitOperationSignatureStr(expectedOpSignatureString);
		Assert.assertEquals("FQ classnames not equal", fqClassName, compSigPair.getFqClassname());
		Assert.assertEquals("Signatures not equal", inputSignature, compSigPair.getSignature());
	}

	/**
	 * A test for the class when using modifiers but no packages.
	 */
	@Test
	public void testNoPackage() {
		final String fqClassName = "D";
		final String opName = "op1";
		final String[] modifiers = { "public", "static" };
		final String returnType = Boolean.class.getName();
		final String[] paramTypes = { Boolean.class.getName(), Integer.class.getName() };
		final Signature inputSignature = new Signature(opName, modifiers, returnType, paramTypes);

		// Obtain operation signature string based on class name and signature and compare with expected string.
		final String opSignatureString = ClassOperationSignaturePair.createOperationSignatureString(fqClassName, inputSignature);
		final String expectedOpSignatureString =
				modifiers[0] + " " + modifiers[1] + " " + returnType + " " + fqClassName + "." + opName + "(" + paramTypes[0] + ", " + paramTypes[1] + ")";
		Assert.assertEquals("Unexpected result", expectedOpSignatureString, opSignatureString);

		// Now split expectedOpSignatureString and compare class name and signature
		final ClassOperationSignaturePair compSigPair = ClassOperationSignaturePair.splitOperationSignatureStr(expectedOpSignatureString);
		Assert.assertEquals("FQ classnames not equal", fqClassName, compSigPair.getFqClassname());
		Assert.assertEquals("Signatures not equal", inputSignature, compSigPair.getSignature());
	}

	/**
	 * A test for the class when using no return type.
	 */
	@Test
	public void testNoModifiersNoReturnType() {
		final String fqClassName = "a.b.c.D";
		final String opName = "op1";
		final String[] modifiers = {};
		final String[] paramTypes = { Boolean.class.getName(), Integer.class.getName() };
		final Signature inputSignature = new Signature(opName, modifiers, null, paramTypes);

		// Obtain operation signature string based on class name and signature and compare with expected string.
		final String opSignatureString = ClassOperationSignaturePair.createOperationSignatureString(fqClassName, inputSignature);
		final String expectedOpSignatureString = fqClassName + "." + opName + "(" + paramTypes[0] + ", " + paramTypes[1] + ")";
		Assert.assertEquals("Unexpected result", expectedOpSignatureString, opSignatureString);

		// Now split expectedOpSignatureString and compare class name and signature
		final ClassOperationSignaturePair compSigPair = ClassOperationSignaturePair.splitOperationSignatureStr(expectedOpSignatureString);
		Assert.assertEquals("FQ classnames not equal", fqClassName, compSigPair.getFqClassname());
		Assert.assertEquals("Signatures not equal", inputSignature, compSigPair.getSignature());
	}

	/**
	 * A test for the class when using no return type.
	 */
	@Test
	public void testConstructorNoModifier() {
		final String fqClassName = "a.b.c.D";
		final String opName = "<init>";
		final String[] modifiers = {};
		final String[] paramTypes = { Boolean.class.getName(), Integer.class.getName() };
		final Signature inputSignature = new Signature(opName, modifiers, null, paramTypes);

		// Obtain operation signature string based on class name and signature and compare with expected string.
		final String opSignatureString = ClassOperationSignaturePair.createOperationSignatureString(fqClassName, inputSignature);
		final String expectedOpSignatureString = fqClassName + "." + opName + "(" + paramTypes[0] + ", " + paramTypes[1] + ")";
		Assert.assertEquals("Unexpected result", expectedOpSignatureString, opSignatureString);

		// Now split expectedOpSignatureString and compare class name and signature
		final ClassOperationSignaturePair compSigPair = ClassOperationSignaturePair.splitOperationSignatureStr(expectedOpSignatureString, true);
		Assert.assertEquals("FQ classnames not equal", fqClassName, compSigPair.getFqClassname());
		Assert.assertEquals("Signatures not equal", inputSignature, compSigPair.getSignature());
	}

	/**
	 * A test for the class when using no return type.
	 */
	@Test
	public void testConstructorPublicModifier() {
		final String fqClassName = "a.b.c.D";
		final String opName = "<init>";
		final String[] modifiers = { "public" };
		final String[] paramTypes = { Boolean.class.getName(), Integer.class.getName() };
		final Signature inputSignature = new Signature(opName, modifiers, null, paramTypes);

		// Obtain operation signature string based on class name and signature and compare with expected string.
		final String opSignatureString = ClassOperationSignaturePair.createOperationSignatureString(fqClassName, inputSignature);
		final String expectedOpSignatureString = "public " + fqClassName + "." + opName + "(" + paramTypes[0] + ", " + paramTypes[1] + ")";
		Assert.assertEquals("Unexpected result", expectedOpSignatureString, opSignatureString);

		// Now split expectedOpSignatureString and compare class name and signature
		final ClassOperationSignaturePair compSigPair = ClassOperationSignaturePair.splitOperationSignatureStr(expectedOpSignatureString, true);
		Assert.assertEquals("FQ classnames not equal", fqClassName, compSigPair.getFqClassname());
		Assert.assertEquals("Signatures not equal", inputSignature, compSigPair.getSignature());
	}

	/**
	 * A test for the class when using no parameters.
	 */
	@Test
	public void testNoParamTypes() {
		final String fqClassName = "a.b.c.D";
		final String opName = "op1";
		final String[] modifiers = { "public" };
		final String returnType = Boolean.class.getName();
		final String[] paramTypes = {};
		final Signature inputSignature = new Signature(opName, modifiers, returnType, paramTypes);

		// Obtain operation signature string based on class name and signature and compare with expected string.
		final String opSignatureString = ClassOperationSignaturePair.createOperationSignatureString(fqClassName, inputSignature);

		final String expectedOpSignatureString = modifiers[0] + " " + returnType + " " + fqClassName + "." + opName + "()";
		Assert.assertEquals("Unexpected result", expectedOpSignatureString, opSignatureString);

		// Now split expectedOpSignatureString and compare class name and signature
		final ClassOperationSignaturePair compSigPair = ClassOperationSignaturePair.splitOperationSignatureStr(expectedOpSignatureString);
		Assert.assertEquals("FQ classnames not equal", fqClassName, compSigPair.getFqClassname());
		Assert.assertEquals("Signatures not equal", inputSignature, compSigPair.getSignature());
	}
}
