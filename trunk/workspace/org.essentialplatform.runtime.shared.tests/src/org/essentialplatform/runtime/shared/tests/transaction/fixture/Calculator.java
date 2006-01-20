package org.essentialplatform.runtime.shared.tests.transaction.fixture;


import org.essentialplatform.progmodel.essential.app.InDomain;

@InDomain
public class Calculator {

	public Calculator() {
		super();
	}
	
	private int result;
	public int getResult() {
		return result;
	}
	public void assignInitialResult(int result) { // not set otherwise gets picked up as a write-only attribute...
		this.result = result;
	}
	
	public void reset() {
		assignInitialResult(0);
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
