package livedemo.managedbeans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import kieker.common.record.controlflow.OperationExecutionRecord;

@ManagedBean(name="recordBean", eager=true)
@ApplicationScoped
public class RecordListBean {
	
	List<OperationExecutionRecord> records = new ArrayList<OperationExecutionRecord>();
	
	@ManagedProperty(value = "#{startingBean}")
	StartingBean startingBean;
	
	public RecordListBean(){
	}
	
	public void setStartingBean(StartingBean startingBean){
		this.startingBean = startingBean;
	}
	
	public List<OperationExecutionRecord> getRecords(){
		List<OperationExecutionRecord> list = startingBean.getCollectionFilter().getList();
		startingBean.getCollectionFilter().clear();
		Collections.reverse(list);
		this.records.addAll(0,list);
		return records;
	}

}
