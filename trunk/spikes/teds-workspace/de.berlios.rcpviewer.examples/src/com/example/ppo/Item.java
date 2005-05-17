package com.example.ppo;

import java.util.Date;

import org.eclipse.emf.ecore.EObject;

/**
 * @model
 */
public interface Item extends EObject {
  /**
   * @model
   */
  String getProductName();

	/**
	 * Sets the value of the '{@link com.example.ppo.Item#getProductName <em>Product Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Product Name</em>' attribute.
	 * @see #getProductName()
	 * @generated
	 */
	void setProductName(String value);

  /**
   * @model
   */
  int getQuantity();

	/**
	 * Sets the value of the '{@link com.example.ppo.Item#getQuantity <em>Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Quantity</em>' attribute.
	 * @see #getQuantity()
	 * @generated
	 */
	void setQuantity(int value);

  /**
   * @model
   */
  int getUSPrice();

	/**
	 * Sets the value of the '{@link com.example.ppo.Item#getUSPrice <em>US Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>US Price</em>' attribute.
	 * @see #getUSPrice()
	 * @generated
	 */
	void setUSPrice(int value);

  /**
   * @model
   */
  String getComment();

	/**
	 * Sets the value of the '{@link com.example.ppo.Item#getComment <em>Comment</em>}' attribute.
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
  Date getShipDate();

	/**
	 * Sets the value of the '{@link com.example.ppo.Item#getShipDate <em>Ship Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ship Date</em>' attribute.
	 * @see #getShipDate()
	 * @generated
	 */
	void setShipDate(Date value);

  /**
   * @model dataType="SKU"
   */
  String getPartNum();
	/**
	 * Sets the value of the '{@link com.example.ppo.Item#getPartNum <em>Part Num</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Part Num</em>' attribute.
	 * @see #getPartNum()
	 * @generated
	 */
	void setPartNum(String value);

}
