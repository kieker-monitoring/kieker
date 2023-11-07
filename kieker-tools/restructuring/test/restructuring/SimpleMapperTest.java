

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.oceandsl.tools.restructuring.stages.exec.mapper.Matcher;

import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyFactory;
import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.assembly.AssemblyOperation;

class SimpleMapperTest {

	private AssemblyModel orig;
	private AssemblyModel goal;

	private HashMap <String, String> opToCG;
	private HashMap <String, String> opToCO;
	@BeforeEach
	void initOrig() {
		///AssemblyPackage.eINSTANCE;
		AssemblyFactory factory = AssemblyFactory.eINSTANCE;
		
		this.orig = factory.createAssemblyModel();
		this.opToCO = new HashMap();
        AssemblyComponent a = factory.createAssemblyComponent();
        AssemblyComponent b = factory.createAssemblyComponent();
        AssemblyComponent c = factory.createAssemblyComponent();
        
       
        this.orig.getComponents().put("A", a);
        this.orig.getComponents().put("B", b);
        this.orig.getComponents().put("C", c);
        //  this.model.getComponents().add(c2);
        
        
        AssemblyOperation opA1 = factory.createAssemblyOperation();
        AssemblyOperation opA2 = factory.createAssemblyOperation();
        AssemblyOperation opA3 = factory.createAssemblyOperation();
        
        AssemblyOperation opB1 = factory.createAssemblyOperation();
        AssemblyOperation opB2 = factory.createAssemblyOperation();
        
        AssemblyOperation opC1 = factory.createAssemblyOperation();
        AssemblyOperation opC2 = factory.createAssemblyOperation();

        
        this.orig.getComponents().get("A").getOperations().put("a1", opA1);
        this.opToCO.put("a1", "A");
        this.orig.getComponents().get("A").getOperations().put("a2", opA2);
        this.opToCO.put("a2", "A");
        this.orig.getComponents().get("A").getOperations().put("a3", opA3);
        this.opToCO.put("a3", "A");
        
        this.orig.getComponents().get("B").getOperations().put("b1", opB1);
        this.opToCO.put("b1", "B");
        this.orig.getComponents().get("B").getOperations().put("b2", opB2);
        this.opToCO.put("b2", "B");
        
        this.orig.getComponents().get("C").getOperations().put("c1", opC1);
        this.opToCO.put("c1", "C");
        this.orig.getComponents().get("C").getOperations().put("c2", opC2);
        this.opToCO.put("c2", "C");
	}
	
	@BeforeEach
	void initGoal() {
		AssemblyFactory factory = AssemblyFactory.eINSTANCE;
		
		this.goal = factory.createAssemblyModel();
		this.opToCG = new HashMap();
		
        AssemblyComponent a = factory.createAssemblyComponent();
        AssemblyComponent b = factory.createAssemblyComponent();
        AssemblyComponent c = factory.createAssemblyComponent();
        

        this.goal.getComponents().put("1", a);
        this.goal.getComponents().put("2", b);
        this.goal.getComponents().put("3", c);
        //  this.model.getComponents().add(c2);
        
        
        AssemblyOperation opA1 = factory.createAssemblyOperation();
        AssemblyOperation opA2 = factory.createAssemblyOperation();
        AssemblyOperation opA3 = factory.createAssemblyOperation();
        
        AssemblyOperation opB1 = factory.createAssemblyOperation();
        AssemblyOperation opB2 = factory.createAssemblyOperation();
        
        AssemblyOperation opC1 = factory.createAssemblyOperation();
        AssemblyOperation opC2 = factory.createAssemblyOperation();

        
        this.goal.getComponents().get("1").getOperations().put("a1", opA1);
        this.opToCG.put("a1", "1");
        this.goal.getComponents().get("1").getOperations().put("b1", opB1);
        this.opToCG.put("b1", "1");
        this.goal.getComponents().get("1").getOperations().put("c1", opC1);
        this.opToCG.put("c1", "1");
        
        
        this.goal.getComponents().get("2").getOperations().put("c2", opC2);
        this.opToCG.put("c2", "2");
        this.goal.getComponents().get("2").getOperations().put("b2", opB2);
        this.opToCG.put("b2", "2");
        this.goal.getComponents().get("3").getOperations().put("a3", opA3);
        this.opToCG.put("a3", "3");
        this.goal.getComponents().get("2").getOperations().put("a2", opA2);
	}
	@Test
	void test() {
		Matcher mapper = new Matcher(this.orig, this.goal);
		HashMap<String, String> oToCG=mapper.getOperationToComponentG();
		HashMap<String, String> oToCO=mapper.getOperationToComponentO();
	//	assertTrue(oToCG.equals(this.opToCG));
		//assertTrue(oToCO.equals(this.opToCO));
		
		HashMap<String, String> oToG=mapper.getOriginallToGoal();
		HashMap<String, String> gToO=mapper.getGoalToOriginal();
		System.out.println(oToG.toString());
		System.out.println(gToO.toString());
		assertTrue(oToG.size()==gToO.size());
	}

}
