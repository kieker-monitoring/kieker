/**
 */
package kieker.model.analysismodel.type;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Provided Interface Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.analysismodel.type.ProvidedInterfaceType#getProvidedOperationTypes <em>Provided Operation Types</em>}</li>
 *   <li>{@link kieker.model.analysismodel.type.ProvidedInterfaceType#getName <em>Name</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.type.TypePackage#getProvidedInterfaceType()
 * @model
 * @generated
 */
public interface ProvidedInterfaceType extends EObject {
	/**
	 * Returns the value of the '<em><b>Provided Operation Types</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link kieker.model.analysismodel.type.OperationType},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Provided Operation Types</em>' map.
	 * @see kieker.model.analysismodel.type.TypePackage#getProvidedInterfaceType_ProvidedOperationTypes()
	 * @model mapType="kieker.model.analysismodel.type.EStringToOperationTypeMapEntry&lt;org.eclipse.emf.ecore.EString, kieker.model.analysismodel.type.OperationType&gt;" ordered="false"
	 * @generated
	 */
	EMap<String, OperationType> getProvidedOperationTypes();

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see kieker.model.analysismodel.type.TypePackage#getProvidedInterfaceType_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.type.ProvidedInterfaceType#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

} // ProvidedInterfaceType
