/**
 */
package kieker.tools.restructuring.restructuremodel;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Merge Component</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link kieker.tools.restructuring.restructuremodel.MergeComponent#getMergeGoalComponent <em>Merge Goal Component</em>}</li>
 * <li>{@link kieker.tools.restructuring.restructuremodel.MergeComponent#getComponentName <em>Component Name</em>}</li>
 * <li>{@link kieker.tools.restructuring.restructuremodel.MergeComponent#getOperations <em>Operations</em>}</li>
 * <li>{@link kieker.tools.restructuring.restructuremodel.MergeComponent#getDeleteTransformation <em>Delete Transformation</em>}</li>
 * <li>{@link kieker.tools.restructuring.restructuremodel.MergeComponent#getOperationToMove <em>Operation To Move</em>}</li>
 * </ul>
 *
 * @see kieker.tools.restructuring.restructuremodel.RestructuremodelPackage#getMergeComponent()
 * @model
 * @generated
 */
public interface MergeComponent extends AbstractTransformationStep {
	/**
	 * Returns the value of the '<em><b>Merge Goal Component</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Merge Goal Component</em>' attribute.
	 * @see #setMergeGoalComponent(String)
	 * @see kieker.tools.restructuring.restructuremodel.RestructuremodelPackage#getMergeComponent_MergeGoalComponent()
	 * @model
	 * @generated
	 */
	String getMergeGoalComponent();

	/**
	 * Sets the value of the '{@link kieker.tools.restructuring.restructuremodel.MergeComponent#getMergeGoalComponent <em>Merge Goal Component</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value
	 *            the new value of the '<em>Merge Goal Component</em>' attribute.
	 * @see #getMergeGoalComponent()
	 * @generated
	 */
	void setMergeGoalComponent(String value);

	/**
	 * Returns the value of the '<em><b>Component Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Component Name</em>' attribute.
	 * @see #setComponentName(String)
	 * @see kieker.tools.restructuring.restructuremodel.RestructuremodelPackage#getMergeComponent_ComponentName()
	 * @model
	 * @generated
	 */
	String getComponentName();

	/**
	 * Sets the value of the '{@link kieker.tools.restructuring.restructuremodel.MergeComponent#getComponentName <em>Component Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value
	 *            the new value of the '<em>Component Name</em>' attribute.
	 * @see #getComponentName()
	 * @generated
	 */
	void setComponentName(String value);

	/**
	 * Returns the value of the '<em><b>Operations</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Operations</em>' attribute list.
	 * @see kieker.tools.restructuring.restructuremodel.RestructuremodelPackage#getMergeComponent_Operations()
	 * @model required="true"
	 * @generated
	 */
	EList<String> getOperations();

	/**
	 * Returns the value of the '<em><b>Delete Transformation</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Delete Transformation</em>' containment reference.
	 * @see #setDeleteTransformation(DeleteComponent)
	 * @see kieker.tools.restructuring.restructuremodel.RestructuremodelPackage#getMergeComponent_DeleteTransformation()
	 * @model containment="true"
	 * @generated
	 */
	DeleteComponent getDeleteTransformation();

	/**
	 * Sets the value of the '{@link kieker.tools.restructuring.restructuremodel.MergeComponent#getDeleteTransformation <em>Delete Transformation</em>}' containment
	 * reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value
	 *            the new value of the '<em>Delete Transformation</em>' containment reference.
	 * @see #getDeleteTransformation()
	 * @generated
	 */
	void setDeleteTransformation(DeleteComponent value);

	/**
	 * Returns the value of the '<em><b>Operation To Move</b></em>' containment reference list.
	 * The list contents are of type {@link kieker.tools.restructuring.restructuremodel.MoveOperation}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Operation To Move</em>' containment reference list.
	 * @see kieker.tools.restructuring.restructuremodel.RestructuremodelPackage#getMergeComponent_OperationToMove()
	 * @model containment="true" required="true"
	 * @generated
	 */
	EList<MoveOperation> getOperationToMove();

} // MergeComponent
