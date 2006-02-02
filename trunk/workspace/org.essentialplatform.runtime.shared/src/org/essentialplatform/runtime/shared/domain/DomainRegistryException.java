package org.essentialplatform.runtime.shared.domain;

public class DomainRegistryException extends RuntimeException {

	public DomainRegistryException() {
		super();
	}

	public DomainRegistryException(String message) {
		super(message);
	}

	public DomainRegistryException(String message, Throwable cause) {
		super(message, cause);
	}

	public DomainRegistryException(Throwable cause) {
		super(cause);
	}

}
