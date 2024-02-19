package kieker.monitoring.probe.bytebuddy;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.core.registry.SessionRegistry;
import kieker.monitoring.timer.ITimeSource;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.dynamic.DynamicType.Unloaded;
import net.bytebuddy.matcher.ElementMatchers;

public class BuildTimeAdaption {
	public static void main(String[] args) throws MalformedURLException, ClassNotFoundException {
		
		File file = new File(args[0]);
		URL url = file.toURI().toURL();         
	    URL[] urls = new URL[]{url};
		
		try (URLClassLoader urlClassLoader = new URLClassLoader(urls)) {
			Class<?> classUnderMonitoring = urlClassLoader.loadClass("moobench.application.MonitoredClassSimple");
			
			Unloaded<?> unloaded = new ByteBuddy().redefine(classUnderMonitoring)
				.defineField("CTRLINST", IMonitoringController.class, Modifier.STATIC | Modifier.FINAL | Modifier.PRIVATE)
				.defineField("TIME", ITimeSource.class, Modifier.STATIC | Modifier.FINAL | Modifier.PRIVATE)
				.defineField("VMNAME", String.class, Modifier.STATIC | Modifier.FINAL | Modifier.PRIVATE)
				.defineField("CFREGISTRY", ControlFlowRegistry.class, Modifier.STATIC | Modifier.FINAL | Modifier.PRIVATE)
				.defineField("SESSIONREGISTRY", SessionRegistry.class, Modifier.STATIC | Modifier.FINAL | Modifier.PRIVATE)
			    .visit(Advice.to(OperationExecutionAdvice.class).on(ElementMatchers.isMethod()))
				.make();
			
			System.out.println(unloaded.getBytes());
		} catch (MalformedURLException e) {
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
