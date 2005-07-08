package de.berlios.rcpviewer.gui.acme.model;

import java.util.Date;

import de.berlios.rcpviewer.gui.util.RandomUtil;
import de.berlios.rcpviewer.progmodel.extended.Lifecycle;
import de.berlios.rcpviewer.progmodel.standard.InDomain;

/**
 * @author Mike
 */
@Lifecycle(instantiable=true,searchable=true,saveable=true)
@InDomain
public class EasyBean {
	
	private int field0 = 0;
	private String field1 = null;
	private Date field2 = null;
	private String field3 = null;
	private String field4 = null;
	private int field5 = 0;
	private Boolean field6 = null;
	private Date field7 = null;
	private int field8 = 0;
	private String field9 = null;
	

	public EasyBean() {
		field0 = RandomUtil.createInt( 0, 5000 );
		field1 = RandomUtil.createString( 0, 10 );
		field2 = RandomUtil.createDate( RandomUtil.DATE_HINT_LAST_YEAR );
		field3 = RandomUtil.createString( 0, 10 );
		field5 = RandomUtil.createInt( 0, 5000 );
		field4 = RandomUtil.createString( 0, 10 );
		field6 = RandomUtil.oneHalfLikely();
		field7 = RandomUtil.createDate( RandomUtil.DATE_HINT_LAST_YEAR );
		field8 = RandomUtil.createInt( 0, 5000 );
		field9 = RandomUtil.createString( 0, 10 );
	}

	public int getField0() {
		return field0;
	}

	public void setField0(int field0) {
		this.field0 = field0;
	}

	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}

	public Date getField2() {
		return field2;
	}

	public void setField2(Date field2) {
		this.field2 = field2;
	}

	public int getField5() {
		return field5;
	}

	public void setField5(int field5) {
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

	public Boolean getField6() {
		return field6;
	}

	public void setField6(Boolean field6) {
		this.field6 = field6;
	}

	public Date getField7() {
		return field7;
	}

	public void setField7(Date field7) {
		this.field7 = field7;
	}

	public int getField8() {
		return field8;
	}

	public void setField8(int field8) {
		this.field8 = field8;
	}

	public String getField9() {
		return field9;
	}

	public void setField9(String field9) {
		this.field9 = field9;
	}
	
	

}
