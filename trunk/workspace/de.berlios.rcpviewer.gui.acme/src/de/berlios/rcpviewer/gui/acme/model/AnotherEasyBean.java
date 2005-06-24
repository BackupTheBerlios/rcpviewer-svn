package de.berlios.rcpviewer.gui.acme.model;

import java.util.Date;

import de.berlios.rcpviewer.gui.util.RandomUtil;
import de.berlios.rcpviewer.progmodel.standard.InDomain;

/**
 * 
 * @author Mike
 */
@InDomain
public class AnotherEasyBean {
	
	private String field0 = null;
	private String field1 = null;
	private String field2 = null;
	private Boolean field3 = null;
	private String field4 = null;
	private String field5 = null;
	private Boolean field6 = null;
	private Date field7 = null;
	private String field8 = null;
	private Date field9 = null;
	

	public AnotherEasyBean() {
		field0 = RandomUtil.createString( 0, 10 );
		field1 = RandomUtil.createString( 0, 10 );
		field2 = RandomUtil.createString( 0, 10 );
		field3 = RandomUtil.oneHalfLikely();
		field4 = RandomUtil.createString( 0, 10 );
		field5 = RandomUtil.createString( 0, 10 );
		field6 = RandomUtil.oneHalfLikely();
		field7 = RandomUtil.createDate( RandomUtil.DATE_HINT_LAST_YEAR );
		field8 = RandomUtil.createString( 0, 10 );
		field9 = RandomUtil.createDate( RandomUtil.DATE_HINT_NEXT_YEAR );
	}

	public String getField0() {
		return field0;
	}

	public void setField0(String field0) {
		this.field0 = field0;
	}

	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}

	public String getField2() {
		return field2;
	}

	public void setField2(String field2) {
		this.field2 = field2;
	}

	public Boolean getField3() {
		return field3;
	}

	public void setField3(Boolean field3) {
		this.field3 = field3;
	}

	public String getField4() {
		return field4;
	}

	public void setField4(String field4) {
		this.field4 = field4;
	}

	public String getField5() {
		return field5;
	}

	public void setField5(String field5) {
		this.field5 = field5;
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

	public String getField8() {
		return field8;
	}

	public void setField8(String field8) {
		this.field8 = field8;
	}

	public Date getField9() {
		return field9;
	}

	public void setField9(Date field9) {
		this.field9 = field9;
	}
	
	

}
