package livedemo.managedbeans;

import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

/**
 * @author Bjoern Weissenfels
 * 
 * @since 1.9
 */
@ManagedBean(name = "jMeterBean")
@ApplicationScoped
public class JMeterBean implements Observer{
	
	private final long TIMEOUT_IN_MILLIS = 120000;
	private final String DEFAULT_BUTTON_TEXT = "Generate Load";
	
	@ManagedProperty(value = "#{analysisBean}")
	private AnalysisBean analysisBean;
		
	private boolean disabled;
	private long timestamp;
	private String buttonText;
	
	public JMeterBean(){
		this.disabled = false;
		this.timestamp = System.currentTimeMillis();
		this.buttonText = this.DEFAULT_BUTTON_TEXT;
	}
	
	@PostConstruct
	public void init() {
		this.analysisBean.getUpdateThread().addObserver(this);
	}

	@PreDestroy
	public void terminate() {
		this.analysisBean.getUpdateThread().deleteObserver(this);
	}
	
	public void setAnalysisBean(final AnalysisBean analysisBean) {
		this.analysisBean = analysisBean;
	}
	
	public String getButtonText(){
		return this.buttonText;
	}
	
	public boolean getDisabled(){
		return this.disabled;
	}
	
	public void runJMeter(){
		synchronized(this){
			long actualtime = System.currentTimeMillis();
			if(actualtime < this.timestamp){
				return;
			}else{
				this.timestamp = actualtime + this.TIMEOUT_IN_MILLIS;
			}
		}
		Runtime runtime = Runtime.getRuntime();
		File dir = new File(new File("").getAbsolutePath()+"/jmeter/bin");
		try {
			Process p = runtime.exec("java -jar ApacheJMeter.jar -n -t Testplan.jmx",null,dir);
			p.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		long actualtime = System.currentTimeMillis();
		if(actualtime > this.timestamp){
			this.buttonText = this.DEFAULT_BUTTON_TEXT;
			this.disabled = false;
		}else{
			this.buttonText = "Generate Load in " + String.valueOf((int)((this.timestamp - actualtime)/1000)) + " s";
			this.disabled = true;
		}
	}

}
