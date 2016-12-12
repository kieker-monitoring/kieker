/**
 */
package kieker.analysisteetime.model.analysismodel.architecture;

import kieker.analysisteetime.model.analysismodel.architecture.impl.IndexedArchitectureFactoryImpl;

public interface IndexedArchitectureFactory {

	IndexedArchitectureFactory INSTANCE = new IndexedArchitectureFactoryImpl();

	IndexedArchitectureRoot createIndexedArchitectureRoot();

	IndexedComponentType createIndexedComponentType();

}
