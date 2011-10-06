/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.tools.traceAnalysis.systemModel.repository;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Collection;

import kieker.tools.traceAnalysis.systemModel.AllocationComponent;
import kieker.tools.traceAnalysis.systemModel.AssemblyComponent;
import kieker.tools.traceAnalysis.systemModel.ComponentType;
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.tools.traceAnalysis.systemModel.ExecutionContainer;
import kieker.tools.traceAnalysis.systemModel.Operation;
import kieker.tools.traceAnalysis.systemModel.Signature;

/**
 * 
 * @author Andre van Hoorn
 */
public class SystemModelRepository {

	private final TypeRepository typeRepositoryFactory;
	private final AssemblyRepository assemblyFactory;
	private final ExecutionEnvironmentRepository executionEnvironmentFactory;
	private final AllocationRepository allocationFactory;
	private final OperationRepository operationFactory;
	private final Execution rootExecution;

	public Execution getRootExecution() {
		return this.rootExecution;
	}

	public SystemModelRepository() {
		final ComponentType rootComponentType = new ComponentType(AbstractSystemSubRepository.ROOT_ELEMENT_ID, "$");
		this.typeRepositoryFactory = new TypeRepository(this, rootComponentType);
		final AssemblyComponent rootAssemblyComponentInstance = new AssemblyComponent(AbstractSystemSubRepository.ROOT_ELEMENT_ID, "$", rootComponentType);
		this.assemblyFactory = new AssemblyRepository(this, rootAssemblyComponentInstance);
		final ExecutionContainer rootExecutionContainer = new ExecutionContainer(AbstractSystemSubRepository.ROOT_ELEMENT_ID, null, "$");
		this.executionEnvironmentFactory = new ExecutionEnvironmentRepository(this, rootExecutionContainer);
		final AllocationComponent rootAllocation = new AllocationComponent(AbstractSystemSubRepository.ROOT_ELEMENT_ID, rootAssemblyComponentInstance,
				rootExecutionContainer);
		this.allocationFactory = new AllocationRepository(this, rootAllocation);
		final Signature rootSignature = new Signature("$", "<>", new String[] {});
		final Operation rootOperation = new Operation(AbstractSystemSubRepository.ROOT_ELEMENT_ID, rootComponentType, rootSignature);
		this.operationFactory = new OperationRepository(this, rootOperation);
		this.rootExecution = new Execution(this.operationFactory.rootOperation, this.allocationFactory.rootAllocationComponent, -1, "-1", -1, -1, -1, -1);
	}

	public final AllocationRepository getAllocationFactory() {
		return this.allocationFactory;
	}

	public final AssemblyRepository getAssemblyFactory() {
		return this.assemblyFactory;
	}

	public final ExecutionEnvironmentRepository getExecutionEnvironmentFactory() {
		return this.executionEnvironmentFactory;
	}

	public final OperationRepository getOperationFactory() {
		return this.operationFactory;
	}

	public final TypeRepository getTypeRepositoryFactory() {
		return this.typeRepositoryFactory;
	}

	private enum EntityType {

		COMPONENT_TYPE, OPERATION, ASSEMBLY_COMPONENT, ALLOCATION_COMPONENT, EXECUTION_CONTAINER
	};

	private String htmlEntityLabel(final int id, final String caption, final EntityType entityType) {
		final StringBuilder strBuild = new StringBuilder();
		strBuild.append("<a name=\"").append(entityType).append("-").append(id).append("\">").append(caption).append("</a>");
		return strBuild.toString();
	}

	private String htmlEntityRef(final int id, final String caption, final EntityType entityType) {
		final StringBuilder strBuild = new StringBuilder();
		strBuild.append("<a href=\"#").append(entityType).append("-").append(id).append("\">").append(caption).append("</a>");
		return strBuild.toString();
	}

	private void printOpenHtmlTable(final PrintStream ps, final String[] columnTitle) {
		ps.println("<table border=\"1\"><tr>");
		for (final String cell : columnTitle) {
			ps.println("<th class=\"colTitle\">" + cell + "</th>");
		}
		ps.println("</tr>");
	}

	private void printHtmlTableRow(final PrintStream ps, final String[] cells) {
		ps.println("<tr class=\"cell\">");
		for (final String cell : cells) {
			ps.println("<td>" + ((cell.length() == 0) ? "&nbsp;" : cell) + "</td>"); // NOCS
		}
		ps.println("</tr>");
	}

	private void printCloseHtmlTable(final PrintStream ps) {
		ps.println("</table>");
	}

	private void htmlHSpace(final PrintStream ps, final int numLines) {
		if (numLines <= 0) {
			return;
		}
		final StringBuilder strBuild = new StringBuilder("<pre>\n");
		for (int i = 0; i < numLines; i++) {
			strBuild.append(".\n");
		}
		strBuild.append("</pre>");
		ps.println(strBuild.toString());
	}

	public void saveSystemToHTMLFile(final String outputFnBase) throws FileNotFoundException {
		final PrintStream ps = new PrintStream(new FileOutputStream(outputFnBase + ".html"));
		ps.println("<html><head><title>System Model Reconstructed by Kieker.TraceAnalysis</title>");
		ps.println("<style type=\"text/css\">\n" + ".colTitle { font-family:sans; font-size:11px; }\n" + ".cell { font-family:monospace; font-size:10px; }\n"
				+ "h1 { font-family:sans; font-size:14px; }\n" + "</style>");
		ps.println("</head><body>");
		this.htmlHSpace(ps, 10);
		ps.println("<h1>Component Types</h1>");
		this.printOpenHtmlTable(ps, new String[] { "ID", "Package", "Name", "Operations" });
		final Collection<ComponentType> componentTypes = this.typeRepositoryFactory.getComponentTypes();
		for (final ComponentType type : componentTypes) {
			final StringBuilder opListBuilder = new StringBuilder();
			if (type.getOperations().size() > 0) {
				for (final Operation op : type.getOperations()) {
					opListBuilder.append("<li>").append(this.htmlEntityRef(op.getId(), op.getSignature().toString(), EntityType.OPERATION)).append("</li>");
				}
			}
			final String[] cells = new String[] { this.htmlEntityLabel(type.getId(), Integer.toString(type.getId()), EntityType.COMPONENT_TYPE),
				type.getPackageName(), type.getTypeName(), opListBuilder.toString() };
			this.printHtmlTableRow(ps, cells);
		}
		this.printCloseHtmlTable(ps);
		ps.println("<h1>Operations</h1>");
		this.printOpenHtmlTable(ps, new String[] { "ID", "Component type", "Name", "Parameter types", "Return type" });
		final Collection<Operation> operations = this.operationFactory.getOperations();
		for (final Operation op : operations) {
			final StringBuilder paramListStrBuild = new StringBuilder();
			for (final String paramType : op.getSignature().getParamTypeList()) {
				paramListStrBuild.append("<li>").append(paramType).append("</li>");
			}
			final String[] cells = new String[] { this.htmlEntityLabel(op.getId(), Integer.toString(op.getId()), EntityType.OPERATION),
				this.htmlEntityRef(op.getComponentType().getId(), op.getComponentType().getFullQualifiedName(), EntityType.COMPONENT_TYPE),
				op.getSignature().getName(), paramListStrBuild.toString(), op.getSignature().getReturnType() };
			this.printHtmlTableRow(ps, cells);
		}
		this.printCloseHtmlTable(ps);
		ps.println("<h1>Assembly Components</h1>");
		this.printOpenHtmlTable(ps, new String[] { "ID", "Name", "Component type" });
		final Collection<AssemblyComponent> assemblyComponents = this.assemblyFactory.getAssemblyComponentInstances();
		for (final AssemblyComponent ac : assemblyComponents) {
			final String[] cells = new String[] { this.htmlEntityLabel(ac.getId(), Integer.toString(ac.getId()), EntityType.ASSEMBLY_COMPONENT), ac.getName(),
				this.htmlEntityRef(ac.getType().getId(), ac.getType().getFullQualifiedName(), EntityType.COMPONENT_TYPE), };
			this.printHtmlTableRow(ps, cells);
		}
		this.printCloseHtmlTable(ps);
		ps.println("<h1>Execution Containers</h1>");
		this.printOpenHtmlTable(ps, new String[] { "ID", "Name" });
		final Collection<ExecutionContainer> containers = this.executionEnvironmentFactory.getExecutionContainers();
		for (final ExecutionContainer container : containers) {
			final String[] cells = new String[] { this.htmlEntityLabel(container.getId(), Integer.toString(container.getId()), EntityType.EXECUTION_CONTAINER),
				container.getName() };
			this.printHtmlTableRow(ps, cells);
		}
		this.printCloseHtmlTable(ps);
		ps.println("<h1>Deployment Components</h1>");
		this.printOpenHtmlTable(ps, new String[] { "ID", "Assembly component", "Execution container" });
		final Collection<AllocationComponent> allocationComponentInstances = this.allocationFactory.getAllocationComponentInstances();
		for (final AllocationComponent allocationComponent : allocationComponentInstances) {
			final String[] cells = new String[] {
				this.htmlEntityLabel(allocationComponent.getId(), Integer.toString(allocationComponent.getId()), EntityType.ALLOCATION_COMPONENT),
				this.htmlEntityRef(allocationComponent.getAssemblyComponent().getId(), allocationComponent.getAssemblyComponent().toString(),
						EntityType.ALLOCATION_COMPONENT),
				this.htmlEntityRef(allocationComponent.getExecutionContainer().getId(), allocationComponent.getExecutionContainer().getName(),
						EntityType.EXECUTION_CONTAINER) };
			this.printHtmlTableRow(ps, cells);
		}
		this.printCloseHtmlTable(ps);
		this.htmlHSpace(ps, 50);
		ps.println("</body></html>");
		ps.flush();
		ps.close();
	}
}
