package livedemo.managedbeans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

import kieker.common.util.ImmutableEntry;

@ManagedBean(name="countingBean", eager=true)
@ApplicationScoped
public class CountingBean {
	
	List<Entry<Date,Long>> countsPerInterval = new ArrayList<Entry<Date,Long>>();
	CartesianChartModel categoryModel = new CartesianChartModel();
	Long maxCounts = 200L;
	
	@ManagedProperty(value = "#{startingBean}")
	StartingBean startingBean;
	
	public CountingBean(){
	}
	
	public void setStartingBean(StartingBean startingBean){
		this.startingBean = startingBean;
	}
	
	public Long getMaxCounts(){
		return this.maxCounts;
	}
	
	public List<Entry<Date, Long>> getCounts(){
		Collection<Entry<Long,Long>> entries = startingBean.getCountingFilter().getCountsPerInterval();
		List<Entry<Long,Long>> newList = new ArrayList<Entry<Long,Long>>();
		newList.addAll(entries);
		List<Entry<Long,Long>> newEntries = newList.subList(this.countsPerInterval.size(), newList.size());
		for(int i = 0; i < newEntries.size(); i++){
			Entry<Long,Long> entry = newEntries.get(i);
			if(entry.getValue() > this.maxCounts){
				this.maxCounts = entry.getValue();
			}
			this.countsPerInterval.add(new ImmutableEntry<Date,Long>(getDate(entry.getKey()),entry.getValue()));			
		}
		return countsPerInterval;
	}
	
	public CartesianChartModel getCategoryModel() {
		return getCategoryModel(10);
	}
	
	private CartesianChartModel getCategoryModel(int number) {
		getCounts();
        ChartSeries counts = new ChartSeries();  
        counts.setLabel("Counts Per Interval"); 
        
        int fromIndex;
		int toIndex = countsPerInterval.size();
		if(toIndex > number){
			fromIndex = toIndex-number;
		}else{
			fromIndex = 0;
		}
        List<Entry<Date, Long>> subList = this.countsPerInterval.subList(fromIndex, toIndex);
        for(Entry<Date,Long> entry : subList){
        	String time = entry.getKey().toString().substring(11, 19);
        	counts.set(time, entry.getValue());
        }
        this.categoryModel.clear();
        this.categoryModel.addSeries(counts);   
        return this.categoryModel;
    }  
	
	public Date getDate(long nanos){
		return new Date(nanos/1000000);
	}

}
