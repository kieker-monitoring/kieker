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
import kieker.common.record.database.DatabaseFailedEvent;
import kieker.common.record.flow.trace.TraceMetadata;
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
	private static final String TECHNOLOGY = "JDBC"; // Hardcoded technology atm, as we use currently only JDBC (czi)
	private static final String LOGGING_PREFIX = "database.AbstractAspect: ";
	private static final String UNDEFINED_VALUE = "<undefined>";

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
		final String classSignature = this.getJoinPointClassName(joinPoint);

		if (!CTRLINST.isProbeActivated(classSignature)) {
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
		final String operationParameters = this.getJoinPointArguments(joinPoint);

		// measure before execution
		CTRLINST.newMonitoringRecord(new BeforeDatabaseEvent(TIME.getTime(), classSignature, traceId,
				trace.getNextOrderId(), operationParameters, TECHNOLOGY));
	}

	@AfterReturning(pointcut = "monitoredOperation() && notWithinKieker()", returning = "returningObject")
	public void afterReturningOperation(final JoinPoint joinPoint, final Object returningObject) {
		if (!CTRLINST.isMonitoringEnabled()) {
			return;
		}

		final String classSignature = this.getJoinPointClassName(joinPoint);

		if (!CTRLINST.isProbeActivated(classSignature)) {
			return;
		}

		final TraceMetadata trace = TRACEREGISTRY.getTrace();
		final String operationReturnType = this.getJoinPointReturnType(classSignature);
		final String returnedValue = this.getReturnedValue(returningObject, operationReturnType);

		CTRLINST.newMonitoringRecord(new AfterDatabaseEvent(TIME.getTime(), classSignature, trace.getTraceId(),
				trace.getNextOrderId(), operationReturnType, returnedValue));
	}

	@AfterThrowing(pointcut = "monitoredOperation() && notWithinKieker()", throwing = "th")
	public void afterThrowing(final JoinPoint joinPoint, final Throwable th) {
		if (!CTRLINST.isMonitoringEnabled()) {
			return;
		}

		final String classSignature = this.getJoinPointClassName(joinPoint);

		if (!CTRLINST.isProbeActivated(classSignature)) {
			return;
		}

		final TraceMetadata trace = TRACEREGISTRY.getTrace();

		CTRLINST.newMonitoringRecord(new DatabaseFailedEvent(TIME.getTime(), classSignature, trace.getTraceId(),
				trace.getNextOrderId(), th.toString()));
	}

	@After("monitoredOperation() && notWithinKieker()")
	public void afterOperation(final JoinPoint joinPoint) {
		if (!CTRLINST.isMonitoringEnabled()) {
			return;
		}

		final String operationSignature = this.getJoinPointClassName(joinPoint);

		if (!CTRLINST.isProbeActivated(operationSignature)) {
			return;
		}

		final int stackIndex = this.currentStackIndex.get().decrementValue();
		if (stackIndex == 1) {
			TRACEREGISTRY.unregisterTrace();
		}
	}

	/**
	 * Retrieves the className of the called method
	 * 
	 * @param currentJoinPoint
	 * @return
	 */
	public String getJoinPointClassName(final JoinPoint currentJoinPoint) {
		final String className = currentJoinPoint.getSignature().toString();
		return className.length() > 0 ? className : UNDEFINED_VALUE; // NOCS
	}

	/**
	 * Retrieves the return type of the called method
	 * 
	 * @param className
	 * @return
	 */
	private String getJoinPointReturnType(final String className) {
		final String[] splittedTypeName = className.split(" ");
		return splittedTypeName.length > 0 ? splittedTypeName[0] : UNDEFINED_VALUE; // NOCS
	}

	/**
	 * Retrieves the passed arguments of the JoinPoint
	 * 
	 * @param currentJoinPoint
	 * @return
	 */
	public String getJoinPointArguments(final JoinPoint currentJoinPoint) {

		String joinPointArgsString = UNDEFINED_VALUE;
		final Object[] joinPointArgs = currentJoinPoint.getArgs();

		if ((joinPointArgs != null) && joinPointArgs.length != 0) {
			joinPointArgsString = this.convertParametersToString(joinPointArgs);
		}
		return joinPointArgsString;
	}

	/**
	 * Retrieves the parameters of the called method
	 * 
	 * @param joinPointArgs
	 * @return
	 */
	private String convertParametersToString(final Object[] joinPointArgs) {
		String parametersString = UNDEFINED_VALUE;
		final StringBuilder sb = new StringBuilder();

		if (joinPointArgs != null) {
			for (final Object obj : joinPointArgs) {
				sb.append(obj);
				sb.append(';');
			}

			parametersString = sb.toString();
			// remove duplicate white spaces + leading and trailing ones
			parametersString = parametersString.replaceAll("\\s+", " ");
			parametersString = parametersString.trim();
		}
		return parametersString;
	}

	/**
	 * Processes the return value of the called method based on the type and returns
	 * a formatted string afterwards
	 * 
	 * @param returningObject
	 * @param returnType
	 * @param returnType
	 * @return
	 */
	private String getReturnedValue(final Object returningObject, final String returnType) {

		String returnValue = UNDEFINED_VALUE;

		if ((!returnType.isEmpty()) && (returningObject != null)) {
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
				returnValue = this.getReturnedNumberOfRows(returningObject);
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

	/**
	 * In case of a resultset we are interessted in the number of rows (affected
	 * rows) by the executed statement
	 * 
	 * @param rawReturnValue
	 * @return
	 */
	private String getReturnedNumberOfRows(final Object rawReturnValue) {
		String returnValue;
		int numberOfRows = 0;
		try {
			try (final ResultSet rs = (ResultSet) rawReturnValue) {
				while (rs.next()) {
					numberOfRows++;
				}
			}
		} catch (SQLException e) {
			LOG.error(LOGGING_PREFIX + "getReturnValue: SQL-Exception: " + e.getMessage()); // NOPMD
		}
		returnValue = String.valueOf(numberOfRows);
		return returnValue;
	}

}
