package livedemo.managedbeans;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
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
	
	private String outputFn = "webapps/kiekerLiveDemo/resources/system.html";
	String model = "";

	@ManagedProperty(value = "#{startingBean}")
	StartingBean startingBean;
	
	public SystemModelBean(){
	}
	
	public void setStartingBean(StartingBean startingBean){
		this.startingBean = startingBean;
	}
	
	public List<ComponentType> getComponentTypes(){
		List<ComponentType> ctList = new ArrayList<ComponentType>();
		Collection<ComponentType> collection = this.startingBean.getSystemModelRepository().getTypeRepositoryFactory().getComponentTypes();
		ctList.addAll(collection);
		return ctList;
	}
	
	public List<Operation> getOperations(){
		List<Operation> opList = new ArrayList<Operation>();
		Collection<Operation> collection = this.startingBean.getSystemModelRepository().getOperationFactory().getOperations();
		opList.addAll(collection);
		return opList;
	}
	
	public List<AssemblyComponent> getAssemblyComponents(){
		List<AssemblyComponent> acList = new ArrayList<AssemblyComponent>();
		Collection<AssemblyComponent> collection = this.startingBean.getSystemModelRepository().getAssemblyFactory().getAssemblyComponentInstances();
		acList.addAll(collection);
		return acList;
	}
	
	public List<ExecutionContainer> getExecutionContainers(){
		List<ExecutionContainer> ecList = new ArrayList<ExecutionContainer>();
		Collection<ExecutionContainer> collection = this.startingBean.getSystemModelRepository().getExecutionEnvironmentFactory().getExecutionContainers();
		ecList.addAll(collection);
		return ecList;
	}
	
	public List<AllocationComponent> getDeploymentComponents(){
		List<AllocationComponent> acList = new ArrayList<AllocationComponent>();
		Collection<AllocationComponent> collection = this.startingBean.getSystemModelRepository().getAllocationFactory().getAllocationComponentInstances();
		acList.addAll(collection);
		return acList;
	}
	
	public String getModel(){
		try {
			this.startingBean.getSystemModelRepository().saveSystemToHTMLFile(outputFn);
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return this.model;
	}
}
