/**
 */
package kieker.model.analysismodel.type;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see kieker.model.analysismodel.type.TypePackage
 * @generated
 */
public interface TypeFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	TypeFactory eINSTANCE = kieker.model.analysismodel.type.impl.TypeFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model</em>'.
	 * @generated
	 */
	TypeModel createTypeModel();

	/**
	 * Returns a new object of class '<em>Component Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Component Type</em>'.
	 * @generated
	 */
	ComponentType createComponentType();

	/**
	 * Returns a new object of class '<em>Operation Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Operation Type</em>'.
	 * @generated
	 */
	OperationType createOperationType();

	/**
	 * Returns a new object of class '<em>Storage Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Storage Type</em>'.
	 * @generated
	 */
	StorageType createStorageType();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	TypePackage getTypePackage();

} //TypeFactory
