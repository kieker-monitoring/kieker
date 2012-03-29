/***************************************************************************
 * Copyright 2012 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.test.tools.junit.traceAnalysis.filter;

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.common.util.ClassOperationSignaturePair;
import kieker.common.util.Signature;

import org.junit.Test;

/**
 * 
 * @author Andre van Hoorn
 * 
 */
public class TestAbstractTraceAnalysisPluginCreateOperationSignatureString extends TestCase {

	@Test
	public void testNoModifiers() {
		final String fqClassName = "a.b.c.D";
		final String opName = "op1";
		final String[] modifiers = {};
		final String returnType = Boolean.class.getName();
		final String[] paramTypes = { Boolean.class.getName(), Integer.class.getName() };
		final Signature inputSignature = new Signature(opName, modifiers, returnType, paramTypes);

		/*
		 * Obtain operation signature string based on class name and signature and compare
		 * with expected string.
		 */
		final String opSignatureString = ClassOperationSignaturePair.createOperationSignatureString(fqClassName, inputSignature);
		final String expectedOpSignatureString = returnType + " " + fqClassName + "." + opName + "(" + paramTypes[0] + ", " + paramTypes[1] + ")";
		Assert.assertEquals("Unexpected result", expectedOpSignatureString, opSignatureString);

		/*
		 * Now split expectedOpSignatureString and compare class name and signature
		 */
		final ClassOperationSignaturePair compSigPair = ClassOperationSignaturePair.splitOperationSignatureStr(expectedOpSignatureString);
		Assert.assertEquals("FQ classnames not equal", fqClassName, compSigPair.getFqClassname());
		// TODO: Comparing Signature.toString() results as long as no implementation of Signature.equals
		Assert.assertEquals("Signatures not equal", inputSignature.toString(), compSigPair.getSignature().toString());
	}

	@Test
	public void testNoPackage() {
		final String fqClassName = "D";
		final String opName = "op1";
		final String[] modifiers = { "public", "static" };
		final String returnType = Boolean.class.getName();
		final String[] paramTypes = { Boolean.class.getName(), Integer.class.getName() };
		final Signature inputSignature = new Signature(opName, modifiers, returnType, paramTypes);

		/*
		 * Obtain operation signature string based on class name and signature and compare
		 * with expected string.
		 */
		final String opSignatureString = ClassOperationSignaturePair.createOperationSignatureString(fqClassName, inputSignature);
		final String expectedOpSignatureString =
				modifiers[0] + " " + modifiers[1] + " " + returnType + " " + fqClassName + "." + opName + "(" + paramTypes[0] + ", " + paramTypes[1] + ")";
		Assert.assertEquals("Unexpected result", expectedOpSignatureString, opSignatureString);

		/*
		 * Now split expectedOpSignatureString and compare class name and signature
		 */
		final ClassOperationSignaturePair compSigPair = ClassOperationSignaturePair.splitOperationSignatureStr(expectedOpSignatureString);
		Assert.assertEquals("FQ classnames not equal", fqClassName, compSigPair.getFqClassname());
		// TODO: Comparing Signature.toString() results as long as no implementation of Signature.equals
		Assert.assertEquals("Signatures not equal", inputSignature.toString(), compSigPair.getSignature().toString());
	}

	@Test
	public void testNoModifiersNoReturnType() {
		final String fqClassName = "a.b.c.D";
		final String opName = "op1";
		final String[] modifiers = {};
		final String[] paramTypes = { Boolean.class.getName(), Integer.class.getName() };
		final Signature inputSignature = new Signature(opName, modifiers, null, paramTypes);

		/*
		 * Obtain operation signature string based on class name and signature and compare
		 * with expected string.
		 */
		final String opSignatureString = ClassOperationSignaturePair.createOperationSignatureString(fqClassName, inputSignature);
		final String expectedOpSignatureString = fqClassName + "." + opName + "(" + paramTypes[0] + ", " + paramTypes[1] + ")";
		Assert.assertEquals("Unexpected result", expectedOpSignatureString, opSignatureString);

		/*
		 * Now split expectedOpSignatureString and compare class name and signature
		 */
		final ClassOperationSignaturePair compSigPair = ClassOperationSignaturePair.splitOperationSignatureStr(expectedOpSignatureString);
		Assert.assertEquals("FQ classnames not equal", fqClassName, compSigPair.getFqClassname());
		// TODO: Comparing Signature.toString() results as long as no implementation of Signature.equals
		Assert.assertEquals("Signatures not equal", inputSignature.toString(), compSigPair.getSignature().toString());
	}

	@Test
	public void testModifierButNoReturnType() {
		final String fqClassName = "a.b.c.D";
		final String opName = "op1";
		final String[] modifiers = { "public" };
		final String[] paramTypes = { Boolean.class.getName(), Integer.class.getName() };
		final Signature inputSignature = new Signature(opName, modifiers, null, paramTypes);

		/*
		 * Obtain operation signature string based on class name and signature.
		 * In this case, we expect an exception to be thrown
		 */
		try {
			final String opSignatureString = ClassOperationSignaturePair.createOperationSignatureString(fqClassName, inputSignature);
			Assert.fail("Expected IllegalArgumentException to be thrown because modifiers but no return type; result: " + opSignatureString);
		} catch (final IllegalArgumentException ex) { // NOPMD (ignore exception)
		}
	}

	@Test
	public void testNoParamTypes() {
		final String fqClassName = "a.b.c.D";
		final String opName = "op1";
		final String[] modifiers = { "public" };
		final String returnType = Boolean.class.getName();
		final String[] paramTypes = {};
		final Signature inputSignature = new Signature(opName, modifiers, returnType, paramTypes);

		/*
		 * Obtain operation signature string based on class name and signature and compare
		 * with expected string.
		 */
		final String opSignatureString =
				ClassOperationSignaturePair.createOperationSignatureString(fqClassName, inputSignature);

		final String expectedOpSignatureString =
				modifiers[0] + " " + returnType + " " + fqClassName + "." + opName + "()";
		Assert.assertEquals("Unexpected result", expectedOpSignatureString, opSignatureString);

		/*
		 * Now split expectedOpSignatureString and compare class name and signature
		 */
		final ClassOperationSignaturePair compSigPair = ClassOperationSignaturePair.splitOperationSignatureStr(expectedOpSignatureString);
		Assert.assertEquals("FQ classnames not equal", fqClassName, compSigPair.getFqClassname());
		// TODO: Comparing Signature.toString() results as long as no implementation of Signature.equals
		Assert.assertEquals("Signatures not equal", inputSignature.toString(), compSigPair.getSignature().toString());
	}
}
