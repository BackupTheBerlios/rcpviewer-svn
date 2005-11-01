package org.essentialplatform.gui.acme.model;

import org.essentialplatform.progmodel.extended.Lifecycle;
import org.essentialplatform.progmodel.extended.Named;
import org.essentialplatform.progmodel.standard.DescribedAs;
import org.essentialplatform.progmodel.standard.InDomain;

/**
 * @author Mike
 */
@Lifecycle(instantiable=true,searchable=true,saveable=true)
@InDomain
public class WrappedPrimitiveBean {
	
	private Boolean booleanField = null;
	private Byte byteField = null;
	private Short shortField = null;
	private Character charField = null;
	private Integer intField = null;
	private Long longField = null;
	private Float floatField = null;
	private Double doubleField = null;
	

	/**
	 * @return Returns the booleanField.
	 */
	public Boolean isBooleanField() {
		return booleanField;
	}
	
	/**
	 * @param booleanField The booleanField to set.
	 */
	public void setBooleanField(Boolean booleanField) {
		this.booleanField = booleanField;
	}
	
	/**
	 * @return Returns the byteField.
	 */
	public Byte getByteField() {
		return byteField;
	}
	
	/**
	 * @param byteField The byteField to set.
	 */
	public void setByteField(Byte byteField) {
		this.byteField = byteField;
	}
	
	/**
	 * @return Returns the charField.
	 */
	public Character getCharField() {
		return charField;
	}
	
	/**
	 * @param charField The charField to set.
	 */
	public void setCharField(Character charField) {
		this.charField = charField;
	}
	
	/**
	 * @return Returns the doubleField.
	 */
	public Double getDoubleField() {
		return doubleField;
	}
	
	/**
	 * @param doubleField The doubleField to set.
	 */
	public void setDoubleField(Double doubleField) {
		this.doubleField = doubleField;
	}
	
	/**
	 * @return Returns the floatField.
	 */
	public Float getFloatField() {
		return floatField;
	}
	
	/**
	 * @param floatField The floatField to set.
	 */
	public void setFloatField(Float floatField) {
		this.floatField = floatField;
	}
	
	/**
	 * @return Returns the intField.
	 */
	public Integer getIntField() {
		return intField;
	}
	
	/**
	 * @param intField The intField to set.
	 */
	public void setIntField(Integer intField) {
		this.intField = intField;
	}
	
	/**
	 * @return Returns the longField.
	 */
	public Long getLongField() {
		return longField;
	}
	
	/**
	 * @param longField The longField to set.
	 */
	public void setLongField(Long longField) {
		this.longField = longField;
	}
	
	/**
	 * @return Returns the shortField.
	 */
	public Short getShortField() {
		return shortField;
	}
	
	/**
	 * @param shortField The shortField to set.
	 */
	public void setShortField(Short shortField) {
		this.shortField = shortField;
	}
		
	
	/* ops (actually renamed setters) */
	
	/**
	 * @param booleanField The booleanField to set.
	 */
	public void booleanArgOp(
			@Named("arg") //$NON-NLS-1$
			@DescribedAs("boolean argument") //$NON-NLS-1$
			boolean booleanField) {
		this.booleanField = booleanField;
	}
	
	/**
	 * @param byteField The byteField to set.
	 */
	public void byteArgOp(
			@Named("arg") //$NON-NLS-1$
			@DescribedAs("byte argument") //$NON-NLS-1$
			byte byteField) {
		this.byteField = byteField;
	}
	
	/**
	 * @param charField The charField to set.
	 */
	public void charArgOp(
			@Named("arg") //$NON-NLS-1$
			@DescribedAs("char argument") //$NON-NLS-1$
			char charField) {
		this.charField = charField;
	}

	/**
	 * @param doubleField The doubleField to set.
	 */
	public void doubleArgOp(
			@Named("arg") //$NON-NLS-1$
			@DescribedAs("double argument") //$NON-NLS-1$
			double doubleField) {
		this.doubleField = doubleField;
	}

	/**
	 * @param floatField The floatField to set.
	 */
	public void floatArgOp(
			@Named("arg") //$NON-NLS-1$
			@DescribedAs("float argument") //$NON-NLS-1$
			float floatField) {
		this.floatField = floatField;
	}
	
	/**
	 * @param intField The intField to set.
	 */
	public void intArgOp(
			@Named("arg") //$NON-NLS-1$
			@DescribedAs("int argument") //$NON-NLS-1$
			int intField) {
		this.intField = intField;
	}
	
	/**
	 * @param longField The longField to set.
	 */
	public void longArgOp(
			@Named("arg") //$NON-NLS-1$
			@DescribedAs("long argument") //$NON-NLS-1$
			long longField) {
		this.longField = longField;
	}
	
	/**
	 * @param shortField The shortField to set.
	 */
	public void shortArgOp(
			@Named("arg") //$NON-NLS-1$
			@DescribedAs("short argument") //$NON-NLS-1$
			short shortField) {
		this.shortField = shortField;
	}
}
