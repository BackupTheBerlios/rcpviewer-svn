package de.berlios.rcpviewer.gui.acme.model;

import de.berlios.rcpviewer.progmodel.extended.Lifecycle;
import de.berlios.rcpviewer.progmodel.standard.InDomain;

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
	 * Both standard variants for boolean getter.
	 * @return Returns the booleanField.
	 */
	public Boolean isBooleanField() {
		return booleanField;
	}
	
	/**
	 * Both standard variants for boolean getter.
	 * @return Returns the booleanField.
	 */
	public Boolean getBooleanField() {
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
		

	
	

	

}
