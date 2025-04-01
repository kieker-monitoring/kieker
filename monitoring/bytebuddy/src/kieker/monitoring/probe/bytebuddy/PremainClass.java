/***************************************************************************
 * Copyright 2025 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package kieker.monitoring.probe.bytebuddy;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.core.registry.SessionRegistry;
import kieker.monitoring.timer.ITimeSource;
import kieker.monitoring.util.KiekerPattern;
import kieker.monitoring.util.KiekerPatternUtil;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.AgentBuilder.Identified.Extendable;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Optional.Valuable;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.utility.JavaModule;

/**
 * Premain class that allows load-time instrumentation using ByteBuddy.
 * 
 * @author David Georg Reichelt
 */
public class PremainClass {

	private static final Logger LOGGER = LoggerFactory.getLogger(PremainClass.class);

	private static final AgentBuilder.Listener ONLY_ERROR_LOGGER = new AgentBuilder.Listener() {
		@Override
		public void onDiscovery(final String typeName, final ClassLoader classLoader, final JavaModule module,
				final boolean loaded) {}

		@Override
		public void onTransformation(final TypeDescription typeDescription, final ClassLoader classLoader,
				final JavaModule module, final boolean loaded, final DynamicType dynamicType) {}

		@Override
		public void onIgnored(final TypeDescription typeDescription, final ClassLoader classLoader,
				final JavaModule module, final boolean loaded) {}

		@Override
		public void onError(final String typeName, final ClassLoader classLoader, final JavaModule module,
				final boolean loaded, final Throwable throwable) {
			throwable.printStackTrace();
		}

		@Override
		public void onComplete(final String typeName, final ClassLoader classLoader, final JavaModule module,
				final boolean loaded) {

		}

	};

	public static void premain(final String agentArgs, final Instrumentation inst) {
		LOGGER.info("Starting instrumentation...");

		final String instrumentables = System.getenv(InstrumentationEnvironmentVariables.KIEKER_SIGNATURES_INCLUDE);
		if (instrumentables != null) {
			final List<KiekerPattern> excludePatterns = getExcludedPatterns();
			final List<KiekerPattern> includePatterns = KiekerPatternUtil.getPatterns(instrumentables);
			final Extendable basicAgentBuilder = new AgentBuilder.Default()
					.with(ONLY_ERROR_LOGGER)
					// .with(AgentBuilder.LambdaInstrumentationStrategy.ENABLED)
					// Instrumenting Lambdas results in strange errors as soon as java.util.regex is needed, but not loaded yet
					.type(new ElementMatcher<TypeDescription>() {
						@Override
						public boolean matches(final TypeDescription target) {
							if (target.isInterface()) {
								return false;
							}
							final String typeName = target.getTypeName();
							final boolean included = KiekerPatternUtil.classIsContained(includePatterns, typeName);
							final boolean excluded = KiekerPatternUtil.classIsContained(excludePatterns, typeName);
							if (included) {
								LOGGER.debug("Checked " + typeName);
								LOGGER.debug("Included: " + included + " Excluded: " + excluded);
							}
							return included && !excluded;
						}
					}).transform(new AgentBuilder.Transformer.ForAdvice().advice(new ElementMatcher<MethodDescription>() {

						@Override
						public boolean matches(final MethodDescription target) {
							if (target.isMethod()) {
								// TODO: Here, we would need the signature. This would require building it from
								// target and target.getType
								// So for now, just instrument every method (and type is checked before already)
								// KiekerPatternUtil.classIsContained(patternObjects, target.getName())
								return true;
							} else {
								return true;
							}
						}

					}, OperationExecutionAdvice.class.getName()));
			addFields(basicAgentBuilder).installOn(inst);
		} else {
			LOGGER.error("Environment variable " + InstrumentationEnvironmentVariables.KIEKER_SIGNATURES_INCLUDE + " not defined - not instrumenting anything!");
		}
	}

	private static Extendable addFields(final Extendable basicAgentBuilder) {
		return basicAgentBuilder.transform(new AgentBuilder.Transformer() {

			@Override
			public Builder<?> transform(final Builder<?> builder, final TypeDescription typeDescription,
					final ClassLoader classLoader, final JavaModule module,
					final ProtectionDomain protectionDomain) {
				LOGGER.info("Instrumenting: {}", typeDescription.getActualName());
				final Valuable<?> definedField = builder.defineField("CTRLINST", IMonitoringController.class,
						Modifier.STATIC | Modifier.FINAL | Modifier.PRIVATE);
				return definedField;
			}
		}).transform(new AgentBuilder.Transformer() {

			@Override
			public Builder<?> transform(final Builder<?> builder, final TypeDescription typeDescription,
					final ClassLoader classLoader, final JavaModule module,
					final ProtectionDomain protectionDomain) {
				final Valuable<?> definedField = builder.defineField("TIME", ITimeSource.class,
						Modifier.STATIC | Modifier.FINAL | Modifier.PRIVATE);
				return definedField;
			}
		}).transform(new AgentBuilder.Transformer() {

			@Override
			public Builder<?> transform(final Builder<?> builder, final TypeDescription typeDescription,
					final ClassLoader classLoader, final JavaModule module,
					final ProtectionDomain protectionDomain) {
				final Valuable<?> definedField = builder.defineField("VMNAME", String.class,
						Modifier.STATIC | Modifier.PRIVATE);
				return definedField;
			}
		}).transform(new AgentBuilder.Transformer() {

			@Override
			public Builder<?> transform(final Builder<?> builder, final TypeDescription typeDescription,
					final ClassLoader classLoader, final JavaModule module,
					final ProtectionDomain protectionDomain) {
				final Valuable<?> definedField = builder.defineField("CFREGISTRY", ControlFlowRegistry.class,
						Modifier.STATIC | Modifier.FINAL | Modifier.PRIVATE);
				return definedField;
			}
		}).transform(new AgentBuilder.Transformer() {

			@Override
			public Builder<?> transform(final Builder<?> builder, final TypeDescription typeDescription,
					final ClassLoader classLoader, final JavaModule module,
					final ProtectionDomain protectionDomain) {
				final Valuable<?> definedField = builder.defineField("SESSIONREGISTRY", SessionRegistry.class,
						Modifier.STATIC | Modifier.PRIVATE);
				return definedField;
			}
		});
	}

	private static List<KiekerPattern> getExcludedPatterns() {
		final List<KiekerPattern> excludePatterns;
		final String exclusions = System.getenv(InstrumentationEnvironmentVariables.KIEKER_SIGNATURES_EXCLUDE);
		if (exclusions != null) {
			excludePatterns = KiekerPatternUtil.getPatterns(exclusions);
		} else {
			excludePatterns = new LinkedList<>();
		}
		return excludePatterns;
	}
}
