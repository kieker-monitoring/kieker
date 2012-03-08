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

import java.util.StringTokenizer;

import kieker.analysis.plugin.AbstractAnalysisPlugin;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
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
@Plugin(repositoryPorts = @RepositoryPort(name = AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME, repositoryType = SystemModelRepository.class))
public abstract class AbstractTraceAnalysisPlugin extends AbstractAnalysisPlugin {
	private static final Log LOG = LogFactory.getLog(AbstractTraceAnalysisPlugin.class);

	public static final String SYSTEM_MODEL_REPOSITORY_NAME = "systemModelRepository";

	private volatile SystemModelRepository systemEntityFactory;

	public AbstractTraceAnalysisPlugin(final Configuration configuration) {
		super(configuration);
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

		if ((signature.getModifier().length != 0) && (!signature.hasReturnType())) {
			throw new IllegalArgumentException("Modifier not list empty but return type null/empty");
		}

		/* Append modifiers and return type */
		if ((signature.getReturnType() != null) && !signature.getReturnType().isEmpty()) {
			for (final String type : signature.getModifier()) {
				strBuilder.append(type).append(" ");
			}
			if (signature.hasReturnType()) {
				strBuilder.append(signature.getReturnType()).append(" ");
			}
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

	// TODO: Move this class to an appropriate package
	public static class FQComponentNameSignaturePair {
		private final String fqClassname;
		private final Signature signature;

		/**
		 * @param fqClassname
		 * @param signature
		 */
		public FQComponentNameSignaturePair(final String fqClassname, final Signature signature) {
			this.fqClassname = fqClassname;
			this.signature = signature;
		}

		/**
		 * @return the fqClassname
		 */
		public String getFqClassname() {
			return this.fqClassname;
		}

		/**
		 * @return the signature
		 */
		public Signature getSignature() {
			return this.signature;
		}
	}

	/**
	 * Extracts an {@link FQComponentNameSignaturePair} from an an operation signature string (e.g., <code>public static Boolean a.b.c.D.op1(Integer, Long)</code>).
	 * Modifier list, return type, and parameter list wrapped by parentheses are optional. But note that
	 * a return type must be given if one or more modifiers are present.
	 * 
	 * @param operationSignatureStr
	 * @return
	 */
	public static FQComponentNameSignaturePair splitOperationSignatureStr(final String operationSignatureStr) {
		final String fqClassname;
		final String returnType;
		String name;
		String opName;
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
			returnType = null;
			modifierList = new String[] {};
		} else {
			// name = modRetName.substring(nameBeginIdx + 1);
			final String[] modRetNameArr = modRetName.split("\\s");
			name = modRetNameArr[modRetNameArr.length - 1];
			returnType = modRetNameArr[modRetNameArr.length - 2];
			modifierList = new String[modRetNameArr.length - 2];
			System.arraycopy(modRetNameArr, 0, modifierList, 0, modifierList.length);
		}

		final int opNameIdx = name.lastIndexOf('.');
		if (opNameIdx != -1) {
			fqClassname = name.substring(0, opNameIdx);
		} else {
			fqClassname = "";
		}
		opName = name.substring(opNameIdx + 1);

		return new FQComponentNameSignaturePair(fqClassname, new Signature(opName, modifierList, returnType, paramTypeList));
	}

	protected final Execution createExecutionByEntityNames(final String executionContainerName, final String componentTypeName,
			final Signature operationSignature, final long traceId, final String sessionId, final int eoi, final int ess,
			final long tin, final long tout, final boolean assumed) {
		// final String executionContainerName = execRec.getHostName();
		// final String componentTypeName = execRec.getClassName();
		final String assemblyComponentName = componentTypeName;
		final String allocationComponentName = new StringBuilder(executionContainerName).append("::").append(assemblyComponentName).toString();
		final String operationFactoryName = new StringBuilder(assemblyComponentName).append(".").append(operationSignature).toString();

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
			op = this.getSystemEntityFactory().getOperationFactory()
					.createAndRegisterOperation(operationFactoryName, allocInst.getAssemblyComponent().getType(), operationSignature);
			allocInst.getAssemblyComponent().getType().addOperation(op);
		}

		return new Execution(op, allocInst, traceId, sessionId, eoi, ess, tin, tout, assumed);
	}

	protected void printMessage(final String[] lines) {
		System.out.println("");
		System.out.println("#");
		System.out.println("# Plugin: " + this.getName());
		for (final String l : lines) {
			System.out.println(l);
		}
	}

	protected final SystemModelRepository getSystemEntityFactory() {
		if (this.systemEntityFactory == null) {
			this.systemEntityFactory = (SystemModelRepository) this.getRepository(AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME);
		}
		if (this.systemEntityFactory == null) {
			AbstractTraceAnalysisPlugin.LOG.error("Failed to connect to system model repository via repository port '"
					+ AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME + "' (not connected?)");
		}
		return this.systemEntityFactory;
	}
}
