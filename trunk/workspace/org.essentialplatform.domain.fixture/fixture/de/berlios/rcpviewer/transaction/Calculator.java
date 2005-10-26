package de.berlios.rcpviewer.transaction;


import de.berlios.rcpviewer.progmodel.standard.InDomain;

@InDomain
public class Calculator {

	public Calculator() {
		super();
	}
	
	private int result;
	public int getResult() {
		return result;
	}
	public void setInitialResult(int result) {
		this.result = result;
	}
	
	public void reset() {
		setInitialResult(0);
	}
	
	public void add(final int amount) {
		result += amount;
	}

	public void computeFactorial(final int n) {
		result = factorial(n);
	}
	
	public int factorial(final int n) {
		if (n==1) { return 1; }
		return n * factorial(n-1);
	}
	
	public void noop() {
	}
	

	/**
	 * Indicates to platform that this object can be deleted.
	 * 
	 * <p>
	 * There is no need for any actual implementation.
	 *
	 */
	public void delete() {
	}

}
