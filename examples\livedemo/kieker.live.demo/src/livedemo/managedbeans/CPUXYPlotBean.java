package livedemo.managedbeans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ViewScoped;
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
@ViewScoped
public class CPUXYPlotBean implements Observer{
	
	@ManagedProperty(value = "#{analysisBean}")
	private AnalysisBean analysisBean;
	
	private XYPlot xyPlot;
	private List<String> keys;
	private List<String> cpuIds;
	private List<Model<CartesianChartModel>> models;
	private int index;
	
	private final List<String> availableAttributes = Arrays.asList("idle","irq","nice","system","totalUtilization","user");
	private List<String> selectedAttributes;// = Arrays.asList("idle","totalUtilization");

	public CPUXYPlotBean(){
		this.models = Collections.synchronizedList(new ArrayList<Model<CartesianChartModel>>());
		this.cpuIds = new ArrayList<String>();
		this.selectedAttributes = new ArrayList<String>();
		this.selectedAttributes.add("idle");
		this.selectedAttributes.add("totalUtilization");
	}
	
	@PostConstruct
	public void init(){
		this.xyPlot = this.analysisBean.getCPUUtilizationDisplayFilter().getXYPlot();
		this.keys = new ArrayList<String>(this.xyPlot.getKeys()); // key = hostname - cpuId - idle
		Collections.sort(this.keys);
		this.index = this.keys.get(0).lastIndexOf('-');
		for(String key : this.keys){
			String id = key.substring(0, this.index - 1); // id = hostname - cpuId
			if(!this.cpuIds.contains(id)){
				this.cpuIds.add(id);
			}
		}
		this.updateModel();
		this.analysisBean.getUpdateThread().addObserver(this);
	}
	
	@PreDestroy
	public void terminate(){
		this.analysisBean.getUpdateThread().deleteObserver(this);
	}
	
	public void setAnalysisBean(AnalysisBean analysisBean){
		this.analysisBean = analysisBean;
	}
	
	public List<String> getAvailableAttributes() {
		return this.availableAttributes;
	}

	public List<String> getSelectedAttributes() {
		if(this.selectedAttributes.isEmpty()){
			this.selectedAttributes.add("totalUtilization");
			this.selectedAttributes.add("idle");
		}
		return selectedAttributes;
	}

	public void setSelectedAttributes(List<String> selectedAttributes) {
		this.selectedAttributes = selectedAttributes;
	}

	public List<Model<CartesianChartModel>> getModels(){
		return this.models;
	}
	
	private ChartSeries computeModel(String key, String attribute){
		Map<Object,Number> data = this.xyPlot.getEntries(key);
		ChartSeries cpuSeries = new ChartSeries(); 
		cpuSeries.setLabel(attribute);
		cpuSeries.setData(data);
		return cpuSeries;
	}
	
	private void updateModel(){
		this.models.clear();
		for(String id : this.cpuIds){ // id = hostname - cpuId
			CartesianChartModel cpuModel = new CartesianChartModel();
			for(String key : this.keys){ // key = hostname - cpuId - idle
				if(key.substring(0, this.index - 1).equals(id)){
					for(String attribute : this.getSelectedAttributes()){
						if(key.substring(index + 2).equals(attribute)){
							cpuModel.addSeries(this.computeModel(key, attribute));
						}
					}
					
				}
			}
			this.models.add(new Model<CartesianChartModel>(cpuModel,id));
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		this.updateModel();
		
	}

}
