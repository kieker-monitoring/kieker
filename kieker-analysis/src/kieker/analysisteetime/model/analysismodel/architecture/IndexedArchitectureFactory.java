/**
 */
package kieker.analysisteetime.model.analysismodel.architecture;

import kieker.analysisteetime.model.analysismodel.architecture.impl.IndexedArchitectureFactoryImpl;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public interface IndexedArchitectureFactory {

	IndexedArchitectureFactory INSTANCE = new IndexedArchitectureFactoryImpl();

	IndexedArchitectureRoot createIndexedArchitectureRoot();

	IndexedComponentType createIndexedComponentType();

}
