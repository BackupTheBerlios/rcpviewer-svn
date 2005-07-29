package de.berlios.rcpviewer.gui.acme.model;

import de.berlios.rcpviewer.progmodel.extended.Lifecycle;
import de.berlios.rcpviewer.progmodel.standard.InDomain;

/**
 * @author Mike
 */
@Lifecycle(instantiable=true,searchable=true,saveable=true)
@InDomain
public class PrimitiveArrayBean {
	
	private boolean[] booleanField;
	private byte[] byteField;
	private short[] shortField;
	private char[] charField;
	private int[] intField;
	private long[] longField;
	private float[] floatField;
	private double[] doubleField;
	
	
	/**
	 * Both standard variants for boolean getter.
	 * @return Returns the booleanField.
	 */
	public boolean[] isBooleanField() {
		return booleanField;
	}
	
	/**
	 * Both standard variants for boolean getter.
	 * @return Returns the booleanField.
	 */
	public boolean[] getBooleanField() {
		return booleanField;
	}
	
	/**
	 * @param booleanField The booleanField to set.
	 */
	public void setBooleanField(boolean[] booleanField) {
		this.booleanField = booleanField;
	}
	
	/**
	 * @return Returns the byteField.
	 */
	public byte[] getByteField() {
		return byteField;
	}
	
	/**
	 * @param byteField The byteField to set.
	 */
	public void setByteField(byte[] byteField) {
		this.byteField = byteField;
	}
	
	/**
	 * @return Returns the charField.
	 */
	public char[] getCharField() {
		return charField;
	}
	
	/**
	 * @param charField The charField to set.
	 */
	public void setCharField(char[] charField) {
		this.charField = charField;
	}
	
	/**
	 * @return Returns the doubleField.
	 */
	public double[] getDoubleField() {
		return doubleField;
	}
	
	/**
	 * @param doubleField The doubleField to set.
	 */
	public void setDoubleField(double[] doubleField) {
		this.doubleField = doubleField;
	}
	
	/**
	 * @return Returns the floatField.
	 */
	public float[] getFloatField() {
		return floatField;
	}
	
	/**
	 * @param floatField The floatField to set.
	 */
	public void setFloatField(float[] floatField) {
		this.floatField = floatField;
	}
	
	/**
	 * @return Returns the intField.
	 */
	public int[] getIntField() {
		return intField;
	}
	
	/**
	 * @param intField The intField to set.
	 */
	public void setIntField(int[] intField) {
		this.intField = intField;
	}
	
	/**
	 * @return Returns the longField.
	 */
	public long[] getLongField() {
		return longField;
	}
	
	/**
	 * @param longField The longField to set.
	 */
	public void setLongField(long[] longField) {
		this.longField = longField;
	}
	
	/**
	 * @return Returns the shortField.
	 */
	public short[] getShortField() {
		return shortField;
	}
	
	/**
	 * @param shortField The shortField to set.
	 */
	public void setShortField(short[] shortField) {
		this.shortField = shortField;
	}
		

	
	

	

}
