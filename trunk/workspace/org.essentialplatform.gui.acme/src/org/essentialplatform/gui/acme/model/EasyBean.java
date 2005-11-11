package org.essentialplatform.gui.acme.model;

import java.util.Date;

import org.eclipse.swt.graphics.Color;

import org.essentialplatform.progmodel.essential.app.FieldLengthOf;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.Lifecycle;
import org.essentialplatform.progmodel.essential.app.MaxLengthOf;
import org.essentialplatform.progmodel.essential.app.Regex;
import org.essentialplatform.progmodel.essential.app.RelativeOrder;

/**
 * @author Mike
 */
@Lifecycle(instantiable=true,searchable=true,saveable=true)
@InDomain
public class EasyBean {
	
	private String string = null;
	public String getString() {
		return string;
	}
	public void setString(String string) {
		this.string = string;
	}


	private String boundedString = null;
	@FieldLengthOf(10)
	@MaxLengthOf(20)
	public String getBoundedString() {
		return boundedString;
	}
	public void setBoundedString(String boundedString) {
		this.boundedString = boundedString;
	}

	
	private String regexString = null;
	/**
	 * In format <tt>nnn-AA</tt> where n is number, A is A-Z.
	 * 
	 * @return
	 */
	@Regex("[0-9]{3}-[A-Z]{2}")
	public String getRegexString() {
		return regexString;
	}
	public void setRegexString(String field0) {
		this.regexString = field0;
	}


	private int field1 = 0;
	public int getField1() {
		return field1;
	}
	public void setField1(int field1) {
		this.field1 = field1;
	}


	private String field2 = null;
	public String getField2() {
		return field2;
	}
	public void setField2(String field2) {
		this.field2 = field2;
	}
	

	private String field3 = null;
	public String getField3() {
		return field3;
	}
	public void setField3(String field3) {
		this.field3 = field3;
	}

	
	private String field4 = null;
	public String getField4() {
		return field4;
	}
	public void setField4(String field4) {
		this.field4 = field4;
	}

	private Color field5 = null;
	@RelativeOrder(5)
	public Color getField5() {
		return field5;
	}
	public void setField5(Color field5) {
		this.field5 = field5;
	}



	private Date field6 = null;
	public Date getField6() {
		return field6;
	}
	public void setField6(Date field6) {
		this.field6 = field6;
	}


	private String field7 = null;
	public String getField7() {
		return field7;
	}
	public void setField7(String field7) {
		this.field7 = field7;
	}


	private int field8 = 0;
	public int getField8() {
		return field8;
	}
	public void setField8(int field8) {
		this.field8 = field8;
	}


	private boolean field9 = false;
	public Boolean getField9() {
		return field9;
	}
	public void setField9(Boolean field9) {
		this.field9 = field9;
	}
	
	

}
