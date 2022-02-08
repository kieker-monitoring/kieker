/**
 */
package kieker.model.collection;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see kieker.model.collection.CollectionPackage
 * @generated
 */
public interface CollectionFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	CollectionFactory eINSTANCE = kieker.model.collection.impl.CollectionFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Interface Collection</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Interface Collection</em>'.
	 * @generated
	 */
	InterfaceCollection createInterfaceCollection();

	/**
	 * Returns a new object of class '<em>Operation Collection</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Operation Collection</em>'.
	 * @generated
	 */
	OperationCollection createOperationCollection();

	/**
	 * Returns a new object of class '<em>Coupling</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Coupling</em>'.
	 * @generated
	 */
	Coupling createCoupling();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	CollectionPackage getCollectionPackage();

} //CollectionFactory
