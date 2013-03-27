package livedemo.managedbeans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.record.system.CPUUtilizationRecord;
import kieker.common.record.system.MemSwapUsageRecord;
import kieker.tools.traceAnalysis.systemModel.AllocationComponent;
import kieker.tools.traceAnalysis.systemModel.AssemblyComponent;
import kieker.tools.traceAnalysis.systemModel.ComponentType;
import kieker.tools.traceAnalysis.systemModel.ExecutionContainer;
import kieker.tools.traceAnalysis.systemModel.Operation;
import livedemo.entities.Record;
import livedemo.filter.ListFilter;

/**
 * @author Bjoern Weissenfels
 */
@ManagedBean(name="dataBean", eager=true)
@ApplicationScoped
public class DataBean extends Observable{
	
	int listLength; // number of entries displayed in the record list
	
	LinkedList<Record> records;
	LinkedList<Record> reverseRecords;
	List<Record> newRecords;
	
	LinkedList<CPUUtilizationRecord> cpuList;
	List<CPUUtilizationRecord> newCpuEntries;
	LinkedList<MemSwapUsageRecord> memSwapList;
	List<MemSwapUsageRecord> newMemSwapEntries;
	
	@ManagedProperty(value = "#{startingBean}")
	StartingBean startingBean;
	
	public DataBean(){
		this.listLength = 1500; // 25 * 60 entries
		this.records = new LinkedList<Record>();
		this.reverseRecords = new LinkedList<Record>();
		this.cpuList = new LinkedList<CPUUtilizationRecord>();
		this.newCpuEntries = new LinkedList<CPUUtilizationRecord>();
		this.memSwapList = new LinkedList<MemSwapUsageRecord>();
		this.newMemSwapEntries = new LinkedList<MemSwapUsageRecord>();
		this.newRecords = new LinkedList<Record>();
	}
	
	public void setStartingBean(StartingBean startingBean){
		this.startingBean = startingBean;
	}
	
	public LinkedList<Record> getOERList(){
		return this.records;
	}
	
	public LinkedList<Record> getReverseOERList(){
		return this.reverseRecords;
	}
	
	public List<Record> getNewOEREntries(){
		return this.newRecords;
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
		
	public synchronized void updateOERList(){
		ListFilter<OperationExecutionRecord> lcf = this.startingBean.getOERCollectionFilter();
		List<OperationExecutionRecord> newEntries;
		newEntries = lcf.getListAndClear();
		
		this.newRecords.clear();
		for(OperationExecutionRecord r : newEntries){
			this.newRecords.add(new Record(r));
		}
		
		// update recordLists
		Collections.reverse(newRecords);
		this.reverseRecords.addAll(0, newRecords);
		Collections.reverse(newRecords);
		this.records.addAll(newRecords);
		int diff = this.records.size() - this.listLength;
		for(int i=0; i < diff; i++){
			this.records.removeFirst();
			this.reverseRecords.removeLast();
		}
		
		this.setChanged();
		this.notifyObservers("oer");
	}
	
	public synchronized void updateCPUList(){
		ListFilter<CPUUtilizationRecord> cpuFilter = this.startingBean.getCPUCollectionFilter();
		this.newCpuEntries = cpuFilter.getListAndClear();
		
		// update list
		if(!this.newCpuEntries.isEmpty()){
			this.cpuList.addAll(this.newCpuEntries);
			int diff = this.cpuList.size() - this.listLength;
			for(int i=0; i < diff; i++){
				this.cpuList.removeFirst();
			}
			this.setChanged();
			this.notifyObservers("cpu");
		}
	}
	
	public synchronized void updateMemSwapList(){
		ListFilter<MemSwapUsageRecord> memSwapFilter = this.startingBean.getMemSwapCollectionFilter();
		this.newMemSwapEntries = memSwapFilter.getListAndClear();
		if(!this.newMemSwapEntries.isEmpty()){
			this.memSwapList.addAll(this.newMemSwapEntries);
			int diff = this.memSwapList.size() - this.listLength;
			for(int i=0; i < diff; i++){
				this.memSwapList.removeFirst();
			} 
			this.setChanged();
			this.notifyObservers("memswap");
		}
	}
}
