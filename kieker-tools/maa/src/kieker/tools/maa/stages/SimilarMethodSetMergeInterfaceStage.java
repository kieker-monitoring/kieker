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
package kieker.tools.maa.stages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.EList;

import kieker.analysis.architecture.repository.ModelRepository;
import kieker.model.analysismodel.type.ProvidedInterfaceType;
import kieker.model.analysismodel.type.TypeModel;
import kieker.model.analysismodel.type.TypePackage;

/**
 *
 * @author Reiner Jung
 * @since 1.3.0
 */
public class SimilarMethodSetMergeInterfaceStage extends AbstractMergeInterfaceStage {

    private final int methodDistance;

    /**
     * Create interface merger.
     *
     * @param methodDistance
     *            maximal distance if methods of two interfaces
     */
    public SimilarMethodSetMergeInterfaceStage(final int methodDistance) {
        super();
        this.methodDistance = methodDistance;
    }

    @Override
    protected void execute(final ModelRepository repository) throws Exception {
        final TypeModel typeModel = repository.getModel(TypePackage.Literals.TYPE_MODEL);
        typeModel.getComponentTypes().values().forEach(componentType -> {
            // find all provided interfaces of a component and compute their distance
            final Stream<MethodDistance> distances = componentType.getProvidedInterfaceTypes().stream()
                    .map(iface -> this.computeDistances(iface, componentType.getProvidedInterfaceTypes()))
                    .reduce(new ArrayList<MethodDistance>(), (l, nl) -> this.joinList(l, nl)).stream()
                    .filter(distance -> distance.distance <= this.methodDistance);
            // group related interfaces
            final Collection<Set<ProvidedInterfaceType>> groupedInterfaces = this.groupInterfaces(distances);

            // merge them
            // groupedInterfaces.forEach(set -> {
            // System.out.println("------------");
            // set.forEach(iface -> this.printIface(iface));
            // });

            // updated dependent required interfaces

            // update assembly and deployment interfaces accordingly
        });
        this.outputPort.send(repository);
    }

    private void printIface(final ProvidedInterfaceType iface) {
        System.out.println(">> " + iface.getSignature());
        iface.getProvidedOperationTypes().values().forEach(op -> System.out.println("  " + op.getSignature()));
    }

    private Collection<Set<ProvidedInterfaceType>> groupInterfaces(final Stream<MethodDistance> distances) {
        final Map<ProvidedInterfaceType, Set<ProvidedInterfaceType>> groupedInterfaces = new HashMap<>();

        distances.forEach(distance -> {
            if (groupedInterfaces.containsKey(distance.firstIface)) {
                groupedInterfaces.get(distance.firstIface).add(distance.firstIface);
                groupedInterfaces.get(distance.firstIface).add(distance.secondIface);
            } else {
                final Set<ProvidedInterfaceType> ifaceSet = new HashSet<>();
                ifaceSet.add(distance.firstIface);
                ifaceSet.add(distance.secondIface);
                groupedInterfaces.put(distance.firstIface, ifaceSet);
            }
            if (groupedInterfaces.containsKey(distance.secondIface)) {
                groupedInterfaces.get(distance.secondIface).add(distance.firstIface);
                groupedInterfaces.get(distance.secondIface).add(distance.secondIface);
            } else {
                final Set<ProvidedInterfaceType> ifaceSet = new HashSet<>();
                ifaceSet.add(distance.firstIface);
                ifaceSet.add(distance.secondIface);
                groupedInterfaces.put(distance.secondIface, ifaceSet);
            }
        });

        final Collection<Set<ProvidedInterfaceType>> distinctSets = new ArrayList<>();
        groupedInterfaces.values().forEach(set -> {
            if (!this.containsSet(distinctSets, set)) {
                distinctSets.add(set);
            }
        });

        return distinctSets;
    }

    private boolean containsSet(final Collection<Set<ProvidedInterfaceType>> distinctSets,
            final Set<ProvidedInterfaceType> set) {
        return distinctSets.stream().anyMatch(distinct -> this.compareSet(distinct, set));
    }

    private boolean compareSet(final Set<ProvidedInterfaceType> distinct, final Set<ProvidedInterfaceType> set) {
        return distinct.stream().allMatch(distinctElement -> set.contains(distinctElement));
    }

    private List<MethodDistance> joinList(final List<MethodDistance> list, final List<MethodDistance> secondList) {
        secondList.forEach(element -> {
            if (!this.isContainedInList(list, element)) {
                list.add(element);
            }
        });
        return list;
    }

    private boolean isContainedInList(final List<MethodDistance> list, final MethodDistance distance) {
        return list.stream().anyMatch(element -> ((element.firstIface == distance.firstIface)
                && (element.secondIface == distance.secondIface))
                || ((element.firstIface == distance.secondIface) && (element.secondIface == distance.firstIface)));
    }

    private List<MethodDistance> computeDistances(final ProvidedInterfaceType iface,
            final EList<ProvidedInterfaceType> providedInterfaceTypes) {
        return providedInterfaceTypes.stream().filter(secondIface -> secondIface != iface)
                .map(secondIface -> this.computeDistance(iface, secondIface))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private MethodDistance computeDistance(final ProvidedInterfaceType firstIface,
            final ProvidedInterfaceType secondIface) {
        final double da = this.countMatchingMethods(firstIface, secondIface);
        // firstIface.getProvidedOperationTypes().size();
        final double db = this.countMatchingMethods(secondIface, firstIface);
        // secondIface.getProvidedOperationTypes().size();

        System.err.printf("distance 1>2 %6.3f 2>1 %6.3f %3d %3d\n", da, db,
                firstIface.getProvidedOperationTypes().size(), secondIface.getProvidedOperationTypes().size());

        final double distance = da + db;

        return new MethodDistance(firstIface, secondIface, distance);
    }

    private double countMatchingMethods(final ProvidedInterfaceType firstIface,
            final ProvidedInterfaceType secondIface) {
        return firstIface.getProvidedOperationTypes().values().stream().map(operation -> {
            secondIface.getProvidedOperationTypes().values().forEach(v -> {
                if (operation.getSignature().equals(v.getSignature())) {
                    System.err.println(operation.getSignature() + " " + v.getSignature());
                }
            });
            return secondIface.getProvidedOperationTypes().containsValue(operation);
        }).filter(element -> element).count();
    }

    private class MethodDistance {

        private final ProvidedInterfaceType firstIface;
        private final ProvidedInterfaceType secondIface;
        private final double distance;

        public MethodDistance(final ProvidedInterfaceType firstIface, final ProvidedInterfaceType secondIface,
                final double distance) {
            this.firstIface = firstIface;
            this.secondIface = secondIface;
            this.distance = distance;
        }

    }

}
