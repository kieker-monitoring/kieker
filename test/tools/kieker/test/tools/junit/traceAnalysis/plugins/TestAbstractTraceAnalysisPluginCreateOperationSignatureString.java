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

package kieker.test.tools.junit.traceAnalysis.plugins;

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.tools.traceAnalysis.plugins.AbstractTraceAnalysisPlugin;
import kieker.tools.traceAnalysis.systemModel.Signature;

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

		final String opSignatureString =
				AbstractTraceAnalysisPlugin.createOperationSignatureString(fqClassName, new Signature(opName, modifiers, returnType, paramTypes));

		final String expectedOpSignatureString = returnType + " " + fqClassName + "." + opName + "(" + paramTypes[0] + ", " + paramTypes[1] + ")";
		Assert.assertEquals("Unexpected result", expectedOpSignatureString, opSignatureString);
	}

	@Test
	public void testNoPackage() {
		final String fqClassName = "D";
		final String opName = "op1";
		final String[] modifiers = {};
		final String returnType = Boolean.class.getName();
		final String[] paramTypes = { Boolean.class.getName(), Integer.class.getName() };

		final String opSignatureString =
				AbstractTraceAnalysisPlugin.createOperationSignatureString(fqClassName, new Signature(opName, modifiers, returnType, paramTypes));

		final String expectedOpSignatureString = returnType + " " + fqClassName + "." + opName + "(" + paramTypes[0] + ", " + paramTypes[1] + ")";
		Assert.assertEquals("Unexpected result", expectedOpSignatureString, opSignatureString);
	}

	@Test
	public void testNoReturnType() {
		final String fqClassName = "a.b.c.D";
		final String opName = "op1";
		final String[] modifiers = {};
		final String returnType = null;
		final String[] paramTypes = { Boolean.class.getName(), Integer.class.getName() };

		try {
			final String opSignatureString =
					AbstractTraceAnalysisPlugin.createOperationSignatureString(fqClassName, new Signature(opName, modifiers, returnType, paramTypes));
			Assert.fail("Expected IllegalArgumentException to be thrown because modifiers but no return type; result: " + opSignatureString);
		} catch (final IllegalArgumentException ex) {
		}
	}

	@Test
	public void testNoParamTypes() {
		final String fqClassName = "a.b.c.D";
		final String opName = "op1";
		final String[] modifiers = {};
		final String returnType = Boolean.class.getName();
		final String[] paramTypes = {};

		final String opSignatureString =
				AbstractTraceAnalysisPlugin.createOperationSignatureString(fqClassName, new Signature(opName, modifiers, returnType, paramTypes));

		final String expectedOpSignatureString = returnType + " " + fqClassName + "." + opName + "()";
		Assert.assertEquals("Unexpected result", expectedOpSignatureString, opSignatureString);
	}
}
