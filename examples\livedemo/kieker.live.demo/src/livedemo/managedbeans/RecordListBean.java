package livedemo.managedbeans;

import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import livedemo.entities.Record;

/**
 * @author Bjoern Weissenfels
 */
@ManagedBean(name="recordBean", eager=true)
@SessionScoped
public class RecordListBean {
	
	int displayedRecords = 100;
	boolean freeze;
	String freezeButton;
	String updateForm;
	@ManagedProperty(value = "#{dataBean}")
	DataBean dataBean;
	
	public RecordListBean(){
		this.freeze = false;
		this.freezeButton = "freeze";
		this.updateForm = "rec";
	}
	
	public String freeze(){
		if(this.freeze){
			this.freeze = false;
			this.freezeButton = "freeze";
			this.updateForm = "rec";
		}else{
			this.freeze = true;
			this.freezeButton = "unfreeze";
			this.updateForm = "";
		}
		return "";
	}
	
	public String getUpdateForm(){
		return this.updateForm;
	}
	
	public String getFreezeButton(){
		return this.freezeButton;
	}
	
	public void setDataBean(DataBean dataBean){
		this.dataBean = dataBean;
	}
	
	public List<Record> getRecords(){
		this.dataBean.updateOERList();
		List<Record> list = this.dataBean.getReverseOERList();
		if(list.size() > this.displayedRecords){
			return list.subList(0, this.displayedRecords);
		}else{
			return list;
		}
		
	}

}
