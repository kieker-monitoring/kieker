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
	
	private boolean disabled;
	
	public JMeterBean(){
		this.disabled = false;
	}
	
	public boolean getDisabled(){
		return this.disabled;
	}
	
	public void change(){
		this.disabled = !this.disabled;
	}
	
	public synchronized void runJMeter(){ 
		this.disabled = true;
		System.out.println("---disabled=true---");
		Runtime runtime = Runtime.getRuntime();
		File dir = new File(new File("").getAbsolutePath()+"/jmeter/bin");
		try {
			Process p = runtime.exec("java -jar ApacheJMeter.jar -n -t Testplan.jmx",null,dir);
			p.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		this.disabled = false;
		System.out.println("---disabled=false---" + runtime.availableProcessors());
	}

}
