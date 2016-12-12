
package kieker.analysisteetime.model.analysismodel.architecture;

public interface IndexedComponentType extends ComponentType {

	// List<IndexedOperationType> getIndexedOperationType();

	OperationType getOperationTypeByName(String name);

	boolean containsOperationTypeByName(String name);

}
