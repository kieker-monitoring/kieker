/**
 */
package kieker.model.analysismodel.execution;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Tuple</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.execution.Tuple#getFirst <em>First</em>}</li>
 * <li>{@link kieker.model.analysismodel.execution.Tuple#getSecond <em>Second</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.execution.ExecutionPackage#getTuple()
 * @model
 * @generated
 */
public interface Tuple<F, S> extends EObject {
	/**
	 * Returns the value of the '<em><b>First</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>First</em>' reference.
	 * @see #setFirst(Object)
	 * @see kieker.model.analysismodel.execution.ExecutionPackage#getTuple_First()
	 * @model kind="reference"
	 * @generated
	 */
	F getFirst();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.execution.Tuple#getFirst <em>First</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value
	 *            the new value of the '<em>First</em>' reference.
	 * @see #getFirst()
	 * @generated
	 */
	void setFirst(F value);

	/**
	 * Returns the value of the '<em><b>Second</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Second</em>' reference.
	 * @see #setSecond(Object)
	 * @see kieker.model.analysismodel.execution.ExecutionPackage#getTuple_Second()
	 * @model kind="reference"
	 * @generated
	 */
	S getSecond();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.execution.Tuple#getSecond <em>Second</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value
	 *            the new value of the '<em>Second</em>' reference.
	 * @see #getSecond()
	 * @generated
	 */
	void setSecond(S value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @model
	 * @generated
	 */
	boolean equals(Object value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @model
	 * @generated
	 */
	int hashCode();

} // Tuple
