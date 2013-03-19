package livedemo.managedbeans;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.annotation.PostConstruct;
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

@ManagedBean(name="cpuBean", eager=true)
@SessionScoped
public class CpuBean implements Observer{
	
	@ManagedProperty(value = "#{dataBean}")
	DataBean dataBean;
	
	List<Integer> possibleNumberOfDisplayedEntries;
	int numberOfDisplayedEntries;
	List<Integer> possibleIntervallLength;
	int intervallLength;
	long duration;
	
	Map<String, LinkedList<DataEntry>> cpuMap; // key = hostname + " - " + cpuId
	final String[] possibleCpuAttributes = {"user", "system", "wait", "nice", "irq", "totalUtilization", "idle"};
	List<String> selectedCpuAttributes;
	List<String> possibleCPUs;
	List<String> selectedCPUs;
	List<Model> cpuModels;
	long cpuTimestamp;
	boolean firstCpuCall;
	
	Map<String, LinkedList<DataEntry>> memSwapMap; // key = hostname
	final String[] possibleMemSwapAttributes = {"memTotal", "memUsed", "memFree", "swapTotal", "swapUsed", "swapFree"};
	List<String> selectedMemSwapAttributes;
	List<String> possibleHosts;
	List<String> selectedHosts;
	List<Model> memSwapModels;
	long memSwapTimestamp;
	boolean firstMemSwapCall;
	
	public CpuBean(){
		this.possibleNumberOfDisplayedEntries = new ArrayList<Integer>();
		this.possibleIntervallLength = new ArrayList<Integer>();
		this.numberOfDisplayedEntries = 12;
		this.intervallLength = 3;
		this.duration = 3 * 1000000000L; // 3 seconds
		
		this.cpuMap = new HashMap<String, LinkedList<DataEntry>>();
		this.selectedCpuAttributes = new ArrayList<String>();
		this.possibleCPUs = new ArrayList<String>();
		this.selectedCPUs = new ArrayList<String>();
		this.cpuModels = new ArrayList<Model>();
		this.cpuTimestamp = new Date().getTime() * 1000000;
		this.firstCpuCall = true;
		
		this.memSwapMap = new HashMap<String, LinkedList<DataEntry>>();
		this.selectedCpuAttributes = new ArrayList<String>();
		this.possibleHosts = new ArrayList<String>();
		this.selectedHosts = new ArrayList<String>();
		this.memSwapModels = new ArrayList<Model>();
		this.memSwapTimestamp = this.cpuTimestamp;
		this.firstMemSwapCall = true;
	}
	
	@PostConstruct
	public void init() {
		this.dataBean.addObserver(this);
		for(int i=10; i<31;i++){
			this.possibleNumberOfDisplayedEntries.add(i);
		}
		for(int i=1; i<61;i++){
			this.possibleIntervallLength.add(i);
		}
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
		}
		return this.possibleCPUs;
	}
	
	public List<String> getSelectedCPUs(){
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
	public String[] getPossibleMemSwapAttributes(){
		return this.possibleMemSwapAttributes;
	}
	
	public List<String> getSelectedMemSwapAttributes(){
		return this.selectedMemSwapAttributes;
	}
	
	public void setSelectedMemSwapAttributes(List<String> selectedMemSwapAttributes){
		this.selectedMemSwapAttributes = selectedMemSwapAttributes;
	}
	
	public List<String> getPossibleHosts(){
		if(this.possibleHosts.isEmpty()){
			// this call will update the map, after this all cpuIds are known
			this.dataBean.updateMemSwapList();  
			Set<String> ids = this.memSwapMap.keySet();
			this.possibleCPUs.clear();
			for(String s : ids){
				this.possibleHosts.add(s);
			}
		}
		return this.possibleHosts;
	}
	
	public List<String> getSelectedHosts(){
		return this.selectedHosts;
	}
	
	public void setSelectedHosts(List<String> selectedHosts){
		this.selectedHosts = selectedHosts;
	}
	
	public List<Model> getMemSwapModels(){
		this.dataBean.updateMemSwapList();
		this.memSwapModels.clear();
		for (String hostname : this.selectedHosts){
			Model model = new Model(this.getMemSwapModel(this.numberOfDisplayedEntries, hostname),hostname);
			this.memSwapModels.add(model);
		}
		return this.memSwapModels;
	}

	// EventListener
	public void changeNumberOfDisplayedEntries(AjaxBehaviorEvent event){
		// TODO: change possibleIntervallLength
	}
	
	public synchronized void changeIntervallLength(AjaxBehaviorEvent event){
		// TODO: change possibleNumberOfDisplayedEntries
		this.duration = this.intervallLength * 1000000000L;
		this.cpuMap.clear();
		this.memSwapMap.clear();
		this.generateCpuMap();
		this.generateMemSwapMap();
	}
	
	private CartesianChartModel getCpuModel(int numberOfDisplayedEntries, String id) {
		CartesianChartModel cpuModel = new CartesianChartModel();
		if(this.getSelectedCpuAttributes().isEmpty()){
			ChartSeries cpuSeries = new ChartSeries();
			cpuSeries.setLabel("choose attribute");
			for(int i=0; i < numberOfDisplayedEntries; i++){
				cpuSeries.set("x"+i,0);
			}
			cpuModel.addSeries(cpuSeries);
			return cpuModel;
		}
		
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
	
	private CartesianChartModel getMemSwapModel(int numberOfDisplayedEntries, String id) {
		CartesianChartModel memSwapModel = new CartesianChartModel();
		if(this.getSelectedMemSwapAttributes().isEmpty()){
			ChartSeries memSwapSeries = new ChartSeries();
			memSwapSeries.setLabel("choose attribute");
			for(int i=0; i < numberOfDisplayedEntries; i++){
				memSwapSeries.set("x"+i,0);
			}
			memSwapModel.addSeries(memSwapSeries);
			return memSwapModel;
		}
		
		List<DataEntry> memSwapList = this.memSwapMap.get(id);
		int fromIndex;
		int toIndex = memSwapList.size() - 1;
		if(toIndex > numberOfDisplayedEntries){
			fromIndex = toIndex - numberOfDisplayedEntries;
		}else{
			fromIndex = 0;
		}
        List<DataEntry> subList = memSwapList.subList(fromIndex, toIndex);
        
		for (String attribute : this.getSelectedMemSwapAttributes()){
			ChartSeries memSwapSeries = new ChartSeries();  
	        memSwapSeries.setLabel(attribute);
	        
	        if("memTotal".equals(attribute)){
	        	for(int i = 0; i < subList.size(); i++){
		        	memSwapSeries.set(subList.get(i).getMinSec(), subList.get(i).getAverageMemTotal());
		        }
	        }else if("memUsed".equals(attribute)){
	        	for(int i = 0; i < subList.size(); i++){
		        	memSwapSeries.set(subList.get(i).getMinSec(), subList.get(i).getAverageMemUsed());
		        }
	        }else if("memFree".equals(attribute)){
	        	for(int i = 0; i < subList.size(); i++){
		        	memSwapSeries.set(subList.get(i).getMinSec(), subList.get(i).getAverageMemFree());
		        }
	        }else if("swapTotal".equals(attribute)){
	        	for(int i = 0; i < subList.size(); i++){
		        	memSwapSeries.set(subList.get(i).getMinSec(), subList.get(i).getAverageSwapTotal());
		        }
	        }else if("swapUsed".equals(attribute)){
	        	for(int i = 0; i < subList.size(); i++){
		        	memSwapSeries.set(subList.get(i).getMinSec(), subList.get(i).getAverageSwapUsed());
		        }
	        }else if("swapFree".equals(attribute)){
	        	for(int i = 0; i < subList.size(); i++){
		        	memSwapSeries.set(subList.get(i).getMinSec(), subList.get(i).getAverageSwapFree());
		        }
	        }
	        memSwapModel.addSeries(memSwapSeries);   
		}
        return memSwapModel;
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
		}
		// TODO: remove old values from cpuMap and memSwapMap?
	}

	
}
