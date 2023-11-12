/***************************************************************************
 * Copyright (C) 2023 OceanDSL (https://oceandsl.uni-kiel.de)
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
package org.oceandsl.tools.fxca.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * No own attributes, only accessor functions for nodes. Implemented as class in order to allow
 * chaining.
 *
 * @author Henning Schnoor
 *
 * @since 1.3.0
 */
public class NodeUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(NodeUtils.class);

    private static final String ROOT_PROGRAM = "main";

    /** methods. */

    public static String nameOfCalledFunction(final Node functionCallNode) {
        return NodeUtils.getSuccessorNode(functionCallNode, "0,0").getTextContent().toLowerCase(Locale.getDefault());
    }

    public static String nameOfCalledOperation(final Node operationCallNode) {
        return NodeUtils.getSuccessorNode(operationCallNode, "1").getTextContent().toLowerCase(Locale.getDefault());
    }

    public static Predicate<Node> hasName(final String name) {
        return node -> {
            if (node == null) { // this happens occasionally, but should not happen
                return false;
            }
            return name.equals(node.getNodeName());
        };
    }

    public static Predicate<Node> hasTextContent(final String content) {
        return node -> content.equals(node.getTextContent());
    }

    public static Predicate<Node> childSatisfies(final String path, final Predicate<Node> predicate) {
        return node -> predicate.test(NodeUtils.getSuccessorNode(node, path));
    }

    public static String nodeType(final short nodeType) {
        switch (nodeType) {
        case Node.ELEMENT_NODE:
            return "Element Node";
        case Node.ATTRIBUTE_NODE:
            return "Attribute Node";
        case Node.TEXT_NODE:
            return "Text Node";
        case Node.CDATA_SECTION_NODE:
            return "CDATA Section Node";
        case Node.ENTITY_REFERENCE_NODE:
            return "Entity Reference Node";
        case Node.ENTITY_NODE:
            return "Entity Node";
        case Node.PROCESSING_INSTRUCTION_NODE:
            return "Processing Instruction Node";
        case Node.COMMENT_NODE:
            return "Comment Node";
        case Node.DOCUMENT_NODE:
            return "Document Node";
        case Node.DOCUMENT_TYPE_NODE:
            return "Document Type Node";
        case Node.DOCUMENT_FRAGMENT_NODE:
            return "Document Fragment Node";
        case Node.NOTATION_NODE:
            return "Notation Node";
        default:
            return "Unknown Node Type";
        }
    }

    // Convenience functions

    public static int printNode(final Node node, final int depth) {
        String spaces = "";
        for (int i = 0; i < depth; i++) {
            spaces = spaces + " ";
        }
        int numberOfPrintedNodes = 1;
        NodeUtils.LOGGER.info("{}[node type] ", spaces, NodeUtils.nodeType(node.getNodeType())); // NOPMD
        NodeUtils.LOGGER.info("{}[node name] ", spaces, node.getNodeName()); // NOPMD
        NodeUtils.LOGGER.info("{}[node value] ", spaces, node.getNodeValue()); // NOPMD
        NodeUtils.LOGGER.info("{}[node text content] ", spaces, node.getTextContent()); // NOPMD
        NodeUtils.LOGGER.info("{}[node #kids] ", spaces, node.getChildNodes().getLength()); // NOPMD

        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            final Node child = node.getChildNodes().item(i);
            numberOfPrintedNodes += NodeUtils.printNode(child, depth + 1);
        }
        return numberOfPrintedNodes;
    }

    public static Node getSuccessorNode(final Node node, final String path) {
        final String firstNumber = StringUtils.substringBefore(path, ",");
        final int childIndex = Integer.parseInt(firstNumber);
        final NodeList children = node.getChildNodes();
        if (children.getLength() < childIndex) {
            NodeUtils.LOGGER.error("No children found for node {}", node);
            return null;
        }

        final String nextPath = StringUtils.substringAfter(path, ",");
        if (nextPath.isEmpty()) {
            return node.getChildNodes().item(childIndex);
        } else {
            return NodeUtils.getSuccessorNode(node.getChildNodes().item(childIndex), nextPath);
        }
    }

    public static Node findChildFirst(final Node parent, final Predicate<Node> condition) {
        for (Node node = parent.getFirstChild(); node != null; node = node.getNextSibling()) {
            if (condition.test(node)) {
                return node;
            }
        }
        return null;
    }

    public static List<Node> findAllSiblingsDescendants(final Node node, final Predicate<Node> select,
            final Predicate<Node> terminate, final boolean includeSiblings) {
        final List<Node> siblings = NodeUtils.findAllSiblings(node, o -> true, terminate);
        final List<Node> nodes = new ArrayList<>();
        siblings.forEach(sibling -> nodes.addAll(NodeUtils.allDescendents(sibling, select, includeSiblings)));

        return nodes;
    }

    public static List<Node> findAllSiblings(final Node firstNode, final Predicate<Node> select,
            final Predicate<Node> terminate) {
        final List<Node> nodes = new ArrayList<>();
        for (Node node = firstNode; (node != null) && !terminate.test(node); node = node.getNextSibling()) {
            if (select.test(node)) {
                nodes.add(node);
            }
        }
        return nodes;
    }

    // NOTE: Only terminates if nextNode eventually returns null or a matching element.
    public static Node findFirst(final Node parent, final Function<Node, Node> nextNode,
            final Predicate<Node> condition, final boolean includeSelf) {
        return NodeUtils.findFirst(parent, nextNode, condition, includeSelf, null);
    }

    /**
     * Find first node.
     *
     * Be careful with the search direction here, if you are searching "backwards", then ")" might
     * be your opening, and "(" your closing paranthesis.
     *
     * use case: when finding the containing operation statement, we need to skip functions that are
     * defined "along the way," i.e., where both the "function declaration" and the "end function
     * declaration" elements appear.
     *
     * @param parent
     *            parent node from which the search begins
     * @param nextNode
     *            function providing the next node
     * @param condition
     *            selection criteria
     * @param includeSelf
     *            whether the parent node should be part of the result
     * @param paranthesistypes
     *            contains pairs of "opening paranthesis" and "closed paranthesis" conditions, where
     *            stuff between the paranthesis is ignored.
     */
    // NOTE: Only terminates if nextNode eventually returns null or a matching element.
    private static Node findFirst(final Node parent, final Function<Node, Node> nextNode,
            final Predicate<Node> condition, final boolean includeSelf,
            final List<Pair<Predicate<Node>, Predicate<Node>>> paranthesesTypes) {

        final List<Node> result = NodeUtils.findAll(parent, nextNode, condition, includeSelf, paranthesesTypes, 1);

        return result.isEmpty() ? null : result.get(0);
    }

    public static List<Node> findAll(final Node parent, final Function<Node, Node> nextNode,
            final Predicate<Node> condition, final boolean includeSelf,
            final List<Pair<Predicate<Node>, Predicate<Node>>> paranthesesTypes, final int maxElementsToFind) {

        final List<Node> result = new ArrayList<>();

        final int numberparanthesesTypes = paranthesesTypes == null ? 0 : paranthesesTypes.size();
        final int[] openParanthesis = new int[numberparanthesesTypes];

        Node current = parent;

        boolean inParanthesisInterval = false;
        // End if we do not have anywhere to search, or we have reached the limit (where "-1" counts
        // as "no limit").
        while ((current != null) && ((result.size() < maxElementsToFind) || (maxElementsToFind == -1))) {
            // current, parent must not be identical
            if (!inParanthesisInterval && condition.test(current) && ((current != parent) || includeSelf)) { // NOPMD
                result.add(current);
            }

            inParanthesisInterval = false;
            for (int i = 0; i < numberparanthesesTypes; i++) {
                final Predicate<Node> openingParanthesis = paranthesesTypes.get(i).first;
                final Predicate<Node> closingParanthesis = paranthesesTypes.get(i).second;

                if (openingParanthesis.test(current)) {
                    openParanthesis[i]++;
                }

                if (closingParanthesis.test(current)) {
                    openParanthesis[i]--;
                }

                if (openParanthesis[i] > 0) {
                    inParanthesisInterval = true;
                }
            }

            // Check for nextNode-stationaly points
            final Node nextNodeResult = nextNode.apply(current);
            current = current == nextNodeResult ? null : nextNodeResult; // NOPMD must be identical
        }

        return result;
    }

    private static boolean hasConnectedWith(final Node parent, final Function<Node, Node> nextNode,
            final Predicate<Node> condition, final boolean includeSelf) {
        return NodeUtils.findFirst(parent, nextNode, condition, includeSelf) != null;
    }

    private static boolean hasLeftSibling(final Node parent, final Predicate<Node> condition,
            final boolean includeSelf) {
        return NodeUtils.hasConnectedWith(parent, node -> node.getPreviousSibling(), condition, includeSelf);
    }

    private static Node firstLeftSibling(final Node parent, final Predicate<Node> condition, final boolean includeSelf,
            final List<Pair<Predicate<Node>, Predicate<Node>>> paranthesisTypes) {
        return NodeUtils.findFirst(parent, node -> node.getPreviousSibling(), condition, includeSelf, paranthesisTypes);
    }

    private static Node firstAncestor(final Node parent, final Predicate<Node> condition, final boolean includeSelf) {
        return NodeUtils.findFirst(parent, node -> node.getParentNode(), condition, includeSelf);
    }

    public static Set<Node> allDescendents(final Node node, final Predicate<Node> predicate,
            final boolean includeSelf) {
        return NodeUtils.addAllDescendentsTo(node, predicate, includeSelf, new HashSet<>());
    }

    private static <T extends Collection<Node>> T addAllDescendentsTo(final Node node, final Predicate<Node> predicate,
            final boolean includeSelf, final T selectedNodes) {
        if (predicate.test(node) && includeSelf) {
            selectedNodes.add(node);
        }

        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            final Node child = node.getChildNodes().item(i);
            NodeUtils.addAllDescendentsTo(child, predicate, true, selectedNodes);
        }

        return selectedNodes;
    }

    public static String getNameOfOperation(final Node node) {
        if (Predicates.isSubroutineStatement.test(node)) {
            return NodeUtils.getNameOfOperation(node, Predicates.isSubroutineName);
        } else if (Predicates.isFunctionStatement.test(node)) {
            return NodeUtils.getNameOfOperation(node, Predicates.isFunctionName);
        } else if (Predicates.isEntryStatement.test(node)) {
            return NodeUtils.getNameOfOperation(node, Predicates.isEntryName);
        } else if (Predicates.isProgramStatement.test(node)) {
            return NodeUtils.ROOT_PROGRAM;
        }

        throw new IllegalArgumentException("Node is not a function, subroutine, entry or program statement.");
    }

    private static String getNameOfOperation(final Node node, final Predicate<Node> predicate) {
        final Set<Node> nameNodes = NodeUtils.allDescendents(node, predicate, true);
        return NodeUtils.getName(nameNodes.iterator().next());
    }

    public static String getName(final Node node) {
        final Node bigNNode = node.getFirstChild();
        final Node littleNNode = bigNNode.getFirstChild();
        return littleNNode.getTextContent().toLowerCase(Locale.getDefault());
    }

    public static String getCalleeNameFromCall(final Node callStatementNode) {
        return NodeUtils.getName(callStatementNode.getFirstChild().getNextSibling());
    }

    public static List<Pair<String, String>> findSubroutineCalls(final Node node)
            throws ParserConfigurationException, SAXException, IOException {
        return NodeUtils.findOperationCalls(node, Predicates.isCallStatement.and(Predicates.isLocalAccess.negate()),
                subroutineCall -> NodeUtils.nameOfCalledOperation(subroutineCall));
    }

    public static Node getAssignmentExpression(final Node stmt) { // right
        final Element e = (Element) stmt;
        final NodeList elems = e.getElementsByTagName("E-2");
        return elems.item(0);
    }

    public static Node getAssigmentVariable(final Node statement) {
        final Element e = (Element) statement;
        final NodeList elems = e.getElementsByTagName("E-1");
        return elems.item(0);
    }

    public static List<Pair<String, String>> findFunctionCalls(final Node node)
            throws ParserConfigurationException, SAXException, IOException {
        return NodeUtils.findOperationCalls(node,
                Predicates.isNamedExpressionAccess.and(Predicates.isLocalAccess.negate()),
                functionCall -> NodeUtils.nameOfCalledFunction(functionCall));
    }

    private static List<Pair<String, String>> findOperationCalls(final Node node, final Predicate<Node> callPredicate,
            final Function<Node, String> calledOperation) {
        final Set<Pair<String, String>> result = new HashSet<>(); // Check for double entries
        final Set<Node> callStatements = NodeUtils.allDescendents(node, callPredicate, true);
        for (final Node callStatement : callStatements) {
            final String callee = calledOperation.apply(callStatement);
            final String caller = NodeUtils.getNameOfContainingOperation(callStatement);
            result.add(new Pair<>(caller, callee));
        }

        return ListUtils.ofM(result, Pair.getComparatorFirstSecond());
    }

    private static Node findContainingStatement(final Node parent, final Predicate<Node> condition,
            final List<Pair<Predicate<Node>, Predicate<Node>>> paranthesisTypes) {

        final Predicate<Node> hasSuchANodeAsLeftSibling = node -> NodeUtils.hasLeftSibling(node, condition, false);
        final Node siblingOfSuchNode = NodeUtils.firstAncestor(parent, hasSuchANodeAsLeftSibling,
                !condition.test(parent));

        if (siblingOfSuchNode != null) {
            return NodeUtils.firstLeftSibling(siblingOfSuchNode, condition, true, paranthesisTypes);
        } else {
            return null;
        }
    }

    private static String getNameOfContainingOperation(final Node node) {
        final Node containingOperationStatement = NodeUtils.findContainingStatement(node,
                Predicates.isOperationStatement, Predicates.paranthesisTypes);
        return containingOperationStatement == null ? NodeUtils.ROOT_PROGRAM
                : NodeUtils.getNameOfOperation(containingOperationStatement);
    }

    // We make some assumptions about the structure of the Fortran files (and the generated XML
    // representations).
    // If we hit a node that does not satisfy these assumptions, throw an exception so that we know
    // we need to
    // handle that case as well.
    public static boolean satisfiesAssumptions(final Node node) {

        if (node == null) {
            return true;
        }

        final short type = node.getNodeType();

        if ((type == Node.TEXT_NODE) && (node.getChildNodes().getLength() > 0)) {
            throw new IllegalArgumentException("text node with children");
        }

        if ("call-stmt".equals(node.getNodeName()) && (node.getChildNodes().getLength() < 2)) {
            NodeUtils.printNode(node, 0);
            throw new IllegalArgumentException("call statement with < 2 children");
        }

        if (Predicates.isNamedExpression.test(node)) {
            // NodeList children = node.getChildNodes();
            final Node firstChild = node.getFirstChild();
            if (!Predicates.isBigN.test(firstChild)) {
                throw new IllegalArgumentException(String.format(
                        "Expected <N> in named expression, but found %s as first child.", firstChild.getNodeName()));
            }

            final Node firstGrandChild = firstChild.getFirstChild();
            if (!Predicates.isSmallN.test(firstGrandChild)) {
                throw new IllegalArgumentException(
                        String.format("Expected <n> in named expression, but found %s as first gand child.",
                                firstGrandChild.getNodeName()));
            }

            if (firstGrandChild.getChildNodes().getLength() > 1) {
                NodeUtils.printNode(firstGrandChild, 0);
                throw new IllegalArgumentException(
                        "named expression with unexpected chlildren length list of first grandchild.");
            }
        }

        return true;
    }

    public int getNumberOfDescendants(final Node parent, final boolean countSelf) {
        return NodeUtils.allDescendents(parent, node -> true, countSelf).size();
    }

}
