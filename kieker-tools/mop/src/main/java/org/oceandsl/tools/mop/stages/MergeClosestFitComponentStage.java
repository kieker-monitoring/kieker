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
package org.oceandsl.tools.mop.stages;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.util.EcoreUtil;

import kieker.analysis.architecture.repository.ModelRepository;
import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.assembly.AssemblyPackage;
import kieker.model.analysismodel.deployment.DeployedComponent;
import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeploymentModel;
import kieker.model.analysismodel.deployment.DeploymentPackage;
import kieker.model.analysismodel.execution.ExecutionModel;
import kieker.model.analysismodel.execution.ExecutionPackage;
import kieker.model.analysismodel.execution.Invocation;
import kieker.model.analysismodel.execution.Tuple;
import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.TypeModel;
import kieker.model.analysismodel.type.TypePackage;

import teetime.framework.OutputPort;
import teetime.stage.basic.AbstractTransformation;

import org.oceandsl.analysis.generic.Table;

/**
 * Identify matching components based on the number of shared operations. This merger assumes that
 * operation are fully qualified and do not appear twice. This can be an issue with object oriented
 * software where methods of different classes can have the same name. For these cases, the method
 * names must be prefixed with their initial FQN class name.
 *
 * @author Reiner Jung
 * @since 1.3.0
 *
 */
public class MergeClosestFitComponentStage extends AbstractTransformation<ModelRepository, ModelRepository> {

    private ModelRepository lastRepository;

    private final OutputPort<Table<String, SimilarityEntry>> similarityOutputPort = this
            .createOutputPort("similarityOutputPort");

    private final double threshold;

    public MergeClosestFitComponentStage(final double threshold) {
        this.threshold = threshold;
    }

    public OutputPort<Table<String, SimilarityEntry>> getSimilarityOutputPort() {
        return this.similarityOutputPort;
    }

    @Override
    protected void execute(final ModelRepository repository) throws Exception {
        repository.getModels().values().forEach(model -> EcoreUtil.resolveAll(model.eResource()));
        if (this.lastRepository == null) {
            this.lastRepository = repository;
        } else {
            final List<SimilarityEntry> entries = this.computeComponentNameSimilarites(repository);

            this.sortForBestFit(entries);
            this.removeLesserFittingEntries(entries);
            this.removeEntriesBelowThreshold(entries);
            this.ensureNoNameClashes(entries, repository);

            this.similarityOutputPort.send(this.makeTable(entries, repository.getName()));

            // this.checkExecution(repository, "BEFORE");

            this.changeComponentNamesBasedOnBestFit(repository, entries);

            // this.checkExecution(repository, "AFTER");
        }
        this.outputPort.send(repository);
    }

    private void checkExecution(final ModelRepository repository, final String name) {
        System.err.println("++ " + name);
        final ExecutionModel em = repository.getModel(ExecutionPackage.Literals.EXECUTION_MODEL);
        em.getInvocations().entrySet().forEach(m -> {
            final Tuple<DeployedOperation, DeployedOperation> key = m.getKey();
            final Invocation value = m.getValue();
            this.check(key.getFirst(), "first");
            this.check(key.getSecond(), "second");
            this.check(value.getCaller(), "caller");
            this.check(value.getCallee(), "callee");
        });
    }

    private void check(final DeployedOperation op, final String string) {
        final DeployedComponent dc = (DeployedComponent) op.eContainer().eContainer();
        if (dc == null) {
            System.err.println(
                    ">> container " + op.getAssemblyOperation().getOperationType().getSignature() + " " + string);
            return;
        }
        if (op.getComponent() == null) {
            System.err.println(
                    ">> component " + op.getAssemblyOperation().getOperationType().getSignature() + " " + string);
            return;
        }

        if (dc.getAssemblyComponent().getComponentType() == null) {
            System.err.println("shite " + string);
            return;
        }

        if (dc.eContainer() == null) {
            System.err.println(">> component NOT CONTAINED " + dc.getSignature());
        }
        if (dc.getContext() == null) {
            System.err.println(">> component NO CONTEXT " + dc.getSignature());
        }
    }

    private void ensureNoNameClashes(final List<SimilarityEntry> entries, final ModelRepository repository) {
        final TypeModel model = repository.getModel(TypePackage.Literals.TYPE_MODEL);
        // we need to ensure that the new name for a component is not already present in the
        // model.
        entries.forEach(entry -> {
            final String signature = entry.getLeft().getSignature();
            final ComponentType component = model.getComponentTypes().get(signature);
            if (component != null && entry.getRight() != component) {
                entry.incrementEqualNamesCount();
            }
        });
    }

    private List<SimilarityEntry> computeComponentNameSimilarites(final ModelRepository repository) {
        final TypeModel lastTypeModel = this.lastRepository.getModel(TypePackage.Literals.TYPE_MODEL);
        final TypeModel newTypeModel = repository.getModel(TypePackage.Literals.TYPE_MODEL);

        final ComponentType[] lastComponents = lastTypeModel.getComponentTypes().values()
                .toArray(new ComponentType[lastTypeModel.getComponentTypes().values().size()]);
        final ComponentType[] newComponents = newTypeModel.getComponentTypes().values()
                .toArray(new ComponentType[newTypeModel.getComponentTypes().values().size()]);

        final List<SimilarityEntry> entries = new ArrayList<>();

        for (final ComponentType left : lastComponents) {
            for (final ComponentType right : newComponents) {
                entries.add(new SimilarityEntry(left, right, this.computeSimilarity(left, right)));
            }
        }
        return entries;
    }

    private void sortForBestFit(final List<SimilarityEntry> entries) {
        // sort best fit
        entries.sort((a, b) -> { // NOCS this is how a comparator is implemented
            if (a.getSimilarity() < b.getSimilarity()) {
                return 1;
            } else if (a.getSimilarity() > b.getSimilarity()) {
                return -1;
            } else {
                return 0;
            }
        });
    }

    private void removeLesserFittingEntries(final List<SimilarityEntry> entries) {
        for (int i = 0; i < entries.size(); i++) {
            final SimilarityEntry current = entries.get(i);
            for (int j = i + 1; j < entries.size(); j++) {
                final SimilarityEntry next = entries.get(j);
                // really check if objects are identical
                if (current.getLeft() == next.getLeft() || current.getLeft() == next.getRight() // NOPMD
                        || current.getRight() == next.getLeft() || current.getRight() == next.getRight()) { // NOPMD
                    entries.remove(j);
                    j--; // NOCS, NOPMD this is necessary due to list operations
                }
            }
        }
    }

    private void removeEntriesBelowThreshold(final List<SimilarityEntry> entries) {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getSimilarity() < this.threshold) {
                entries.remove(i);
                i--; // NOCS, NOPMD
            }
        }
    }

    private void changeComponentNamesBasedOnBestFit(final ModelRepository repository,
            final List<SimilarityEntry> entries) {
        entries.forEach(entry -> {
            final String replacement;
            final ComponentType componentToRename;
            if (this.isNumber(entry.getRight().getSignature()) || this.isHash(entry.getRight().getSignature())) {
                // use left signature
                replacement = this.makeName(entry.getLeft().getSignature(), entry.getEqualNamesCount());
                componentToRename = entry.getRight();
            } else if (this.isNumber(entry.getLeft().getSignature()) || this.isHash(entry.getLeft().getSignature())) {
                // use right signature
                replacement = this.makeName(entry.getRight().getSignature(), entry.getEqualNamesCount());
                componentToRename = entry.getLeft();
            } else {
                // default to left signature
                replacement = this.makeName(entry.getLeft().getSignature(), entry.getEqualNamesCount());
                componentToRename = entry.getRight();
            }
            final String search = componentToRename.getSignature();
            this.fixDeploymentSignature(repository, search, replacement);
            this.fixAssemblySignature(repository, search, replacement);
            this.fixTypeSignature(repository, search, replacement);
            componentToRename.setSignature(replacement);
        });

    }

    private String makeName(final String signature, final int equalNamesCount) {
        if (equalNamesCount > 0) {
            return String.format("%s_%d", signature, equalNamesCount);
        } else {
            return signature;
        }
    }

    private Table<String, SimilarityEntry> makeTable(final List<SimilarityEntry> entries, final String name) {
        final Table<String, SimilarityEntry> table = new Table<>(name);
        table.getRows().addAll(entries);
        return table;
    }

    private void fixTypeSignature(final ModelRepository repository, final String search, final String replacement) {
        final TypeModel model = repository.getModel(TypePackage.Literals.TYPE_MODEL);
        final ComponentType component = model.getComponentTypes().get(search);
        component.setSignature(replacement);
        component.setName(replacement);
        model.getComponentTypes().removeKey(search);
        model.getComponentTypes().put(replacement, component);
    }

    private void fixAssemblySignature(final ModelRepository repository, final String search, final String replacement) {
        final AssemblyModel model = repository.getModel(AssemblyPackage.Literals.ASSEMBLY_MODEL);
        final AssemblyComponent component = model.getComponents().get(search);
        component.setSignature(replacement);
        model.getComponents().removeKey(search);
        model.getComponents().put(replacement, component);
    }

    private void fixDeploymentSignature(final ModelRepository repository, final String search,
            final String replacement) {
        final DeploymentModel model = repository.getModel(DeploymentPackage.Literals.DEPLOYMENT_MODEL);
        model.getContexts().values().forEach(context -> {
            final DeployedComponent component = context.getComponents().get(search);
            component.setSignature(replacement);
            context.getComponents().removeKey(search);
            context.getComponents().put(replacement, component);
        });
    }

    private boolean isNumber(final String signature) {
        try {
            Integer.parseInt(signature);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }

    private boolean isHash(final String signature) {
        if (signature.length() == 32) {
            return signature.matches("[a-f0-9]*");
        } else {
            return false;
        }
    }

    private double computeSimilarity(final ComponentType left, final ComponentType right) {
        final Set<String> leftSet = left.getProvidedOperations().keySet();
        final Set<String> rightSet = right.getProvidedOperations().keySet();

        final double leftMatch = leftSet.stream().filter(l -> rightSet.contains(l)).count();
        final double rightMatch = rightSet.stream().filter(r -> leftSet.contains(r)).count();

        return (leftMatch / leftSet.size() + rightMatch / rightSet.size()) / 2.0d;
    }
}
