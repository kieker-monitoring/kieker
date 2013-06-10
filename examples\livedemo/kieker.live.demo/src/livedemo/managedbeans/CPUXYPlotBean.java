package livedemo.managedbeans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

import kieker.analysis.display.XYPlot;
import livedemo.entities.Model;

/**
 * @author Bjoern Weissenfels
 */
@ManagedBean(name="cpuXYPlotBean")
@ApplicationScoped
public class CPUXYPlotBean {
	
	@ManagedProperty(value = "#{analysisBean}")
	AnalysisBean analysisBean;
	
	private XYPlot xyPlot;
	private List<String> keys;
	private List<String> cpuIds;
	private List<Model> models;
	private int index;
	private TimeoutThread timeoutThread;
	
	public CPUXYPlotBean(){
		this.models = Collections.synchronizedList(new ArrayList<Model>());
		this.cpuIds = new ArrayList<String>();
		this.timeoutThread = new TimeoutThread(this, 1000);
	}
	
	@PostConstruct
	public void init(){
		this.xyPlot = this.analysisBean.getCPUUtilizationDisplayFilter().getXYPlot();
		this.keys = new ArrayList<String>(this.xyPlot.getKeys());
		Collections.sort(this.keys);
		this.index = this.keys.get(0).lastIndexOf('-');
		for(String key : this.keys){
			String id = key.substring(0, this.index - 1);
			if(!this.cpuIds.contains(id)){
				this.cpuIds.add(id);
			}
		}
		this.timeoutThread.start();
	}
	
	public void setAnalysisBean(AnalysisBean analysisBean){
		this.analysisBean = analysisBean;
	}
	
	public List<Model> getModels(){
		return this.models;
	}
	
	private void computeModels(){
		this.models.clear();
		for(String id : this.cpuIds){
			CartesianChartModel cpuModel = new CartesianChartModel();
			for(String key : this.keys){
				if(key.contains(id)){
					cpuModel.addSeries(this.computeModel(key));
				}
			}
			this.models.add(new Model(cpuModel,id));
		}
	}
	
	private ChartSeries computeModel(String key){
		Map<Object,Number> data = this.xyPlot.getEntries(key);
		ChartSeries cpuSeries = new ChartSeries(); 
		cpuSeries.setLabel(key.substring(index + 2));
		cpuSeries.setData(data);
		return cpuSeries;
	}
	
	@PreDestroy
	public void terminate(){
		this.timeoutThread.terminate();
	}
	
	private class TimeoutThread extends Thread{
		
		private long timeout;
		private CPUXYPlotBean bean;
		private boolean stop;
		
		public TimeoutThread(CPUXYPlotBean bean, long timeout){
			this.timeout = timeout;
			this.bean = bean;
			this.stop = false;
		}
		
		public void terminate(){
			this.stop = true;
		}
		
		public void run(){
			while(!stop){
				this.bean.computeModels();
				try {
					this.wait(timeout);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}
