package mikespike3.model;

import java.util.Date;

import mikespike3.EasyBeanExample;
import mikespike3.util.RandomUtil;
import de.berlios.rcpviewer.progmodel.standard.DescribedAs;
import de.berlios.rcpviewer.progmodel.standard.InDomain;

@InDomain("default")
@DescribedAs("A Lean Mean Bean Machine")
public class EasyBean {
	
	private Boolean field0 = null;
	private String field1 = null;
	private Date field2 = null;
	private String field3 = null;
	private String field4 = null;
	private Boolean field5 = null;
	private Boolean field6 = null;
	private Date field7 = null;
	private String field8 = null;
	private String field9 = null;
	

	public EasyBean() {
		field0 = RandomUtil.oneHalfLikely();
		field1 = RandomUtil.createString( 0, 10 );
		field2 = RandomUtil.createDate( RandomUtil.DATE_HINT_LAST_YEAR );
		field3 = RandomUtil.createString( 0, 10 );
		field5 = RandomUtil.oneHalfLikely();
		field4 = RandomUtil.createString( 0, 10 );
		field6 = RandomUtil.oneHalfLikely();
		field7 = RandomUtil.createDate( RandomUtil.DATE_HINT_LAST_YEAR );
		field8 = RandomUtil.createString( 0, 10 );
		field9 = RandomUtil.createString( 0, 10 );
	}

	public Boolean getField0() {
		return field0;
	}

	public void setField0(Boolean field0) {
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

	public Boolean getField5() {
		return field5;
	}

	public void setField5(Boolean field5) {
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

	public String getField8() {
		return field8;
	}

	public void setField8(String field8) {
		this.field8 = field8;
	}

	public String getField9() {
		return field9;
	}

	public void setField9(String field9) {
		this.field9 = field9;
	}
	
	
	@Override
	public String toString() {
		return getClass().getName()+":"+hashCode();
	}
	

}
