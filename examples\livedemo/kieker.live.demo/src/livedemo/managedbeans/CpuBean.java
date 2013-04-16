package livedemo.managedbeans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

import kieker.common.record.system.CPUUtilizationRecord;
import kieker.common.record.system.MemSwapUsageRecord;
import livedemo.entities.DataEntry;
import livedemo.entities.Model;

/**
 * @author Bjoern Weissenfels
 */
@ManagedBean(name="cpuBean", eager=true)
@SessionScoped
public class CpuBean implements Observer{
	
	@ManagedProperty(value = "#{dataBean}")
	DataBean dataBean;
	
	int numberOfDisplayedEntries = 12; // 12 is the default start value
	final int maxNumberOfDisplayedEntries = 25;
	int intervallLength = 3; // in seconds; 3 is the default start value
	final int maxIntervallLength = 60; // in seconds
	long duration = 3 * 1000000000L; // in nanos (intervallLength * 1.000.000.000)
	List<Integer> possibleNumberOfDisplayedEntries;
	List<Integer> possibleIntervallLength;
	
	Map<String, LinkedList<DataEntry>> cpuMap; // key = hostname + " - " + cpuId
	final String[] possibleCpuAttributes = {"user", "system", "wait", "nice", "irq", "totalUtilization", "idle"};
	List<String> selectedCpuAttributes;
	List<String> possibleCPUs;
	List<String> selectedCPUs;
	List<Model> cpuModels;
	long cpuTimestamp;
	boolean firstCpuCall;
	
	Map<String, LinkedList<DataEntry>> memSwapMap; // key = hostname
	List<String> possibleHosts;
	List<String> selectedHosts;
	List<Model> memSwapModels;
	long memSwapTimestamp;
	boolean firstMemSwapCall;
	
	public CpuBean(){
		this.possibleNumberOfDisplayedEntries = new ArrayList<Integer>();
		this.possibleIntervallLength = new ArrayList<Integer>();
		
		this.cpuMap = new HashMap<String, LinkedList<DataEntry>>();
		this.selectedCpuAttributes = new ArrayList<String>();
		this.possibleCPUs = new ArrayList<String>();
		this.selectedCPUs = new ArrayList<String>();
		this.cpuModels = new ArrayList<Model>();
		this.cpuTimestamp = new Date().getTime() * 1000000;
		this.firstCpuCall = true;
		
		this.memSwapMap = new HashMap<String, LinkedList<DataEntry>>();
		this.possibleHosts = new ArrayList<String>();
		this.selectedHosts = new ArrayList<String>();
		this.memSwapModels = new ArrayList<Model>();
		this.memSwapTimestamp = this.cpuTimestamp;
		this.firstMemSwapCall = true;
	}
	
	@PostConstruct
	public void init() {
		this.dataBean.addObserver(this);
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
	
	// getter and setter
	public void setDataBean(DataBean dataBean){
		this.dataBean = dataBean;
	}
	
	public List<Integer> getPossibleNumberOfDisplayedEntries(){
		return this.possibleNumberOfDisplayedEntries;
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
		
	// getter and setter for CPU variables
	public String[] getPossibleCpuAttributes(){
		return this.possibleCpuAttributes;
	}
	
	public List<String> getSelectedCpuAttributes() {
		if(this.selectedCpuAttributes.isEmpty()){
			this.selectedCpuAttributes.add("system");
			this.selectedCpuAttributes.add("totalUtilization");
		}
		return this.selectedCpuAttributes;
	}
	
	public void setSelectedCpuAttributes(List<String> selectedAttributes){
		this.selectedCpuAttributes = selectedAttributes;
	}
	
	public List<String> getPossibleCPUs(){
		if(this.possibleCPUs.isEmpty()){
			// this call will update the map, after this all cpuIds are known
			this.dataBean.updateCPUList();  
			Set<String> ids = this.cpuMap.keySet();
			this.possibleCPUs.clear();
			
			for(String s : ids){
				this.possibleCPUs.add(s);
			}
			Collections.sort(this.possibleCPUs);
		}
		return this.possibleCPUs;
	}
	
	public List<String> getSelectedCPUs(){
		if(this.selectedCPUs.isEmpty() && !this.possibleCPUs.isEmpty()){
			this.selectedCPUs.addAll(this.possibleCPUs);
		}
		return this.selectedCPUs;
	}
	
	public void setSelectedCPUs(List<String> selectedCPUs){
		this.selectedCPUs = selectedCPUs;
	}
	
	public List<Model> getCpuModels(){
		this.dataBean.updateCPUList();
		this.cpuModels.clear();
		for (String id : this.selectedCPUs){
			Model model = new Model(this.getCpuModel(this.numberOfDisplayedEntries, id),id);
			this.cpuModels.add(model);
		}
		return this.cpuModels;
	}
	
	// getter and setter for MemSwap variables
	public List<String> getPossibleHosts(){
		if(this.possibleHosts.isEmpty()){
			// this call will update the map, after this all cpuIds are known
			this.dataBean.updateMemSwapList();  
			Set<String> ids = this.memSwapMap.keySet();
			this.possibleHosts.clear();
			for(String s : ids){
				this.possibleHosts.add(s);
			}
		}
		return this.possibleHosts;
	}
	
	public List<String> getSelectedHosts(){
		if(this.selectedHosts.isEmpty() && !this.possibleHosts.isEmpty()){
			this.selectedHosts.add(this.possibleHosts.get(0));
		}
		return this.selectedHosts;
	}
	
	public void setSelectedHosts(List<String> selectedHosts){
		this.selectedHosts = selectedHosts;
	}
	
	public List<Model> getMemSwapModels(){
		this.dataBean.updateMemSwapList();
		this.memSwapModels.clear();
		for (String hostname : this.getSelectedHosts()){
			this.memSwapModels.add(this.getMemSwapModel(this.numberOfDisplayedEntries, hostname));
		}
		return this.memSwapModels;
	}

	// EventListener
	public synchronized void changeIntervallLength(AjaxBehaviorEvent event){
		this.duration = this.intervallLength * 1000000000L;
		this.cpuMap.clear();
		this.memSwapMap.clear();
		this.generateCpuMap();
		this.generateMemSwapMap();
	}
	
	private CartesianChartModel getCpuModel(int numberOfDisplayedEntries, String id) {
		CartesianChartModel cpuModel = new CartesianChartModel();
		List<DataEntry> cpuList = this.cpuMap.get(id);
		int fromIndex;
		int toIndex = cpuList.size() - 1;
		if(toIndex > numberOfDisplayedEntries){
			fromIndex = toIndex - numberOfDisplayedEntries;
		}else{
			fromIndex = 0;
		}
        List<DataEntry> subList = cpuList.subList(fromIndex, toIndex);
        
		for (String attribute : this.getSelectedCpuAttributes()){
			ChartSeries cpuSeries = new ChartSeries();  
	        cpuSeries.setLabel(attribute);
	        
	        if("user".equals(attribute)){
	        	for(int i = 0; i < subList.size(); i++){
		        	cpuSeries.set(subList.get(i).getMinSec(), subList.get(i).getAverageUser());
		        }
	        }else if("system".equals(attribute)){
	        	for(int i = 0; i < subList.size(); i++){
		        	cpuSeries.set(subList.get(i).getMinSec(), subList.get(i).getAverageSystem());
		        }
	        }else if("wait".equals(attribute)){
	        	for(int i = 0; i < subList.size(); i++){
		        	cpuSeries.set(subList.get(i).getMinSec(), subList.get(i).getAverageWait());
		        }
	        }else if("nice".equals(attribute)){
	        	for(int i = 0; i < subList.size(); i++){
		        	cpuSeries.set(subList.get(i).getMinSec(), subList.get(i).getAverageNice());
		        }
	        }else if("irq".equals(attribute)){
	        	for(int i = 0; i < subList.size(); i++){
		        	cpuSeries.set(subList.get(i).getMinSec(), subList.get(i).getAverageIrq());
		        }
	        }else if("totalUtilization".equals(attribute)){
	        	for(int i = 0; i < subList.size(); i++){
		        	cpuSeries.set(subList.get(i).getMinSec(), subList.get(i).getAverageTotalUtilization());
		        }
	        }else if("idle".equals(attribute)){
	        	for(int i = 0; i < subList.size(); i++){
		        	cpuSeries.set(subList.get(i).getMinSec(), subList.get(i).getAverageIdle());
		        }
	        }
	        cpuModel.addSeries(cpuSeries);   
		}
        return cpuModel;
	}
	
	private Model getMemSwapModel(int numberOfDisplayedEntries, String id) {
		CartesianChartModel memModel = new CartesianChartModel();
		CartesianChartModel swapModel = new CartesianChartModel();
	
		List<DataEntry> memSwapList = this.memSwapMap.get(id);
		int fromIndex;
		int toIndex = memSwapList.size() - 1;
		if(toIndex > numberOfDisplayedEntries){
			fromIndex = toIndex - numberOfDisplayedEntries;
		}else{
			fromIndex = 0;
		}
        List<DataEntry> subList = memSwapList.subList(fromIndex, toIndex);
        
	    ChartSeries memUsedSeries = new ChartSeries();  
	    memUsedSeries.setLabel("Used");
		for(int i = 0; i < subList.size(); i++){
			memUsedSeries.set(subList.get(i).getMinSec(), subList.get(i).getAverageMemUsed());
		}
	    memModel.addSeries(memUsedSeries);
	    
	    ChartSeries memFreeSeries = new ChartSeries();  
	    memFreeSeries.setLabel("Free");
		for(int i = 0; i < subList.size(); i++){
			memFreeSeries.set(subList.get(i).getMinSec(), subList.get(i).getAverageMemFree());
		}
	    memModel.addSeries(memFreeSeries);
	    
	    ChartSeries swapUsedSeries = new ChartSeries();  
	    swapUsedSeries.setLabel("Used");
		for(int i = 0; i < subList.size(); i++){
			swapUsedSeries.set(subList.get(i).getMinSec(), subList.get(i).getAverageSwapUsed());
		}
	    swapModel.addSeries(swapUsedSeries);
	    
	    ChartSeries swapFreeSeries = new ChartSeries();  
	    swapFreeSeries.setLabel("Free");
		for(int i = 0; i < subList.size(); i++){
			swapFreeSeries.set(subList.get(i).getMinSec(), subList.get(i).getAverageSwapFree());
		}
	    swapModel.addSeries(swapFreeSeries);
		
        return new Model(memModel, swapModel, id);
	}
	
	// should be called at the beginning and when duration has changed
	private void generateCpuMap(){
		LinkedList<CPUUtilizationRecord> allRecords = this.dataBean.getCpuList();
		for(CPUUtilizationRecord r : allRecords){
			String key = r.getHostname() + " - " + r.getCpuID();
			if(null == this.cpuMap.get(key)){
				this.cpuMap.put(key, new LinkedList<DataEntry>());;
			}else{
				this.firstCpuCall = false;
				break;
			}
		}
		this.cpuTimestamp = allRecords.getFirst().getLoggingTimestamp();
		for(CPUUtilizationRecord r : allRecords){
			String key = r.getHostname() + " - " + r.getCpuID();
			if(r.getLoggingTimestamp() >= this.cpuTimestamp){
				this.cpuTimestamp += this.duration;
				for(String id : this.cpuMap.keySet()){
					this.cpuMap.get(id).add(new DataEntry(this.cpuTimestamp));
				}
				this.cpuMap.get(key).getLast().addCPUUtilizationRecord(r);
			}else{
				this.cpuMap.get(key).getLast().addCPUUtilizationRecord(r);
			}
		}
	}
	
	private void generateMemSwapMap() {
		LinkedList<MemSwapUsageRecord> allRecords = this.dataBean.getMemSwapList();
		for(MemSwapUsageRecord r : allRecords){
			String key = r.getHostname();
			if(null == this.memSwapMap.get(key)){
				this.memSwapMap.put(key, new LinkedList<DataEntry>());;
			}else{
				this.firstMemSwapCall = false;
				break;
			}
		}
		this.memSwapTimestamp = allRecords.getFirst().getLoggingTimestamp();
		for(MemSwapUsageRecord r : allRecords){
			String key = r.getHostname();
			if(r.getLoggingTimestamp() >= this.memSwapTimestamp){
				this.memSwapTimestamp += this.duration;
				for(String id : this.memSwapMap.keySet()){
					this.memSwapMap.get(id).add(new DataEntry(this.memSwapTimestamp));
				}
				this.memSwapMap.get(key).getLast().addMemSwapUsageRecord(r);
			}else{
				this.memSwapMap.get(key).getLast().addMemSwapUsageRecord(r);
			}
		}
	}


	@Override
	public synchronized void update(Observable arg0, Object message) {
		if("cpu".equals(message)){
			if(this.firstCpuCall){
				this.generateCpuMap();
			}else{
				for(CPUUtilizationRecord r : this.dataBean.getNewCpuEntries()){
					String key = r.getHostname() + " - " + r.getCpuID();
					if(r.getLoggingTimestamp() >= this.cpuTimestamp){
						this.cpuTimestamp += this.duration;
						for(String id : this.cpuMap.keySet()){
							this.cpuMap.get(id).add(new DataEntry(this.cpuTimestamp));
						}
						this.cpuMap.get(key).getLast().addCPUUtilizationRecord(r);
					}else{
						this.cpuMap.get(key).getLast().addCPUUtilizationRecord(r);
					}
				}
			}
			// remove old values
			for(String signature : this.cpuMap.keySet()){
				LinkedList<DataEntry> list = this.cpuMap.get(signature);
				int removeFirst = list.size() - this.maxNumberOfDisplayedEntries - 1;
				for(int i=0; i < removeFirst; i++){
					list.removeFirst();
				}
			}
		}else if("memswap".equals(message)){
			if(this.firstMemSwapCall){
				this.generateMemSwapMap();
			}else{
				for(MemSwapUsageRecord r : this.dataBean.getNewMemSwapEntries()){
					String key = r.getHostname();
					if(r.getLoggingTimestamp() >= this.memSwapTimestamp){
						this.memSwapTimestamp += this.duration;
						for(String id : this.memSwapMap.keySet()){
							this.memSwapMap.get(id).add(new DataEntry(this.memSwapTimestamp));
						}
						this.memSwapMap.get(key).getLast().addMemSwapUsageRecord(r);
					}else{
						this.memSwapMap.get(key).getLast().addMemSwapUsageRecord(r);
					}
				}
			}
			// remove old values
			for(String signature : this.memSwapMap.keySet()){
				LinkedList<DataEntry> list = this.memSwapMap.get(signature);
				int removeFirst = list.size() - this.maxNumberOfDisplayedEntries - 1;
				for(int i=0; i < removeFirst; i++){
					list.removeFirst();
				}
			}
		}
		
	}

	
}
