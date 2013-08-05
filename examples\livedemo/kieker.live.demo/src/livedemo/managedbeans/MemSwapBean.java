package livedemo.managedbeans;

import java.util.Observable;
import java.util.Observer;

import javax.annotation.PostConstruct;
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
@ManagedBean(name="memSwapBean")
@ApplicationScoped
public class MemSwapBean implements Observer {

	@ManagedProperty(value = "#{analysisBean}")
	private AnalysisBean analysisBean;
	
	private XYPlot xyplot;
	private Model<CartesianChartModel> memModel;
	private Model<CartesianChartModel> swapModel;
	
	public MemSwapBean(){
		this.memModel = new Model<CartesianChartModel>(new CartesianChartModel(), "KIEKER-DEMO-SVR - MEM");
		this.swapModel = new Model<CartesianChartModel>(new CartesianChartModel(), "KIEKER-DEMO-SVR - SWAP");
	}
	
	@PostConstruct
	public void init(){
		this.xyplot = this.analysisBean.getMemSwapUtilizationDisplayFilter().getXYPlot();
		this.updateXYPlot();
		String key = this.xyplot.getKeys().iterator().next();
		int index = key.lastIndexOf('-');
		String hostname = key.substring(0, index - 1);
		this.memModel.setName(hostname + " - MEM");
		this.swapModel.setName(hostname + " - SWAP");
		this.analysisBean.getUpdateThread().addObserver(this);
	}
	
	public void setAnalysisBean(AnalysisBean analysisBean){
		this.analysisBean = analysisBean;
	}

	public Model<CartesianChartModel> getMemModel(){
		return this.memModel;
	}
	
	public Model<CartesianChartModel> getSwapModel(){
		return this.swapModel;
	}
	
	private void updateXYPlot(){
		CartesianChartModel mem = new CartesianChartModel();
		CartesianChartModel swap = new CartesianChartModel();
		for(String key : this.xyplot.getKeys()){ // key = hostname - memFree|memTotal|memUsed|swapFree|...
			if(key.endsWith("memFree")){
				ChartSeries series = new ChartSeries();
				series.setLabel("memFree");
				series.setData(this.xyplot.getEntries(key));
				mem.addSeries(series);
			}else if(key.endsWith("memUsed")){
				ChartSeries series = new ChartSeries();
				series.setLabel("memUsed");
				series.setData(this.xyplot.getEntries(key));
				mem.addSeries(series);
			}else if(key.endsWith("swapFree")){
				ChartSeries series = new ChartSeries();
				series.setLabel("swapFree");
				series.setData(this.xyplot.getEntries(key));
				swap.addSeries(series);
			}else if(key.endsWith("swapUsed")){
				ChartSeries series = new ChartSeries();
				series.setLabel("swapUsed");
				series.setData(this.xyplot.getEntries(key));
				swap.addSeries(series);
			}
		}
		this.memModel.setModel(mem);
		this.swapModel.setModel(swap);
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		this.updateXYPlot();
	}
}
