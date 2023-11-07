/***************************************************************************
 * Copyright (C) 2021 OceanDSL (https://oceandsl.uni-kiel.de)
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
package org.oceandsl.tools.cmi;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import kieker.analysis.architecture.repository.ModelRepository;
import kieker.model.analysismodel.assembly.AssemblyOperation;
import kieker.model.analysismodel.assembly.AssemblyStorage;
import kieker.model.analysismodel.deployment.DeployedComponent;
import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeployedStorage;
import kieker.model.analysismodel.deployment.DeploymentContext;
import kieker.model.analysismodel.type.OperationType;
import kieker.model.analysismodel.type.StorageType;

import org.oceandsl.tools.cmi.stages.Report;

/**
 * @author Reiner Jung
 *
 */
public final class RepositoryUtils {

    private RepositoryUtils() {
        // private constructor
    }

    public static void print(final Report report, final ModelRepository repository) {
        report.addMessage("Print models"); // NOPMD testing tool
        for (final EObject model : repository.getModels().values()) {
            report.addMessage("---------------------"); // NOPMD testing tool
            RepositoryUtils.print(report, model, "");
        }
        report.addMessage("---------------------"); // NOPMD testing tool
    }

    public static void print(final Report report, final EObject object, final String offset) {
        object.eCrossReferences();
        report.addMessage(offset + object.getClass().getCanonicalName()); // NOPMD testing tool
        RepositoryUtils.printAttributes(object, offset + "  ");
        RepositoryUtils.printContainments(report, object, offset + "  ");
        RepositoryUtils.printReferences(report, object, offset + "  ");
    }

    private static void printReferences(final Report report, final EObject object, final String offset) {
        for (final EReference reference : object.eClass().getEAllReferences()) {
            if (!reference.isContainment()) {
                final Object result = object.eGet(reference, true);
                if (result instanceof List<?>) {
                    final List<?> list = (List<?>) result;
                    report.addMessage("%sref %s = {", offset, reference.getName());
                    for (final Object element : list) {
                        if (element instanceof EObject) {
                            ((EObject) element).eCrossReferences();
                            final boolean proxy = ((EObject) element).eIsProxy();
                            report.addMessage("%s %b %s", offset, proxy, RepositoryUtils.getName((EObject) element));
                        }
                    }
                    report.addMessage("%s}", offset); // NOPMD testing tool
                } else if (result instanceof EObject) {
                    ((EObject) result).eCrossReferences();
                    final boolean proxy = ((EObject) result).eIsProxy();
                    report.addMessage("%sref %s = %b %s", offset, reference.getName(), proxy, // NOPMD
                            RepositoryUtils.getName((EObject) result));
                } else {
                    report.addMessage("%sERROR", offset); // NOPMD
                }
            }
        }
    }

    public static String getName(final EObject result) {
        if (result instanceof DeployedOperation) {
            final DeployedOperation operation = (DeployedOperation) result;
            final String typeName;
            final AssemblyOperation assembly = operation.getAssemblyOperation();
            if (assembly != null) {
                final OperationType type = assembly.getOperationType();
                if (type != null) {
                    typeName = RepositoryUtils.getName(type);
                } else {
                    typeName = "<operation(t)>";
                }
            } else {
                typeName = "<operation(a)>";
            }
            return String.format("%s@%s.%s", RepositoryUtils.getName(operation.getComponent().getContext()),
                    RepositoryUtils.getName(operation.getComponent()), typeName);

        }
        if (result instanceof DeployedStorage) {
            final DeployedStorage storage = (DeployedStorage) result;
            final String typeName;
            final AssemblyStorage assembly = storage.getAssemblyStorage();
            if (assembly != null) {
                final StorageType type = assembly.getStorageType();
                if (type != null) {
                    typeName = RepositoryUtils.getName(type);
                } else {
                    typeName = "<storage(t)>";
                }
            } else {
                typeName = "<storage(a)>";
            }
            return String.format("%s@%s.%s", RepositoryUtils.getName(storage.getComponent().getContext()),
                    RepositoryUtils.getName(storage.getComponent()), typeName);
        }
        if (result instanceof DeploymentContext) {
            final DeploymentContext context = (DeploymentContext) result;
            return "<<" + context.getName() + ">>";
        }
        if (result instanceof DeployedComponent) {
            final DeployedComponent component = (DeployedComponent) result;
            return "deployed component " + component.getSignature();
        }
        final EClass clazz = result.eClass();
        for (final EAttribute attribute : clazz.getEAllAttributes()) {
            if ("signature".equals(attribute.getName())) {
                return "signature=" + result.eGet(attribute);
            } else if ("name".equals(attribute.getName())) {
                return "name=" + result.eGet(attribute);
            }
        }
        return result.toString();
    }

    private static void printContainments(final Report report, final EObject object, final String offset) {
        for (final EReference contained : object.eClass().getEAllContainments()) {
            final Object result = object.eGet(contained, true);
            if (result instanceof List<?>) {
                final List<?> list = (List<?>) result;
                System.out.printf("%s%s = {\n", offset, contained.getName()); // NOPMD
                for (final Object element : list) {
                    if (element instanceof EObject) {
                        RepositoryUtils.print(report, (EObject) element, "  " + offset);
                    }
                }
                System.out.printf("%s}\n", offset); // NOPMD
            } else if (result instanceof EObject) {
                System.out.printf("%s%s = \n", offset, contained.getName()); // NOPMD
                RepositoryUtils.print(report, (EObject) result, "  " + offset);
            } else {
                System.out.printf("%sERROR\n", offset); // NOPMD
            }
        }
    }

    private static void printAttributes(final EObject object, final String offset) {
        for (final EAttribute attribute : object.eClass().getEAllAttributes()) {
            final Object value = object.eGet(attribute);
            if (value != null) {
                System.out.printf("%s%s = '%s'\n", offset, attribute.getName(), value.toString()); // NOPMD
            } else {
                System.out.printf("%s%s = null\n", offset, attribute.getName()); // NOPMD
            }

        }
    }

}
