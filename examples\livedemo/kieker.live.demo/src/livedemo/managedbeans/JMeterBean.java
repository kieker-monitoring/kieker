package livedemo.managedbeans;

import java.io.File;
import java.io.IOException;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 * @author Bjoern Weissenfels
 * 
 * @since 1.9
 */
@ManagedBean(name = "jMeterBean")
@ApplicationScoped
public class JMeterBean {
	
	private void runJMeter(){
		Runtime runtime = Runtime.getRuntime();
		File dir = new File("/jmeter/bin");
		try {
			runtime.exec("java -jar ApacheJMeter.jar -n -t Testplan.jmx",null,dir);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
