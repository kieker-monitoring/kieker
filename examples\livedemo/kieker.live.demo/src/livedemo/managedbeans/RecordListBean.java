package livedemo.managedbeans;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import livedemo.entities.Record;

/**
 * @author Bjoern Weissenfels
 */
@ManagedBean(name="recordBean", eager=true)
@SessionScoped
public class RecordListBean implements Observer{
	
	int displayedRecords = 100;
	List<Record> lastRecords;
	boolean firstCall;
	boolean freeze;
	String freezeButton;
	String updateForm;
	@ManagedProperty(value = "#{dataBean}")
	DataBean dataBean;
	
	public RecordListBean(){
		this.lastRecords = new LinkedList<Record>();
		this.firstCall = true;
		this.freeze = false;
		this.freezeButton = "freeze";
		this.updateForm = "rec";
	}
	
	@PostConstruct
	public void init() {
		this.dataBean.addObserver(this);
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
		return this.lastRecords;
	}

	@Override
	public void update(Observable arg0, Object message) {
		if("oer".equals(message)){
			if(this.firstCall){
				List<Record> tmp = this.dataBean.getOERList();
				int size = tmp.size();
				if(size > this.displayedRecords){
					for(int i=1; i <= this.displayedRecords; i++){
						this.lastRecords.add(tmp.get(size - i));
					}
				}else{
					for(int i=1; i <= size; i++){
						this.lastRecords.add(tmp.get(size - i));
					}
				}
				this.firstCall = false;
			}else{
				List<Record> tmp = this.dataBean.getNewOEREntries();
				for(int i=0; i < tmp.size(); i++){
					this.lastRecords.add(0, tmp.get(i));
				}
				if(this.lastRecords.size() > this.displayedRecords){
					this.lastRecords.subList(this.displayedRecords, this.lastRecords.size()).clear();
				}
			}
		}
	}

}
