package de.berlios.rcpviewer.gui.acme.model;

import java.util.Date;

import org.eclipse.swt.graphics.Color;

import de.berlios.rcpviewer.progmodel.extended.Lifecycle;
import de.berlios.rcpviewer.progmodel.extended.Order;
import de.berlios.rcpviewer.progmodel.standard.InDomain;

/**
 * @author Mike
 */
@Lifecycle(instantiable=true,searchable=true,saveable=true)
@InDomain
public class EasyBean {
	
	private String field0 = null;
	private int field1 = 0;
	private String field2 = null;
	private String field3 = null;
	private String field4 = null;
	private Color field5 = null;
	private Date field6 = null;
	private String field7 = null;
	private int field8 = 0;
	private boolean field9 = false;
	
	public String getField0() {
		return field0;
	}

	public void setField0(String field0) {
		this.field0 = field0;
	}

	public int getField1() {
		return field1;
	}

	public void setField1(int field1) {
		this.field1 = field1;
	}

	public String getField2() {
		return field2;
	}

	public void setField2(String field2) {
		this.field2 = field2;
	}
	
	@Order(5)
	public Color getField5() {
		return field5;
	}

	public void setField5(Color field5) {
		this.field5 = field5;
	}

	public String getField4() {
		return field4;
	}

	public void setField4(String field4) {
		this.field4 = field4;
	}

	public String getField3() {
		return field3;
	}

	public void setField3(String field3) {
		this.field3 = field3;
	}

	public Date getField6() {
		return field6;
	}

	public void setField6(Date field6) {
		this.field6 = field6;
	}

	public String getField7() {
		return field7;
	}

	public void setField7(String field7) {
		this.field7 = field7;
	}

	public int getField8() {
		return field8;
	}

	public void setField8(int field8) {
		this.field8 = field8;
	}

	public Boolean getField9() {
		return field9;
	}

	public void setField9(Boolean field9) {
		this.field9 = field9;
	}
	
	

}
