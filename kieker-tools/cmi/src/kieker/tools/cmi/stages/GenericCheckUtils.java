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
package kieker.tools.cmi.stages;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import kieker.tools.cmi.RepositoryUtils;

/**
 * Utility class.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public final class GenericCheckUtils {

    private GenericCheckUtils() {
    }

    public static void checkReferences(final EClass modelClass, final TreeIterator<EObject> iterator,
            final Report report) {
        long errorCount = 0;
        while (iterator.hasNext()) {
            final EObject object = iterator.next();
            final EClass clazz = object.eClass();
            for (final EReference reference : clazz.getEAllReferences()) {
                final Object referencedObject = object.eGet(reference, true);
                if (referencedObject == null) {
                    report.addMessage("Model %s Missing referenced object: %s -> %s:%s", // NOPMD
                            modelClass.getName(), RepositoryUtils.getName(object), reference.getEType().getName(),
                            reference.getName());
                    errorCount++;

                }
            }
        }
        report.addMessage("Missing references in %s %s", report.getName(), errorCount);
    }

    public static void missingSignature(final TreeIterator<EObject> iterator, final Report report) {
        GenericCheckUtils.missingAttribute(iterator, "signature", report);
    }

    public static void missingName(final TreeIterator<EObject> iterator, final Report report) {
        GenericCheckUtils.missingAttribute(iterator, "name", report);
    }

    public static void missingPackage(final TreeIterator<EObject> iterator, final Report report) {
        GenericCheckUtils.missingAttribute(iterator, "package", report);
    }

    private static void missingAttribute(final TreeIterator<EObject> iterator, final String attributeName,
            final Report report) {
        long errorCount = 0;
        while (iterator.hasNext()) {
            final EObject object = iterator.next();
            final EClass clazz = object.eClass();
            for (final EAttribute attribute : clazz.getEAllAttributes()) {
                if (attributeName.equals(attribute.getName())) {
                    final Object value = object.eGet(attribute);
                    if (value == null) {
                        report.addMessage("Object %s of type %s has %s attribute, but no value", // NOPMD
                                attributeName, GenericCheckUtils.print(object), clazz.getName());
                        errorCount++;
                    }
                }
            }
        }
        report.addMessage("Missing %s in %s %s", attributeName, report.getName(), errorCount);
    }

    private static String print(final EObject object) {
        final EClass clazz = object.eClass();
        String result = "class " + clazz.getName();
        for (final EAttribute attribute : clazz.getEAllAttributes()) {
            result += " " + attribute.getName() + "=" + object.eGet(attribute);
        }
        for (final EReference reference : clazz.getEAllReferences()) {
            result += " " + reference.getEReferenceType().getName() + ":" + reference.getName() + "="
                    + object.eGet(reference);
        }
        return result;
    }
}
