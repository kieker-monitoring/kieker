/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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
package kieker.monitoring.probe.aspectj.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.JoinPoint.StaticPart;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.database.AfterDatabaseEvent;
import kieker.common.record.database.BeforeDatabaseEvent;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationFailedEvent;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.TraceRegistry;
import kieker.monitoring.probe.aspectj.AbstractAspectJProbe;
import kieker.monitoring.probe.aspectj.beforeafter.onlycallee.Counter;
import kieker.monitoring.timer.ITimeSource;

/**
 * This aspect spawns before and after events by foregoing an around advice.
 * Instead, it uses before and after advices only so that "cflow" can be used
 * when specifying its pointcut.
 * 
 * <blockquote> This aspect is based on the new
 * kieker.monitoring.probe.aspectj.beforeafter.onlycallee.AbstractAspect
 * </blockquote>
 * 
 * @author Christian Zirkelbach (czi@informatik.uni-kiel.de)
 * 
 * @since 1.14
 *
 */
@Aspect
public abstract class AbstractAspect extends AbstractAspectJProbe {

	private static final Log LOG = LogFactory.getLog(AbstractAspect.class);
	private static final IMonitoringController CTRLINST = MonitoringController.getInstance();
	private static final ITimeSource TIME = CTRLINST.getTimeSource();
	private static final TraceRegistry TRACEREGISTRY = TraceRegistry.INSTANCE;
	private static final String TECHNOLOGY = "JDBC"; // Hardcoded technology atm, as we use currently JDBC only (czi)

	private final ThreadLocal<Counter> currentStackIndex = new ThreadLocal<Counter>() {
		@Override
		protected Counter initialValue() {
			return new Counter();
		}
	};

	public AbstractAspect() {
		// default constructor
	}

	/**
	 * The pointcut for the monitored operations. Inheriting classes should extend
	 * the pointcut in order to find the correct executions of the methods (e.g. all
	 * methods or only methods with specific annotations).
	 */
	@Pointcut
	public abstract void monitoredOperation();

	@Before("monitoredOperation() && notWithinKieker()")
	public void beforeOperation(final JoinPoint joinPoint) {

		if (!CTRLINST.isMonitoringEnabled()) {
			return;
		}
		final String operationSignature = this.signatureToLongString(joinPoint.getSignature());

		if (!CTRLINST.isProbeActivated(operationSignature)) {
			return;
		}

		TraceMetadata trace = TRACEREGISTRY.getTrace();
		final boolean newTrace = trace == null;
		if (newTrace) {
			trace = TRACEREGISTRY.registerTrace(); // TO-DO parent trace is never used, so reduce impl. (chw)
			CTRLINST.newMonitoringRecord(trace);
		}

		this.currentStackIndex.get().incrementValue();

		final long traceId = trace.getTraceId();
		final String typeName = joinPoint.getSignature().getDeclaringTypeName();
		final String parameters = this.getParameters(joinPoint);

		// measure before execution
		CTRLINST.newMonitoringRecord(new BeforeDatabaseEvent(TIME.getTime(), typeName, operationSignature, traceId,
				trace.getNextOrderId(), parameters, TECHNOLOGY));
	}

	@AfterReturning(pointcut = "monitoredOperation() && notWithinKieker()", returning = "returningObject")
	public void afterReturningOperation(final JoinPoint joinPoint, final Object returningObject) {
		if (!CTRLINST.isMonitoringEnabled()) {
			return;
		}

		final String operationSignature = this.signatureToLongString(joinPoint.getSignature());
		if (!CTRLINST.isProbeActivated(operationSignature)) {
			return;
		}

		final TraceMetadata trace = TRACEREGISTRY.getTrace();
		final String typeName = joinPoint.getSignature().getDeclaringTypeName(); // called classname
		final String returnType = this.getReturnType(typeName);
		final String returnValue = this.getReturnValue(returningObject, typeName, returnType);

		CTRLINST.newMonitoringRecord(new AfterDatabaseEvent(TIME.getTime(), typeName, operationSignature,
				trace.getTraceId(), trace.getNextOrderId(), returnType, returnValue));
	}

	@AfterThrowing(pointcut = "monitoredOperation() && notWithinKieker()", throwing = "th")
	public void afterThrowing(final StaticPart jpStaticPart, final Throwable th) {
		if (!CTRLINST.isMonitoringEnabled()) {
			return;
		}

		final String operationSignature = this.signatureToLongString(jpStaticPart.getSignature());
		if (!CTRLINST.isProbeActivated(operationSignature)) {
			return;
		}

		final TraceMetadata trace = TRACEREGISTRY.getTrace();
		final String typeName = jpStaticPart.getSignature().getDeclaringTypeName();

		CTRLINST.newMonitoringRecord(new AfterOperationFailedEvent(TIME.getTime(), trace.getTraceId(),
				trace.getNextOrderId(), operationSignature, typeName, th.toString()));
	}

	@After("monitoredOperation() && notWithinKieker()")
	public void afterOperation(final StaticPart jpStaticPart) {
		if (!CTRLINST.isMonitoringEnabled()) {
			return;
		}

		final String operationSignature = this.signatureToLongString(jpStaticPart.getSignature());
		if (!CTRLINST.isProbeActivated(operationSignature)) {
			return;
		}

		final int stackIndex = this.currentStackIndex.get().decrementValue();
		if (stackIndex == 1) {
			TRACEREGISTRY.unregisterTrace();
		}
	}

	/**
	 * Retrieves the parameters of the called method
	 * 
	 * @param joinPoint
	 * @return
	 */
	public String getParameters(final JoinPoint joinPoint) {
		final Object[] joinPointArgs = joinPoint.getArgs();
		String returnValue = "";
		final StringBuilder sb = new StringBuilder();

		if (joinPointArgs != null) {
			for (final Object obj : joinPointArgs) {
				sb.append(obj);
				sb.append(';');
			}

			returnValue = sb.toString();
			// remove duplicate white spaces + leading and trailing ones
			returnValue = returnValue.replaceAll("\\s+", " ");
			returnValue = returnValue.trim();
		}
		return returnValue;
	}

	/**
	 * Returns the type of the called method
	 * 
	 * @param typeName
	 * @return
	 */
	public String getReturnType(final String typeName) {
		final String[] splittedTypeName = typeName.split(" ");
		return splittedTypeName.length > 0 ? splittedTypeName[0] : "<undefined>"; // NOCS
	}

	/**
	 * Processes the return value of the called method based on the type and returns
	 * a formatted string afterwards
	 * 
	 * @param returningObject
	 * @param returnType
	 * @param typeName
	 * @return
	 */
	private String getReturnValue(final Object returningObject, final String typeName, final String returnType) {

		String returnValue = "<undefined>";

		if ((!typeName.isEmpty()) && (returningObject != null)) {
			int numberOfRows = 0;

			switch (returnType) {
			case "STRING":
				returnValue = returningObject.toString();
				break;
			case "BOOLEAN":
				returnValue = returningObject.toString();
				break;
			case "INT":
				numberOfRows = (int) returningObject;
				returnValue = String.valueOf(numberOfRows);
				break;
			case "RESULTSET":
				returnValue = this.getNumberOfNumbers(returningObject);
				break;
			// TO-DO Further relevant return types? (czi)
			// case "STATEMENT":
			// break;
			// case "PREPAREDSTATEMENT":
			// break;
			// case "INT[]":
			// break;
			// case "CALLABLESTATEMENT":
			// break;
			default:
				break;
			}
		}
		return returnValue;
	}

	private String getNumberOfNumbers(final Object rawReturnValue) {
		String returnValue;
		int numberOfRows = 0;
		try {
			try (final ResultSet rs = (ResultSet) rawReturnValue) {
				while (rs.next()) {
					numberOfRows++;
				}
			}
		} catch (SQLException e) {
			LOG.error("DatabaseAbstract: getReturnValue: SQL-Exception: " + e.getMessage()); // NOPMD
		}
		returnValue = String.valueOf(numberOfRows);
		return returnValue;
	}

}