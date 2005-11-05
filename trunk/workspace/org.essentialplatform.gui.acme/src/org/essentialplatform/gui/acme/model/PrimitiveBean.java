package org.essentialplatform.gui.acme.model;

import org.essentialplatform.progmodel.essential.app.DescribedAs;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.Lifecycle;
import org.essentialplatform.progmodel.essential.app.Named;

/**
 * @author Mike
 */
@Lifecycle(instantiable=true,searchable=true,saveable=true)
@InDomain
public class PrimitiveBean {
	
	private boolean booleanField;
	private byte byteField;
	private short shortField;
	private char charField;
	private int intField;
	private long longField;
	private float floatField;
	private double doubleField;
	
	/* getters / setters */
	
	/**
	 * @return Returns the booleanField.
	 */
	public boolean isBooleanField() {
		return booleanField;
	}
	
	/**
	 * @param booleanField The booleanField to set.
	 */
	public void setBooleanField(boolean booleanField) {
		this.booleanField = booleanField;
	}
	
	/**
	 * @return Returns the byteField.
	 */
	public byte getByteField() {
		return byteField;
	}
	
	/**
	 * @param byteField The byteField to set.
	 */
	public void setByteField(byte byteField) {
		this.byteField = byteField;
	}
	
	/**
	 * @return Returns the charField.
	 */
	public char getCharField() {
		return charField;
	}
	
	/**
	 * @param charField The charField to set.
	 */
	public void setCharField(char charField) {
		this.charField = charField;
	}
	
	/**
	 * @return Returns the doubleField.
	 */
	public double getDoubleField() {
		return doubleField;
	}
	
	/**
	 * @param doubleField The doubleField to set.
	 */
	public void setDoubleField(double doubleField) {
		this.doubleField = doubleField;
	}
	
	/**
	 * @return Returns the floatField.
	 */
	public float getFloatField() {
		return floatField;
	}
	
	/**
	 * @param floatField The floatField to set.
	 */
	public void setFloatField(float floatField) {
		this.floatField = floatField;
	}
	
	/**
	 * @return Returns the intField.
	 */
	public int getIntField() {
		return intField;
	}
	
	/**
	 * @param intField The intField to set.
	 */
	public void setIntField(int intField) {
		this.intField = intField;
	}
	
	/**
	 * @return Returns the longField.
	 */
	public long getLongField() {
		return longField;
	}
	
	/**
	 * @param longField The longField to set.
	 */
	public void setLongField(long longField) {
		this.longField = longField;
	}
	
	/**
	 * @return Returns the shortField.
	 */
	public short getShortField() {
		return shortField;
	}
	
	/**
	 * @param shortField The shortField to set.
	 */
	public void setShortField(short shortField) {
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
