package de.berlios.rcpviewer.gui.acme.model;

import de.berlios.rcpviewer.progmodel.extended.Named;
import de.berlios.rcpviewer.progmodel.standard.DescribedAs;
import de.berlios.rcpviewer.progmodel.standard.InDomain;

@InDomain
public class Counter {
	
	int count = 0;
	
	public int getCount() {
		return count;
	}
	
	public void increment() {
		count++;
	}
	
	public void increment( 
			@Named("inc") //$NON-NLS-1$
			@DescribedAs("How much to add to the counter.") //$NON-NLS-1$
			int inc ) {
		count = count + inc;
	}
	
	public void increment( 
			@Named("firstInc") //$NON-NLS-1$
			@DescribedAs("The first amount to add to the counter.") //$NON-NLS-1$
			int firstInc, 
			@Named("secondInc") //$NON-NLS-1$
			@DescribedAs("The second amount to add to the counter.") //$NON-NLS-1$
			int secondInc ) {
		count = count + firstInc + secondInc;
	}
	
	public void increment( 
			@Named("bean1") //$NON-NLS-1$
			@DescribedAs("Adds the value of field0 of an EasyBean instance to the counter.") //$NON-NLS-1$
			EasyBean bean1,
			@Named("bean2") //$NON-NLS-1$
			@DescribedAs("Adds the value of field0 of an AnotherEasyBean instance to the counter.") //$NON-NLS-1$
			AnotherEasyBean bean2 ) {
		count = count + bean1.getField0() + bean2.getField0();
	}
	


}
