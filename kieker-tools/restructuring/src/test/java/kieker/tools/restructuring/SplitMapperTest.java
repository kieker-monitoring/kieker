/***************************************************************************
 * Copyright (C) 2023 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package kieker.tools.restructuring;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map.Entry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import kieker.tools.restructuring.mapper.ComponentsMapper;
import kieker.tools.restructuring.stages.exec.RestructureStepFinder;
import kieker.tools.restructuring.util.TransformationFactory;

import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyFactory;
import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.assembly.AssemblyOperation;

class SplitMapperTest {

	private AssemblyModel orig;
	private AssemblyModel goal;

	private HashMap<String, String> opToCG;
	private HashMap<String, String> opToCO;

	@BeforeEach
	void initOrig() {
		/// AssemblyPackage.eINSTANCE;
		AssemblyFactory factory = AssemblyFactory.eINSTANCE;

		this.orig = factory.createAssemblyModel();
		this.opToCO = new HashMap();
		AssemblyComponent a = factory.createAssemblyComponent();
		AssemblyComponent b = factory.createAssemblyComponent();
		AssemblyComponent c = factory.createAssemblyComponent();

		this.orig.getComponents().put("A", a);
		this.orig.getComponents().put("B", b);
		this.orig.getComponents().put("C", c);
		// this.model.getComponents().add(c2);

		AssemblyOperation opA1 = factory.createAssemblyOperation();
		AssemblyOperation opA2 = factory.createAssemblyOperation();
		AssemblyOperation opA3 = factory.createAssemblyOperation();
		AssemblyOperation opA4 = factory.createAssemblyOperation();
		AssemblyOperation opA5 = factory.createAssemblyOperation();

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
		this.orig.getComponents().get("A").getOperations().put("a4", opA4);
		this.opToCO.put("a4", "A");
		this.orig.getComponents().get("A").getOperations().put("a5", opA5);
		this.opToCO.put("a5", "A");

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

		AssemblyComponent d = factory.createAssemblyComponent();
		AssemblyComponent e = factory.createAssemblyComponent();
		AssemblyComponent f = factory.createAssemblyComponent();

		this.goal.getComponents().put("1", a);
		this.goal.getComponents().put("2", b);
		this.goal.getComponents().put("3", c);
		this.goal.getComponents().put("4", d);
		this.goal.getComponents().put("5", e);
		this.goal.getComponents().put("6", f);
		// this.model.getComponents().add(c2);

		AssemblyOperation opA1 = factory.createAssemblyOperation();
		AssemblyOperation opA2 = factory.createAssemblyOperation();
		AssemblyOperation opA3 = factory.createAssemblyOperation();
		AssemblyOperation opA4 = factory.createAssemblyOperation();
		AssemblyOperation opA5 = factory.createAssemblyOperation();

		AssemblyOperation opB1 = factory.createAssemblyOperation();
		AssemblyOperation opB2 = factory.createAssemblyOperation();

		AssemblyOperation opC1 = factory.createAssemblyOperation();
		AssemblyOperation opC2 = factory.createAssemblyOperation();

		this.goal.getComponents().get("1").getOperations().put("b1", opB1);
		this.opToCG.put("b1", "1");
		this.goal.getComponents().get("1").getOperations().put("c1", opC1);
		this.opToCG.put("c1", "1");
		this.goal.getComponents().get("1").getOperations().put("c2", opC2);
		this.opToCG.put("c2", "1");
		this.goal.getComponents().get("1").getOperations().put("b2", opB2);
		this.opToCG.put("b2", "1");

		this.goal.getComponents().get("2").getOperations().put("a1", opA1);
		this.opToCG.put("a1", "2");

		this.goal.getComponents().get("3").getOperations().put("a2", opA2);
		this.opToCG.put("a2", "3");

		this.goal.getComponents().get("4").getOperations().put("a3", opA3);
		this.opToCG.put("a3", "4");

		this.goal.getComponents().get("5").getOperations().put("a4", opA4);
		this.opToCG.put("a4", "5");

		this.goal.getComponents().get("6").getOperations().put("a5", opA5);
		this.opToCG.put("a5", "6");
		// this.goal.getComponents().get("C").getOperations().put("c2", opC2);
	}

	// needs fixing, same reason as in TestRestructureStepFInder
	// @Test
	// void test() {
	// ComponentsMapper mapper = new ComponentsMapper(this.orig, this.goal);
	// RestructureStepFinder stepFinder = new RestructureStepFinder(this.orig, this.goal,mapper);
	// stepFinder.findTransformation();
	// System.out.println("orig");
	// for(int i = 0; i<orig.getComponents().size(); i++) {
	// //for(Entry<String, AssemblyComponent>e:orig.getComponents().entrySet()) {
	// Entry<String, AssemblyComponent>e = orig.getComponents().get(i);
	// System.out.println(e.getKey());
	// for(int j = 0; j<e.getValue().getOperations().size(); j++){
	// //for(Entry<String, AssemblyOperation>e2:e.getValue().getOperations().entrySet()) {
	// Entry<String, AssemblyOperation>e2 = e.getValue().getOperations().get(j);
	// System.out.println(" "+ e2.getKey());
	// }
	// }
	//
	// System.out.println("\n goal");
	// for(int i = 0; i<goal.getComponents().size(); i++) {
	// Entry<String, AssemblyComponent>e = goal.getComponents().get(i);
	// // for(Entry<String, AssemblyComponent>e:goal.getComponents().entrySet()) {
	// System.out.println(e.getKey());
	// for(int j = 0; j<e.getValue().getOperations().size(); j++){
	// Entry<String, AssemblyOperation>e2 = e.getValue().getOperations().get(j);
	// //for(Entry<String, AssemblyOperation>e2:e.getValue().getOperations().entrySet()) {
	// System.out.println(" "+ e2.getKey());
	// }
	// }
	// assertTrue(TransformationFactory.areSameModels(goal, orig));
	// }
}
