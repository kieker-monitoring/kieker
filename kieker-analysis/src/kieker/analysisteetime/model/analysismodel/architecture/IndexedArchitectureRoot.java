/**
 */
package kieker.analysisteetime.model.analysismodel.architecture;

public interface IndexedArchitectureRoot extends ArchitectureRoot {

	// List<IndexedComponentType> getIndexedComponentTypes();

	ComponentType getComponentTypeByName(String name);

	boolean containsComponentTypeByName(String name);

}
