package com.example.ppo;

import org.eclipse.emf.ecore.EObject;

/**
 * @model 
 */
public interface USAddress extends EObject {
  /**
   * @model 
   */
  String getName();

	/**
	 * Sets the value of the '{@link com.example.ppo.USAddress#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

  /**
   * @model 
   */
  String getStreet();

	/**
	 * Sets the value of the '{@link com.example.ppo.USAddress#getStreet <em>Street</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Street</em>' attribute.
	 * @see #getStreet()
	 * @generated
	 */
	void setStreet(String value);

  /**
   * @model 
   */
  String getCity();

	/**
	 * Sets the value of the '{@link com.example.ppo.USAddress#getCity <em>City</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>City</em>' attribute.
	 * @see #getCity()
	 * @generated
	 */
	void setCity(String value);

  /**
   * @model 
   */
  String getState();

	/**
	 * Sets the value of the '{@link com.example.ppo.USAddress#getState <em>State</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>State</em>' attribute.
	 * @see #getState()
	 * @generated
	 */
	void setState(String value);

  /**
   * @model 
   */
  int getZip();

	/**
	 * Sets the value of the '{@link com.example.ppo.USAddress#getZip <em>Zip</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Zip</em>' attribute.
	 * @see #getZip()
	 * @generated
	 */
	void setZip(int value);

  /**
   * @model default="US" changeable="false"
   */
  String getCountry();
}
