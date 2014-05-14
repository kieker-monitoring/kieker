package livedemo.managedbeans;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.primefaces.model.chart.CartesianChartModel;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
@ManagedBean(name = "jvmDataDisplayBean")
@ApplicationScoped
public class JVMDataDisplayBean {

	@ManagedProperty(value = "#{analysisBean}")
	private AnalysisBean analysisBean;

	private CartesianChartModel gcCountModel;
	private CartesianChartModel gcTimeModel;
	private CartesianChartModel compilationModel;
	private CartesianChartModel classLoadingModel;
	private CartesianChartModel threadsStatusModel;

	public JVMDataDisplayBean() {}

	@PostConstruct
	protected void initialize() {
		this.gcCountModel = this.analysisBean.getGcCountDisplayFilter().getChartModel();
		this.gcTimeModel = this.analysisBean.getGcTimeDisplayFilter().getChartModel();
		this.compilationModel = this.analysisBean.getJitCompilationDisplayFilter().getChartModel();
		this.classLoadingModel = this.analysisBean.getClassLoadingDisplayFilter().getChartModel();
		this.threadsStatusModel = this.analysisBean.getThreadsStatusDisplayFilter().getChartModel();
	}

	public void setAnalysisBean(final AnalysisBean analysisBean) {
		this.analysisBean = analysisBean;
	}

	public CartesianChartModel getGcCountModel() {
		return this.gcCountModel;
	}

	public CartesianChartModel getGcTimeModel() {
		return this.gcTimeModel;
	}

	public CartesianChartModel getCompilationModel() {
		return this.compilationModel;
	}

	public CartesianChartModel getClassLoadingModel() {
		return this.classLoadingModel;
	}

	public CartesianChartModel getThreadsStatusModel() {
		return this.threadsStatusModel;
	}

}
