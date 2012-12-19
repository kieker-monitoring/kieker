/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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
import java.io.UnsupportedEncodingException;
import java.util.Collection;

import kieker.analysis.IProjectContext;
import kieker.analysis.repository.AbstractRepository;
import kieker.analysis.repository.annotation.Repository;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.systemModel.AllocationComponent;
import kieker.tools.traceAnalysis.systemModel.AssemblyComponent;
import kieker.tools.traceAnalysis.systemModel.ComponentType;
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.tools.traceAnalysis.systemModel.ExecutionContainer;
import kieker.tools.traceAnalysis.systemModel.Operation;

/**
 * 
 * @author Andre van Hoorn
 */
@Repository(
		name = "System model repository",
		description = "Model manager for Kieker's component model ")
public class SystemModelRepository extends AbstractRepository {

	public static final Execution ROOT_EXECUTION =
			new Execution(OperationRepository.ROOT_OPERATION, AllocationRepository.ROOT_ALLOCATION_COMPONENT, -1, "-1", -1, -1, -1, -1, false);

	private static final String ENCODING = "UTF-8";

	private final TypeRepository typeRepositoryFactory;
	private final AssemblyRepository assemblyFactory;
	private final ExecutionEnvironmentRepository executionEnvironmentFactory;
	private final AllocationRepository allocationFactory;
	private final OperationRepository operationFactory;
	private final AllocationComponentOperationPairFactory allocationPairFactory;
	private final AssemblyComponentOperationPairFactory assemblyPairFactory;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration to use for this repository.
	 * @param projectContext
	 *            The project context to use for this repository.
	 * 
	 * @since 1.7
	 */
	public SystemModelRepository(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		this.typeRepositoryFactory = new TypeRepository(this);
		this.assemblyFactory = new AssemblyRepository(this);
		this.executionEnvironmentFactory = new ExecutionEnvironmentRepository(this);
		this.allocationFactory = new AllocationRepository(this);
		this.operationFactory = new OperationRepository(this);
		this.allocationPairFactory = new AllocationComponentOperationPairFactory(this);
		this.assemblyPairFactory = new AssemblyComponentOperationPairFactory(this);
	}

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration to use for this repository.
	 * 
	 * @deprecated
	 */
	@Deprecated
	public SystemModelRepository(final Configuration configuration) {
		this(configuration, null);
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

	public AllocationComponentOperationPairFactory getAllocationPairFactory() {
		return this.allocationPairFactory;
	}

	public AssemblyComponentOperationPairFactory getAssemblyPairFactory() {
		return this.assemblyPairFactory;
	}

	private static enum EntityType {

		COMPONENT_TYPE, OPERATION, ASSEMBLY_COMPONENT, ALLOCATION_COMPONENT, EXECUTION_CONTAINER
	};

	private String htmlEntityLabel(final int id, final String caption, final EntityType entityType) {
		final StringBuilder strBuild = new StringBuilder(64);
		strBuild.append("<a name=\"").append(entityType).append('-').append(id).append("\">").append(caption).append("</a>");
		return strBuild.toString();
	}

	private String htmlEntityRef(final int id, final String caption, final EntityType entityType) {
		final StringBuilder strBuild = new StringBuilder(64);
		strBuild.append("<a href=\"#").append(entityType).append('-').append(id).append("\">").append(caption).append("</a>");
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

	/**
	 * Writes the contents of this system model to an HTML file.
	 * 
	 * @param outputFn
	 *            file system location of the output file (as accepted by {@link java.io.File#File(String)}).
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public void saveSystemToHTMLFile(final String outputFn) throws FileNotFoundException, UnsupportedEncodingException {
		final PrintStream ps = new PrintStream(new FileOutputStream(outputFn), false, ENCODING);
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
					opListBuilder.append("<li>").append(this.htmlEntityRef(op.getId(), op.getSignature().toString(), EntityType.OPERATION))
							.append("</li>");
				}
			}
			final String[] cells = new String[] {
				this.htmlEntityLabel(type.getId(), Integer.toString(type.getId()), EntityType.COMPONENT_TYPE),
				type.getPackageName(), type.getTypeName(), opListBuilder.toString(), };
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
				op.getSignature().getName(), paramListStrBuild.toString(), op.getSignature().getReturnType(), };
			this.printHtmlTableRow(ps, cells);
		}
		this.printCloseHtmlTable(ps);
		ps.println("<h1>Assembly Components</h1>");
		this.printOpenHtmlTable(ps, new String[] { "ID", "Name", "Component type" });
		final Collection<AssemblyComponent> assemblyComponents = this.assemblyFactory.getAssemblyComponentInstances();
		for (final AssemblyComponent ac : assemblyComponents) {
			final String[] cells = new String[] {
				this.htmlEntityLabel(ac.getId(), Integer.toString(ac.getId()), EntityType.ASSEMBLY_COMPONENT),
				ac.getName(), this.htmlEntityRef(ac.getType().getId(), ac.getType().getFullQualifiedName(), EntityType.COMPONENT_TYPE), };
			this.printHtmlTableRow(ps, cells);
		}
		this.printCloseHtmlTable(ps);
		ps.println("<h1>Execution Containers</h1>");
		this.printOpenHtmlTable(ps, new String[] { "ID", "Name" });
		final Collection<ExecutionContainer> containers = this.executionEnvironmentFactory.getExecutionContainers();
		for (final ExecutionContainer container : containers) {
			final String[] cells = new String[] {
				this.htmlEntityLabel(container.getId(), Integer.toString(container.getId()), EntityType.EXECUTION_CONTAINER),
				container.getName(), };
			this.printHtmlTableRow(ps, cells);
		}
		this.printCloseHtmlTable(ps);
		ps.println("<h1>Deployment Components</h1>");
		this.printOpenHtmlTable(ps, new String[] { "ID", "Assembly component", "Execution container" });
		final Collection<AllocationComponent> allocationComponentInstances = this.allocationFactory.getAllocationComponentInstances();
		for (final AllocationComponent allocationComponent : allocationComponentInstances) {
			final String[] cells = new String[] {
				this.htmlEntityLabel(allocationComponent.getId(), Integer.toString(allocationComponent.getId()),
						EntityType.ALLOCATION_COMPONENT),
				this.htmlEntityRef(allocationComponent.getAssemblyComponent().getId(), allocationComponent.getAssemblyComponent().toString(),
						EntityType.ALLOCATION_COMPONENT),
				this.htmlEntityRef(allocationComponent.getExecutionContainer().getId(), allocationComponent.getExecutionContainer().getName(),
						EntityType.EXECUTION_CONTAINER), };
			this.printHtmlTableRow(ps, cells);
		}
		this.printCloseHtmlTable(ps);
		this.htmlHSpace(ps, 50);
		ps.println("</body></html>");
		ps.flush();
		ps.close();
	}

	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}
}
