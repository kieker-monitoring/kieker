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

package kieker.tools.traceAnalysis.plugins;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import kieker.analysis.plugin.AbstractAnalysisPlugin;
import kieker.analysis.repository.AbstractRepository;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.systemModel.AllocationComponent;
import kieker.tools.traceAnalysis.systemModel.AssemblyComponent;
import kieker.tools.traceAnalysis.systemModel.ComponentType;
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.tools.traceAnalysis.systemModel.ExecutionContainer;
import kieker.tools.traceAnalysis.systemModel.Operation;
import kieker.tools.traceAnalysis.systemModel.Signature;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * 
 * @author Andre van Hoorn
 */
public abstract class AbstractTraceAnalysisPlugin extends AbstractAnalysisPlugin {

	public static final String CONFIG_NAME = AbstractTraceAnalysisPlugin.class.getName() + ".name";
	public static final String SYSTEM_MODEL_REPOSITORY_NAME = AbstractTraceAnalysisPlugin.class.getName() + ".systemModelRepository";
	private final String name;
	private final SystemModelRepository systemEntityFactory;

	public AbstractTraceAnalysisPlugin(final Configuration configuration, final Map<String, AbstractRepository> repositories) {
		super(configuration, repositories);

		/* Use the given repository if possible. */
		if (repositories.containsKey(AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME) && (repositories
				.get(AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME) instanceof SystemModelRepository)) {
			this.systemEntityFactory = (SystemModelRepository) repositories.get(AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME);
		} else {
			this.systemEntityFactory = null;
		}

		/* Try to load the name from the given configuration. */
		if (configuration.containsKey(AbstractTraceAnalysisPlugin.CONFIG_NAME)) {
			this.name = configuration.getStringProperty(AbstractTraceAnalysisPlugin.CONFIG_NAME);
		} else {
			this.name = null;
		}
	}

	/**
	 * Given a fully-qualified class name <i>fqClassName</i> (e.g., <code>a.b.c.D</code>) and
	 * and a {@link Signature} (e.g., for operation <code>op1</code> with modifiers <code>public</code> and <code>static</code>, the return type <code>Boolean</code>
	 * ,
	 * and the parameter types <code>Integer</code> and <code>Long</code>), the method
	 * returns an operation signature string (e.g., <code>public static Boolean a.b.c.D.op1(Integer, Long)</code>).
	 * 
	 * @param fqClassName
	 * @param signature
	 * @return
	 */
	public static String createOperationSignatureString(final String fqClassName, final Signature signature) {
		final StringBuilder strBuilder = new StringBuilder();

		if ((signature.getModifier().length == 0) && ((signature.getReturnType() == null) || signature.getReturnType().isEmpty())) {
			throw new IllegalArgumentException("Modifier list empty and return type null/empty");
		}

		/* Append modifiers and return type */
		if ((signature.getReturnType() != null) && !signature.getReturnType().isEmpty()) {
			for (final String type : signature.getModifier()) {
				strBuilder.append(type).append(" ");
			}
			strBuilder.append(signature.getReturnType()).append(" ");
		}

		/* Append operation name and parameter type list */
		strBuilder.append(fqClassName);
		strBuilder.append(".").append(signature.getName());
		strBuilder.append("(");
		boolean first = true;
		for (final String type : signature.getParamTypeList()) {
			if (!first) {
				strBuilder.append(", ");
			}
			first = false;
			strBuilder.append(type);
		}
		strBuilder.append(")");

		return strBuilder.toString();
	}

	/**
	 * Creates a signature from a signature string of format <code>operatioName(Type0,..,TypeN)</code>.
	 * The parameter type list wrapped by parentheses is optional.
	 * 
	 * @param operationSignatureStr
	 * @return
	 */
	public static Signature createSignature(final String operationSignatureStr) {
		final String returnType;
		String name;
		String[] paramTypeList;
		String[] modifierList;
		final int openParenIdx = operationSignatureStr.indexOf('(');
		final String modRetName;
		if (openParenIdx == -1) { // no parameter list
			paramTypeList = new String[] {};
			modRetName = operationSignatureStr;
		} else {
			modRetName = operationSignatureStr.substring(0, openParenIdx);
			final StringTokenizer strTokenizer = new StringTokenizer(operationSignatureStr.substring(openParenIdx + 1, operationSignatureStr.length() - 1), ",");
			paramTypeList = new String[strTokenizer.countTokens()];
			for (int i = 0; strTokenizer.hasMoreTokens(); i++) {
				paramTypeList[i] = strTokenizer.nextToken().trim();
			}
		}
		final int nameBeginIdx = modRetName.lastIndexOf(' ');
		if (nameBeginIdx == -1) {
			name = modRetName;
			// TODO: find package and name and return both separately, also a few lines below this
			returnType = "N/A";
			modifierList = new String[] {};
		} else {
			name = modRetName.substring(nameBeginIdx + 1);
			final String[] modRet = name.split("\\s");
			returnType = modRet[modRet.length - 1];
			modifierList = new String[modRet.length - 1];
			System.arraycopy(modRet, 0, modifierList, 0, modifierList.length);
		}
		return new Signature(name, modifierList, returnType, paramTypeList);
	}

	protected final Execution createExecutionByEntityNames(final String executionContainerName, final String componentTypeName,
			final String operationSignature, final long traceId, final String sessionId, final int eoi, final int ess,
			final long tin, final long tout) {
		// final String executionContainerName = execRec.getHostName();
		// final String componentTypeName = execRec.getClassName();
		final String assemblyComponentName = componentTypeName;
		final String allocationComponentName = new StringBuilder(executionContainerName).append("::").append(assemblyComponentName).toString();
		final String operationFactoryName = new StringBuilder(assemblyComponentName).append(".").append(operationSignature).toString();
		final String operationSignatureStr = operationSignature;

		AllocationComponent allocInst = this.getSystemEntityFactory().getAllocationFactory()
				.lookupAllocationComponentInstanceByNamedIdentifier(allocationComponentName);
		if (allocInst == null) { /* Allocation component instance doesn't exist */
			AssemblyComponent assemblyComponent = this.getSystemEntityFactory().getAssemblyFactory()
					.lookupAssemblyComponentInstanceByNamedIdentifier(assemblyComponentName);
			if (assemblyComponent == null) { // assembly instance doesn't exist
				ComponentType componentType = this.getSystemEntityFactory().getTypeRepositoryFactory().lookupComponentTypeByNamedIdentifier(componentTypeName);
				if (componentType == null) { // NOCS (NestedIf)
					/* Component type doesn't exist */
					componentType = this.getSystemEntityFactory().getTypeRepositoryFactory().createAndRegisterComponentType(componentTypeName, componentTypeName);
				}
				assemblyComponent = this.getSystemEntityFactory().getAssemblyFactory()
						.createAndRegisterAssemblyComponentInstance(assemblyComponentName, componentType);
			}
			ExecutionContainer execContainer = this.getSystemEntityFactory().getExecutionEnvironmentFactory()
					.lookupExecutionContainerByNamedIdentifier(executionContainerName);
			if (execContainer == null) { /* doesn't exist, yet */
				execContainer = this.getSystemEntityFactory().getExecutionEnvironmentFactory()
						.createAndRegisterExecutionContainer(executionContainerName, executionContainerName);
			}
			allocInst = this.getSystemEntityFactory().getAllocationFactory()
					.createAndRegisterAllocationComponentInstance(allocationComponentName, assemblyComponent, execContainer);
		}

		Operation op = this.getSystemEntityFactory().getOperationFactory().lookupOperationByNamedIdentifier(operationFactoryName);
		if (op == null) { /* Operation doesn't exist */
			final Signature signature = AbstractTraceAnalysisPlugin.createSignature(operationSignatureStr);
			op = this.getSystemEntityFactory().getOperationFactory()
					.createAndRegisterOperation(operationFactoryName, allocInst.getAssemblyComponent().getType(), signature);
			allocInst.getAssemblyComponent().getType().addOperation(op);
		}

		return new Execution(op, allocInst, traceId, sessionId, eoi, ess, tin, tout);
	}

	protected void printMessage(final String[] lines) {
		System.out.println("");
		System.out.println("#");
		System.out.println("# Plugin: " + this.name);
		for (final String l : lines) {
			System.out.println(l);
		}
	}

	protected final SystemModelRepository getSystemEntityFactory() {
		return this.systemEntityFactory;
	}

	@Override
	public Map<String, AbstractRepository> getCurrentRepositories() {
		final Map<String, AbstractRepository> map = new HashMap<String, AbstractRepository>();
		map.put(AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME, this.systemEntityFactory);
		return map;
	}
}
