.. _instrumenting-software-java-spring:

Instrumentation of Spring Applications
======================================

Depending on the circumstances, spring applications can be monitored with different configurations:
- Spring interceptors can be used if solely spring endpoints should be monitored. This is described in :ref:`_tutorial-servlet-example`
- Spring applications **without** Sprinng Boot can be instrumented as any other Java application. This is described in :ref:`_gt-aspectj-instrumentation-example`
- Spring applications **with** Spring Boot use a different classloader. Therefore, their instrumentation is described here.

# Instrumenting Spring Boot Applications

The instrumentation of Spring Boot requires 3 steps:
- Specification of the instrumentation aspects in the `aop.xml` (equivalent to :ref:`_gt-aspectj-instrumentation-example`)
- Adaption of the configuration: To adapt the configuration, the annotation `@EnableLoadTimeWeaving` needs to be added to the spring config. Furthermore, it needs to be specified that the `InstrumentationLoadTimeWeaver` is used. Therefore, an example configuration can look like this:
```
@Configuration
@EnableLoadTimeWeaving
public class AppConfig implements LoadTimeWeavingConfigurer {

	@Override
	public LoadTimeWeaver getLoadTimeWeaver() {
		return new InstrumentationLoadTimeWeaver();
	}
}
```
- Configuring the CLI call: While regular applications are called using `java -javaagent:kieker-XX-aspectj.jar`, Spring Boot applications should not be instrumented directly, but instead, the things loaded by the Spring Boot classloader should be instrumented. Therefore, `spring-instrument-XXX.jar` needs to be added as an agent **before** the Kieker agent. Furthermore, the default aop configuration should be disabled for multi-classloader environments, using `-Dkieker.monitoring.skipDefaultAOPConfiguration=true`. Overall, the call will look like `java -Dkieker.monitoring.skipDefaultAOPConfiguration=true -javaagent:spring-instrument-XX.jar -javaagent:lib/kieker-XX-aspectj.jar -jar XX.jar`

A full example can be found in https://github.com/kieker-monitoring/kieker/tree/main/examples/monitoring/probe-spring-boot
