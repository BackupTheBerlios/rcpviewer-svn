package org.essentialplatform.progmodel.extended;

import org.essentialplatform.progmodel.standard.DescribedAs;
import org.essentialplatform.progmodel.standard.InDomain;

@InDomain
public class CustomerToTestMinMaxFieldLengthOf {


	/**
	 * No annotations at all.
	 * 
	 * @return
	 */
	public String getAttributeWithNoAnnotations() {
		return attributeWithNoAnnotations;
	}
	private String attributeWithNoAnnotations;

	/**
	 * Operation to check
	 * No annotations at all.
	 * 
	 * @return
	 */
	public void operationToUpdateAttributeWithNoAnnotations(
			@Named("attributeWithNoAnnotations")
			final String attributeWithNoAnnotations) {
		this.attributeWithNoAnnotations = attributeWithNoAnnotations;
	}


	/**
	 * Non-string attribute; annotations should be ignored.
	 * 
	 * @return
	 */
	@MinLengthOf(3)
	@FieldLengthOf(30)
	@MaxLengthOf(50)
	public int getNonStringAttributeWithLengthAnnotations() {
		return nonStringAttributeWithLengthAnnotations;
	}
	private int nonStringAttributeWithLengthAnnotations;

	/**
	 * Non-string attribute; annotations should be ignored.
	 * 
	 * @return
	 */
	public void operationToUpdateNonStringAttributeWithLengthAnnotations(
			@Named("nonStringAttributeWithLengthAnnotations")
			@MinLengthOf(3)
			@FieldLengthOf(30)
			@MaxLengthOf(50)
			final int nonStringAttributeWithLengthAnnotations) {
		this.nonStringAttributeWithLengthAnnotations = nonStringAttributeWithLengthAnnotations;
	}

	/**
	 * Just a max length.
	 * 
	 * @return
	 */
	@MaxLengthOf(64)
	public String getLastName() {
		return lastName;
	}
	private String lastName;
	/**
	 * Operation to update last name.
	 * 
	 * @return
	 */
	public void updateLastName(
			@Named("lastName")
			@MaxLengthOf(64)
			final String lastName) {
		this.lastName = lastName;
	}


	
	/**
	 * Just a field length.
	 * 
	 * @return
	 */
	@FieldLengthOf(32)
	public String getMiddleName() {
		return middleName;
	}
	private String middleName;

	/**
	 * Operation to update middle name.
	 * 
	 * @return
	 */
	public void updateMiddleName(
			@Named("middleName")
			@FieldLengthOf(32)
			final String middleName) {
		this.middleName = middleName;
	}

	/**
	 * Just a min length.
	 * 
	 * @return
	 */
	@MinLengthOf(12)
	public String getSuffix() {
		return suffix;
	}
	private String suffix;


	/**
	 * Operation with parameter that has just a min length.
	 * 
	 * @return
	 */
	public void updateSuffix(
			@Named("suffix")
			@MinLengthOf(12)
			final String suffix) {
		this.suffix = suffix;
	}

	/**
	 * Field length and max length, with field length < max length (as would
	 * be usual)
	 * 
	 * @return
	 */
	@FieldLengthOf(32)
	@MaxLengthOf(64)
	public String getFirstName() {
		return firstName;
	}
	private String firstName;
	
	/**
	 * A min length and a max length.
	 * 
	 * @return
	 */
	@MinLengthOf(3)
	@MaxLengthOf(128)
	public String getEmail() {
		return email;
	}
	private String email;
	

	/**
	 * All three annotations
	 * 
	 * @return
	 */
	@MinLengthOf(2)
	@FieldLengthOf(10)
	@MaxLengthOf(20)
	public String getFavoriteColour() {
		return favouriteColour;
	}
	private String favouriteColour;
	

	/**
	 * Error conditions: -ve value for field length, can use max length (and
	 * ignore the min length even though it too is valid)
	 * 
	 * @return
	 */
	@FieldLengthOf(-1)
	@MinLengthOf(10)
	@MaxLengthOf(20)
	public String getAttributeWithNegativeFieldLengthButValidMaxLength() {
		return attributeWithNegativeFieldLengthButValidMaxLength;
	}
	private String attributeWithNegativeFieldLengthButValidMaxLength;
	

	/**
	 * Operation to check:
	 * Error conditions: -ve value for field length, can use max length (and
	 * ignore the min length even though it too is valid)
	 */
	public void operationToUpdateAttributeWithNegativeFieldLengthButValidMaxLength(
			@Named("attributeWithNegativeFieldLengthButValidMaxLength")
			@FieldLengthOf(-1)
			@MinLengthOf(10)
			@MaxLengthOf(20)
			final String attributeWithNegativeFieldLengthButValidMaxLength) {
		this.attributeWithNegativeFieldLengthButValidMaxLength = attributeWithNegativeFieldLengthButValidMaxLength;
	}

	
	/**
	 * Error conditions: -ve values for field length, can use min length
	 * 
	 * @return
	 */
	@FieldLengthOf(-1)
	@MinLengthOf(20)
	public String getAttributeWithNegativeFieldLengthButValidMinLength() {
		return attributeWithNegativeFieldLengthButValidMinLength;
	}
	private String attributeWithNegativeFieldLengthButValidMinLength;

	/**
	 * Operation to check:
	 * Error conditions: -ve values for field length, can use min length
	 */
	public void operationToUpdateAttributeWithNegativeFieldLengthButValidMinLength(
			@Named("attributeWithNegativeFieldLengthButValidMinLength")
			@FieldLengthOf(-1)
			@MinLengthOf(20)
			final String attributeWithNegativeFieldLengthButValidMinLength) {
		this.attributeWithNegativeFieldLengthButValidMinLength = attributeWithNegativeFieldLengthButValidMinLength;
	}

	

	/**
	 * Error conditions: -ve values for max length, can use min length
	 * 
	 * @return
	 */
	@MinLengthOf(10)
	@MaxLengthOf(-1)
	public String getAttributeWithNegativeMaxLengthButValidMinLength() {
		return attributeWithNegativeMaxLengthButValidMinLength;
	}
	private String attributeWithNegativeMaxLengthButValidMinLength;

	/**
	 * Operation to check:
	 * Error conditions: -ve values for max length, can use min length
	 * 
	 * @return
	 */
	public void operationToUpdateAttributeWithNegativeMaxLengthButValidMinLength(
			@Named("attributeWithNegativeMaxLengthButValidMinLength")
			@MinLengthOf(10)
			@MaxLengthOf(-1)
			final String attributeWithNegativeMaxLengthButValidMinLength) {
		this.attributeWithNegativeMaxLengthButValidMinLength = attributeWithNegativeMaxLengthButValidMinLength;
	}


	/**
	 * Error conditions: -ve values for min length, don't use any other length
	 * 
	 * @return
	 */
	@MinLengthOf(-1)
	@FieldLengthOf(10)
	public String getAttributeWithNegativeMinLengthIgnoresValidFieldLength() {
		return attributeWithNegativeMinLengthIgnoresValidFieldLength;
	}
	private String attributeWithNegativeMinLengthIgnoresValidFieldLength;
	
	/**
	 * Error conditions: -ve values for min length, don't use any other length
	 * 
	 * @return
	 */
	@MinLengthOf(-1)
	@MaxLengthOf(10)
	public String getAttributeWithNegativeMinLengthIgnoresValidMaxLength() {
		return attributeWithNegativeMinLengthIgnoresValidMaxLength;
	}
	private String attributeWithNegativeMinLengthIgnoresValidMaxLength;
	
	/**
	 * Error conditions: -ve values for max length, can use field length
	 * 
	 * @return
	 */
	@MinLengthOf(5)
	@FieldLengthOf(10)
	@MaxLengthOf(-1)
	public String getAttributeWithNegativeMaxLengthButValidFieldLength() {
		return attributeWithNegativeMaxLengthButValidFieldLength;
	}
	private String attributeWithNegativeMaxLengthButValidFieldLength;

	/**
	 * Operation to check:
	 * Error conditions: -ve values for max length, can use field length
	 * 
	 * @return
	 */
	public void operationToUpdateAttributeWithNegativeMaxLengthButValidFieldLength(
			@Named("attributeWithNegativeMaxLengthButValidFieldLength")
			@MinLengthOf(5)
			@FieldLengthOf(10)
			@MaxLengthOf(-1)
			final String attributeWithNegativeMaxLengthButValidFieldLength) {
		this.attributeWithNegativeMaxLengthButValidFieldLength = attributeWithNegativeMaxLengthButValidFieldLength;
	}

	/**
	 * Error conditions: -ve values
	 * 
	 * @return
	 */
	@MinLengthOf(-1)
	@FieldLengthOf(-1)
	@MaxLengthOf(-1)
	public String getAttributeWithNegativeLengths() {
		return attributeWithNegativeLengths;
	}
	private String attributeWithNegativeLengths;
	
	/**
	 * Operation to check:
	 * Error conditions: -ve values
	 * 
	 * @return
	 */
	public void operationToUpdateAttributeWithNegativeLengths(
			@MinLengthOf(-1)
			@FieldLengthOf(-1)
			@MaxLengthOf(-1)
			@Named("attributeWithNegativeLengths")
			final String attributeWithNegativeLengths) {
		this.attributeWithNegativeLengths = attributeWithNegativeLengths;
	}
	
	/**
	 * Error conditions: 0
	 * 
	 * @return
	 */
	@MinLengthOf(0)
	@FieldLengthOf(0)
	@MaxLengthOf(0)
	public String getAttributeWithZeroLengths() {
		return attributeWithZeroLengths;
	}
	private String attributeWithZeroLengths;
	

	/**
	 * Error conditions: field length > max length
	 * 
	 * @return
	 */
	@FieldLengthOf(30)
	@MaxLengthOf(20)
	public String getAttributeWithFieldLengthLongerThanMaxLength() {
		return attributeWithFieldLengthLongerThanMaxLength;
	}
	private String attributeWithFieldLengthLongerThanMaxLength;


	/**
	 * Error conditions: min length > max length
	 * 
	 * @return
	 */
	@MinLengthOf(40)
	@MaxLengthOf(20)
	public String getAttributeWithMinLengthLongerThanMaxLength() {
		return attributeWithMinLengthLongerThanMaxLength;
	}
	private String attributeWithMinLengthLongerThanMaxLength;

	/**
	 * Error conditions: min length > field length
	 * 
	 * @return
	 */
	@MinLengthOf(40)
	@FieldLengthOf(10)
	public String getAttributeWithMinLengthLongerThanFieldLength() {
		return attributeWithMinLengthLongerThanFieldLength;
	}
	private String attributeWithMinLengthLongerThanFieldLength;


}