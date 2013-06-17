package livedemo.managedbeans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import org.primefaces.model.chart.MeterGaugeChartModel;

import kieker.analysis.display.MeterGauge;
import livedemo.entities.Model;

/**
 * @author Bjoern Weissenfels
 */
@ManagedBean(name="cpuMeterGaugeBean")
@ApplicationScoped
public class CPUMeterGaugeBean implements Observer{
	
	@ManagedProperty(value = "#{analysisBean}")
	private AnalysisBean analysisBean;
	
	private MeterGauge meterGauge;
	private List<Model<MeterGaugeChartModel>> meterGaugeModels;
	
	private List<String> cpuIds;
	private final String colors = "66cc66, E7E658, cc6666";
	private DashboardModel dashboardModel;
	private DashboardColumn column1;
	private DashboardColumn column2;
	
	public CPUMeterGaugeBean(){
		this.meterGaugeModels = Collections.synchronizedList(new ArrayList<Model<MeterGaugeChartModel>>());
		this.dashboardModel = new DefaultDashboardModel();
		this.column1 = new DefaultDashboardColumn();  
        this.column2 = new DefaultDashboardColumn();
	}
	
	@PostConstruct
	public void init(){
		this.meterGauge = this.analysisBean.getCPUUtilizationDisplayFilter().getMeterGauge();
		this.cpuIds = new ArrayList<String>(this.meterGauge.getKeys());
		Collections.sort(this.cpuIds);
		this.updateMeterGaugeModels();
		this.column1.addWidget("test");
		for(int i=0; i < this.cpuIds.size(); i+=2){
			this.column1.addWidget("model" + i);
			if(this.cpuIds.size() > i+1){
				this.column2.addWidget("model" + (i+1));
			}
			
		}
		this.dashboardModel.addColumn(column1);
        this.dashboardModel.addColumn(column2);
		this.analysisBean.getUpdateThread().addObserver(this);
	}
	
	@PreDestroy
	public void terminate(){
		this.analysisBean.getUpdateThread().deleteObserver(this);
	}
	
	public void setAnalysisBean(AnalysisBean analysisBean){
		this.analysisBean = analysisBean;
	}
	
	public DashboardModel getDashboardModel(){
		return this.dashboardModel;
	}
	
	public List<Model<MeterGaugeChartModel>> getMeterGaugeModels(){
		return this.meterGaugeModels;
	}
	
	public String getColors(){
		return this.colors;
	}
	
	private void updateMeterGaugeModels(){
		for(String id : this.cpuIds){
			MeterGaugeChartModel meterGaugeChartModel = new MeterGaugeChartModel(this.meterGauge.getValue(id), this.meterGauge.getIntervals(id));
			Model<MeterGaugeChartModel> model = new Model<MeterGaugeChartModel>(meterGaugeChartModel, id);
			String value = id.substring(id.length() - 1);
			model.addId("model" + value);
			model.addId("test" + value);
			this.meterGaugeModels.add(model);
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		this.meterGaugeModels.clear();
		this.updateMeterGaugeModels();
	}

}
