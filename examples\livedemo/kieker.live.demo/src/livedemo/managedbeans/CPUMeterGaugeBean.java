package livedemo.managedbeans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import org.primefaces.model.chart.MeterGaugeChartModel;

import kieker.analysis.display.MeterGauge;

/**
 * @author Bjoern Weissenfels
 */
@ManagedBean(name="cpuMeterGaugeBean")
@ApplicationScoped
public class CPUMeterGaugeBean {
	
	@ManagedProperty(value = "#{analysisBean}")
	AnalysisBean analysisBean;
	
	private MeterGauge meterGauge;
	private List<MeterGaugeChartModel> meterGaugeModels;
	
	private List<String> cpuIds;
	private final String colors = "66cc66, E7E658, cc6666";
	private DashboardModel dashboardModel;
	private DashboardColumn column1;
	private DashboardColumn column2;
	
	public CPUMeterGaugeBean(){
		this.meterGaugeModels = Collections.synchronizedList(new ArrayList<MeterGaugeChartModel>());
		this.dashboardModel = new DefaultDashboardModel();
		this.column1 = new DefaultDashboardColumn();  
        this.column2 = new DefaultDashboardColumn(); 
        this.column1.addWidget("model0");
        this.column2.addWidget("model1");
        this.column1.addWidget("model2");
        this.column2.addWidget("model3");
        this.dashboardModel.addColumn(column1);
        this.dashboardModel.addColumn(column2);
	}
	
	@PostConstruct
	public void init(){
		this.meterGauge = this.analysisBean.getCPUUtilizationDisplayFilter().getMeterGauge();
		this.cpuIds = new ArrayList<String>(this.meterGauge.getKeys());
		Collections.sort(this.cpuIds);
		this.updateMeterGaugeModels();
	}
	
	public void setAnalysisBean(AnalysisBean analysisBean){
		this.analysisBean = analysisBean;
	}
	
	public DashboardModel getDashboardModel(){
		return this.dashboardModel;
	}
	
	//not used yet
	public List<MeterGaugeChartModel> getMeterGaugeModels(){
		this.meterGaugeModels.clear();
		this.updateMeterGaugeModels();
		return this.meterGaugeModels;
	}
	
	public String getColors(){
		return this.colors;
	}
	
	private void updateMeterGaugeModels(){
		for(String id : this.cpuIds){
			this.meterGaugeModels.add(new MeterGaugeChartModel(this.meterGauge.getValue(id), this.meterGauge.getIntervals(id)));
		}
	}
	
	public MeterGaugeChartModel getModel0(){
		this.meterGaugeModels.clear();
		this.updateMeterGaugeModels();
		return this.meterGaugeModels.get(0);
	}
	
	public MeterGaugeChartModel getModel1(){
		MeterGaugeChartModel m = this.meterGaugeModels.get(1);
		if(null != m){
			return m;
		}else{
			return new MeterGaugeChartModel(0, Arrays.asList((Number) 70,90,100));
		}
	}
	
	public MeterGaugeChartModel getModel2(){
		MeterGaugeChartModel m = this.meterGaugeModels.get(2);
		if(null != m){
			return m;
		}else{
			return new MeterGaugeChartModel(0, Arrays.asList((Number) 70,90,100));
		}	
	}
	
	public MeterGaugeChartModel getModel3(){
		MeterGaugeChartModel m = this.meterGaugeModels.get(3);
		if(null != m){
			return m;
		}else{
			return new MeterGaugeChartModel(0, Arrays.asList((Number) 70,90,100));
		}	
	}

}
