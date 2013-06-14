package livedemo.managedbeans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

import kieker.analysis.display.XYPlot;
import livedemo.entities.Model;

/**
 * @author Bjoern Weissenfels
 */
@ManagedBean(name="cpuXYPlotBean")
@ApplicationScoped
public class CPUXYPlotBean implements Observer{
	
	@ManagedProperty(value = "#{analysisBean}")
	AnalysisBean analysisBean;
	
	private XYPlot xyPlot;
	private List<String> keys;
	private List<String> cpuIds;
	private List<Model> models;
	private int index;
	
	public CPUXYPlotBean(){
		this.models = Collections.synchronizedList(new ArrayList<Model>());
		this.cpuIds = new ArrayList<String>();
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
		this.analysisBean.getUpdateThread().addObserver(this);
	}
	
	@PreDestroy
	public void terminate(){
		this.analysisBean.getUpdateThread().deleteObserver(this);
	}
	
	public void setAnalysisBean(AnalysisBean analysisBean){
		this.analysisBean = analysisBean;
	}
	
	public List<Model> getModels(){
		return this.models;
	}
	
	private ChartSeries computeModel(String key){
		Map<Object,Number> data = this.xyPlot.getEntries(key);
		ChartSeries cpuSeries = new ChartSeries(); 
		cpuSeries.setLabel(key.substring(index + 2));
		cpuSeries.setData(data);
		return cpuSeries;
	}

	@Override
	public void update(Observable o, Object arg) {
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

}
