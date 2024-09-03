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

package kieker.monitoring.probe.aspectj;

import java.lang.reflect.Modifier;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.ConstructorSignature;
import org.aspectj.lang.reflect.MethodSignature;

import kieker.monitoring.probe.IMonitoringProbe;

/**
 * @author Jan Waller
 *
 * @since 1.2
 */
@Aspect
public abstract class AbstractAspectJProbe implements IMonitoringProbe { // NOPMD

	private final ConcurrentMap<Signature, String> signatureCache = new ConcurrentHashMap<>();

	// Pointcuts should not be final!

	/**
	 * This is a pointcut accepting everything that is not within kieker.common, kieker.monitoring, kieker.analysis and kieker.tools.
	 */
	@Pointcut("!within(kieker.common..*) && !within(kieker.monitoring..*) && !within(kieker.analysis..*) && !within(kieker.tools..*)")
	public void notWithinKieker() {} // NOPMD (Aspect)

	/**
	 * This is a pointcut accepting all calls and executions of setter methods (methods which return void and start with {@code set}).
	 */
	@Pointcut("execution(void set*(..)) || call(void set*(..))")
	public void setter() {} // NOPMD (Aspect)

	/**
	 * This is a pointcut accepting all calls and executions of getter methods (methods which return something and start with {@code get} or return a boolean and
	 * start with {@code is}).
	 */
	@Pointcut("execution(* get*(..)) || call(* get*(..)) || execution(boolean is*(..)) || call(boolean is*(..))")
	public void getter() {} // NOPMD (Aspect)

	/**
	 * This is a pointcut accepting everything that is neither a getter nor a setter.
	 */
	@Pointcut("!getter() && !setter()")
	public void noGetterAndSetter() {} // NOPMD (Aspect)

	/**
	 * Better handling of AspectJ Signature.toLongString (especially with constructors).
	 *
	 * @param sig
	 *            an AspectJ Signature
	 * @return LongString representation of the signature
	 */
	protected String signatureToLongString(final Signature sig) {
		String signatureString = this.signatureCache.get(sig);
		if (null != signatureString) {
			return signatureString;
		} else {
			if (sig instanceof MethodSignature) {
				final MethodSignature signature = (MethodSignature) sig;
				final StringBuilder sb = new StringBuilder(256);
				// modifiers
				final String modString = Modifier.toString(signature.getModifiers());
				sb.append(modString);
				if (modString.length() > 0) {
					sb.append(' ');
				}
				// return
				this.addType(sb, signature.getReturnType());
				sb.append(' ')
						.append(signature.getDeclaringTypeName()) // component
						.append('.')
						.append(signature.getName()) // name
						.append('(');
				// parameters

				this.addTypeList(sb, signature.getParameterTypes());
				sb.append(')');
				// throws
				// this.addTypeList(sb, signature.getExceptionTypes());
				signatureString = sb.toString();
			} else if (sig instanceof ConstructorSignature) {
				final ConstructorSignature signature = (ConstructorSignature) sig;
				final StringBuilder sb = new StringBuilder(256);
				// modifiers
				final String modString = Modifier.toString(signature.getModifiers());
				sb.append(modString);
				if (modString.length() > 0) {
					sb.append(' ');
				}
				// component
				sb.append(signature.getDeclaringTypeName())
						.append('.')
						.append(signature.getName())// name
						.append('(');
				// parameters
				this.addTypeList(sb, signature.getParameterTypes());
				sb.append(')');
				// throws
				// this.addTypeList(sb, signature.getExceptionTypes());
				signatureString = sb.toString();
			} else {
				signatureString = sig.toLongString();
			}
		}
		this.signatureCache.putIfAbsent(sig, signatureString);
		return signatureString;
	}

	private final StringBuilder addTypeList(final StringBuilder sb, final Class<?>[] clazzes) {
		if (null != clazzes) {
			boolean first = true;
			for (final Class<?> clazz : clazzes) {
				if (first) {
					first = false;
				} else {
					sb.append(",");
				}
				this.addType(sb, clazz);
			}
		}
		return sb;
	}

	private final StringBuilder addType(final StringBuilder sb, final Class<?> clazz) {
		if (null == clazz) {
			sb.append("ANONYMOUS");
		} else if (clazz.isArray()) {
			final Class<?> componentType = clazz.getComponentType();
			this.addType(sb, componentType);
			sb.append("[]");
		} else {
			sb.append(clazz.getName());
		}
		return sb;
	}
}
