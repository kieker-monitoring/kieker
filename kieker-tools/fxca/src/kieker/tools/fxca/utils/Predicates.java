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
package kieker.tools.fxca.utils;

import java.util.List;
import java.util.function.Predicate;

import org.w3c.dom.Node;

/**
 * Predicates for node selection and attribute checks.
 *
 * @author Henning Schnoor
 * @author Reiner Jung -- refactoring
 *
 * @since 1.3.0
 */
public final class Predicates { // NOPMD NCS

	public static Predicate<Node> isProgramStatement = NodeUtils.hasName("program-stmt");
	public static Predicate<Node> isEndProgramStatement = NodeUtils.hasName("end-program-stmt");

	public static Predicate<Node> isSubroutineStatement = NodeUtils.hasName("subroutine-stmt");
	public static Predicate<Node> isFunctionStatement = NodeUtils.hasName("function-stmt");
	public static Predicate<Node> isEntryStatement = NodeUtils.hasName("entry-stmt");
	public static Predicate<Node> isOperationStatement = Predicates.isSubroutineStatement
			.or(Predicates.isFunctionStatement).or(Predicates.isEntryStatement);

	public static Predicate<Node> isEndSubroutineStatement = NodeUtils.hasName("end-subroutine-stmt");
	public static Predicate<Node> isEndFunctionStatement = NodeUtils.hasName("end-function-stmt");
	public static Predicate<Node> isEndOperationStatement = Predicates.isEndSubroutineStatement
			.or(Predicates.isEndFunctionStatement);

	public static Predicate<Node> isModuleStatement = NodeUtils.hasName("module-stmt");
	public static Predicate<Node> isUseStatement = NodeUtils.hasName("use-stmt");

	public static Predicate<Node> isImplicitNoneStmt = NodeUtils.hasName("implicit-none-stmt");

	public static Predicate<Node> isCallStatement = NodeUtils.hasName("call-stmt");
	public static Predicate<Node> isC = NodeUtils.hasName("C");
	public static Predicate<Node> isInclude = NodeUtils.hasName("include");

	public static Predicate<Node> isProgramName = NodeUtils.hasName("program-N");
	public static Predicate<Node> isSubroutineName = NodeUtils.hasName("subroutine-N");
	public static Predicate<Node> isFunctionName = NodeUtils.hasName("function-N");
	public static Predicate<Node> isEntryName = NodeUtils.hasName("entry-N");

	public static Predicate<Node> isArgumentSpecification = NodeUtils.hasName("arg-spec");
	public static Predicate<Node> isArgument = NodeUtils.hasName("arg");
	public static Predicate<Node> isArgumentName = NodeUtils.hasName("arg-N");

	public static Predicate<Node> isNamedExpression = NodeUtils.hasName("named-E");
	public static Predicate<Node> isBigN = NodeUtils.hasName("N");
	public static Predicate<Node> isTDeclStmt = NodeUtils.hasName("T-decl-stmt");
	public static Predicate<Node> isDimStmt = NodeUtils.hasName("DIM-stmt");
	public static Predicate<Node> isEnDecl = NodeUtils.hasName("EN-decl");
	public static Predicate<Node> isSmallN = NodeUtils.hasName("n");
	public static Predicate<Node> isElementLT = NodeUtils.hasName("element-LT");
	public static Predicate<Node> isRLT = NodeUtils.hasName("R-LT");
	public static Predicate<Node> isElement = NodeUtils.hasName("element");
	public static Predicate<Node> isParensR = NodeUtils.hasName("parens-R");
	public static Predicate<Node> isRegularLeftParanthesis = Predicates.isParensR
			.and(node -> node.getTextContent().startsWith("("));
	public static Predicate<Node> isCommonStatement = NodeUtils.hasName("common-stmt");
	public static Predicate<Node> isCommonBlockObjectName = NodeUtils.hasName("common-block-obj-N");
	public static Predicate<Node> isCommonBlockObject = NodeUtils.hasName("common-block-obj");
	public static Predicate<Node> isLocalAccess = node -> LocalExpressionAccessUtils.isLocalAccess(node);
	public static Predicate<Node> isDummyArgumentLT = NodeUtils.hasName("dummy-arg-LT");

	public static Predicate<Node> isNamedExpressionAccess = Predicates.isNamedExpression
			.and(NodeUtils.childSatisfies("0", Predicates.isBigN))
			.and(NodeUtils.childSatisfies("0,0", Predicates.isSmallN))
			.and(NodeUtils.childSatisfies("1", Predicates.isRLT))
			.and(NodeUtils.childSatisfies("1,0", Predicates.isRegularLeftParanthesis));

	public static Pair<Predicate<Node>, Predicate<Node>> endFunctionToBeginFunction = new Pair<>(
			Predicates.isEndFunctionStatement, Predicates.isFunctionStatement);

	public static Pair<Predicate<Node>, Predicate<Node>> endSubroutineToBeginSubroutine = new Pair<>(
			Predicates.isEndSubroutineStatement, Predicates.isSubroutineStatement);

	public static List<Pair<Predicate<Node>, Predicate<Node>>> paranthesisTypes = List
			.of(Predicates.endFunctionToBeginFunction, Predicates.endSubroutineToBeginSubroutine);
	public static Predicate<Node> isIfThenStatement = NodeUtils.hasName("if-then-stmt");
	public static Predicate<Node> isElseIfStatement = NodeUtils.hasName("else-if-stmt");

	public static Predicate<Node> isIfStatement = NodeUtils.hasName("if-stmt");

	public static Predicate<Node> isEndIfStatement = NodeUtils.hasName("end-if-stmt");
	public static Predicate<Node> isSelectCaseStatement = NodeUtils.hasName("select-case-stmt");
	public static Predicate<Node> isDoStatement = NodeUtils.hasName("do-stmt");
	public static Predicate<Node> isEndDoStatement = NodeUtils.hasName("end-do-stmt");
	public static Predicate<Node> isAssignmentStatement = NodeUtils.hasName("a-stmt");
	public static Predicate<Node> isENDeclLT = NodeUtils.hasName("EN-decl-LT");

	public static Predicate<Node> isOperandExpression = NodeUtils.hasName("op-E");
	public static Predicate<Node> isLiteralE = NodeUtils.hasName("literal-E");
	public static Predicate<Node> isStringE = NodeUtils.hasName("string-E");
	public static Predicate<Node> isOperand = NodeUtils.hasName("op");
	public static Predicate<Node> isParensExpression = NodeUtils.hasName("parens-E");
	public static Predicate<Node> isM = NodeUtils.hasName("m");
	public static Predicate<Node> isCnt = NodeUtils.hasName("cnt");
	public static Predicate<Node> isText = node -> !node.getTextContent().isEmpty();

	public static Predicate<Node> isIterator = NodeUtils.hasName("iterator");
	public static Predicate<Node> isIteratorDefinitionLT = NodeUtils.hasName("iterator-definition-LT");

	public static Predicate<Node> isAssignmentE1 = NodeUtils.hasName("E-1");
	public static Predicate<Node> isAssignmentE2 = NodeUtils.hasName("E-2");
	public static Predicate<Node> isFile = NodeUtils.hasName("file");

	public static Predicate<Node> isLowerBound = NodeUtils.hasName("lower-bound");
	public static Predicate<Node> isUpperBound = NodeUtils.hasName("upper-bound");
	public static Predicate<Node> isEndStatement = Predicates.isEndFunctionStatement.or(Predicates.isEndIfStatement)
			.or(Predicates.isEndProgramStatement).or(Predicates.isEndSubroutineStatement)
			.or(Predicates.isEndDoStatement);

	public static Predicate<Node> isSaveStatement = NodeUtils.hasName("save-stmt");
	public static Predicate<Node> isDataStatement = NodeUtils.hasName("data-stmt");
	public static Predicate<Node> isDataStatementSet = NodeUtils.hasName("data-stmt-set");
	public static Predicate<Node> isDataStatementObjectLT = NodeUtils.hasName("data-stmt-obj-LT");
	public static Predicate<Node> isDataStatementObject = NodeUtils.hasName("data-stmt-obj");
	public static Predicate<Node> isDataStatementValueLT = NodeUtils.hasName("data-stmt-value-LT");
	public static Predicate<Node> isDataStatementValue = NodeUtils.hasName("data-stmt-value");
	public static Predicate<Node> isDataStatementConstant = NodeUtils.hasName("data-stmt-constant");

	public static Predicate<Node> isReturnStatement = NodeUtils.hasName("return-stmt");
	public static Predicate<Node> isWhereStatement = NodeUtils.hasName("where-stmt");
	public static Predicate<Node> isPrintStatement = NodeUtils.hasName("print-stmt");

	public static Predicate<Node> isWriteStatement = NodeUtils.hasName("write-stmt");
	public static Predicate<Node> isLabel = NodeUtils.hasName("label");
	public static Predicate<Node> isFormatStatement = NodeUtils.hasName("format-stmt");
	public static Predicate<Node> isRewindStatement = NodeUtils.hasName("rewind-stmt");
	public static Predicate<Node> isContinueStatement = NodeUtils.hasName("continue-stmt");
	public static Predicate<Node> isGotoStatement = NodeUtils.hasName("goto-stmt");
	public static Predicate<Node> isIOControlSpec = NodeUtils.hasName("io-control-spec");
	public static Predicate<Node> isIOControl = NodeUtils.hasName("io-control");
	public static Predicate<Node> isOutputItemLT = NodeUtils.hasName("output-item-LT");
	public static Predicate<Node> isOutputItem = NodeUtils.hasName("output-item");

	public static Predicate<Node> isReadStatement = NodeUtils.hasName("read-stmt");
	public static Predicate<Node> isInputItemLT = NodeUtils.hasName("input-item-LT");
	public static Predicate<Node> isInputItem = NodeUtils.hasName("input-item");

	public static Predicate<Node> isStopStatement = NodeUtils.hasName("stop-stmt");

	public static Predicate<Node> isActionStatement = NodeUtils.hasName("action-stmt");
	public static Predicate<Node> isElseStatement = NodeUtils.hasName("else-stmt");
	public static Predicate<Node> isSavedEnLT = NodeUtils.hasName("saved-EN-LT");
	public static Predicate<Node> isSavedEn = NodeUtils.hasName("saved-EN");
	public static Predicate<Node> isEnN = NodeUtils.hasName("EN-N");
	public static Predicate<Node> isMaskExpression = NodeUtils.hasName("mask-E");
	public static Predicate<Node> isAllocateStatement = NodeUtils.hasName("allocate-stmt");
	public static Predicate<Node> isDeallocateStatement = NodeUtils.hasName("deallocate-stmt");

	public static Predicate<Node> isParameterStatement = NodeUtils.hasName("parameter-stmt");
	public static Predicate<Node> isInquireStatement = NodeUtils.hasName("inquire-stmt");
	public static Predicate<Node> isArrayConstructorExpression = NodeUtils.hasName("array-constructor-E");
	public static Predicate<Node> isAcValueLT = NodeUtils.hasName("ac-value-LT");
	public static Predicate<Node> isAcValue = NodeUtils.hasName("ac-value");

	public static Predicate<Node> isCloseStatement = NodeUtils.hasName("close-stmt");
	public static Predicate<Node> isDIMStatement = NodeUtils.hasName("DIM-stmt");
	public static Predicate<Node> isDoLabelStatement = NodeUtils.hasName("do-label-stmt");
	public static Predicate<Node> isEndFileStatement = NodeUtils.hasName("end-file-stmt");
	public static Predicate<Node> isExitStatement = NodeUtils.hasName("exit-stmt");
	public static Predicate<Node> isNamelistStatement = NodeUtils.hasName("namelist-stmt");
	public static Predicate<Node> isOpenStatement = NodeUtils.hasName("open-stmt");

	public static Predicate<Node> isArraySpecification = NodeUtils.hasName("array-spec");
	public static Predicate<Node> isShapeSpecLT = NodeUtils.hasName("shape-spec-LT");
	public static Predicate<Node> isShapeSpec = NodeUtils.hasName("shape-spec");
	public static Predicate<Node> isDoV = NodeUtils.hasName("do-V");

	public static Predicate<Node> isNamelistGroupObjLT = NodeUtils.hasName("namelist-group-obj-LT");
	public static Predicate<Node> isNamelistGroupObj = NodeUtils.hasName("namelist-group-obj");
	public static Predicate<Node> isNamelistGroupObjN = NodeUtils.hasName("namelist-group-obj-N");
	public static Predicate<Node> isTestExpression = NodeUtils.hasName("test-E");
	public static Predicate<Node> isExpression = Predicates.isOperandExpression.or(Predicates.isNamedExpression)
			.or(Predicates.isNamedExpressionAccess).or(Predicates.isParensExpression);
	public static Predicate<Node> isConditionExpression = NodeUtils.hasName("condition-E");
	public static Predicate<Node> isStatement = Predicates.isAssignmentStatement.or(Predicates.isCallStatement)
			.or(Predicates.isIfStatement).or(Predicates.isIfThenStatement).or(Predicates.isDataStatement)
			.or(Predicates.isDoStatement).or(Predicates.isDoLabelStatement).or(Predicates.isReadStatement)
			.or(Predicates.isWriteStatement).or(Predicates.isWhereStatement).or(Predicates.isCloseStatement)
			.or(Predicates.isOpenStatement).or(Predicates.isSaveStatement);
	public static Predicate<Node> isExternalStatement = NodeUtils.hasName("external-stmt");

	private Predicates() {
		// make this a utility class
	}
}
