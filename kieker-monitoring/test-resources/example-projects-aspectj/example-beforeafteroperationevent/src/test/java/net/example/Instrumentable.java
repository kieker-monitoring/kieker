package net.example;

public class Instrumentable {
	
	public Instrumentable() {
		
	}
	
	public void callee() {
		callee2("this is a test!");
	}
	
	public void callee2(String someParameter) {
		
	}
	
	public void throwingCallee() {
		callee2("should be contained");
		throw new RuntimeException("Test");
	}
}
