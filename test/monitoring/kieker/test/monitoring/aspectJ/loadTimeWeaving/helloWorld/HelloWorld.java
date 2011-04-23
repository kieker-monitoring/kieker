package kieker.test.monitoring.aspectJ.loadTimeWeaving.helloWorld;
/**
 * Simple example, used in the tutorial for
 * illustration the instrumentation and monitoring
 * capabilities of tpmon.
 * 
 * @author Matthias Rohr
 */
public class HelloWorld {
	public static void main (String args[]) {
		System.out.println("Hello");
		doSomething();
	}

	public static void doSomething(){
		System.out.println("doing something");
	}
}        