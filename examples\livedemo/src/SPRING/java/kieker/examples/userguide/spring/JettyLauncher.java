package kieker.examples.userguide.spring;

import org.springframework.context.support.FileSystemXmlApplicationContext;

public class JettyLauncher {

	public static void main(String[] args) {
		FileSystemXmlApplicationContext ctx = new FileSystemXmlApplicationContext("src/SPRING/webapp/jetty.xml");
	}

}
