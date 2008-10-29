package kieker.tests.helloWorld;
/**
 * Simple example, used in the tutorial for
 * illustration the instrumentation and monitoring
 * capabilities of tpmon.
 * 
 * @author matthias
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