package livedemo.managedbeans;

import java.util.Collections;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import livedemo.entities.Record;

/**
 * @author Bjoern Weissenfels
 */
@ManagedBean(name="recordBean", eager=true)
@ViewScoped
public class RecordListBean{
	
	@ManagedProperty(value = "#{analysisBean}")
	private AnalysisBean analysisBean;
	
	private boolean freeze;
	private String freezeButton;
	private String updateForm;
	
	public RecordListBean(){
		this.freeze = false;
		this.freezeButton = "freeze";
		this.updateForm = "rec";
	}
	
	public void setAnalysisBean(AnalysisBean analysisBean){
		this.analysisBean = analysisBean;
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
	
	public List<Record> getRecords(){
		List<Record> list = this.analysisBean.getRecordListFilter().getList();
		Collections.reverse(list);
		return list;
	}

}
