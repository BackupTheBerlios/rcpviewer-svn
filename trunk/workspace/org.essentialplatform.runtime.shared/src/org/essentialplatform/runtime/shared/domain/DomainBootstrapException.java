package org.essentialplatform.runtime.shared.domain;

public class DomainBootstrapException extends RuntimeException {

	public DomainBootstrapException() {
		super();
	}

	public DomainBootstrapException(String message) {
		super(message);
	}

	public DomainBootstrapException(String message, Throwable cause) {
		super(message, cause);
	}

	public DomainBootstrapException(Throwable cause) {
		super(cause);
	}

}
