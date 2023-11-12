/**
 */
package kieker.tools.restructuring.restructuremodel;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Transformation Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link kieker.tools.restructuring.restructuremodel.TransformationModel#getTransformations <em>Transformations</em>}</li>
 *   <li>{@link kieker.tools.restructuring.restructuremodel.TransformationModel#getName <em>Name</em>}</li>
 * </ul>
 *
 * @see kieker.tools.restructuring.restructuremodel.RestructuremodelPackage#getTransformationModel()
 * @model
 * @generated
 */
public interface TransformationModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Transformations</b></em>' containment reference list.
	 * The list contents are of type {@link kieker.tools.restructuring.restructuremodel.AbstractTransformationStep}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Transformations</em>' containment reference list.
	 * @see kieker.tools.restructuring.restructuremodel.RestructuremodelPackage#getTransformationModel_Transformations()
	 * @model containment="true" required="true"
	 * @generated
	 */
	EList<AbstractTransformationStep> getTransformations();

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see kieker.tools.restructuring.restructuremodel.RestructuremodelPackage#getTransformationModel_Name()
	 * @model id="true" required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link kieker.tools.restructuring.restructuremodel.TransformationModel#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

} // TransformationModel
