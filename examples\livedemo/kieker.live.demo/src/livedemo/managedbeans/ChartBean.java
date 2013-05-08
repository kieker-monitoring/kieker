package livedemo.managedbeans;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import livedemo.entities.DataEntry;
import livedemo.entities.Record;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 * @author Bjoern Weissenfels
 */
@ManagedBean(name="chartBean", eager=true)
@ViewScoped
public class ChartBean implements Observer{
	
	int numberOfDisplayedEntries = 12;
	final int maxNumberOfDisplayedEntries = 25;
	int intervallLength = 3; // in seconds
	final int maxIntervallLength = 60; // in seconds
	long duration = 3 * 1000000000L; // duration for collecting method response times in nanos (intervallLength * 1.000.000.000)
	List<Integer> possibleNumberOfDisplayedEntries;
	List<Integer> possibleIntervallLength;
	
	CartesianChartModel countingModel;
	CartesianChartModel responsetimeModel;
	Map<String, LinkedList<DataEntry>> oerMap; // Map of Signatures and corresponding DataEntries
	List<String> availableMethods;
	List<String> selectedMethods;

	long oerTimestamp; // temp var in nanos
	long lastTimestamp;
	boolean firstCall;
	
	@ManagedProperty(value = "#{dataBean}")
	DataBean dataBean;
	
	public ChartBean(){
		this.countingModel = new CartesianChartModel();
		this.responsetimeModel = new CartesianChartModel();
		this.selectedMethods = new ArrayList<String>();
		this.oerMap = new HashMap<String, LinkedList<DataEntry>>();
		this.possibleNumberOfDisplayedEntries = new ArrayList<Integer>();
		this.possibleIntervallLength = new ArrayList<Integer>();
		
		this.oerTimestamp = new Date().getTime() * 1000000;
		this.lastTimestamp = System.currentTimeMillis();
		this.availableMethods = new ArrayList<String>();
		this.firstCall = true;
	}
	
	@PostConstruct
	public void init(){
		this.dataBean.addObserver(this);
		System.out.println("View gestartet");
		for(int i=10; i<=this.maxNumberOfDisplayedEntries;i++){
			this.possibleNumberOfDisplayedEntries.add(i);
		}
		for(int i=1; i<=this.maxIntervallLength;i++){
			this.possibleIntervallLength.add(i);
		}
	}
	
	@PreDestroy
	public void cleanup(){
		this.dataBean.deleteObserver(this);
	}
	
	public void setDataBean(DataBean dataBean){
		this.dataBean = dataBean;
	}

	public List<Integer> getPossibleNumberOfDisplayedEntries(){
		return this.possibleNumberOfDisplayedEntries;
	}
	
	public List<String> getSelectedMethods(){
		if(this.selectedMethods.isEmpty() && !this.availableMethods.isEmpty()){
			this.selectedMethods.add(this.availableMethods.get(0));
		}
		return this.selectedMethods;
	}
	
	public void setSelectedMethods(List<String> methods){
		this.selectedMethods = methods;
	}
	
	public int getNumberOfDisplayedEntries(){
		return this.numberOfDisplayedEntries;
	}
	
	public void setNumberOfDisplayedEntries(int newNumber){
		this.numberOfDisplayedEntries = newNumber;
	}
	
	public List<Integer> getPossibleIntervallLength(){
		return this.possibleIntervallLength;
	}
	
	public int getIntervallLength(){
		return this.intervallLength;
	}
	
	public void setIntervallLength(int length){
		this.intervallLength = length;
	}
	
	public CartesianChartModel getCountingModel() {
		this.dataBean.updateOERList();
		return getCountingModel(this.numberOfDisplayedEntries);
	}
		
	public CartesianChartModel getResponsetimeModel() {
		return getResponsetimeModel(this.numberOfDisplayedEntries);
	}
	
	public List<String> getAvailableMethods(){
		return this.availableMethods;
	}
	
	public synchronized void changeIntervallLength(AjaxBehaviorEvent event){
		this.duration = this.intervallLength * 1000000000L;
		this.oerMap.clear();
		this.availableMethods.clear();
		this.generateOERMap();
	}
	
	private CartesianChartModel getResponsetimeModel(int number) {
		this.responsetimeModel.clear();
		if(this.getSelectedMethods().isEmpty()){
			ChartSeries responsetimes = new ChartSeries();
			responsetimes.setLabel("choose method");
			long time = System.currentTimeMillis() * 1000000;
			for(int i=0; i < number; i++){
				responsetimes.set(computeTime(time-this.duration*(number-i)),0);
			}
			this.responsetimeModel.addSeries(responsetimes);
			return this.responsetimeModel;
		}
		
		for (String signature : this.getSelectedMethods()){
			List<DataEntry> dataEntries = this.oerMap.get(signature);
			ChartSeries responsetimes = new ChartSeries();  
	        responsetimes.setLabel(signature);
	        int fromIndex;
			int toIndex = dataEntries.size() - 1;
			int diff = 0;
			if(toIndex <= 0){
				long time = System.currentTimeMillis() * 1000000;
				for(int i=0; i < number; i++){
					responsetimes.set(computeTime(time-this.duration*(number-i)),0);
				}
				this.responsetimeModel.addSeries(responsetimes);
				return this.responsetimeModel;
			}else if(toIndex > number){
				fromIndex = toIndex - number;
			}else if(toIndex < number){
				fromIndex = 0;
				diff = number - toIndex;
			}else{
				fromIndex = 0;
			}
	        List<DataEntry> subList = dataEntries.subList(fromIndex, toIndex);
	        long time = subList.get(0).getTimestamp();
			for(int i=0; i < diff; i++){
				responsetimes.set(computeTime(time-this.duration*(number-i)),0);
			}
	        for(int i = 0; i < subList.size(); i++){
	        	responsetimes.set(subList.get(i).getMinSec(), subList.get(i).getAverageResponsetime());
	        }
	        this.responsetimeModel.addSeries(responsetimes);   
		}
        return this.responsetimeModel;
	}
	
	private CartesianChartModel getCountingModel(int number) {
		this.countingModel.clear();
		if(this.getSelectedMethods().isEmpty()){
			ChartSeries count = new ChartSeries();
			count.setLabel("choose method");
			long time = System.currentTimeMillis() * 1000000;
			for(int i=0; i < number; i++){
				count.set(computeTime(time-this.duration*(number-i)),0);
			}
			this.countingModel.addSeries(count);
			return this.countingModel;
		}
		for (String signature : this.getSelectedMethods()){
			List<DataEntry> dataEntries = this.oerMap.get(signature);
			ChartSeries count = new ChartSeries();  
			count.setLabel(signature);
	        int fromIndex;
			int toIndex = dataEntries.size() - 1;
			int diff = 0;
			if(toIndex <= 0){
				long time = System.currentTimeMillis() * 1000000;
				for(int i=0; i < number; i++){
					count.set(computeTime(time-this.duration*(number-i)),0);
				}
				this.countingModel.addSeries(count);
				return this.countingModel;
			}else if(toIndex > number){
				fromIndex = toIndex - number;
			}else if(toIndex < number){
				fromIndex = 0;
				diff = number - toIndex;	
			}else{
				fromIndex = 0;
			}
	        List<DataEntry> subList = dataEntries.subList(fromIndex, toIndex);
	        long time = subList.get(0).getTimestamp();
	        for(int i=0; i < diff; i++){
				count.set(computeTime(time-this.duration*(diff-i)), 0);
			}
	        for(int i = 0; i < subList.size(); i++){
	        	count.set(subList.get(i).getMinSec(), subList.get(i).getCount());
	        }
	        this.countingModel.addSeries(count);   
		}
        return this.countingModel;
    }  
	
	private String computeTime(long timestamp){
		Date date = new Date(timestamp/1000000);
		return date.toString().substring(14, 19);
	}
		
	// should be called at the beginning and when duration has changed
	private void generateOERMap(){
		LinkedList<Record> allRecords = this.dataBean.getOERList();
		if(!allRecords.isEmpty()){
			this.oerTimestamp = allRecords.getFirst().getOperationExecutionRecord().getLoggingTimestamp();
			for(Record r : allRecords){
				String signature = r.getShortSignature();
				if(r.getOperationExecutionRecord().getLoggingTimestamp() >= this.oerTimestamp){
					do{
						this.oerTimestamp += this.duration;
						for(String sig : this.oerMap.keySet()){
							this.oerMap.get(sig).add(new DataEntry(this.oerTimestamp));
						}
					}
					while(r.getOperationExecutionRecord().getLoggingTimestamp() >= this.oerTimestamp);
					if(!this.oerMap.containsKey(signature)){
						LinkedList<DataEntry> newData = new LinkedList<DataEntry>();
						DataEntry data = new DataEntry(this.oerTimestamp);
						data.addRecord(r);
						newData.add(data);
						this.oerMap.put(signature, newData);
						this.availableMethods.add(signature);
					}else{
						LinkedList<DataEntry> entries = this.oerMap.get(signature);
						entries.getLast().addRecord(r);
					}
				}else{
					if(this.oerMap.containsKey(signature)){
						LinkedList<DataEntry> entries = this.oerMap.get(signature);
						entries.getLast().addRecord(r);
					}else{
						LinkedList<DataEntry> newData = new LinkedList<DataEntry>();
						DataEntry data = new DataEntry(this.oerTimestamp);
						data.addRecord(r);
						newData.add(data);
						this.oerMap.put(signature, newData);
						this.availableMethods.add(signature);
					}
				}	
			}
		}
		this.lastTimestamp = System.currentTimeMillis();
		if((this.oerTimestamp + this.duration)/1000000 < this.lastTimestamp){
			do{
				
				this.oerTimestamp += this.duration;
				for(String signature : this.oerMap.keySet()){
					this.oerMap.get(signature).add(new DataEntry(this.oerTimestamp));
				}
			}while((this.oerTimestamp + this.duration)/1000000 < this.lastTimestamp);
		}
		
	}

	@Override
	public synchronized void update(Observable arg0, Object message) {
		if("oer".equals(message)){
			if(this.firstCall){
				this.generateOERMap();
				this.firstCall = false;
			}else{
				List<Record> newRecords = this.dataBean.getNewOEREntries();
				if(newRecords.isEmpty()){
					long currentTime = System.currentTimeMillis();
					if(this.lastTimestamp + (this.duration/1000000) < currentTime){
						do{
							this.oerTimestamp += this.duration;
							for(String signature : this.oerMap.keySet()){
								this.oerMap.get(signature).add(new DataEntry(this.oerTimestamp));
							}
							this.lastTimestamp += this.duration/1000000;
						}while(this.lastTimestamp + (this.duration/1000000) < currentTime);
					}
				}else{
					for(Record r : newRecords){
						String signature = r.getShortSignature();
						if(r.getOperationExecutionRecord().getLoggingTimestamp() > this.oerTimestamp){
							do{
								this.oerTimestamp += this.duration;
								for(String sig : this.oerMap.keySet()){
									this.oerMap.get(sig).add(new DataEntry(this.oerTimestamp));
								}
							}
							while(r.getOperationExecutionRecord().getLoggingTimestamp() > this.oerTimestamp);
							if(!this.oerMap.containsKey(signature)){
								LinkedList<DataEntry> newData = new LinkedList<DataEntry>();
								DataEntry data = new DataEntry(this.oerTimestamp);
								data.addRecord(r);
								newData.add(data);
								this.oerMap.put(signature, newData);
								this.availableMethods.add(signature);
							}else{
								LinkedList<DataEntry> entries = this.oerMap.get(signature);
								entries.getLast().addRecord(r);
							}
						}else{
							if(this.oerMap.containsKey(signature)){
								LinkedList<DataEntry> entries = this.oerMap.get(signature);
								entries.getLast().addRecord(r);
							}else{
								LinkedList<DataEntry> newData = new LinkedList<DataEntry>();
								DataEntry data = new DataEntry(this.oerTimestamp);
								data.addRecord(r);
								newData.add(data);
								this.oerMap.put(signature, newData);
								this.availableMethods.add(signature);
							}
						}	
					}
					this.lastTimestamp = System.currentTimeMillis();
					// remove old values
					for(String signature : this.oerMap.keySet()){
						LinkedList<DataEntry> list = this.oerMap.get(signature);
						int removeFirst = list.size() - this.maxNumberOfDisplayedEntries - 1;
						for(int i=0; i < removeFirst; i++){
							list.removeFirst();
						}
					}
				}
			}
		}
		
	}

}
