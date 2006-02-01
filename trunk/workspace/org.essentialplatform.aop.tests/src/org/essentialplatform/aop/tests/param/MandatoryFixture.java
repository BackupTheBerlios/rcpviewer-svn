package org.essentialplatform.aop.tests.param;

import org.essentialplatform.aop.param.Mandatory;
import org.essentialplatform.aop.param.WithPreconditions;

public class MandatoryFixture {

	public MandatoryFixture() {
	}

	@WithPreconditions
	public MandatoryFixture(@Mandatory String param) {
	}

	@WithPreconditions
	public void withPreconditionsAndMandatoryParam0(@Mandatory String param) {
	}
	
	@WithPreconditions
	public void withPreconditionsButNonMandatoryParam0(String param) {
	}
	
	public void withoutPreconditionsButWithMandatoryParam0(@Mandatory String param) {
	}


	@WithPreconditions
	public MandatoryFixture(String a, @Mandatory String param) {
	}

	@WithPreconditions
	public void withPreconditionsAndMandatoryParam1(String a, @Mandatory String param) {
	}
	
	@WithPreconditions
	public void withPreconditionsButNonMandatoryParam1(String a, String param) {
	}
	
	public void withoutPreconditionsButWithMandatoryParam1(String a, @Mandatory String param) {
	}


	// forgot @WithPreconditions
	public MandatoryFixture(String a, String b, @Mandatory String c) {
	}

	// non-mandatory
	@WithPreconditions
	public MandatoryFixture(String a, String b, String c, String d) {
	}


	
	
	@WithPreconditions
	public MandatoryFixture(String a, String b, String c, String d, String e, String f, String g, @Mandatory String param) {
	}

	@WithPreconditions
	public void withPreconditionsAndMandatoryParam7(String a, String b, String c, String d, String e, String f, String g, @Mandatory String param) {
	}
	
	@WithPreconditions
	public void withPreconditionsButNonMandatoryParam7(String a, String b, String c, String d, String e, String f, String g, String param) {
	}
	
	public void withoutPreconditionsButWithMandatoryParam7(String a, String b, String c, String d, String e, String f, String g, @Mandatory String param) {
	}


	@WithPreconditions
	public MandatoryFixture(String a, String b, String c, String d, String e, String f, String g, String h, @Mandatory String param) {
	}

	@WithPreconditions
	public void withPreconditionsAndMandatoryParam8(String a, String b, String c, String d, String e, String f, String g, String h, @Mandatory String param) {
	}
	
	@WithPreconditions
	public void withPreconditionsButNonMandatoryParam8(String a, String b, String c, String d, String e, String f, String g, String h, String param) {
	}
	
	public void withoutPreconditionsButWithMandatoryParam8(String a, String b, String c, String d, String e, String f, String g, String h, @Mandatory String param) {
	}

	
}
