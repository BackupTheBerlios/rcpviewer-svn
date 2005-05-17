package com.example.ppo;

import java.util.Date;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * @model 
 */
public interface PurchaseOrder extends EObject {
  /**
   * @model 
   */
  String getComment();

	/**
	 * Sets the value of the '{@link com.example.ppo.PurchaseOrder#getComment <em>Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Comment</em>' attribute.
	 * @see #getComment()
	 * @generated
	 */
	void setComment(String value);

  /**
   * @model
   */
  Date getOrderDate();

	/**
	 * Sets the value of the '{@link com.example.ppo.PurchaseOrder#getOrderDate <em>Order Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Order Date</em>' attribute.
	 * @see #getOrderDate()
	 * @generated
	 */
	void setOrderDate(Date value);

  /**
   * @model type="Item" containment="true"
   */
  EList getItems();

  /**
   * @model containment="true" required="true"
   */
  USAddress getBillTo();

	/**
	 * Sets the value of the '{@link com.example.ppo.PurchaseOrder#getBillTo <em>Bill To</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Bill To</em>' containment reference.
	 * @see #getBillTo()
	 * @generated
	 */
	void setBillTo(USAddress value);

  /**
   * @model containment="true" required="true"
   */
  USAddress getShipTo();
	/**
	 * Sets the value of the '{@link com.example.ppo.PurchaseOrder#getShipTo <em>Ship To</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ship To</em>' containment reference.
	 * @see #getShipTo()
	 * @generated
	 */
	void setShipTo(USAddress value);

}
