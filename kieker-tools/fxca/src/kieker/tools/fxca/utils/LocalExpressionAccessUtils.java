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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import org.w3c.dom.Node;

/**
 *
 * @author Henning Schnoor
 *
 * @since 1.3.0
 */
public final class LocalExpressionAccessUtils {

    private static LocalAccessParameters namesInCommonBlocks = new LocalAccessParameters(Predicates.isCommonStatement,
            Predicates.isCommonBlockObjectName, Predicates.isSmallN,
            smallNNode -> NodeUtils.getSuccessorNode(smallNNode, "0").getTextContent());

    private static LocalAccessParameters namesInOperationParameterList = new LocalAccessParameters(
            Predicates.isOperationStatement, Predicates.isArgumentName, Predicates.isSmallN,
            smallNNode -> NodeUtils.getSuccessorNode(smallNNode, "0").getTextContent());

    private static LocalAccessParameters namesInLocalVariableList = new LocalAccessParameters(Predicates.isTDeclStmt,
            Predicates.isEnDecl, Predicates.isSmallN,
            smallNNode -> NodeUtils.getSuccessorNode(smallNNode, "0").getTextContent());

    private LocalExpressionAccessUtils() {
        // private constructor for utility class
    }

    // we often need to search for names that are defined at the current node, which are *not*
    // external
    // calls. Examples are
    // - common block definitions
    // - local variables
    // - parameters to the function we're currently in.
    //
    // This class collects the parameters for looking for these values.

    /**
     * Different access types.
     *
     * @author Henning Schnoor
     *
     */
    public enum EAccessType {
        COMMON_BLOCK, LOCAL_VARIABLE, OPERATION_PARAMETER, OPERATION_CALL
    }

    private static Set<String> localNamesDefinedInApplyingBlocks(final Node node,
            final LocalAccessParameters parameters) {

        final List<Node> applyingBlocks = new ArrayList<>();

        Node current = node;

        while (current != null) {
            final List<Node> applyingBlocksOnThisLevel = NodeUtils.findAll(current, nnode -> nnode.getPreviousSibling(),
                    parameters.blockNodeTypeCheckPredicate, true, Predicates.paranthesisTypes, -1);
            applyingBlocks.addAll(applyingBlocksOnThisLevel);
            current = current.getParentNode();
        }

        return LocalExpressionAccessUtils.localNamesDefinedInBlocks(applyingBlocks, parameters); // allNamesDefinedInCommonBlocks
    }

    private static Set<String> localNamesDefinedInBlocks(final Collection<Node> applyingBlocks,
            final LocalAccessParameters parameters) {
        final Set<String> result = new HashSet<>();

        for (final Node blockNode : applyingBlocks) {
            result.addAll(LocalExpressionAccessUtils.localNamesDefinedInBlock(blockNode, parameters));
        }

        return result;
    }

    private static Set<String> localNamesDefinedInBlock(final Node blockNode, final LocalAccessParameters parameters) {

        if (!parameters.blockNodeTypeCheckPredicate.test(blockNode)) {
            throw new IllegalArgumentException("type checking failure.");
        }

        final HashSet<String> result = new HashSet<>();
        for (final Node element : NodeUtils.allDescendents(blockNode, parameters.outerDelimiterPredicate, true)) {
            for (final Node smallN : NodeUtils.allDescendents(element, parameters.innerDelimiterPredicate, true)) {
                result.add(parameters.extractName.apply(smallN));
            }
        }

        return result;
    }

    public static boolean isNamedExpressionLocalReference(final Node node, final LocalAccessParameters parameters) {

        if (!Predicates.isNamedExpressionAccess.test(node)) {
            return false;
        }

        final String nameOfCalledFunction = NodeUtils.nameOfCalledFunction(node);

        return LocalExpressionAccessUtils.localNamesDefinedInApplyingBlocks(node, parameters)
                .contains(nameOfCalledFunction);
    }

    public static EAccessType typeOfReferenceAccess(final Node referenceNode) {

        if (LocalExpressionAccessUtils.isNamedExpressionLocalReference(referenceNode,
                LocalExpressionAccessUtils.namesInCommonBlocks)) {
            return EAccessType.COMMON_BLOCK;
        }

        if (LocalExpressionAccessUtils.isNamedExpressionLocalReference(referenceNode,
                LocalExpressionAccessUtils.namesInOperationParameterList)) {
            return EAccessType.OPERATION_PARAMETER;
        }

        if (LocalExpressionAccessUtils.isNamedExpressionLocalReference(referenceNode,
                LocalExpressionAccessUtils.namesInLocalVariableList)) {
            return EAccessType.LOCAL_VARIABLE;
        }

        return EAccessType.OPERATION_CALL;
    }

    public static boolean isLocalAccess(final Node referenceNode) {
        return Predicates.isCallStatement.or(Predicates.isNamedExpressionAccess).test(referenceNode)
                && (LocalExpressionAccessUtils.typeOfReferenceAccess(referenceNode) != EAccessType.OPERATION_CALL);
    }

    public static String nameOfCalledFunctionOrLocalReference(final Node referenceNode) {

        // rewritten the switch statement because checkstyle cannot parse it

        final EAccessType switchValue = LocalExpressionAccessUtils.typeOfReferenceAccess(referenceNode);
        String suffix = null;

        if (switchValue == EAccessType.COMMON_BLOCK) {
            suffix = "-common-access";
        }

        if (switchValue == EAccessType.LOCAL_VARIABLE) {
            suffix = "-local-variable";
        }

        if (switchValue == EAccessType.OPERATION_PARAMETER) {
            suffix = "-parameter-access";
        }

        if (suffix == null) {
            throw new IllegalStateException();
        }

        return NodeUtils.nameOfCalledFunction(referenceNode) + suffix;
    }

    /**
     * Local data.
     *
     * @author Henning Schnoor
     *
     */
    public static class LocalAccessParameters {
        private final Predicate<Node> blockNodeTypeCheckPredicate;
        private final Predicate<Node> outerDelimiterPredicate;
        private final Predicate<Node> innerDelimiterPredicate;
        private final Function<Node, String> extractName;

        private LocalAccessParameters(final Predicate<Node> blockNodeTypeCheckPredicate,
                final Predicate<Node> outerDelimiterPredicate, final Predicate<Node> innerDelimiterPredicate,
                final Function<Node, String> extractName) {
            this.blockNodeTypeCheckPredicate = blockNodeTypeCheckPredicate;
            this.outerDelimiterPredicate = outerDelimiterPredicate;
            this.innerDelimiterPredicate = innerDelimiterPredicate;
            this.extractName = extractName;
        }
    }

}
