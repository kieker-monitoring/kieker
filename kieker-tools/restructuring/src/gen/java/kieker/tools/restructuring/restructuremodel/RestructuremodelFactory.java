/**
 */
package kieker.tools.restructuring.restructuremodel;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see kieker.tools.restructuring.restructuremodel.RestructuremodelPackage
 * @generated
 */
public interface RestructuremodelFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	RestructuremodelFactory eINSTANCE = kieker.tools.restructuring.restructuremodel.impl.RestructuremodelFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Transformation Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Transformation Model</em>'.
	 * @generated
	 */
	TransformationModel createTransformationModel();

	/**
	 * Returns a new object of class '<em>Create Component</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Create Component</em>'.
	 * @generated
	 */
	CreateComponent createCreateComponent();

	/**
	 * Returns a new object of class '<em>Delete Component</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Delete Component</em>'.
	 * @generated
	 */
	DeleteComponent createDeleteComponent();

	/**
	 * Returns a new object of class '<em>Cut Operation</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Cut Operation</em>'.
	 * @generated
	 */
	CutOperation createCutOperation();

	/**
	 * Returns a new object of class '<em>Paste Operation</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Paste Operation</em>'.
	 * @generated
	 */
	PasteOperation createPasteOperation();

	/**
	 * Returns a new object of class '<em>Move Operation</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Move Operation</em>'.
	 * @generated
	 */
	MoveOperation createMoveOperation();

	/**
	 * Returns a new object of class '<em>Merge Component</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Merge Component</em>'.
	 * @generated
	 */
	MergeComponent createMergeComponent();

	/**
	 * Returns a new object of class '<em>Split Component</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Split Component</em>'.
	 * @generated
	 */
	SplitComponent createSplitComponent();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	RestructuremodelPackage getRestructuremodelPackage();

} //RestructuremodelFactory
