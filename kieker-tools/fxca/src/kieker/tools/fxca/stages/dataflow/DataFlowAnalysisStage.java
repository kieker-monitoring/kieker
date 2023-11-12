/***************************************************************************
 * Copyright (C) 2023 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.fxca.stages.dataflow;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.w3c.dom.Node;

import kieker.analysis.code.CodeUtils;
import kieker.model.analysismodel.execution.EDirection;
import kieker.tools.fxca.model.CommonBlock;
import kieker.tools.fxca.model.FortranModule;
import kieker.tools.fxca.model.FortranOperation;
import kieker.tools.fxca.model.FortranParameter;
import kieker.tools.fxca.model.FortranProject;
import kieker.tools.fxca.model.FortranVariable;
import kieker.tools.fxca.model.IDataflowEndpoint;
import kieker.tools.fxca.stages.dataflow.data.CallerCalleeDataflow;
import kieker.tools.fxca.stages.dataflow.data.CommonBlockArgumentDataflow;
import kieker.tools.fxca.stages.dataflow.data.CommonBlockEntry;
import kieker.tools.fxca.stages.dataflow.data.DataflowEndpoint;
import kieker.tools.fxca.stages.dataflow.data.IDataflowEntry;
import kieker.tools.fxca.stages.dataflow.data.MultipleDataflowEndpoint;
import kieker.tools.fxca.utils.NodeUtils;
import kieker.tools.fxca.utils.Pair;
import kieker.tools.fxca.utils.Predicates;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

public class DataFlowAnalysisStage extends AbstractConsumerStage<FortranProject> {

	private final OutputPort<CommonBlockEntry> commonBlockOutputPort = this.createOutputPort(CommonBlockEntry.class);
	private final OutputPort<IDataflowEntry> dataflowOutputPort = this.createOutputPort(IDataflowEntry.class);

	public OutputPort<CommonBlockEntry> getCommonBlockOutputPort() {
		return this.commonBlockOutputPort;
	}

	public OutputPort<IDataflowEntry> getDataflowOutputPort() {
		return this.dataflowOutputPort;
	}

	@Override
	protected void execute(final FortranProject project) throws Exception {
		project.getModules().values().stream().forEach(module -> {
			this.logger.debug("Dataflow analysis for {}", module.getFileName());
			module.getCommonBlocks().values().forEach(commonBlock -> this.analyzeCommonBlock(module, commonBlock));
			module.getOperations().values().forEach(operation -> this.analyzeOperation(project, module, operation));
		});
	}

	private void analyzeCommonBlock(final FortranModule module, final CommonBlock commonBlock) {
		final CommonBlockEntry entry = new CommonBlockEntry(commonBlock.getName());
		entry.getModules().add(module);
		commonBlock.getVariables().values().forEach(variable -> entry.getVariables().add(variable.getName()));
		this.commonBlockOutputPort.send(entry);
	}

	private void analyzeOperation(final FortranProject project, final FortranModule module,
			final FortranOperation operation) {
		// common blocks specified explicitly in this subroutine
		operation.getCommonBlocks().values().forEach(commonBlock -> {
			this.analyzeCommonBlock(module, commonBlock);
			this.analyzeCommonBlockOperationDataflow(module, operation, commonBlock);
		});
		// common blocks specified explicitly in the module/file of the operation
		operation.getModule().getCommonBlocks().values()
				.forEach(commonBlock -> this.analyzeCommonBlockOperationDataflow(module, operation, commonBlock));

		// call based dataflow
		NodeUtils
				.findAllSiblings(operation.getNode(), o -> true,
						Predicates.isEndSubroutineStatement.or(Predicates.isEndFunctionStatement))
				.forEach(node -> this.analyzeStatement(project, module, operation, node));
	}

	private void analyzeCommonBlockOperationDataflow(final FortranModule module, final FortranOperation operation,
			final CommonBlock commonBlock) {
		final boolean parametersInvolved = operation.getParameters().values().stream()
				.anyMatch(parameter -> commonBlock.getVariables().containsKey(parameter.getName()));

		if (operation.getVariables().values().stream()
				.anyMatch(variable -> commonBlock.getVariables().containsKey(variable.getName())
						|| this.sourcesContainedInCommonBlock(variable.getSources(), commonBlock))) {
			EDirection direction = this.computeVariableCommonBlockDirection(operation, commonBlock);
			if (parametersInvolved) {
				direction = this.computeParameterCommonBlockDirection(operation, commonBlock, direction);
			}

			this.dataflowOutputPort.send(new CommonBlockArgumentDataflow(commonBlock.getName(), module.getFileName(),
					module.getModuleName(), operation.getName(), direction));
		} else if (parametersInvolved) {
			final EDirection direction = this.computeParameterCommonBlockDirection(operation, commonBlock,
					EDirection.NONE);

			this.dataflowOutputPort.send(new CommonBlockArgumentDataflow(commonBlock.getName(), module.getFileName(),
					module.getModuleName(), operation.getName(), direction));
		}
		// if no variable and no parameter uses something from the common block
	}

	private boolean sourcesContainedInCommonBlock(final Set<IDataflowEndpoint> sources, final CommonBlock commonBlock) {
		return sources.stream().anyMatch(source -> commonBlock.getVariables().containsKey(source.getName()));
	}

	private EDirection computeParameterCommonBlockDirection(final FortranOperation operation,
			final CommonBlock commonBlock, final EDirection direction) {
		return operation.getParameters().values().stream().map(parameter -> {
			if (commonBlock.getVariables().containsKey(parameter.getName())) {
				return parameter.getDirection();
			} else {
				return EDirection.NONE;
			}
		}).reduce(direction, (d, n) -> CodeUtils.merge(d, n));
	}

	private EDirection computeVariableCommonBlockDirection(final FortranOperation operation,
			final CommonBlock commonBlock) {
		return operation.getVariables().values().stream().map(variable -> {
			if (commonBlock.getVariables().containsKey(variable.getName())) {
				return this.resolveDirection(variable);
			} else {
				return variable.getSources().stream().map(source -> this.findDirection(source, commonBlock))
						.reduce(EDirection.NONE, (d, n) -> CodeUtils.merge(d, n));
			}
		}).reduce(EDirection.NONE, (d, n) -> CodeUtils.merge(d, n));
	}

	private EDirection findDirection(final IDataflowEndpoint source, final CommonBlock commonBlock) {
		if (source instanceof FortranVariable) {
			final FortranVariable sourceVariable = (FortranVariable) source;
			if (commonBlock.getVariables().containsKey(sourceVariable.getName())) {
				return this.resolveDirection(sourceVariable);
			} else {
				return EDirection.NONE;
			}
		} else { // must be parameter
			final FortranParameter sourceParameter = (FortranParameter) source;
			if (commonBlock.getVariables().containsKey(sourceParameter.getName())) {
				return sourceParameter.getDirection();
			} else {
				return EDirection.NONE;
			}
		}
	}

	private EDirection resolveDirection(final FortranVariable variable) {
		if (variable.getDirection() == EDirection.BOTH) {
			return EDirection.BOTH;
		} else if (variable.getSources().size() > 0) {
			return variable.getSources().stream().map(source -> {
				if (source instanceof FortranVariable) {
					return ((FortranVariable) source).getDirection();
				} else if (source instanceof FortranParameter) {
					return ((FortranParameter) source).getDirection();
				} else {
					return EDirection.NONE;
				}
			}).reduce(variable.getDirection(), (o, n) -> CodeUtils.merge(o, n));
		} else {
			return variable.getDirection();
		}
	}

	private void analyzeStatement(final FortranProject project, final FortranModule module,
			final FortranOperation operation, final Node statement) {
		if (Predicates.isCallStatement.test(statement)) {
			this.analyzeCallStatement(project, module, operation, statement);
		} else if (Predicates.isAssignmentStatement.test(statement)) {
			this.analyzeAssignmentStatement(project, module, operation, statement);
		} else if (Predicates.isIfStatement.test(statement)) {
			this.analyzeIfStatement(project, module, operation, statement);
		} else if (Predicates.isIfThenStatement.test(statement)) {
			this.analyzeIfThenStatement(project, module, operation, statement);
		} else if (Predicates.isElseIfStatement.test(statement)) {
			this.analyzeElseIfStatement(project, module, operation, statement);
		} else if (Predicates.isSelectCaseStatement.test(statement)) {
			this.analyzeSelectCaseStatement(project, module, operation, statement);
		} else if (Predicates.isDataStatement.test(statement)) {
			this.analyzeDataStatement();
		} else if (Predicates.isDoStatement.or(Predicates.isDoLabelStatement).test(statement)) {
			this.analyzeDoStatement(project, module, operation, statement);
		} else if (Predicates.isSaveStatement.test(statement)) {
			this.analyzeSaveArgument();
		} else if (Predicates.isWhereStatement.test(statement)) {
			this.analyzeWhereStatement(project, module, operation, statement);
		} else if (Predicates.isAllocateStatement.or(Predicates.isC).or(Predicates.isCloseStatement) // NOPMD
				.or(Predicates.isProgramStatement).or(Predicates.isCommonStatement).or(Predicates.isContinueStatement)
				.or(Predicates.isDeallocateStatement).or(Predicates.isDIMStatement).or(Predicates.isElseStatement)
				.or(Predicates.isEndFileStatement).or(Predicates.isEndStatement).or(Predicates.isExitStatement)
				.or(Predicates.isFile).or(Predicates.isFormatStatement).or(Predicates.isGotoStatement)
				.or(Predicates.isImplicitNoneStmt).or(Predicates.isInclude).or(Predicates.isInquireStatement)
				.or(Predicates.isLabel).or(Predicates.isM).or(Predicates.isNamelistStatement)
				.or(Predicates.isOpenStatement).or(Predicates.isOperationStatement).or(Predicates.isParameterStatement)
				.or(Predicates.isPrintStatement).or(Predicates.isReadStatement).or(Predicates.isReturnStatement)
				.or(Predicates.isRewindStatement).or(Predicates.isStopStatement).or(Predicates.isTDeclStmt)
				.or(Predicates.isWriteStatement).or(Predicates.isExternalStatement).test(statement)) {
			// ignore
		} else if (statement.getNodeType() == Node.TEXT_NODE) { // NOPMD
			// ignore
		} else {
			this.logger.error("Error: Unsupported statement by {}: {} ", this.getClass().getSimpleName(), statement);
		}
	}

	private void analyzeSaveArgument() {
		// save variables after termination of a subroutine
		// https://docs.oracle.com/cd/E19957-01/805-4939/6j4m0vnb7/index.html
	}

	private void analyzeDataStatement() {
		// nothing to be done here, as read/write access is determined in other stage
	}

	private void analyzeWhereStatement(final FortranProject project, final FortranModule module,
			final FortranOperation operation, final Node statement) {
		final Node maskExpression = NodeUtils.findChildFirst(statement, Predicates.isMaskExpression);
		final Node expression = NodeUtils.findChildFirst(maskExpression, Predicates.isExpression);
		final IDataflowEndpoint endpoint = this.analyzeExpression(project, module, operation, expression);
		this.createFlowFromEndpointToOperation(endpoint, module, operation);
	}

	/**
	 * Analyze a call statement for data flow. The dataflow is from all variables and parameters of
	 * the operation, module and common blocks, to the called subroutine and potentially invoked
	 * functions.
	 *
	 * @param project
	 *            project context
	 * @param module
	 *            module context
	 * @param operation
	 *            operation context
	 * @param statement
	 *            statement
	 */
	private void analyzeCallStatement(final FortranProject project, final FortranModule module,
			final FortranOperation operation, final Node statement) {
		// get the called subroutine
		final String calleeName = NodeUtils.getCalleeNameFromCall(statement);
		final Pair<FortranModule, FortranOperation> callee = this.findOperation(project.getModules().values(),
				calleeName);
		// get all arguments for the call
		final Node argumentSpecification = NodeUtils.findChildFirst(statement, Predicates.isArgumentSpecification);
		if (argumentSpecification != null) {
			final List<Node> arguments = NodeUtils.findAllSiblings(argumentSpecification.getFirstChild(),
					Predicates.isArgument, o -> false);
			for (int i = 0; i < arguments.size(); i++) {
				this.analyzeArgument(project, module, operation, callee.first, callee.second,
						arguments.get(i).getFirstChild(), i);
			}
		}
	}

	private void analyzeAssignmentStatement(final FortranProject project, final FortranModule module,
			final FortranOperation operation, final Node statement) {
		final Node expessionNode = NodeUtils.getAssignmentExpression(statement);
		if (Predicates.isAssignmentE2.test(expessionNode)) {
			final Node content = NodeUtils.findChildFirst(expessionNode, Predicates.isExpression);
			if (content != null) {
				final IDataflowEndpoint endpoint = this.analyzeExpression(project, module, operation, content);
				this.createFlowFromEndpointToOperation(endpoint, module, operation);
			}
		} else {
			this.logger.warn("Unknown expression type {}", statement.toString());
		}
	}

	private void analyzeDoStatement(final FortranProject project, final FortranModule module,
			final FortranOperation operation, final Node statement) {
		final Node doV = NodeUtils.findChildFirst(statement, Predicates.isDoV);
		if (doV == null) { // do while
			final Node testExpression = NodeUtils.findChildFirst(statement, Predicates.isTestExpression);
			final Node expression = NodeUtils.findChildFirst(testExpression, Predicates.isExpression);
			this.analyzeExpression(project, module, operation, expression);
		} else { // do statement or do label statement
			final String elementName = NodeUtils.getName(doV);
			final IDataflowEndpoint writeEndpoint = this.findEndpoint(module, operation, elementName);

			this.analyzeLimit(project, module, operation, NodeUtils.findChildFirst(statement, Predicates.isLowerBound),
					writeEndpoint);
			this.analyzeLimit(project, module, operation, NodeUtils.findChildFirst(statement, Predicates.isUpperBound),
					writeEndpoint);
		}
	}

	private void analyzeLimit(final FortranProject project, final FortranModule module,
			final FortranOperation operation, final Node boundary, final IDataflowEndpoint writeEndpoint) {
		if (boundary != null) {
			final Node expression = NodeUtils.findChildFirst(boundary, Predicates.isExpression);
			if (expression != null) {
				final IDataflowEndpoint readEndpoint = this.analyzeExpression(project, module, operation, expression);
				this.createFlowFromEndpointToOperation(writeEndpoint, readEndpoint);
			}
		}
	}

	private void analyzeSelectCaseStatement(final FortranProject project, final FortranModule module,
			final FortranOperation operation, final Node statement) {
		// TODO add support for case statement
		this.logger.error("CASE statement not supoprted {} {} {} {}", project, module, operation, statement);
	}

	private void analyzeIfStatement(final FortranProject project, final FortranModule module,
			final FortranOperation operation, final Node statement) {
		final Node condition = NodeUtils.findChildFirst(statement, Predicates.isConditionExpression);
		final Node expression = NodeUtils.findChildFirst(condition, Predicates.isExpression);
		this.createFlowFromEndpointToOperation(this.analyzeExpression(project, module, operation, expression), module,
				operation);

		final Node actionStatement = NodeUtils.findChildFirst(statement, Predicates.isActionStatement);

		NodeUtils.findAllSiblings(actionStatement.getFirstChild(), Predicates.isStatement, o -> false)
				.forEach(containedStatement -> this.analyzeStatement(project, module, operation, containedStatement));
	}

	private void analyzeIfThenStatement(final FortranProject project, final FortranModule module,
			final FortranOperation operation, final Node statement) {
		final Node condition = NodeUtils.findChildFirst(statement, Predicates.isConditionExpression);
		final Node expression = NodeUtils.findChildFirst(condition, Predicates.isExpression);
		this.createFlowFromEndpointToOperation(this.analyzeExpression(project, module, operation, expression), module,
				operation);
	}

	private void analyzeElseIfStatement(final FortranProject project, final FortranModule module,
			final FortranOperation operation, final Node statement) {
		final Node condition = NodeUtils.findChildFirst(statement, Predicates.isConditionExpression);
		final Node expression = NodeUtils.findChildFirst(condition, Predicates.isExpression);
		this.createFlowFromEndpointToOperation(this.analyzeExpression(project, module, operation, expression), module,
				operation);
	}

	/**
	 * Analyze the dataflow of one argument. Target of the flow is the called subroutine (callee),
	 * the variable and parameter context is the context. In case the argument is the argument of a
	 * function, then the result of the function is send to the caller function.
	 *
	 * @param project
	 * @param contextModule
	 * @param contextOperation
	 * @param callerModule
	 * @param operation2
	 * @param first
	 * @param second
	 * @param firstChild
	 * @param i
	 */
	private void analyzeArgument(final FortranProject project, final FortranModule contextModule,
			final FortranOperation contextOperation, final FortranModule calleeModule,
			final FortranOperation calleeOperation, final Node argumentNode, final int argumentIndex) {
		// get the callee's parameter declaration
		final Optional<FortranParameter> calleeParameter = this.findCalleeParameter(calleeOperation, argumentIndex);
		if (calleeParameter.isEmpty()) {
			this.logger.error("Error: Context {}::{}: No parameter exists for argument index {} of {}::{}",
					contextModule.getFileName(), contextOperation.getName(), argumentIndex, calleeModule.getFileName(),
					calleeOperation.getName());
		}
		final IDataflowEndpoint endpoint = this.analyzeExpression(project, contextModule, contextOperation,
				argumentNode);
		this.createFlowFromEndpointToOperation(endpoint, calleeModule, calleeOperation);
	}

	private void createFlowFromEndpointToOperation(final IDataflowEndpoint endpoint, final FortranModule calleeModule,
			final FortranOperation calleeOperation) {
		if (endpoint != null) {
			if (endpoint instanceof DataflowEndpoint) {
				final DataflowEndpoint simpleEndpoint = (DataflowEndpoint) endpoint;
				this.dataflowOutputPort.send(new CallerCalleeDataflow(simpleEndpoint.getModule().getFileName(),
						simpleEndpoint.getModule().getModuleName(), simpleEndpoint.getOperation().getName(),
						calleeModule.getFileName(), calleeModule.getModuleName(), calleeOperation.getName(),
						simpleEndpoint.getDirection()));
				// System.err.printf("CREATE FLOW from %s -> %s::%s\n", endpoint.toString(),
				// calleeModule.getFileName(),
				// calleeOperation.getName());
			} else if (endpoint instanceof MultipleDataflowEndpoint) {
				((MultipleDataflowEndpoint) endpoint).getEndpoints().forEach(e -> {
					this.dataflowOutputPort.send(new CallerCalleeDataflow(e.getModule().getFileName(),
							e.getModule().getModuleName(), e.getOperation().getName(), calleeModule.getFileName(),
							calleeModule.getModuleName(), calleeOperation.getName(), e.getDirection()));
					// System.err.printf("CREATE FLOW from %s -> %s::%s\n", e.toString(),
					// calleeModule.getFileName(),
					// calleeOperation.getName());
				});
			}
		}
	}

	private IDataflowEndpoint findEndpoint(final FortranModule module, final FortranOperation operation,
			final String elementName) {
		final FortranParameter parameterOperation = operation.getParameters().get(elementName);
		if (parameterOperation != null) {
			return parameterOperation;
		}

		final FortranVariable variableOperation = operation.getVariables().get(elementName);
		if (variableOperation != null) {
			return variableOperation;
		}

		final FortranVariable variableModule = module.getVariables().get(elementName);
		if (variableModule != null) {
			return variableModule;
		}

		return null;
	}

	private void createFlowFromEndpointToOperation(final IDataflowEndpoint writeEndpoint,
			final IDataflowEndpoint readEndpoint) {
		if (readEndpoint instanceof DataflowEndpoint) {
			final DataflowEndpoint dataflowEndpoint = (DataflowEndpoint) readEndpoint;
			this.createFlowFromEndpointToOperation(writeEndpoint, dataflowEndpoint.getModule(),
					dataflowEndpoint.getOperation());
		} else if (readEndpoint instanceof MultipleDataflowEndpoint) {
			((MultipleDataflowEndpoint) readEndpoint).getEndpoints().forEach(e -> {
				this.createFlowFromEndpointToOperation(writeEndpoint, e);
			});
		} else if (readEndpoint == null) { // NOPMD skip
		} else {
			this.logger.error("Internal error: Illegal read endpoint {}", readEndpoint.toString());
		}
	}

	/**
	 * The dataflow is from the expression to the callee. The context provides parameter and
	 * variable accesses.
	 *
	 * @param project
	 *            project context
	 * @param contextModule
	 * @param contextOperation
	 * @param calleeModule
	 * @param calleeOperation
	 * @param expressionNode
	 *            data source node
	 */
	private IDataflowEndpoint analyzeExpression(final FortranProject project, final FortranModule contextModule,
			final FortranOperation contextOperation, final Node expressionNode) {
		if (Predicates.isNamedExpressionAccess.test(expressionNode)) {
			return this.analyzeNamedExpressionAccess(project, contextModule, contextOperation, expressionNode);
		} else if (Predicates.isNamedExpression.test(expressionNode)) {
			return this.analyzeNamedExpressionAccess(project, contextModule, contextOperation, expressionNode);
		} else if (Predicates.isOperandExpression.test(expressionNode)) {
			return this.analyzeOperationExpression(project, contextModule, contextOperation, expressionNode);
		} else if (Predicates.isParensExpression.test(expressionNode)) {
			return this.analyzeExpression(project, contextModule, contextOperation,
					expressionNode.getFirstChild().getNextSibling());
		} else if (Predicates.isLiteralE.or(Predicates.isStringE).or(Predicates.isM).test(expressionNode)) {
			// skip
			return null;
		} else {
			this.logger.error("Unsupported expression part found in dataflow analysis: {}", expressionNode);
			return new DataflowEndpoint(contextModule, contextOperation, null, EDirection.NONE);
		}
	}

	/**
	 * An operation may contain many sub elements which serve as data endpoint.
	 *
	 * @param project
	 * @param contextModule
	 * @param contextOperation
	 * @param expressionNode
	 * @return
	 */
	private IDataflowEndpoint analyzeOperationExpression(final FortranProject project,
			final FortranModule contextModule, final FortranOperation contextOperation, final Node expressionNode) {
		final List<Node> operators = NodeUtils.findAllSiblings(expressionNode.getFirstChild(),
				Predicates.isOperand.negate(), o -> false);

		final MultipleDataflowEndpoint multipleEndpoint = new MultipleDataflowEndpoint();
		operators.stream().filter(Predicates.isExpression)
				.map(operator -> this.analyzeExpression(project, contextModule, contextOperation, operator))
				.forEach(endpoint -> multipleEndpoint.add(endpoint));

		return multipleEndpoint;
	}

	private IDataflowEndpoint analyzeNamedExpressionAccess(final FortranProject project,
			final FortranModule contextModule, final FortranOperation contextOperation, final Node namedElementNode) {
		final String elementName = NodeUtils.getName(namedElementNode);
		// check whether this is a parameter or variable
		final FortranParameter parameter = contextOperation.getParameters().get(elementName);
		if (parameter != null) {
			return this.createFlowEndpoint(contextModule, contextOperation, parameter, parameter.getDirection());
		} else {
			final FortranVariable variable = contextOperation.getVariables().get(elementName);
			if (variable != null) {
				return this.createFlowEndpoint(contextModule, contextOperation, variable, variable.getDirection());
			} else {
				return this.analyzeFunctionCall(project, contextModule, contextOperation, elementName,
						namedElementNode);
			}
		}
	}

	private DataflowEndpoint analyzeFunctionCall(final FortranProject project, final FortranModule contextModule,
			final FortranOperation contextOperation, final String functionName, final Node functionNode) {
		final Pair<FortranModule, FortranOperation> callee = this.findOperation(project.getModules().values(),
				functionName);

		if (callee != null) {
			final List<Node> rlt = NodeUtils.findAllSiblings(functionNode.getFirstChild(), Predicates.isRLT,
					o -> false);
			if (!rlt.isEmpty()) {
				final Node parensR = rlt.get(0).getFirstChild();
				if (Predicates.isParensR.test(parensR)) {
					final Node elementLT = NodeUtils.findChildFirst(parensR, Predicates.isElementLT);
					final List<Node> arguments = NodeUtils.findAllSiblings(elementLT.getFirstChild(),
							Predicates.isElement, o -> false);
					for (int i = 0; i < arguments.size(); i++) {
						this.analyzeArgument(project, contextModule, contextOperation, callee.first, callee.second,
								arguments.get(i).getFirstChild(), i);
					}
				} else {
					this.logger.error("Internal error: Unknown function parameter definition {}", parensR);
				}
			} else {
				this.logger.warn("No parameter for function '{}' in context {}::{} in invocation {}::{}", functionName,
						contextModule.getFileName(), contextOperation.getName(), callee.getFirst().getFileName(),
						callee.getSecond().getName());
			}
			return new DataflowEndpoint(callee.first, callee.second, callee.second, EDirection.READ);
		} else {
			this.logger.error("In expression context {}::{} unknown function {} invoked", contextModule.getFileName(),
					contextOperation.getName(), functionName);
			return null;
		}
	}

	private IDataflowEndpoint createFlowEndpoint(final FortranModule module, final FortranOperation operation,
			final IDataflowEndpoint endpoint, final EDirection direction) {
		return new DataflowEndpoint(module, operation, endpoint, direction);
	}

	private Optional<FortranParameter> findCalleeParameter(final FortranOperation calleeOperation,
			final int argumentIndex) {
		final int corectedIndex = this.computeArgumentIndex(calleeOperation, argumentIndex);
		return calleeOperation.getParameters().values().stream()
				.filter(parameter -> parameter.getPosition() == corectedIndex).findFirst();
	}

	private int computeArgumentIndex(final FortranOperation operation, final int argumentIndex) {
		if (operation.isVariableArguments() && (argumentIndex >= operation.getParameters().size())) {
			return operation.getParameters().size() - 1;
		} else {
			return argumentIndex;
		}
	}

	// TODO duplicate form ProcessOperationCallStage
	private Pair<FortranModule, FortranOperation> findOperation(final Collection<FortranModule> modules,
			final String signature) {
		final Optional<FortranModule> moduleOptional = modules.stream().filter(module -> module.getOperations().values()
				.stream().anyMatch(operation -> operation.getName().equals(signature))).findFirst();
		if (moduleOptional.isPresent()) {
			return new Pair<>(moduleOptional.get(), moduleOptional.get().getOperations().get(signature));
		} else {
			return null;
		}
	}

}
