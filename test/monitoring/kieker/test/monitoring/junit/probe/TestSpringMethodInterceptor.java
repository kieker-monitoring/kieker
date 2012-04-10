/***************************************************************************
 * Copyright 2011 by
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

package kieker.test.monitoring.junit.probe;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

import org.aopalliance.intercept.AttributeRegistry;
import org.aopalliance.intercept.Invocation;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.core.registry.SessionRegistry;

/**
 * 
 * @author Andre van Hoorn
 * 
 */
public class TestSpringMethodInterceptor {
	protected final ControlFlowRegistry controlFlowRegistry = ControlFlowRegistry.INSTANCE;
	protected final SessionRegistry sessionRegistry = SessionRegistry.INSTANCE;

	@Before
	public void init() {
		this.controlFlowRegistry.unsetThreadLocalEOI();
		this.controlFlowRegistry.unsetThreadLocalESS();
		this.controlFlowRegistry.unsetThreadLocalTraceId();
	}

	// TODO: finalize
	@Test
	public void testNoTraceId() throws Throwable {
		// final OperationExecutionMethodInvocationInterceptor methodInterceptor = new OperationExecutionMethodInvocationInterceptor(false); // do not log executions

		// final Object bookstoreObject = new Object();
		// final Method bookstoreMethod = bookstoreObject.getClass().getMethods()[0];

		// final Object catalogObject = new Object();
		// final Method catalogMethod = catalogObject.getClass().getMethods()[0];

		final Object crmObject = new Object();
		// final Method crmMethod = crmObject.getClass().getMethods()[0];

		// final MethodInvocation invocation11Catalog = new BasicMethodInvocation(catalogMethod, methodInterceptor, new MethodInvocation[0]);
		// final MethodInvocation invocation32Catalog = new BasicMethodInvocation(catalogMethod, methodInterceptor, new MethodInvocation[0]);
		// final MethodInvocation invocation21CRM = new BasicMethodInvocation(crmMethod, methodInterceptor, new MethodInvocation[] { invocation32Catalog });
		// final MethodInvocation invocation00Bookstore =
		// new BasicMethodInvocation(bookstoreMethod, methodInterceptor, new MethodInvocation[] { invocation11Catalog, invocation21CRM });
		// invocation00Bookstore.proceed();

		Assert.assertNotNull("", crmObject); // just to have a test; TODO: remove
	}

	@After
	public void cleanup() {
		this.controlFlowRegistry.unsetThreadLocalEOI();
		this.controlFlowRegistry.unsetThreadLocalESS();
		this.controlFlowRegistry.unsetThreadLocalTraceId();
	}

	/**
	 * 
	 * @author Andre van Hoorn
	 * 
	 */
	class BasicMethodInvocation implements MethodInvocation {
		private final Method myMethod;
		private final MethodInterceptor methodInterceptor;
		private final MethodInvocation[] methodsToInvoke;

		private final int expectedEoiBeforeInvokes;
		private final int expectedEssBeforeInvokes;
		private final int expectedEoiAfterInvokes;
		private final int expectedEssAfterInvokes;

		public BasicMethodInvocation(
				final int expectedEoiBeforeInvokes, final int expectedEssBeforeInvokes,
				final int expectedEoiAfterInvokes, final int expectedEssAfterInvokes,
				final Method myMethod, final MethodInterceptor methodInterceptor, final MethodInvocation[] subInvocations) {
			this.expectedEoiBeforeInvokes = expectedEoiBeforeInvokes;
			this.expectedEssBeforeInvokes = expectedEssBeforeInvokes;
			this.expectedEoiAfterInvokes = expectedEoiAfterInvokes;
			this.expectedEssAfterInvokes = expectedEssAfterInvokes;
			this.myMethod = myMethod;
			this.methodInterceptor = methodInterceptor;
			this.methodsToInvoke = subInvocations;
		}

		public Object proceed() throws Throwable {
			Assert.assertEquals("Unexpected eoi before invokes", this.expectedEoiBeforeInvokes,
					TestSpringMethodInterceptor.this.controlFlowRegistry.recallThreadLocalEOI());
			Assert.assertEquals("Unexpected ess before invokes", this.expectedEssBeforeInvokes,
					TestSpringMethodInterceptor.this.controlFlowRegistry.recallThreadLocalESS());
			for (final MethodInvocation miv : this.methodsToInvoke) {
				this.methodInterceptor.invoke(miv);
			}
			Assert.assertEquals("Unexpected eoi after invokes", this.expectedEoiAfterInvokes,
					TestSpringMethodInterceptor.this.controlFlowRegistry.recallThreadLocalEOI());
			Assert.assertEquals("Unexpected ess after invokes", this.expectedEssAfterInvokes,
					TestSpringMethodInterceptor.this.controlFlowRegistry.recallThreadLocalESS());
			return null;
		}

		public Object getThis() {
			throw new UnsupportedOperationException();
		}

		public AccessibleObject getStaticPart() {
			throw new UnsupportedOperationException();
		}

		public void setArgument(final int arg0, final Object arg1) {
			throw new UnsupportedOperationException();
		}

		public AttributeRegistry getAttributeRegistry() {
			throw new UnsupportedOperationException();
		}

		public Object getAttachment(final String arg0) {
			throw new UnsupportedOperationException();
		}

		public Object[] getArguments() {
			throw new UnsupportedOperationException();
		}

		public int getArgumentCount() {
			throw new UnsupportedOperationException();
		}

		public Object getArgument(final int arg0) {
			throw new UnsupportedOperationException();
		}

		public Invocation cloneInstance() {
			throw new UnsupportedOperationException();
		}

		public Object addAttachment(final String arg0, final Object arg1) {
			throw new UnsupportedOperationException();
		}

		public Method getMethod() {
			return this.myMethod;
		}
	}
}
