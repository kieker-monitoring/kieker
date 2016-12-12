
package kieker.analysisteetime.model.analysismodel.architecture;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public interface IndexedComponentType extends ComponentType {

	// List<IndexedOperationType> getIndexedOperationType();

	OperationType getOperationTypeByName(String name);

	boolean containsOperationTypeByName(String name);

}
