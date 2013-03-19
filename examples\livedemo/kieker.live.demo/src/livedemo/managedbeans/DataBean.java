package livedemo.managedbeans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.record.system.CPUUtilizationRecord;
import kieker.common.record.system.MemSwapUsageRecord;
import kieker.tools.traceAnalysis.systemModel.AllocationComponent;
import kieker.tools.traceAnalysis.systemModel.AssemblyComponent;
import kieker.tools.traceAnalysis.systemModel.ComponentType;
import kieker.tools.traceAnalysis.systemModel.ExecutionContainer;
import kieker.tools.traceAnalysis.systemModel.Operation;
import livedemo.entities.DataEntry;
import livedemo.entities.Record;

@ManagedBean(name="dataBean", eager=true)
@ApplicationScoped
public class DataBean extends Observable{
	
	int listLength; // number of entries displayed in the record list
	
	LinkedList<Record> records;
	Map<String, List<DataEntry>> dataEntries; // Map of Signatures and corresponding DataEntries
	long oerDuration; // duration for collecting method response times in nanos
	long oerTimestamp; // temp var in nanos
	
	LinkedList<CPUUtilizationRecord> cpuList;
	List<CPUUtilizationRecord> newCpuEntries;
	LinkedList<MemSwapUsageRecord> memSwapList;
	List<MemSwapUsageRecord> newMemSwapEntries;
	int cpuAndMemSwapListLength;
	
	@ManagedProperty(value = "#{startingBean}")
	StartingBean startingBean;
	
	public DataBean(){
		this.listLength = 100;
		
		this.records = new LinkedList<Record>();
		this.oerDuration = 3 * 1000000000L; // 3 seconds
		this.dataEntries = new HashMap<String, List<DataEntry>>();
		this.oerTimestamp = new Date().getTime() * 1000000;
		
		this.cpuList = new LinkedList<CPUUtilizationRecord>();
		this.newCpuEntries = new LinkedList<CPUUtilizationRecord>();
		this.memSwapList = new LinkedList<MemSwapUsageRecord>();
		this.newMemSwapEntries = new LinkedList<MemSwapUsageRecord>();
		this.cpuAndMemSwapListLength = 1800; // 30 * 60 entries
	}
	
	public void setStartingBean(StartingBean startingBean){
		this.startingBean = startingBean;
	}
	
	public long getDuration(){
		return this.oerDuration;
	}
	
	public void setDuration(long duration){
		this.oerDuration = duration;
	}
	
	public Set<String> getAvailableMethods(){
		return this.dataEntries.keySet();
	}
	
	public List<Record> getOERList(){
		this.updateOERList();
		return this.records;
	}
	
	public LinkedList<CPUUtilizationRecord> getCpuList(){
		return this.cpuList;
	}
	
	public List<CPUUtilizationRecord> getNewCpuEntries(){
		return this.newCpuEntries;
	}
	
	public LinkedList<MemSwapUsageRecord> getMemSwapList(){
		return this.memSwapList;
	}
	
	public List<MemSwapUsageRecord> getNewMemSwapEntries(){
		return this.newMemSwapEntries;
	}
	
	public Map<String, List<DataEntry>> getDataEntries(){
		this.updateOERList();
		return this.dataEntries;
	}
	
	public List<ComponentType> getComponentTypes(){
		List<ComponentType> ctList = new ArrayList<ComponentType>();
		Collection<ComponentType> collection = this.startingBean.getSystemModelRepository().getTypeRepositoryFactory().getComponentTypes();
		ctList.addAll(collection);
		return ctList;
	}
	
	public List<Operation> getOperations(){
		List<Operation> opList = new ArrayList<Operation>();
		Collection<Operation> collection = this.startingBean.getSystemModelRepository().getOperationFactory().getOperations();
		opList.addAll(collection);
		return opList;
	}
	
	public List<AssemblyComponent> getAssemblyComponents(){
		List<AssemblyComponent> acList = new ArrayList<AssemblyComponent>();
		Collection<AssemblyComponent> collection = this.startingBean.getSystemModelRepository().getAssemblyFactory().getAssemblyComponentInstances();
		acList.addAll(collection);
		return acList;
	}
	
	public List<ExecutionContainer> getExecutionContainers(){
		List<ExecutionContainer> ecList = new ArrayList<ExecutionContainer>();
		Collection<ExecutionContainer> collection = this.startingBean.getSystemModelRepository().getExecutionEnvironmentFactory().getExecutionContainers();
		ecList.addAll(collection);
		return ecList;
	}
	
	public List<AllocationComponent> getDeploymentComponents(){
		List<AllocationComponent> acList = new ArrayList<AllocationComponent>();
		Collection<AllocationComponent> collection = this.startingBean.getSystemModelRepository().getAllocationFactory().getAllocationComponentInstances();
		acList.addAll(collection);
		return acList;
	}
		
	private void updateOERList(){
		ListCollectionFilter<OperationExecutionRecord> lcf = this.startingBean.getOERCollectionFilter();
		List<OperationExecutionRecord> newEntries;
		synchronized(lcf){
			newEntries = lcf.getList();
			lcf.clear();
		}
		
		List<Record> newRecords = new ArrayList<Record>();
		for(OperationExecutionRecord r : newEntries){
			newRecords.add(new Record(r));
		}
		
		// update response times
		for(Record r : newRecords){
			if(r.getOperationExecutionRecord().getLoggingTimestamp() > this.oerTimestamp){
				this.oerTimestamp += this.oerDuration;
				while(r.getOperationExecutionRecord().getLoggingTimestamp() > this.oerTimestamp){
					for(String signature : this.dataEntries.keySet()){
						this.dataEntries.get(signature).add(new DataEntry(this.oerTimestamp));
					}
					this.oerTimestamp += this.oerDuration;
				}
				if(!this.dataEntries.containsKey(r.getOperationExecutionRecord().getOperationSignature())){
					this.dataEntries.put(r.getOperationExecutionRecord().getOperationSignature(), new ArrayList<DataEntry>());
				}
				for(String signature : this.dataEntries.keySet()){
					this.dataEntries.get(signature).add(new DataEntry(this.oerTimestamp));
				}
				List<DataEntry> entries = this.dataEntries.get(r.getOperationExecutionRecord().getOperationSignature());
				entries.get(entries.size() - 1).addRecord(r);
			}else{
				if(this.dataEntries.containsKey(r.getOperationExecutionRecord().getOperationSignature())){
					List<DataEntry> entries = this.dataEntries.get(r.getOperationExecutionRecord().getOperationSignature());
					entries.get(entries.size() - 1).addRecord(r);
				}else{
					List<DataEntry> newData = new ArrayList<DataEntry>();
					DataEntry data = new DataEntry(this.oerTimestamp);
					data.addRecord(r);
					newData.add(data);
					this.dataEntries.put(r.getOperationExecutionRecord().getOperationSignature(), newData);
				}
			}	
		}
		
		// update recordList
		Collections.reverse(newRecords);
		this.records.addAll(0, newRecords);
		int diff = this.records.size() - this.listLength;
		for(int i=0; i < diff; i++){
			this.records.removeLast();
		}
	}
	
	public void updateCPUList(){
		ListCollectionFilter<CPUUtilizationRecord> cpuFilter = this.startingBean.getCPUCollectionFilter();
		// TODO: talk with Jan about synchronized block
		synchronized(cpuFilter){
			this.newCpuEntries = cpuFilter.getList();
			cpuFilter.clear();
		}
		
		// update list
		if(!this.newCpuEntries.isEmpty()){
			this.cpuList.addAll(this.newCpuEntries);
			int diff = this.cpuList.size() - this.cpuAndMemSwapListLength;
			for(int i=0; i < diff; i++){
				this.cpuList.removeFirst();
			}
			this.setChanged();
			this.notifyObservers("cpu");
		}
	}
	
	public void updateMemSwapList(){
		ListCollectionFilter<MemSwapUsageRecord> memSwapFilter = this.startingBean.getMemSwapCollectionFilter();
		synchronized(memSwapFilter){
			this.newMemSwapEntries = memSwapFilter.getList();
			memSwapFilter.clear();
		}
		if(!this.newMemSwapEntries.isEmpty()){
			this.memSwapList.addAll(this.newMemSwapEntries);
			int diff = this.memSwapList.size() - this.cpuAndMemSwapListLength;
			for(int i=0; i < diff; i++){
				this.memSwapList.removeFirst();
			} 
			this.setChanged();
			this.notifyObservers("memswap");
		}
	}
}
