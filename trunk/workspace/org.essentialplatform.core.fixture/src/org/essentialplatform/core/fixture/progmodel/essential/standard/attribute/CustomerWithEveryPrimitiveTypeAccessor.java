/**
 * 
 */
package org.essentialplatform.core.fixture.progmodel.essential.standard.attribute;
import org.essentialplatform.progmodel.essential.app.InDomain;

@InDomain
public class CustomerWithEveryPrimitiveTypeAccessor {
	byte aByte;
	public byte getAByte() {
		return aByte;
	}
	short aShort;
	public short getAShort() {
		return aShort;
	}
	int anInt;
	public int getAnInt() {
		return anInt;
	}
	long aLong;
	public long getALong() {
		return aLong;
	}
	char aChar;
	public char getAChar() {
		return aChar;
	}
	float aFloat;
	public float getAFloat() {
		return aFloat;
	}
	double aDouble;
	public double getADouble() {
		return aDouble;
	}
	boolean aBoolean;
	public boolean isABoolean() {
		return aBoolean;
	}
}