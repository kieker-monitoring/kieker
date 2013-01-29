package livedemo.managedbeans;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.common.record.controlflow.OperationExecutionRecord;

@ManagedBean(name="recordBean", eager=true)
@ApplicationScoped
public class RecordListBean {
	
	LinkedList<OperationExecutionRecord> records;
	int listLength;
	
	@ManagedProperty(value = "#{startingBean}")
	StartingBean startingBean;
	
	public RecordListBean(){
		records = new LinkedList<OperationExecutionRecord>();
		listLength = 100;
	}
	
	public void setStartingBean(StartingBean startingBean){
		this.startingBean = startingBean;
	}
	
	public List<OperationExecutionRecord> getRecords(){
		// should be executed automatically every x seconds 
		this.updateList();
		return records;
	}
	
	private synchronized void updateList(){
		ListCollectionFilter<OperationExecutionRecord> lcf = this.startingBean.getCollectionFilter();
		List<OperationExecutionRecord> newEntries;
		synchronized(lcf){
			newEntries = lcf.getList();
			lcf.clear();
		}
		Collections.reverse(newEntries);
		this.records.addAll(0, newEntries);
		int diff = this.records.size() - this.listLength;
		for(int i=0; i < diff; i++){
			this.records.removeLast();
		}
	}

}
