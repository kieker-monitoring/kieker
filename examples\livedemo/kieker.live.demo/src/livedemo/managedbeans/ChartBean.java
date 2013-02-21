package livedemo.managedbeans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import livedemo.entities.DataEntry;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

@ManagedBean(name="chartBean", eager=true)
@ApplicationScoped
public class ChartBean {
	
	CartesianChartModel countingModel;
	CartesianChartModel responsetimeModel;
	int numberOfCounts;
	List<String> selectedMethods;
	String method;
	int maxCalls;
	int maxResponsetime;
	
	@ManagedProperty(value = "#{dataBean}")
	DataBean dataBean;
	
	public ChartBean(){
		countingModel = new CartesianChartModel();
		responsetimeModel = new CartesianChartModel();
		numberOfCounts = 10;
		method = "";
		this.selectedMethods = new ArrayList<String>();
		this.maxCalls = 200;
		this.maxResponsetime = 30;
	}
	
	public void setDataBean(DataBean dataBean){
		this.dataBean = dataBean;
	}
	
	public int getMaxCalls(){
		return this.maxCalls;
	}
	
	public int getMaxResponsetime(){
		return this.maxResponsetime;
	}
	
	public String getMethod(){
		return this.method;
	}
	
	public void setMethod(String method){
		this.method = method;
	}
	
	public List<String> getSelectedMethods(){
		return this.selectedMethods;
	}
	
	public void setSelectedMethods(List<String> methods){
		this.selectedMethods = methods;
	}
	
	public CartesianChartModel getCountingModel() {
		return getCountingModel(this.numberOfCounts);
	}
		
	public CartesianChartModel getResponsetimeModel() {
		return getResponsetimeModel(this.numberOfCounts);
	}
	
	public List<String> getAvailableMethods(){
		Set<String> methods = this.dataBean.getAvailableMethods();
		List<String> result = new ArrayList<String>();
		for(String s : methods){
			result.add(s);
		}
		return result;
	}
	
	private CartesianChartModel getResponsetimeModel(int number) {
		this.responsetimeModel.clear();
		if(this.getSelectedMethods().isEmpty()){
			ChartSeries responsetimes = new ChartSeries();
			responsetimes.setLabel("choose method");
			for(int i=0; i < number; i++){
				responsetimes.set("x"+i,0);
			}
			this.responsetimeModel.addSeries(responsetimes);
			return this.responsetimeModel;
		}
		
		Map<String, List<DataEntry>> methodMap = this.dataBean.getDataEntries();
		for (String signature : this.getSelectedMethods()){
			List<DataEntry> dataEntries = methodMap.get(signature);
			ChartSeries responsetimes = new ChartSeries();  
	        responsetimes.setLabel(this.getMethodName(signature));
	        int fromIndex;
			int toIndex = dataEntries.size() - 1;
			int diff;
			if(toIndex > number){
				fromIndex = toIndex - number;
			}else if(toIndex < number){
				fromIndex = 0;
				diff = toIndex - number;
				for(int i=0; i < diff; i++){
					responsetimes.set("x"+i, 0);
				}
			}else{
				fromIndex = 0;
			}
	        List<DataEntry> subList = dataEntries.subList(fromIndex, toIndex);
	        for(int i = 0; i < subList.size(); i++){
	        	responsetimes.set(subList.get(i).getTimestamp(), subList.get(i).getAverageResponsetime());
	        }
	        this.responsetimeModel.addSeries(responsetimes);   
		}
        return this.responsetimeModel;
	}
	
	private CartesianChartModel getCountingModel(int number) {
		this.countingModel.clear();
		if(this.getSelectedMethods().isEmpty()){
			ChartSeries count = new ChartSeries();
			count.setLabel("choose method");
			for(int i=0; i < number; i++){
				count.set("x"+i,0);
			}
			this.countingModel.addSeries(count);
			return this.countingModel;
		}
		
		Map<String, List<DataEntry>> methodMap = this.dataBean.getDataEntries();
		for (String signature : this.getSelectedMethods()){
			List<DataEntry> dataEntries = methodMap.get(signature);
			ChartSeries count = new ChartSeries();  
			count.setLabel(this.getMethodName(signature));
	        int fromIndex;
			int toIndex = dataEntries.size() - 1;
			int diff;
			if(toIndex > number){
				fromIndex = toIndex - number;
			}else if(toIndex < number){
				fromIndex = 0;
				diff = toIndex - number;
				for(int i=0; i < diff; i++){
					count.set("x"+i, 0);
				}
			}else{
				fromIndex = 0;
			}
	        List<DataEntry> subList = dataEntries.subList(fromIndex, toIndex);
	        for(int i = 0; i < subList.size(); i++){
	        	count.set(subList.get(i).getTimestamp(), subList.get(i).getCount());
	        }
	        this.countingModel.addSeries(count);   
		}
        return this.countingModel;
    }  
	
	public String getMethodName(String signature){
		String[] array = signature.split("\\(");
		array = array[0].split("\\.");
		int end = array.length;
		String result = "..." + array[end-2] + "." + array[end-1] + "(...)";
		return result;
	}

}
