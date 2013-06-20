package livedemo.managedbeans;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import org.primefaces.model.tagcloud.DefaultTagCloudItem;
import org.primefaces.model.tagcloud.DefaultTagCloudModel;
import org.primefaces.model.tagcloud.TagCloudModel;

/**
 * @author Bjoern Weissenfels
 */
@ManagedBean(name="tagCloudBean")
@ApplicationScoped
public class TagCloudBean implements Observer {
	
	@ManagedProperty(value = "#{analysisBean}")
	private AnalysisBean analysisBean;
	
	private Map<String, AtomicLong> methodMap;
	private TagCloudModel methodModel;
	private Map<String, AtomicLong> componentMap;
	private TagCloudModel componentModel;
	
	private DashboardModel dashboardModel;
	private DashboardColumn column1;
	private DashboardColumn column2;
	
	public TagCloudBean(){
		this.methodModel = new DefaultTagCloudModel();
		this.componentModel = new DefaultTagCloudModel();
		this.dashboardModel = new DefaultDashboardModel();
		this.column1 = new DefaultDashboardColumn();  
        this.column2 = new DefaultDashboardColumn();
        this.column1.addWidget("c1");
        this.column2.addWidget("c2");
        this.dashboardModel.addColumn(column1);
        this.dashboardModel.addColumn(column2);
	}
	
	@PostConstruct
	public void init(){
		this.methodMap = this.analysisBean.getTagCloudFilter().methodTagCloudDisplay().getCounters();
		this.componentMap = this.analysisBean.getTagCloudFilter().componentTagCloudDisplay().getCounters();
		this.analysisBean.getUpdateThread().addObserver(this);
	}
	
	@PreDestroy
	public void terminate(){
		this.analysisBean.getUpdateThread().deleteObserver(this);
	}
	
	public DashboardModel getDashboardModel(){
		return this.dashboardModel;
	}
	
	public TagCloudModel getMethodModel(){
		return this.methodModel;
	}
	
	public TagCloudModel getComponentModel(){
		return this.componentModel;
	}
	
	public void setAnalysisBean(AnalysisBean analysisBean){
		this.analysisBean = analysisBean;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		this.methodModel.clear();
		for(String key : this.methodMap.keySet()){
			int value = this.methodMap.get(key).intValue();
			this.methodModel.addTag(new DefaultTagCloudItem(key, value));
		}
		this.componentModel.clear();
		for(String key : this.componentMap.keySet()){
			int value = this.componentMap.get(key).intValue();
			this.componentModel.addTag(new DefaultTagCloudItem(key, value));
		}
	}

}
