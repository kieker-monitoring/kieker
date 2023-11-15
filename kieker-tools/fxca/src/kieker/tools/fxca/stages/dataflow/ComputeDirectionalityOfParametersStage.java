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

import java.util.List;
import java.util.Optional;

import org.w3c.dom.Node;

import kieker.model.analysismodel.execution.EDirection;
import kieker.tools.fxca.model.FortranModule;
import kieker.tools.fxca.model.FortranOperation;
import kieker.tools.fxca.model.FortranParameter;
import kieker.tools.fxca.model.FortranProject;
import kieker.tools.fxca.model.FortranVariable;
import kieker.tools.fxca.utils.NodeUtils;
import kieker.tools.fxca.utils.Predicates;

import teetime.stage.basic.AbstractTransformation;

/**
 * @author Reiner Jung
 * @since 2.0.0
 */
public class ComputeDirectionalityOfParametersStage extends AbstractTransformation<FortranProject, FortranProject> {

	@Override
	protected void execute(final FortranProject project) throws Exception {
		project.getModules().values().forEach(module -> module.getOperations().values()
				.forEach(operation -> this.computeParameterDirectionality(module, operation)));
		this.logger.info("Computed parameter directionality and variable dependencies.");
		this.outputPort.send(project);
	}

	private void computeParameterDirectionality(final FortranModule module, final FortranOperation operation) { // NOPMD
		final List<Node> statements = NodeUtils.findAllSiblings(operation.getNode(), o -> true,
				Predicates.isEndSubroutineStatement.or(Predicates.isEndProgramStatement)
						.or(Predicates.isEndFunctionStatement));
		statements.forEach(statement -> {
			if (Predicates.isAssignmentStatement.test(statement)) {
				this.checkAssignment(operation, statement);
			} else if (Predicates.isCallStatement.test(statement)) {
				this.checkCall(operation, statement);
			} else if (Predicates.isIfThenStatement.test(statement)) {
				this.checkIfThen(operation, statement);
			} else if (Predicates.isIfStatement.test(statement)) {
				this.checkIf(operation, statement);
			} else if (Predicates.isElseIfStatement.test(statement)) {
				this.checkIfThen(operation, statement);
			} else if (Predicates.isDoStatement.or(Predicates.isDoLabelStatement).test(statement)) {
				this.checkDoStatement(module, operation, statement);
			} else if (Predicates.isReadStatement.test(statement)) {
				this.checkReadStatement(operation, statement);
			} else if (Predicates.isWriteStatement.test(statement)) {
				this.checkWriteStatement(operation, statement);
			} else if (Predicates.isSaveStatement.test(statement)) {
				this.checkSaveStatement(module, operation, statement);
			} else if (Predicates.isDataStatement.test(statement)) {
				this.checkDataStatement(operation, statement);
			} else if (Predicates.isPrintStatement.test(statement)) {
				this.checkPrintStatement(operation, statement);
			} else if (Predicates.isWhereStatement.test(statement)) {
				this.checkWhereStatement(operation, statement);
			} else if (Predicates.isCloseStatement.test(statement)) {
				this.checkCloseStatement();
			} else if (Predicates.isOpenStatement.test(statement)) {
				this.checkOpenStatement();
			} else if (Predicates.isDIMStatement.test(statement)) {
				this.checkDIMStatement(module, operation, statement);
			} else if (Predicates.isEndFileStatement.test(statement)) {
				this.checkEndFileStatement(operation, statement);
			} else if (Predicates.isNamelistStatement.test(statement)) {
				this.checkNamelistStatement(operation, statement);
			} else if (Predicates.isImplicitNoneStmt.test(statement)) {
				operation.setImplicit(false);
			} else if (Predicates.isM.or(Predicates.isC).or(Predicates.isTDeclStmt).or(Predicates.isFile) // NOPMD
					.or(Predicates.isInclude).or(Predicates.isOperationStatement).or(Predicates.isProgramStatement)
					.or(Predicates.isEndStatement).or(Predicates.isGotoStatement).or(Predicates.isLabel)
					.or(Predicates.isContinueStatement).or(Predicates.isFormatStatement).or(Predicates.isElseStatement)
					.or(Predicates.isReturnStatement).or(Predicates.isRewindStatement).or(Predicates.isStopStatement)
					.or(Predicates.isAllocateStatement).or(Predicates.isDeallocateStatement)
					.or(Predicates.isInquireStatement).or(Predicates.isParameterStatement)
					.or(Predicates.isCommonStatement).or(Predicates.isExitStatement).or(Predicates.isExternalStatement)
					.test(statement)) {
				// ignore
			} else if (statement.getNodeType() == Node.TEXT_NODE) { // NOPMD
				// ignore text
			} else {
				this.logger.debug("In file {}: Unkown statement {}", module.getFileName(), statement);
			}
		});
	}

	private void checkOpenStatement() {
		// currently not considered dataflow
	}

	private void checkDIMStatement(final FortranModule module, final FortranOperation operation,
			final Node dimStatement) {
		final List<Node> enDecls = NodeUtils.findAllSiblings(dimStatement.getFirstChild(), Predicates.isENDeclLT,
				o -> false);
		enDecls.forEach(enDecl -> this.checkEnDecl(module, operation, enDecl));
	}

	private void checkEnDecl(final FortranModule module, final FortranOperation operation, final Node enDecl) {
		final Node enN = NodeUtils.findChildFirst(enDecl, Predicates.isEnN);
		if (enN != null) {
			final Node bigN = NodeUtils.findChildFirst(enN, Predicates.isBigN);
			final Node smallN = NodeUtils.findChildFirst(bigN, Predicates.isSmallN);
			this.checkVariable(operation, smallN.getTextContent(), EDirection.READ);

			NodeUtils.findAllSiblings(enN, Predicates.isArraySpecification, o -> false).forEach(arraySpecification -> {
				final Node shapeSpecLT = NodeUtils.findChildFirst(arraySpecification, Predicates.isShapeSpecLT);
				NodeUtils.findAllSiblings(shapeSpecLT.getFirstChild(), Predicates.isShapeSpec, o -> false)
						.forEach(shapeSpec -> this.checkLimits(module, operation, null, shapeSpec));
			});
		}
	}

	private void checkEndFileStatement(final FortranOperation operation, final Node sibling) { // NOPMD
		// no dataflow
	}

	private void checkNamelistStatement(final FortranOperation operation, final Node namelistStatement) {
		final Node namelistGroupObjLT = NodeUtils.findChildFirst(namelistStatement, Predicates.isNamelistGroupObjLT);
		NodeUtils.findAllSiblings(namelistGroupObjLT, Predicates.isNamelistGroupObj, o -> false)
				.forEach(namelistGroupObj -> this.checkNamelistGroupObj(operation, namelistGroupObj));
	}

	private void checkNamelistGroupObj(final FortranOperation operation, final Node namelistGroupObj) {
		final String elementName = NodeUtils.getName(namelistGroupObj);
		final FortranParameter parameter = operation.getParameters().get(elementName);
		if (parameter != null) {
			parameter.addDirection(EDirection.WRITE);
		} else {
			final FortranVariable variable = operation.getVariables().get(elementName);
			variable.addDirection(EDirection.WRITE);
		}
	}

	private void checkCloseStatement() {
		// not considered dataflow right now
	}

	private void checkAssignment(final FortranOperation operation, final Node assignment) {
		this.checkAssignee(operation, assignment);
		this.checkAssignmentExpression(operation, assignment);
	}

	private void checkAssignee(final FortranOperation operation, final Node assignment) {
		final String name = NodeUtils.getName(NodeUtils.getAssigmentVariable(assignment));
		final FortranParameter parameter = operation.getParameters().get(name);
		if (parameter != null) {
			parameter.addDirection(EDirection.WRITE);
		}
		final FortranVariable variable = operation.getVariables().get(name);
		if (variable != null) {
			variable.addDirection(EDirection.WRITE);
		}
	}

	private void checkAssignmentExpression(final FortranOperation operation, final Node assignment) {
		final Node node = NodeUtils.getAssignmentExpression(assignment);
		if (Predicates.isNamedExpression.test(node)) { // NOPMD ignore
		} else if (Predicates.isAssignmentE2.test(node)) {
			this.checkExpression(operation, node, EDirection.READ);
		} else {
			this.logger.warn("Unknown expression type {}", node.toString());
		}
	}

	private void checkDoStatement(final FortranModule module, final FortranOperation operation,
			final Node doStatement) {
		final Node doV = NodeUtils.findChildFirst(doStatement, Predicates.isDoV);
		if (doV == null) { // do while
			final Node testE = NodeUtils.findChildFirst(doStatement, Predicates.isTestExpression);
			this.checkExpression(operation, testE, EDirection.READ);
		} else { // do statement or do label statement
			final String elementName = NodeUtils.getName(doV);
			FortranVariable loopVariable = this.checkVariable(operation, elementName, EDirection.WRITE);
			if (!this.checkParameter(operation, elementName) && (loopVariable == null)) {
				if (!operation.isImplicit()) {
					this.logger.error("Unknown value assignee {} after do statement in {}::{}", elementName,
							module.getFileName(), operation.getName());
				} else {
					loopVariable = new FortranVariable(elementName);
					loopVariable.setDirection(EDirection.WRITE);
					operation.getVariables().put(elementName, loopVariable);
				}
			}
			this.checkLimits(module, operation, loopVariable, doStatement);
		}
	}

	private void checkLimits(final FortranModule module, final FortranOperation operation,
			final FortranVariable variable, final Node range) {
		final Node lowerBound = NodeUtils.findChildFirst(range, Predicates.isLowerBound);
		final Node upperBound = NodeUtils.findChildFirst(range, Predicates.isUpperBound);
		if (lowerBound != null) {
			this.checkExpression(operation, lowerBound, EDirection.READ);
			if (variable != null) {
				this.linkVariables(module, operation, variable, lowerBound);
			}
		}
		if (upperBound != null) {
			this.checkExpression(operation, upperBound, EDirection.READ);
			if (variable != null) {
				this.linkVariables(module, operation, variable, upperBound);
			}
		}
	}

	private void linkVariables(final FortranModule module, final FortranOperation operation,
			final FortranVariable variable, final Node bound) {
		NodeUtils.allDescendents(bound, Predicates.isNamedExpression, true).stream().forEach(element -> {
			final String elementName = NodeUtils.getName(element);
			final FortranVariable sourceVariable = operation.getVariables().get(elementName);
			if (sourceVariable != null) {
				variable.getSources().add(sourceVariable);
			}
			final FortranVariable sourceModuleVariable = module.getVariables().get(elementName);
			if (sourceModuleVariable != null) {
				variable.getSources().add(sourceModuleVariable);
			}
			final FortranParameter sourceParameter = operation.getParameters().get(elementName);
			if (sourceParameter != null) {
				variable.getSources().add(sourceParameter);
			}
		});
	}

	private void checkReadStatement(final FortranOperation operation, final Node readStatement) {
		final Node controls = NodeUtils.findChildFirst(readStatement, Predicates.isIOControlSpec);
		if (controls != null) {
			NodeUtils.findAllSiblings(controls.getFirstChild(), Predicates.isIOControl, o -> false)
					.forEach(value -> this.checkExpression(operation, value, EDirection.READ));
		}

		final Node outputs = NodeUtils.findChildFirst(readStatement, Predicates.isInputItemLT);
		if (outputs != null) {
			NodeUtils.findAllSiblings(outputs.getFirstChild(), Predicates.isInputItem, o -> false)
					.forEach(value -> this.checkVariable(operation, NodeUtils.getName(value), EDirection.WRITE));
		}
	}

	private void checkWriteStatement(final FortranOperation operation, final Node writeStatement) {
		final Node controls = NodeUtils.findChildFirst(writeStatement, Predicates.isIOControlSpec);
		if (controls != null) {
			NodeUtils.findAllSiblings(controls.getFirstChild(), Predicates.isIOControl, o -> false)
					.forEach(value -> this.checkExpression(operation, value, EDirection.READ));
		}

		final Node outputs = NodeUtils.findChildFirst(writeStatement, Predicates.isOutputItemLT);
		if (outputs != null) {
			NodeUtils.findAllSiblings(outputs.getFirstChild(), Predicates.isOutputItem, o -> false)
					.forEach(value -> this.checkVariable(operation, NodeUtils.getName(value), EDirection.READ));
		}
	}

	private void checkPrintStatement(final FortranOperation operation, final Node printStatement) {
		final Node outputs = NodeUtils.findChildFirst(printStatement, Predicates.isOutputItemLT);
		if (outputs != null) {
			NodeUtils.findAllSiblings(outputs.getFirstChild(), Predicates.isOutputItem, o -> false)
					.forEach(value -> this.checkVariable(operation, NodeUtils.getName(value), EDirection.READ));
		}
	}

	private void checkSaveStatement(final FortranModule module, final FortranOperation operation,
			final Node saveStatement) {
		final Node savedEnLt = NodeUtils.findChildFirst(saveStatement, Predicates.isSavedEnLT);
		NodeUtils.findAllSiblings(savedEnLt.getFirstChild(), Predicates.isSavedEn, o -> false)
				.forEach(savedEn -> this.checkEnDecl(module, operation, savedEn));
	}

	private void checkWhereStatement(final FortranOperation operation, final Node whereStatement) {
		final Node maskExpression = NodeUtils.findChildFirst(whereStatement, Predicates.isMaskExpression);
		this.checkExpression(operation, maskExpression, EDirection.READ);
		this.checkActionStatement(operation, NodeUtils.findChildFirst(whereStatement, Predicates.isActionStatement));
	}

	private void checkDataStatement(final FortranOperation operation, final Node dataStatement) {
		final Node dataStementSet = NodeUtils.findChildFirst(dataStatement, Predicates.isDataStatementSet);
		final Node objectLT = NodeUtils.findChildFirst(dataStementSet, Predicates.isDataStatementObjectLT);
		final Node object = NodeUtils.findChildFirst(objectLT, Predicates.isDataStatementObject);
		final String dataName = NodeUtils.getName(object);
		this.checkVariable(operation, dataName, EDirection.WRITE);
	}

	private void checkIfThen(final FortranOperation operation, final Node ifThen) {
		final Node condition = ifThen.getFirstChild().getNextSibling();
		this.checkExpression(operation, condition, EDirection.READ);
	}

	private void checkIf(final FortranOperation operation, final Node ifStatement) {
		final Node condition = ifStatement.getFirstChild().getNextSibling();
		this.checkExpression(operation, condition, EDirection.READ);
		this.checkActionStatement(operation, NodeUtils.findChildFirst(ifStatement, Predicates.isActionStatement));
	}

	private void checkActionStatement(final FortranOperation operation, final Node actionStatement) {
		NodeUtils.findAllSiblings(actionStatement.getFirstChild(), Predicates.isAssignmentStatement, o -> false)
				.forEach(assignment -> this.checkAssignment(operation, assignment));
	}

	private void checkCall(final FortranOperation operation, final Node call) {
		final String calleeName = NodeUtils.getName(call.getFirstChild().getNextSibling().getFirstChild());
		final FortranOperation callee = this
				.findOperation((FortranProject) ((FortranModule) operation.getParent()).getParent(), calleeName);

		if (callee != null) {
			final Node argumentSpecification = NodeUtils.findChildFirst(call, Predicates.isArgumentSpecification);
			if (argumentSpecification != null) {
				final List<Node> arguments = NodeUtils.findAllSiblings(argumentSpecification.getFirstChild(),
						Predicates.isArgument, o -> false);
				for (int i = 0; i < arguments.size(); i++) {
					final Node argument = arguments.get(i);
					final Optional<FortranParameter> parameter = this.findParameter(callee, i);
					if (parameter.isPresent()) {
						this.checkExpression(operation, argument, parameter.get().getDirection());
					} else {
						this.logger.warn("Operation {} has not parameter number {}", callee.getName(), i);
					}
				}
			}
		} else {
			this.logger.warn("Missing subroutine, function or library function {}", calleeName);
		}
	}

	private Optional<FortranParameter> findParameter(final FortranOperation operation, final int i) {
		final Optional<FortranParameter> parameterOptional = operation.getParameters().values().stream()
				.filter(parameter -> parameter.getPosition() == i).findFirst();
		if (parameterOptional.isPresent()) {
			return parameterOptional;
		} else {
			return operation.getParameters().values().stream()
					.filter(parameter -> parameter.getPosition() == operation.getParameters().size()).findFirst();
		}
	}

	private FortranOperation findOperation(final FortranProject project, final String operationName) {
		for (final FortranModule module : project.getModules().values()) {
			for (final FortranOperation operation : module.getOperations().values()) {
				if (operation.getName().equals(operationName)) {
					return operation;
				}
			}
		}
		return null;
	}

	/**
	 * check dataflow in expressions.
	 *
	 * @param operation
	 *            operation context
	 * @param node
	 *            expression node
	 * @param direction
	 *            direction of variable value access
	 */
	private void checkExpression(final FortranOperation operation, final Node node, final EDirection direction) { // NOPMD
																													// complexity
		final List<Node> expression = NodeUtils.findAllSiblings(node.getFirstChild(), o -> true, o -> false);
		expression.forEach(expressionElement -> {
			if (Predicates.isNamedExpression.test(expressionElement)) {
				final String elementName = NodeUtils.getName(expressionElement);
				this.checkVariable(operation, elementName, direction);
				this.checkParameter(operation, elementName);
				this.checkFunction(operation, expressionElement);
			} else if (Predicates.isOperandExpression.test(expressionElement)) {
				this.checkExpression(operation, expressionElement, EDirection.READ);
			} else if (Predicates.isParensExpression.test(expressionElement)) {
				this.checkExpression(operation, expressionElement, EDirection.READ);
			} else if (Predicates.isIterator.test(expressionElement)) {
				this.checkIterator(operation, expressionElement);
			} else if (Predicates.isArgumentName.test(expressionElement)) {
				this.checkArgumentName();
			} else if (Predicates.isArrayConstructorExpression.test(expressionElement)) {
				this.checkArrayConstructorExpression(operation, expressionElement, direction);
			} else if (Predicates.isC.or(Predicates.isLabel).or(Predicates.isOperand).or(Predicates.isM) // NOPMD
					.or(Predicates.isCnt).or(Predicates.isLiteralE).or(Predicates.isStringE).test(expressionElement)) {
				// ignore
			} else if (expressionElement.getNodeType() == Node.TEXT_NODE) { // NOPMD
				// ignore
			} else {
				this.logger.error("Unknown expression element {}", expressionElement.toString());
			}
		});
	}

	private void checkArrayConstructorExpression(final FortranOperation operation, final Node expression,
			final EDirection direction) {
		final Node acValueLT = NodeUtils.findChildFirst(expression, Predicates.isAcValueLT);
		NodeUtils.findAllSiblings(acValueLT.getFirstChild(), Predicates.isAcValue, o -> false)
				.forEach(value -> this.checkExpression(operation, value, direction));
	}

	private void checkArgumentName() {
		// TODO we do not know how to handle argument names in read and write statements
	}

	private boolean checkParameter(final FortranOperation operation, final String elementName) {
		final FortranParameter parameter = operation.getParameters().get(elementName);
		if (parameter != null) {
			parameter.addDirection(EDirection.READ);
			return true;
		} else {
			return false;
		}
	}

	private FortranVariable checkVariable(final FortranOperation operation, final String elementName,
			final EDirection direction) {
		if (operation.getVariables().containsKey(elementName)) {
			final FortranVariable variable = operation.getVariables().get(elementName);
			variable.addDirection(direction);
			return variable;
		} else if (((FortranModule) operation.getParent()).getVariables().containsKey(elementName)) {
			final FortranVariable variable = ((FortranModule) operation.getParent()).getVariables().get(elementName);
			variable.addDirection(direction);
			return variable;
		} else {
			final Optional<FortranVariable> variableOptional = ((FortranModule) operation.getParent()).getCommonBlocks()
					.values().stream().map(commonBlock -> {
						return commonBlock.getVariables().get(elementName);
					}).filter(v -> v != null).findFirst();
			if (variableOptional.isPresent()) {
				variableOptional.get().addDirection(direction);
				return variableOptional.get();
			} else {
				return null;
			}
		}
	}

	private boolean checkFunction(final FortranOperation operation, final Node expressionElement) {
		if (Predicates.isNamedExpressionAccess.test(expressionElement)) {
			// System.err.println("function " + elementName);
			final Node rlt = NodeUtils.findChildFirst(expressionElement, Predicates.isRLT);
			final Node parensR = NodeUtils.findChildFirst(rlt, Predicates.isParensR);
			final Node elementLT = NodeUtils.findChildFirst(parensR, Predicates.isElementLT);
			final List<Node> elements = NodeUtils.findAllSiblings(elementLT.getFirstChild(), Predicates.isElement,
					o -> false);
			elements.forEach(element -> this.checkExpression(operation, element, EDirection.READ));
			return true;
		} else {
			return false;
		}
	}

	private void checkIterator(final FortranOperation operation, final Node iterator) {
		final List<Node> definitions = NodeUtils.findAllSiblings(iterator, Predicates.isIteratorDefinitionLT,
				o -> false);
		definitions.forEach(definition -> {
			NodeUtils.findAllSiblings(definition.getFirstChild(), o -> true, o -> false)
					.forEach(element -> this.checkExpression(operation, element, EDirection.READ));
		});
	}
}
