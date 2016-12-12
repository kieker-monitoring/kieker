/**
 */
package kieker.analysisteetime.model.analysismodel.architecture.impl;

import java.util.Arrays;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EReference;

import kieker.analysisteetime.model.analysismodel.architecture.ArchitecturePackage;
import kieker.analysisteetime.model.analysismodel.architecture.ComponentType;
import kieker.analysisteetime.model.analysismodel.architecture.IndexedArchitectureRoot;
import kieker.analysisteetime.modeltooling.EReferenceIndex;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class IndexedArchitectureRootImpl extends ArchitectureRootImpl implements IndexedArchitectureRoot {

	private final EReferenceIndex<String, ComponentType> componentTypeIndex;

	protected IndexedArchitectureRootImpl() {
		super();

		final EReference componentTypesFeature = ArchitecturePackage.eINSTANCE.getArchitectureRoot_ComponentTypes();
		final EAttribute componentSignatureFeature = ArchitecturePackage.eINSTANCE.getComponentType_Signature();
		this.componentTypeIndex = EReferenceIndex.createEmpty(this, componentTypesFeature, Arrays.asList(componentSignatureFeature),
				ComponentType::getSignature);
	}

	@Override
	public ComponentType getComponentTypeByName(final String name) {
		return this.componentTypeIndex.get(name);
	}

	@Override
	public boolean containsComponentTypeByName(final String name) {
		return this.componentTypeIndex.contains(name);
	}

}
