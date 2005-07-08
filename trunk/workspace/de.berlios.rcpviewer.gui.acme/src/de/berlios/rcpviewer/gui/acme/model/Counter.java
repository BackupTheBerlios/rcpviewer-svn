package de.berlios.rcpviewer.gui.acme.model;

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
	
	public void increment( int inc ) {
		count = count + inc;
	}
	
	public void increment( int inc1, int inc2 ) {
		count = count + inc1 + inc2;
	}

}
