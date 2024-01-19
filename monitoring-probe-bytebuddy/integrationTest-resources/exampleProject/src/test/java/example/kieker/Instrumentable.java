package example.kieker;

public class Instrumentable {
	public static void staticMethod() {
		System.out.println("Static method is called");
		staticMethod2();
	}
	
	public static void staticMethod2() {
		System.out.println("Static method2 is called");
	}
}