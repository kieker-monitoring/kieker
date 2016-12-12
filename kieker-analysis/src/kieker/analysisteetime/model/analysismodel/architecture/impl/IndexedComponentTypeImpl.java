/**
 */
package kieker.analysisteetime.model.analysismodel.architecture.impl;

import java.util.Arrays;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EReference;

import kieker.analysisteetime.model.analysismodel.architecture.ArchitecturePackage;
import kieker.analysisteetime.model.analysismodel.architecture.IndexedComponentType;
import kieker.analysisteetime.model.analysismodel.architecture.OperationType;
import kieker.analysisteetime.modeltooling.EReferenceIndex;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class IndexedComponentTypeImpl extends ComponentTypeImpl implements IndexedComponentType {

	private final EReferenceIndex<String, OperationType> providedOperationsIndex;

	protected IndexedComponentTypeImpl() {
		super();

		final EReference providedOperationsFeature = ArchitecturePackage.eINSTANCE.getComponentType_ProvidedOperations();
		final EAttribute operationSignatureFeature = ArchitecturePackage.eINSTANCE.getOperationType_Signature();
		this.providedOperationsIndex = EReferenceIndex.createEmpty(this, providedOperationsFeature, Arrays.asList(operationSignatureFeature),
				OperationType::getSignature);
	}

	@Override
	public OperationType getOperationTypeByName(final String name) {
		return this.providedOperationsIndex.get(name);
	}

	@Override
	public boolean containsOperationTypeByName(final String name) {
		return this.providedOperationsIndex.contains(name);
	}

}
