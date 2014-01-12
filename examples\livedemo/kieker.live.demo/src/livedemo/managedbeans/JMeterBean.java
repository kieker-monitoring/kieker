package livedemo.managedbeans;

import java.util.Observable;
import java.util.Observer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.apache.jmeter.JMeter;

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
	private final JMeter JMETER = new JMeter();
	
	@ManagedProperty(value = "#{analysisBean}")
	private AnalysisBean analysisBean;
		
	private boolean disabled;
	private long timestamp;
	private String buttonText;
	private String[] arguments;
	
	public JMeterBean(){
		this.disabled = false;
		this.timestamp = System.currentTimeMillis();
		this.buttonText = this.DEFAULT_BUTTON_TEXT;
	}
	
	@PostConstruct
	public void init() {
		this.analysisBean.getUpdateThread().addObserver(this);
		String userDir = System.getProperty("user.dir");
		String fileSeparator = System.getProperty("file.separator");
		String bin = "webapps" + fileSeparator + "root" + fileSeparator + "WEB-INF" + fileSeparator + "bin";
		String newUserDir = userDir + fileSeparator + bin;
		System.setProperty("user.dir", newUserDir);
		String testplan = bin + fileSeparator + "Testplan.jmx";
		this.arguments = new String[]{"-n", "-t", testplan};
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
		this.JMETER.start(this.arguments);
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
