package org.essentialplatform.progmodel.standard.operation;

import org.essentialplatform.progmodel.extended.IPrerequisites;
import org.essentialplatform.progmodel.extended.Prerequisites;
import org.essentialplatform.progmodel.standard.InDomain;

@InDomain
public class CustomerOperationWithPreAndArgs {


	public int computeDifference(int a, int b) {
		return a-b;
	}
	
	public IPrerequisites computeDifferencePre(int a, int b) {
		return Prerequisites
		          .require(a>0, "First argument must be positive")
		       .andRequire(b>0, "Second argument must be positive")
		       .andRequire(a>b, "First argument must be > second argument");
	}

}
