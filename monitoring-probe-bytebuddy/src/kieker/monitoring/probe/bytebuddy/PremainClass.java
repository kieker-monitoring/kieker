package kieker.monitoring.probe.bytebuddy;

import java.lang.instrument.Instrumentation;
import java.util.LinkedList;
import java.util.List;

import kieker.monitoring.core.signaturePattern.InvalidPatternException;
import kieker.monitoring.core.signaturePattern.PatternParser;
import kieker.monitoring.probe.javassist.KiekerClassTransformer;
import kieker.monitoring.util.KiekerPattern;
import kieker.monitoring.util.KiekerPatternUtil;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

public class PremainClass {
	private static final AgentBuilder.Listener ONLY_ERROR_LOGGER = new AgentBuilder.Listener() {
		@Override
		public void onDiscovery(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
		}

		@Override
		public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module,
				boolean loaded, DynamicType dynamicType) {
		}

		@Override
		public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module,
				boolean loaded) {
		}

		@Override
		public void onError(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded,
				Throwable throwable) {
			throwable.printStackTrace();
		}

		@Override
		public void onComplete(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
			
		}
		
	};
	
	public static void premain(String agentArgs, Instrumentation inst) {
		System.out.println("Starting instrumentation...");
		
		String instrumentables = System.getenv("KIEKER_SIGNATURES");
		if (instrumentables != null) {
			final List<KiekerPattern> patternObjects = KiekerPatternUtil.getPatterns(instrumentables);
			new AgentBuilder.Default()
			.with(ONLY_ERROR_LOGGER)
	        .type(new ElementMatcher<TypeDescription>() {
				@Override
				public boolean matches(TypeDescription target) {
					String typeName = target.getTypeName();
					return KiekerPatternUtil.classIsContained(patternObjects, typeName);
				}
	        })
	        .transform(new AgentBuilder.Transformer.ForAdvice()
	                .advice(
	                        new ElementMatcher<MethodDescription>() {

								public boolean matches(MethodDescription target) {
									String typeName = target.getDeclaringType().getTypeName();
									if (typeName.startsWith("java.net.") || typeName.startsWith("org.junit.")) {
										return false;
									} else if (typeName.startsWith("example.kieker")) {
										System.out.println("Instrumenting: " + typeName);
										return true;
									}
									return false;
								}
	                        	
	                        },
	                        OperationExecutionAdvice.class.getName()
	                ))
	        .installOn(inst);
		} else {
			System.err.println("Environment variable KIEKER_SIGNATURES not defined - not instrumenting anything!");
		}
		
		
		
//		new AgentBuilder.Default().with(new AgentBuilder.Listener() {
//
//			@Override
//			public void onDiscovery(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module,
//					boolean loaded, DynamicType dynamicType) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module,
//					boolean loaded) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void onError(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded,
//					Throwable throwable) {
//				throwable.printStackTrace();
//				
//			}
//
//			@Override
//			public void onComplete(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
//				
//			}
//			
//		}) .type(ElementMatchers.any()).transform((builder, type, classLoader, module,
//				protectionDomain) -> builder.method(new ElementMatcher<MethodDescription>() {
//
//					@Override
//					public boolean matches(MethodDescription target) {
//						System.out.println("Checking2: " + target);
//						return true;
//					}
//
//				}).
//				visit(Advice.to(OperationExecutionAdvice.class))
//				.installOn(inst);
//		new AgentBuilder.Default().with(new AgentBuilder.Listener() {
//
//			@Override
//			public void onDiscovery(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module,
//					boolean loaded, DynamicType dynamicType) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module,
//					boolean loaded) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void onError(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded,
//					Throwable throwable) {
//				throwable.printStackTrace();
//				
//			}
//
//			@Override
//			public void onComplete(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
//				
//			}
//			
//		}) .type(ElementMatchers.any()).transform((builder, type, classLoader, module,
//				protectionDomain) -> builder.method(new ElementMatcher<MethodDescription>() {
//
//					@Override
//					public boolean matches(MethodDescription target) {
//						System.out.println("Checking2: " + target);
//						return true;
//					}
//
//				}).intercept(MethodDelegation.to(OperationExecutionAdvice.class)))
//				.installOn(inst);
	}
	
}
