package de.berlios.rcpviewer.transaction;


import de.berlios.rcpviewer.progmodel.standard.InDomain;

@InDomain
public class Calculator {

	public Calculator() {
		super();
	}
	
	private int _result;
	public int getResult() {
		return _result;
	}
	public void setInitialResult(int result) {
		_result = result;
	}
	
	public void reset() {
		setInitialResult(0);
	}
	
	public void add(final int amount) {
		_result += amount;
	}

	public void computeFactorial(final int n) {
		_result = factorial(n);
	}
	
	public int factorial(final int n) {
		if (n==1) { return 1; }
		return n * factorial(n-1);
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
