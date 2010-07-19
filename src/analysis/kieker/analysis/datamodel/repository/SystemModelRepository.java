package kieker.analysis.datamodel.repository;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import kieker.analysis.datamodel.AllocationComponent;
import kieker.analysis.datamodel.AssemblyComponent;
import kieker.analysis.datamodel.ComponentType;
import kieker.analysis.datamodel.Execution;
import kieker.analysis.datamodel.ExecutionContainer;
import kieker.analysis.datamodel.Operation;
import kieker.analysis.datamodel.Signature;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2010 Kieker Project
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
 * ==================================================
 */
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
        ComponentType rootComponentType =
                new ComponentType(AbstractSystemSubRepository.ROOT_ELEMENT_ID, "$");
        this.typeRepositoryFactory = new TypeRepository(this, rootComponentType);
        AssemblyComponent rootAssemblyComponentInstance =
                new AssemblyComponent(AbstractSystemSubRepository.ROOT_ELEMENT_ID, "$", rootComponentType);
        this.assemblyFactory = new AssemblyRepository(this, rootAssemblyComponentInstance);
        ExecutionContainer rootExecutionContainer =
                new ExecutionContainer(AbstractSystemSubRepository.ROOT_ELEMENT_ID, null, "$");
        this.executionEnvironmentFactory = new ExecutionEnvironmentRepository(this, rootExecutionContainer);
        AllocationComponent rootAllocation =
                new AllocationComponent(AbstractSystemSubRepository.ROOT_ELEMENT_ID, rootAssemblyComponentInstance, rootExecutionContainer);
        this.allocationFactory = new AllocationRepository(this, rootAllocation);
        Signature rootSignature = new Signature("$", "<>", new String[]{});
        Operation rootOperation = new Operation(AbstractSystemSubRepository.ROOT_ELEMENT_ID, rootComponentType, rootSignature);
        this.operationFactory = new OperationRepository(this, rootOperation);
        this.rootExecution = new Execution(
                operationFactory.rootOperation,
                allocationFactory.rootAllocationComponent,
                -1, "-1", -1, -1, -1, -1);
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

        COMPONENT_TYPE, OPERATION, ASSEMBLY_COMPONENT,
        ALLOCATION_COMPONENT, EXECUTION_CONTAINER
    };

    private String htmlEntityLabel(int id, String caption, EntityType entityType) {
        StringBuilder strBuild = new StringBuilder();
        strBuild.append("<a name=\"").append(entityType).append("-").append(id).append("\">").append(caption).append("</a>");
        return strBuild.toString();
    }

    private String htmlEntityRef(int id, String caption, EntityType entityType) {
        StringBuilder strBuild = new StringBuilder();
        strBuild.append("<a href=\"#").append(entityType).append("-").append(id).append("\">").append(caption).append("</a>");
        return strBuild.toString();
    }

    private void printOpenHtmlTable(final PrintStream ps, final String[] columnTitle) {
        ps.println("<table border=\"1\"><tr>");
        for (String cell : columnTitle) {
            ps.println("<th>" + cell + "</th>");
        }
        ps.println("</tr>");
    }

    private void printHtmlTableRow(final PrintStream ps, final String[] cells) {
        ps.println("<tr class=\"cell\">");
        for (String cell : cells) {
            ps.println("<td>" + ((cell.length() == 0) ? "&nbsp;" : cell) + "</td>");
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
        StringBuilder strBuild = new StringBuilder("<pre>\n");
        for (int i = 0; i < numLines; i++) {
            strBuild.append(".\n");
        }
        strBuild.append("</pre>");
        ps.println(strBuild.toString());
    }

    public void saveSystemToHTMLFile(final String outputFnBase) throws FileNotFoundException {
        PrintStream ps = new PrintStream(new FileOutputStream(outputFnBase + ".html"));
        ps.println("<html><head>");
        ps.println("<style type=\"text/css\">\n"
                + ".cell { font-family:monospace; }\n"
                + "</style>");
        ps.println("</head><body>");
        htmlHSpace(ps, 10);
        ps.println("<h1>Component Types</h1>");
        printOpenHtmlTable(ps, new String[]{"ID", "package", "name", "operations"});
        Collection<ComponentType> componentTypes = this.typeRepositoryFactory.getComponentTypes();
        for (ComponentType type : componentTypes) {
            StringBuilder opListBuilder = new StringBuilder();
            if (type.getOperations().size() > 0) {
                //opListBuilder.append("<ul>");
                for (Operation op : type.getOperations()) {
                    opListBuilder.append("<li>").append(htmlEntityRef(op.getId(), op.getSignature().toString(), EntityType.OPERATION)).append("</li>");
                }
                //opListBuilder.append("</ul>");
            }
            String[] cells = new String[]{
                htmlEntityLabel(type.getId(), type.getId() + "", EntityType.COMPONENT_TYPE),
                type.getPackageName(),
                type.getTypeName(),
                opListBuilder.toString()
            };
            printHtmlTableRow(ps, cells);
        }
        printCloseHtmlTable(ps);
        ps.println("<h1>Operations</h1>");
        printOpenHtmlTable(ps, new String[]{"ID", "component type", "name", "parameter types", "return type"});
        Collection<Operation> operations = this.operationFactory.getOperations();
        for (Operation op : operations) {
            StringBuilder paramListStrBuild = new StringBuilder();
            //paramListStrBuild.append("<ul>");
            for (String paramType : op.getSignature().getParamTypeList()) {
                paramListStrBuild.append("<li>").append(paramType).append("</li>");
            }
            //paramListStrBuild.append("</ul>");
            String[] cells = new String[]{
                htmlEntityLabel(op.getId(), op.getId() + "", EntityType.OPERATION),
                htmlEntityRef(op.getComponentType().getId(), op.getComponentType().getFullQualifiedName(), EntityType.COMPONENT_TYPE),
                op.getSignature().getName(),
                paramListStrBuild.toString(),
                op.getSignature().getReturnType()
            };
            printHtmlTableRow(ps, cells);
        }
        printCloseHtmlTable(ps);
        ps.println("<h1>Assembled Components</h1>");
        printOpenHtmlTable(ps, new String[]{"ID", "name", "component type"});
        Collection<AssemblyComponent> assemblyComponents = this.assemblyFactory.getAssemblyComponentInstances();
        for (AssemblyComponent ac : assemblyComponents) {
            String[] cells = new String[]{
                htmlEntityLabel(ac.getId(), ac.getId() + "", EntityType.ASSEMBLY_COMPONENT),
                ac.getName(),
                htmlEntityRef(ac.getType().getId(), ac.getType().getFullQualifiedName(), EntityType.COMPONENT_TYPE),};
            printHtmlTableRow(ps, cells);
        }
        printCloseHtmlTable(ps);
        ps.println("<h1>Execution Containers</h1>");
        printOpenHtmlTable(ps, new String[]{"ID", "name"});
        Collection<ExecutionContainer> containers = this.executionEnvironmentFactory.getExecutionContainers();
        for (ExecutionContainer container : containers) {
            String[] cells = new String[]{
                htmlEntityLabel(container.getId(), container.getId() + "", EntityType.EXECUTION_CONTAINER),
                container.getName()
            };
            printHtmlTableRow(ps, cells);
        }
        printCloseHtmlTable(ps);
        ps.println("<h1>Allocated Components</h1>");
        printOpenHtmlTable(ps, new String[]{"ID", "assembly component", "execution container"});
        Collection<AllocationComponent> allocationComponentInstances = this.allocationFactory.getAllocationComponentInstances();
        for (AllocationComponent allocationComponent : allocationComponentInstances) {
            String[] cells = new String[]{
                htmlEntityLabel(allocationComponent.getId(), allocationComponent.getId() + "", EntityType.ALLOCATION_COMPONENT),
                htmlEntityRef(allocationComponent.getAssemblyComponent().getId(), allocationComponent.getAssemblyComponent().toString(), EntityType.ALLOCATION_COMPONENT),
                htmlEntityRef(allocationComponent.getExecutionContainer().getId(), allocationComponent.getExecutionContainer().getName(), EntityType.EXECUTION_CONTAINER)
            };
            printHtmlTableRow(ps, cells);
        }
        printCloseHtmlTable(ps);
        htmlHSpace(ps, 50);
        ps.println("</body></html>");
        ps.flush();
        ps.close();
    }
}
