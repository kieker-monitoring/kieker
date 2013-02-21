package livedemo.managedbeans;

import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import livedemo.entities.Record;

@ManagedBean(name="recordBean", eager=true)
@ApplicationScoped
public class RecordListBean {
	
	@ManagedProperty(value = "#{dataBean}")
	DataBean dataBean;
	
	public RecordListBean(){
	}
	
	public void setDataBean(DataBean dataBean){
		this.dataBean = dataBean;
	}
	
	public List<Record> getRecords(){
		return this.dataBean.getRecords();
	}
	
	

}
