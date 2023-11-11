/**
 */
package kieker.tools.restructuring.restructuremodel;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Move Operation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link kieker.tools.restructuring.restructuremodel.MoveOperation#getFrom <em>From</em>}</li>
 *   <li>{@link kieker.tools.restructuring.restructuremodel.MoveOperation#getTo <em>To</em>}</li>
 *   <li>{@link kieker.tools.restructuring.restructuremodel.MoveOperation#getOperationName <em>Operation Name</em>}</li>
 *   <li>{@link kieker.tools.restructuring.restructuremodel.MoveOperation#getCutOperation <em>Cut Operation</em>}</li>
 *   <li>{@link kieker.tools.restructuring.restructuremodel.MoveOperation#getPasteOperation <em>Paste Operation</em>}</li>
 * </ul>
 *
 * @see kieker.tools.restructuring.restructuremodel.RestructuremodelPackage#getMoveOperation()
 * @model
 * @generated
 */
public interface MoveOperation extends AbstractTransformationStep {
	/**
	 * Returns the value of the '<em><b>From</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>From</em>' attribute.
	 * @see #setFrom(String)
	 * @see kieker.tools.restructuring.restructuremodel.RestructuremodelPackage#getMoveOperation_From()
	 * @model
	 * @generated
	 */
	String getFrom();

	/**
	 * Sets the value of the '{@link kieker.tools.restructuring.restructuremodel.MoveOperation#getFrom <em>From</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>From</em>' attribute.
	 * @see #getFrom()
	 * @generated
	 */
	void setFrom(String value);

	/**
	 * Returns the value of the '<em><b>To</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>To</em>' attribute.
	 * @see #setTo(String)
	 * @see kieker.tools.restructuring.restructuremodel.RestructuremodelPackage#getMoveOperation_To()
	 * @model
	 * @generated
	 */
	String getTo();

	/**
	 * Sets the value of the '{@link kieker.tools.restructuring.restructuremodel.MoveOperation#getTo <em>To</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>To</em>' attribute.
	 * @see #getTo()
	 * @generated
	 */
	void setTo(String value);

	/**
	 * Returns the value of the '<em><b>Operation Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Operation Name</em>' attribute.
	 * @see #setOperationName(String)
	 * @see kieker.tools.restructuring.restructuremodel.RestructuremodelPackage#getMoveOperation_OperationName()
	 * @model
	 * @generated
	 */
	String getOperationName();

	/**
	 * Sets the value of the '{@link kieker.tools.restructuring.restructuremodel.MoveOperation#getOperationName <em>Operation Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Operation Name</em>' attribute.
	 * @see #getOperationName()
	 * @generated
	 */
	void setOperationName(String value);

	/**
	 * Returns the value of the '<em><b>Cut Operation</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cut Operation</em>' containment reference.
	 * @see #setCutOperation(CutOperation)
	 * @see kieker.tools.restructuring.restructuremodel.RestructuremodelPackage#getMoveOperation_CutOperation()
	 * @model containment="true"
	 * @generated
	 */
	CutOperation getCutOperation();

	/**
	 * Sets the value of the '{@link kieker.tools.restructuring.restructuremodel.MoveOperation#getCutOperation <em>Cut Operation</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cut Operation</em>' containment reference.
	 * @see #getCutOperation()
	 * @generated
	 */
	void setCutOperation(CutOperation value);

	/**
	 * Returns the value of the '<em><b>Paste Operation</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Paste Operation</em>' containment reference.
	 * @see #setPasteOperation(PasteOperation)
	 * @see kieker.tools.restructuring.restructuremodel.RestructuremodelPackage#getMoveOperation_PasteOperation()
	 * @model containment="true"
	 * @generated
	 */
	PasteOperation getPasteOperation();

	/**
	 * Sets the value of the '{@link kieker.tools.restructuring.restructuremodel.MoveOperation#getPasteOperation <em>Paste Operation</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Paste Operation</em>' containment reference.
	 * @see #getPasteOperation()
	 * @generated
	 */
	void setPasteOperation(PasteOperation value);

} // MoveOperation
