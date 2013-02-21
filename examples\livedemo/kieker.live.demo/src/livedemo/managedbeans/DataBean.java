package livedemo.managedbeans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.tools.traceAnalysis.systemModel.AllocationComponent;
import kieker.tools.traceAnalysis.systemModel.AssemblyComponent;
import kieker.tools.traceAnalysis.systemModel.ComponentType;
import kieker.tools.traceAnalysis.systemModel.ExecutionContainer;
import kieker.tools.traceAnalysis.systemModel.Operation;
import livedemo.entities.DataEntry;
import livedemo.entities.Record;

@ManagedBean(name="dataBean", eager=true)
@ApplicationScoped
public class DataBean {
	
	LinkedList<Record> records;
	int listLength;
	Map<String, List<DataEntry>> dataEntries; // Map of Signatures and corresponding DataEntries
	long duration; // duration for collecting method response times in nanos
	long timestamp; // in nanos
	
	@ManagedProperty(value = "#{startingBean}")
	StartingBean startingBean;
	
	public DataBean(){
		this.records = new LinkedList<Record>();
		this.listLength = 100;
		this.duration = 10000000000L; // 10 seconds
		this.dataEntries = new HashMap<String, List<DataEntry>>();
		this.timestamp = new Date().getTime() * 1000000;
	}
	
	public void setStartingBean(StartingBean startingBean){
		this.startingBean = startingBean;
	}
	
	public long getDuration(){
		return this.duration;
	}
	
	public void setDuration(long duration){
		this.duration = duration;
	}
	
	public Set<String> getAvailableMethods(){
		return this.dataEntries.keySet();
	}
	
	public List<Record> getRecords(){
		this.updateList(); // should be executed automatically every x seconds 
		return this.records;
	}
	
	public Map<String, List<DataEntry>> getDataEntries(){
		this.updateList(); // should be executed automatically every x seconds
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
		
	private void updateList(){
		ListCollectionFilter<OperationExecutionRecord> lcf = this.startingBean.getCollectionFilter();
		List<OperationExecutionRecord> newEntries;
		synchronized(lcf){
			newEntries = lcf.getList();
			lcf.clear();
		}
		
		List<Record> newRecords = new ArrayList<Record>();
		for(OperationExecutionRecord r : newEntries){
			newRecords.add(new Record(r));
		}
		
		// update dataEntries with response times
		
		for(Record r : newRecords){
			if(r.getOperationExecutionRecord().getLoggingTimestamp() > this.timestamp){
				this.timestamp += this.duration;
				while(r.getOperationExecutionRecord().getLoggingTimestamp() > this.timestamp){
					for(String signature : this.dataEntries.keySet()){
						this.dataEntries.get(signature).add(new DataEntry(this.timestamp));
					}
					this.timestamp += this.duration;
				}
				if(!this.dataEntries.containsKey(r.getOperationSignature())){
					this.dataEntries.put(r.getOperationSignature(), new ArrayList<DataEntry>());
				}
				for(String signature : this.dataEntries.keySet()){
					this.dataEntries.get(signature).add(new DataEntry(this.timestamp));
				}
				List<DataEntry> entries = this.dataEntries.get(r.getOperationSignature());
				entries.get(entries.size() - 1).addRecord(r);
			}else{
				if(this.dataEntries.containsKey(r.getOperationSignature())){
					List<DataEntry> entries = this.dataEntries.get(r.getOperationSignature());
					entries.get(entries.size() - 1).addRecord(r);
				}else{
					List<DataEntry> newData = new ArrayList<DataEntry>();
					DataEntry data = new DataEntry(this.timestamp);
					data.addRecord(r);
					newData.add(data);
					this.dataEntries.put(r.getOperationSignature(), newData);
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
}
