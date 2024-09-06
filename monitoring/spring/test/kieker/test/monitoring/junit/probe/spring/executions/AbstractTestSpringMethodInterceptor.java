/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.monitoring.junit.probe.spring.executions;

import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.aopalliance.intercept.Invocation;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import kieker.common.configuration.Configuration;
import kieker.monitoring.core.configuration.ConfigurationConstants;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.core.registry.SessionRegistry;
import kieker.monitoring.probe.spring.executions.OperationExecutionMethodInvocationInterceptor;
import kieker.monitoring.writer.filesystem.FileWriter;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * Tests the {@link kieker.monitoring.probe.spring.executions.OperationExecutionMethodInvocationInterceptor}.
 *
 * @author Andre van Hoorn
 *
 * @since 1.5
 */
public abstract class AbstractTestSpringMethodInterceptor extends AbstractKiekerTest { // NOPMD (AbstractClassWithoutAbstractMethod)

	/** A rule making sure that a temporary folder exists for every test method (which is removed after the test). */
	@Rule
	public final TemporaryFolder tmpFolder = new TemporaryFolder(); // NOCS (@Rule must be public)

	protected final ControlFlowRegistry controlFlowRegistry = ControlFlowRegistry.INSTANCE;
	protected final SessionRegistry sessionRegistry = SessionRegistry.INSTANCE;

	private final String sessionId = UUID.randomUUID().toString();

	private final boolean interceptorIsEntryPoint; // if true, we'll emulate a wrapping execution, which may e.g. be a servlet filter

	private IMonitoringController monitoringCtrl;

	/**
	 * @param interceptorIsEntryPoint
	 *            Whether the interceptor is an entry point or not.
	 */
	public AbstractTestSpringMethodInterceptor(final boolean interceptorIsEntryPoint) {
		this.interceptorIsEntryPoint = interceptorIsEntryPoint;
	}

	@Before
	public void init() throws IOException {
		final Configuration config = ConfigurationFactory.createDefaultConfiguration();
		config.setProperty(ConfigurationConstants.WRITER_CLASSNAME, FileWriter.class.getName());
		config.setProperty(FileWriter.CONFIG_PATH, this.tmpFolder.getRoot().getAbsolutePath());
		this.monitoringCtrl = MonitoringController.createInstance(config);

		this.controlFlowRegistry.unsetThreadLocalEOI();
		this.controlFlowRegistry.unsetThreadLocalESS();
		this.controlFlowRegistry.unsetThreadLocalTraceId();
	}

	@After
	public void cleanup() {
		this.controlFlowRegistry.unsetThreadLocalEOI();
		this.controlFlowRegistry.unsetThreadLocalESS();
		this.controlFlowRegistry.unsetThreadLocalTraceId();
		// this.tmpFolder.delete();
	}

	@Test
	public void testIt() throws Throwable { // NOPMD (JUnitTestsShouldIncludeAssert), assertions in inv.checkEoiEss(); // NOCS
		final OperationExecutionMethodInvocationInterceptor methodInterceptor = new OperationExecutionMethodInvocationInterceptor(this.monitoringCtrl); // do not log
																																						// executions

		final Bookstore bookstoreObject = new Bookstore();
		final Method bookstoreMethod = bookstoreObject.lookupPseudoMethod();

		final Catalog catalogObject = new Catalog();
		final Method catalogMethod = catalogObject.lookupPseudoMethod();

		final CRM crmObject = new CRM();
		final Method crmMethod = crmObject.lookupPseudoMethod();

		final int eoiExpectedForOuterInterceptor;
		final int essExpectedForOuterInterceptor;
		if (this.interceptorIsEntryPoint) {
			eoiExpectedForOuterInterceptor = 0;
			essExpectedForOuterInterceptor = 0;
		} else {
			eoiExpectedForOuterInterceptor = 1;
			essExpectedForOuterInterceptor = 1;
		}

		if (!this.interceptorIsEntryPoint) {
			this.registerSessionInfo();
			this.registerTraceInfo();
		}

		final List<BasicMethodInvocation> invocations = new ArrayList<>(4);

		// Note that right before the proceed we expect the ess to be proceeding execution's ess +1!
		final BasicMethodInvocation invocation11Catalog = new BasicMethodInvocation(// eoi should not increase because no sub call
				eoiExpectedForOuterInterceptor + 1, eoiExpectedForOuterInterceptor + 2, // eoi/ess before sub calls (right before proceed)
				eoiExpectedForOuterInterceptor + 1, eoiExpectedForOuterInterceptor + 2, // eoi/ess after sub calls (right after proceed)
				catalogMethod, methodInterceptor, new MethodInvocation[0]);
		invocations.add(invocation11Catalog);
		final BasicMethodInvocation invocation32Catalog = new BasicMethodInvocation(// eoi should not increase because no sub call
				eoiExpectedForOuterInterceptor + 3, eoiExpectedForOuterInterceptor + 3, // eoi/ess before sub calls (right before proceed)
				eoiExpectedForOuterInterceptor + 3, eoiExpectedForOuterInterceptor + 3, // eoi/ess after sub calls (right after proceed)
				catalogMethod, methodInterceptor, new MethodInvocation[0]);
		invocations.add(invocation32Catalog);
		final BasicMethodInvocation invocation21CRM = new BasicMethodInvocation(// eoi increases due to sub call
				eoiExpectedForOuterInterceptor + 2, eoiExpectedForOuterInterceptor + 2, // eoi/ess before sub calls (right before proceed)
				eoiExpectedForOuterInterceptor + 3, eoiExpectedForOuterInterceptor + 2, // eoi/ess after sub calls (right after proceed)
				crmMethod, methodInterceptor, new MethodInvocation[] { invocation32Catalog });
		invocations.add(invocation21CRM);
		final BasicMethodInvocation invocation00Bookstore = new BasicMethodInvocation(// eoi increases due to sub calls(right after proceed)
				eoiExpectedForOuterInterceptor + 0, essExpectedForOuterInterceptor + 1, // eoi/ess before sub calls (right before proceed)
				eoiExpectedForOuterInterceptor + 3, essExpectedForOuterInterceptor + 1, // eoi/ess after sub calls (right after proceed)
				bookstoreMethod, methodInterceptor, new MethodInvocation[] { invocation11Catalog, invocation21CRM });
		invocations.add(invocation00Bookstore);

		methodInterceptor.invoke(invocation00Bookstore); // we emulate the AOP framework here and execute the Bookstore application

		for (final BasicMethodInvocation inv : invocations) {
			inv.checkEoiEss();
		}

		if (!this.interceptorIsEntryPoint) {
			this.unregisterTraceInfo();
			this.unregisterSessionInfo();
		}
	}

	private void registerSessionInfo() {
		this.sessionRegistry.storeThreadLocalSessionId(this.sessionId);
	}

	private void unregisterSessionInfo() {
		this.sessionRegistry.unsetThreadLocalSessionId();
	}

	private long registerTraceInfo() {
		final long traceId = this.controlFlowRegistry.getAndStoreUniqueThreadLocalTraceId();
		this.controlFlowRegistry.storeThreadLocalEOI(0); // current execution's eoi is 0
		this.controlFlowRegistry.storeThreadLocalESS(1); // *current* execution's ess is 0; next execution is at stack depth 1
		return traceId;
	}

	private void unregisterTraceInfo() {
		this.controlFlowRegistry.unsetThreadLocalTraceId();
		this.controlFlowRegistry.unsetThreadLocalEOI();
		this.controlFlowRegistry.unsetThreadLocalESS();
	}

	/**
	 *
	 * @author Andre van Hoorn
	 *
	 * @since 1.5
	 */
	class BasicMethodInvocation implements MethodInvocation {
		private final Method myMethod;
		private final MethodInterceptor methodInterceptor;
		private final MethodInvocation[] methodsToInvoke;

		private final int expectedEoiBeforeInvokes;
		private final int expectedEssBeforeInvokes;
		private final int expectedEoiAfterInvokes;
		private final int expectedEssAfterInvokes;

		private volatile int actualEoiBeforeInvokes;
		private volatile int actualEssBeforeInvokes;
		private volatile int actualEoiAfterInvokes;
		private volatile int actualEssAfterInvokes;

		public BasicMethodInvocation(
				final int expectedEoiBeforeInvokes, final int expectedEssBeforeInvokes,
				final int expectedEoiAfterInvokes, final int expectedEssAfterInvokes,
				final Method myMethod, final MethodInterceptor methodInterceptor,
				final MethodInvocation[] subInvocations) { // NOPMD (ArrayIsStoredDirectly); is a test, we don't care here
			this.expectedEoiBeforeInvokes = expectedEoiBeforeInvokes;
			this.expectedEssBeforeInvokes = expectedEssBeforeInvokes;
			this.expectedEoiAfterInvokes = expectedEoiAfterInvokes;
			this.expectedEssAfterInvokes = expectedEssAfterInvokes;
			this.myMethod = myMethod;
			this.methodInterceptor = methodInterceptor;
			this.methodsToInvoke = subInvocations;
		}

		@Override
		public Object proceed() throws Throwable { // NOCS (Throwable)
			this.actualEoiBeforeInvokes = AbstractTestSpringMethodInterceptor.this.controlFlowRegistry.recallThreadLocalEOI();
			this.actualEssBeforeInvokes = AbstractTestSpringMethodInterceptor.this.controlFlowRegistry.recallThreadLocalESS();
			for (final MethodInvocation miv : this.methodsToInvoke) {
				this.methodInterceptor.invoke(miv);
			}
			this.actualEoiAfterInvokes = AbstractTestSpringMethodInterceptor.this.controlFlowRegistry.recallThreadLocalEOI();
			this.actualEssAfterInvokes = AbstractTestSpringMethodInterceptor.this.controlFlowRegistry.recallThreadLocalESS();
			return null;
		}

		/**
		 * Compares expected with actual eois and ess before and after the proceed invocation.
		 */
		public void checkEoiEss() {
			Assert.assertEquals("Unexpected eoi before invokes for " + this.myMethod.toString(),
					this.expectedEoiBeforeInvokes, this.actualEoiBeforeInvokes);
			Assert.assertEquals("Unexpected ess before invokes for " + this.myMethod.toString(),
					this.expectedEssBeforeInvokes, this.actualEssBeforeInvokes);
			Assert.assertEquals("Unexpected eoi after invokes for " + this.myMethod.toString(),
					this.expectedEoiAfterInvokes, this.actualEoiAfterInvokes);
			Assert.assertEquals("Unexpected ess after invokes for " + this.myMethod.toString(),
					this.expectedEssAfterInvokes, this.actualEssAfterInvokes);
		}

		@Override
		public Object getThis() {
			throw new UnsupportedOperationException();
		}

		@Override
		public AccessibleObject getStaticPart() {
			throw new UnsupportedOperationException();
		}

		public void setArgument(final int arg0, final Object arg1) {
			throw new UnsupportedOperationException();
		}

		public Object getAttachment(final String arg0) {
			throw new UnsupportedOperationException();
		}

		@Override
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

		@Override
		public Method getMethod() {
			return this.myMethod;
		}
	}

	/**
	 * @author Andre van Hoorn
	 *
	 * @since 1.5
	 */
	public abstract static class AbstractPseudoComponent { // NOPMD (AbstractClassWithoutAbstractMethod)
		private final String pseudoMethodName;

		AbstractPseudoComponent(final String pseudoMethodName) {
			this.pseudoMethodName = pseudoMethodName;
		}

		public Method lookupPseudoMethod() throws SecurityException, NoSuchMethodException {
			return this.getClass().getMethod(this.pseudoMethodName, new Class<?>[0]);
		}
	}

	/**
	 * @author Andre van Hoorn
	 *
	 * @since 1.5
	 */
	public static final class Bookstore extends AbstractPseudoComponent { // NOPMD (TestClassWithoutTestCases, reported because classname ends with "Test")
		Bookstore() {
			super("searchBook");
		}

		/**
		 * Searches a book (dummy method).
		 */
		public void searchBook() {} // NOPMD (UncommentedEmptyMethod)
	}

	/**
	 * @author Andre van Hoorn
	 *
	 * @since 1.5
	 */
	public static final class CRM extends AbstractPseudoComponent { // NOPMD (TestClassWithoutTestCases, reported because classname ends with "Test")
		CRM() {
			super("getOffers");
		}

		/**
		 * Delivers the offers (dummy method).
		 */
		public void getOffers() {} // NOPMD (UncommentedEmptyMethod)
	}

	/**
	 * @author Andre van Hoorn
	 *
	 * @since 1.5
	 */
	public static final class Catalog extends AbstractPseudoComponent { // NOPMD (TestClassWithoutTestCases, reported because classname ends with "Test")

		Catalog() {
			super("getBook");
		}

		/**
		 * Delivers a book (dummy method).
		 */
		public void getBook() {} // NOPMD (UncommentedEmptyMethod)
	}

}
