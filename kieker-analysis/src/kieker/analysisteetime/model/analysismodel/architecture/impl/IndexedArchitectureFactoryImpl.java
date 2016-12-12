/**
 */
package kieker.analysisteetime.model.analysismodel.architecture.impl;

import kieker.analysisteetime.model.analysismodel.architecture.IndexedArchitectureFactory;
import kieker.analysisteetime.model.analysismodel.architecture.IndexedArchitectureRoot;
import kieker.analysisteetime.model.analysismodel.architecture.IndexedComponentType;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class IndexedArchitectureFactoryImpl implements IndexedArchitectureFactory {

	public IndexedArchitectureFactoryImpl() {}

	@Override
	public IndexedArchitectureRoot createIndexedArchitectureRoot() {
		return new IndexedArchitectureRootImpl();
	}

	@Override
	public IndexedComponentType createIndexedComponentType() {
		return new IndexedComponentTypeImpl();
	}

}
