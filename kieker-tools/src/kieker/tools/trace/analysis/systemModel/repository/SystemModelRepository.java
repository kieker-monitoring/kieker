/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.trace.analysis.systemModel.repository;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;

import kieker.analysis.IProjectContext;
import kieker.analysis.repository.AbstractRepository;
import kieker.analysis.repository.annotation.Repository;
import kieker.common.configuration.Configuration;
import kieker.tools.trace.analysis.systemModel.AllocationComponent;
import kieker.tools.trace.analysis.systemModel.AssemblyComponent;
import kieker.tools.trace.analysis.systemModel.ComponentType;
import kieker.tools.trace.analysis.systemModel.Execution;
import kieker.tools.trace.analysis.systemModel.ExecutionContainer;
import kieker.tools.trace.analysis.systemModel.Operation;

/**
 * This repository is a model manager for the Kieker's component model. It consists of multiple "sub"repositories.
 *
 * @author Andre van Hoorn
 *
 * @since 1.1
 */
@Repository(
		name = "System model repository",
		description = "Model manager for Kieker's component model ")
public class SystemModelRepository extends AbstractRepository {

	public static final Execution ROOT_EXECUTION = new Execution(OperationRepository.ROOT_OPERATION, AllocationRepository.ROOT_ALLOCATION_COMPONENT, -1, "-1", -1,
			-1, -1, -1, false);

	public static final String ROOT_NODE_LABEL = "'Entry'";

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

	public final AllocationRepository getAllocationFactory() {
		return this.allocationFactory;
	}

	public final AssemblyRepository getAssemblyFactory() {
		return this.assemblyFactory;
	}

	public final ExecutionEnvironmentRepository getExecutionEnvironmentFactory() {
		return this.executionEnvironmentFactory;
	}

	/**
	 * Delivering the factory managing the available operations.
	 *
	 * @return The operation factory.
	 */
	public final OperationRepository getOperationFactory() {
		return this.operationFactory;
	}

	/**
	 * Delivering the factory managing the available component types.
	 *
	 * @return The types factory.
	 */
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
	}

	private String htmlEntityLabel(final int id, final String caption, final EntityType entityType) {
		final StringBuilder strBuild = new StringBuilder(64);
		strBuild.append("<a name=\"").append(SystemModelRepository.simpleHTMLEscape(entityType.toString())).append('-').append(id).append("\">").append(caption)
				.append("</a>");
		return strBuild.toString();
	}

	private String htmlEntityRef(final int id, final String caption, final EntityType entityType) {
		final StringBuilder strBuild = new StringBuilder(64);
		strBuild.append("<a href=\"#").append(SystemModelRepository.simpleHTMLEscape(entityType.toString())).append('-').append(id).append("\">").append(caption)
				.append("</a>");
		return strBuild.toString();
	}

	private void printOpenHtmlTable(final PrintStream ps, final String title, final String[] columnTitle) {
		ps.println("<table class=\"tab\" border=\"1\" style=\"width:100%\">");
		ps.println("<tr><th class=\"tabTitle\" colspan=\"" + columnTitle.length + "\">" + title + "</th></tr>");
		ps.println("<tr>");
		for (final String cell : columnTitle) {
			ps.println("<th class=\"colTitle space\">" + cell + "</th>");
		}
		ps.println("</tr>");
	}

	private void printHtmlTableRow(final PrintStream ps, final String[] cells) {
		ps.println("<tr class=\"cell\">");
		for (final String cell : cells) {
			ps.println("<td class=\"space\">" + ((cell.length() == 0) ? "&nbsp;" : cell) + "</td>"); // NOCS
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
	 * @throws IOException
	 *             on io error
	 */
	public void saveSystemToHTMLFile(final String outputFn) throws IOException {
		final PrintStream ps = new PrintStream(Files.newOutputStream(Paths.get(outputFn)), false, ENCODING);
		ps.println("<html><head><title>System Model Reconstructed by Kieker.TraceAnalysis</title>");
		ps.println("<style type=\"text/css\">\n"
				+ ".colTitle {font-size: 11px; background: linear-gradient(to bottom, #FDFDFD, #DDDDDD) transparent }\n"
				+ ".cell {font-family: monospace; font-size: 10px; font-family: inherited}\n"
				+ ".tabTitle {padding: 4px 4px; font-size: 12px; background: linear-gradient(to bottom, #FFFFFF, #CCEEFF) transparent; border: 1px solid #4DC4FF;"
				+ "color: #333399}\n"
				+ ".tab {border-collapse: collapse;  border: 1px solid #9D9D9D; font-family: \"Segoe UI\", \"Verdana\", \"Arial\", sans-serif}\n"
				+ ".space{padding: 4px 10px;}\n" + "</style>");
		ps.println("</head><body>");
		this.htmlHSpace(ps, 10);
		this.printOpenSurroundingSpan(ps);
		this.printOpenHtmlTable(ps, "Component Types", new String[] { "ID", "Package", "Name", "Operations" });
		final Collection<ComponentType> componentTypes = this.typeRepositoryFactory.getComponentTypes();
		for (final ComponentType type : componentTypes) {
			final StringBuilder opListBuilder = new StringBuilder();
			if (type.getOperations().size() > 0) {
				for (final Operation op : type.getOperations()) {
					opListBuilder.append("<li>")
							.append(this.htmlEntityRef(op.getId(), SystemModelRepository.simpleHTMLEscape(op.getSignature().toString()), EntityType.OPERATION))
							.append("</li>");
				}
			}
			final String[] cells = {
				this.htmlEntityLabel(type.getId(), Integer.toString(type.getId()), EntityType.COMPONENT_TYPE),
				SystemModelRepository.simpleHTMLEscape(type.getPackageName()), SystemModelRepository.simpleHTMLEscape(type.getTypeName()),
				opListBuilder.toString(), };
			this.printHtmlTableRow(ps, cells);
		}
		this.printCloseHtmlTable(ps);
		this.printLinebreak(ps);
		this.printOpenHtmlTable(ps, "Operations", new String[] { "ID", "Component type", "Name", "Parameter types", "Return type" });
		final Collection<Operation> operations = this.operationFactory.getOperations();
		for (final Operation op : operations) {
			final StringBuilder paramListStrBuild = new StringBuilder();
			for (final String paramType : op.getSignature().getParamTypeList()) {
				paramListStrBuild.append("<li>").append(SystemModelRepository.simpleHTMLEscape(paramType)).append("</li>");
			}
			final String[] cells = {
				this.htmlEntityLabel(op.getId(), Integer.toString(op.getId()), EntityType.OPERATION),
				this.htmlEntityRef(op.getComponentType().getId(), SystemModelRepository.simpleHTMLEscape(op.getComponentType().getFullQualifiedName()),
						EntityType.COMPONENT_TYPE),
				SystemModelRepository.simpleHTMLEscape(op.getSignature().getName()), paramListStrBuild.toString(),
				SystemModelRepository.simpleHTMLEscape(op.getSignature().getReturnType()), };
			this.printHtmlTableRow(ps, cells);
		}
		this.printCloseHtmlTable(ps);
		this.printLinebreak(ps);
		this.printOpenHtmlTable(ps, "Assembly Components", new String[] { "ID", "Name", "Component type" });
		final Collection<AssemblyComponent> assemblyComponents = this.assemblyFactory.getAssemblyComponentInstances();
		for (final AssemblyComponent ac : assemblyComponents) {
			final String[] cells = {
				this.htmlEntityLabel(ac.getId(), Integer.toString(ac.getId()), EntityType.ASSEMBLY_COMPONENT),
				ac.getName(),
				this.htmlEntityRef(ac.getType().getId(), SystemModelRepository.simpleHTMLEscape(ac.getType().getFullQualifiedName()), EntityType.COMPONENT_TYPE), };
			this.printHtmlTableRow(ps, cells);
		}
		this.printCloseHtmlTable(ps);
		this.printLinebreak(ps);
		this.printOpenHtmlTable(ps, "Execution Containers", new String[] { "ID", "Name" });
		final Collection<ExecutionContainer> containers = this.executionEnvironmentFactory.getExecutionContainers();
		for (final ExecutionContainer container : containers) {
			final String[] cells = {
				this.htmlEntityLabel(container.getId(), Integer.toString(container.getId()), EntityType.EXECUTION_CONTAINER),
				SystemModelRepository.simpleHTMLEscape(container.getName()), };
			this.printHtmlTableRow(ps, cells);
		}
		this.printCloseHtmlTable(ps);
		this.printLinebreak(ps);
		this.printOpenHtmlTable(ps, "Deployment Components", new String[] { "ID", "Assembly component", "Execution container" });
		final Collection<AllocationComponent> allocationComponentInstances = this.allocationFactory.getAllocationComponentInstances();
		for (final AllocationComponent allocationComponent : allocationComponentInstances) {
			final String[] cells = {
				this.htmlEntityLabel(allocationComponent.getId(), Integer.toString(allocationComponent.getId()),
						EntityType.ALLOCATION_COMPONENT),
				this.htmlEntityRef(allocationComponent.getAssemblyComponent().getId(),
						SystemModelRepository.simpleHTMLEscape(allocationComponent.getAssemblyComponent().toString()),
						EntityType.ALLOCATION_COMPONENT),
				this.htmlEntityRef(allocationComponent.getExecutionContainer().getId(),
						SystemModelRepository.simpleHTMLEscape(allocationComponent.getExecutionContainer().getName()),
						EntityType.EXECUTION_CONTAINER), };
			this.printHtmlTableRow(ps, cells);
		}
		this.printCloseHtmlTable(ps);
		this.printCloseSurroundingSpan(ps);
		this.htmlHSpace(ps, 50);
		ps.println("</body></html>");
		ps.flush();
		ps.close();
	}

	private void printOpenSurroundingSpan(final PrintStream ps) {
		ps.println("<span style=\"display: inline-block\">");
	}

	private void printCloseSurroundingSpan(final PrintStream ps) {
		ps.println("</span>");
	}

	private void printLinebreak(final PrintStream ps) {
		ps.println("<br/>");
	}

	private static String simpleHTMLEscape(final String input) {
		return input.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#x27;").replace("/", "&#x2F;");
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}
}
