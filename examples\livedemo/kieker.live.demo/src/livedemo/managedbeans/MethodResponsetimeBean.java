package livedemo.managedbeans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

import kieker.analysis.display.XYPlot;
import livedemo.entities.DataEntry;

/**
 * @author Bjoern Weissenfels
 */
@ManagedBean(name="methodResponsetimeBean", eager=true)
@SessionScoped
public class MethodResponsetimeBean implements Observer {
	
	@ManagedProperty(value = "#{analysisBean}")
	private AnalysisBean analysisBean;
	
	private final int displayedEntries = 20;
	private int intervallLength = 2; // in seconds
	private final int maxIntervallLength = 60; // in seconds
	private final List<Integer> possibleIntervallLength;
	
	private final List<String> availableMethods;
	private List<String> selectedMethods;
	private final Map<String,String> signatureMap;
	
	private XYPlot xyplot; // key = shortSignature, value = List<Timestamp,Responsetime>
	private final Map<String, List<DataEntry>> responsetimeMap; // key = shortSignature
	private final CartesianChartModel responsetimeModel;
	
	private long lastTimestamp;
	
	public MethodResponsetimeBean(){
		this.possibleIntervallLength = new ArrayList<Integer>();
		this.availableMethods = new ArrayList<String>();
		this.signatureMap = new ConcurrentHashMap<String, String>();
		this.selectedMethods = new ArrayList<String>();
		this.responsetimeMap = new ConcurrentHashMap<String, List<DataEntry>>();
		this.responsetimeModel = new CartesianChartModel();
	}
	
	@PostConstruct
	public void init(){
		for(int i = 1; i < this.maxIntervallLength; i++){
			this.possibleIntervallLength.add(i);
		}
		this.xyplot = this.analysisBean.getMethodResponsetimeDisplayFilter().getXYPlot();
		for(String key : this.xyplot.getKeys()){
			String signature = this.createShortSignature(key);
			if(!this.availableMethods.contains(signature)){
				this.availableMethods.add(signature);
			}
			this.signatureMap.put(signature, key);
		}
		this.lastTimestamp = System.currentTimeMillis();
		this.analysisBean.getUpdateThread().addObserver(this);
	}
	
	@PreDestroy
	public void terminate(){
		this.analysisBean.getUpdateThread().deleteObserver(this);
	}
	
	public void setAnalysisBean(AnalysisBean analysisBean){
		this.analysisBean = analysisBean;
	}
	
	public int getIntervallLength() {
		return intervallLength;
	}

	public List<Integer> getPossibleIntervallLength() {
		return possibleIntervallLength;
	}

	public List<String> getAvailableMethods() {
		return availableMethods;
	}

	public void setIntervallLength(int intervallLength) {
		this.intervallLength = intervallLength;
	}
	
	public void setSelectedMethods(List<String> selectedMethods){
		this.selectedMethods = selectedMethods;
	}

	public List<String> getSelectedMethods() {
		if(this.selectedMethods.isEmpty() && !this.availableMethods.isEmpty()){
			this.selectedMethods.add(this.availableMethods.get(0));
		}
		return selectedMethods;
	}

	public CartesianChartModel getResponsetimeModel(){
		return this.responsetimeModel;
	}
	
	public synchronized void changeIntervallLength(AjaxBehaviorEvent event){
	//	this.computeNewMap();
	}
	
	private String createShortSignature(String signature){
		String[] array = signature.split("\\(");
		array = array[0].split("\\.");
		int end = array.length;
		String result = "..." + array[end-2] + "." + array[end-1] + "(...)";
		return result;
	}
	
	private double computeResponseTime(long duration){
		return Math.round(duration/100000.0)/10.0;
	}
	
	@Override
	public synchronized void update(Observable o, Object arg) {
		this.responsetimeModel.clear();
		for(String key : this.getSelectedMethods()){
			ChartSeries responsetimes = new ChartSeries();
			responsetimes.setLabel(key);
			String signature = this.signatureMap.get(key);
			Map<Object, Number> map = this.xyplot.getEntries(signature);
			responsetimes.setData(map);
			this.responsetimeModel.addSeries(responsetimes);
		}
		for(String key : this.xyplot.getKeys()){
			if(null == this.signatureMap.get(key)){
				String signature = this.createShortSignature(key);
				if(!this.availableMethods.contains(signature)){
					this.availableMethods.add(signature);
				}
				this.signatureMap.put(signature, key);
			}
			
		}
		
	}

}
