package livedemo.managedbeans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import kieker.common.record.system.MemSwapUsageRecord;

@ManagedBean(name="memSwapBean", eager=true)
@SessionScoped
public class MemSwapBean {

	@ManagedProperty(value = "#{dataBean}")
	DataBean dataBean;
	final String[] possibleAttributes = {"memUsed", "memFree", "swapTotal", "swapUsed", "swapFree"} ; // memSwap attributes
	List<String> selectedAttributes;
	
	public MemSwapBean(){
		this.selectedAttributes = new ArrayList<String>();
	}
	
	public void setDataBean(DataBean dataBean){
		this.dataBean = dataBean;
	}
	
	public void setChoice(List<String> choice){
		this.selectedAttributes = choice;
	}
	
	public List<MemSwapUsageRecord> getMemSwapList(){
		return this.dataBean.getMemSwapList();
	}
}
