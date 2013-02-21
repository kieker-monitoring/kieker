package livedemo.managedbeans;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import kieker.tools.traceAnalysis.systemModel.AllocationComponent;
import kieker.tools.traceAnalysis.systemModel.AssemblyComponent;
import kieker.tools.traceAnalysis.systemModel.ComponentType;
import kieker.tools.traceAnalysis.systemModel.ExecutionContainer;
import kieker.tools.traceAnalysis.systemModel.Operation;

@ManagedBean(name="systemModelBean", eager=true)
@ApplicationScoped
public class SystemModelBean {
	
	@ManagedProperty(value = "#{dataBean}")
	DataBean dataBean;
	
	public SystemModelBean(){
	}
	
	public void setDataBean(DataBean dataBean){
		this.dataBean = dataBean;
	}
	
	public List<ComponentType> getComponentTypes(){
		return this.dataBean.getComponentTypes();
	}
	
	public List<Operation> getOperations(){
		return this.dataBean.getOperations();
	}
	
	public List<AssemblyComponent> getAssemblyComponents(){
		return this.dataBean.getAssemblyComponents();
	}
	
	public List<ExecutionContainer> getExecutionContainers(){
		return this.dataBean.getExecutionContainers();
	}
	
	public List<AllocationComponent> getDeploymentComponents(){
		return this.dataBean.getDeploymentComponents();
	}
	
}
