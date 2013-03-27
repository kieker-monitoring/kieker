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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;

import kieker.common.record.controlflow.OperationExecutionRecord;

import livedemo.entities.DataEntry;
import livedemo.entities.Record;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 * @author Bjoern Weissenfels
 */
@ManagedBean(name="chartBean", eager=true)
@SessionScoped
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
	boolean newMethod;
	
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
		this.newMethod = false;
	}
	
	@PostConstruct
	public void init(){
		this.dataBean.addObserver(this);
		for(int i=10; i<=this.maxNumberOfDisplayedEntries;i++){
			this.possibleNumberOfDisplayedEntries.add(i);
		}
		for(int i=1; i<=this.maxIntervallLength;i++){
			this.possibleIntervallLength.add(i);
		}
	}
	
	public void setDataBean(DataBean dataBean){
		this.dataBean = dataBean;
	}
	
	public boolean getNewMethod(){
		System.out.println("try to get newMethod: " + this.newMethod);
		if(this.newMethod){
			this.newMethod = false;
			return false;
		}else{
			return true;
		}
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
			for(int i=0; i < number; i++){
				responsetimes.set(" ",0);
			}
			this.responsetimeModel.addSeries(responsetimes);
			return this.responsetimeModel;
		}
		
		for (String signature : this.getSelectedMethods()){
			List<DataEntry> dataEntries = this.oerMap.get(signature);
			ChartSeries responsetimes = new ChartSeries();  
	        responsetimes.setLabel(this.getMethodName(signature));
	        int fromIndex;
			int toIndex = dataEntries.size() - 1;
			int diff;
			if(toIndex > number){
				fromIndex = toIndex - number;
			}else if(toIndex < number){
				fromIndex = 0;
				diff = number - toIndex;
				for(int i=0; i < diff; i++){
					responsetimes.set(" ", 0);
				}
			}else{
				fromIndex = 0;
			}
	        List<DataEntry> subList = dataEntries.subList(fromIndex, toIndex);
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
			for(int i=0; i < number; i++){
				count.set(" ",0);
			}
			this.countingModel.addSeries(count);
			return this.countingModel;
		}
		for (String signature : this.getSelectedMethods()){
			List<DataEntry> dataEntries = this.oerMap.get(signature);
			ChartSeries count = new ChartSeries();  
			count.setLabel(this.getMethodName(signature));
	        int fromIndex;
			int toIndex = dataEntries.size() - 1;
			int diff;
			if(toIndex > number){
				fromIndex = toIndex - number;
			}else if(toIndex < number){
				fromIndex = 0;
				diff = number - toIndex;
				for(int i=0; i < diff; i++){
					count.set(" ", 0);
				}
			}else{
				fromIndex = 0;
			}
	        List<DataEntry> subList = dataEntries.subList(fromIndex, toIndex);
	        for(int i = 0; i < subList.size(); i++){
	        	count.set(subList.get(i).getMinSec(), subList.get(i).getCount());
	        }
	        this.countingModel.addSeries(count);   
		}
        return this.countingModel;
    }  
	
	public String getMethodName(String signature){
		String[] array = signature.split("\\(");
		array = array[0].split("\\.");
		int end = array.length;
		String result = "..." + array[end-2] + "." + array[end-1] + "(...)";
		return result;
	}
	
	// should be called at the beginning and when duration has changed
	private void generateOERMap(){
		LinkedList<Record> allRecords = this.dataBean.getOERList();
		if(!allRecords.isEmpty()){
			this.oerTimestamp = allRecords.getFirst().getOperationExecutionRecord().getLoggingTimestamp();
			for(Record r : allRecords){
				OperationExecutionRecord oer = r.getOperationExecutionRecord();
				String signature = oer.getOperationSignature();
				if(oer.getLoggingTimestamp() >= this.oerTimestamp){
					do{
						this.oerTimestamp += this.duration;
						for(String sig : this.oerMap.keySet()){
							this.oerMap.get(sig).add(new DataEntry(this.oerTimestamp));
						}
					}
					while(oer.getLoggingTimestamp() >= this.oerTimestamp);
					if(!this.oerMap.containsKey(signature)){
						LinkedList<DataEntry> newData = new LinkedList<DataEntry>();
						DataEntry data = new DataEntry(this.oerTimestamp);
						data.addRecord(r);
						newData.add(data);
						this.oerMap.put(signature, newData);
						this.availableMethods.add(signature);
						this.newMethod = true;
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
						this.newMethod = true;
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
						OperationExecutionRecord oer = r.getOperationExecutionRecord();
						String signature = oer.getOperationSignature();
						if(oer.getLoggingTimestamp() > this.oerTimestamp){
							do{
								this.oerTimestamp += this.duration;
								for(String sig : this.oerMap.keySet()){
									this.oerMap.get(sig).add(new DataEntry(this.oerTimestamp));
								}
							}
							while(oer.getLoggingTimestamp() > this.oerTimestamp);
							if(!this.oerMap.containsKey(signature)){
								LinkedList<DataEntry> newData = new LinkedList<DataEntry>();
								DataEntry data = new DataEntry(this.oerTimestamp);
								data.addRecord(r);
								newData.add(data);
								this.oerMap.put(signature, newData);
								this.availableMethods.add(signature);
								this.newMethod = true;
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
								this.newMethod = true;
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
